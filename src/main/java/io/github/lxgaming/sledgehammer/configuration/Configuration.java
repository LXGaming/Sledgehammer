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
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;

import java.nio.file.Path;

public class Configuration {
    
    private ConfigurationLoader<CommentedConfigurationNode> configurationLoader;
    private ObjectMapper<Config>.BoundInstance objectMapper;
    
    public Configuration(Path path) {
        try {
            this.configurationLoader = HoconConfigurationLoader.builder().setPath(path).build();
            this.objectMapper = ObjectMapper.forClass(Config.class).bindToNew();
        } catch (Exception ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error while initializing configuration", ex);
        }
    }
    
    public void loadConfiguration() {
        try {
            this.objectMapper.populate(this.configurationLoader.load());
            Sledgehammer.getInstance().getLogger().info("Successfully loaded configuration file.");
        } catch (Exception ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error while loading config", ex);
        }
    }
    
    public void saveConfiguration() {
        try {
            ConfigurationNode configurationNode = this.configurationLoader.createEmptyNode();
            this.objectMapper.serialize(configurationNode);
            this.configurationLoader.save(configurationNode);
            Sledgehammer.getInstance().getLogger().info("Successfully saved configuration file.");
        } catch (Exception ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error while saving config", ex);
        }
    }
    
    public Config getConfig() {
        return this.objectMapper.getInstance();
    }
}