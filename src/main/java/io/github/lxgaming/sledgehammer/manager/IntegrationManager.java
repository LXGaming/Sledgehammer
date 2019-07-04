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
import io.github.lxgaming.sledgehammer.configuration.category.integration.ClientIntegrationCategory;
import io.github.lxgaming.sledgehammer.configuration.category.integration.CommonIntegrationCategory;
import io.github.lxgaming.sledgehammer.configuration.category.integration.ServerIntegrationCategory;
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
        registerServerIntegration(BotaniaIntegration.class, ServerIntegrationCategory::isBotania);
        registerServerIntegration(ForgeIntegration.class, ServerIntegrationCategory::isForge);
        registerServerIntegration(MistIntegration.class, ServerIntegrationCategory::isMist);
        registerServerIntegration(PrimalIntegration.class, ServerIntegrationCategory::isPrimal);
        registerServerIntegration(SpongeIntegration_Border.class, ServerIntegrationCategory::isSpongeBorder);
        registerServerIntegration(SpongeIntegration_Death.class, ServerIntegrationCategory::isSpongeDeath);
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
    
    private static boolean registerClientIntegration(Class<? extends AbstractIntegration> integrationClass, Function<ClientIntegrationCategory, Boolean> function) {
        return registerIntegration(integrationClass, config -> function.apply(config.getClientIntegrationCategory()));
    }
    
    private static boolean registerCommonIntegration(Class<? extends AbstractIntegration> integrationClass, Function<CommonIntegrationCategory, Boolean> function) {
        return registerIntegration(integrationClass, config -> function.apply(config.getCommonIntegrationCategory()));
    }
    
    private static boolean registerServerIntegration(Class<? extends AbstractIntegration> integrationClass, Function<ServerIntegrationCategory, Boolean> function) {
        return registerIntegration(integrationClass, config -> function.apply(config.getServerIntegrationCategory()));
    }
    
    private static boolean registerIntegration(Class<? extends AbstractIntegration> integrationClass, Function<Config, Boolean> function) {
        if (getIntegrationClasses().contains(integrationClass)) {
            Sledgehammer.getInstance().getLogger().warn("{} is already registered", integrationClass.getSimpleName());
            return false;
        }
        
        getIntegrationClasses().add(integrationClass);
        if (!Sledgehammer.getInstance().getConfig().map(function).orElse(false)) {
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