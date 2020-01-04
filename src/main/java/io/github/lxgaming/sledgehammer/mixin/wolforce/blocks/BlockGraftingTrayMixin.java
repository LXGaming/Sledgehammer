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

package io.github.lxgaming.sledgehammer.mixin.wolforce.blocks;

import com.google.common.util.concurrent.ListenableFutureTask;
import io.github.lxgaming.sledgehammer.launch.SledgehammerLaunch;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import wolforce.Main;
import wolforce.blocks.BlockGraftingTray;
import wolforce.blocks.tile.TileGraftingTray;

@Mixin(value = BlockGraftingTray.class, priority = 1337, remap = false)
public abstract class BlockGraftingTrayMixin {
    
    @Shadow
    @Final
    public static PropertyBool FILLED;
    
    /**
     * @author LX_Gaming
     * @reason The BulkBlockCapture in Sponge's PhaseTracker causes the call setBlockState to get delayed,
     * This delay causes the call getTileEntity to return inconsistent results.
     * <p>
     * The fix implemented is to check if Sponge is present and to re-schedule the logic if need be.
     */
    @Overwrite
    public static void changeFilled(World world, BlockPos blockPos, boolean filled) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (!(tileEntity instanceof TileGraftingTray)) {
            return;
        }
        
        ItemStackHandler inventory = ((TileGraftingTray) tileEntity).inventory;
        world.setBlockState(blockPos, Main.grafting_tray.getDefaultState().withProperty(FILLED, filled));
        
        MinecraftServer server = world.getMinecraftServer();
        if (server != null && SledgehammerLaunch.isSpongeInitialized()) {
            // Runs the logic at the start of the next tick.
            // addScheduledTask won't work as it executes the task immediately if it's scheduled while on the Main Thread.
            server.futureTaskQueue.add(ListenableFutureTask.create(() -> sledgehammer$applyInventory(world, blockPos, inventory), null));
        } else {
            sledgehammer$applyInventory(world, blockPos, inventory);
        }
    }
    
    private static void sledgehammer$applyInventory(World world, BlockPos blockPos, ItemStackHandler inventory) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileGraftingTray) {
            ((TileGraftingTray) tileEntity).inventory = inventory;
        }
    }
}