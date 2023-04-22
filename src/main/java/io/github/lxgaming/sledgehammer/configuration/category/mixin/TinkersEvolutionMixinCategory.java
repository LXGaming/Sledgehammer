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

package io.github.lxgaming.sledgehammer.configuration.category.mixin;

import io.github.lxgaming.sledgehammer.configuration.annotation.Mapping;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class TinkersEvolutionMixinCategory {
    
    @Mapping(value = "tconevo.trait.draconicevolution.TraitEvolvedMixin", dependencies = "tconevo")
    @Setting(value = "draconic-upgrade", comment = "If 'true', fixes unintended upgrading of Draconic Modifiers")
    private boolean draconicUpgrade = false;
    
    @Mapping(value = "tconevo.integration.redstonerepository.RedstoneRepositoryHooksImplMixin", dependencies = "tconevo")
    @Setting(value = "redstone-repository", comment = "If 'true', fixes NoClassDefFoundError with Redstone Repository")
    private boolean redstoneRepository = false;
    
    public boolean isDraconicUpgrade() {
        return draconicUpgrade;
    }
    
    public boolean isRedstoneRepository() {
        return redstoneRepository;
    }
}