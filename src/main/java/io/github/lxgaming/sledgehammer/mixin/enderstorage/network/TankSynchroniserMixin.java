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

package io.github.lxgaming.sledgehammer.mixin.enderstorage.network;

import codechicken.enderstorage.network.TankSynchroniser;
import codechicken.lib.packet.PacketCustom;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;

@Mixin(value = TankSynchroniser.class, remap = false)
public abstract class TankSynchroniserMixin {
    
    @Shadow
    private static HashMap<String, TankSynchroniser.PlayerItemTankCache> playerItemTankStates;
    
    /**
     * @author LX_Gaming
     * @reason Fixes NullPointerException
     */
    @Overwrite
    public static void handleVisiblityPacket(EntityPlayerMP player, PacketCustom packet) {
        if (playerItemTankStates == null) {
            return;
        }
        
        TankSynchroniser.PlayerItemTankCache state = playerItemTankStates.get(player.getName());
        if (state == null) {
            return;
        }
        
        state.handleVisiblityPacket(packet);
    }
}