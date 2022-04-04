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
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ArmorUnderMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.AstralSorceryMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.BDSMMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.BetterQuestingMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.BewitchmentMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.BiblioCraftMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.BiomesOPlentyMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.CareerBeesMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.CarryOnMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ChampionsMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ChestTransporterMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.CoreMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.DankNullMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.EnderIOMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.EnderStorageMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.FluxNetworksMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ForgeMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ImmersiveEngineeringMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.IndustrialForegoingMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.IntegratedDynamicsMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.JourneyMapMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.KubeJSMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.LogisticsPipesMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.MatterOverdriveMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.MorphMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.MowziesMobsMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.NaturesAuraMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.PrimitiveCraftingMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.PrimitiveMobsMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ProjectRedMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.PyrotechMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.QMDMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.QuarkMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.RandomThingsMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.RealFilingCabinetMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.RuinsMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.SpongeMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.StorageNetworkMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ThaumicWondersMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.TombManyGravesMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.TopographyMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.TotemicMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.VaultopicMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.WolforceMixinCategory;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class MixinCategory {
    
    @Setting(value = "actually-additions", comment = "Actually Additions")
    private ActuallyAdditionsMixinCategory actuallyAdditionsMixinCategory = new ActuallyAdditionsMixinCategory();
    
    @Setting(value = "armor-under", comment = "Armor Underwear")
    private ArmorUnderMixinCategory armorUnderMixinCategory = new ArmorUnderMixinCategory();
    
    @Setting(value = "astral-sorcery", comment = "Astral Sorcery")
    private AstralSorceryMixinCategory astralSorceryMixinCategory = new AstralSorceryMixinCategory();
    
    @Setting(value = "bdsm", comment = "Barrels, Drums, Storage & More")
    private BDSMMixinCategory bdsmMixinCategory = new BDSMMixinCategory();
    
    @Setting(value = "betterquesting", comment = "Better Questing")
    private BetterQuestingMixinCategory betterQuestingMixinCategory = new BetterQuestingMixinCategory();
    
    @Setting(value = "bewitchment", comment = "Bewitchment")
    private BewitchmentMixinCategory bewitchmentMixinCategory = new BewitchmentMixinCategory();
    
    @Setting(value = "bibliocraft", comment = "BiblioCraft")
    private BiblioCraftMixinCategory biblioCraftMixinCategory = new BiblioCraftMixinCategory();
    
    @Setting(value = "biomesoplenty", comment = "Biomes O' Plenty")
    private BiomesOPlentyMixinCategory biomesOPlentyMixinCategory = new BiomesOPlentyMixinCategory();
    
    @Setting(value = "careerbees", comment = "Career Bees")
    private CareerBeesMixinCategory careerBeesMixinCategory = new CareerBeesMixinCategory();
    
    @Setting(value = "carry-on", comment = "Carry On")
    private CarryOnMixinCategory carryOnMixinCategory = new CarryOnMixinCategory();
    
    @Setting(value = "champions", comment = "Champions")
    private ChampionsMixinCategory championsMixinCategory = new ChampionsMixinCategory();
    
    @Setting(value = "chesttransporter", comment = "Chest Transporter")
    private ChestTransporterMixinCategory chestTransporterMixinCategory = new ChestTransporterMixinCategory();
    
    @Setting(value = "core", comment = "Minecraft")
    private CoreMixinCategory coreMixinCategory = new CoreMixinCategory();
    
    @Setting(value = "dank-null", comment = "DankNull")
    private DankNullMixinCategory dankNullMixinCategory = new DankNullMixinCategory();
    
    @Setting(value = "enderio", comment = "Ender IO")
    private EnderIOMixinCategory enderIOMixinCategory = new EnderIOMixinCategory();
    
    @Setting(value = "enderstorage", comment = "Ender Storage")
    private EnderStorageMixinCategory enderStorageMixinCategory = new EnderStorageMixinCategory();
    
    @Setting(value = "flux-networks", comment = "Flux Networks")
    private FluxNetworksMixinCategory fluxNetworksMixinCategory = new FluxNetworksMixinCategory();
    
    @Setting(value = "forge", comment = "Forge")
    private ForgeMixinCategory forgeMixinCategory = new ForgeMixinCategory();
    
    @Setting(value = "immersiveengineering", comment = "Immersive Engineering")
    private ImmersiveEngineeringMixinCategory immersiveEngineeringMixinCategory = new ImmersiveEngineeringMixinCategory();
    
    @Setting(value = "industrialforegoing", comment = "Industrial Foregoing")
    private IndustrialForegoingMixinCategory industrialForegoingMixinCategory = new IndustrialForegoingMixinCategory();
    
    @Setting(value = "integrateddynamics", comment = "Integrated Dynamics")
    private IntegratedDynamicsMixinCategory integratedDynamicsMixinCategory = new IntegratedDynamicsMixinCategory();
    
    @Setting(value = "journeymap", comment = "JourneyMap")
    private JourneyMapMixinCategory journeyMapMixinCategory = new JourneyMapMixinCategory();
    
    @Setting(value = "kubejs", comment = "KubeJS")
    private KubeJSMixinCategory kubeJSMixinCategory = new KubeJSMixinCategory();
    
    @Setting(value = "logistics-pipes", comment = "Logistics Pipes")
    private LogisticsPipesMixinCategory logisticsPipesMixinCategory = new LogisticsPipesMixinCategory();
    
    @Setting(value = "matter-overdrive", comment = "Matter Overdrive")
    private MatterOverdriveMixinCategory matterOverdriveMixinCategory = new MatterOverdriveMixinCategory();
    
    @Setting(value = "morph", comment = "Morph")
    private MorphMixinCategory morphMixinCategory = new MorphMixinCategory();
    
    @Setting(value = "mowziesmobs", comment = "Mowzie's Mobs")
    private MowziesMobsMixinCategory mowziesMobsMixinCategory = new MowziesMobsMixinCategory();
    
    @Setting(value = "naturesaura", comment = "Nature's Aura")
    private NaturesAuraMixinCategory naturesAuraMixinCategory = new NaturesAuraMixinCategory();
    
    @Setting(value = "primitivecrafting", comment = "Primitive Crafting")
    private PrimitiveCraftingMixinCategory primitiveCraftingMixinCategory = new PrimitiveCraftingMixinCategory();
    
    @Setting(value = "primitive-mobs", comment = "Primitive Mobs")
    private PrimitiveMobsMixinCategory primitiveMobsMixinCategory = new PrimitiveMobsMixinCategory();
    
    @Setting(value = "project-red", comment = "Project Red")
    private ProjectRedMixinCategory projectRedMixinCategory = new ProjectRedMixinCategory();
    
    @Setting(value = "pyrotech", comment = "Pyrotech")
    private PyrotechMixinCategory pyrotechMixinCategory = new PyrotechMixinCategory();
    
    @Setting(value = "qmd", comment = "Quantum Minecraft Dynamics")
    private QMDMixinCategory qmdMixinCategory = new QMDMixinCategory();
    
    @Setting(value = "quark", comment = "Quark")
    private QuarkMixinCategory quarkMixinCategory = new QuarkMixinCategory();
    
    @Setting(value = "randomthings", comment = "Random Things")
    private RandomThingsMixinCategory randomThingsMixinCategory = new RandomThingsMixinCategory();
    
    @Setting(value = "realfilingcabinet", comment = "Real Filing Cabinet")
    private RealFilingCabinetMixinCategory realFilingCabinetMixinCategory = new RealFilingCabinetMixinCategory();
    
    @Setting(value = "ruins", comment = "Ruins (Structure Spawning System)")
    private RuinsMixinCategory ruinsMixinCategory = new RuinsMixinCategory();
    
    @Setting(value = "sponge", comment = "SpongeForge / SpongeVanilla")
    private SpongeMixinCategory spongeMixinCategory = new SpongeMixinCategory();
    
    @Setting(value = "storage-network", comment = "Storage Network")
    private StorageNetworkMixinCategory storageNetworkMixinCategory = new StorageNetworkMixinCategory();
    
    @Setting(value = "thaumicwonders", comment = "Thaumic Wonders")
    private ThaumicWondersMixinCategory thaumicWondersMixinCategory = new ThaumicWondersMixinCategory();
    
    @Setting(value = "tomb-many-graves", comment = "Tomb Many Graves")
    private TombManyGravesMixinCategory tombManyGravesMixinCategory = new TombManyGravesMixinCategory();
    
    @Setting(value = "topography", comment = "Topography")
    private TopographyMixinCategory topographyMixinCategory = new TopographyMixinCategory();
    
    @Setting(value = "totemic", comment = "Totemic")
    private TotemicMixinCategory totemicMixinCategory = new TotemicMixinCategory();
    
    @Setting(value = "vaultopic", comment = "Vaultopic - Early Inventory Management")
    private VaultopicMixinCategory vaultopicMixinCategory = new VaultopicMixinCategory();
    
    @Setting(value = "wolforce", comment = "Hearth Well")
    private WolforceMixinCategory wolforceMixinCategory = new WolforceMixinCategory();
    
    public ActuallyAdditionsMixinCategory getActuallyAdditionsMixinCategory() {
        return actuallyAdditionsMixinCategory;
    }
    
    public ArmorUnderMixinCategory getArmorUnderMixinCategory() {
        return armorUnderMixinCategory;
    }
    
    public AstralSorceryMixinCategory getAstralSorceryMixinCategory() {
        return astralSorceryMixinCategory;
    }
    
    public BDSMMixinCategory getBdsmMixinCategory() {
        return bdsmMixinCategory;
    }
    
    public BetterQuestingMixinCategory getBetterQuestingMixinCategory() {
        return betterQuestingMixinCategory;
    }
    
    public BewitchmentMixinCategory getBewitchmentMixinCategory() {
        return bewitchmentMixinCategory;
    }
    
    public BiblioCraftMixinCategory getBiblioCraftMixinCategory() {
        return biblioCraftMixinCategory;
    }
    
    public BiomesOPlentyMixinCategory getBiomesOPlentyMixinCategory() {
        return biomesOPlentyMixinCategory;
    }
    
    public CareerBeesMixinCategory getCareerBeesMixinCategory() {
        return careerBeesMixinCategory;
    }
    
    public CarryOnMixinCategory getCarryOnMixinCategory() {
        return carryOnMixinCategory;
    }
    
    public ChampionsMixinCategory getChampionsMixinCategory() {
        return championsMixinCategory;
    }
    
    public ChestTransporterMixinCategory getChestTransporterMixinCategory() {
        return chestTransporterMixinCategory;
    }
    
    public CoreMixinCategory getCoreMixinCategory() {
        return coreMixinCategory;
    }
    
    public DankNullMixinCategory getDankNullMixinCategory() {
        return dankNullMixinCategory;
    }
    
    public EnderIOMixinCategory getEnderIOMixinCategory() {
        return enderIOMixinCategory;
    }
    
    public EnderStorageMixinCategory getEnderStorageMixinCategory() {
        return enderStorageMixinCategory;
    }
    
    public FluxNetworksMixinCategory getFluxNetworksMixinCategory() {
        return fluxNetworksMixinCategory;
    }
    
    public ForgeMixinCategory getForgeMixinCategory() {
        return forgeMixinCategory;
    }
    
    public ImmersiveEngineeringMixinCategory getImmersiveEngineeringMixinCategory() {
        return immersiveEngineeringMixinCategory;
    }
    
    public IndustrialForegoingMixinCategory getIndustrialForegoingMixinCategory() {
        return industrialForegoingMixinCategory;
    }
    
    public IntegratedDynamicsMixinCategory getIntegratedDynamicsMixinCategory() {
        return integratedDynamicsMixinCategory;
    }
    
    public JourneyMapMixinCategory getJourneyMapMixinCategory() {
        return journeyMapMixinCategory;
    }
    
    public KubeJSMixinCategory getKubeJSMixinCategory() {
        return kubeJSMixinCategory;
    }
    
    public LogisticsPipesMixinCategory getLogisticsPipesMixinCategory() {
        return logisticsPipesMixinCategory;
    }
    
    public MatterOverdriveMixinCategory getMatterOverdriveMixinCategory() {
        return matterOverdriveMixinCategory;
    }
    
    public MorphMixinCategory getMorphMixinCategory() {
        return morphMixinCategory;
    }
    
    public MowziesMobsMixinCategory getMowziesMobsMixinCategory() {
        return mowziesMobsMixinCategory;
    }
    
    public NaturesAuraMixinCategory getNaturesAuraMixinCategory() {
        return naturesAuraMixinCategory;
    }
    
    public PrimitiveCraftingMixinCategory getPrimitiveCraftingMixinCategory() {
        return primitiveCraftingMixinCategory;
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
    
    public QMDMixinCategory getQmdMixinCategory() {
        return qmdMixinCategory;
    }
    
    public QuarkMixinCategory getQuarkMixinCategory() {
        return quarkMixinCategory;
    }
    
    public RandomThingsMixinCategory getRandomThingsMixinCategory() {
        return randomThingsMixinCategory;
    }
    
    public RealFilingCabinetMixinCategory getRealFilingCabinetMixinCategory() {
        return realFilingCabinetMixinCategory;
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
    
    public ThaumicWondersMixinCategory getThaumicWondersMixinCategory() {
        return thaumicWondersMixinCategory;
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
    
    public WolforceMixinCategory getWolforceMixinCategory() {
        return wolforceMixinCategory;
    }
}