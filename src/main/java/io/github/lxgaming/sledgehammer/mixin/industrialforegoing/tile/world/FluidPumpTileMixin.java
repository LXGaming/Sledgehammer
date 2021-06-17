package io.github.lxgaming.sledgehammer.mixin.industrialforegoing.tile.world;

import com.buuz135.industrial.tile.world.FluidPumpTile;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

/**
 * Used to prevent a NullPointerException on FluidPumpTile.java:101 (`handler` is null).
 */
class FakeFluidHandler implements IFluidHandler {
    FakeFluidHandler() {

    }
    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[0];
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
    }
}
@Mixin(FluidPumpTile.class)
public class FluidPumpTileMixin {
    @Redirect(
            method = "work",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/fluids/FluidUtil;getFluidHandler(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Lnet/minecraftforge/fluids/capability/IFluidHandler;"
            ),
            remap = false
    )
    private IFluidHandler getFluidHandler(World world, BlockPos blockPos, @Nullable EnumFacing side) {
        IFluidHandler handler = FluidUtil.getFluidHandler(world, blockPos, side);
        if(handler != null)
            return handler;
        else
            return new FakeFluidHandler();
    }
}
