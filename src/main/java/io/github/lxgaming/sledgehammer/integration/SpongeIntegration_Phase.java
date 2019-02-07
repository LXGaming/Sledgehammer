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

package io.github.lxgaming.sledgehammer.integration;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.common.event.tracking.PhaseContext;
import org.spongepowered.common.event.tracking.PhaseData;
import org.spongepowered.common.event.tracking.PhaseTracker;
import org.spongepowered.common.event.tracking.context.CapturedBlockEntitySpawnSupplier;

import java.lang.reflect.Field;

public class SpongeIntegration_Phase extends AbstractIntegration {
    
    public SpongeIntegration_Phase() {
        addDependency("sponge");
    }
    
    @Override
    public void execute() {
        Sponge.getEventManager().registerListeners(Sledgehammer.getInstance().getPluginContainer(), this);
    }
    
    @Listener(order = Order.LAST)
    public void onMoveEntityTeleport(MoveEntityEvent.Teleport event, @Root Player player) {
        PhaseData currentPhase = PhaseTracker.getInstance().getCurrentPhaseData();
        PhaseContext<?> context = currentPhase.context;
        
        try {
            Field field = PhaseContext.class.getDeclaredField("blockEntitySpawnSupplier");
            field.setAccessible(true);
            field.set(context, new CapturedBlockEntitySpawnSupplier());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}