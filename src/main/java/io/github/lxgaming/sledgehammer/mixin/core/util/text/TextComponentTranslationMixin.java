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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentTranslationFormatException;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = TextComponentTranslation.class, priority = 1337)
public abstract class TextComponentTranslationMixin {
    
    @Shadow
    private List<ITextComponent> children;
    
    @Shadow
    protected abstract void initializeFromFormat(String format);
    
    @Shadow
    public abstract String getKey();
    
    @Redirect(method = "ensureInitialized",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/util/text/TextComponentTranslation;initializeFromFormat(Ljava/lang/String;)V",
                    ordinal = 1
            )
    )
    private void onEnsureInitialized(TextComponentTranslation component, String format) {
        try {
            initializeFromFormat(format);
        } catch (TextComponentTranslationFormatException ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error while initializing translation: {}", ex.getMessage());
            this.children.add(new TextComponentString(String.format("Invalid Translation: %s", getKey())).setStyle(new Style().setColor(TextFormatting.RED)));
        }
    }
}