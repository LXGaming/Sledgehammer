/*
 * Copyright 2022 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.abyssalcraft.common.inventory;

import com.shinoow.abyssalcraft.api.recipe.MaterializerRecipes;
import com.shinoow.abyssalcraft.common.blocks.tile.TileEntityMaterializer;
import com.shinoow.abyssalcraft.common.inventory.InventoryMaterializer;
import com.shinoow.abyssalcraft.common.network.PacketDispatcher;
import com.shinoow.abyssalcraft.common.network.server.TransferStackMessage;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = InventoryMaterializer.class, remap = false)
public abstract class InventoryMaterializerMixin implements IInventory {
    
    @Shadow
    @Final
    private NonNullList<ItemStack> inventoryContents;
    
    @Shadow
    @Final
    private TileEntityMaterializer tile;
    
    /**
     * @author LX_Gaming
     * @reason https://github.com/Shinoow/AbyssalCraft/issues/491
     */
    @Overwrite
    public ItemStack func_70298_a(int index, int count) {
        ItemStack itemStack = ItemStackHelper.getAndSplit(this.inventoryContents, index, count);
        if (!itemStack.isEmpty()) {
            this.markDirty();
            if (this.tile.getWorld().isRemote) {
                PacketDispatcher.sendToServer(new TransferStackMessage(index, itemStack));
            }
            
            MaterializerRecipes.instance().processMaterialization(itemStack, this.tile.func_70301_a(0));
            this.tile.isDirty = true;
        }
        
        return itemStack;
    }
}