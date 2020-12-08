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

package io.github.lxgaming.sledgehammer.mixin.forge.fml.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.bridge.fml.common.LoaderBridge;
import io.github.lxgaming.sledgehammer.manager.MappingManager;
import net.minecraftforge.fml.common.FMLModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModClassLoader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.discovery.ModDiscoverer;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.libraries.Artifact;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(value = Loader.class, priority = 1337, remap = false)
public abstract class LoaderMixin implements LoaderBridge {
    
    @Shadow
    private ModClassLoader modClassLoader;
    
    @Shadow
    private List<ModContainer> mods;
    
    @Shadow
    private static File minecraftDir;
    
    private final List<Path> sledgehammer$candidates = Lists.newArrayList();
    private final Set<File> sledgehammer$files = Sets.newHashSet();
    private final Map<File, Set<String>> sledgehammer$mappings = Maps.newHashMap();
    
    @Inject(
            method = "identifyMods",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraftforge/fml/relauncher/libraries/LibraryManager;gatherLegacyCanidates(Ljava/io/File;)Ljava/util/List;"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onGatherLegacyCanidates(List<String> additionalContainers, CallbackInfoReturnable<ModDiscoverer> callbackInfoReturnable,
                                         ModDiscoverer discoverer, List<Artifact> maven_canidates, List<File> file_canidates) {
        file_canidates.forEach(file -> sledgehammer$candidates.add(sledgehammer$toPath(file)));
        maven_canidates.forEach(artifact -> sledgehammer$candidates.add(sledgehammer$toPath(artifact.getFile())));
        
        for (Map.Entry<File, Set<String>> entry : sledgehammer$mappings.entrySet()) {
            if (CoreModManager.getReparseableCoremods().contains(entry.getKey().getName())) {
                continue;
            }
            
            for (String id : entry.getValue()) {
                if (MappingManager.getModMapping(id).isPresent()) {
                    file_canidates.removeIf(entry.getKey()::equals);
                    maven_canidates.removeIf(artifact -> entry.getKey().equals(artifact.getFile()));
                    break;
                }
            }
        }
        
        sledgehammer$files.clear();
        sledgehammer$mappings.clear();
    }
    
    @Inject(
            method = "identifyMods",
            at = @At(
                    value = "RETURN"
            )
    )
    private void onIdentifyModsPost(List<String> additionalContainers, CallbackInfoReturnable<ModDiscoverer> callbackInfoReturnable) {
        if (Sledgehammer.getInstance().isDebug()) {
            Sledgehammer.getInstance().getLogger().info("Mods Order Pre:");
            sledgehammer$printModsOrder();
        }
        
        mods.sort(Comparator.comparingInt(modContainer -> {
            if (!(modContainer instanceof FMLModContainer) || modContainer.getSource() == null) {
                return 0;
            }
            
            return sledgehammer$candidates.indexOf(sledgehammer$toPath(modContainer.getSource()));
        }));
        
        sledgehammer$candidates.clear();
        
        if (Sledgehammer.getInstance().isDebug()) {
            Sledgehammer.getInstance().getLogger().info("Mods Order Post:");
            sledgehammer$printModsOrder();
        }
    }
    
    public void bridge$addFile(File file) {
        try {
            if (modClassLoader.containsSource(file) || sledgehammer$files.contains(file)) {
                return;
            }
            
            modClassLoader.addFile(file);
            sledgehammer$files.add(file);
            Sledgehammer.getInstance().getLogger().info("Loaded {}", file.getName());
        } catch (MalformedURLException ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error while adding {} to the classloader", file, ex);
        }
    }
    
    public File bridge$getMinecraftDirectory() {
        return minecraftDir;
    }
    
    public Map<File, Set<String>> bridge$getMappings() {
        return sledgehammer$mappings;
    }
    
    private void sledgehammer$printModsOrder() {
        for (int index = 0; index < mods.size(); index++) {
            ModContainer modContainer = mods.get(index);
            if (modContainer.getSource() == null) {
                continue;
            }
            
            Sledgehammer.getInstance().getLogger().debug("{}: {}", index, modContainer.getSource().getName());
        }
    }
    
    private Path sledgehammer$toPath(File file) {
        return file.toPath().toAbsolutePath().normalize();
    }
}