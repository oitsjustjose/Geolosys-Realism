package com.oitsjustjose.natprog.common.event;

import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TwigPlacement {

    @SubscribeEvent
    public void registerEvent(PlayerInteractEvent.RightClickBlock event) {
        if (!CommonConfig.ARE_TWIGS_PLACEABLE.get()) return;

        if (event.getItemStack().getItem() != Items.STICK) return;

        var ctx = new BlockPlaceContext(event.getEntity(), event.getHand(), event.getItemStack(), event.getHitVec());
        var result = NatProg.getInstance().REGISTRY.stickBlockItem.get().place(ctx);
        if (result != InteractionResult.FAIL)
        {
            event.getEntity().swing(event.getHand());
        }
    }
}
