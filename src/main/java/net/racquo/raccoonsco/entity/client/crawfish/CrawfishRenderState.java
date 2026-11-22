package net.racquo.raccoonsco.entity.client.crawfish;


import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;
import net.racquo.raccoonsco.entity.custom.CrawfishVariant;

public class CrawfishRenderState extends LivingEntityRenderState {
    public final AnimationState idleAnimationState = new AnimationState();

    public CrawfishVariant variant;
}
