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

package io.github.lxgaming.sledgehammer.configuration.category.mixin;

import com.google.common.collect.Lists;
import io.github.lxgaming.sledgehammer.configuration.annotation.Mapping;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class SpongeMixinCategory {
    
    @Mapping(value = "core.advancements.PlayerAdvancementsMixin")
    @Setting(value = "advancement-initialized", comment = "If 'true', catches an IllegalStateException thrown by Sponge.")
    private boolean advancementInitialized = false;
    
    @Mapping(value = "core.network.NetHandlerPlayServerMixin_Event")
    @Setting(value = "interact-events", comment = "If 'true', fixes https://github.com/SpongePowered/SpongeCommon/issues/2013.")
    private boolean interactEvents = false;
    
    @Mapping(value = "sponge.common.event.tracking.phase.packet.inventory.BasicInventoryPacketStateMixin")
    @Mapping(value = "sponge.common.event.SpongeCommonEventFactoryMixin")
    @Setting(value = "inventory-debug", comment = "If 'true', disables inventory debugging messages.")
    private boolean inventoryDebug = false;
    
    @Mapping(value = "core.entity.EntityMixin_Teleport")
    @Mapping(value = "forge.entity.EntityMixin_Teleport")
    @Setting(value = "item-teleport", comment = "If 'true', prevents or deletes any items that attempt to teleport across dimensions.")
    private boolean itemTeleport = false;
    
    @Setting(value = "item-teleport-whitelist", comment = "Don't prevent these items from teleporting.")
    private List<String> itemTeleportWhitelist = Lists.newArrayList("draconicevolution:ender_energy_manipulator");
    
    @Mapping(value = "core.tileentity.TileEntityMixin_StackOverflow")
    @Setting(value = "tile-entity-stack-overflow", comment = "If 'true', prevents StackOverflow on writeToNBT.")
    private boolean tileEntityStackOverflow = false;
    
    public boolean isAdvancementInitialized() {
        return advancementInitialized;
    }
    
    public boolean isInteractEvents() {
        return interactEvents;
    }
    
    public boolean isInventoryDebug() {
        return inventoryDebug;
    }
    
    public boolean isItemTeleport() {
        return itemTeleport;
    }
    
    public List<String> getItemTeleportWhitelist() {
        return itemTeleportWhitelist;
    }
    
    public boolean isTileEntityStackOverflow() {
        return tileEntityStackOverflow;
    }
}