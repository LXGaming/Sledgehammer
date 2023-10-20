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

package io.github.lxgaming.sledgehammer.mixin.core.server.management;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.util.PrettyPrinterProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.util.PrettyPrinter;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Mixin(value = PlayerChunkMap.class, priority = 1337)
public abstract class PlayerChunkMapMixin {
    
    private final AtomicBoolean sledgehammer$lock = new AtomicBoolean(false);
    
    @Inject(
            method = "tick",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onTickPre(CallbackInfo callbackInfo) {
        if (!sledgehammer$lock.compareAndSet(false, true)) {
            Sledgehammer.getInstance().getLogger().error("Unexpected lock state: true");
        }
    }
    
    @Inject(
            method = "tick",
            at = @At(
                    value = "RETURN"
            )
    )
    private void onTickPost(CallbackInfo callbackInfo) {
        if (!sledgehammer$lock.compareAndSet(true, false)) {
            Sledgehammer.getInstance().getLogger().error("Unexpected lock state: false");
        }
    }
    
    @Inject(
            method = "entryChanged",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onEntryChanged(PlayerChunkMapEntry entry, CallbackInfo callbackInfo) {
        if (sledgehammer$lock.get()) {
            PrettyPrinterProxy.error(new PrettyPrinter()
                    .add("Tried to mark PlayerChunkMapEntry as dirty while locked").centre().hr()
                    .add("Chunk: %d, %d", entry.getPos().x, entry.getPos().z)
                    .add("Players: %s", entry.getWatchingPlayers().stream().map(EntityPlayer::getName).collect(Collectors.joining(", ")))
                    .add("StackTrace:")
                    .add(new Exception(String.format("%s v%s", Sledgehammer.NAME, Sledgehammer.VERSION))));
            
            callbackInfo.cancel();
        }
    }
    
    @Inject(
            method = "removeEntry",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onRemoveEntry(PlayerChunkMapEntry entry, CallbackInfo callbackInfo) {
        if (sledgehammer$lock.get()) {
            PrettyPrinterProxy.error(new PrettyPrinter()
                    .add("Tried to remove PlayerChunkMapEntry while locked").centre().hr()
                    .add("Chunk: %d, %d", entry.getPos().x, entry.getPos().z)
                    .add("Players: %s", entry.getWatchingPlayers().stream().map(EntityPlayer::getName).collect(Collectors.joining(", ")))
                    .add("StackTrace:")
                    .add(new Exception(String.format("%s v%s", Sledgehammer.NAME, Sledgehammer.VERSION))));
            
            callbackInfo.cancel();
        }
    }
}