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
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.MessageCategory;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import io.github.lxgaming.sledgehammer.util.Broadcast;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.entity.Entity;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.vehicle.minecart.Minecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.common.interfaces.entity.IMixinEntity;

@Mixin(value = Entity.class, priority = 1337)
public abstract class MixinEntity_Teleport {
    
    @Inject(method = "setPortal", at = @At(value = "HEAD"), cancellable = true)
    private void onSetPortal(CallbackInfo callbackInfo) {
        if (sledgehammer$shouldRemove((IMixinEntity) this)) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = "changeDimension(I)Lnet/minecraft/entity/Entity;", at = @At(value = "HEAD"))
    private void onChangeDimension(int dimension, CallbackInfoReturnable<Entity> callbackInfoReturnable) {
        IMixinEntity mixinEntity = (IMixinEntity) this;
        if (sledgehammer$shouldRemove(mixinEntity)) {
            mixinEntity.remove();
            
            Sledgehammer.getInstance().getConfig().map(Config::getMessageCategory).map(MessageCategory::getItemTeleport).filter(StringUtils::isNotBlank).ifPresent(message -> {
                Broadcast broadcast = Broadcast.builder()
                        .message(Toolbox.convertColor(message.replace("[ID]", Toolbox.getRootId(Toolbox.cast(this, Entity.class)))))
                        .type(Broadcast.Type.CHAT)
                        .build();
                
                if (Sledgehammer.getInstance().getConfig().map(Config::isDebug).orElse(false)) {
                    broadcast.sendMessage(Sponge.getServer().getConsole());
                }
                
                mixinEntity.getCreator().flatMap(Sponge.getServer()::getPlayer).ifPresent(broadcast::sendMessage);
            });
        }
    }
    
    private boolean sledgehammer$shouldRemove(IMixinEntity mixinEntity) {
        if (!(mixinEntity instanceof Item || mixinEntity instanceof Minecart) || mixinEntity.isRemoved()) {
            return false;
        }
        
        return Sledgehammer.getInstance().getConfig()
                .map(Config::getMixinCategory)
                .map(MixinCategory::getItemTeleportWhitelist)
                .map(list -> !list.contains(Toolbox.getRootId(Toolbox.cast(this, Entity.class)))).orElse(false);
    }
}