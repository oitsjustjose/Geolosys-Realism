package com.oitsjustjose.natprog.common.items;

import com.oitsjustjose.natprog.common.config.CommonConfig;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PebbleItem extends BlockItem {
    public PebbleItem(Block blockForm, Item.Properties props) {
        super(blockForm, props);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (Objects.requireNonNull(context.getPlayer()).getOffhandItem().getItem() instanceof PebbleItem) {
            if (context.getPlayer().getMainHandItem().getItem() instanceof PebbleItem) {
                return use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
            }
        }
        return super.useOn(context);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!CommonConfig.ENABLE_KNAPPING.get()) return super.use(level, player, hand);
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        if (!mainHand.isEmpty() && !offHand.isEmpty()) {
            if (mainHand.getItem() instanceof PebbleItem && offHand.getItem() instanceof PebbleItem) {
                InteractionHand swingArm = player.getRandom().nextBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

                if (level.isClientSide()) {
                    player.swing(swingArm);
                } else {
                    // 5% chance of knapping actually working:
                    if (player.getRandom().nextInt(100) <= 5) {
                        player.getItemInHand(swingArm).shrink(1);

                        if (player.getRandom().nextInt(100) < CommonConfig.FLINT_KNAP_CHANCE.get()) {
                            level.playSound(null, player.getOnPos(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1.0F, 0.5F);
                            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.FLINT, 1));
                        } else {
                            level.playSound(null, player.getOnPos(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 0.5F, 0.75F);
                        }
                    } else {
                        level.playSound(null, player.getOnPos(), SoundEvents.STONE_HIT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    public boolean isEnchantable(@NotNull ItemStack __) {
        return false;
    }
}
