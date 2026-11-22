package net.racquo.raccoonsco.entity.ai.raccoon;


import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.racquo.raccoonsco.entity.custom.RaccoonEntity;

import java.util.EnumSet;
import java.util.List;

public class RaccoonEatFoodGoal extends Goal {
    private final RaccoonEntity raccoon;
    private ItemEntity targetFood;
    private int repathCooldown = 0;

    public RaccoonEatFoodGoal(RaccoonEntity raccoon) {
        this.raccoon = raccoon;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse(){
        if (raccoon. isInSittingPose()) return false;
        if (raccoon.isSleeping() || raccoon.isWashing() || raccoon.isGrabbing()) return false;
        if(!raccoon.canEatDroppedFood()) return false;
        if(raccoon.isFull()) return false;


        List<ItemEntity> items = raccoon.level().getEntitiesOfClass(
                ItemEntity.class,
                raccoon.getBoundingBox().inflate(8.0),
                item -> raccoon.canEatItem(item.getItem())
        );

        if (items.isEmpty()) return false;

        targetFood = items.get(0);
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return targetFood != null
                && targetFood.isAlive()
                && !raccoon.isInSittingPose()
                && raccoon.distanceToSqr(targetFood) > 1.2
                && !raccoon.isSleeping()
                && !raccoon.isFull();
    }

    @Override
    public void start() {
        this.repathCooldown = 0;
    }

    @Override
    public void tick() {
        if(targetFood == null) { return; }

        raccoon.getLookControl().setLookAt(targetFood, 30.0F, 30.0F);

        // Repath every 10 ticks
        if (repathCooldown-- <= 0) {
            raccoon.getNavigation().moveTo(
                    targetFood.getX(),
                    targetFood.getY(),
                    targetFood.getZ(),
                    1.1D
            );
            repathCooldown = 10;
        }

        if (raccoon.distanceToSqr(targetFood) <= 2.5) {
            raccoon.consumeDroppedFood(targetFood);
            targetFood = null;
        }
    }

    @Override
    public void stop() {
        targetFood = null;
        raccoon.getNavigation().stop();
    }

}
