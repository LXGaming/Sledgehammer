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

package io.github.lxgaming.sledgehammer.integration.biomesoplenty;

import biomesoplenty.common.entities.item.EntityBOPBoat;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import io.github.lxgaming.sledgehammer.integration.Integration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BiomesOPlentyIntegration extends Integration {
    
    @Override
    public boolean prepare() {
        addDependency("forge");
        addDependency("biomesoplenty");
        state(SledgehammerPlatform.State.SERVER_STARTING);
        return true;
    }
    
    @Override
    public void execute() throws Exception {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityBOPBoat) {
            event.setCanceled(true);
            Sledgehammer.getInstance().debug("Biomes O' Plenty Boat was blocked");
        }
    }
}