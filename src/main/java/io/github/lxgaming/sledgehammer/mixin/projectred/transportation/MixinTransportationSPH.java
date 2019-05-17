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

package io.github.lxgaming.sledgehammer.mixin.projectred.transportation;

import codechicken.lib.packet.PacketCustom;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.util.Broadcast;
import io.github.lxgaming.sledgehammer.util.Text;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import mrtjp.projectred.ProjectRedTransportation;
import mrtjp.projectred.transportation.ItemRoutingChip;
import mrtjp.projectred.transportation.RoutingChip;
import mrtjp.projectred.transportation.TransportationSPH$;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ChatType;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = TransportationSPH$.class, priority = 1337, remap = false)
public abstract class MixinTransportationSPH {
    
    /**
     * @author LX_Gaming
     * @reason Fix Exploit
     */
    @Overwrite
    private void setChipNBT(PacketCustom packet, EntityPlayerMP player) {
        short slot = packet.readUByte();
        ItemStack stack = packet.readItemStack();
        
        if (stack.getItem() == ProjectRedTransportation.itemRoutingChip()) {
            ItemStack playerStack = player.inventory.getStackInSlot(slot);
            
            if (playerStack.getItem() == ProjectRedTransportation.itemRoutingChip()) {
                RoutingChip chip = ItemRoutingChip.loadChipFromItemStack(stack);
                ItemRoutingChip.saveChipToItemStack(playerStack, chip);
                
                player.inventory.setInventorySlotContents(slot, playerStack);
                player.inventory.markDirty();
            }
        } else {
            Sledgehammer.getInstance().getConfig().map(Config::getGeneralCategory).ifPresent(generalCategory -> {
                String message = generalCategory.getMessageCategory().getProjectRedExploit();
                if (StringUtils.isBlank(message)) {
                    return;
                }
                
                Broadcast broadcast = Broadcast.builder()
                        .message(Toolbox.convertColor(message.replace("[ID]", Toolbox.getResourceLocation(stack.getItem()).map(ResourceLocation::toString).orElse("Unknown"))))
                        .type(ChatType.CHAT)
                        .build();
                
                player.connection.disconnect(Text.of(Toolbox.getTextPrefix(), broadcast.getMessage()));
            });
        }
    }
}