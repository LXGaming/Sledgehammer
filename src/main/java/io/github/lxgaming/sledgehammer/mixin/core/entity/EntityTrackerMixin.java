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

package io.github.lxgaming.sledgehammer.mixin.core.entity;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.util.PrettyPrinterProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.util.PrettyPrinter;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(value = EntityTracker.class)
public abstract class EntityTrackerMixin {
    
    private final AtomicBoolean sledgehammer$lock = new AtomicBoolean(false);
    
    @Inject(
            method = "sendLeashedEntitiesInChunk",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onSendLeashedEntitiesInChunkPre(CallbackInfo callbackInfo) {
        if (!sledgehammer$lock.compareAndSet(false, true)) {
            Sledgehammer.getInstance().getLogger().error("Unexpected lock state: true");
        }
    }
    
    @Inject(
            method = "sendLeashedEntitiesInChunk",
            at = @At(
                    value = "RETURN"
            )
    )
    private void onSendLeashedEntitiesInChunkPost(CallbackInfo callbackInfo) {
        if (!sledgehammer$lock.compareAndSet(true, false)) {
            Sledgehammer.getInstance().getLogger().error("Unexpected lock state: false");
        }
    }
    
    @Inject(
            method = "track(Lnet/minecraft/entity/Entity;IIZ)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onTrack(Entity entity, int trackingRange, final int updateFrequency, boolean sendVelocityUpdates, CallbackInfo callbackInfo) {
        if (sledgehammer$lock.get()) {
            PrettyPrinterProxy.error(new PrettyPrinter()
                    .add("Tried to track entity while locked").centre().hr()
                    .add("StackTrace:")
                    .add(new Exception(String.format("%s v%s", Sledgehammer.NAME, Sledgehammer.VERSION))));
            
            callbackInfo.cancel();
        }
    }
    
    @Inject(
            method = "untrack(Lnet/minecraft/entity/Entity;)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onUntrack(Entity entity, CallbackInfo callbackInfo) {
        if (sledgehammer$lock.get()) {
            PrettyPrinterProxy.error(new PrettyPrinter()
                    .add("Tried to untrack entity while locked").centre().hr()
                    .add("StackTrace:")
                    .add(new Exception(String.format("%s v%s", Sledgehammer.NAME, Sledgehammer.VERSION))));
            
            callbackInfo.cancel();
        }
    }
}