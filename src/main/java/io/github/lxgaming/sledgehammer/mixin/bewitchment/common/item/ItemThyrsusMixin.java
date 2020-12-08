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

package io.github.lxgaming.sledgehammer.mixin.bewitchment.common.item;

import com.bewitchment.common.item.ItemThyrsus;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.BewitchmentMixinCategory;
import io.github.lxgaming.sledgehammer.util.Locale;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import io.github.lxgaming.sledgehammer.util.text.adapter.LocaleAdapter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemThyrsus.class, remap = false)
public abstract class ItemThyrsusMixin {
    
    @Inject(
            method = "func_111207_a",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void onItemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        BewitchmentMixinCategory mixinCategory = Sledgehammer.getInstance().getConfig()
                .map(Config::getMixinCategory)
                .map(MixinCategory::getBewitchmentMixinCategory)
                .orElse(null);
        if (mixinCategory == null) {
            Sledgehammer.getInstance().getLogger().error("BewitchmentMixinCategory is unavailable");
            return;
        }
        
        if ((target instanceof EntityAnimal && ((EntityAnimal) target).isInLove()) || (target instanceof EntityTameable && ((EntityTameable) target).isTamed())) {
            if (mixinCategory.isPreventTameStealing()) {
                callbackInfoReturnable.setReturnValue(false);
                LocaleAdapter.sendMessage(player, Locale.MESSAGE_BEWITCHMENT_TAME, target.getName());
                return;
            }
        }
        
        if (mixinCategory.isTameDeny() && mixinCategory.getTameDenyList().contains(Toolbox.getRootId(target))) {
            callbackInfoReturnable.setReturnValue(false);
            LocaleAdapter.sendMessage(player, Locale.MESSAGE_BEWITCHMENT_TAME, target.getName());
        }
    }
}