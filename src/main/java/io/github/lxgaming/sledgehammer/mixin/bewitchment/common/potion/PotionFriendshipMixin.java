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

package io.github.lxgaming.sledgehammer.mixin.bewitchment.common.potion;

import com.bewitchment.common.potion.PotionFriendship;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.BewitchmentMixinCategory;
import io.github.lxgaming.sledgehammer.util.Locale;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import io.github.lxgaming.sledgehammer.util.text.adapter.LocaleAdapter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PotionFriendship.class, remap = false)
public abstract class PotionFriendshipMixin {
    
    @Inject(
            method = "func_180793_a",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/passive/EntityTameable;setTamedBy(Lnet/minecraft/entity/player/EntityPlayer;)V",
                    remap = true
            ),
            cancellable = true
    )
    private void onSetTamedBy(Entity source, Entity indirectSource, EntityLivingBase living, int amplifier, double health, CallbackInfo callbackInfo) {
        BewitchmentMixinCategory mixinCategory = Sledgehammer.getInstance().getConfig()
                .map(Config::getMixinCategory)
                .map(MixinCategory::getBewitchmentMixinCategory)
                .orElse(null);
        if (mixinCategory == null) {
            Sledgehammer.getInstance().getLogger().error("BewitchmentMixinCategory is unavailable");
            return;
        }
        
        EntityPlayer player = (EntityPlayer) indirectSource;
        EntityTameable entityTameable = (EntityTameable) living;
        
        if (entityTameable.isTamed() && mixinCategory.isPreventTameStealing()) {
            callbackInfo.cancel();
            LocaleAdapter.sendMessage(player, Locale.MESSAGE_BEWITCHMENT_TAME, entityTameable.getName());
            return;
        }
        
        if (mixinCategory.isTameDeny() && mixinCategory.getTameDenyList().contains(Toolbox.getRootId(entityTameable))) {
            callbackInfo.cancel();
            LocaleAdapter.sendMessage(player, Locale.MESSAGE_BEWITCHMENT_TAME, entityTameable.getName());
        }
    }
}