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

package io.github.lxgaming.sledgehammer.configuration.category.integration;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class SpongeIntegrationCategory {
    
    @Setting(value = "border", comment = "If 'true', prevents movement outside of the world border")
    private boolean border = false;
    
    @Setting(value = "death", comment = "If 'true', prevents sending blank death messages")
    private boolean death = false;
    
    public boolean isBorder() {
        return border;
    }
    
    public boolean isDeath() {
        return death;
    }
}