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

package io.github.lxgaming.sledgehammer.mixin.core.world.biome;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(value = BiomeProvider.class, priority = 1337)
public abstract class MixinBiomeProvider {
    
    @Shadow
    private GenLayer genBiomes;
    
    @Inject(method = "findBiomePosition", at = @At(value = "HEAD"), cancellable = true)
    private void onFindBiomePosition(int x, int z, int range, List<Biome> biomes, Random random, CallbackInfoReturnable<BlockPos> callbackInfoReturnable) {
        if (genBiomes == null) {
            Sledgehammer.getInstance().getLogger().warn("Cannot find Biome Position");
            callbackInfoReturnable.setReturnValue(null);
        }
    }
}