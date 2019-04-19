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

package io.github.lxgaming.sledgehammer.mixin.carryon.common.event;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import tschipp.carryon.common.event.ItemEvents;

import java.util.Iterator;
import java.util.Map;

@Mixin(value = ItemEvents.class, priority = 1337, remap = false)
public abstract class MixinItemEvents {
    
    @Shadow
    public static Map<BlockPos, Integer> positions;
    
    /**
     * @author LX_Gaming
     * @reason Fixes ConcurrentModificationException
     */
    @Overwrite
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        for (Iterator<Map.Entry<BlockPos, Integer>> iterator = positions.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<BlockPos, Integer> entry = iterator.next();
            entry.setValue(entry.getValue() + 1);
            if (entry.getValue() > 3) {
                iterator.remove();
            }
        }
    }
}