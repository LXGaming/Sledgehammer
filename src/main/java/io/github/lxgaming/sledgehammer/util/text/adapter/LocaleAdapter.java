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
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ChatType;

public class LocaleAdapter {
    
    public static void sendErrorMessage(ICommandSender commandSender, Locale locale, Object... arguments) {
        TextAdapter.sendErrorMessage(commandSender, LocaleManager.serialize(locale, arguments));
    }
    
    public static void sendFeedback(ICommandSender commandSender, Locale locale, Object... arguments) {
        TextAdapter.sendFeedback(commandSender, LocaleManager.serialize(locale, arguments));
    }
    
    public static void sendMessage(EntityPlayer player, Locale locale, Object... arguments) {
        TextAdapter.sendMessage(player, LocaleManager.serialize(locale, arguments));
    }
    
    public static void sendStatusMessage(EntityPlayer player, Locale locale, Object... arguments) {
        TextAdapter.sendStatusMessage(player, LocaleManager.serialize(locale, arguments));
    }
    
    public static void disconnect(EntityPlayerMP player, Locale locale, Object... arguments) {
        TextAdapter.disconnect(player, LocaleManager.serialize(locale, arguments));
    }
    
    public static void sendMessage(EntityPlayerMP player, ChatType chatType, Locale locale, Object... arguments) {
        TextAdapter.sendMessage(player, chatType, LocaleManager.serialize(locale, arguments));
    }
}