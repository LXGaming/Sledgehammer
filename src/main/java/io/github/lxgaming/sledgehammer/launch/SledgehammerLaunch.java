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

package io.github.lxgaming.sledgehammer.launch;

import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.TypesafeMap;
import io.github.lxgaming.sledgehammer.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SledgehammerLaunch {
    
    private static final Logger LOGGER = LogManager.getLogger("Sledgehammer Launch");
    private static final Supplier<TypesafeMap.Key<Boolean>> FORGE_INITIALIZED = IEnvironment.buildKey("forge.initialized", Boolean.class);
    
    public static void configureEnvironment() {
        List<Map<String, String>> modlist = Launcher.INSTANCE.environment().getProperty(IEnvironment.Keys.MODLIST.get()).orElseThrow(() -> new RuntimeException("The MODLIST isn't set, huh?"));
        for (Map<String, String> mod : modlist) {
            String name = mod.get("name");
            String type = mod.get("type");
            
            if (StringUtils.equals(name, "fml") && StringUtils.equals(type, "TRANSFORMATIONSERVICE")) {
                Launcher.INSTANCE.environment().computePropertyIfAbsent(FORGE_INITIALIZED.get(), key -> true);
                LOGGER.debug("Detected Forge");
            }
        }
    }
    
    public static boolean isForgeInitialized() {
        return Launcher.INSTANCE.environment().getProperty(FORGE_INITIALIZED.get()).orElse(false);
    }
}