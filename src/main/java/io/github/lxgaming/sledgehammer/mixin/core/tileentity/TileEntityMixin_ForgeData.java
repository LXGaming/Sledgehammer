/*
 * Copyright 2020 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.core.tileentity;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileEntity.class)
public abstract class TileEntityMixin_ForgeData {
    
    @Shadow(remap = false)
    private NBTTagCompound customTileData;
    
    @Inject(
            method = "writeToNBT",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onWriteToNBT(NBTTagCompound compound, CallbackInfoReturnable<NBTTagCompound> callbackInfoReturnable) {
        if (this.customTileData != null && this.customTileData == compound) {
            Sledgehammer.getInstance().debug("An attempt to write CustomTileData has been made, This is not allowed.", new IllegalArgumentException("CustomTileData"));
            callbackInfoReturnable.setReturnValue(compound);
        }
    }
}