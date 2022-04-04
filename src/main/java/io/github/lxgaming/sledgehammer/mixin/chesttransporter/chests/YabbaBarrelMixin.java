/*
 * Copyright 2022 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.chesttransporter.chests;

import com.feed_the_beast.ftblib.lib.tile.TileBase;
import cubex2.mods.chesttransporter.chests.TransportableChestImpl;
import cubex2.mods.chesttransporter.chests.YabbaBarrel;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = YabbaBarrel.class, remap = false)
public abstract class YabbaBarrelMixin extends TransportableChestImpl {
    
    public YabbaBarrelMixin(Block chestBlock, int chestMeta, String name) {
        super(chestBlock, chestMeta, name);
    }
    
    @Override
    public void preRemoveChest(World world, BlockPos pos, EntityPlayer player, ItemStack transporter) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileBase) {
            ((TileBase) tileEntity).brokenByCreative = true;
        }
    }
}