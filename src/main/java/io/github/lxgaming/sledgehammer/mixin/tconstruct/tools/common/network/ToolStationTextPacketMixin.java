/*
 * Copyright 2023 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.tconstruct.tools.common.network;

import io.github.lxgaming.sledgehammer.util.StringUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import slimeknights.mantle.network.AbstractPacketThreadsafe;
import slimeknights.tconstruct.common.TinkerNetwork;
import slimeknights.tconstruct.tools.common.inventory.ContainerToolStation;
import slimeknights.tconstruct.tools.common.network.ToolStationTextPacket;

@Mixin(value = ToolStationTextPacket.class, remap = false)
public abstract class ToolStationTextPacketMixin extends AbstractPacketThreadsafe {
    
    @Shadow
    public String text;
    
    /**
     * @author LX_Gaming
     * @reason https://github.com/SlimeKnights/TinkersConstruct/issues/3997
     */
    @Overwrite
    public void handleServerSafe(NetHandlerPlayServer netHandler) {
        Container container = netHandler.player.openContainer;
        if (!(container instanceof ContainerToolStation)) {
            return;
        }
        
        ContainerToolStation containerToolStation = (ContainerToolStation) container;
        
        // Sledgehammer start
        if (StringUtils.equals(containerToolStation.toolName, text)) {
            return;
        }
        // Sledgehammer end
        
        containerToolStation.setToolName(text);
        
        WorldServer world = netHandler.player.getServerWorld();
        for (EntityPlayer player : world.playerEntities) {
            if (netHandler.player == player) {
                continue;
            }
            
            Container otherContainer = player.openContainer;
            if (!(otherContainer instanceof ContainerToolStation)) {
                continue;
            }
            
            ContainerToolStation otherContainerToolStation = (ContainerToolStation) otherContainer;
            if (!containerToolStation.sameGui(otherContainerToolStation)) {
                continue;
            }
            
            // Sledgehammer start
            otherContainerToolStation.setToolName(text);
            // Sledgehammer end
            
            TinkerNetwork.sendTo(this, (EntityPlayerMP) player);
        }
    }
}