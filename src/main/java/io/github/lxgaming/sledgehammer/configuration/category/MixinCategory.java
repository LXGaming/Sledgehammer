/*
 * Copyright 2019 Alex Thomson
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

package io.github.lxgaming.sledgehammer.configuration.category;

import io.github.lxgaming.sledgehammer.configuration.category.mixin.ActuallyAdditionsMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.BetterSurvivalMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.CarryOnMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.CoreMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ForgeMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ImmersiveEngineeringMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.MatterOverdriveMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.MorphMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.NoTreePunchingMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.P455w0rdsLibMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.PrimitiveMobsMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ProjectRedMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.PyrotechMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.QuarkMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ReliquaryMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.RuinsMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.SpongeMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.StorageNetworkMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.TombManyGravesMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.TopographyMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.TotemicMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.VaultopicMixinCategory;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class MixinCategory {
    
    @Setting(value = "actually-additions", comment = "Actually Additions")
    private ActuallyAdditionsMixinCategory actuallyAdditionsMixinCategory = new ActuallyAdditionsMixinCategory();
    
    @Setting(value = "better-survival", comment = "Better Survival")
    private BetterSurvivalMixinCategory betterSurvivalMixinCategory = new BetterSurvivalMixinCategory();
    
    @Setting(value = "carry-on", comment = "Carry On")
    private CarryOnMixinCategory carryOnMixinCategory = new CarryOnMixinCategory();
    
    @Setting(value = "core", comment = "Minecraft")
    private CoreMixinCategory coreMixinCategory = new CoreMixinCategory();
    
    @Setting(value = "forge", comment = "Forge")
    private ForgeMixinCategory forgeMixinCategory = new ForgeMixinCategory();
    
    @Setting(value = "immersive-engineering", comment = "Immersive Engineering")
    private ImmersiveEngineeringMixinCategory immersiveEngineeringMixinCategory = new ImmersiveEngineeringMixinCategory();
    
    @Setting(value = "matter-overdrive", comment = "Matter Overdrive")
    private MatterOverdriveMixinCategory matterOverdriveMixinCategory = new MatterOverdriveMixinCategory();
    
    @Setting(value = "morph", comment = "Morph")
    private MorphMixinCategory morphMixinCategory = new MorphMixinCategory();
    
    @Setting(value = "no-tree-punching", comment = "No Tree Punching")
    private NoTreePunchingMixinCategory noTreePunchingMixinCategory = new NoTreePunchingMixinCategory();
    
    @Setting(value = "p455w0rdslib", comment = "p455w0rd's Library")
    private P455w0rdsLibMixinCategory p455w0rdsLibMixinCategory = new P455w0rdsLibMixinCategory();
    
    @Setting(value = "primitive-mobs", comment = "Primitive Mobs")
    private PrimitiveMobsMixinCategory primitiveMobsMixinCategory = new PrimitiveMobsMixinCategory();
    
    @Setting(value = "project-red", comment = "Project Red")
    private ProjectRedMixinCategory projectRedMixinCategory = new ProjectRedMixinCategory();
    
    @Setting(value = "pyrotech", comment = "Pyrotech")
    private PyrotechMixinCategory pyrotechMixinCategory = new PyrotechMixinCategory();
    
    @Setting(value = "quark", comment = "Quark")
    private QuarkMixinCategory quarkMixinCategory = new QuarkMixinCategory();
    
    @Setting(value = "reliquary", comment = "Reliquary")
    private ReliquaryMixinCategory reliquaryMixinCategory = new ReliquaryMixinCategory();
    
    @Setting(value = "ruins", comment = "Ruins (Structure Spawning System)")
    private RuinsMixinCategory ruinsMixinCategory = new RuinsMixinCategory();
    
    @Setting(value = "sponge", comment = "SpongeForge / SpongeVanilla")
    private SpongeMixinCategory spongeMixinCategory = new SpongeMixinCategory();
    
    @Setting(value = "storage-network", comment = "Storage Network")
    private StorageNetworkMixinCategory storageNetworkMixinCategory = new StorageNetworkMixinCategory();
    
    @Setting(value = "tomb-many-graves", comment = "Tomb Many Graves")
    private TombManyGravesMixinCategory tombManyGravesMixinCategory = new TombManyGravesMixinCategory();
    
    @Setting(value = "topography", comment = "Topography")
    private TopographyMixinCategory topographyMixinCategory = new TopographyMixinCategory();
    
    @Setting(value = "totemic", comment = "Totemic")
    private TotemicMixinCategory totemicMixinCategory = new TotemicMixinCategory();
    
    @Setting(value = "vaultopic", comment = "Vaultopic - Early Inventory Management")
    private VaultopicMixinCategory vaultopicMixinCategory = new VaultopicMixinCategory();
    
    public ActuallyAdditionsMixinCategory getActuallyAdditionsMixinCategory() {
        return actuallyAdditionsMixinCategory;
    }
    
    public BetterSurvivalMixinCategory getBetterSurvivalMixinCategory() {
        return betterSurvivalMixinCategory;
    }
    
    public CarryOnMixinCategory getCarryOnMixinCategory() {
        return carryOnMixinCategory;
    }
    
    public CoreMixinCategory getCoreMixinCategory() {
        return coreMixinCategory;
    }
    
    public ForgeMixinCategory getForgeMixinCategory() {
        return forgeMixinCategory;
    }
    
    public ImmersiveEngineeringMixinCategory getImmersiveEngineeringMixinCategory() {
        return immersiveEngineeringMixinCategory;
    }
    
    public MatterOverdriveMixinCategory getMatterOverdriveMixinCategory() {
        return matterOverdriveMixinCategory;
    }
    
    public MorphMixinCategory getMorphMixinCategory() {
        return morphMixinCategory;
    }
    
    public NoTreePunchingMixinCategory getNoTreePunchingMixinCategory() {
        return noTreePunchingMixinCategory;
    }
    
    public P455w0rdsLibMixinCategory getP455w0rdsLibMixinCategory() {
        return p455w0rdsLibMixinCategory;
    }
    
    public PrimitiveMobsMixinCategory getPrimitiveMobsMixinCategory() {
        return primitiveMobsMixinCategory;
    }
    
    public ProjectRedMixinCategory getProjectRedMixinCategory() {
        return projectRedMixinCategory;
    }
    
    public PyrotechMixinCategory getPyrotechMixinCategory() {
        return pyrotechMixinCategory;
    }
    
    public QuarkMixinCategory getQuarkMixinCategory() {
        return quarkMixinCategory;
    }
    
    public ReliquaryMixinCategory getReliquaryMixinCategory() {
        return reliquaryMixinCategory;
    }
    
    public RuinsMixinCategory getRuinsMixinCategory() {
        return ruinsMixinCategory;
    }
    
    public SpongeMixinCategory getSpongeMixinCategory() {
        return spongeMixinCategory;
    }
    
    public StorageNetworkMixinCategory getStorageNetworkMixinCategory() {
        return storageNetworkMixinCategory;
    }
    
    public TombManyGravesMixinCategory getTombManyGravesMixinCategory() {
        return tombManyGravesMixinCategory;
    }
    
    public TopographyMixinCategory getTopographyMixinCategory() {
        return topographyMixinCategory;
    }
    
    public TotemicMixinCategory getTotemicMixinCategory() {
        return totemicMixinCategory;
    }
    
    public VaultopicMixinCategory getVaultopicMixinCategory() {
        return vaultopicMixinCategory;
    }
}