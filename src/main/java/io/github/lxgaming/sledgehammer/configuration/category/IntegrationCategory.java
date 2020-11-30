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

package io.github.lxgaming.sledgehammer.configuration.category;

import io.github.lxgaming.sledgehammer.configuration.category.integration.BiomesOPlentyIntegrationCategory;
import io.github.lxgaming.sledgehammer.configuration.category.integration.BotaniaIntegrationCategory;
import io.github.lxgaming.sledgehammer.configuration.category.integration.ForgeIntegrationCategory;
import io.github.lxgaming.sledgehammer.configuration.category.integration.MistIntegrationCategory;
import io.github.lxgaming.sledgehammer.configuration.category.integration.PrimalIntegrationCategory;
import io.github.lxgaming.sledgehammer.configuration.category.integration.SpongeIntegrationCategory;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class IntegrationCategory {
    
    @Setting(value = "biomesoplenty", comment = "Biomes O' Plenty")
    private BiomesOPlentyIntegrationCategory biomesOPlentyIntegrationCategory = new BiomesOPlentyIntegrationCategory();
    
    @Setting(value = "botania", comment = "Botania")
    private BotaniaIntegrationCategory botaniaIntegrationCategory = new BotaniaIntegrationCategory();
    
    @Setting(value = "forge", comment = "Forge")
    private ForgeIntegrationCategory forgeIntegrationCategory = new ForgeIntegrationCategory();
    
    @Setting(value = "mist", comment = "Mist")
    private MistIntegrationCategory mistIntegrationCategory = new MistIntegrationCategory();
    
    @Setting(value = "primal", comment = "Primal")
    private PrimalIntegrationCategory primalIntegrationCategory = new PrimalIntegrationCategory();
    
    @Setting(value = "sponge", comment = "SpongeForge / SpongeVanilla")
    private SpongeIntegrationCategory spongeIntegrationCategory = new SpongeIntegrationCategory();
    
    public BiomesOPlentyIntegrationCategory getBiomesOPlentyIntegrationCategory() {
        return biomesOPlentyIntegrationCategory;
    }
    
    public BotaniaIntegrationCategory getBotaniaIntegrationCategory() {
        return botaniaIntegrationCategory;
    }
    
    public ForgeIntegrationCategory getForgeIntegrationCategory() {
        return forgeIntegrationCategory;
    }
    
    public MistIntegrationCategory getMistIntegrationCategory() {
        return mistIntegrationCategory;
    }
    
    public PrimalIntegrationCategory getPrimalIntegrationCategory() {
        return primalIntegrationCategory;
    }
    
    public SpongeIntegrationCategory getSpongeIntegrationCategory() {
        return spongeIntegrationCategory;
    }
}