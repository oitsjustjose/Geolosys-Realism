package com.oitsjustjose.natprog.common.event;

import com.oitsjustjose.natprog.Constants;
import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class BoneEvent {

    public static final TagKey<EntityType<?>> ENTITY_THAT_DROPS_BONE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Constants.MOD_ID, "drops_bone"));
    private final HashMap<UUID, Long> playersLastRightClicked = new HashMap<>();
    private final Random rand = new Random();

    @SubscribeEvent
    public void onItemRightClick(PlayerInteractEvent.RightClickItem evt) {
        if (!CommonConfig.ENABLE_KNAPPING.get()) return;
        if (playersLastRightClicked.containsKey(evt.getEntity().getUUID())) {
            if ((System.currentTimeMillis() - playersLastRightClicked.get(evt.getEntity().getUUID())) < 200) {
                return;
            }
        }

        var boneHand = evt.getEntity().getMainHandItem().getItem() == Items.BONE ? InteractionHand.MAIN_HAND : evt.getEntity().getOffhandItem().getItem() == Items.BONE ? InteractionHand.OFF_HAND : null;
        var flintHand = evt.getEntity().getMainHandItem().getItem() == Items.FLINT ? InteractionHand.MAIN_HAND : evt.getEntity().getOffhandItem().getItem() == Items.FLINT ? InteractionHand.OFF_HAND : null;

        if (boneHand == null || flintHand == null) return;
        evt.getEntity().swing(flintHand);

        if (evt.getEntity().getRandom().nextInt(100) <= CommonConfig.BONE_KNAP_CHANCE.get()) {
            if (evt.getLevel().isClientSide()) {
                evt.getEntity().playSound(SoundEvents.PARROT_IMITATE_SKELETON, 1F, 0.7F);
            } else {
                var count = evt.getEntity().getRandom().nextInt(2) + 1;
                var shard = NatProg.getInstance().REGISTRY.boneShard.get();
                ItemHandlerHelper.giveItemToPlayer(evt.getEntity(), new ItemStack(shard, count));
                evt.getEntity().getItemInHand(boneHand).shrink(1);
            }
        }

        playersLastRightClicked.put(evt.getEntity().getUUID(), System.currentTimeMillis());
    }

    @SubscribeEvent
    public void livingDrop(LivingDropsEvent evt) {
        if (CommonConfig.BONE_DROP_CHANCE.get() == 0) return;
        if (evt.getEntity().getType().is(ENTITY_THAT_DROPS_BONE) || CommonConfig.ALL_ENTITIES_DROP_BONES.get()) {
            if (rand.nextInt(100) < CommonConfig.BONE_DROP_CHANCE.get()) {
                var drop = new ItemEntity(evt.getEntity().level(), evt.getEntity().getX(), evt.getEntity().getY(), evt.getEntity().getZ(), new ItemStack(Items.BONE));
                evt.getDrops().add(drop);
            }
        }
    }
}
