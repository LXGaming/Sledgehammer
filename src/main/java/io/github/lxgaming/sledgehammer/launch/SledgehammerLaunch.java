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

import io.github.lxgaming.sledgehammer.util.Reference;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.common.ForgeVersion;
import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.util.List;

public class SledgehammerLaunch {
    
    private static final String FORGE_CLASS = "net.minecraftforge.fml.relauncher.CoreModManager";
    private static final String FORGE_INITIALIZED = ForgeVersion.MOD_ID + ".initialized";
    private static final String SLEDGEHAMMER_INITIALIZED = Reference.ID + ".initialized";
    
    private SledgehammerLaunch() {
    }
    
    public static void configureClassLoader(LaunchClassLoader classLoader) {
        classLoader.addClassLoaderExclusion("io.github.lxgaming.sledgehammer.launch.");
    }
    
    public static void configureEnvironment() {
        if (!isForgeRegistered() && Toolbox.isClassPresent(FORGE_CLASS)) {
            registerForge();
        }
        
        if (!isMixinRegistered()) {
            MixinBootstrap.init();
        }
        
        if (!isSledgehammerRegistered()) {
            registerSledgehammer();
            
            Mixins.addConfiguration("mixins.sledgehammer.preinit.json");
        }
    }
    
    public static List<ITweaker> getTweakers() {
        return GlobalProperties.get("Tweaks");
    }
    
    public static boolean isForgeRegistered() {
        return GlobalProperties.get(FORGE_INITIALIZED) != null;
    }
    
    private static void registerForge() {
        GlobalProperties.put(FORGE_INITIALIZED, ForgeVersion.getVersion());
    }
    
    public static boolean isMixinRegistered() {
        return GlobalProperties.get(GlobalProperties.Keys.INIT) != null;
    }
    
    public static boolean isSledgehammerRegistered() {
        return GlobalProperties.get(SLEDGEHAMMER_INITIALIZED) != null;
    }
    
    private static void registerSledgehammer() {
        GlobalProperties.put(SLEDGEHAMMER_INITIALIZED, Reference.VERSION);
    }
}