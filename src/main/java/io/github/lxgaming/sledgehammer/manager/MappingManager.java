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

package io.github.lxgaming.sledgehammer.manager;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.annotation.Mapping;
import io.github.lxgaming.sledgehammer.configuration.category.GeneralCategory;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import io.github.lxgaming.sledgehammer.launch.SledgehammerLaunch;
import io.github.lxgaming.sledgehammer.util.StringUtils;
import io.github.lxgaming.sledgehammer.util.Toolbox;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

public final class MappingManager {
    
    public static final Map<String, Boolean> MIXIN_MAPPINGS = Maps.newHashMap();
    public static final Map<String, Boolean> MOD_MAPPINGS = Maps.newHashMap();
    public static final Map<Class<?>, SledgehammerPlatform.State> STATE_MAPPINGS = Maps.newHashMap();
    
    public static void prepare() {
        Sledgehammer.getInstance().getConfig().map(Config::getGeneralCategory).map(GeneralCategory::getModMappings).ifPresent(MOD_MAPPINGS::putAll);
        
        MixinCategory mixinCategory = Sledgehammer.getInstance().getConfig().map(Config::getMixinCategory).orElseThrow(NullPointerException::new);
        for (Field field : mixinCategory.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                registerMixinMappings(field.get(mixinCategory));
            } catch (Exception ex) {
                Sledgehammer.getInstance().getLogger().error("Encountered an error while registering {} ({})", Toolbox.getClassSimpleName(field.getType()), field.getName());
            }
        }
        
        // Internal Mixins
        MIXIN_MAPPINGS.put("core.client.MinecraftMixin", true);
        MIXIN_MAPPINGS.put("core.crash.CrashReportMixin", true);
        MIXIN_MAPPINGS.put("core.server.DedicatedServerMixin", true);
        MIXIN_MAPPINGS.put("core.util.text.TextFormattingMixin", true);
        MIXIN_MAPPINGS.put("forge.fml.common.LoaderMixin", SledgehammerLaunch.isForgeRegistered() && !MOD_MAPPINGS.isEmpty());
        MIXIN_MAPPINGS.put("forge.fml.common.MetadataCollectionAccessor", SledgehammerLaunch.isForgeRegistered() && !MOD_MAPPINGS.isEmpty());
        MIXIN_MAPPINGS.put("platform.SledgehammerPlatformMixin_Mod", SledgehammerLaunch.isForgeRegistered() && !SledgehammerLaunch.isSpongeRegistered());
        MIXIN_MAPPINGS.put("platform.SledgehammerPlatformMixin_Plugin", SledgehammerLaunch.isSpongeRegistered());
    }
    
    private static void registerMixinMappings(Object object) throws Exception {
        Preconditions.checkNotNull(object);
        for (Field field : object.getClass().getDeclaredFields()) {
            Mapping[] mappings = field.getDeclaredAnnotationsByType(Mapping.class);
            if (mappings.length == 0) {
                continue;
            }
            
            field.setAccessible(true);
            boolean value = field.getBoolean(object);
            
            for (Mapping mapping : mappings) {
                if (value) {
                    registerModMappings(mapping.dependencies());
                }
                
                // Don't overwrite the mapping value if it's already true
                if (!MIXIN_MAPPINGS.getOrDefault(mapping.value(), false)) {
                    MIXIN_MAPPINGS.put(mapping.value(), value);
                }
            }
        }
    }
    
    private static void registerModMappings(String... dependencies) {
        Preconditions.checkNotNull(dependencies);
        for (String dependency : dependencies) {
            MOD_MAPPINGS.putIfAbsent(dependency, true);
        }
    }
    
    public static Optional<Boolean> getMixinMapping(String mixin) {
        return Optional.ofNullable(MIXIN_MAPPINGS.get(StringUtils.removeStart(mixin, "io.github.lxgaming.sledgehammer.mixin.")));
    }
    
    public static Optional<Boolean> getModMapping(String mod) {
        return Optional.ofNullable(MOD_MAPPINGS.get(mod));
    }
    
    public static Optional<SledgehammerPlatform.State> getStateMapping(Class<?> stateClass) {
        return Optional.ofNullable(STATE_MAPPINGS.get(stateClass));
    }
}