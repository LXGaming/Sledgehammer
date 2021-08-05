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

package io.github.lxgaming.sledgehammer.mixin.core.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/* We use a lower priority than Sponge's 1000 to make sure its mixin runs after ours */
/* The mixins in this class also have require = 0 to allow loading on Sponge */
@Mixin(value = EntityLiving.class, priority = 900)
public abstract class EntityLivingMixin_DespawnRanges extends EntityLivingBase {
    
    public EntityLivingMixin_DespawnRanges(World worldIn) {
        super(worldIn);
    }
    
    /**
     * @return The render distance of the server, as shown in server.properties.
     */
    private int sledgehammer$getRenderDistance() {
        if (this.world.isRemote) {
            return 10;
        }
        MinecraftServer server = this.world.getMinecraftServer();
        if (server != null) {
            return server.getPlayerList().getViewDistance();
        } else {
            return 10;
        }
    }
    
    @ModifyConstant(
            method = "despawnEntity",
            constant = @Constant(
                    doubleValue = 16384.0D
            ),
            require = 0
    )
    private double adjustHardDespawnRange(final double value) {
        /* Do nothing if client-side */
        if (this.world.isRemote) {
            return value;
        }
        int rd = sledgehammer$getRenderDistance();
        if (rd < 10) {
            /* Despawn mobs one chunk before the edge of the render distance, but never closer than 24 blocks */
            return Math.pow(Math.max(24, (rd - 1) * 16), 2);
        }
        /* Otherwise, use the default value */
        return value;
    }
    
    @ModifyConstant(
            method = "despawnEntity",
            constant = @Constant(
                    doubleValue = 1024.0D
            ),
            expect = 2,
            require = 0
    )
    private double getSoftDespawnRange(final double value) {
        /* Do nothing if client-side */
        if (this.world.isRemote) {
            return value;
        }
        /* drop to 24 blocks if render distance is too low */
        if (sledgehammer$getRenderDistance() < 6) {
            return Math.pow(24, 2);
        }
        return value;
    }
}