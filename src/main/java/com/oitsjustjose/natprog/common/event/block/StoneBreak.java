package com.oitsjustjose.natprog.common.event.block;

import com.oitsjustjose.natprog.Constants;
import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.common.event.DamageTypes;
import net.minecraft.network.chat.Component;
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
        if (CommonConfig.ENABLE_STONE_PUNCHING.get()) return;
        if (evt.getState() == null || evt.getEntity() == null || evt.getPosition().isEmpty()) return;
        if (evt.getState().is(IGNORED_STONE_BLOCKS) || !evt.getState().is(STONE_BLOCKS)) return;
        if (evt.getEntity().level().isClientSide()) return;

        var level = evt.getEntity().level();
        var heldItem = evt.getEntity().getMainHandItem();
        if (heldItem.is(CONSIDERED_AS_PICKAXE)) return;
        if (heldItem.canPerformAction(ToolActions.PICKAXE_DIG)) return;
        evt.setCanceled(true);

        // Random chance to even perform the hurt anim if the player is empty-handed
        if (evt.getEntity().getMainHandItem().isEmpty() && evt.getEntity().getRandom().nextInt(25) == 1 && CommonConfig.INCORRECT_TOOL_DAMAGE.get()) {
            // And when it's shown, random chance to actually hurt from breaking bones
            if (evt.getEntity().getRandom().nextInt(2) == 1) {
                evt.getEntity().hurt(DamageTypes.getDamageSource(level, DamageTypes.CRUSHING), 1F);
            } else {
                NatProg.proxy.doHurtAnimation(evt.getEntity());
            }
        }

        // Show breaking help, if applicable
        if (!CommonConfig.SHOW_BREAKING_HELP.get()) return;
        evt.getEntity().displayClientMessage(Component.translatable("natprog.stone.warning"), true);
    }
}
