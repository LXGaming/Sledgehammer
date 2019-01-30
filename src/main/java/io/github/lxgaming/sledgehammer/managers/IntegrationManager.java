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

package io.github.lxgaming.sledgehammer.managers;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.IntegrationCategory;
import io.github.lxgaming.sledgehammer.integrations.AbstractIntegration;
import io.github.lxgaming.sledgehammer.util.Reference;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import org.spongepowered.api.Sponge;
import org.spongepowered.asm.util.PrettyPrinter;

import java.util.Set;
import java.util.function.Function;

public final class IntegrationManager {
    
    private static final Set<AbstractIntegration> INTEGRATIONS = Toolbox.newLinkedHashSet();
    private static final Set<Class<? extends AbstractIntegration>> INTEGRATION_CLASSES = Toolbox.newLinkedHashSet();
    
    public static boolean registerIntegration(Class<? extends AbstractIntegration> integrationClass) {
        if (getIntegrationClasses().contains(integrationClass)) {
            Sledgehammer.getInstance().getLogger().warn("{} has already been registered", integrationClass.getSimpleName());
            return false;
        }
        
        getIntegrationClasses().add(integrationClass);
        if (!shouldApplyIntegration(integrationClass)) {
            return false;
        }
        
        AbstractIntegration integration = Toolbox.newInstance(integrationClass).orElse(null);
        if (integration == null) {
            Sledgehammer.getInstance().getLogger().error("{} failed to initialize", integrationClass.getSimpleName());
            return false;
        }
        
        getIntegrations().add(integration);
        Set<String> missingDependencies = Toolbox.newLinkedHashSet();
        for (String dependency : integration.getDependencies()) {
            if (!Sponge.getPluginManager().isLoaded(dependency)) {
                missingDependencies.add(dependency);
            }
        }
        
        if (!missingDependencies.isEmpty()) {
            Sledgehammer.getInstance().getLogger().warn("{} is missing the following dependencies: {}", integrationClass.getSimpleName(), String.join(", ", missingDependencies));
            return false;
        }
        
        if (!integration.prepareIntegration()) {
            Sledgehammer.getInstance().getLogger().error("{} failed to prepare", integrationClass.getSimpleName());
            return false;
        }
        
        Sledgehammer.getInstance().debugMessage("{} registered", integrationClass.getSimpleName());
        return true;
    }
    
    private static boolean shouldApplyIntegration(Class<? extends AbstractIntegration> integrationClass) {
        Function<IntegrationCategory, Boolean> integrationMapping = Sledgehammer.getInstance().getIntegrationMappings().get(integrationClass.getName());
        if (integrationMapping == null) {
            new PrettyPrinter(50).add("Could not find function for " + Reference.NAME + " integration").centre().hr()
                    .add("Missing function for class: " + integrationClass.getName())
                    .print();
            return false;
        }
        
        return Sledgehammer.getInstance().getConfig().map(Config::getIntegrationCategory).map(integrationMapping).orElse(false);
    }
    
    public static Set<AbstractIntegration> getIntegrations() {
        return INTEGRATIONS;
    }
    
    private static Set<Class<? extends AbstractIntegration>> getIntegrationClasses() {
        return INTEGRATION_CLASSES;
    }
}