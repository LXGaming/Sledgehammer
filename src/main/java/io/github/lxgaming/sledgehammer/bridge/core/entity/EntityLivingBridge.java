package io.github.lxgaming.sledgehammer.bridge.core.entity;

/**
 * Helper to allow invoking methods from EntityLivingMixin_Despawning.
 */
public interface EntityLivingBridge {
    void sledgehammer$incrementIdleTime();
    void sledgehammer$despawnEntity();
}
