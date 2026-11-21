package net.racquo.raccoonsco.block.custom;


import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.racquo.raccoonsco.sound.ModSounds;

public class SwampyReedsBlock extends DoublePlantBlock {
    public SwampyReedsBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void entityInside(
            BlockState state,
            Level level,
            BlockPos pos,
            Entity entity,
            net.minecraft.world.entity.InsideBlockEffectApplier handler,
            boolean bl
    ) {
        super.entityInside(state, level, pos, entity, handler, bl);

        // Only trigger for players
        if (entity instanceof Player player) {

            // Only play occasionally, avoiding sound spam
            if (!level.isClientSide()
                    && player.hasMovedHorizontallyRecently()
                    && player.tickCount % 15 == 0) {

                level.playSound(
                        null,
                        pos,
                        ModSounds.REED_RUSTLE,
                        SoundSource.BLOCKS,
                        0.3f,
                        1.0f + level.getRandom().nextFloat() * 0.2f
                );
            }
        }
    }
}
