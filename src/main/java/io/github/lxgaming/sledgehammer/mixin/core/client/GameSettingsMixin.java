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

package io.github.lxgaming.sledgehammer.mixin.core.client;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.CoreMixinCategory;
import net.minecraft.client.GameSettings;
import net.minecraft.client.tutorial.TutorialSteps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameSettings.class)
public abstract class GameSettingsMixin {
    
    @Shadow
    public TutorialSteps tutorialStep;
    
    @Inject(
            method = "load",
            at = @At(
                    value = "RETURN"
            )
    )
    private void onLoad(CallbackInfo callbackInfo) {
        CoreMixinCategory coreMixinCategory = Sledgehammer.getInstance().getConfig()
                .map(Config::getMixinCategory)
                .map(MixinCategory::getCoreMixinCategory)
                .orElse(null);
        if (coreMixinCategory == null) {
            return;
        }
        
        if (coreMixinCategory.isTutorial()) {
            this.tutorialStep = TutorialSteps.NONE;
        }
    }
}