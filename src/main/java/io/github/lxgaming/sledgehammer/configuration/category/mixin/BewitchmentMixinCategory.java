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

import io.github.lxgaming.sledgehammer.configuration.annotation.Mapping;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class BewitchmentMixinCategory {
    
    @Mapping(value = "bewitchment.common.block.tile.entity.TileEntityGlyphMixin", dependencies = {"bewitchment"})
    @Setting(value = "disable-ritual-deluge", comment = "If 'true', disables Ritual of the Deluge")
    private boolean disableRitualDeluge = false;
    
    @Mapping(value = "bewitchment.common.block.tile.entity.TileEntityGlyphMixin", dependencies = {"bewitchment"})
    @Setting(value = "disable-ritual-high-moon", comment = "If 'true', disables Ritual of the High Moon")
    private boolean disableRitualHighMoon = false;
    
    @Mapping(value = "bewitchment.common.block.tile.entity.TileEntityGlyphMixin", dependencies = {"bewitchment"})
    @Setting(value = "disable-ritual-sands-of-time", comment = "If 'true', disables Ritual of the Sands of Time")
    private boolean disableRitualSandsOfTime = false;
    
    @Mapping(value = "bewitchment.common.block.tile.entity.TileEntityGlyphMixin", dependencies = {"bewitchment"})
    @Setting(value = "disable-ritual-solar-glory", comment = "If 'true', disables Ritual of Solar Glory")
    private boolean disableRitualSolarGlory = false;
    
    public boolean isDisableRitualDeluge() {
        return disableRitualDeluge;
    }
    
    public boolean isDisableRitualHighMoon() {
        return disableRitualHighMoon;
    }
    
    public boolean isDisableRitualSandsOfTime() {
        return disableRitualSandsOfTime;
    }
    
    public boolean isDisableRitualSolarGlory() {
        return disableRitualSolarGlory;
    }
}