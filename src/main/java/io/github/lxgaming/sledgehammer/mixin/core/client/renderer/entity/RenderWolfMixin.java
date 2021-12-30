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

package io.github.lxgaming.sledgehammer.mixin.core.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.passive.EntityWolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderWolf.class)
public abstract class RenderWolfMixin {

    @Redirect(
            method = "doRender(Lnet/minecraft/entity/passive/EntityWolf;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/passive/EntityWolf;getBrightness()F"
            )
    )
    private float getRealBrightness(EntityWolf entityWolf) {
        /*
         * The vanilla shading effect does not seem to be perceptible if the entity is rendered at full brightness
         * (1.0f), even though that is technically the correct value.
         * For that reason, the wolf is rendered at 75% brightness when in water instead. This mimics vanilla behavior
         * without the wolf turning completely black (MC-41825, MC-105248).
         */
        return 0.75f;
    }
}
