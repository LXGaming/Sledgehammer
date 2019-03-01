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

package io.github.lxgaming.sledgehammer.mixin.quark.base.module;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.tweaks.feature.ImprovedSleeping;

@Mixin(value = ModuleLoader.class, priority = 1337)
public abstract class MixinModuleLoader {
    
    @Inject(method = "isFeatureEnabled", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void onIsFeatureEnabled(Class<? extends Feature> featureClass, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (featureClass == ImprovedSleeping.class) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
}