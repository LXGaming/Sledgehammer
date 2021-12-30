/*
 * Copyright 2021 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.projectred.integration;

import mrtjp.projectred.integration.Comparator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = Comparator.class, remap = false)
public abstract class ComparatorMixin {
    
    /**
     * @author embeddedt
     * @reason Follow changes in upstream
     * https://github.com/MrTJP/ProjectRed/commit/4134ea2461a0ae30f4da4629f72ed66ac11ebe6a
     */
    @Overwrite
    public boolean requireStrongInput(int r) {
        return false;
    }
}