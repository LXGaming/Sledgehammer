/*
 * Copyright 2020 Alex Thomson
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

package io.github.lxgaming.sledgehammer.integration.forge;

import com.google.common.collect.Maps;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.IntegrationCategory;
import io.github.lxgaming.sledgehammer.configuration.category.integration.ForgeIntegrationCategory;
import io.github.lxgaming.sledgehammer.integration.Integration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.List;
import java.util.Map;

public class ForgeIntegration_Recipe extends Integration {
    
    @Override
    public boolean prepare() {
        addDependency("forge");
        state(SledgehammerPlatform.State.LOAD_COMPLETE);
        return true;
    }
    
    @Override
    public void execute() throws Exception {
        List<String> blacklistedRecipeItems = Sledgehammer.getInstance().getConfig()
                .map(Config::getIntegrationCategory)
                .map(IntegrationCategory::getForgeIntegrationCategory)
                .map(ForgeIntegrationCategory::getBlacklistedRecipeItems)
                .orElse(null);
        if (blacklistedRecipeItems == null || blacklistedRecipeItems.isEmpty()) {
            return;
        }
        
        Map<ResourceLocation, ResourceLocation> resourceLocations = Maps.newHashMap();
        IForgeRegistryModifiable<IRecipe> recipes = (IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES;
        for (Map.Entry<ResourceLocation, IRecipe> entry : recipes.getEntries()) {
            ItemStack recipeOutput = entry.getValue().getRecipeOutput();
            if (recipeOutput.isEmpty()) {
                continue;
            }
            
            ResourceLocation resourceLocation = Item.REGISTRY.getNameForObject(recipeOutput.getItem());
            if (resourceLocation == null || !blacklistedRecipeItems.contains(resourceLocation.toString())) {
                continue;
            }
            
            resourceLocations.put(entry.getKey(), resourceLocation);
        }
        
        for (Map.Entry<ResourceLocation, ResourceLocation> entry : resourceLocations.entrySet()) {
            recipes.remove(entry.getKey());
            Sledgehammer.getInstance().getLogger().info("Removed recipe {} ({})", entry.getKey(), entry.getValue());
        }
    }
}