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

package io.github.lxgaming.sledgehammer.mixin.champions.common.affix;

import c4.champions.common.affix.AffixEvents;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AffixEvents.class, remap = false)
public abstract class AffixEventsMixin {
    
    @Inject(
            method = "onLivingDamaged",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onLivingDamaged(LivingDamageEvent event, CallbackInfo callbackInfo) {
        if (event.getEntityLiving() == event.getSource().getTrueSource()) {
            Sledgehammer.getInstance().debug("Champion attempted to damaged itself");
            callbackInfo.cancel();
        }
    }
}