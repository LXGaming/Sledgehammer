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

package io.github.lxgaming.sledgehammer.configuration;

import io.github.lxgaming.sledgehammer.configuration.category.IntegrationCategory;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Config {
    
    @Setting(value = "debug", comment = "For debugging purposes")
    private boolean debug = false;
    
    @Setting(value = "integration")
    private IntegrationCategory integrationCategory = new IntegrationCategory();
    
    @Setting(value = "mixin")
    private MixinCategory mixinCategory = new MixinCategory();
    
    public boolean isDebug() {
        return debug;
    }
    
    public IntegrationCategory getIntegrationCategory() {
        return integrationCategory;
    }
    
    public MixinCategory getMixinCategory() {
        return mixinCategory;
    }
}