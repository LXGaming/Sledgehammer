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

package io.github.lxgaming.sledgehammer.mixin.plugin;

import com.google.common.collect.Sets;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.bridge.fml.common.LoaderBridge;
import io.github.lxgaming.sledgehammer.launch.SledgehammerLaunch;
import io.github.lxgaming.sledgehammer.manager.MappingManager;
import io.github.lxgaming.sledgehammer.mixin.forge.fml.common.MetadataCollectionAccessor;
import io.github.lxgaming.sledgehammer.util.StringUtils;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MetadataCollection;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.libraries.Artifact;
import net.minecraftforge.fml.relauncher.libraries.LibraryManager;
import net.minecraftforge.fml.relauncher.libraries.ModList;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ForgePlugin extends CorePlugin {
    
    private static boolean INITIALIZED;
    
    @Override
    public void onLoad(String mixinPackage) {
        super.onLoad(mixinPackage);
        if (MixinEnvironment.getCurrentEnvironment().getPhase() != MixinEnvironment.Phase.DEFAULT) {
            return;
        }
        
        if (INITIALIZED
                || SledgehammerLaunch.isDeobfuscatedEnvironment()
                || !SledgehammerLaunch.isForgeInitialized()
                || !SledgehammerLaunch.isSledgehammerInitialized()
                || MappingManager.MOD_MAPPINGS.isEmpty()) {
            return;
        }
        
        INITIALIZED = true;
        if (!MappingManager.getMixinMapping("forge.fml.common.LoaderMixin").orElse(false)) {
            Sledgehammer.getInstance().getLogger().error("LoaderMixin is disabled");
            return;
        }
        
        if (!MappingManager.getMixinMapping("forge.fml.common.MetadataCollectionAccessor").orElse(false)) {
            Sledgehammer.getInstance().getLogger().error("MetadataCollectionAccessor is disabled");
            return;
        }
        
        LoaderBridge loaderBridge = (LoaderBridge) Loader.instance();
        
        Set<File> files = Sets.newLinkedHashSet();
        files.addAll(LibraryManager.gatherLegacyCanidates(loaderBridge.bridge$getMinecraftDirectory()));
        ModList.getBasicLists(loaderBridge.bridge$getMinecraftDirectory()).stream()
                .map(ModList::getArtifacts)
                .flatMap(Collection::stream)
                .map(Artifact::getFile)
                .forEach(files::add);
        
        for (File file : files) {
            Set<String> ids = getModIds(file);
            if (ids == null || ids.isEmpty()) {
                Sledgehammer.getInstance().getLogger().debug("{}: No metadata", file.getName());
                continue;
            }
            
            loaderBridge.bridge$getMappings().computeIfAbsent(file, key -> Sets.newHashSet());
            Sledgehammer.getInstance().getLogger().debug("{}:", file.getName());
            for (String id : ids) {
                if (loaderBridge.bridge$getMappings().get(file).add(id)) {
                    Sledgehammer.getInstance().getLogger().debug("- {}", id);
                } else {
                    Sledgehammer.getInstance().getLogger().debug("- {} (Duplicate)", id);
                }
            }
        }
        
        int size = loaderBridge.bridge$getMappings().size();
        Sledgehammer.getInstance().getLogger().info("Identified {} {}", size, Toolbox.formatUnit(size, "mod", "mods"));
        
        for (Map.Entry<String, Boolean> entry : MappingManager.MOD_MAPPINGS.entrySet()) {
            File file = getFile(entry.getKey());
            if (file == null) {
                continue;
            }
            
            if (entry.getValue()) {
                // Add Mods
                if (CoreModManager.getIgnoredMods().removeIf(file.getName()::equals)) {
                    Sledgehammer.getInstance().getLogger().info("Acknowledged {}", file.getName());
                }
                
                loaderBridge.bridge$addFile(file);
            } else {
                // Remove Mods
                if (CoreModManager.getIgnoredMods().contains(file.getName())) {
                    continue;
                }
                
                CoreModManager.getIgnoredMods().add(file.getName());
                Sledgehammer.getInstance().getLogger().info("Ignored {}", file.getName());
            }
        }
    }
    
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!SledgehammerLaunch.isForgeInitialized()) {
            return false;
        }
        
        return super.shouldApplyMixin(targetClassName, mixinClassName);
    }
    
    private File getFile(String id) {
        LoaderBridge loaderBridge = (LoaderBridge) Loader.instance();
        for (Map.Entry<File, Set<String>> entry : loaderBridge.bridge$getMappings().entrySet()) {
            if (StringUtils.containsIgnoreCase(entry.getValue(), id)) {
                return entry.getKey();
            }
        }
        
        return null;
    }
    
    private Set<String> getModIds(File file) {
        Map<String, ModMetadata> metadatas = getMetadataCollection(file).accessor$getMetadatas();
        if (metadatas.isEmpty()) {
            return null;
        }
        
        metadatas.keySet().removeIf(key -> key.equals("examplemod"));
        if (!metadatas.isEmpty()) {
            return metadatas.keySet();
        }
        
        AnnotationNode annotationNode = getModAnnotation(file);
        if (annotationNode == null) {
            return null;
        }
        
        String id = parseModAnnotation(annotationNode);
        if (id != null) {
            return Sets.newHashSet(id);
        }
        
        return null;
    }
    
    private MetadataCollectionAccessor getMetadataCollection(File file) {
        try (JarFile jarFile = new JarFile(file)) {
            ZipEntry zipEntry = jarFile.getEntry("mcmod.info");
            if (zipEntry == null) {
                return (MetadataCollectionAccessor) MetadataCollection.from(null, "");
            }
            
            try (InputStream inputStream = jarFile.getInputStream(zipEntry)) {
                return (MetadataCollectionAccessor) MetadataCollection.from(inputStream, jarFile.getName());
            }
        } catch (Exception ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error while getting metadata from {}", file, ex);
            return (MetadataCollectionAccessor) MetadataCollection.from(null, "");
        }
    }
    
    private AnnotationNode getModAnnotation(File file) {
        try (JarFile jarFile = new JarFile(file)) {
            for (Enumeration<JarEntry> enumeration = jarFile.entries(); enumeration.hasMoreElements(); ) {
                JarEntry jarEntry = enumeration.nextElement();
                if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                    continue;
                }
                
                try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
                    ClassNode classNode = new ClassNode();
                    ClassReader classReader = new ClassReader(inputStream);
                    classReader.accept(classNode, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                    
                    if (classNode.visibleAnnotations == null || classNode.visibleAnnotations.isEmpty()) {
                        continue;
                    }
                    
                    for (AnnotationNode annotationNode : classNode.visibleAnnotations) {
                        if (annotationNode.desc.equals("Lnet/minecraftforge/fml/common/Mod;")) {
                            return annotationNode;
                        }
                    }
                }
            }
            
            return null;
        } catch (Exception ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error while getting annotation from {}", file, ex);
            return null;
        }
    }
    
    private String parseModAnnotation(AnnotationNode annotationNode) {
        List<Object> values = annotationNode.values;
        if (values == null || values.isEmpty()) {
            return null;
        }
        
        for (int index = 0; index < values.size(); index++) {
            Object value = values.get(index);
            if (!(value instanceof String) || !value.equals("modid")) {
                continue;
            }
            
            return (String) values.get(index + 1);
        }
        
        return null;
    }
}