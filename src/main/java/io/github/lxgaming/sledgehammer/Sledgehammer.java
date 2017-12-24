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

import io.github.lxgaming.sledgehammer.util.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = Reference.PLUGIN_ID,
        name = Reference.PLUGIN_NAME,
        version = Reference.PLUGIN_VERSION,
        description = Reference.DESCRIPTION,
        authors = {Reference.AUTHORS},
        url = Reference.WEBSITE
)
public class Sledgehammer {
    
    private static Sledgehammer instance;
    private final Logger logger;
    
    public Sledgehammer() {
        instance = this;
        logger = LogManager.getLogger(Reference.PLUGIN_ID);
    }
    
    @Listener
    public void onGameLoadComplete(GameLoadCompleteEvent event) {
        getLogger().info("{} v{} has started.", Reference.PLUGIN_NAME, Reference.PLUGIN_VERSION);
    }
    
    @Listener
    public void onGameStopping(GameStoppingEvent event) {
        getLogger().info("{} v{} has stopped.", Reference.PLUGIN_NAME, Reference.PLUGIN_VERSION);
    }
    
    public static Sledgehammer getInstance() {
        return instance;
    }
    
    public Logger getLogger() {
        return logger;
    }
}