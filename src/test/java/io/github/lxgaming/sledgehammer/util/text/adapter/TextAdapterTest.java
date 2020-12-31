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

package io.github.lxgaming.sledgehammer.util.text.adapter;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.util.text.EmptyTextComponent;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TextAdapterTest {
    
    @Test
    public void testColor() {
        ITextComponent legacyTextComponent = TextAdapter.serializeLegacy("&00&11&22&33&44&55&66&77&88&99&aa&bb&cc&dd&ee&ff");
        
        IFormattableTextComponent nativeTextComponent = new EmptyTextComponent();
        nativeTextComponent.append(new StringTextComponent("0").mergeStyle(TextFormatting.BLACK));
        nativeTextComponent.append(new StringTextComponent("1").mergeStyle(TextFormatting.DARK_BLUE));
        nativeTextComponent.append(new StringTextComponent("2").mergeStyle(TextFormatting.DARK_GREEN));
        nativeTextComponent.append(new StringTextComponent("3").mergeStyle(TextFormatting.DARK_AQUA));
        nativeTextComponent.append(new StringTextComponent("4").mergeStyle(TextFormatting.DARK_RED));
        nativeTextComponent.append(new StringTextComponent("5").mergeStyle(TextFormatting.DARK_PURPLE));
        nativeTextComponent.append(new StringTextComponent("6").mergeStyle(TextFormatting.GOLD));
        nativeTextComponent.append(new StringTextComponent("7").mergeStyle(TextFormatting.GRAY));
        nativeTextComponent.append(new StringTextComponent("8").mergeStyle(TextFormatting.DARK_GRAY));
        nativeTextComponent.append(new StringTextComponent("9").mergeStyle(TextFormatting.BLUE));
        nativeTextComponent.append(new StringTextComponent("a").mergeStyle(TextFormatting.GREEN));
        nativeTextComponent.append(new StringTextComponent("b").mergeStyle(TextFormatting.AQUA));
        nativeTextComponent.append(new StringTextComponent("c").mergeStyle(TextFormatting.RED));
        nativeTextComponent.append(new StringTextComponent("d").mergeStyle(TextFormatting.LIGHT_PURPLE));
        nativeTextComponent.append(new StringTextComponent("e").mergeStyle(TextFormatting.YELLOW));
        nativeTextComponent.append(new StringTextComponent("f").mergeStyle(TextFormatting.WHITE));
        
        String legacyText = TextComponent.Serializer.toJson(legacyTextComponent);
        String nativeText = TextComponent.Serializer.toJson(nativeTextComponent);
        
        Assertions.assertNotNull(legacyText);
        Assertions.assertNotNull(nativeText);
        Assertions.assertEquals(legacyText, nativeText);
    }
    
    @Test
    public void testStyle() {
        ITextComponent legacyTextComponent = TextAdapter.serializeLegacy("&kk&r&ll&r&mm&r&nn&r&oo&rr");
        
        IFormattableTextComponent nativeTextComponent = new EmptyTextComponent();
        nativeTextComponent.append(new StringTextComponent("k").mergeStyle(TextFormatting.OBFUSCATED));
        nativeTextComponent.append(new StringTextComponent("l").mergeStyle(TextFormatting.BOLD));
        nativeTextComponent.append(new StringTextComponent("m").mergeStyle(TextFormatting.STRIKETHROUGH));
        nativeTextComponent.append(new StringTextComponent("n").mergeStyle(TextFormatting.UNDERLINE));
        nativeTextComponent.append(new StringTextComponent("o").mergeStyle(TextFormatting.ITALIC));
        nativeTextComponent.append(new StringTextComponent("r").mergeStyle(TextFormatting.RESET));
        
        String legacyText = TextComponent.Serializer.toJson(legacyTextComponent);
        String nativeText = TextComponent.Serializer.toJson(nativeTextComponent);
        
        Assertions.assertNotNull(legacyText);
        Assertions.assertNotNull(nativeText);
        Assertions.assertEquals(legacyText, nativeText);
    }
    
    @Test
    public void testLink() {
        ITextComponent legacyTextComponent = TextAdapter.serializeLegacyWithLinks(Sledgehammer.WEBSITE);
        
        IFormattableTextComponent nativeTextComponent = new EmptyTextComponent();
        IFormattableTextComponent textComponent = new StringTextComponent(Sledgehammer.WEBSITE);
        textComponent.modifyStyle(style -> style
                .setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Sledgehammer.WEBSITE))
                .applyFormatting(TextFormatting.BLUE));
        nativeTextComponent.append(textComponent);
        
        String legacyText = TextComponent.Serializer.toJson(legacyTextComponent);
        String nativeText = TextComponent.Serializer.toJson(nativeTextComponent);
        
        Assertions.assertNotNull(legacyText);
        Assertions.assertNotNull(nativeText);
        Assertions.assertEquals(legacyText, nativeText);
    }
}