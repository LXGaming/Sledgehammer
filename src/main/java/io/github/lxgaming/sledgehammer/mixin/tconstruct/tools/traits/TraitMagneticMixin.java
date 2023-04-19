/*
 * Copyright 2023 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.tconstruct.tools.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import slimeknights.tconstruct.library.potion.TinkerPotion;

import javax.vecmath.Vector3d;
import java.util.List;

@Mixin(targets = "slimeknights.tconstruct.tools.traits.TraitMagnetic$MagneticPotion", remap = false)
public abstract class TraitMagneticMixin extends TinkerPotion {
    
    public TraitMagneticMixin(ResourceLocation location, boolean badEffect, boolean showInInventory) {
        super(location, badEffect, showInInventory);
    }
    
    /**
     * @author LX_Gaming
     * @reason https://github.com/SlimeKnights/TinkersConstruct/issues/3857
     */
    @SuppressWarnings("DuplicatedCode")
    @Overwrite
    public void func_76394_a(EntityLivingBase entity, int id) {
        double x = entity.posX;
        double y = entity.posY;
        double z = entity.posZ;
        double range = 1.8D;
        float strength = 0.07F;
        
        PotionEffect activePotionEffect = entity.getActivePotionEffect(this);
        if (activePotionEffect != null) {
            range += activePotionEffect.getAmplifier() * 0.3F;
        }
        
        List<EntityItem> items = entity.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
        int pulled = 0;
        for (EntityItem item : items) {
            if (!item.isEntityAlive() || item.cannotPickup() || item.getItem().isEmpty()) {
                continue;
            }
            
            Vector3d vec = new Vector3d(x, y, z);
            vec.sub(new Vector3d(item.posX, item.posY, item.posZ));
            
            if (vec.lengthSquared() <= 0.05) {
                continue;
            }
            
            vec.normalize();
            vec.scale(strength);
            
            item.motionX += vec.x;
            item.motionY += vec.y;
            item.motionZ += vec.z;
            
            if (++pulled >= 200) {
                break;
            }
        }
    }
}