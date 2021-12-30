/*
 * Copyright 2021 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.forge.world;

import com.google.common.collect.ImmutableSetMultimap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = World.class)
public abstract class WorldMixin_IgnoreForcedChunkTicking {
    
    /**
     * Return an empty map to fool the entity ticking logic into thinking no chunks are forcefully loaded by Forge.
     */
    @Redirect(
            method = "updateEntityWithOptionalForce",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getPersistentChunks()Lcom/google/common/collect/ImmutableSetMultimap;",
                    remap = false
            )
    )
    public ImmutableSetMultimap<ChunkPos, ForgeChunkManager.Ticket> getEmptyPersistentChunks(World world) {
        return ImmutableSetMultimap.of();
    }
}