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

package io.github.lxgaming.sledgehammer.mixin.immersiveengineering.client.render;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.client.render.TileRenderWorkbench;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityModWorkbench;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = TileRenderWorkbench.class, priority = 1337, remap = false)
public abstract class TileRenderWorkbenchMixin {
    
    private int sledgehammer$expectedValue = 0;
    
    @Inject(method = "render", at = @At(value = "HEAD"))
    private void onRender(CallbackInfo callbackInfo) {
        this.sledgehammer$expectedValue = 0;
    }
    
    @Inject(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lblusunrize/immersiveengineering/client/render/TileRenderAutoWorkbench;getBlueprintDrawable(Lblusunrize/immersiveengineering/api/crafting/BlueprintCraftingRecipe;Lnet/minecraft/world/World;)Lblusunrize/immersiveengineering/client/render/TileRenderAutoWorkbench$BlueprintLines;"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void onRender(TileEntityModWorkbench te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo callbackInfo,
                          EnumFacing facing, float angle, ItemStack stack, boolean showIngredients, double playerDistanceSq, BlueprintCraftingRecipe[] recipes,
                          float lineWidth, int l, int perRow, float scale, int i, BlueprintCraftingRecipe recipe) {
        if (this.sledgehammer$expectedValue == i) {
            this.sledgehammer$expectedValue++;
        } else {
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
            callbackInfo.cancel();
        }
    }
}