/*
 * Copyright 2021 Alex Thomson
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
public class ThaumicWondersMixinCategory {
    
    @Mapping(value = "thaumicwonders.common.network.packets.PacketMeteorbActionMixin", dependencies = {"thaumicwonders"})
    @Setting(value = "meteorb-action", comment = "If 'true', prevents the Meteorb Action from changing the weather.")
    private boolean meteorbAction = false;
    
    @Mapping(value = "thaumicwonders.common.network.packets.PacketTimewinderActionMixin", dependencies = {"thaumicwonders"})
    @Setting(value = "timewinder-action", comment = "If 'true', prevents the Timewinder Action from changing the time.")
    private boolean timewinderAction = false;
    
    public boolean isMeteorbAction() {
        return meteorbAction;
    }
    
    public boolean isTimewinderAction() {
        return timewinderAction;
    }
}