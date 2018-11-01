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

import com.flowpowered.math.vector.Vector3d;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.MessageCategory;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Map;
import java.util.UUID;

public class SpongeIntegration_Border extends AbstractIntegration {
    
    private final Map<UUID, Long> cooldown = Toolbox.newHashMap();
    
    public SpongeIntegration_Border() {
        addDependency("sponge");
    }
    
    @Override
    public boolean prepareIntegration() {
        Sponge.getEventManager().registerListeners(Sledgehammer.getInstance().getPluginContainer(), this);
        return true;
    }
    
    @Listener(order = Order.LATE)
    public void onClientConnectionDisconnect(ClientConnectionEvent.Disconnect event) {
        this.cooldown.remove(event.getTargetEntity().getUniqueId());
    }
    
    @Listener(order = Order.EARLY)
    public void onClientConnectionJoin(ClientConnectionEvent.Join event) {
        this.cooldown.put(event.getTargetEntity().getUniqueId(), System.currentTimeMillis());
    }
    
    @Listener(order = Order.LATE)
    public void onMoveEntity(MoveEntityEvent event, @Root Player player) {
        // https://github.com/NucleusPowered/Nucleus/blob/c55d92191741214b09a6952ca853723c27f640d0/src/main/java/io/github/nucleuspowered/nucleus/Util.java#L393
        Location<World> location = event.getToTransform().getLocation();
        World world = location.getExtent();
        long radius = (long) Math.floor(world.getWorldBorder().getDiameter() / 2.0) + 1L;
        Vector3d displacement = location.getPosition().sub(world.getWorldBorder().getCenter()).abs();
        if (displacement.getX() > radius || displacement.getZ() > radius) {
            event.setCancelled(true);
            
            long currentTime = System.currentTimeMillis();
            if (currentTime - this.cooldown.getOrDefault(player.getUniqueId(), 0L) < 5000L) {
                return;
            }
            
            this.cooldown.put(player.getUniqueId(), currentTime);
            Sledgehammer.getInstance().debugMessage("Movement denied for {} ({})", player.getName(), player.getUniqueId());
            Sledgehammer.getInstance().getConfig().map(Config::getMessageCategory).map(MessageCategory::getMoveOutsideBorder).filter(StringUtils::isNotBlank).ifPresent(message -> {
                player.sendMessage(Text.of(Toolbox.getTextPrefix(), Toolbox.convertColor(message)));
            });
        }
    }
}