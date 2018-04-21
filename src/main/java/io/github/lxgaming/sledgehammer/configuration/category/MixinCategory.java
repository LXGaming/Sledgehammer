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

package io.github.lxgaming.sledgehammer.configuration.category;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class MixinCategory {
    
    @Setting(value = "ceremony-rain", comment = "Disables weather changing from Totemic")
    private boolean ceremonyRain = false;
    
    @Setting(value = "entity-villager", comment = "Prevents villagers crashing the server")
    private boolean entityVillager = false;
    
    @Setting(value = "traveling-merchant", comment = "Fixes Traveling Merchants from PrimitiveMobs")
    private boolean travelingMerchant = false;
    
    public boolean isCeremonyRain() {
        return ceremonyRain;
    }
    
    public void setCeremonyRain(boolean ceremonyRain) {
        this.ceremonyRain = ceremonyRain;
    }
    
    public boolean isEntityVillager() {
        return entityVillager;
    }
    
    public void setEntityVillager(boolean entityVillager) {
        this.entityVillager = entityVillager;
    }
    
    public boolean isTravelingMerchant() {
        return travelingMerchant;
    }
    
    public void setTravelingMerchant(boolean travelingMerchant) {
        this.travelingMerchant = travelingMerchant;
    }
}