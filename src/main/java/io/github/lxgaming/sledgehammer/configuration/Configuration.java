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

import io.github.lxgaming.sledgehammer.Sledgehammer;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.NodeResolver;

import java.nio.file.Path;

public class Configuration {
    
    private final ConfigurationLoader<CommentedConfigurationNode> configurationLoader;
    private Config config;
    
    public Configuration(Path path) {
        this.configurationLoader = HoconConfigurationLoader.builder()
                .path(path)
                .defaultOptions(options -> options
                        .implicitInitialization(true)
                        .serializers(builder -> builder.registerAnnotatedObjects(ObjectMapper.factoryBuilder().addNodeResolver(NodeResolver.onlyWithSetting()).build()))
                        .shouldCopyDefaults(true))
                .build();
    }
    
    public void loadConfiguration() {
        try {
            CommentedConfigurationNode configurationNode = configurationLoader.load();
            this.config = configurationNode.get(Config.class);
            Sledgehammer.getInstance().getLogger().info("Successfully loaded configuration file.");
        } catch (Exception ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error while loading config", ex);
        }
    }
    
    public void saveConfiguration() {
        try {
            ConfigurationNode configurationNode = configurationLoader.createNode();
            configurationNode.set(config);
            configurationLoader.save(configurationNode);
            Sledgehammer.getInstance().getLogger().info("Successfully saved configuration file.");
        } catch (Exception ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error while saving config", ex);
        }
    }
    
    public Config getConfig() {
        return config;
    }
}