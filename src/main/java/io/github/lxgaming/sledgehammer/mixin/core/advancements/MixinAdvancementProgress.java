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
import net.minecraft.advancements.AdvancementProgress;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.advancement.criteria.AdvancementCriterion;
import org.spongepowered.api.advancement.criteria.OperatorCriterion;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.common.advancement.ICriterionProgress;
import org.spongepowered.common.interfaces.advancement.IMixinAdvancementProgress;
import org.spongepowered.common.interfaces.advancement.IMixinPlayerAdvancements;

import java.util.Map;
import java.util.Optional;

@Mixin(value = AdvancementProgress.class, priority = 1337)
public abstract class MixinAdvancementProgress implements org.spongepowered.api.advancement.AdvancementProgress, IMixinAdvancementProgress {
    
    @Dynamic(mixin = org.spongepowered.common.mixin.core.advancement.MixinAdvancementProgress.class)
    private Map<AdvancementCriterion, ICriterionProgress> progressMap;
    
    @Inject(method = "isDone", at = @At(value = "HEAD"), cancellable = true)
    private void onIsDone(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (progressMap == null || progressMap.isEmpty()) {
            callbackInfoReturnable.setReturnValue(false);
            return;
        }
        
        AdvancementCriterion advancementCriterion = getAdvancement().getCriterion();
        Player player = ((IMixinPlayerAdvancements) getPlayerAdvancements()).getPlayer();
        
        Optional<ICriterionProgress> criterionProgress;
        if (advancementCriterion instanceof OperatorCriterion) {
            criterionProgress = getCriterionProgress((OperatorCriterion) advancementCriterion);
        } else {
            criterionProgress = getCriterionProgress(advancementCriterion);
        }
        
        callbackInfoReturnable.setReturnValue(criterionProgress.map(ICriterionProgress::achieved).orElse(false));
        
        if (!criterionProgress.isPresent()) {
            Sledgehammer.getInstance().debugMessage("Failed to find {} for {} ({})", advancementCriterion.getName(), player.getName(), player.getUniqueId());
        }
    }
    
    private Optional<ICriterionProgress> getCriterionProgress(AdvancementCriterion advancementCriterion) {
        for (Map.Entry<AdvancementCriterion, ICriterionProgress> entry : progressMap.entrySet()) {
            if (entry.getKey() instanceof OperatorCriterion) {
                continue;
            }
            
            if (StringUtils.equals(advancementCriterion.getName(), entry.getKey().getName())) {
                return Optional.of(entry.getValue());
            }
        }
        
        return Optional.empty();
    }
    
    private Optional<ICriterionProgress> getCriterionProgress(OperatorCriterion operatorCriterion) {
        for (Map.Entry<AdvancementCriterion, ICriterionProgress> entry : progressMap.entrySet()) {
            if (!(entry.getKey() instanceof OperatorCriterion)) {
                continue;
            }
            
            return Optional.of(entry.getValue());
        }
        
        return Optional.empty();
    }
}