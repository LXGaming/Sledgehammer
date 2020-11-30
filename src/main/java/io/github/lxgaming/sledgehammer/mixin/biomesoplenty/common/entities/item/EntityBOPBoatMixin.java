/*
 * Copyright 2020 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.biomesoplenty.common.entities.item;

import biomesoplenty.common.entities.item.EntityBOPBoat;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = EntityBOPBoat.class, remap = false)
public class EntityBOPBoatMixin extends EntityBoat {
    
    @Shadow
    @Final
    private static DataParameter<Integer> TIME_SINCE_HIT;
    
    @Shadow
    @Final
    private static DataParameter<Integer> FORWARD_DIRECTION;
    
    @Shadow
    @Final
    private static DataParameter<Float> DAMAGE_TAKEN;
    
    @Shadow
    @Final
    private static DataParameter<Integer> BOAT_TYPE;
    
    @Shadow
    @Final
    private static DataParameter<Boolean>[] DATA_ID_PADDLE;
    
    public EntityBOPBoatMixin(World worldIn) {
        super(worldIn);
    }
    
    /**
     * getPaddleState
     *
     * @author LX_Gaming
     * @reason https://github.com/Glitchfiend/BiomesOPlenty/issues/1262
     */
    @Overwrite
    public boolean func_184457_a(int side) {
        try {
            return this.dataManager.get(DATA_ID_PADDLE[side]) && this.getControllingPassenger() != null;
        } catch (Exception ex) {
            return false;
        }
    }
    
    /**
     * getDamageTaken
     *
     * @author LX_Gaming
     * @reason https://github.com/Glitchfiend/BiomesOPlenty/issues/1262
     */
    @Overwrite
    public float func_70271_g() {
        try {
            return this.dataManager.get(DAMAGE_TAKEN);
        } catch (Exception ex) {
            return 0.0F;
        }
    }
    
    /**
     * getTimeSinceHit
     *
     * @author LX_Gaming
     * @reason https://github.com/Glitchfiend/BiomesOPlenty/issues/1262
     */
    @Overwrite
    public int func_70268_h() {
        try {
            return this.dataManager.get(TIME_SINCE_HIT);
        } catch (Exception ex) {
            return 0;
        }
    }
    
    /**
     * getForwardDirection
     *
     * @author LX_Gaming
     * @reason https://github.com/Glitchfiend/BiomesOPlenty/issues/1262
     */
    @Overwrite
    public int func_70267_i() {
        try {
            return this.dataManager.get(FORWARD_DIRECTION);
        } catch (Exception ex) {
            return 0;
        }
    }
    
    /**
     * @author LX_Gaming
     * @reason https://github.com/Glitchfiend/BiomesOPlenty/issues/1262
     */
    @Overwrite
    public EntityBOPBoat.Type getBOPBoatType() {
        try {
            return EntityBOPBoat.Type.byId(this.dataManager.get(BOAT_TYPE));
        } catch (Exception ex) {
            return EntityBOPBoat.Type.SACRED_OAK;
        }
    }
}