/*
 * Copyright 2023 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.embers.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import teamroots.embers.tileentity.TileEntityFurnaceTop;

import java.util.List;

@Mixin(value = TileEntityFurnaceTop.class, remap = false)
public abstract class TileEntityFurnaceTopMixin {
    
    @Redirect(
            method = "func_73660_a",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;func_72872_a(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;"
            )
    )
    private <T extends Entity> List<T> onGetEntitiesWithinAABB(World world, Class<T> entityClass, AxisAlignedBB axisAlignedBB) {
        return world.getEntitiesWithinAABB(entityClass, axisAlignedBB, entity -> EntitySelectors.NOT_SPECTATING.apply(entity) && entity.isEntityAlive());
    }
}