/*
 * Copyright 2018 Alex Thomson
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
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import net.minecraft.advancements.AdvancementProgress;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.common.interfaces.advancement.IMixinAdvancementProgress;
import org.spongepowered.common.interfaces.advancement.IMixinPlayerAdvancements;

import java.lang.reflect.Field;

@Mixin(value = AdvancementProgress.class, priority = 1337)
public abstract class MixinAdvancementProgress implements org.spongepowered.api.advancement.AdvancementProgress, IMixinAdvancementProgress {
    
    @Inject(method = "isDone", at = @At(value = "INVOKE"))
    private void onIsDone(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (!Sledgehammer.getInstance().getConfig().map(Config::getMixinCategory).map(MixinCategory::isAdvancementProgress).orElse(false)) {
            return;
        }
        
        if (!get(getAdvancement().getCriterion()).isPresent()) {
            Player player = ((IMixinPlayerAdvancements) getPlayerAdvancements()).getPlayer();
            Sledgehammer.getInstance().debugMessage("Resetting {} for {} ({})", getAdvancement().getCriterion().getName(), player.getName(), player.getUniqueId());
            resetProgressMap();
        }
    }
    
    private void resetProgressMap() {
        try {
            Field field = this.getClass().getDeclaredField("progressMap");
            field.setAccessible(true);
            field.set(this, null);
        } catch (Exception ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error processing {}::resetProgressMap", getClass().getSimpleName(), ex);
        }
    }
}