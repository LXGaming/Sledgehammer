/*
 * Copyright 2020 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.armorunder.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jwaresoftware.mcmods.armorunder.item.GooPak;
import org.jwaresoftware.mcmods.lib.capability.ITemperatureChangeable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GooPak.class, remap = false)
public abstract class GooPakMixin implements ITemperatureChangeable {
    
    @Redirect(
            method = "func_77663_a",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/jwaresoftware/mcmods/armorunder/item/GooPak;onUpdateTickBonusTemperature(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;IZ)Z"
            )
    )
    private boolean onUpdateBonusTemperature(GooPak gooPak, ItemStack itemStack, World world, Entity entity, int slotindex, boolean selected) {
        try {
            return onUpdateTickBonusTemperature(itemStack, world, entity, slotindex, selected);
        } catch (NullPointerException ex) {
            return true;
        }
    }
}