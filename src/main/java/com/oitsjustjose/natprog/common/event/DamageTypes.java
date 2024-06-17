package com.oitsjustjose.natprog.common.event;

import com.oitsjustjose.natprog.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class DamageTypes {

    public static final ResourceKey<DamageType> SPLINTERING = register("splintering");
    public static final ResourceKey<DamageType> CRUSHING = register("crushing");

    private static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Constants.MOD_ID, name));
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> key) {
        return level.damageSources().source(key);
    }
}
