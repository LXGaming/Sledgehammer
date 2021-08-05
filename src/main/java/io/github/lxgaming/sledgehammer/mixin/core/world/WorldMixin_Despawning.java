package io.github.lxgaming.sledgehammer.mixin.core.world;

import io.github.lxgaming.sledgehammer.bridge.entity.EntityLivingBridge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin( value = World.class )
public class WorldMixin_Despawning {
    @Redirect( method = "updateEntityWithOptionalForce", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;canEntityUpdate(Lnet/minecraft/entity/Entity;)Z", remap = false))
    public boolean canEntityUpdate(Entity entity) {
        /*
         * Reaching this point means that the entity is probably going to be blocked from ticking.
         * This will prevent despawning from occuring. Check whether a Forge event overrides this and
         * allows updating anyways. If not, we will run the despawn checks ourselves.
         */
        boolean canUpdate = ForgeEventFactory.canEntityUpdate(entity);
        if(!canUpdate && entity instanceof EntityLiving) {
            EntityLivingBridge helper = (EntityLivingBridge)entity;
            helper.sledgehammer$incrementIdleTime();
            helper.sledgehammer$despawnEntity();
        }
        return canUpdate;
    }
}
