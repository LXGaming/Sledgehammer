/*
 * Copyright 2018 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.forge.entity.passive;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.common.bridge.entity.EntityVillagerBridge;
import org.spongepowered.common.entity.SpongeProfession;

import java.util.List;

@Mixin(value = EntityVillager.class, priority = 1337)
public abstract class EntityVillagerMixin extends EntityAgeable implements EntityVillagerBridge {
    
    @Shadow
    private int careerId;
    
    @Shadow
    private int careerLevel;
    
    @Shadow
    private MerchantRecipeList buyingList;
    
    public EntityVillagerMixin(World worldIn, int careerId) {
        super(worldIn);
        this.careerId = careerId;
    }
    
    @Inject(
            method = "populateBuyingList",
            at = @At(
                    value = "RETURN"
            ),
            cancellable = true
    )
    private void onPopulateBuyingListReturn(CallbackInfo callbackInfo) {
        if (SledgehammerPlatform.getInstance().isLoaded("primitivemobs") && StringUtils.equals(Toolbox.cast(this, Entity.class).getType().getId(), "primitivemobs:travelingmerchant")) {
            sledgehammer$populateTravelingMerchant();
        }
    }
    
    @Inject(
            method = "processInteract",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onProcessInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (SledgehammerPlatform.getInstance().isLoaded("primitivemobs") && StringUtils.equals(Toolbox.cast(this, Entity.class).getType().getId(), "primitivemobs:travelingmerchant")) {
            sledgehammer$populateTravelingMerchant();
        }
    }
    
    @SuppressWarnings("deprecation")
    private void sledgehammer$populateTravelingMerchant() {
        if (this.buyingList != null && !this.buyingList.isEmpty()) {
            return;
        }
        
        if (!bridge$getProfessionOptional().isPresent()) {
            return;
        }
        
        VillagerRegistry.VillagerProfession villagerProfession = VillagerRegistry.getById(((SpongeProfession) bridge$getProfessionOptional().get()).type);
        VillagerRegistry.VillagerCareer villagerCareer = villagerProfession.getCareer(this.careerId - 1);
        List<EntityVillager.ITradeList> trades = villagerCareer.getTrades(this.careerLevel - 1);
        if (trades == null) {
            Sledgehammer.getInstance().getLogger().warn("Failed to populate TravelingMerchant");
            return;
        }
        
        for (EntityVillager.ITradeList trade : trades) {
            trade.addMerchantRecipe(Toolbox.cast(this, IMerchant.class), this.buyingList, this.rand);
        }
        
        Sledgehammer.getInstance().debug("TravelingMerchant Populated");
    }
}