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

package io.github.lxgaming.sledgehammer.commands;

import com.google.common.collect.Lists;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.stream.Collectors;

public class BookCommand extends AbstractCommand {
    
    public BookCommand() {
        addAlias("book");
        setPermission("sledgehammer.book.base");
        setUsage("<Written|Writable> <Pages>");
    }
    
    @Override
    public CommandResult execute(CommandSource commandSource, List<String> arguments) {
        if (!(commandSource instanceof Player)) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "This command can only be executed by players."));
            return CommandResult.empty();
        }
        
        Player player = (Player) commandSource;
        if (arguments.size() != 2) {
            player.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Invalid arguments: ", getUsage()));
            return CommandResult.empty();
        }
        
        ItemType itemType = getType(arguments.remove(0));
        if (itemType == ItemTypes.AIR) {
            player.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Invalid type"));
            return CommandResult.empty();
        }
        
        Integer pages = Toolbox.parseInteger(arguments.remove(0)).orElse(null);
        if (pages == null) {
            player.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Failed to parse pages"));
            return CommandResult.empty();
        }
        
        if (pages < 0 || pages > 3072) {
            player.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Value is outside of the allowed range (0 ~ 3072)"));
            return CommandResult.empty();
        }
        
        ItemStack itemStack = generateBook(itemType, pages);
        if (itemStack.getType() == ItemTypes.NONE) {
            player.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Failed to generate book"));
            return CommandResult.empty();
        }
        
        if (player.getInventory().offer(itemStack).getType() == InventoryTransactionResult.Type.SUCCESS) {
            player.sendMessage(Text.of(Toolbox.getTextPrefix(),
                    TextColors.GREEN, "Generated ",
                    TextColors.AQUA, StringUtils.substringAfter(itemType.getId(), ":"),
                    TextColors.GREEN, " with ",
                    TextColors.AQUA, pages, " ",
                    TextColors.GREEN, Toolbox.formatUnit(pages, "page", "pages")));
            return CommandResult.success();
        } else {
            player.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Failed to offer ItemStack"));
            return CommandResult.empty();
        }
    }
    
    private ItemStack generateBook(ItemType itemType, int size) {
        List<String> pages = Lists.newArrayList();
        for (int index = 0; index < size; index++) {
            pages.add(RandomStringUtils.random(255, true, true));
        }
        
        ItemStack itemStack = ItemStack.of(itemType, 1);
        if (itemType == ItemTypes.WRITABLE_BOOK) {
            itemStack.offer(Keys.PLAIN_BOOK_PAGES, pages);
            return itemStack;
        }
        
        if (itemType == ItemTypes.WRITTEN_BOOK) {
            itemStack.offer(Keys.BOOK_PAGES, pages.stream().map(Text::of).collect(Collectors.toList()));
            itemStack.offer(Keys.BOOK_AUTHOR, Text.of(System.currentTimeMillis()));
            return itemStack;
        }
        
        return ItemStack.empty();
    }
    
    private ItemType getType(String type) {
        if (StringUtils.equalsIgnoreCase(type, "written")) {
            return ItemTypes.WRITTEN_BOOK;
        }
        
        if (StringUtils.equalsIgnoreCase(type, "writable")) {
            return ItemTypes.WRITABLE_BOOK;
        }
        
        return ItemTypes.NONE;
    }
}