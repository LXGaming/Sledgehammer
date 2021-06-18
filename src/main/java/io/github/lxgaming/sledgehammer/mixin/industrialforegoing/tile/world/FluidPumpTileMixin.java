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

package io.github.lxgaming.sledgehammer.mixin.industrialforegoing.tile.world;

import com.buuz135.industrial.tile.world.FluidPumpTile;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FluidPumpTile.class, remap = false)
public abstract class FluidPumpTileMixin {
    
    @Redirect(
            method = "work",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/fluids/capability/IFluidHandler;drain(IZ)Lnet/minecraftforge/fluids/FluidStack;"
            )
    )
    private FluidStack onDrain(IFluidHandler fluidHandler, int maxDrain, boolean doDrain) {
        return fluidHandler != null ? fluidHandler.drain(maxDrain, doDrain) : null;
    }
}