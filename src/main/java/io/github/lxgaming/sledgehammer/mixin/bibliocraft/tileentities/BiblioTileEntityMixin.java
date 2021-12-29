/*
 * Copyright 2021 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.bibliocraft.tileentities;

import jds.bibliocraft.tileentities.BiblioTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;

@Mixin(value = BiblioTileEntity.class, remap = false)
public abstract class BiblioTileEntityMixin implements IItemHandler {
    
    @Shadow
    public abstract ItemStack func_70301_a(int slot);
    
    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return func_70301_a(slot);
    }
}