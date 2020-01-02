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

public enum Locale {
    
    COMMAND_BASE("command.base"),
    COMMAND_EXCEPTION("command.exception"),
    COMMAND_INVALID_ARGUMENTS("command.invalid_arguments"),
    COMMAND_NO_PERMISSION("command.no_permission"),
    COMMAND_NOT_FOUND("command.not_found"),
    
    COMMAND_DEBUG_DISABLE("command.debug.disable"),
    COMMAND_DEBUG_ENABLE("command.debug.enable"),
    
    COMMAND_RELOAD_FAILURE("command.reload.failure"),
    COMMAND_RELOAD_SUCCESS("command.reload.success"),
    
    CONFIGURATION_ERROR("configuration.error"),
    
    GENERAL_INFORMATION("general.information"),
    GENERAL_PREFIX("general.prefix"),
    
    TRANSLATION_INVALID("translation.invalid"),
    
    UNKNOWN("unknown");
    
    private final String key;
    
    Locale(String key) {
        this.key = key;
    }
    
    public String getKey() {
        return key;
    }
    
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}