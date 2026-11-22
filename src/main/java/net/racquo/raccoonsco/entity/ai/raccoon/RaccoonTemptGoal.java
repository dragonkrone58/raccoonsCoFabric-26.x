
package net.racquo.raccoonsco.entity.ai.raccoon;

import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;
import net.racquo.raccoonsco.entity.custom.RaccoonEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class RaccoonTemptGoal extends TemptGoal {

    private final RaccoonEntity raccoon;

    private static final double BEG_DISTANCE = 2.5D;

    public RaccoonTemptGoal(
            RaccoonEntity raccoon,
            double speed,
            Ingredient ingredient
    ) {
        super(raccoon, speed, ingredient, false);
        this.raccoon = raccoon;
    }

    @Override
    public boolean canUse() {
        return !raccoon.isEating()
                && !raccoon.isFull()
                && !raccoon.isSleeping()
                && !raccoon.isGrabbing()
                && !raccoon.isWashing()
                && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !raccoon.isEating()
                && !raccoon.isFull()
                && !raccoon.isSleeping()
                && !raccoon.isGrabbing()
                && !raccoon.isWashing()
                && super.canContinueToUse();
    }

    @Override
    public void tick() {
        super.tick();

        // Find the nearest player for the begging behaviour.
        Player closestPlayer = raccoon.level().getNearestPlayer(
                raccoon.getX(),
                raccoon.getY(),
                raccoon.getZ(),
                BEG_DISTANCE,
                player -> true
        );

        if (closestPlayer == null) {
            raccoon.stopBegging();
            return;
        }

        if (raccoon.isEating()
                || raccoon.isFull()
                || raccoon.isSleeping()
                || raccoon.isGrabbing()
                || raccoon.isWashing()) {
            raccoon.stopBegging();
            return;
        }

        double distanceSq = raccoon.distanceToSqr(closestPlayer);

        if (distanceSq <= BEG_DISTANCE * BEG_DISTANCE) {
            raccoon.getNavigation().stop();
            raccoon.startBegging();

            raccoon.getLookControl().setLookAt(
                    closestPlayer,
                    30.0F,
                    30.0F
            );
        } else {
            raccoon.stopBegging();
        }
    }

    @Override
    public void stop() {
        super.stop();
        raccoon.stopBegging();
    }
}
