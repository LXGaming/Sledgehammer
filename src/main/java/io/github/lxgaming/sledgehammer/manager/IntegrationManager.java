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
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.IntegrationCategory;
import io.github.lxgaming.sledgehammer.integration.AbstractIntegration;
import io.github.lxgaming.sledgehammer.integration.BotaniaIntegration;
import io.github.lxgaming.sledgehammer.integration.ForgeIntegration;
import io.github.lxgaming.sledgehammer.integration.MistIntegration;
import io.github.lxgaming.sledgehammer.integration.PrimalIntegration;
import io.github.lxgaming.sledgehammer.integration.SpongeIntegration_Border;
import io.github.lxgaming.sledgehammer.integration.SpongeIntegration_Death;
import io.github.lxgaming.sledgehammer.util.Toolbox;

import java.util.Set;
import java.util.function.Function;

public final class IntegrationManager {
    
    private static final Set<AbstractIntegration> INTEGRATIONS = Sets.newLinkedHashSet();
    private static final Set<Class<? extends AbstractIntegration>> INTEGRATION_CLASSES = Sets.newLinkedHashSet();
    
    public static void register() {
        registerIntegration(BotaniaIntegration.class, IntegrationCategory::isBotania);
        registerIntegration(ForgeIntegration.class, IntegrationCategory::isForge);
        registerIntegration(MistIntegration.class, IntegrationCategory::isMist);
        registerIntegration(PrimalIntegration.class, IntegrationCategory::isPrimal);
        registerIntegration(SpongeIntegration_Border.class, IntegrationCategory::isSpongeBorder);
        registerIntegration(SpongeIntegration_Death.class, IntegrationCategory::isSpongeDeath);
    }
    
    public static void process() {
        SledgehammerPlatform.State state = SledgehammerPlatform.getInstance().getState();
        if (state == null) {
            return;
        }
        
        for (AbstractIntegration integration : getIntegrations()) {
            if (integration.getState() != state) {
                continue;
            }
            
            Set<String> missingDependencies = Sets.newLinkedHashSet();
            for (String dependency : integration.getDependencies()) {
                if (!SledgehammerPlatform.getInstance().isLoaded(dependency)) {
                    missingDependencies.add(dependency);
                }
            }
            
            if (!missingDependencies.isEmpty()) {
                Sledgehammer.getInstance().getLogger().warn("{} is missing the following dependencies: {}",
                        integration.getClass().getSimpleName(), String.join(", ", missingDependencies));
                continue;
            }
            
            Sledgehammer.getInstance().debug("Processing {} ({})", integration.getClass().getSimpleName(), state);
            
            try {
                integration.execute();
            } catch (Exception ex) {
                Sledgehammer.getInstance().getLogger().error("Encountered an error while executing {}", integration.getClass().getSimpleName(), ex);
            }
        }
    }
    
    private static boolean registerIntegration(Class<? extends AbstractIntegration> integrationClass, Function<IntegrationCategory, Boolean> function) {
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
        Sledgehammer.getInstance().debug("{} registered", integrationClass.getSimpleName());
        return true;
    }
    
    public static Set<AbstractIntegration> getIntegrations() {
        return INTEGRATIONS;
    }
    
    private static Set<Class<? extends AbstractIntegration>> getIntegrationClasses() {
        return INTEGRATION_CLASSES;
    }
}