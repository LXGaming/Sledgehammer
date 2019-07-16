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

package io.github.lxgaming.sledgehammer.mixin.core.world.chunk.storage;

import io.github.lxgaming.sledgehammer.exception.ChunkSaveException;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Mixin(targets = "net/minecraft/world/chunk/storage/RegionFile$ChunkBuffer", priority = 1337)
public abstract class RegionFileChunkBufferMixin extends ByteArrayOutputStream {
    
    @Shadow
    @Final
    private int chunkX;
    
    @Shadow
    @Final
    private int chunkZ;
    
    @Inject(method = "close", at = @At(value = "HEAD"), remap = false)
    private void onClose(CallbackInfo callbackInfo) throws IOException {
        int length = (this.count + 5) / 4096 + 1;
        if (length >= 256) {
            throw new ChunkSaveException("Chunk (" + this.chunkX + ", " + this.chunkZ + ") too big (" + length + " >= 256)");
        }
    }
}