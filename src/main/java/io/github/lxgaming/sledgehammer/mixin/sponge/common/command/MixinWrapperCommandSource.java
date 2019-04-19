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

package io.github.lxgaming.sledgehammer.mixin.sponge.common.command;

import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.common.command.WrapperCommandSource;

@Mixin(value = WrapperCommandSource.class, priority = 1337, remap = false)
public abstract class MixinWrapperCommandSource {
    
    @Shadow
    @Final
    private PermissionService service;
    
    /**
     * @author dualspiral
     * @reason Fix ClassCastException - https://github.com/SpongePowered/SpongeCommon/commit/073e287f453317bcdda09fcc6f3fbf6fd154d33a
     */
    @Overwrite
    public SubjectCollection getContainingCollection() {
        return this.service.loadCollection(PermissionService.SUBJECTS_SYSTEM).join();
    }
}