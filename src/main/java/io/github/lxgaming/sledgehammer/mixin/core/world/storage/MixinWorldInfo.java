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

package io.github.lxgaming.sledgehammer.mixin.core.world.storage;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldInfo.class, priority = 1337)
public abstract class MixinWorldInfo {
    
    @Inject(method = "setRaining", at = @At(value = "HEAD"), cancellable = true)
    private void onSetRaining(boolean isRaining, CallbackInfo callbackInfo) {
        if (Sledgehammer.getInstance().getConfig().map(Config::getMixinCategory).map(MixinCategory::isCeremonyRain).orElse(false)) {
            if (Toolbox.isClassStackFrame("pokefenn.totemic.ceremony.CeremonyRain", Thread.currentThread().getStackTrace())) {
                callbackInfo.cancel();
                Sledgehammer.getInstance().debugMessage("CeremonyRain from Totemic was blocked");
            }
        }
    }
    
    @Inject(method = "setRainTime", at = @At(value = "HEAD"), cancellable = true)
    private void onSetRainTime(int time, CallbackInfo callbackInfo) {
        if (Sledgehammer.getInstance().getConfig().map(Config::getMixinCategory).map(MixinCategory::isCeremonyRain).orElse(false)) {
            if (Toolbox.isClassStackFrame("pokefenn.totemic.ceremony.CeremonyRain", Thread.currentThread().getStackTrace())) {
                callbackInfo.cancel();
                Sledgehammer.getInstance().debugMessage("CeremonyRain from Totemic was blocked");
            }
        }
    }
}