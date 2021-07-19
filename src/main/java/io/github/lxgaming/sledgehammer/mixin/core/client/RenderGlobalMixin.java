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
        int subChunkList = subChunkYPos / 16;
        /* Make sure there is actually going to be an entity list, to be on the safe side */
        if(subChunkList < 0 || subChunkList > 15)
            return renderChunk.boundingBox;
        ClassInheritanceMultiMap<Entity> entityMap = chunk.getEntityLists()[subChunkList];
        AxisAlignedBB box = renderChunk.boundingBox;
        if (!entityMap.isEmpty()) {
            for (Entity entity : entityMap) {
                /* Grow the entity bounding box by 0.5 to have a bit of a margin for error */
                AxisAlignedBB entityBox = entity.getRenderBoundingBox().grow(0.5d);
                /*
                 * Union the entity box with our current bounding box - this will return the smallest bounding box which
                 * can fit both.
                 */
                box = box.union(entityBox);
            }
        }
        return box;
    }
}
