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

package io.github.lxgaming.sledgehammer.command;

import com.google.common.collect.Lists;
import io.github.lxgaming.sledgehammer.util.Text;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class BookCommand extends AbstractCommand {
    
    public BookCommand() {
        addAlias("book");
        setPermission("sledgehammer.book.base");
        setUsage("<Written|Writable> <Pages>");
    }
    
    @Override
    public void execute(ICommandSender commandSender, List<String> arguments) {
        if (!(commandSender instanceof EntityPlayer)) {
            commandSender.sendMessage(Text.of(Toolbox.getTextPrefix(), TextFormatting.RED, "This command can only be executed by players."));
            return;
        }
        
        EntityPlayer player = (EntityPlayer) commandSender;
        if (arguments.size() != 2) {
            player.sendMessage(Text.of(Toolbox.getTextPrefix(), TextFormatting.RED, "Invalid arguments: " + getUsage()));
            return;
        }
        
        Item item = getType(arguments.remove(0));
        if (item == null) {
            player.sendMessage(Text.of(Toolbox.getTextPrefix(), TextFormatting.RED, "Invalid type"));
            return;
        }
        
        Integer pages = Toolbox.parseInteger(arguments.remove(0)).orElse(null);
        if (pages == null) {
            player.sendMessage(Text.of(Toolbox.getTextPrefix(), TextFormatting.RED, "Failed to parse pages"));
            return;
        }
        
        if (pages < 0 || pages > 3072) {
            player.sendMessage(Text.of(Toolbox.getTextPrefix(), TextFormatting.RED, "Value is outside of the allowed range (0 ~ 3072)"));
            return;
        }
        
        ItemStack itemStack = generateBook(item, pages, player.getName(), String.valueOf(System.currentTimeMillis()));
        if (itemStack == null) {
            player.sendMessage(Text.of(Toolbox.getTextPrefix(), TextFormatting.RED, "Failed to generate book"));
            return;
        }
        
        if (player.inventory.addItemStackToInventory(itemStack)) {
            player.sendMessage(Text.of(Toolbox.getTextPrefix(),
                    TextFormatting.GREEN, "Generated ",
                    TextFormatting.AQUA, Toolbox.getResourceLocation(item).map(ResourceLocation::getPath).orElse("Unknown"),
                    TextFormatting.GREEN, " with ",
                    TextFormatting.AQUA, pages,
                    TextFormatting.GREEN, " " + Toolbox.formatUnit(pages, "page", "pages")
            ));
        } else {
            player.sendMessage(Text.of(Toolbox.getTextPrefix(), TextFormatting.RED, "Failed to add ItemStack to your Inventory"));
        }
    }
    
    private ItemStack generateBook(Item item, int size, String author, String title) {
        List<String> pages = Lists.newArrayList();
        for (int index = 0; index < size; index++) {
            pages.add(RandomStringUtils.random(255, true, true));
        }
        
        ItemStack itemStack = new ItemStack(item, 1);
        if (item == Items.WRITABLE_BOOK) {
            NBTTagList nbtTagList = new NBTTagList();
            pages.stream()
                    .map(NBTTagString::new)
                    .forEach(nbtTagList::appendTag);
            
            itemStack.setTagInfo("pages", nbtTagList);
            return itemStack;
        }
        
        if (item == Items.WRITTEN_BOOK) {
            NBTTagList nbtTagList = new NBTTagList();
            pages.stream()
                    .map(TextComponentString::new)
                    .map(ITextComponent.Serializer::componentToJson)
                    .map(NBTTagString::new)
                    .forEach(nbtTagList::appendTag);
            
            itemStack.setTagInfo("pages", nbtTagList);
            itemStack.setTagInfo("author", new NBTTagString(author));
            itemStack.setTagInfo("title", new NBTTagString(title));
            return itemStack;
        }
        
        return null;
    }
    
    private Item getType(String type) {
        if (StringUtils.equalsIgnoreCase(type, "written")) {
            return Items.WRITTEN_BOOK;
        }
        
        if (StringUtils.equalsIgnoreCase(type, "writable")) {
            return Items.WRITABLE_BOOK;
        }
        
        return null;
    }
}