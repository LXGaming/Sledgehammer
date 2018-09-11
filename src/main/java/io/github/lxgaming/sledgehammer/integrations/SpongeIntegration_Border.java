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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SpongeIntegration_Border extends AbstractIntegration {
    
    public SpongeIntegration_Border() {
        addDependency("sponge");
    }
    
    @Override
    public boolean prepareIntegration() {
        Sponge.getEventManager().registerListeners(Sledgehammer.getInstance().getPluginContainer(), this);
        return true;
    }
    
    @Listener(order = Order.LAST)
    public void onMoveEntity(MoveEntityEvent event, @Root Player player) {
        // https://github.com/NucleusPowered/Nucleus/blob/c55d92191741214b09a6952ca853723c27f640d0/src/main/java/io/github/nucleuspowered/nucleus/Util.java#L393
        Location<World> location = event.getToTransform().getLocation();
        World world = location.getExtent();
        long radius = (long) Math.floor(world.getWorldBorder().getDiameter() / 2.0) + 1L;
        Vector3d displacement = location.getPosition().sub(world.getWorldBorder().getCenter()).abs();
        if (displacement.getX() > radius || displacement.getZ() > radius) {
            event.setCancelled(true);
            Sledgehammer.getInstance().debugMessage("Move denied for {} ({})", player.getName(), player.getUniqueId());
        }
    }
}