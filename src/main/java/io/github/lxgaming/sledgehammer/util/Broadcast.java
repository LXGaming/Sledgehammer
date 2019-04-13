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

package io.github.lxgaming.sledgehammer.util;

import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang3.StringUtils;

public class Broadcast {
    
    private final String message;
    private final String permission;
    private final ChatType type;
    
    private Broadcast(String message, String permission, ChatType type) {
        this.message = message;
        this.permission = permission;
        this.type = type;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public void sendMessage() {
        MinecraftServer minecraftServer = SledgehammerPlatform.getInstance().getServer();
        if (minecraftServer != null) {
            sendMessage(minecraftServer);
            minecraftServer.getPlayerList().getPlayers().forEach(this::sendMessage);
        }
    }
    
    public void sendMessage(ICommandSender commandSender) {
        commandSender.sendMessage(Text.of(Toolbox.getTextPrefix(), getMessage()));
    }
    
    public void sendMessage(EntityPlayer player) {
        if ((StringUtils.isNotBlank(getPermission()) && !player.canUseCommand(4, getPermission())) || getType() == null) {
            return;
        }
        
        if (getType() == ChatType.GAME_INFO) {
            sendMessage(player, Text.of(getMessage()), getType());
        } else {
            sendMessage(player, Text.of(Toolbox.getTextPrefix(), getMessage()), getType());
        }
    }
    
    private void sendMessage(EntityPlayer player, ITextComponent message, ChatType chatType) {
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).connection.sendPacket(new SPacketChat(message, chatType));
            return;
        }
        
        Minecraft.getMinecraft().ingameGUI.addChatMessage(chatType, message);
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getPermission() {
        return permission;
    }
    
    public ChatType getType() {
        return type;
    }
    
    public static final class Builder {
        
        private String message;
        private String permission;
        private ChatType type;
        
        public Broadcast build() {
            if (getMessage() == null) {
                message("");
            }
            
            return new Broadcast(getMessage(), getPermission(), getType());
        }
        
        private String getMessage() {
            return message;
        }
        
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        
        private String getPermission() {
            return permission;
        }
        
        public Builder permission(String permission) {
            this.permission = permission;
            return this;
        }
        
        private ChatType getType() {
            return type;
        }
        
        public Builder type(ChatType type) {
            this.type = type;
            return this;
        }
    }
}