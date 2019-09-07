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
    
    @Redirect(method = "<init>(Lnet/minecraft/world/chunk/Chunk;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;getUpdateTag()Lnet/minecraft/nbt/NBTTagCompound;"))
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