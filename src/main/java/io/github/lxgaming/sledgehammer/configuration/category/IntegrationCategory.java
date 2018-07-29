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

package io.github.lxgaming.sledgehammer.configuration.category;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class IntegrationCategory {
    
    @Setting(value = "forge", comment = "Forces permission check for OP based commands")
    private boolean forge = false;
    
    @Setting(value = "mist", comment = "Fixes portal issues")
    private boolean mist = false;
    
    @Setting(value = "primal", comment = "Fixes https://github.com/An-Sar/PrimalCore/issues/210 (Fixed in 0.6.56 according to Dev)")
    private boolean primal = false;
    
    public boolean isForge() {
        return forge;
    }
    
    public boolean isMist() {
        return mist;
    }
    
    public boolean isPrimal() {
        return primal;
    }
}