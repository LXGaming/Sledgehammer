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

import com.google.common.collect.Sets;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.IntegrationCategory;
import io.github.lxgaming.sledgehammer.integration.AbstractIntegration;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import org.spongepowered.api.Sponge;

import java.util.Set;
import java.util.function.Function;

public final class IntegrationManager {
    
    private static final Set<AbstractIntegration> INTEGRATIONS = Sets.newLinkedHashSet();
    private static final Set<Class<? extends AbstractIntegration>> INTEGRATION_CLASSES = Sets.newLinkedHashSet();
    
    public static void process() {
        for (AbstractIntegration integration : getIntegrations()) {
            if (integration.getState() == null || integration.getState() != Sponge.getGame().getState()) {
                continue;
            }
            
            Set<String> missingDependencies = Sets.newLinkedHashSet();
            for (String dependency : integration.getDependencies()) {
                if (!Sponge.getPluginManager().isLoaded(dependency)) {
                    missingDependencies.add(dependency);
                }
            }
            
            if (!missingDependencies.isEmpty()) {
                Sledgehammer.getInstance().getLogger().warn("{} is missing the following dependencies: {}",
                        integration.getClass().getSimpleName(), String.join(", ", missingDependencies));
                continue;
            }
            
            Sledgehammer.getInstance().debugMessage("Processing {} ({})", integration.getClass().getSimpleName(), integration.getState());
            
            try {
                integration.execute();
            } catch (Exception ex) {
                Sledgehammer.getInstance().getLogger().error("Encountered an error while executing {}", integration.getClass().getSimpleName(), ex);
            }
        }
    }
    
    public static boolean registerIntegration(Class<? extends AbstractIntegration> integrationClass, Function<IntegrationCategory, Boolean> function) {
        if (getIntegrationClasses().contains(integrationClass)) {
            Sledgehammer.getInstance().getLogger().warn("{} is already registered", integrationClass.getSimpleName());
            return false;
        }
        
        getIntegrationClasses().add(integrationClass);
        if (!Sledgehammer.getInstance().getConfig().map(Config::getIntegrationCategory).map(function).orElse(false)) {
            return false;
        }
        
        AbstractIntegration integration = Toolbox.newInstance(integrationClass).orElse(null);
        if (integration == null) {
            Sledgehammer.getInstance().getLogger().error("{} failed to initialize", integrationClass.getSimpleName());
            return false;
        }
        
        getIntegrations().add(integration);
        Sledgehammer.getInstance().debugMessage("{} registered", integrationClass.getSimpleName());
        return true;
    }
    
    public static Set<AbstractIntegration> getIntegrations() {
        return INTEGRATIONS;
    }
    
    private static Set<Class<? extends AbstractIntegration>> getIntegrationClasses() {
        return INTEGRATION_CLASSES;
    }
}