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

import io.github.lxgaming.sledgehammer.bridge.util.text.TextFormattingBridge;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import io.github.lxgaming.sledgehammer.util.text.EmptyTextComponent;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextAdapter {
    
    public static final char CHARACTER = '&';
    
    // Modified pattern from KyoriPowered/text
    // - Requires http or https to be present
    // - Supports domains consisting of single characters for example https://1.1.1.1/
    private static final Pattern URL_PATTERN = Pattern.compile("(?:(https?)://)([-\\w_.]+\\.\\w+)(/\\S*)?");
    
    public static void sendFailure(CommandSource commandSource, ITextComponent component) {
        commandSource.sendFailure(component);
    }
    
    public static void sendSuccess(CommandSource commandSource, ITextComponent component) {
        sendSuccess(commandSource, false, component);
    }
    
    public static void sendSuccess(CommandSource commandSource, boolean allowLogging, ITextComponent component) {
        commandSource.sendSuccess(component, allowLogging);
    }
    
    public static void sendMessage(PlayerEntity player, ITextComponent component) {
        player.sendMessage(component, Util.NIL_UUID);
    }
    
    public static void displayClientMessage(PlayerEntity player, ITextComponent component) {
        player.displayClientMessage(component, true);
    }
    
    public static void disconnect(ServerPlayerEntity player, ITextComponent component) {
        player.connection.disconnect(component);
    }
    
    public static void sendMessage(ServerPlayerEntity player, ChatType chatType, ITextComponent component) {
        player.sendMessage(component, chatType, Util.NIL_UUID);
    }
    
    public static ITextComponent serializeLegacyWithLinks(String string) {
        return serializeLegacyWithLinks(string, CHARACTER);
    }
    
    public static ITextComponent serializeLegacyWithLinks(String string, char character) {
        return serializeLegacyWithLinks(string, character, TextFormatting.BLUE);
    }
    
    public static ITextComponent serializeLegacyWithLinks(String string, char character, TextFormatting... textFormattings) {
        final IFormattableTextComponent rootTextComponent = new EmptyTextComponent();
        
        Matcher matcher = URL_PATTERN.matcher(string);
        int currentIndex = 0;
        while (matcher.find()) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();
            String url = matcher.group();
            
            // The text between the current and last url
            String text = string.substring(currentIndex, startIndex);
            if (!text.isEmpty()) {
                // Append the text component to the root text component
                rootTextComponent.append(serializeLegacy(text, character));
            }
            
            IFormattableTextComponent textComponent = new StringTextComponent(url);
            
            // Apply styles
            textComponent.withStyle(modify -> modify.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));
            textComponent.withStyle(textFormattings);
            
            // Append the text component to the root text component
            rootTextComponent.append(textComponent);
            
            // Mark the current index
            currentIndex = endIndex;
        }
        
        // The remaining text
        String text = string.substring(currentIndex);
        if (!text.isEmpty()) {
            // Append the text component to the root text component
            rootTextComponent.append(serializeLegacy(text, character));
        }
        
        return rootTextComponent;
    }
    
    public static ITextComponent serializeLegacy(String string) {
        return serializeLegacy(string, CHARACTER);
    }
    
    public static ITextComponent serializeLegacy(String string, char character) {
        final IFormattableTextComponent rootTextComponent = new EmptyTextComponent();
        Style currentStyle = Style.EMPTY;
        
        IFormattableTextComponent parentTextComponent = rootTextComponent;
        int currentIndex = 0;
        int index = 0;
        while ((index = string.indexOf(character, index)) != -1) {
            int startIndex = index;
            
            // Skip the first character, this is always the character parameter
            index += 1;
            
            // Break out of the while loop if the end of the string has been reached
            if (index == string.length()) {
                break;
            }
            
            // Parse the formatting code
            TextFormatting textFormatting = getTextFormatting(string.charAt(index));
            if (textFormatting == null) {
                continue;
            }
            
            // Skip the second character, this is always a valid formatting code
            index += 1;
            
            // The text between the current and last formatting code
            String text = string.substring(currentIndex, startIndex);
            
            // Mark the current index
            currentIndex = index;
            
            if (!text.isEmpty()) {
                IFormattableTextComponent textComponent = new StringTextComponent(text);
                
                // Apply the style
                textComponent.setStyle(currentStyle);
                
                // Clear the style
                currentStyle = Style.EMPTY;
                
                // Append the text component to the parent text component
                parentTextComponent.append(textComponent);
                
                if (textFormatting.isFormat()) {
                    // Set the parent text component to the text component
                    parentTextComponent = textComponent;
                } else {
                    // Reset the parent text component back to the root text component
                    parentTextComponent = rootTextComponent;
                }
            } else if (!textFormatting.isFormat()) {
                // Reset the parent text component back to the root text component
                parentTextComponent = rootTextComponent;
                
                // Clear the style
                currentStyle = Style.EMPTY;
            }
            
            // Apply the text formatting to the current style
            currentStyle = currentStyle.applyFormat(textFormatting);
        }
        
        // The remaining text
        String text = string.substring(currentIndex);
        if (!text.isEmpty()) {
            IFormattableTextComponent textComponent = new StringTextComponent(text);
            
            // Set the style, shallow copy isn't required as no further changes will be made
            textComponent.setStyle(currentStyle);
            
            // Append the text component to the parent text component
            parentTextComponent.append(textComponent);
        }
        
        return rootTextComponent;
    }
    
    private static TextFormatting getTextFormatting(char character) {
        final char code = Character.toLowerCase(character);
        for (TextFormatting textFormatting : TextFormatting.values()) {
            if (getCode(textFormatting) == code) {
                return textFormatting;
            }
        }
        
        return null;
    }
    
    private static char getCode(TextFormatting textFormatting) {
        // bridge$getCode is only valid at runtime
        // noinspection ConstantConditions
        if (TextFormattingBridge.class.isInstance(textFormatting)) {
            return Toolbox.cast(textFormatting, TextFormattingBridge.class).bridge$getCode();
        }
        
        // code is only valid at build time
        return textFormatting.code;
    }
}