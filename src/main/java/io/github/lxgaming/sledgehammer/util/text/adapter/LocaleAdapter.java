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

package io.github.lxgaming.sledgehammer.util.text.adapter;

import io.github.lxgaming.sledgehammer.manager.LocaleManager;
import io.github.lxgaming.sledgehammer.util.Locale;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ChatType;

public class LocaleAdapter {
    
    public static void sendErrorMessage(CommandSource commandSource, Locale locale, Object... arguments) {
        TextAdapter.sendErrorMessage(commandSource, LocaleManager.serialize(locale, arguments));
    }
    
    public static void sendFeedback(CommandSource commandSource, Locale locale, Object... arguments) {
        TextAdapter.sendFeedback(commandSource, false, LocaleManager.serialize(locale, arguments));
    }
    
    public static void sendFeedback(CommandSource commandSource, boolean allowLogging, Locale locale, Object... arguments) {
        TextAdapter.sendFeedback(commandSource, allowLogging, LocaleManager.serialize(locale, arguments));
    }
    
    public static void sendMessage(PlayerEntity player, Locale locale, Object... arguments) {
        TextAdapter.sendMessage(player, LocaleManager.serialize(locale, arguments));
    }
    
    public static void sendStatusMessage(PlayerEntity player, Locale locale, Object... arguments) {
        TextAdapter.sendStatusMessage(player, LocaleManager.serialize(locale, arguments));
    }
    
    public static void disconnect(ServerPlayerEntity player, Locale locale, Object... arguments) {
        TextAdapter.disconnect(player, LocaleManager.serialize(locale, arguments));
    }
    
    public static void sendMessage(ServerPlayerEntity player, ChatType chatType, Locale locale, Object... arguments) {
        TextAdapter.sendMessage(player, chatType, LocaleManager.serialize(locale, arguments));
    }
}