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

package io.github.lxgaming.sledgehammer.integrations;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import org.h2.util.StringUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.TickBlockEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class MistIntegration extends AbstractIntegration {
    
    public MistIntegration() {
        addDependency("forge");
        addDependency("mist");
    }
    
    @Override
    public boolean prepareIntegration() {
        Sponge.getEventManager().registerListeners(Sledgehammer.getInstance().getPluginContainer(), this);
        return true;
    }
    
    @Listener(order = Order.LATE)
    public void onTickBlock(TickBlockEvent.Scheduled event, @Getter("getTargetBlock") BlockSnapshot blockSnapshot) {
        if (event.isCancelled()) {
            return;
        }
        
        Location<World> location = event.getTargetBlock().getLocation().orElse(null);
        if (location == null || !StringUtils.equals(event.getTargetBlock().getState().getId(), "mist:portal")) {
            return;
        }
        
        BlockState blockDown = location.getBlockRelative(Direction.DOWN).getBlock();
        if (!StringUtils.equals(blockDown.getId(), "mist:portal_base") && !StringUtils.equals(blockDown.getId(), "mist:portal_work")) {
            return;
        }
        
        BlockState blockUp = location.getBlockRelative(Direction.UP).getBlock();
        if (!StringUtils.equals(blockUp.getId(), "mist:portal_base") && !StringUtils.equals(blockUp.getId(), "mist:portal_work")) {
            return;
        }
        
        event.setCancelled(true);
    }
}