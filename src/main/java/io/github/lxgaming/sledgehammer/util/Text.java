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

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class Text {
    
    public static final ITextComponent NEW_LINE = new TextComponentString("\n");
    
    public static ITextComponent of(Object... objects) {
        ITextComponent parentComponent = new TextComponentString("");
        Style style = new Style();
        for (Object object : objects) {
            if (object == null) {
                throw new IllegalArgumentException("Object cannot be null");
            } else if (object instanceof ITextComponent) {
                ITextComponent childComponent = (ITextComponent) object;
                if (!style.isEmpty()) {
                    childComponent.setStyle(style.createShallowCopy());
                    clearStyle(style);
                }
                
                parentComponent.appendSibling(childComponent);
            } else if (object instanceof Boolean
                    || object instanceof Character
                    || object instanceof CharSequence
                    || object instanceof Number
                    || object.getClass().isPrimitive()) {
                ITextComponent childComponent = new TextComponentString(String.valueOf(object));
                if (!style.isEmpty()) {
                    childComponent.setStyle(style.createShallowCopy());
                    clearStyle(style);
                }
                
                parentComponent.appendSibling(childComponent);
            } else if (object instanceof TextFormatting) {
                applyStyle(style, (TextFormatting) object);
            } else if (object instanceof ClickEvent) {
                style.setClickEvent((ClickEvent) object);
            } else if (object instanceof HoverEvent) {
                style.setHoverEvent((HoverEvent) object);
            } else {
                throw new UnsupportedOperationException(String.format("Unsupported object: %s", Toolbox.getClassSimpleName(object.getClass())));
            }
        }
        
        if (!style.isEmpty()) {
            throw new IllegalStateException("Style is not empty");
        }
        
        return parentComponent;
    }
    
    private static void applyStyle(Style style, TextFormatting textFormatting) {
        if (textFormatting.isColor()) {
            style.setColor(textFormatting);
        } else if (textFormatting == TextFormatting.BOLD) {
            style.setBold(true);
        } else if (textFormatting == TextFormatting.ITALIC) {
            style.setItalic(true);
        } else if (textFormatting == TextFormatting.OBFUSCATED) {
            style.setObfuscated(true);
        } else if (textFormatting == TextFormatting.STRIKETHROUGH) {
            style.setStrikethrough(true);
        } else if (textFormatting == TextFormatting.UNDERLINE) {
            style.setUnderlined(true);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported style: %s", textFormatting.name()));
        }
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
}