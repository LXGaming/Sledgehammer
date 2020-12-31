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
import io.github.lxgaming.sledgehammer.launch.SledgehammerLaunch;
import io.github.lxgaming.sledgehammer.manager.MappingManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.util.Optional;

public class Sledgehammer {
    
    public static final String ID = "sledgehammer";
    public static final String NAME = "Sledgehammer";
    public static final String VERSION = "@version@";
    public static final String DESCRIPTION = "Smashes the stupid out of the client & server.";
    public static final String AUTHORS = "LX_Gaming";
    public static final String SOURCE = "https://github.io/LXGaming/Sledgehammer";
    public static final String WEBSITE = "https://lxgaming.github.io/";
    
    private static Sledgehammer instance;
    private final Logger logger;
    private final Configuration configuration;
    
    private Sledgehammer() {
        instance = this;
        this.logger = LogManager.getLogger(Sledgehammer.NAME);
        this.configuration = new Configuration(Paths.get("config").resolve(Sledgehammer.ID + ".conf"));
    }
    
    public static boolean init() {
        if (getInstance() != null) {
            return false;
        }
        
        SledgehammerLaunch.configureEnvironment();
        Sledgehammer sledgehammer = new Sledgehammer();
        if (!sledgehammer.reload()) {
            return false;
        }
        
        MappingManager.prepare();
        return true;
    }
    
    public boolean reload() {
        getConfiguration().loadConfiguration();
        if (!getConfig().isPresent()) {
            return false;
        }
        
        getConfiguration().saveConfiguration();
        return true;
    }
    
    public void debug(String format, Object... arguments) {
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
        return Optional.ofNullable(getConfiguration().getConfig());
    }
}