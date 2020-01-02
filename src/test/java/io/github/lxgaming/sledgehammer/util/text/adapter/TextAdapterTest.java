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
        
        ITextComponent nativeTextComponent = new EmptyTextComponent();
        nativeTextComponent.appendSibling(new StringTextComponent("0").applyTextStyle(TextFormatting.BLACK));
        nativeTextComponent.appendSibling(new StringTextComponent("1").applyTextStyle(TextFormatting.DARK_BLUE));
        nativeTextComponent.appendSibling(new StringTextComponent("2").applyTextStyle(TextFormatting.DARK_GREEN));
        nativeTextComponent.appendSibling(new StringTextComponent("3").applyTextStyle(TextFormatting.DARK_AQUA));
        nativeTextComponent.appendSibling(new StringTextComponent("4").applyTextStyle(TextFormatting.DARK_RED));
        nativeTextComponent.appendSibling(new StringTextComponent("5").applyTextStyle(TextFormatting.DARK_PURPLE));
        nativeTextComponent.appendSibling(new StringTextComponent("6").applyTextStyle(TextFormatting.GOLD));
        nativeTextComponent.appendSibling(new StringTextComponent("7").applyTextStyle(TextFormatting.GRAY));
        nativeTextComponent.appendSibling(new StringTextComponent("8").applyTextStyle(TextFormatting.DARK_GRAY));
        nativeTextComponent.appendSibling(new StringTextComponent("9").applyTextStyle(TextFormatting.BLUE));
        nativeTextComponent.appendSibling(new StringTextComponent("a").applyTextStyle(TextFormatting.GREEN));
        nativeTextComponent.appendSibling(new StringTextComponent("b").applyTextStyle(TextFormatting.AQUA));
        nativeTextComponent.appendSibling(new StringTextComponent("c").applyTextStyle(TextFormatting.RED));
        nativeTextComponent.appendSibling(new StringTextComponent("d").applyTextStyle(TextFormatting.LIGHT_PURPLE));
        nativeTextComponent.appendSibling(new StringTextComponent("e").applyTextStyle(TextFormatting.YELLOW));
        nativeTextComponent.appendSibling(new StringTextComponent("f").applyTextStyle(TextFormatting.WHITE));
        
        String legacyText = TextComponent.Serializer.toJson(legacyTextComponent);
        String nativeText = TextComponent.Serializer.toJson(nativeTextComponent);
        
        Assertions.assertNotNull(legacyText);
        Assertions.assertNotNull(nativeText);
        Assertions.assertEquals(legacyText, nativeText);
    }
    
    @Test
    public void testStyle() {
        ITextComponent legacyTextComponent = TextAdapter.serializeLegacy("&kk&r&ll&r&mm&r&nn&r&oo&rr");
        
        ITextComponent nativeTextComponent = new EmptyTextComponent();
        nativeTextComponent.appendSibling(new StringTextComponent("k").applyTextStyle(TextFormatting.OBFUSCATED));
        nativeTextComponent.appendSibling(new StringTextComponent("l").applyTextStyle(TextFormatting.BOLD));
        nativeTextComponent.appendSibling(new StringTextComponent("m").applyTextStyle(TextFormatting.STRIKETHROUGH));
        nativeTextComponent.appendSibling(new StringTextComponent("n").applyTextStyle(TextFormatting.UNDERLINE));
        nativeTextComponent.appendSibling(new StringTextComponent("o").applyTextStyle(TextFormatting.ITALIC));
        nativeTextComponent.appendSibling(new StringTextComponent("r").applyTextStyle(TextFormatting.RESET));
        
        String legacyText = TextComponent.Serializer.toJson(legacyTextComponent);
        String nativeText = TextComponent.Serializer.toJson(nativeTextComponent);
        
        Assertions.assertNotNull(legacyText);
        Assertions.assertNotNull(nativeText);
        Assertions.assertEquals(legacyText, nativeText);
    }
    
    @Test
    public void testLink() {
        ITextComponent legacyTextComponent = TextAdapter.serializeLegacyWithLinks(Sledgehammer.WEBSITE);
        
        ITextComponent nativeTextComponent = new EmptyTextComponent();
        TextComponent textComponent = new StringTextComponent(Sledgehammer.WEBSITE);
        textComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Sledgehammer.WEBSITE));
        textComponent.getStyle().setColor(TextFormatting.BLUE);
        nativeTextComponent.appendSibling(textComponent);
        
        String legacyText = TextComponent.Serializer.toJson(legacyTextComponent);
        String nativeText = TextComponent.Serializer.toJson(nativeTextComponent);
        
        Assertions.assertNotNull(legacyText);
        Assertions.assertNotNull(nativeText);
        Assertions.assertEquals(legacyText, nativeText);
    }
}