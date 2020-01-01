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

package io.github.lxgaming.sledgehammer.mixin.forge.entity;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.SpongeMixinCategory;
import io.github.lxgaming.sledgehammer.util.Locale;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import io.github.lxgaming.sledgehammer.util.text.adapter.LocaleAdapter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ITeleporter;
import org.spongepowered.api.Sponge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class, priority = 1337)
public abstract class EntityMixin_Teleport {
    
    @Inject(
            method = "changeDimension(ILnet/minecraftforge/common/util/ITeleporter;)Lnet/minecraft/entity/Entity;",
            at = @At(
                    value = "HEAD"
            ),
            remap = false
    )
    private void onChangeDimension(int dimension, ITeleporter teleporter, CallbackInfoReturnable<Entity> callbackInfoReturnable) {
        Entity entity = Toolbox.cast(this, Entity.class);
        if (sledgehammer$shouldRemove(entity)) {
            entity.setDead();
            
            Toolbox.cast(entity, org.spongepowered.api.entity.Entity.class).getCreator()
                    .flatMap(Sponge.getServer()::getPlayer)
                    .map(EntityPlayer.class::cast)
                    .ifPresent(player -> LocaleAdapter.sendMessage(player, Locale.MESSAGE_ITEM_TELEPORT, Toolbox.getRootId(entity)));
        }
    }
    
    private boolean sledgehammer$shouldRemove(Entity entity) {
        if (!(entity instanceof EntityItem || entity instanceof EntityMinecart) || entity.isDead) {
            return false;
        }
        
        return Sledgehammer.getInstance().getConfig()
                .map(Config::getMixinCategory)
                .map(MixinCategory::getSpongeMixinCategory)
                .map(SpongeMixinCategory::getItemTeleportWhitelist)
                .map(list -> !list.contains(Toolbox.getRootId(entity))).orElse(false);
    }
}
