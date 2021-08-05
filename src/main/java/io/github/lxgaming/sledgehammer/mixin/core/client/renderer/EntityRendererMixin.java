package io.github.lxgaming.sledgehammer.mixin.core.client.renderer;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Redirect( method = "updateCameraAndRender", at = @At(value="FIELD", target = "Lnet/minecraft/client/settings/GameSettings;limitFramerate:I"))
    private int getMinimumFPSForChunkBuilding(GameSettings settings) {
        return 30;
    }
}
