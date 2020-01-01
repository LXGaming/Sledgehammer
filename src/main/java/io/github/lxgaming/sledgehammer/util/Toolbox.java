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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class Toolbox {
    
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
    
    public static String getClassSimpleName(Class<?> type) {
        if (type.getEnclosingClass() != null) {
            return getClassSimpleName(type.getEnclosingClass()) + "." + type.getSimpleName();
        }
        
        return type.getSimpleName();
    }
    
    public static <T> T cast(Object object, Class<? extends T> type) {
        return type.cast(object);
    }
    
    public static <T> T newInstance(Class<? extends T> type) {
        try {
            return type.newInstance();
        } catch (Throwable ex) {
            return null;
        }
    }
}