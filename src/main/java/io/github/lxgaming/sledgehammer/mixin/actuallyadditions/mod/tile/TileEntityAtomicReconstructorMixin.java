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

package io.github.lxgaming.sledgehammer.mixin.actuallyadditions.mod.tile;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.lens.ILensItem;
import de.ellpeck.actuallyadditions.api.lens.Lens;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityAtomicReconstructor;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityInventoryBase;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = TileEntityAtomicReconstructor.class, priority = 1337)
public abstract class TileEntityAtomicReconstructorMixin extends TileEntityInventoryBase {
    
    public TileEntityAtomicReconstructorMixin(int slots, String name) {
        super(slots, name);
    }
    
    /**
     * @author LX_Gaming
     * @reason Disable Disruption Lens
     */
    @Overwrite(remap = false)
    public Lens getLens() {
        Item item = inv.getStackInSlot(0).getItem();
        if (item instanceof ILensItem) {
            return ((ILensItem) item).getLens();
        }
        
        return ActuallyAdditionsAPI.lensDefaultConversion;
    }
}