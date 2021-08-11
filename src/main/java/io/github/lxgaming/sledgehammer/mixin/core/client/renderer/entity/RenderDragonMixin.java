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
import net.minecraft.client.renderer.entity.RenderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderDragon.class)
public class RenderDragonMixin {
    /**
     * Use regular lighting enable/disable instead of the item one. It is unclear why Mojang used the item one.
     */
    @Redirect(
            method = "renderCrystalBeams",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderHelper;disableStandardItemLighting()V"
            )
    )
    private static void disableLighting() {
        GlStateManager.disableLighting();
    }

    /**
     * Use regular lighting enable/disable instead of the item one. It is unclear why Mojang used the item one.
     */
    @Redirect(
            method = "renderCrystalBeams",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderHelper;enableStandardItemLighting()V"
            )
    )
    private static void enableLighting() {
        GlStateManager.enableLighting();
    }
}
