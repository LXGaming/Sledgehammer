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

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.chat.ChatTypes;

public class Broadcast {
    
    private final String message;
    private final String permission;
    private final Type type;
    
    private Broadcast(String message, String permission, Type type) {
        this.message = message;
        this.permission = permission;
        this.type = type;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public void sendMessage() {
        if (Sponge.isServerAvailable()) {
            sendMessage(Sponge.getServer().getConsole());
            Sponge.getServer().getOnlinePlayers().forEach(this::sendMessage);
        }
    }
    
    public void sendMessage(ConsoleSource consoleSource) {
        consoleSource.sendMessage(Text.of(Toolbox.getTextPrefix(), getMessage()));
    }
    
    public void sendMessage(Player player) {
        if (!player.isOnline() || (StringUtils.isNotBlank(getPermission()) && !player.hasPermission(getPermission())) || getType().getChatType() == null) {
            return;
        }
        
        if (getType() == Type.ACTION_BAR) {
            //player.sendMessage(getType().getChatType(), getMessage());
        } else {
            player.sendMessage(getType().getChatType(), Text.of(Toolbox.getTextPrefix(), getMessage()));
        }
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getPermission() {
        return permission;
    }
    
    public Type getType() {
        return type;
    }
    
    public enum Type {
        
        ACTION_BAR(ChatTypes.ACTION_BAR),
        CHAT(ChatTypes.CHAT),
        CONSOLE(null),
        SYSTEM(ChatTypes.SYSTEM);
        
        private final ChatType chatType;
        
        Type(ChatType chatType) {
            this.chatType = chatType;
        }
        
        public ChatType getChatType() {
            return chatType;
        }
    }
    
    public static final class Builder {
        
        private String message;
        private String permission;
        private Type type;
        
        public Broadcast build() {
            if (getMessage() == null) {
                message("");
            }
            
            if (getType() == null) {
                type(Type.CONSOLE);
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
        
        private Type getType() {
            return type;
        }
        
        public Builder type(Type type) {
            this.type = type;
            return this;
        }
    }
}