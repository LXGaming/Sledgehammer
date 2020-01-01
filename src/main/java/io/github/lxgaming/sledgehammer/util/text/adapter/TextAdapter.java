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
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
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
    
    public static void sendErrorMessage(ICommandSender commandSender, ITextComponent component) {
        component.getStyle().setColor(TextFormatting.RED);
        commandSender.sendMessage(component);
    }
    
    public static void sendFeedback(ICommandSender commandSender, ITextComponent component) {
        commandSender.sendMessage(component);
    }
    
    public static void sendMessage(EntityPlayer player, ITextComponent component) {
        player.sendMessage(component);
    }
    
    public static void sendStatusMessage(EntityPlayer player, ITextComponent component) {
        player.sendStatusMessage(component, true);
    }
    
    public static void disconnect(EntityPlayerMP player, ITextComponent component) {
        player.connection.disconnect(component);
    }
    
    public static void sendMessage(EntityPlayerMP player, ChatType chatType, ITextComponent component) {
        player.connection.sendPacket(new SPacketChat(component, chatType));
    }
    
    public static ITextComponent serializeLegacyWithLinks(String string) {
        return serializeLegacyWithLinks(string, CHARACTER);
    }
    
    public static ITextComponent serializeLegacyWithLinks(String string, char character) {
        return serializeLegacyWithLinks(string, character, TextFormatting.BLUE);
    }
    
    public static ITextComponent serializeLegacyWithLinks(String string, char character, TextFormatting... textFormattings) {
        final ITextComponent rootTextComponent = new EmptyTextComponent();
        
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
                rootTextComponent.appendSibling(serializeLegacy(text, character));
            }
            
            ITextComponent textComponent = new TextComponentString(url);
            textComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
            for (TextFormatting textFormatting : textFormattings) {
                applyStyle(textComponent.getStyle(), textFormatting);
            }
            
            // Append the text component to the root text component
            rootTextComponent.appendSibling(textComponent);
            
            // Mark the current index
            currentIndex = endIndex;
        }
        
        // The remaining text
        String text = string.substring(currentIndex);
        if (!text.isEmpty()) {
            // Append the text component to the root text component
            rootTextComponent.appendSibling(serializeLegacy(text, character));
        }
        
        return rootTextComponent;
    }
    
    public static ITextComponent serializeLegacy(String string) {
        return serializeLegacy(string, CHARACTER);
    }
    
    public static ITextComponent serializeLegacy(String string, char character) {
        final ITextComponent rootTextComponent = new EmptyTextComponent();
        final Style currentStyle = new Style();
        
        ITextComponent parentTextComponent = rootTextComponent;
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
                ITextComponent textComponent = new TextComponentString(text);
                
                // Set the style to a shallow copy, this preserves unset formatting
                textComponent.setStyle(currentStyle.createShallowCopy());
                
                // Clear the style
                clearStyle(currentStyle);
                
                // Append the text component to the parent text component
                parentTextComponent.appendSibling(textComponent);
                
                if (textFormatting.isFancyStyling()) {
                    // Set the parent text component to the text component
                    parentTextComponent = textComponent;
                } else {
                    // Reset the parent text component back to the root text component
                    parentTextComponent = rootTextComponent;
                }
            } else if (!textFormatting.isFancyStyling()) {
                // Reset the parent text component back to the root text component
                parentTextComponent = rootTextComponent;
                
                // Clear the style
                clearStyle(currentStyle);
            }
            
            // Apply the text formatting to the current style
            applyStyle(currentStyle, textFormatting);
        }
        
        // The remaining text
        String text = string.substring(currentIndex);
        if (!text.isEmpty()) {
            ITextComponent textComponent = new TextComponentString(text);
            
            // Set the style, shallow copy isn't required as no further changes will be made
            textComponent.setStyle(currentStyle);
            
            // Append the text component to the parent text component
            parentTextComponent.appendSibling(textComponent);
        }
        
        return rootTextComponent;
    }
    
    @SuppressWarnings("ConstantConditions")
    private static void clearStyle(Style style) {
        style.setBold(null);
        style.setClickEvent(null);
        style.setColor(null);
        style.setHoverEvent(null);
        style.setInsertion(null);
        style.setItalic(null);
        style.setObfuscated(null);
        style.setParentStyle(null);
        style.setStrikethrough(null);
        style.setUnderlined(null);
    }
    
    private static void applyStyle(Style style, TextFormatting textFormatting) {
        if (textFormatting.isColor()) {
            style.setColor(textFormatting);
        } else if (textFormatting == TextFormatting.OBFUSCATED) {
            style.setObfuscated(true);
        } else if (textFormatting == TextFormatting.BOLD) {
            style.setBold(true);
        } else if (textFormatting == TextFormatting.STRIKETHROUGH) {
            style.setStrikethrough(true);
        } else if (textFormatting == TextFormatting.UNDERLINE) {
            style.setUnderlined(true);
        } else if (textFormatting == TextFormatting.ITALIC) {
            style.setItalic(true);
        } else if (textFormatting == TextFormatting.RESET) {
            // no-op
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported style: %s", textFormatting.name()));
        }
    }
    
    private static TextFormatting getTextFormatting(char character) {
        final char formattingCode = Character.toLowerCase(character);
        for (TextFormatting textFormatting : TextFormatting.values()) {
            if (getFormattingCode(textFormatting) == formattingCode) {
                return textFormatting;
            }
        }
        
        return null;
    }
    
    private static char getFormattingCode(TextFormatting textFormatting) {
        // bridge$getFormattingCode is only valid at runtime
        // noinspection ConstantConditions
        if (TextFormattingBridge.class.isInstance(textFormatting)) {
            return Toolbox.cast(textFormatting, TextFormattingBridge.class).bridge$getFormattingCode();
        }
        
        // formattingCode is only valid at build time
        return textFormatting.formattingCode;
    }
}