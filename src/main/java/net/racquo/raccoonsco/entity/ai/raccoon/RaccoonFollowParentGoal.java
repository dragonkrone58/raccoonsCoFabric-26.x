package net.racquo.raccoonsco.entity.ai.raccoon;


import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.racquo.raccoonsco.entity.custom.RaccoonEntity;

public class RaccoonFollowParentGoal extends FollowParentGoal {

    private final RaccoonEntity raccoon;

    public RaccoonFollowParentGoal(RaccoonEntity raccoon, double speed) {
        super(raccoon, speed);
        this.raccoon = raccoon;
    }

    @Override
    public boolean canUse() {
        //Sitting raccoons should never follow
        if (raccoon.isInSittingPose()) {
            return false;
        }

        // Tamed babies should not auto-follow
        if (raccoon.isTame()) {
            return false;
        }

        return super.canUse();
    }
}
