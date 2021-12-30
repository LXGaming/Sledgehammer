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

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderGuardian;
import net.minecraft.entity.monster.EntityGuardian;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGuardian.class)
public abstract class RenderGuardianMixin {

    @Inject(method = "doRender(Lnet/minecraft/entity/monster/EntityGuardian;DDDFF)V", at = @At("RETURN"))
    public void resetGlState(EntityGuardian entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if(entity.getTargetedEntity() != null) {
            GlStateManager.enableLighting();
            GlStateManager.enableCull();
        }
    }
}
