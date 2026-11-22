package net.racquo.raccoonsco.entity.ai.raccoon;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.racquo.raccoonsco.entity.custom.RaccoonEntity;
import net.racquo.raccoonsco.sound.ModSounds;

public class RaccoonAttackGoal extends MeleeAttackGoal {

    private final RaccoonEntity raccoon;

    public RaccoonAttackGoal(RaccoonEntity raccoon, double speed) {
        super(raccoon, speed, true);
        this.raccoon = raccoon;
    }

    @Override
    public boolean canUse() {
        return !raccoon.isTame()
                && !raccoon.isFull()
                && !raccoon.isSleeping()
                && super.canUse();
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target) {
        if (this.canPerformAttack(target)) {
            this.resetAttackCooldown();
            this.mob.doHurtTarget(getServerLevel(this.mob), target);
            raccoon.playSound(ModSounds.RACCOON_BITES, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !raccoon.isTame()
                && !raccoon.isFull()
                && super.canContinueToUse();
    }
}
