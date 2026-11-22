package net.racquo.raccoonsco.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.racquo.raccoonsco.block.ModBlocks;
import org.jetbrains.annotations.NotNull;

public class SwampGrassBlock extends FlowerBlock implements BonemealableBlock {
    public SwampGrassBlock(Holder<@NotNull MobEffect> effect, float effectSeconds, Properties properties) {
        super(effect, effectSeconds, properties);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, @NotNull BlockState state) {
        return level.isEmptyBlock(pos.above());
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random,
                                     @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random,
                                @NotNull BlockPos pos, @NotNull BlockState state) {
        DoublePlantBlock.placeAt(level, ModBlocks.SWAMPY_REEDS.defaultBlockState(),
                pos, Block.UPDATE_ALL);
    }
}
