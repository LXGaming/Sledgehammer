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

package io.github.lxgaming.sledgehammer.mixin.core.advancements;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PlayerAdvancements.class, priority = 1337)
public abstract class PlayerAdvancementsMixin {
    
    @Redirect(method = "grantCriterion", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/AdvancementProgress;grantCriterion(Ljava/lang/String;)Z"))
    private boolean onGrantCriterion(AdvancementProgress advancementProgress, String criterion) {
        try {
            return advancementProgress.grantCriterion(criterion);
        } catch (IllegalStateException ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error while granting criterion {}: {}", criterion, ex);
            return false;
        }
    }
}