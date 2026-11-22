
package net.racquo.raccoonsco.entity.ai.raccoon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.racquo.raccoonsco.entity.custom.RaccoonEntity;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class RaccoonSeekShadeSleepGoal extends Goal {

    private final RaccoonEntity raccoon;

    private double targetX;
    private double targetY;
    private double targetZ;

    private final double speed;

    private static final int MAX_ATTEMPTS = 15;

    public RaccoonSeekShadeSleepGoal(RaccoonEntity raccoon, double speed) {
        this.raccoon = raccoon;
        this.speed = speed;

        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (raccoon.isTame()) return false;
        if (!raccoon.isFull()) return false;
        if (raccoon.isEating()) return false;
        if (raccoon.isSleeping()) return false;
        if (raccoon.isPanic()) return false;

        Vec3 target = findShade();

        if (target == null) return false;

        targetX = target.x;
        targetY = target.y;
        targetZ = target.z;

        return true;
    }

    @Override
    public void start() {
        raccoon.getNavigation().moveTo(
                targetX,
                targetY,
                targetZ,
                speed
        );
    }

    @Override
    public boolean canContinueToUse() {
        return !raccoon.isSleeping()
                && !raccoon.getNavigation().isDone();
    }

    @Override
    public void tick() {
        if (raccoon.isSleeping()) return;

        double dx = targetX - raccoon.getX();
        double dy = targetY - raccoon.getY();
        double dz = targetZ - raccoon.getZ();

        double distanceSq = dx * dx + dy * dy + dz * dz;

        // Start sleeping once within approximately one block.
        if (distanceSq < 1.0) {
            raccoon.getNavigation().stop();
            raccoon.startSleeping();
        }
    }

    @Override
    public void stop() {
        raccoon.getNavigation().stop();

        targetX = 0;
        targetY = 0;
        targetZ = 0;
    }

    @Nullable
    private Vec3 findShade() {
        BlockPos origin = raccoon.blockPosition();
        RandomSource random = raccoon.getRandom();

        for (int i = 0; i < MAX_ATTEMPTS; i++) {

            BlockPos candidate = origin.offset(
                    random.nextInt(25) - 12,
                    random.nextInt(9) - 4,
                    random.nextInt(25) - 12
            );

            // Must be protected from direct sunlight.
            if (raccoon.level().canSeeSky(candidate)) continue;

            // Must have sufficiently low light.
            if (raccoon.level().getMaxLocalRawBrightness(candidate) > 12) continue;

            // Must have a solid block underneath.
            BlockPos floor = candidate.below();

            if (!raccoon.level().getBlockState(floor)
                    .isFaceSturdy(raccoon.level(), floor, Direction.UP)) {
                continue;
            }

            // Sleeping position must be empty.
            if (!raccoon.level().getBlockState(candidate).isAir()) continue;

            return Vec3.atBottomCenterOf(candidate);
        }

        return null;
    }
}
