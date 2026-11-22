
package net.racquo.raccoonsco.entity.ai.raccoon;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.racquo.raccoonsco.entity.custom.RaccoonEntity;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class RaccoonWashConcreteGoal extends Goal {

    private final RaccoonEntity raccoon;
    private final double speed;

    private ItemEntity targetPowder;

    public RaccoonWashConcreteGoal(RaccoonEntity raccoon, double speed) {
        this.raccoon = raccoon;
        this.speed = speed;

        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!raccoon.isTame()) return false;
        if (raccoon.isPostWashCoolingDown()) return false;
        if (raccoon.isInSittingPose()) return false;
        if (raccoon.isEating() || raccoon.isSleeping()) return false;
        if (!raccoon.grabbedStack.isEmpty()) return false;
        if (raccoon.isWashing()) return false;
        if (raccoon.isGrabbing()) return false;

        targetPowder = findNearestConcretePowder();

        return targetPowder != null;
    }

    @Override
    public void start() {
        if (targetPowder != null) {
            raccoon.getNavigation().moveTo(targetPowder, speed);
        }
    }

    @Override
    public void tick() {
        if (targetPowder == null || !targetPowder.isAlive()) {
            raccoon.getNavigation().stop();
            return;
        }

        double distance = raccoon.distanceTo(targetPowder);

        // Close enough to start the grabbing animation.
        if (distance <= 1.5F) {

            raccoon.getNavigation().stop();

            if (!raccoon.isGrabbing()
                    && raccoon.canGrab(targetPowder.getItem())) {

                raccoon.setGrabTarget(targetPowder);
                raccoon.startGrabSequence();
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return targetPowder != null
                && targetPowder.isAlive()
                && !raccoon.isWashing()
                && !raccoon.isGrabbing()
                && raccoon.grabbedStack.isEmpty()
                && !raccoon.isEating()
                && !raccoon.isSleeping();
    }

    @Override
    public void stop() {
        raccoon.getNavigation().stop();
        targetPowder = null;
    }

    // ---------------- HELPERS ----------------

    private ItemEntity findNearestConcretePowder() {

        List<ItemEntity> items = raccoon.level().getEntitiesOfClass(
                ItemEntity.class,
                raccoon.getBoundingBox().inflate(12),
                item -> {

                    ItemStack stack = item.getItem();

                    if (stack.isEmpty()) return false;

                    if (!(stack.getItem() instanceof BlockItem blockItem)) {
                        return false;
                    }

                    if (!(blockItem.getBlock() instanceof ConcretePowderBlock)) {
                        return false;
                    }

                    return raccoon.canGrab(stack);
                }
        );

        return items.stream()
                .min(Comparator.comparingDouble(raccoon::distanceTo))
                .orElse(null);
    }
}
