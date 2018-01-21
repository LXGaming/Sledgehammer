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

package io.github.lxgaming.sledgehammer.mixin.plugin;

import com.google.common.base.Stopwatch;
import io.github.lxgaming.sledgehammer.util.Reference;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.MetadataCollection;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.FileListHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

public final class SledgehammerPlugin implements IMixinConfigPlugin {
    
    private final Logger logger = LogManager.getLogger(Reference.PLUGIN_ID);
    private final Pattern pattern = Pattern.compile("(.+).(zip|jar)$");
    private final List<ModData> modDataList = Toolbox.newArrayList();
    
    @Override
    public void onLoad(String mixinPackage) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        getLogger().info("{} v{} Loading...", Reference.PLUGIN_NAME, Reference.PLUGIN_VERSION);
        if (initialize()) {
            getLogger().info("Successfully loaded ({}ms)", stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
            return;
        }
        
        getLogger().error("Failed to load ({}ms)", stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
        FMLCommonHandler.instance().exitJava(1, true);
    }
    
    @Override
    public String getRefMapperConfig() {
        return null;
    }
    
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }
    
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }
    
    @Override
    public List<String> getMixins() {
        return null;
    }
    
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
    
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
    
    private boolean initialize() {
        try {
            if (!isClassPresent("net.minecraftforge.fml.relauncher.CoreModManager") && !isClassPresent("cpw.mods.fml.relauncher.CoreModManager")) {
                getLogger().info("Proceeding without FML support");
                return true;
            }
            
            getModDataList().add(new ModData(Action.REMOVE, Reference.PLUGIN_ID));
            return processModData(getModsDirectory().orElse(null));
        } catch (Exception ex) {
            getLogger().error("Encountered an error processing {}::initialize", getClass().getName(), ex);
            return false;
        }
    }
    
    private boolean processModData(File directory) {
        try {
            Objects.requireNonNull(directory);
            for (File file : FileListHelper.sortFileList(directory, null)) {
                if (file.isDirectory()) {
                    processModData(file);
                    continue;
                }
                
                if (!getPattern().matcher(file.getName()).matches()) {
                    continue;
                }
                
                Optional<MetadataCollection> metadataCollection = getMetadataCollection(file);
                if (!metadataCollection.isPresent()) {
                    continue;
                }
                
                for (Iterator<ModData> iterator = getModDataList().iterator(); iterator.hasNext(); ) {
                    ModData modData = iterator.next();
                    if (!modData.isValid()) {
                        continue;
                    }
                    
                    ModMetadata modMetadata = metadataCollection.get().getMetadataForId(modData.id, modData.createDummyMeta());
                    if (!modMetadata.autogenerated) {
                        modData.process(file.getName());
                        iterator.remove();
                    }
                }
                
                if (getModDataList().isEmpty()) {
                    return true;
                }
            }
            
            return false;
        } catch (RuntimeException ex) {
            getLogger().error("Encountered an error processing {}::processModData", getClass().getName(), ex);
            return false;
        }
    }
    
    private Optional<MetadataCollection> getMetadataCollection(File file) {
        try (JarFile jarFile = new JarFile(file)) {
            ZipEntry zipEntry = jarFile.getEntry("mcmod.info");
            if (zipEntry == null) {
                getLogger().debug("{} appears to be missing an mcmod.info file", file.getName());
                return Optional.empty();
            }
            
            try (InputStream inputStream = jarFile.getInputStream(zipEntry)) {
                return Optional.of(MetadataCollection.from(inputStream, jarFile.getName()));
            }
        } catch (IOException | RuntimeException ex) {
            getLogger().error("Encountered an error processing {}::getMetadataCollection", getClass().getName(), ex);
            return Optional.empty();
        }
    }
    
    private Optional<File> getModsDirectory() {
        try {
            return Optional.of(new File(Launch.minecraftHome, "mods"));
        } catch (RuntimeException ex) {
            getLogger().error("Encountered an error processing {}::getModsDirectory", getClass().getName(), ex);
            return Optional.empty();
        }
    }
    
    private boolean isClassPresent(String name) {
        try {
            return Class.forName(name, false, Launch.classLoader) != null;
        } catch (Exception ex) {
            return false;
        }
    }
    
    private Logger getLogger() {
        return logger;
    }
    
    private Pattern getPattern() {
        return pattern;
    }
    
    private List<ModData> getModDataList() {
        return modDataList;
    }
    
    private final class ModData {
        
        private final Action action;
        private final String id;
        
        private ModData(Action action, String id) {
            this.action = action;
            this.id = id;
        }
        
        private boolean isValid() {
            return getAction() != null && StringUtils.isNotBlank(getId());
        }
        
        private void process(String name) {
            if (StringUtils.isBlank(name)) {
                getLogger().warn("Provided arguments are invalid");
                return;
            }
            
            if (Objects.equals(getAction(), Action.ADD) && !CoreModManager.getIgnoredMods().contains(name)) {
                CoreModManager.getIgnoredMods().add(name);
                getLogger().info("Added {} ({}) to ignored mods", name, getId());
                return;
            }
            
            if (Objects.equals(getAction(), Action.REMOVE) && CoreModManager.getIgnoredMods().contains(name)) {
                CoreModManager.getIgnoredMods().remove(name);
                getLogger().info("Removed {} ({}) from ignored mods", name, getId());
                return;
            }
            
            getLogger().warn("Skipped {} ({})", name, getId());
        }
        
        private Map<String, Object> createDummyMeta() {
            Map<String, Object> dummyMeta = Toolbox.newHashMap();
            dummyMeta.put("name", getId());
            dummyMeta.put("version", "");
            return dummyMeta;
        }
        
        private Action getAction() {
            return action;
        }
        
        private String getId() {
            return id;
        }
    }
    
    private enum Action {
        
        ADD, REMOVE;
        
        @Override
        public String toString() {
            return StringUtils.capitalize(name().toLowerCase());
        }
    }
}