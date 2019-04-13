/*
 * Copyright 2017 Alex Thomson
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

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.launch.SledgehammerLaunch;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.common.launch.SpongeLaunch;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public class Toolbox {
    
    public static ITextComponent getTextPrefix() {
        ITextComponent text = new TextComponentString("");
        Style style = new Style()
                .setColor(TextFormatting.BLUE)
                .setBold(true)
                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, getPluginInformation()));
        
        text.appendSibling(new TextComponentString("[" + Reference.NAME + "]").setStyle(style));
        return text;
    }
    
    public static ITextComponent getPluginInformation() {
        ITextComponent text = new TextComponentString("");
        text.appendSibling(new TextComponentString(Reference.NAME).setStyle(new Style().setColor(TextFormatting.BLUE).setBold(true)));
        text.appendSibling(new TextComponentString("\n"));
        text.appendSibling(new TextComponentString("    Version: ").setStyle(new Style().setColor(TextFormatting.DARK_GRAY)));
        text.appendSibling(new TextComponentString(Reference.VERSION).setStyle(new Style().setColor(TextFormatting.WHITE)));
        text.appendSibling(new TextComponentString("\n"));
        text.appendSibling(new TextComponentString("    Authors: ").setStyle(new Style().setColor(TextFormatting.DARK_GRAY)));
        text.appendSibling(new TextComponentString(Reference.AUTHORS).setStyle(new Style().setColor(TextFormatting.WHITE)));
        text.appendSibling(new TextComponentString("\n"));
        text.appendSibling(new TextComponentString("    Source: ").setStyle(new Style().setColor(TextFormatting.DARK_GRAY)));
        text.appendSibling(new TextComponentString(Reference.SOURCE).setStyle(getURLStyle(Reference.SOURCE)));
        text.appendSibling(new TextComponentString("\n"));
        text.appendSibling(new TextComponentString("    Website: ").setStyle(new Style().setColor(TextFormatting.DARK_GRAY)));
        text.appendSibling(new TextComponentString(Reference.WEBSITE).setStyle(getURLStyle(Reference.SOURCE)));
        return text;
    }
    
    public static Style getURLStyle(String url) {
        return new Style().setColor(TextFormatting.BLUE).setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
    }
    
    public static String convertColor(String string) {
        return string.replaceAll("(?i)\u0026([0-9A-FK-OR])", "\u00A7$1");
    }
    
    /*
    public static Text getTextPrefix() {
        Text.Builder textBuilder = Text.builder();
        textBuilder.onHover(TextActions.showText(getPluginInformation()));
        textBuilder.append(Text.of(TextColors.BLUE, TextStyles.BOLD, "[", Reference.NAME, "]"));
        return Text.of(textBuilder.build(), TextStyles.RESET, " ");
    }
    
    public static Text getPluginInformation() {
        Text.Builder textBuilder = Text.builder();
        textBuilder.append(Text.of(TextColors.BLUE, TextStyles.BOLD, Reference.NAME, Text.NEW_LINE));
        textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Version: ", TextColors.WHITE, Reference.VERSION, Text.NEW_LINE));
        textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Authors: ", TextColors.WHITE, Reference.AUTHORS, Text.NEW_LINE));
        textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Source: ", TextColors.BLUE, getURLTextAction(Reference.SOURCE), Reference.SOURCE, Text.NEW_LINE));
        textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Website: ", TextColors.BLUE, getURLTextAction(Reference.WEBSITE), Reference.WEBSITE));
        return textBuilder.build();
    }
    
    public static TextAction<?> getURLTextAction(String url) {
        try {
            return TextActions.openUrl(new URL(url));
        } catch (MalformedURLException ex) {
            return TextActions.suggestCommand(url);
        }
    }
    
    public static Text convertColor(String string) {
        return TextSerializers.FORMATTING_CODE.deserialize(string);
    }
    */
    
    public static String formatUnit(long unit, String singular, String plural) {
        if (unit == 1) {
            return singular;
        }
        
        return plural;
    }
    
    public static boolean saveCrashReport(CrashReport crashReport) {
        Path crashPath = SpongeLaunch.getGameDir()
                .resolve("crash-reports")
                .resolve("crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt");
        
        if (crashReport.saveToFile(crashPath.toFile())) {
            Sledgehammer.getInstance().getLogger().info("This crash report has been saved to: {}", crashPath);
            return true;
        } else {
            Sledgehammer.getInstance().getLogger().error("We were unable to save this crash report to disk.");
            return false;
        }
    }
    
    /*
    @Deprecated
    public static CatalogType getRootType(Entity entity) {
        if (entity instanceof Item) {
            return ((Item) entity).getItemType();
        }
        
        return entity.getType();
    }
    */
    
    public static String getRootId(Entity entity) {
        if (SledgehammerLaunch.isSpongeRegistered()) {
            if (entity instanceof EntityItem) {
                return ((ItemType) ((EntityItem) entity).getItem().getItem()).getId();
            }
            
            return ((EntityType) entity).getId();
        }
        
        if (SledgehammerLaunch.isForgeRegistered()) {
            if (entity instanceof EntityItem) {
                ResourceLocation resourceLocation = Item.REGISTRY.getNameForObject(((EntityItem) entity).getItem().getItem());
                if (resourceLocation != null) {
                    return resourceLocation.toString();
                }
            }
            
            return EntityList.getEntityString(entity);
        }
        
        return "Unknown";
    }
    
    /**
     * Removes non-printable characters (excluding new line and carriage return) in the provided {@link java.lang.String String}.
     *
     * @param string The {@link java.lang.String String} to filter.
     * @return The filtered {@link java.lang.String String}.
     */
    public static String filter(String string) {
        return StringUtils.replaceAll(string, "[^\\x20-\\x7E\\x0A\\x0D]", "");
    }
    
    public static boolean containsIgnoreCase(Collection<String> list, String targetString) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        
        for (String string : list) {
            if (StringUtils.equalsIgnoreCase(string, targetString)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static Optional<Integer> parseInteger(String string) {
        try {
            return Optional.of(Integer.parseInt(string));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }
    
    public static boolean isClassStackFrame(String className, StackTraceElement[] stackTraceElements) {
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (StringUtils.equals(stackTraceElement.getClassName(), className)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static <T> T cast(Object object, Class<T> type) {
        return type.cast(object);
    }
    
    public static <T> Optional<T> newInstance(Class<T> type) {
        try {
            return Optional.of(type.newInstance());
        } catch (Throwable ex) {
            return Optional.empty();
        }
    }
}