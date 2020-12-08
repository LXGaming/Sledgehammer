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

package io.github.lxgaming.sledgehammer.mixin.bewitchment.common.block.tile.entity;

import com.bewitchment.api.registry.Ritual;
import com.bewitchment.common.block.tile.entity.TileEntityGlyph;
import com.bewitchment.common.ritual.RitualDeluge;
import com.bewitchment.common.ritual.RitualDrought;
import com.bewitchment.common.ritual.RitualHighMoon;
import com.bewitchment.common.ritual.RitualSandsOfTime;
import com.bewitchment.common.ritual.RitualSolarGlory;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.BewitchmentMixinCategory;
import io.github.lxgaming.sledgehammer.util.Locale;
import io.github.lxgaming.sledgehammer.util.StringUtils;
import io.github.lxgaming.sledgehammer.util.text.adapter.LocaleAdapter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityGlyph.class, remap = false)
public abstract class TileEntityGlyphMixin {
    
    @Inject(
            method = "startRitual",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onStartRitual(EntityPlayer player, Ritual rit, CallbackInfo callbackInfo) {
        BewitchmentMixinCategory mixinCategory = Sledgehammer.getInstance().getConfig()
                .map(Config::getMixinCategory)
                .map(MixinCategory::getBewitchmentMixinCategory)
                .orElse(null);
        if (mixinCategory == null) {
            Sledgehammer.getInstance().getLogger().error("BewitchmentMixinCategory is unavailable");
            return;
        }
        
        if (sledgehammer$isBroomRitual(rit)) {
            if (mixinCategory.isDisableRitualRisingTwigs()) {
                callbackInfo.cancel();
                LocaleAdapter.sendMessage(player, Locale.MESSAGE_DISABLED, "Rite of the Rising Twigs");
            }
            
            return;
        }
        
        if (rit instanceof RitualDeluge) {
            if (mixinCategory.isDisableRitualDeluge()) {
                callbackInfo.cancel();
                LocaleAdapter.sendMessage(player, Locale.MESSAGE_DISABLED, "Ritual of the Deluge");
            }
            
            return;
        }
        
        if (rit instanceof RitualDrought) {
            if (mixinCategory.isDisableRitualDrought()) {
                callbackInfo.cancel();
                LocaleAdapter.sendMessage(player, Locale.MESSAGE_DISABLED, "Ritual of the Parched Sands");
            }
            
            return;
        }
        
        if (rit instanceof RitualHighMoon) {
            if (mixinCategory.isDisableRitualHighMoon()) {
                callbackInfo.cancel();
                LocaleAdapter.sendMessage(player, Locale.MESSAGE_DISABLED, "Ritual of the High Moon");
            }
            
            return;
        }
        
        if (rit instanceof RitualSandsOfTime) {
            if (mixinCategory.isDisableRitualSandsOfTime()) {
                callbackInfo.cancel();
                LocaleAdapter.sendMessage(player, Locale.MESSAGE_DISABLED, "Ritual of the Sands of Time");
            }
            
            return;
        }
        
        if (rit instanceof RitualSolarGlory) {
            if (mixinCategory.isDisableRitualSolarGlory()) {
                callbackInfo.cancel();
                LocaleAdapter.sendMessage(player, Locale.MESSAGE_DISABLED, "Ritual of Solar Glory");
            }
            
            return;
        }
    }
    
    private boolean sledgehammer$isBroomRitual(Ritual ritual) {
        if (!ritual.getClass().equals(Ritual.class)) {
            return false;
        }
        
        ResourceLocation resourceLocation = ritual.getRegistryName();
        return resourceLocation != null && StringUtils.equalsAny(
                resourceLocation.getPath(),
                "cypress_broom", "dragons_blood_broom", "elder_broom", "juniper_broom"
        );
    }
}