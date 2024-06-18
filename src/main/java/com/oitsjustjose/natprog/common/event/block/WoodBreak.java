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

public class WoodBreak {

    public static final TagKey<Item> CONSIDERED_AS_AXE = ItemTags.create(new ResourceLocation(Constants.MOD_ID, "considered_as_axe"));
    public static final TagKey<Block> IGNORED_WOOD_BLOCKS = BlockTags.create(new ResourceLocation(Constants.MOD_ID, "ignored_wood_blocks"));
    public static final TagKey<Block> WOOD_BLOCKS = BlockTags.create(new ResourceLocation(Constants.MOD_ID, "woods_requiring_tool"));


    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed evt) {
        Utils.breakHandler(evt, new Utils.BreakHandlerData(
                CommonConfig.ENABLE_WOOD_PUNCHING.get(),
                IGNORED_WOOD_BLOCKS,
                WOOD_BLOCKS,
                CONSIDERED_AS_AXE,
                ToolActions.AXE_DIG,
                DamageTypes.SPLINTERING,
                .10F,
                "natprog.wood.warning"
        ));
    }
}
