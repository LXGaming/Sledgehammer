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

package io.github.lxgaming.sledgehammer.mixin.qmd;

import lach_01298.qmd.TickItemHandler;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TickItemHandler.class, remap = false)
public abstract class TickItemHandlerMixin {
    
    @Inject(
            method = "getTileInventory",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private static void onGetTileInventory(ICapabilityProvider provider, EnumFacing side, CallbackInfoReturnable<IItemHandler> callbackInfoReturnable) {
        if (side == null) {
            callbackInfoReturnable.setReturnValue(null);
        }
    }
}