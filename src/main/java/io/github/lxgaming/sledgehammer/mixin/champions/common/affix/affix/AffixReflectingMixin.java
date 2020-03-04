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

package io.github.lxgaming.sledgehammer.mixin.champions.common.affix.affix;

import c4.champions.common.affix.affix.AffixReflecting;
import c4.champions.common.capability.IChampionship;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AffixReflecting.class, remap = false)
public abstract class AffixReflectingMixin {
    
    @Inject(
            method = "onDamaged",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onDamaged(EntityLiving entity, IChampionship championship, DamageSource source, float amount, float newAmount, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (source.getDamageType().equalsIgnoreCase("reflecting")) {
            Sledgehammer.getInstance().debug("Reflecting attempted to reflect Reflecting");
            callbackInfoReturnable.cancel();
        }
    }
}