package com.oitsjustjose.natprog.common.event;

import com.oitsjustjose.natprog.Constants;
import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockBreak {

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed evt) {
        processGroundBreak(evt);
        processStoneBreak(evt);
        processWoodBreak(evt);
    }

    /**
     * Processes the "Harder Ground Blocks" tweak when breaking ground blocks without the right tool
     *
     * @param evt the original player break event passed through
     */
    private void processGroundBreak(PlayerEvent.BreakSpeed evt) {
        if (!CommonConfig.MAKE_GROUND_BLOCKS_HARDER.get()) return;
        if (!evt.getState().is(Constants.GROUND)) return;
        if (evt.getEntity().getMainHandItem().isCorrectToolForDrops(evt.getState())) return;
        evt.setNewSpeed(evt.getOriginalSpeed() / 4);
    }

    /**
     * Processes the Stone Break tweak, preventing the process from succeeding if not using the right tool
     *
     * @param evt the original player break event passed through
     */
    private void processStoneBreak(PlayerEvent.BreakSpeed evt) {
        breakHandler(evt, new BreakHandlerData(CommonConfig.ENABLE_STONE_PUNCHING.get(), Constants.IGNORED_STONE_BLOCKS, Constants.STONE_BLOCKS, Constants.CONSIDERED_AS_PICKAXE, ToolActions.PICKAXE_DIG, DamageTypes.CRUSHING, .66F, "natprog.stone.warning"));
    }

    /**
     * Processes the Wood Break tweak, preventing the process from succeeding if not using the right tool
     *
     * @param evt the original player break event passed through
     */
    private void processWoodBreak(PlayerEvent.BreakSpeed evt) {
        breakHandler(evt, new BreakHandlerData(CommonConfig.ENABLE_WOOD_PUNCHING.get(), Constants.IGNORED_WOOD_BLOCKS, Constants.WOOD_BLOCKS, Constants.CONSIDERED_AS_AXE, ToolActions.AXE_DIG, DamageTypes.SPLINTERING, .50F, "natprog.wood.warning"));
    }

    /**
     * The master handler behind Wood & Stone Breaking logic.
     * Checks to see if the block isn't in the ignored tag and is an applicable blocks,
     * Checks to see if the player is holding either a tool that can effectively break said block *or* is added via tag
     * And if these criteria are met, *on the server side* the player receives damage based on the given frequency
     *
     * @param evt  the original player break event passed through
     * @param data the handler data containing skipHandling state, TagKeys, etc.
     */
    private void breakHandler(PlayerEvent.BreakSpeed evt, BreakHandlerData data) {
        if (data.skipHandling) return;
        // Standard null checking
        if (evt.getState() == null || evt.getEntity() == null || evt.getPosition().isEmpty()) return;
        // Block isn't applicable
        if (!evt.getState().is(data.appliedBlocks)) return;
        // Block was set to be ignored
        if (evt.getState().is(data.ignoredBlocks)) return;

        var level = evt.getEntity().level();
        var heldItem = evt.getEntity().getMainHandItem();
        // Item was manually added as an applicable tool via tag
        if (heldItem.is(data.addedTools)) return;
        // Item is naturally able to do the required action
        if (heldItem.canPerformAction(data.requiredAction)) return;
        evt.setCanceled(true);

        // ONLY do these things on the server-side, otherwise using Jade will cause you to take damage by using your eyeballs
        if (!level.isClientSide()) {
            // Random chance to even perform the hurt anim if the player is empty-handed
            if (CommonConfig.INCORRECT_TOOL_DAMAGE.get() && evt.getEntity().getMainHandItem().isEmpty() && evt.getEntity().getRandom().nextInt(25) == 1) {
                // And when it's shown, random chance to actually hurt from breaking bones
                if (level.getRandom().nextDouble() <= data.hurtChance) {
                    evt.getEntity().hurt(DamageTypes.getDamageSource(level, data.damageType), 1F);
                } else {
                    NatProg.proxy.doHurtAnimation(evt.getEntity());
                }
            }

            // Show breaking help, if applicable
            if (CommonConfig.SHOW_BREAKING_HELP.get()) {
                evt.getEntity().displayClientMessage(Component.translatable(data.breakHelpKey), true);
            }
        }
    }

    private record BreakHandlerData(boolean skipHandling, TagKey<Block> ignoredBlocks, TagKey<Block> appliedBlocks,
                                    TagKey<Item> addedTools, ToolAction requiredAction,
                                    ResourceKey<DamageType> damageType, float hurtChance, String breakHelpKey) {
    }
}
