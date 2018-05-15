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

package io.github.lxgaming.sledgehammer.mixin.plugin;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import io.github.lxgaming.sledgehammer.util.Reference;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.util.PrettyPrinter;

import java.util.function.Function;

public abstract class AbstractPlugin implements IMixinConfigPlugin {
    
    @Override
    public void onLoad(String mixinPackage) {
        Sledgehammer.init();
    }
    
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        Function<MixinCategory, Boolean> mixinMapping = Sledgehammer.getInstance().getMixinMappings().get(mixinClassName);
        if (mixinMapping == null) {
            new PrettyPrinter(50).add("Could not find function for " + Reference.PLUGIN_NAME + " mixin").centre().hr()
                    .add("Missing function for class: " + mixinClassName)
                    .print();
            return false;
        }
        
        return Sledgehammer.getInstance().getConfig().map(Config::getMixinCategory).map(mixinMapping).orElse(false);
    }
}
