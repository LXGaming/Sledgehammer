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

import java.util.function.Supplier;

@Mixin(value = LazyLoadBase.class)
public abstract class LazyLoadBaseMixin<T> {
    
    @Shadow
    private Supplier<T> supplier;
    
    @Shadow
    private T value;
    
    /**
     * @author LX_Gaming
     * @reason Make Thread-safe
     */
    @Overwrite
    public T getValue() {
        synchronized (this) {
            Supplier<T> supplier = this.supplier;
            if (supplier != null) {
                this.value = supplier.get();
                this.supplier = null;
            }
            
            return this.value;
        }
    }
}