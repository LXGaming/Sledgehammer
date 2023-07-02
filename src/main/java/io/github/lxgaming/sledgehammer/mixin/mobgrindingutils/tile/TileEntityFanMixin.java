/*
 * Copyright 2023 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.mobgrindingutils.tile;

import mob_grinding_utils.tile.TileEntityFan;
import mob_grinding_utils.tile.TileEntityInventoryHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntityFan.class, remap = false)
public abstract class TileEntityFanMixin extends TileEntityInventoryHelper {
    
    @Shadow
    public abstract void setAABBWithModifiers();
    
    public TileEntityFanMixin(int inventorySize) {
        super(inventorySize);
    }
    
    @Redirect(
            method = "func_73660_a",
            at = @At(
                    value = "INVOKE",
                    target = "Lmob_grinding_utils/tile/TileEntityFan;setAABBWithModifiers()V"
            )
    )
    public void onSetAABBWithModifiers(TileEntityFan instance) {
        if (getWorld().getTotalWorldTime() % 20L == 0) {
            setAABBWithModifiers();
        }
    }
}