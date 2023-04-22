/*
 * Copyright 2023 Alex Thomson
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
public class TinkersConstructMixinCategory {
    
    @Mapping(value = "tconstruct.tools.traits.TraitMagneticMixin", dependencies = "tconstruct")
    @Setting(value = "magnetic-void", comment = "If 'true', fixes magnetic trait voiding items")
    private boolean magneticVoid = false;
    
    @Mapping(value = "tconstruct.tools.common.network.ToolStationTextPacketMixin", dependencies = "tconstruct")
    @Setting(value = "text-sync", comment = "If 'true', fixes item duplication when renaming items in the Tool Forge")
    private boolean textSync = false;
    
    public boolean isMagneticVoid() {
        return magneticVoid;
    }
    
    public boolean isTextSync() {
        return textSync;
    }
}