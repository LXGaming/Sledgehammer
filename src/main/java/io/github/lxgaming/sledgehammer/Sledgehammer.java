/*
 * Copyright 2017 Alex Thomson
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

package io.github.lxgaming.sledgehammer;

import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.Configuration;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import io.github.lxgaming.sledgehammer.util.Reference;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.common.launch.SpongeLaunch;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class Sledgehammer {
    
    private static Sledgehammer instance;
    private final Logger logger = LoggerFactory.getLogger(Reference.PLUGIN_NAME);
    private final Configuration configuration = new Configuration(SpongeLaunch.getConfigDir().resolve(Reference.PLUGIN_ID + ".conf"));
    private final Map<String, Function<MixinCategory, Boolean>> mixinMappings = Toolbox.newHashMap();
    private PluginContainer pluginContainer;
    
    private Sledgehammer() {
        instance = this;
    }
    
    public static boolean init() {
        if (getInstance() != null) {
            return false;
        }
        
        Sledgehammer sledgehammer = new Sledgehammer();
        sledgehammer.getConfiguration().loadConfiguration();
        sledgehammer.register();
        sledgehammer.getConfiguration().saveConfiguration();
        return true;
    }
    
    private void register() {
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.core.advancements.MixinAdvancementManager", MixinCategory::isAdvancementStacktrace);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.core.advancements.MixinAdvancementProgress", MixinCategory::isAdvancementProgress);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.core.block.MixinBlockGrass", MixinCategory::isBlockGrass);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.core.block.MixinBlockIce", MixinCategory::isBlockIce);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.core.server.MixinDedicatedServer", (module) -> true);
        
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.forge.common.MixinDimensionManager", MixinCategory::isDimensionManager);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.forge.common.MixinForgeHooks", MixinCategory::isAdvancementStacktrace);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.forge.entity.passive.MixinEntityVillager", MixinCategory::isTravelingMerchant);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.forge.world.storage.MixinWorldInfo", MixinCategory::isCeremonyRain);
    }
    
    public void debugMessage(String format, Object... arguments) {
        if (getConfig().map(Config::isDebug).orElse(false)) {
            getLogger().info(format, arguments);
        }
    }
    
    public static Sledgehammer getInstance() {
        return instance;
    }
    
    public Logger getLogger() {
        return logger;
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }
    
    public Optional<Config> getConfig() {
        if (getConfiguration() != null) {
            return Optional.ofNullable(getConfiguration().getConfig());
        }
        
        return Optional.empty();
    }
    
    public Map<String, Function<MixinCategory, Boolean>> getMixinMappings() {
        return mixinMappings;
    }
    
    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }
    
    protected void setPluginContainer(PluginContainer pluginContainer) {
        this.pluginContainer = pluginContainer;
    }
}