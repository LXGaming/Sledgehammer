package io.github.lxgaming.sledgehammer.mixin.core.client;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.Entity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin( value = RenderGlobal.class, priority = 1337)
public class RenderGlobalMixin {
    @Redirect( method = "setupTerrain", at = @At( value = "FIELD", target = "Lnet/minecraft/client/renderer/chunk/RenderChunk;boundingBox:Lnet/minecraft/util/math/AxisAlignedBB;"))
    public AxisAlignedBB getBoundingBoxForChunk(RenderChunk renderChunk) {
        // Fixes MC-88176 by checking the entity list for the subchunk and adjusting the bounding box as needed.
        int subChunkYPos = renderChunk.getPosition().getY();
        Chunk chunk = renderChunk.getWorld().getChunk(renderChunk.getPosition());
        ClassInheritanceMultiMap<Entity> entityMap = chunk.getEntityLists()[subChunkYPos / 16];
        double extraMaxY = 0;
        if (!entityMap.isEmpty()) {
            for (Entity entity : entityMap) {
                AxisAlignedBB entityBox = entity.getRenderBoundingBox().grow(0.5d);
                double height = entityBox.maxY - entityBox.minY;
                double subChunkOffset = entity.getPosition().getY() - subChunkYPos;
                extraMaxY = Math.max(extraMaxY, (subChunkOffset+height)-16);
            }
        }
        AxisAlignedBB box = renderChunk.boundingBox;
        box = new AxisAlignedBB(box.minX, box.minY, box.minZ, box.maxX, box.maxY + extraMaxY, box.maxZ);
        return box;
    }
}
