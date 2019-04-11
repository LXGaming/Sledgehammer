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
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.GameState;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.trait.BlockTrait;
import org.spongepowered.api.block.trait.BooleanTrait;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.TickBlockEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class MistIntegration extends AbstractIntegration {
    
    public MistIntegration() {
        addDependency("forge");
        addDependency("mist");
        setState(GameState.INITIALIZATION);
    }
    
    @Override
    public void execute() {
        Sponge.getEventManager().registerListeners(SledgehammerPlatform.getInstance().getContainer(), this);
    }
    
    @Listener(order = Order.LATE)
    public void onTickBlock(TickBlockEvent.Scheduled event, @Getter("getTargetBlock") BlockSnapshot blockSnapshot) {
        if (!StringUtils.equals(blockSnapshot.getState().getType().getId(), "mist:portal")) {
            return;
        }
        
        Location<World> location = blockSnapshot.getLocation().orElse(null);
        if (location != null && isValidPortalBlock(location.getBlockRelative(Direction.DOWN).getBlock()) && isValidPortalBlock(location.getBlockRelative(Direction.UP).getBlock())) {
            event.setCancelled(true);
            Sledgehammer.getInstance().debugMessage("Portal Tick Cancelled {}", location.getBlockPosition().toString());
        }
    }
    
    @Listener(order = Order.LATE)
    public void onChangeBlockBreak(ChangeBlockEvent.Break event) {
        for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
            if (!isValidPortalBlock(transaction.getOriginal().getState())) {
                continue;
            }
            
            BlockTrait blockTrait = transaction.getOriginal().getState().getTrait("isup").orElse(null);
            if (!(blockTrait instanceof BooleanTrait)) {
                continue;
            }
            
            if (transaction.getOriginal().getState().getTraitValue((BooleanTrait) blockTrait).orElse(false)) {
                transaction.getOriginal().getLocation().ifPresent(location -> location.getExtent().addScheduledUpdate(location.getBlockRelative(Direction.DOWN).getBlockPosition(), 1, 1));
            } else {
                transaction.getOriginal().getLocation().ifPresent(location -> location.getExtent().addScheduledUpdate(location.getBlockRelative(Direction.UP).getBlockPosition(), 1, 1));
            }
        }
    }
    
    @Listener(order = Order.LATE)
    public void onChangeBlockPlace(ChangeBlockEvent.Place event) {
        for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
            if (!StringUtils.equals(transaction.getFinal().getState().getType().getId(), "minecraft:gold_block")) {
                continue;
            }
            
            Location<World> location = transaction.getFinal().getLocation().orElse(null);
            if (location != null && isValidPortalBlock(location.getBlockRelative(Direction.DOWN).getBlock()) && isValidPortalBlock(location.getBlockRelative(Direction.UP).getBlock())) {
                // Only the Upper Portal Stone Block creates the portal on updateTick
                location.getExtent().addScheduledUpdate(location.getBlockRelative(Direction.UP).getBlockPosition(), 1, 1);
            }
        }
    }
    
    private boolean isValidPortalBlock(BlockState blockState) {
        if (!StringUtils.equals(blockState.getType().getId(), "mist:portal_base") && !StringUtils.equals(blockState.getType().getId(), "mist:portal_work")) {
            return false;
        }
        
        return blockState.getTrait("isnew").isPresent() && blockState.getTrait("isup").isPresent();
    }
}