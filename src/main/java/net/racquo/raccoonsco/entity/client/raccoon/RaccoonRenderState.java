package net.racquo.raccoonsco.entity.client.raccoon;


import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;
import net.racquo.raccoonsco.entity.custom.RaccoonVariant;

public class RaccoonRenderState extends LivingEntityRenderState {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState sittingAnimationState = new AnimationState();
    public AnimationState eatingAnimationState = new AnimationState();
    public AnimationState sleepingAnimationState = new AnimationState();
    public AnimationState begAnimationState = new AnimationState();
    public AnimationState grabAnimationState = new AnimationState();
    public AnimationState washAnimationState = new AnimationState();

    public RaccoonVariant variant;
}
