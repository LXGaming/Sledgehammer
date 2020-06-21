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

import io.github.lxgaming.sledgehammer.Sledgehammer;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.mixin.Mixins;

import java.lang.reflect.Method;
import java.util.List;

public class SledgehammerLaunch {
    
    private static final Logger LOGGER = LogManager.getLogger("Sledgehammer Launch");
    private static final String DEOBFUSCATED_ENVIRONMENT = "fml.deobfuscatedEnvironment";
    private static final String FORGE_INITIALIZED = "forge.initialized";
    private static final String MIXIN_INITIALIZED = "mixin.initialised";
    private static final String SLEDGEHAMMER_INITIALIZED = "sledgehammer.initialized";
    private static final String SPONGE_INITIALIZED = "sponge.initialized";
    private static final String TWEAK_CLASSES = "TweakClasses";
    private static final String TWEAKS = "Tweaks";
    private static final String FORGE_CLASS = "net.minecraftforge.fml.relauncher.CoreModManager";
    private static final String FORGE_DEOBF_TWEAKER_CLASS = "net.minecraftforge.fml.common.launcher.FMLDeobfTweaker";
    private static final String GRADLE_START_COMMON_CLASS = "net.minecraftforge.gradle.GradleStartCommon";
    private static final String MIXIN_STATE_TWEAKER_CLASS = "org.spongepowered.asm.mixin.EnvironmentStateTweaker";
    private static final String SPONGE_CLASS = "org.spongepowered.common.launch.SpongeLaunch";
    
    private SledgehammerLaunch() {
    }
    
    public static void configureClassLoader(LaunchClassLoader classLoader) {
        classLoader.addClassLoaderExclusion("io.github.lxgaming.sledgehammer.launch.");
        classLoader.addTransformerExclusion("io.github.lxgaming.sledgehammer.launch.");
        classLoader.addTransformerExclusion("io.github.lxgaming.sledgehammer.lib.");
    }
    
    public static void configureEnvironment() {
        if (!isForgeInitialized() && isClassPresent(FORGE_CLASS)) {
            putProperty(FORGE_INITIALIZED, Boolean.TRUE);
            SledgehammerLaunch.getLogger().debug("Detected Forge");
        }
        
        if (!isSpongeInitialized() && isClassPresent(SPONGE_CLASS)) {
            putProperty(SPONGE_INITIALIZED, Boolean.TRUE);
            SledgehammerLaunch.getLogger().debug("Detected Sponge");
        }
        
        if (!isSledgehammerInitialized() && isMixinInitialized() && isTweakerQueued(SledgehammerTweaker.class)) {
            putProperty(SLEDGEHAMMER_INITIALIZED, Sledgehammer.VERSION);
            
            // Triggers IMixinConfigPlugin::onLoad
            // ConcurrentModificationException - SpongeVanilla
            Mixins.addConfiguration("mixins.sledgehammer.preinit.json");
            SledgehammerLaunch.getLogger().debug("Detected Mixin & SledgehammerTweaker");
        }
    }
    
    public static boolean isEarly() {
        return !isClassPresent(FORGE_CLASS) || isClassPresent(GRADLE_START_COMMON_CLASS) || isTweakerQueued(FORGE_DEOBF_TWEAKER_CLASS);
    }
    
    public static boolean isStateTweakerPresent() {
        return isTweakerQueued(MIXIN_STATE_TWEAKER_CLASS) && isClassPresentInStackTrace(MIXIN_STATE_TWEAKER_CLASS);
    }
    
    public static boolean isClassPresent(String name) {
        try {
            return Class.forName(name, false, Launch.classLoader) != null;
        } catch (Throwable ex) {
            return false;
        }
    }
    
    public static boolean isClassPresentInStackTrace(String className) {
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            if (stackTraceElement.getClassName().equals(className)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean isTweakerQueued(Class<? extends ITweaker> tweakerClass) {
        return isTweakerQueued(tweakerClass.getName());
    }
    
    public static boolean isTweakerQueued(String tweakerClass) {
        return getTweakerClasses().contains(tweakerClass)
                || getTweakers().stream().map(ITweaker::getClass).map(Class::getName).anyMatch(tweakerClass::equals);
    }
    
    public static Logger getLogger() {
        return LOGGER;
    }
    
    public static List<String> getTweakerClasses() {
        return getProperty(TWEAK_CLASSES);
    }
    
    public static List<ITweaker> getTweakers() {
        return getProperty(TWEAKS);
    }
    
    public static String getMixinVersion() {
        return getProperty(MIXIN_INITIALIZED);
    }
    
    public static boolean isDeobfuscatedEnvironment() {
        return getProperty(DEOBFUSCATED_ENVIRONMENT, false);
    }
    
    public static boolean isForgeInitialized() {
        return getProperty(FORGE_INITIALIZED) == Boolean.TRUE;
    }
    
    public static boolean isMixinInitialized() {
        return getMixinVersion() != null;
    }
    
    public static boolean isSledgehammerInitialized() {
        return getProperty(SLEDGEHAMMER_INITIALIZED) != null;
    }
    
    public static boolean isSpongeInitialized() {
        return getProperty(SPONGE_INITIALIZED) == Boolean.TRUE;
    }
    
    private static <T> T getProperty(String key) {
        return getProperty(key, null);
    }
    
    @SuppressWarnings("unchecked")
    private static <T> T getProperty(String key, T defaultValue) {
        try {
            Object propertyKey = getPropertyKey(key);
            Method getMethod = GlobalProperties.class.getMethod("get", propertyKey.getClass(), Object.class);
            Object object = getMethod.invoke(null, propertyKey, defaultValue);
            if (object != null) {
                return (T) object;
            }
            
            return defaultValue;
        } catch (Throwable ex) {
            LOGGER.error("Encountered an error while getting {}: {}", key, ex);
            return defaultValue;
        }
    }
    
    private static void putProperty(String key, Object value) {
        try {
            Object propertyKey = getPropertyKey(key);
            Method setMethod = GlobalProperties.class.getMethod("put", propertyKey.getClass(), Object.class);
            setMethod.invoke(null, propertyKey, value);
        } catch (Throwable ex) {
            LOGGER.error("Encountered an error while setting {}: {}", key, ex);
        }
    }
    
    private static Object getPropertyKey(String name) {
        try {
            Method ofMethod = GlobalProperties.Keys.class.getMethod("of", String.class);
            
            // Mixin 0.8
            return ofMethod.invoke(null, name);
        } catch (Throwable ex) {
            // Mixin 0.7
            return name;
        }
    }
}