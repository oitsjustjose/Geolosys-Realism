package com.oitsjustjose.natprog.common.items;

import com.oitsjustjose.natprog.NatProg;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class DynamicItemTier implements Tier {
    private int uses;
    private float speed;
    private float attackDamageBonus;
    private int harvestLvl;
    private int enchantability;
    private Ingredient repairIngredient;

    public DynamicItemTier() {
        this.uses = 0;
        this.speed = 0;
        this.attackDamageBonus = 0;
        this.harvestLvl = 0;
        this.enchantability = 0;
        this.repairIngredient = Ingredient.of(Items.BARRIER);
    }

    public DynamicItemTier setMaxUses(int maxUses) {
        this.uses = maxUses;
        return this;
    }

    public DynamicItemTier setEfficiency(float eff) {
        this.speed = eff;
        return this;
    }

    public DynamicItemTier setAttackDamage(float dmg) {
        this.attackDamageBonus = dmg;
        return this;
    }

    public DynamicItemTier setHarvestLvl(int lvl) {
        this.harvestLvl = lvl;
        return this;
    }

    public DynamicItemTier setEnchantability(int ench) {
        this.enchantability = ench;
        return this;
    }

    public DynamicItemTier setRepairMat(@Nullable TagKey<Item> tag) {
        if (tag == null) return this;

        var tagItems = Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).getTag(tag).stream().toList();
        if (tagItems.isEmpty()) {
            this.repairIngredient = Ingredient.of(Items.BARRIER);
            NatProg.getInstance().LOGGER.warn("Dynamic saw repair material {} could not be found. Defaulting to Bedrock", tag.toString());
        } else {
            this.repairIngredient = Ingredient.of(tag);
        }
        return this;
    }

    public DynamicItemTier setRepairMats(Item... items) {
        this.repairIngredient = Ingredient.of(items);
        return this;
    }

    @Override
    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamageBonus;
    }

    @Override
    public int getLevel() {
        return this.harvestLvl;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    @Nonnull
    public Ingredient getRepairIngredient() {
        return this.repairIngredient;
    }
}
