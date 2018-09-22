/*
 * Copyright 2018 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.spongeforge.mod.item.inventory.fabric;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.mod.item.inventory.fabric.IItemHandlerFabric;

@Mixin(value = IItemHandlerFabric.class, priority = 1337, remap = false)
public abstract class MixinIItemHandlerFabric {
    
    @Inject(method = "setIItemHandlerStack", at = @At(value = "HEAD"), cancellable = true)
    private static void onSetIItemHandlerStack(IItemHandler handler, int index, ItemStack stack, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
        Sledgehammer.getInstance().debugMessage("IItemHandlerFabric::setIItemHandlerStack Cancelled");
    }
}