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

package io.github.lxgaming.sledgehammer.mixin.core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Minecraft.class)
public abstract class MinecraftMixin_DropItem {
    
    @Redirect(
            method = "processKeyBinds",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/player/ClientPlayerEntity;dropItem(Z)Lnet/minecraft/entity/item/ItemEntity;"
            )
    )
    private ItemEntity onDropItem(ClientPlayerEntity player, boolean dropAll) {
        if (player.openContainer == null) {
            return player.dropItem(dropAll);
        }
        
        return null;
    }
}