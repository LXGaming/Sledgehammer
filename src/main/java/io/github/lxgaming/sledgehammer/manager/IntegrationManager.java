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
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.AstralSorceryMixinCategory;
import io.github.lxgaming.sledgehammer.integration.Integration;
import io.github.lxgaming.sledgehammer.integration.astralsorcery.AstralSorceryIntegration;
import io.github.lxgaming.sledgehammer.integration.biomesoplenty.BiomesOPlentyIntegration;
import io.github.lxgaming.sledgehammer.integration.botania.BotaniaIntegration;
import io.github.lxgaming.sledgehammer.integration.forge.ForgeIntegration_Permission;
import io.github.lxgaming.sledgehammer.integration.forge.ForgeIntegration_Recipe;
import io.github.lxgaming.sledgehammer.integration.mist.MistIntegration;
import io.github.lxgaming.sledgehammer.integration.primal.PrimalIntegration;
import io.github.lxgaming.sledgehammer.integration.sledgehammer.CommandIntegration_Server;
import io.github.lxgaming.sledgehammer.integration.sponge.SpongeIntegration_Border;
import io.github.lxgaming.sledgehammer.integration.sponge.SpongeIntegration_Death;
import io.github.lxgaming.sledgehammer.util.Toolbox;

import java.util.Set;
import java.util.function.Function;

public final class IntegrationManager {
    
    public static final Set<Integration> INTEGRATIONS = Sets.newLinkedHashSet();
    private static final Set<Class<? extends Integration>> INTEGRATION_CLASSES = Sets.newHashSet();
    
    public static void prepare() {
        // Internal Integration
        // registerIntegration(CommandIntegration_Client.class, category -> true);
        registerIntegration(CommandIntegration_Server.class, category -> true);
        
        registerIntegration(AstralSorceryIntegration.class, category -> Sledgehammer.getInstance().getConfig()
                .map(Config::getMixinCategory)
                .map(MixinCategory::getAstralSorceryMixinCategory)
                .map(AstralSorceryMixinCategory::isDataSerializers)
                .orElse(false)
        );
        registerIntegration(BiomesOPlentyIntegration.class, category -> category.getBiomesOPlentyIntegrationCategory().isBoatPurge());
        registerIntegration(BotaniaIntegration.class, category -> category.getBotaniaIntegrationCategory().isIslandCreation());
        registerIntegration(ForgeIntegration_Permission.class, category -> category.getForgeIntegrationCategory().isCheckPermissions());
        registerIntegration(ForgeIntegration_Recipe.class, category -> !category.getForgeIntegrationCategory().getBlacklistedRecipeItems().isEmpty());
        registerIntegration(MistIntegration.class, category -> category.getMistIntegrationCategory().isPortal());
        registerIntegration(PrimalIntegration.class, category -> category.getPrimalIntegrationCategory().isFlake());
        registerIntegration(SpongeIntegration_Border.class, category -> category.getSpongeIntegrationCategory().isBorder());
        registerIntegration(SpongeIntegration_Death.class, category -> category.getSpongeIntegrationCategory().isDeath());
    }
    
    public static void execute() {
        SledgehammerPlatform.State state = SledgehammerPlatform.getInstance().getState();
        for (Integration integration : INTEGRATIONS) {
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
                        Toolbox.getClassSimpleName(integration.getClass()), String.join(", ", missingDependencies));
                continue;
            }
            
            Sledgehammer.getInstance().getLogger().debug("Processing {} ({})", Toolbox.getClassSimpleName(integration.getClass()), state);
            
            try {
                integration.execute();
            } catch (Exception ex) {
                Sledgehammer.getInstance().getLogger().error("Encountered an error while executing {}", Toolbox.getClassSimpleName(integration.getClass()), ex);
            }
        }
    }
    
    private static boolean registerIntegration(Class<? extends Integration> integrationClass, Function<IntegrationCategory, Boolean> function) {
        if (INTEGRATION_CLASSES.contains(integrationClass)) {
            Sledgehammer.getInstance().getLogger().warn("{} is already registered", Toolbox.getClassSimpleName(integrationClass));
            return false;
        }
        
        INTEGRATION_CLASSES.add(integrationClass);
        if (!Sledgehammer.getInstance().getConfig().map(Config::getIntegrationCategory).map(function).orElse(false)) {
            return false;
        }
        
        Integration integration = Toolbox.newInstance(integrationClass);
        if (integration == null) {
            Sledgehammer.getInstance().getLogger().error("{} failed to initialize", Toolbox.getClassSimpleName(integrationClass));
            return false;
        }
        
        if (!integration.prepare()) {
            Sledgehammer.getInstance().getLogger().warn("{} failed to prepare", Toolbox.getClassSimpleName(integrationClass));
            return false;
        }
        
        INTEGRATIONS.add(integration);
        Sledgehammer.getInstance().getLogger().debug("{} registered", Toolbox.getClassSimpleName(integrationClass));
        return true;
    }
}