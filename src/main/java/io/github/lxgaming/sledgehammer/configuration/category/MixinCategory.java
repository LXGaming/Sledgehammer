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

package io.github.lxgaming.sledgehammer.configuration.category;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class MixinCategory {
    
    @Setting(value = "advancement-progress", comment = "Fixes https://github.com/SpongePowered/SpongeCommon/issues/1904 (Fixed in SpongeForge-1.12.2-2705-7.1.0-BETA-3158 & SpongeVanilla-1.12.2-7.1.0-BETA-58)")
    private boolean advancementProgress = false;
    
    @Setting(value = "advancement-stacktrace", comment = "Prints a single message instead of a stacktrace for advancement errors")
    private boolean advancementStacktrace = false;
    
    @Setting(value = "biome-provider", comment = "Fixed NPE in findBiomePosition method")
    private boolean biomeProvider = false;
    
    @Setting(value = "block-grass", comment = "Prevents Grass turning into Dirt")
    private boolean blockGrass = false;
    
    @Setting(value = "block-ice", comment = "Prevents Ice turning into Water")
    private boolean blockIce = false;
    
    @Setting(value = "ceremony-rain", comment = "Prevents Totemic from changing the weather")
    private boolean ceremonyRain = false;
    
    @Setting(value = "dimension-manager", comment = "Fixes https://github.com/SpongePowered/SpongeForge/issues/2173 (Fixed in SpongeForge-1.12.2-2703-7.1.0-BETA-3124)")
    private boolean dimensionManager = false;
    
    @Setting(value = "harvest-block", comment = "Prevents ClassCastException caused by Sponge assuming things")
    private boolean harvestBlock = false;
    
    @Setting(value = "interact-events", comment = "Fixes https://github.com/SpongePowered/SpongeCommon/issues/2013")
    private boolean interactEvents = false;
    
    @Setting(value = "message-spam", comment = "Redirects spammy messages to Sledgehammer debug")
    private boolean messageSpam = false;
    
    @Setting(value = "network-system", comment = "Fixes potential deadlock on shutdown")
    private boolean networkSystem = false;
    
    @Setting(value = "traveling-merchant", comment = "Fixes https://github.com/Daveyx0/PrimitiveMobs/issues/59")
    private boolean travelingMerchant = false;
    
    public boolean isAdvancementProgress() {
        return advancementProgress;
    }
    
    public boolean isAdvancementStacktrace() {
        return advancementStacktrace;
    }
    
    public boolean isBiomeProvider() {
        return biomeProvider;
    }
    
    public boolean isBlockGrass() {
        return blockGrass;
    }
    
    public boolean isBlockIce() {
        return blockIce;
    }
    
    public boolean isCeremonyRain() {
        return ceremonyRain;
    }
    
    public boolean isDimensionManager() {
        return dimensionManager;
    }
    
    public boolean isHarvestBlock() {
        return harvestBlock;
    }
    
    public boolean isInteractEvents() {
        return interactEvents;
    }
    
    public boolean isMessageSpam() {
        return messageSpam;
    }
    
    public boolean isNetworkSystem() {
        return networkSystem;
    }
    
    public boolean isTravelingMerchant() {
        return travelingMerchant;
    }
}