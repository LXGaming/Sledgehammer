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

package io.github.lxgaming.sledgehammer.mixin.tconevo.integration.redstonerepository;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import xyz.phanta.tconevo.integration.redstonerepository.RedstoneRepositoryHooksImpl;

import java.lang.reflect.Field;
import java.util.Optional;

@Mixin(value = RedstoneRepositoryHooksImpl.class, remap = false)
public abstract class RedstoneRepositoryHooksImplMixin {
    
    private Item sledgehammer$itemCapacitorAmulet;
    
    /**
     * @author LX_Gaming
     * @reason https://github.com/phantamanta44/tinkers-evolution/issues/114
     */
    @Overwrite
    public Optional<ItemStack> getItemGelidCapacitor() throws Exception {
        if (sledgehammer$itemCapacitorAmulet == null) {
            Class<?> equipmentInitClass = Class.forName("thundr.redstonerepository.init.RedstoneRepositoryEquipment$EquipmentInit");
            Field itemCapacitorAmuletField = equipmentInitClass.getField("itemCapacitorAmulet");
            sledgehammer$itemCapacitorAmulet = (Item) itemCapacitorAmuletField.get(null);
        }
        
        return Optional.of(new ItemStack(sledgehammer$itemCapacitorAmulet));
    }
}