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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Collection;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
    
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
    
    public static String convertColor(String string) {
        return string.replaceAll("(?i)\u0026([0-9A-FK-OR])", "\u00A7$1");
    }
    
    /**
     * Removes non-printable characters (excluding new line and carriage return) in the provided {@link java.lang.String String}.
     *
     * @param string The {@link java.lang.String String} to filter.
     * @return The filtered {@link java.lang.String String}.
     */
    public static String filter(String string) {
        return string.replaceAll("[^\\x20-\\x7E\\x0A\\x0D]", "");
    }
    
    public static String toString(Object object) {
        if (object != null) {
            return object.toString();
        }
        
        return "null";
    }
    
    public static String toString(JsonElement jsonElement) throws UnsupportedOperationException {
        if (jsonElement instanceof JsonPrimitive) {
            return jsonElement.getAsString();
        }
        
        if (jsonElement instanceof JsonArray) {
            return toString((JsonArray) jsonElement);
        }
        
        throw new UnsupportedOperationException(String.format("%s is not supported", jsonElement.getClass().getSimpleName()));
    }
    
    public static String toString(JsonArray jsonArray) throws UnsupportedOperationException {
        StringBuilder stringBuilder = new StringBuilder();
        for (JsonElement jsonElement : jsonArray) {
            if (!(jsonElement instanceof JsonPrimitive)) {
                throw new UnsupportedOperationException(String.format("%s is not supported inside a JsonArray", jsonElement.getClass().getSimpleName()));
            }
            
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            
            stringBuilder.append(jsonElement.getAsString());
        }
        
        return stringBuilder.toString();
    }
}