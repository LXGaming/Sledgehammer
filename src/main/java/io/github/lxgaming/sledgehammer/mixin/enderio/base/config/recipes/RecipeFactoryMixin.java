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

package io.github.lxgaming.sledgehammer.mixin.enderio.base.config.recipes;

import crazypants.enderio.base.Log;
import crazypants.enderio.base.config.recipes.RecipeFactory;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.io.IOException;

@Mixin(value = RecipeFactory.class, remap = false)
public abstract class RecipeFactoryMixin {
    
    /**
     * Oh the irony
     */
    @Inject(
            method = "copyCore_dontMakeShittyCoreModsPlease",
            at = @At(
                    value = "INVOKE",
                    target = "Lcrazypants/enderio/base/Log;error([Ljava/lang/Object;)V"
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onError(ResourceLocation resourceLocation, File file, CallbackInfo callbackInfo, IOException e) {
        Log.error("Copying default recipe file from " + resourceLocation + " to " + file + " failed. Reason: " + e.getMessage());
        callbackInfo.cancel();
    }
}