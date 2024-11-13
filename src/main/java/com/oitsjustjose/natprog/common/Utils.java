package com.oitsjustjose.natprog.common;

import com.oitsjustjose.natprog.NatProg;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import static com.oitsjustjose.natprog.Constants.GROUND;

public class Utils {


    public static Block getPebbleForPos(WorldGenLevel level, BlockPos pos) {
        var mapper = NatProg.getInstance().REGISTRY.Mapper;
        var search = new BlockPos(pos.getX(), level.getHeight(), pos.getZ());
        for (var y = level.getHeight() / 2; y < search.getY(); y++) {
            var at = level.getBlockState(search.below(y));
            if (at.getBlock() == Blocks.STONE || at.getBlock() == Blocks.DEEPSLATE || at.isAir()) {
                continue;
            }

            var resloc = ForgeRegistries.BLOCKS.getKey(at.getBlock());
            if (mapper.containsKey(resloc)) return mapper.get(resloc).get();
        }

        // Fallback, a choice of Deepslate or Stone
        var choice = level.getRandom().nextBoolean() ? Blocks.STONE : Blocks.DEEPSLATE;
        var resloc = ForgeRegistries.BLOCKS.getKey(choice);
        return mapper.get(resloc).get();
    }

    /**
     * @param level an ISeedReader instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block is water (since we can waterlog)
     */
    public static boolean isInWater(WorldGenLevel level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() == Blocks.WATER;
    }

    /**
     * @param level an ISeedReader instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block is in a non-water fluid
     */
    public static boolean inNonWaterFluid(WorldGenLevel level, BlockPos pos) {
        return (!level.getBlockState(pos).getFluidState().isEmpty()) && !isInWater(level, pos);
    }

    @Nullable
    public static BlockPos getTopLevelPlacePos(WorldGenLevel level, ChunkPos chunkPos) {
        return getTopLevelPlacePos(level, chunkPos, -1);
    }

    @Nullable
    public static BlockPos getTopLevelPlacePos(WorldGenLevel level, ChunkPos chunkPos, int spread) {

        if (!(level instanceof WorldGenRegion region)) {
            return null;
        }

        var usedSpread = Math.max(8, spread);
        var xCenter = (chunkPos.getMinBlockX() + chunkPos.getMaxBlockX()) / 2;
        var zCenter = (chunkPos.getMinBlockZ() + chunkPos.getMaxBlockZ()) / 2;

        // Only put things in the negative X|Z if the spread is provided.
        var blockPosX = xCenter + (level.getRandom().nextInt(usedSpread) * ((level.getRandom().nextBoolean()) ? 1 : -1));
        var blockPosZ = zCenter + (level.getRandom().nextInt(usedSpread) * ((level.getRandom().nextBoolean()) ? 1 : -1));

        if (!region.hasChunk(chunkPos.x, chunkPos.z)) {
            return null;
        }

        var searchPos = new BlockPos(blockPosX, region.getHeight(), blockPosZ);

        // With worlds being so much deeper,
        // it makes most sense to take a top-down approach
        while (searchPos.getY() > region.getMinBuildHeight()) {
            // BlockState blockToPlaceOn = world.getBlockState(searchPos);
            // Check if the location itself is solid
            if (canPlaceOn(level, searchPos)) {
                // Then check if the block above it is either air, or replacable
                var actualPlacePos = searchPos.above();
                if (canReplace(region, actualPlacePos)) {
                    return actualPlacePos;
                }
            }
            searchPos = searchPos.below();
        }

        return null;
    }

    /**
     * Determines if the sample can be placed on this block
     *
     * @param level: A WorldGenLevel instance
     * @param pos:   The current searching position that will be used to confirm
     * @return true if the block below is solid on top AND isn't in the blacklist
     */
    public static boolean canPlaceOn(WorldGenLevel level, BlockPos pos) {
        var state = level.getBlockState(pos);
        return Block.isShapeFullBlock(state.getShape(level, pos)) && state.is(GROUND);
    }

    /**
     * @param level an ISeedReader instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block at pos is replaceable
     */
    public static boolean canReplace(WorldGenLevel level, BlockPos pos) {
        var state = level.getBlockState(pos);
        return !state.getFluidState().isEmpty() || state.isAir() || state.is(BlockTags.LEAVES) || state.canBeReplaced();
    }

    /**
     * Fixes the state for SNOWY when replacing a block above
     *
     * @param level     an ISeedReader instance
     * @param posPlaced The position where we placed a block
     */
    public static void fixSnowyBlock(WorldGenLevel level, BlockPos posPlaced) {
        var below = level.getBlockState(posPlaced.below());
        if (below.hasProperty(BlockStateProperties.SNOWY)) {
            level.setBlock(posPlaced.below(), below.setValue(BlockStateProperties.SNOWY, Boolean.FALSE), 2 | 16);
        }
    }
}