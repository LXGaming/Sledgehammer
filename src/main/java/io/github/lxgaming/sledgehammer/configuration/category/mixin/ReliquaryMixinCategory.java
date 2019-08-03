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

package io.github.lxgaming.sledgehammer.configuration.category.mixin;

import io.github.lxgaming.sledgehammer.configuration.annotation.Mapping;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ReliquaryMixinCategory {
    
    @Mapping(value = "xreliquary.items.ItemRendingGaleMixin", dependencies = {"xreliquary"})
    @Setting(value = "item-rending-gale", comment = "If 'true', fixes https://github.com/P3pp3rF1y/Reliquary/issues/370.")
    private boolean itemRendingGale = false;
    
    public boolean isItemRendingGale() {
        return itemRendingGale;
    }
}