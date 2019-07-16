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

package io.github.lxgaming.sledgehammer.mixin.core.util;

import net.minecraft.util.LazyLoadBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = LazyLoadBase.class, priority = 1337)
public abstract class LazyLoadBaseMixin<T> {
    
    @Shadow
    private T value;
    
    @Shadow
    private boolean isLoaded;
    
    @Shadow
    protected abstract T load();
    
    /**
     * @author LX_Gaming
     * @reason Make Thread-safe
     */
    @Overwrite
    public T getValue() {
        synchronized (this) {
            if (!this.isLoaded) {
                this.isLoaded = true;
                this.value = this.load();
            }
            
            return this.value;
        }
    }
}