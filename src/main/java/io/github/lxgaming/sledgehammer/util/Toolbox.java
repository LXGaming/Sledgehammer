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
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public class Toolbox {
    
    public static ITextComponent getTextPrefix() {
        return Text.of(
                TextFormatting.BLUE, TextFormatting.BOLD,
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, getPluginInformation()),
                "[" + Reference.NAME + "]", " "
        );
    }
    
    public static ITextComponent getPluginInformation() {
        return Text.of(
                TextFormatting.BLUE, TextFormatting.BOLD, Reference.NAME, Text.NEW_LINE,
                TextFormatting.DARK_GRAY, "    Version: ", TextFormatting.WHITE, Reference.VERSION, Text.NEW_LINE,
                TextFormatting.DARK_GRAY, "    Authors: ", TextFormatting.WHITE, Reference.AUTHORS, Text.NEW_LINE,
                TextFormatting.DARK_GRAY, "    Source: ", TextFormatting.BLUE, createURLEvent(Reference.SOURCE), Reference.SOURCE, Text.NEW_LINE,
                TextFormatting.DARK_GRAY, "    Website: ", TextFormatting.BLUE, createURLEvent(Reference.WEBSITE), Reference.WEBSITE
        );
    }
    
    public static ClickEvent createURLEvent(String url) {
        return new ClickEvent(ClickEvent.Action.OPEN_URL, url);
    }
    
    public static String convertColor(String string) {
        return string.replaceAll("(?i)\u0026([0-9A-FK-OR])", "\u00A7$1");
    }
    
    public static String formatUnit(long unit, String singular, String plural) {
        if (unit == 1) {
            return singular;
        }
        
        return plural;
    }
    
    public static boolean saveCrashReport(CrashReport crashReport) {
        Path crashPath = Paths.get("crash-reports")
                .resolve("crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt");
        
        if (crashReport.saveToFile(crashPath.toFile())) {
            Sledgehammer.getInstance().getLogger().info("This crash report has been saved to: {}", crashPath);
            return true;
        } else {
            Sledgehammer.getInstance().getLogger().error("We were unable to save this crash report to disk.");
            return false;
        }
    }
    
    public static String getRootId(Entity entity) {
        if (entity instanceof EntityItem) {
            ItemStack itemStack = ((EntityItem) entity).getItem();
            if (!itemStack.isEmpty()) {
                return getResourceLocation(itemStack.getItem()).map(ResourceLocation::toString).orElse("Unknown");
            }
        }
        
        return getResourceLocation(entity).map(ResourceLocation::toString).orElse("Unknown");
    }
    
    public static Optional<ResourceLocation> getResourceLocation(Object object) {
        if (object instanceof Block) {
            return Optional.of(Block.REGISTRY.getNameForObject((Block) object));
        }
        
        if (object instanceof Entity) {
            return Optional.ofNullable(EntityList.getKey((Entity) object));
        }
        
        if (object instanceof Item) {
            return Optional.ofNullable(Item.REGISTRY.getNameForObject((Item) object));
        }
        
        return Optional.empty();
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
    
    public static String getClassSimpleName(Class<?> clazz) {
        if (clazz.getEnclosingClass() != null) {
            return getClassSimpleName(clazz.getEnclosingClass()) + "." + clazz.getSimpleName();
        }
        
        return clazz.getSimpleName();
    }
    
    public static <T> T cast(Object object, Class<? extends T> type) {
        return type.cast(object);
    }
    
    public static <T> Optional<T> newInstance(Class<? extends T> type) {
        try {
            return Optional.of(type.newInstance());
        } catch (Throwable ex) {
            return Optional.empty();
        }
    }
}