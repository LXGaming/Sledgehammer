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

package io.github.lxgaming.sledgehammer.mixin.core.util.text;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.manager.LocaleManager;
import io.github.lxgaming.sledgehammer.util.Locale;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TranslationTextComponentFormatException;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = TranslationTextComponent.class)
public abstract class TranslationTextComponentMixin {
    
    @Shadow
    @Final
    private String key;
    
    @Shadow
    @Final
    protected List<ITextComponent> children;
    
    @Shadow
    protected abstract void initializeFromFormat(String format);
    
    @Redirect(
            method = "ensureInitialized",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/text/TranslationTextComponent;initializeFromFormat(Ljava/lang/String;)V",
                    ordinal = 1
            )
    )
    private void onEnsureInitialized(TranslationTextComponent textComponent, String format) {
        try {
            this.initializeFromFormat(format);
        } catch (TranslationTextComponentFormatException ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error while initializing translation: {}", ex.getMessage());
            this.children.add(LocaleManager.serialize(Locale.TRANSLATION_INVALID, this.key));
        }
    }
}