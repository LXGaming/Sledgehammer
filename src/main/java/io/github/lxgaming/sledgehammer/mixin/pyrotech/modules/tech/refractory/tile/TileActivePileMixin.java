/*
 * Copyright 2019 Alex Thomson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.lxgaming.sledgehammer.mixin.pyrotech.modules.tech.refractory.tile;

import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileBurnableBase;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.TileActivePile;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.TilePitAsh;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFutureTask;
import io.github.lxgaming.sledgehammer.launch.SledgehammerLaunch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = TileActivePile.class, priority = 1337, remap = false)
public abstract class TileActivePileMixin extends TileBurnableBase {
    
    @Shadow
    private ItemStackHandler output;
    
    public TileActivePileMixin(ITileDataService tileDataService) {
        super(tileDataService);
    }
    
    /*
    // For debugging purposes
    @Inject(method = "getTotalBurnTimeTicks", at = @At(value = "RETURN"), cancellable = true)
    private void onGetTotalBurnTimeTicks(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        int totalBurnTimeTicks = 200; // 1 second
        Sledgehammer.getInstance().getLogger().info("Speeding up burn time from {} -> {} seconds", callbackInfoReturnable.getReturnValue() / 200, totalBurnTimeTicks / 200);
        callbackInfoReturnable.setReturnValue(totalBurnTimeTicks);
    }
    */
    
    /**
     * @author LX_Gaming
     * @reason The BulkBlockCapture in Sponge's PhaseTracker causes the call setBlockState to get delayed,
     * This delay causes the call getTileEntity to return inconsistent results.
     * <p>
     * The fix implemented is to check if Sponge is present and to re-schedule the logic if need be.
     */
    @Overwrite
    protected void onAllBurnStagesComplete() {
        List<ItemStack> itemStacks = Lists.newArrayListWithCapacity(this.output.getSlots());
        for (int index = 0; index < this.output.getSlots(); index++) {
            ItemStack itemStack = this.output.getStackInSlot(index);
            this.output.setStackInSlot(index, ItemStack.EMPTY);
            if (!itemStack.isEmpty()) {
                itemStacks.add(itemStack);
            }
        }
        
        IBlockState blockState = ModuleTechRefractory.Blocks.PIT_ASH_BLOCK.getDefaultState();
        this.world.setBlockState(this.pos, blockState);
        
        // Sledgehammer Start
        MinecraftServer server = this.world.getMinecraftServer();
        if (server != null && SledgehammerLaunch.isSpongeRegistered()) {
            // Runs the logic at the start of the next tick.
            // addScheduledTask won't work as it executes the task immediately if it's scheduled while on the Main Thread.
            server.futureTaskQueue.add(ListenableFutureTask.create(() -> sledgehammer$applyItemStacks(this.pos, itemStacks), null));
        } else {
            sledgehammer$applyItemStacks(this.pos, itemStacks);
        }
        
        // Sledgehammer End
    }
    
    private void sledgehammer$applyItemStacks(BlockPos blockPos, List<ItemStack> itemStacks) {
        // Short circuit
        if (itemStacks.isEmpty()) {
            return;
        }
        
        TileEntity tileEntity = this.world.getTileEntity(blockPos);
        if (tileEntity instanceof TilePitAsh) {
            TilePitAsh tilePitAsh = (TilePitAsh) tileEntity;
            
            // Ensure the TilePitAsh is empty to prevent item duplication
            for (int index = 0; index < this.output.getSlots(); index++) {
                this.output.setStackInSlot(index, ItemStack.EMPTY);
            }
            
            for (ItemStack itemStack : itemStacks) {
                tilePitAsh.insertItem(itemStack);
            }
        }
    }
}