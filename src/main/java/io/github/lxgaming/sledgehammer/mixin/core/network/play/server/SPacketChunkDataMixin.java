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

package io.github.lxgaming.sledgehammer.mixin.core.network.play.server;

import net.minecraft.crash.CrashReport;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ReportedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = SPacketChunkData.class, priority = 1337)
public abstract class SPacketChunkDataMixin {
    
    @Redirect(
            method = "<init>(Lnet/minecraft/world/chunk/Chunk;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/tileentity/TileEntity;getUpdateTag()Lnet/minecraft/nbt/NBTTagCompound;"
            )
    )
    private NBTTagCompound onGetUpdateTag(TileEntity tile) {
        try {
            return tile.getUpdateTag();
        } catch (Throwable t) {
            CrashReport crashReport = CrashReport.makeCrashReport(t, "Getting update tag");
            tile.addInfoToCrashReport(crashReport.makeCategory("Block entity being queried"));
            throw new ReportedException(crashReport);
        }
    }
}