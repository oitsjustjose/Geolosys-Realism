package com.oitsjustjose.natprog;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class Constants {
    public static final String MOD_ID = "natprog";
    public static final List<ResourceLocation> PebbleMaterials = Lists.newArrayList(new ResourceLocation("minecraft", "stone"), new ResourceLocation("minecraft", "andesite"), new ResourceLocation("minecraft", "diorite"), new ResourceLocation("minecraft", "granite"), new ResourceLocation("minecraft", "sandstone"), new ResourceLocation("minecraft", "red_sandstone"), new ResourceLocation("minecraft", "tuff"), new ResourceLocation("minecraft", "deepslate"), new ResourceLocation("minecraft", "dripstone_block"), new ResourceLocation("minecraft", "netherrack"), new ResourceLocation("minecraft", "end_stone"), new ResourceLocation("quark", "marble"), new ResourceLocation("quark", "limestone"), new ResourceLocation("quark", "jasper"), new ResourceLocation("quark", "slate"), new ResourceLocation("quark", "basalt"), new ResourceLocation("create", "asurine"), new ResourceLocation("create", "crimsite"), new ResourceLocation("create", "limestone"), new ResourceLocation("create", "ochrum"), new ResourceLocation("create", "scorchia"), new ResourceLocation("create", "scoria"), new ResourceLocation("create", "veridium"));

    // Tag Keys for break speed changes
    public static final TagKey<Block> GROUND = BlockTags.create(new ResourceLocation(Constants.MOD_ID, "ground"));
    public static final TagKey<Block> IGNORED_STONE_BLOCKS = BlockTags.create(new ResourceLocation(Constants.MOD_ID, "ignored_stone_blocks"));
    public static final TagKey<Block> IGNORED_WOOD_BLOCKS = BlockTags.create(new ResourceLocation(Constants.MOD_ID, "ignored_wood_blocks"));
    public static final TagKey<Block> STONE_BLOCKS = BlockTags.create(new ResourceLocation(Constants.MOD_ID, "stones_requiring_tool"));
    public static final TagKey<Block> WOOD_BLOCKS = BlockTags.create(new ResourceLocation(Constants.MOD_ID, "woods_requiring_tool"));
    public static final TagKey<Item> CONSIDERED_AS_AXE = ItemTags.create(new ResourceLocation(Constants.MOD_ID, "considered_as_axe"));
    public static final TagKey<Item> CONSIDERED_AS_PICKAXE = ItemTags.create(new ResourceLocation(Constants.MOD_ID, "considered_as_pickaxe"));
}
