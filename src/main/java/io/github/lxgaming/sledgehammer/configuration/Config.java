/*
 * Copyright 2018 Alex Thomson
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

package io.github.lxgaming.sledgehammer.configuration;

import io.github.lxgaming.sledgehammer.configuration.category.GeneralCategory;
import io.github.lxgaming.sledgehammer.configuration.category.integration.ClientIntegrationCategory;
import io.github.lxgaming.sledgehammer.configuration.category.integration.CommonIntegrationCategory;
import io.github.lxgaming.sledgehammer.configuration.category.integration.ServerIntegrationCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ClientMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.CommonMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ServerMixinCategory;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Config {
    
    @Setting(value = "general")
    private GeneralCategory generalCategory = new GeneralCategory();
    
    @Setting(value = "integration-client")
    private ClientIntegrationCategory clientIntegrationCategory = new ClientIntegrationCategory();
    
    @Setting(value = "integration-common")
    private CommonIntegrationCategory commonIntegrationCategory = new CommonIntegrationCategory();
    
    @Setting(value = "integration-server")
    private ServerIntegrationCategory serverIntegrationCategory = new ServerIntegrationCategory();
    
    @Setting(value = "mixin-client")
    private ClientMixinCategory clientMixinCategory = new ClientMixinCategory();
    
    @Setting(value = "mixin-common")
    private CommonMixinCategory commonMixinCategory = new CommonMixinCategory();
    
    @Setting(value = "mixin-server")
    private ServerMixinCategory serverMixinCategory = new ServerMixinCategory();
    
    public GeneralCategory getGeneralCategory() {
        return generalCategory;
    }
    
    public ClientIntegrationCategory getClientIntegrationCategory() {
        return clientIntegrationCategory;
    }
    
    public CommonIntegrationCategory getCommonIntegrationCategory() {
        return commonIntegrationCategory;
    }
    
    public ServerIntegrationCategory getServerIntegrationCategory() {
        return serverIntegrationCategory;
    }
    
    public ClientMixinCategory getClientMixinCategory() {
        return clientMixinCategory;
    }
    
    public CommonMixinCategory getCommonMixinCategory() {
        return commonMixinCategory;
    }
    
    public ServerMixinCategory getServerMixinCategory() {
        return serverMixinCategory;
    }
}