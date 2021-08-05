/*
 * Copyright 2021 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.core.world;

import io.github.lxgaming.sledgehammer.bridge.entity.EntityLivingBridge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = World.class)
public abstract class WorldMixin_Despawning {
    
    @Redirect(
            method = "updateEntityWithOptionalForce",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/event/ForgeEventFactory;canEntityUpdate(Lnet/minecraft/entity/Entity;)Z",
                    remap = false
            )
    )
    public boolean canEntityUpdate(Entity entity) {
        /*
         * Reaching this point means that the entity is probably going to be blocked from ticking.
         * This will prevent despawning from occuring. Check whether a Forge event overrides this and
         * allows updating anyways. If not, we will run the despawn checks ourselves.
         */
        boolean canUpdate = ForgeEventFactory.canEntityUpdate(entity);
        if (!canUpdate && entity instanceof EntityLiving) {
            EntityLivingBridge helper = (EntityLivingBridge) entity;
            helper.sledgehammer$incrementIdleTime();
            helper.sledgehammer$despawnEntity();
        }
        return canUpdate;
    }
}