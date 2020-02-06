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

package io.github.lxgaming.sledgehammer.integration.botania;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import io.github.lxgaming.sledgehammer.integration.Integration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class BotaniaIntegration extends Integration {
    
    @Override
    public boolean prepare() {
        addDependency("botania");
        addDependency("forge");
        addDependency("sponge");
        state(SledgehammerPlatform.State.INITIALIZATION);
        return true;
    }
    
    @Override
    public void execute() throws Exception {
        Sponge.getEventManager().registerListeners(SledgehammerPlatform.getInstance().getContainer(), this);
    }
    
    @Listener(order = Order.EARLY, beforeModifications = true)
    public void onClientConnectionJoin(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player player) {
        NBTTagCompound entityData = ((EntityPlayer) player).getEntityData();
        if (!entityData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
        }
        
        NBTTagCompound persisted = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        if (!persisted.getBoolean("Botania-MadeIsland")) {
            persisted.setBoolean("Botania-MadeIsland", true);
            Sledgehammer.getInstance().debug("Set Botania-MadeIsland=true for {} ({})", player.getName(), player.getUniqueId());
        }
        
        if (!persisted.getBoolean("Botania-HasOwnIsland")) {
            persisted.setBoolean("Botania-HasOwnIsland", true);
            Sledgehammer.getInstance().debug("Set Botania-HasOwnIsland=true for {} ({})", player.getName(), player.getUniqueId());
        }
    }
}