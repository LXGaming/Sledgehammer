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

package io.github.lxgaming.sledgehammer.mixin.core.entity;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.SpongeMixinCategory;
import io.github.lxgaming.sledgehammer.util.Broadcast;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ChatType;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class, priority = 1337)
public abstract class EntityMixin_Teleport {
    
    @Inject(method = "setPortal", at = @At(value = "HEAD"), cancellable = true)
    private void onSetPortal(CallbackInfo callbackInfo) {
        if (sledgehammer$shouldRemove(Toolbox.cast(this, Entity.class))) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = "changeDimension(I)Lnet/minecraft/entity/Entity;", at = @At(value = "HEAD"))
    private void onChangeDimension(int dimension, CallbackInfoReturnable<Entity> callbackInfoReturnable) {
        Entity entity = Toolbox.cast(this, Entity.class);
        if (sledgehammer$shouldRemove(entity)) {
            entity.setDead();
            
            Sledgehammer.getInstance().getConfig().map(Config::getGeneralCategory).ifPresent(generalCategory -> {
                String message = generalCategory.getMessageCategory().getItemTeleport();
                if (StringUtils.isBlank(message)) {
                    return;
                }
                
                Broadcast broadcast = Broadcast.builder()
                        .message(Toolbox.convertColor(message.replace("[ID]", Toolbox.getRootId(Toolbox.cast(this, Entity.class)))))
                        .type(ChatType.CHAT)
                        .build();
                
                if (generalCategory.isDebug()) {
                    broadcast.sendMessage(SledgehammerPlatform.getInstance().getServer());
                }
                
                Toolbox.cast(entity, org.spongepowered.api.entity.Entity.class).getCreator()
                        .flatMap(Sponge.getServer()::getPlayer)
                        .map(EntityPlayer.class::cast)
                        .ifPresent(broadcast::sendMessage);
            });
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