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

package io.github.lxgaming.sledgehammer.mixin.core.item;

import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ItemWritableBook.class, priority = 1337)
public abstract class MixinItemWritableBook {
    
    /**
     * @author LX_Gaming
     * @reason Limit books based on {@link net.minecraft.client.gui.GuiScreenBook GuiScreenBook} values.
     */
    @Overwrite
    public static boolean isNBTValid(NBTTagCompound compound) {
        // TAG_LIST
        if (compound == null || !compound.hasKey("pages", 9)) {
            return false;
        }
        
        // TAG_STRING
        NBTTagList pages = compound.getTagList("pages", 8);
        
        // net.minecraft.item.ItemWrittenBook#resolveContents(ItemStack, EntityPlayer)
        if (compound.getBoolean("resolved")) {
            for (int index = 0; index < pages.tagCount(); index++) {
                if (pages.getStringTagAt(index).length() > 32767) {
                    return false;
                }
            }
            
            return true;
        }
        
        for (int index = 0; index < pages.tagCount(); index++) {
            if (index >= 50) {
                pages.removeTag(index);
                index--;
                continue;
            }
            
            String page = pages.getStringTagAt(index);
            if (page.length() >= 256) {
                pages.set(index, new NBTTagString(StringUtils.truncate(page, 252) + "..."));
            }
        }
        
        return true;
    }
}