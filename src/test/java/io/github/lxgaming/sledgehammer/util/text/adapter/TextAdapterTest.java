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
        nativeTextComponent.append(new StringTextComponent("0").withStyle(TextFormatting.BLACK));
        nativeTextComponent.append(new StringTextComponent("1").withStyle(TextFormatting.DARK_BLUE));
        nativeTextComponent.append(new StringTextComponent("2").withStyle(TextFormatting.DARK_GREEN));
        nativeTextComponent.append(new StringTextComponent("3").withStyle(TextFormatting.DARK_AQUA));
        nativeTextComponent.append(new StringTextComponent("4").withStyle(TextFormatting.DARK_RED));
        nativeTextComponent.append(new StringTextComponent("5").withStyle(TextFormatting.DARK_PURPLE));
        nativeTextComponent.append(new StringTextComponent("6").withStyle(TextFormatting.GOLD));
        nativeTextComponent.append(new StringTextComponent("7").withStyle(TextFormatting.GRAY));
        nativeTextComponent.append(new StringTextComponent("8").withStyle(TextFormatting.DARK_GRAY));
        nativeTextComponent.append(new StringTextComponent("9").withStyle(TextFormatting.BLUE));
        nativeTextComponent.append(new StringTextComponent("a").withStyle(TextFormatting.GREEN));
        nativeTextComponent.append(new StringTextComponent("b").withStyle(TextFormatting.AQUA));
        nativeTextComponent.append(new StringTextComponent("c").withStyle(TextFormatting.RED));
        nativeTextComponent.append(new StringTextComponent("d").withStyle(TextFormatting.LIGHT_PURPLE));
        nativeTextComponent.append(new StringTextComponent("e").withStyle(TextFormatting.YELLOW));
        nativeTextComponent.append(new StringTextComponent("f").withStyle(TextFormatting.WHITE));
        
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
        nativeTextComponent.append(new StringTextComponent("k").withStyle(TextFormatting.OBFUSCATED));
        nativeTextComponent.append(new StringTextComponent("l").withStyle(TextFormatting.BOLD));
        nativeTextComponent.append(new StringTextComponent("m").withStyle(TextFormatting.STRIKETHROUGH));
        nativeTextComponent.append(new StringTextComponent("n").withStyle(TextFormatting.UNDERLINE));
        nativeTextComponent.append(new StringTextComponent("o").withStyle(TextFormatting.ITALIC));
        nativeTextComponent.append(new StringTextComponent("r").withStyle(TextFormatting.RESET));
        
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
        textComponent.withStyle(style -> style
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Sledgehammer.WEBSITE))
                .applyFormat(TextFormatting.BLUE));
        nativeTextComponent.append(textComponent);
        
        String legacyText = TextComponent.Serializer.toJson(legacyTextComponent);
        String nativeText = TextComponent.Serializer.toJson(nativeTextComponent);
        
        Assertions.assertNotNull(legacyText);
        Assertions.assertNotNull(nativeText);
        Assertions.assertEquals(legacyText, nativeText);
    }
}