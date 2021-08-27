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

package io.github.lxgaming.sledgehammer.mixin.core.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = RenderGlobal.class)
public abstract class RenderGlobalMixin_VoidBox {
    @Shadow private WorldClient world;

    @Shadow private boolean vboEnabled;

    @Shadow private VertexBuffer sky2VBO;

    @Shadow private int glSkyList2;

    @Redirect(method = "renderSky(FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;getPositionEyes(F)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d getFakePosition(EntityPlayerSP entityPlayerSP, float partialTicks) {
        double offset = entityPlayerSP.getPositionEyes(partialTicks).y - world.getHorizon();
        if(offset < 0.0d) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 12.0F, 0.0F);

            if (vboEnabled)
            {
                sky2VBO.bindBuffer();
                GlStateManager.glEnableClientState(32884);
                GlStateManager.glVertexPointer(3, 5126, 12, 0);
                sky2VBO.drawArrays(7);
                sky2VBO.unbindBuffer();
                GlStateManager.glDisableClientState(32884);
            }
            else
            {
                GlStateManager.callList(glSkyList2);
            }

            GlStateManager.popMatrix();
        }
        return new Vec3d(0, Double.POSITIVE_INFINITY, 0);
    }
}