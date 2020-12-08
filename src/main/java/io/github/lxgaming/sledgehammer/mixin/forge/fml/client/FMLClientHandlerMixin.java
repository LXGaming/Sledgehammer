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

package io.github.lxgaming.sledgehammer.mixin.forge.fml.client;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ForgeMixinCategory;
import io.github.lxgaming.sledgehammer.mixin.core.client.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FMLClientHandler.class, remap = false)
public abstract class FMLClientHandlerMixin {
    
    /**
     * Thanks covers1624
     */
    @Inject(
            method = "reloadSearchTrees",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onReloadSearchTrees(CallbackInfo callbackInfo) {
        ForgeMixinCategory mixinCategory = Sledgehammer.getInstance().getConfig()
                .map(Config::getMixinCategory)
                .map(MixinCategory::getForgeMixinCategory)
                .orElse(null);
        if (mixinCategory == null) {
            Sledgehammer.getInstance().getLogger().error("ForgeMixinCategory is unavailable");
            return;
        }
        
        if (mixinCategory.isNukeSearchTree() || (mixinCategory.isNukeSearchTreeShutdown() && !((MinecraftAccessor) Minecraft.getMinecraft()).accessor$isRunning())) {
            Sledgehammer.getInstance().getLogger().warn("Skipping reloadSearchTrees");
            callbackInfo.cancel();
        }
    }
}