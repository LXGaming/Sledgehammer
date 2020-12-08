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

package io.github.lxgaming.sledgehammer.configuration.category.mixin;

import com.google.common.collect.Lists;
import io.github.lxgaming.sledgehammer.configuration.annotation.Mapping;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class BewitchmentMixinCategory {
    
    @Mapping(value = "bewitchment.common.block.tile.entity.TileEntityGlyphMixin", dependencies = {"bewitchment"})
    @Setting(value = "disable-ritual-deluge", comment = "If 'true', disables Ritual of the Deluge")
    private boolean disableRitualDeluge = false;
    
    @Mapping(value = "bewitchment.common.block.tile.entity.TileEntityGlyphMixin", dependencies = {"bewitchment"})
    @Setting(value = "disable-ritual-drought", comment = "If 'true', disables Ritual of the Parched Sands")
    private boolean disableRitualDrought = false;
    
    @Mapping(value = "bewitchment.common.block.tile.entity.TileEntityGlyphMixin", dependencies = {"bewitchment"})
    @Setting(value = "disable-ritual-high-moon", comment = "If 'true', disables Ritual of the High Moon")
    private boolean disableRitualHighMoon = false;
    
    @Mapping(value = "bewitchment.common.block.tile.entity.TileEntityGlyphMixin", dependencies = {"bewitchment"})
    @Setting(value = "disable-ritual-rising-twigs", comment = "If 'true', disables Rite of the Rising Twigs")
    private boolean disableRitualRisingTwigs = false;
    
    @Mapping(value = "bewitchment.common.block.tile.entity.TileEntityGlyphMixin", dependencies = {"bewitchment"})
    @Setting(value = "disable-ritual-sands-of-time", comment = "If 'true', disables Ritual of the Sands of Time")
    private boolean disableRitualSandsOfTime = false;
    
    @Mapping(value = "bewitchment.common.block.tile.entity.TileEntityGlyphMixin", dependencies = {"bewitchment"})
    @Setting(value = "disable-ritual-solar-glory", comment = "If 'true', disables Ritual of Solar Glory")
    private boolean disableRitualSolarGlory = false;
    
    @Mapping(value = "bewitchment.common.item.ItemThyrsusMixin", dependencies = {"bewitchment"})
    @Mapping(value = "bewitchment.common.potion.PotionFriendshipMixin", dependencies = {"bewitchment"})
    @Setting(value = "prevent-tame-stealing", comment = "If 'true', prevents Friendship Potion & Thyrsus from taming already tamed mobs")
    private boolean preventTameStealing = false;
    
    @Mapping(value = "bewitchment.common.item.ItemThyrsusMixin", dependencies = {"bewitchment"})
    @Mapping(value = "bewitchment.common.potion.PotionFriendshipMixin", dependencies = {"bewitchment"})
    @Setting(value = "tame-deny", comment = "If 'true', prevents Friendship Potion & Thyrsus from taming mobs listed in tame-deny-list.")
    private boolean tameDeny = false;
    
    @Setting(value = "tame-deny-list", comment = "Mobs that are not allowed to be tamed")
    private List<String> tameDenyList = Lists.newArrayList("minecraft:ender_dragon");
    
    public boolean isDisableRitualDeluge() {
        return disableRitualDeluge;
    }
    
    public boolean isDisableRitualDrought() {
        return disableRitualDrought;
    }
    
    public boolean isDisableRitualHighMoon() {
        return disableRitualHighMoon;
    }
    
    public boolean isDisableRitualRisingTwigs() {
        return disableRitualRisingTwigs;
    }
    
    public boolean isDisableRitualSandsOfTime() {
        return disableRitualSandsOfTime;
    }
    
    public boolean isDisableRitualSolarGlory() {
        return disableRitualSolarGlory;
    }
    
    public boolean isPreventTameStealing() {
        return preventTameStealing;
    }
    
    public boolean isTameDeny() {
        return tameDeny;
    }
    
    public List<String> getTameDenyList() {
        return tameDenyList;
    }
}