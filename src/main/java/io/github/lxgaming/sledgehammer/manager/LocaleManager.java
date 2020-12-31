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

package io.github.lxgaming.sledgehammer.manager;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.GeneralCategory;
import io.github.lxgaming.sledgehammer.util.Locale;
import io.github.lxgaming.sledgehammer.util.StringUtils;
import io.github.lxgaming.sledgehammer.util.text.adapter.TextAdapter;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public final class LocaleManager {
    
    public static final String PLACEHOLDER_START = "{";
    public static final String PLACEHOLDER_END = "}";
    private static final Map<String, String> LOCALE = Maps.newHashMap();
    
    public static void prepare() {
        if (loadLocale(GeneralCategory.DEFAULT_LOCALE)) {
            Sledgehammer.getInstance().getLogger().info("Loaded default locale");
        } else {
            Sledgehammer.getInstance().getLogger().warn("Failed to load default locale");
        }
        
        GeneralCategory generalCategory = Sledgehammer.getInstance().getConfig().map(Config::getGeneralCategory).orElseThrow(NullPointerException::new);
        if (!generalCategory.getLocale().equals(GeneralCategory.DEFAULT_LOCALE)) {
            if (loadLocale(generalCategory.getLocale())) {
                Sledgehammer.getInstance().getLogger().info("Loaded {} locale", generalCategory.getLocale());
            } else {
                Sledgehammer.getInstance().getLogger().warn("Failed to load {} locale", generalCategory.getLocale());
            }
        }
        
        for (Map.Entry<String, List<String>> entry : generalCategory.getLocaleOverrides().entrySet()) {
            LOCALE.put(entry.getKey(), String.join("\n", entry.getValue()));
        }
    }
    
    public static ITextComponent serialize(Locale locale, Object... arguments) {
        return serialize(locale.getKey(), arguments);
    }
    
    public static ITextComponent serialize(String key, Object... arguments) {
        String translation = getTranslation(key);
        if (translation == null) {
            return new StringTextComponent("Failed to translate message").mergeStyle(TextFormatting.RED);
        }
        
        int matches = StringUtils.countMatches(translation, PLACEHOLDER_START + PLACEHOLDER_END);
        if (matches != arguments.length) {
            Sledgehammer.getInstance().getLogger().warn("Incorrect Arguments for {}. Expected {}, got {}", key, matches, arguments.length);
        }
        
        String format = format(translation, arguments);
        if (StringUtils.isEmpty(format)) {
            return new StringTextComponent("Failed to format message").mergeStyle(TextFormatting.RED);
        }
        
        return TextAdapter.serializeLegacyWithLinks(format);
    }
    
    private static String format(String format, Object... arguments) {
        StringBuilder stringBuilder = new StringBuilder(format);
        int startIndex = 0;
        int endIndex = 0;
        int argumentIndex = 0;
        while ((startIndex = stringBuilder.indexOf(PLACEHOLDER_START, startIndex)) != -1 && ((endIndex = stringBuilder.indexOf(PLACEHOLDER_END, endIndex)) != -1)) {
            int length = stringBuilder.length();
            
            if ((endIndex - startIndex) == PLACEHOLDER_START.length()) {
                if (argumentIndex >= arguments.length) {
                    endIndex += PLACEHOLDER_END.length();
                    startIndex = endIndex;
                    continue;
                }
                
                stringBuilder.replace(startIndex, endIndex + PLACEHOLDER_END.length(), StringUtils.toString(arguments[argumentIndex]));
                argumentIndex += 1;
            } else {
                String key = stringBuilder.substring(startIndex + PLACEHOLDER_START.length(), endIndex);
                String value = getTranslation(key);
                if (value != null) {
                    stringBuilder.replace(startIndex, endIndex + PLACEHOLDER_END.length(), value);
                } else {
                    stringBuilder.delete(startIndex + PLACEHOLDER_START.length(), endIndex);
                }
            }
            
            int difference = stringBuilder.length() - length;
            startIndex += difference;
            endIndex += difference;
        }
        
        return stringBuilder.toString();
    }
    
    private static boolean loadLocale(String name) {
        Map<String, String> locale = deserializeLocaleFile(String.format("/assets/%s/locale/%s.json", Sledgehammer.ID, name));
        if (locale != null) {
            LOCALE.putAll(locale);
            return true;
        }
        
        return false;
    }
    
    private static Map<String, String> deserializeLocaleFile(String name) {
        InputStream inputStream = LocaleManager.class.getResourceAsStream(name);
        if (inputStream == null) {
            Sledgehammer.getInstance().getLogger().warn("Resource {} doesn't exist", name);
            return null;
        }
        
        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            JsonObject jsonObject = JSONUtils.fromJson(reader);
            
            // noinspection ConstantConditions
            if (jsonObject == null) {
                throw new JsonParseException(String.format("Failed to parse locale %s", name));
            }
            
            Map<String, String> translations = Maps.newHashMap();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                translations.put(entry.getKey(), StringUtils.toString(entry.getValue()));
            }
            
            return translations;
        } catch (Exception ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error while deserializing {}", name, ex);
            return null;
        }
    }
    
    private static String getTranslation(String key) {
        String translation = LOCALE.get(key);
        if (translation == null) {
            Sledgehammer.getInstance().getLogger().warn("Missing translation for {}", key);
            return null;
        }
        
        return translation;
    }
}