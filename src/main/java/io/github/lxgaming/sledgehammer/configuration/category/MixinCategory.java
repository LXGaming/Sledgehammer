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

import com.google.common.collect.Lists;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class MixinCategory {
    
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
    
    @Setting(value = "flush-network-on-tick", comment = "Reduce Network usage by postponing flush")
    private boolean flushNetworkOnTick = false;
    
    @Setting(value = "interact-events", comment = "Fixes https://github.com/SpongePowered/SpongeCommon/issues/2013")
    private boolean interactEvents = false;
    
    @Setting(value = "inventory-debug", comment = "Redirects inventory debugging messages")
    private boolean inventoryDebug = false;
    
    @Setting(value = "itemstack-exploit", comment = "Fixes MC-134716")
    private boolean itemstackExploit = false;
    
    @Setting(value = "network-system", comment = "Fixes potential deadlock on shutdown")
    private boolean networkSystem = false;
    
    @Setting(value = "packet-spam", comment = "Cancels spammy packets")
    private boolean packetSpam = false;
    
    @Setting(value = "item-teleport", comment = "Prevents or deletes any items that attempt to teleport across dimensions")
    private boolean itemTeleport = false;
    
    @Setting(value = "item-teleport-whitelist", comment = "Don't prevent these items from teleporting")
    private List<String> itemTeleportWhitelist = Lists.newArrayList("draconicevolution:ender_energy_manipulator");
    
    @Setting(value = "traveling-merchant", comment = "Fixes https://github.com/Daveyx0/PrimitiveMobs/issues/59")
    private boolean travelingMerchant = false;
    
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
    
    public boolean isFlushNetworkOnTick() {
        return flushNetworkOnTick;
    }
    
    public boolean isInteractEvents() {
        return interactEvents;
    }
    
    public boolean isInventoryDebug() {
        return inventoryDebug;
    }
    
    public boolean isItemstackExploit() {
        return itemstackExploit;
    }
    
    public boolean isNetworkSystem() {
        return networkSystem;
    }
    
    public boolean isPacketSpam() {
        return packetSpam;
    }
    
    public boolean isItemTeleport() {
        return itemTeleport;
    }
    
    public List<String> getItemTeleportWhitelist() {
        return itemTeleportWhitelist;
    }
    
    public boolean isTravelingMerchant() {
        return travelingMerchant;
    }
}