/*
 * Copyright 2020 Alex Thomson
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

package io.github.lxgaming.sledgehammer.configuration.category.integration;

import com.google.common.collect.Lists;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class ForgeIntegrationCategory {
    
    @Setting(value = "blacklisted-recipe-items", comment = "Removes recipes which output blacklisted items")
    private List<String> blacklistedRecipeItems = Lists.newArrayList();
    
    @Setting(value = "check-permissions", comment = "If 'true', forces permission check for OP based commands")
    private boolean checkPermissions = false;
    
    @Setting(value = "package-permissions", comment = "If 'true', the package containing the command class will be prepended to the permission check")
    private boolean packagePermissions = false;
    
    public List<String> getBlacklistedRecipeItems() {
        return blacklistedRecipeItems;
    }
    
    public boolean isCheckPermissions() {
        return checkPermissions;
    }
    
    public boolean isPackagePermissions() {
        return packagePermissions;
    }
}