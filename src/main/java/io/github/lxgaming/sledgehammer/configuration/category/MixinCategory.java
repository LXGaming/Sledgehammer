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

package io.github.lxgaming.sledgehammer.configuration.category;

import io.github.lxgaming.sledgehammer.configuration.category.mixin.CoreMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.IntegratedDynamicsMixinCategory;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class MixinCategory {
    
    @Setting(value = "core")
    @Comment(value = "Minecraft")
    private CoreMixinCategory coreMixinCategory = new CoreMixinCategory();
    
    @Setting(value = "integrateddynamics")
    @Comment(value = "Integrated Dynamics")
    private IntegratedDynamicsMixinCategory integratedDynamicsMixinCategory = new IntegratedDynamicsMixinCategory();
    
    public CoreMixinCategory getCoreMixinCategory() {
        return coreMixinCategory;
    }
    
    public IntegratedDynamicsMixinCategory getIntegratedDynamicsMixinCategory() {
        return integratedDynamicsMixinCategory;
    }
}