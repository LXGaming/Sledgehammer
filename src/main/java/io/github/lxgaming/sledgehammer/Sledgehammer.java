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
import io.github.lxgaming.sledgehammer.configuration.category.GeneralCategory;
import io.github.lxgaming.sledgehammer.manager.MappingManager;
import io.github.lxgaming.sledgehammer.util.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.util.Optional;

public class Sledgehammer {
    
    private static Sledgehammer instance;
    private final Logger logger = LogManager.getLogger(Reference.NAME);
    private final Configuration configuration = new Configuration(Paths.get("config").resolve(Reference.ID + ".conf"));
    
    private Sledgehammer() {
        instance = this;
    }
    
    public static boolean init() {
        if (getInstance() != null) {
            return false;
        }
        
        Sledgehammer sledgehammer = new Sledgehammer();
        sledgehammer.getConfiguration().loadConfiguration();
        MappingManager.registerMods();
        MappingManager.registerMixins();
        sledgehammer.getConfiguration().saveConfiguration();
        return true;
    }
    
    public void debugMessage(String format, Object... arguments) {
        if (getConfig().map(Config::getGeneralCategory).map(GeneralCategory::isDebug).orElse(false)) {
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
}