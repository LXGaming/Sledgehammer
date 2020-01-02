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
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TextAdapterTest {
    
    @Test
    public void testColor() {
        ITextComponent legacyTextComponent = TextAdapter.serializeLegacy("&00&11&22&33&44&55&66&77&88&99&aa&bb&cc&dd&ee&ff");
        
        ITextComponent nativeTextComponent = new EmptyTextComponent();
        nativeTextComponent.appendSibling(new TextComponentString("0").setStyle(new Style().setColor(TextFormatting.BLACK)));
        nativeTextComponent.appendSibling(new TextComponentString("1").setStyle(new Style().setColor(TextFormatting.DARK_BLUE)));
        nativeTextComponent.appendSibling(new TextComponentString("2").setStyle(new Style().setColor(TextFormatting.DARK_GREEN)));
        nativeTextComponent.appendSibling(new TextComponentString("3").setStyle(new Style().setColor(TextFormatting.DARK_AQUA)));
        nativeTextComponent.appendSibling(new TextComponentString("4").setStyle(new Style().setColor(TextFormatting.DARK_RED)));
        nativeTextComponent.appendSibling(new TextComponentString("5").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
        nativeTextComponent.appendSibling(new TextComponentString("6").setStyle(new Style().setColor(TextFormatting.GOLD)));
        nativeTextComponent.appendSibling(new TextComponentString("7").setStyle(new Style().setColor(TextFormatting.GRAY)));
        nativeTextComponent.appendSibling(new TextComponentString("8").setStyle(new Style().setColor(TextFormatting.DARK_GRAY)));
        nativeTextComponent.appendSibling(new TextComponentString("9").setStyle(new Style().setColor(TextFormatting.BLUE)));
        nativeTextComponent.appendSibling(new TextComponentString("a").setStyle(new Style().setColor(TextFormatting.GREEN)));
        nativeTextComponent.appendSibling(new TextComponentString("b").setStyle(new Style().setColor(TextFormatting.AQUA)));
        nativeTextComponent.appendSibling(new TextComponentString("c").setStyle(new Style().setColor(TextFormatting.RED)));
        nativeTextComponent.appendSibling(new TextComponentString("d").setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
        nativeTextComponent.appendSibling(new TextComponentString("e").setStyle(new Style().setColor(TextFormatting.YELLOW)));
        nativeTextComponent.appendSibling(new TextComponentString("f").setStyle(new Style().setColor(TextFormatting.WHITE)));
        
        String legacyText = ITextComponent.Serializer.componentToJson(legacyTextComponent);
        String nativeText = ITextComponent.Serializer.componentToJson(nativeTextComponent);
        
        Assertions.assertNotNull(legacyText);
        Assertions.assertNotNull(nativeText);
        Assertions.assertEquals(legacyText, nativeText);
    }
    
    @Test
    public void testStyle() {
        ITextComponent legacyTextComponent = TextAdapter.serializeLegacy("&kk&r&ll&r&mm&r&nn&r&oo&rr");
        
        ITextComponent nativeTextComponent = new EmptyTextComponent();
        nativeTextComponent.appendSibling(new TextComponentString("k").setStyle(new Style().setObfuscated(true)));
        nativeTextComponent.appendSibling(new TextComponentString("l").setStyle(new Style().setBold(true)));
        nativeTextComponent.appendSibling(new TextComponentString("m").setStyle(new Style().setStrikethrough(true)));
        nativeTextComponent.appendSibling(new TextComponentString("n").setStyle(new Style().setUnderlined(true)));
        nativeTextComponent.appendSibling(new TextComponentString("o").setStyle(new Style().setItalic(true)));
        nativeTextComponent.appendSibling(new TextComponentString("r"));
        
        String legacyText = ITextComponent.Serializer.componentToJson(legacyTextComponent);
        String nativeText = ITextComponent.Serializer.componentToJson(nativeTextComponent);
        
        Assertions.assertNotNull(legacyText);
        Assertions.assertNotNull(nativeText);
        Assertions.assertEquals(legacyText, nativeText);
    }
    
    @Test
    public void testLink() {
        ITextComponent legacyTextComponent = TextAdapter.serializeLegacyWithLinks(Sledgehammer.WEBSITE);
        
        ITextComponent nativeTextComponent = new EmptyTextComponent();
        ITextComponent textComponent = new TextComponentString(Sledgehammer.WEBSITE);
        textComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Sledgehammer.WEBSITE));
        textComponent.getStyle().setColor(TextFormatting.BLUE);
        nativeTextComponent.appendSibling(textComponent);
        
        String legacyText = ITextComponent.Serializer.componentToJson(legacyTextComponent);
        String nativeText = ITextComponent.Serializer.componentToJson(nativeTextComponent);
        
        Assertions.assertNotNull(legacyText);
        Assertions.assertNotNull(nativeText);
        Assertions.assertEquals(legacyText, nativeText);
    }
}