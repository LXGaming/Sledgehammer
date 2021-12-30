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

import io.github.lxgaming.sledgehammer.bridge.entity.EntityLivingBridge;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/* Ideally this would be implemented with pure accessors, but they seemed to be causing issues. */
@Mixin(value = EntityLiving.class)
public abstract class EntityLivingMixin_Despawning extends EntityLivingBase implements EntityLivingBridge {
    
    @Shadow
    protected abstract void despawnEntity();
    
    public EntityLivingMixin_Despawning(World worldIn) {
        super(worldIn);
    }
    
    public void sledgehammer$incrementIdleTime() {
        this.idleTime++;
    }
    
    public void sledgehammer$despawnEntity() {
        this.despawnEntity();
    }
}