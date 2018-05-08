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

import com.google.common.reflect.TypeToken;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.util.Objects;

public class Configuration {
    
    private ConfigurationLoader<CommentedConfigurationNode> configurationLoader;
    private ConfigurationOptions configurationOptions;
    private CommentedConfigurationNode configurationNode;
    private Config config;
    
    public void loadConfiguration() {
        try {
            configurationLoader = HoconConfigurationLoader.builder().setPath(Sledgehammer.getInstance().getPath()).build();
            configurationOptions = ConfigurationOptions.defaults().setObjectMapperFactory(Sledgehammer.getInstance().getFactory());
            configurationNode = getConfigurationLoader().load(getConfigurationOptions());
            config = getConfigurationNode().getValue(TypeToken.of(Config.class), new Config());
            Sledgehammer.getInstance().getLogger().info("Successfully loaded configuration file.");
        } catch (IOException | ObjectMappingException | RuntimeException ex) {
            configurationNode = getConfigurationLoader().createEmptyNode(getConfigurationOptions());
            Sledgehammer.getInstance().getLogger().error("Encountered an error processing {}::loadConfiguration", getClass().getSimpleName(), ex);
        }
    }
    
    public void saveConfiguration() {
        try {
            Objects.requireNonNull(getConfig(), "Config cannot be null");
            getConfigurationNode().setValue(TypeToken.of(Config.class), getConfig());
            getConfigurationLoader().save(getConfigurationNode());
            Sledgehammer.getInstance().getLogger().info("Successfully saved configuration file.");
        } catch (IOException | ObjectMappingException | RuntimeException ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error processing {}::saveConfiguration", getClass().getSimpleName(), ex);
        }
    }
    
    private ConfigurationLoader<CommentedConfigurationNode> getConfigurationLoader() {
        return configurationLoader;
    }
    
    private ConfigurationOptions getConfigurationOptions() {
        return configurationOptions;
    }
    
    private CommentedConfigurationNode getConfigurationNode() {
        return configurationNode;
    }
    
    public Config getConfig() {
        return config;
    }
}