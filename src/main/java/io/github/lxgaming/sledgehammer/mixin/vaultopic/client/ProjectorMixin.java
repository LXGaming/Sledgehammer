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

package io.github.lxgaming.sledgehammer.mixin.vaultopic.client;

import io.github.lxgaming.sledgehammer.util.Reference;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wolforce.vaultopic.Vaultopic;
import wolforce.vaultopic.client.Projector;

import java.lang.reflect.Method;

@Mixin(value = Projector.class, priority = 1337, remap = false)
public abstract class ProjectorMixin {
    
    @Shadow
    private static Method setupCameraTransform;
    
    private static boolean sledgehammer$state = false;
    
    @Inject(method = "loadMatrixes", at = @At(value = "HEAD"))
    private static void onLoadMatrixes(float partialTicks, CallbackInfo callbackInfo) {
        try {
            if (setupCameraTransform == null && !sledgehammer$state) {
                setupCameraTransform = EntityRenderer.class.getDeclaredMethod("func_78479_a", Float.TYPE, Integer.TYPE);
                setupCameraTransform.setAccessible(true);
                sledgehammer$state = true;
                Vaultopic.logger.info("{} fixed setupCameraTransform", Reference.NAME);
            }
        } catch (Exception ex) {
            Vaultopic.logger.info("{} failed to fix setupCameraTransform", Reference.NAME);
        }
    }
}