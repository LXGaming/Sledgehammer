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

package io.github.lxgaming.sledgehammer.mixin.core.world;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import io.github.lxgaming.sledgehammer.util.world.chunk.EmptyChunk;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = World.class)
public abstract class WorldMixin_ChunkUnload {
    
    private boolean sledgehammer$unloading = false;
    
    @Redirect(
            method = "updateEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/tileentity/TileEntity;onChunkUnload()V",
                    remap = false
            )
    )
    private void onChunkUnload(TileEntity tileEntity) {
        sledgehammer$unloading = true;
        tileEntity.onChunkUnload();
        sledgehammer$unloading = false;
    }
    
    @Inject(
            method = "getChunk(II)Lnet/minecraft/world/chunk/Chunk;",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onGetChunk(int chunkX, int chunkZ, CallbackInfoReturnable<Chunk> callbackInfoReturnable) {
        if (!sledgehammer$unloading) {
            return;
        }
        
        Chunk chunk = Toolbox.getLoadedChunkWithoutMarkingActive((World) (Object) this, chunkX, chunkZ);
        if (chunk != null && chunk.isLoaded() && !chunk.unloadQueued) {
            callbackInfoReturnable.setReturnValue(chunk);
            return;
        }
        
        Sledgehammer.getInstance().debug("A TileEntity attempted to load a Chunk ({}, {}) during Chunk unload", chunkX, chunkZ);
        callbackInfoReturnable.setReturnValue(new EmptyChunk((World) (Object) this, 0, 0));
    }
}