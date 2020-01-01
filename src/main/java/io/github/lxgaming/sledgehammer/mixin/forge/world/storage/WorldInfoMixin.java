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

package io.github.lxgaming.sledgehammer.mixin.forge.world.storage;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import io.github.lxgaming.sledgehammer.launch.SledgehammerLaunch;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldInfo.class, priority = 1337)
public abstract class WorldInfoMixin {
    
    @Inject(
            method = "setRaining",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onSetRaining(boolean isRaining, CallbackInfo callbackInfo) {
        if (SledgehammerPlatform.getInstance().isLoaded("totemic") && SledgehammerLaunch.isClassPresentInStackTrace("pokefenn.totemic.ceremony.CeremonyRain")) {
            callbackInfo.cancel();
            Sledgehammer.getInstance().debug("CeremonyRain from Totemic was blocked");
        }
    }
    
    @Inject(
            method = "setRainTime",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onSetRainTime(int time, CallbackInfo callbackInfo) {
        if (SledgehammerPlatform.getInstance().isLoaded("totemic") && SledgehammerLaunch.isClassPresentInStackTrace("pokefenn.totemic.ceremony.CeremonyRain")) {
            callbackInfo.cancel();
            Sledgehammer.getInstance().debug("CeremonyRain from Totemic was blocked");
        }
    }
}