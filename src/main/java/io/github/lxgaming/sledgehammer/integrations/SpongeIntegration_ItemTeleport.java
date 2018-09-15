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
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.MessageCategory;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;

public class SpongeIntegration_ItemTeleport extends AbstractIntegration {
    
    public SpongeIntegration_ItemTeleport() {
        addDependency("sponge");
    }
    
    @Override
    public boolean prepareIntegration() {
        Sponge.getEventManager().registerListeners(Sledgehammer.getInstance().getPluginContainer(), this);
        return true;
    }
    
    @Listener(order = Order.LAST)
    public void onMoveEntity(MoveEntityEvent.Teleport event, @Root Item item) {
        if (event.getFromTransform().getExtent() == event.getToTransform().getExtent()) {
            return;
        }
        
        event.setCancelled(true);
        item.remove();
        
        Sledgehammer.getInstance().debugMessage("Item {} removed", item.getItemType().getId());
        Sledgehammer.getInstance().getConfig().map(Config::getMessageCategory).map(MessageCategory::getItemTeleport).filter(StringUtils::isNotBlank).ifPresent(message -> {
            item.getCreator().flatMap(Sponge.getServer()::getPlayer).ifPresent(player -> {
                player.sendMessage(Text.of(Toolbox.getTextPrefix(), Toolbox.convertColor(message.replace("[ITEM]", item.getItemType().getId()))));
            });
        });
    }
}