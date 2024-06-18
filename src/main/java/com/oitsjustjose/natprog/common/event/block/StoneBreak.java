package com.oitsjustjose.natprog.common.event.block;

import com.oitsjustjose.natprog.Constants;
import com.oitsjustjose.natprog.common.Utils;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.common.event.DamageTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StoneBreak {

    public static final TagKey<Item> CONSIDERED_AS_PICKAXE = ItemTags.create(new ResourceLocation(Constants.MOD_ID, "considered_as_pickaxe"));
    public static final TagKey<Block> IGNORED_STONE_BLOCKS = BlockTags.create(new ResourceLocation(Constants.MOD_ID, "ignored_stone_blocks"));
    public static final TagKey<Block> STONE_BLOCKS = BlockTags.create(new ResourceLocation(Constants.MOD_ID, "stones_requiring_tool"));

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed evt) {
        Utils.breakHandler(evt, new Utils.BreakHandlerData(
                CommonConfig.ENABLE_STONE_PUNCHING.get(),
                IGNORED_STONE_BLOCKS,
                STONE_BLOCKS,
                CONSIDERED_AS_PICKAXE,
                ToolActions.PICKAXE_DIG,
                DamageTypes.CRUSHING,
                .50F,
                "natprog.stone.warning"
        ));
    }
}
