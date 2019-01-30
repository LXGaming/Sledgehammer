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

package io.github.lxgaming.sledgehammer;

import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.Configuration;
import io.github.lxgaming.sledgehammer.configuration.category.IntegrationCategory;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import io.github.lxgaming.sledgehammer.util.Reference;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.common.launch.SpongeLaunch;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class Sledgehammer {
    
    private static Sledgehammer instance;
    private final Logger logger = LoggerFactory.getLogger(Reference.NAME);
    private final Configuration configuration = new Configuration(SpongeLaunch.getConfigDir().resolve(Reference.ID + ".conf"));
    private final Map<String, Function<IntegrationCategory, Boolean>> integrationMappings = Toolbox.newHashMap();
    private final Map<String, Function<MixinCategory, Boolean>> mixinMappings = Toolbox.newHashMap();
    private PluginContainer pluginContainer;
    
    private Sledgehammer() {
        instance = this;
    }
    
    public static boolean init() {
        if (getInstance() != null) {
            return false;
        }
        
        Sledgehammer sledgehammer = new Sledgehammer();
        sledgehammer.getConfiguration().loadConfiguration();
        sledgehammer.registerMappings();
        sledgehammer.getConfiguration().saveConfiguration();
        return true;
    }
    
    private void registerMappings() {
        // Integration
        getIntegrationMappings().put("io.github.lxgaming.sledgehammer.integrations.ForgeIntegration", IntegrationCategory::isForge);
        getIntegrationMappings().put("io.github.lxgaming.sledgehammer.integrations.MistIntegration", IntegrationCategory::isMist);
        getIntegrationMappings().put("io.github.lxgaming.sledgehammer.integrations.PrimalIntegration", IntegrationCategory::isPrimal);
        getIntegrationMappings().put("io.github.lxgaming.sledgehammer.integrations.SpongeIntegration_Border", IntegrationCategory::isSpongeBorder);
        getIntegrationMappings().put("io.github.lxgaming.sledgehammer.integrations.SpongeIntegration_Death", IntegrationCategory::isSpongeDeath);
        getIntegrationMappings().put("io.github.lxgaming.sledgehammer.integrations.SpongeIntegration_Phase", IntegrationCategory::isSpongePhase);
        
        // Mixin Core
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.core.advancements.MixinAdvancementManager", MixinCategory::isAdvancementStacktrace);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.core.block.MixinBlockGrass", MixinCategory::isBlockGrass);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.core.block.MixinBlockIce", MixinCategory::isBlockIce);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.core.entity.MixinEntity_Teleport", MixinCategory::isItemTeleport);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.core.item.MixinItemStack_Exploit", MixinCategory::isItemstackExploit);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.core.network.MixinNetHandlerPlayServer_Event", MixinCategory::isInteractEvents);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.core.network.MixinNetworkManager", MixinCategory::isFlushNetworkOnTick);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.core.network.MixinNetworkSystem", MixinCategory::isNetworkSystem);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.core.server.MixinDedicatedServer", (module) -> true);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.core.world.biome.MixinBiomeProvider", MixinCategory::isBiomeProvider);
        
        // Mixin Forge
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.forge.common.MixinForgeHooks_Advancement", MixinCategory::isAdvancementStacktrace);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.forge.entity.passive.MixinEntityVillager", MixinCategory::isTravelingMerchant);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.forge.entity.MixinEntity_Teleport", MixinCategory::isItemTeleport);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.forge.fml.common.network.simpleimpl.MixinSimpleChannelHandlerWrapper", MixinCategory::isFlushNetworkOnTick);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.forge.fml.common.network.simpleimpl.MixinSimpleNetworkWrapper", MixinCategory::isPacketSpam);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.forge.fml.common.network.MixinFMLEmbeddedChannel", MixinCategory::isFlushNetworkOnTick);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.forge.fml.common.network.MixinFMLEventChannel", MixinCategory::isFlushNetworkOnTick);
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.forge.world.storage.MixinWorldInfo", MixinCategory::isCeremonyRain);
        
        // Mixin Sponge
        getMixinMappings().put("io.github.lxgaming.sledgehammer.mixin.sponge.common.event.tracking.phase.packet.inventory.MixinBasicInventoryPacketState", MixinCategory::isInventoryDebug);
    }
    
    public void debugMessage(String format, Object... arguments) {
        if (getConfig().map(Config::isDebug).orElse(false)) {
            getLogger().info(format, arguments);
        }
    }
    
    public static Sledgehammer getInstance() {
        return instance;
    }
    
    public Logger getLogger() {
        return logger;
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }
    
    public Optional<Config> getConfig() {
        if (getConfiguration() != null) {
            return Optional.ofNullable(getConfiguration().getConfig());
        }
        
        return Optional.empty();
    }
    
    public Map<String, Function<IntegrationCategory, Boolean>> getIntegrationMappings() {
        return integrationMappings;
    }
    
    public Map<String, Function<MixinCategory, Boolean>> getMixinMappings() {
        return mixinMappings;
    }
    
    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }
    
    protected void setPluginContainer(PluginContainer pluginContainer) {
        this.pluginContainer = pluginContainer;
    }
}