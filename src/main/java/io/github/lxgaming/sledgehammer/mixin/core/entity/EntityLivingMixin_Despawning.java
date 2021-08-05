package io.github.lxgaming.sledgehammer.mixin.core.entity;

import io.github.lxgaming.sledgehammer.bridge.entity.EntityLivingBridge;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/* Ideally this would be implemented with pure accessors, but they seemed to be causing issues. */
@Mixin(EntityLiving.class)
public abstract class EntityLivingMixin_Despawning extends EntityLivingBase implements EntityLivingBridge {
    @Shadow protected abstract void despawnEntity();

    public EntityLivingMixin_Despawning(World worldIn) {
        super(worldIn);
    }

    public void sledgehammer$incrementIdleTime() {
        this.idleTime++;
    }

    public void sledgehammer$despawnEntity() {
        this.despawnEntity();
    }
}
