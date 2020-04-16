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

package io.github.lxgaming.sledgehammer.mixin.realfilingcabinet.helpers.enums;

import com.bafomdad.realfilingcabinet.items.capabilities.CapabilityFolder;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * {@link com.bafomdad.realfilingcabinet.helpers.enums.FolderType#NORMAL FolderType#NORMAL}
 */
@Mixin(targets = "com/bafomdad/realfilingcabinet/helpers/enums/FolderType$1", priority = 1337, remap = false)
public abstract class FolderType_1Mixin {
    
    /**
     * @author LX_Gaming
     * @reason Do not modify the ItemStack
     */
    @Overwrite
    public ItemStack insert(CapabilityFolder cap, Object toInsert, boolean sim, boolean oreDict) {
        if (!(toInsert instanceof ItemStack) || !cap.isItemStack()) {
            return ItemStack.EMPTY;
        }
        
        ItemStack stack = (ItemStack) toInsert;
        if (!ItemStack.areItemsEqual(stack, cap.getItemStack()) && !oreDict) {
            return stack;
        }
        
        if (!sim) {
            cap.setCount(cap.getCount() + stack.getCount());
            return ItemStack.EMPTY;
        }
        
        return ItemStack.EMPTY;
    }
}