package net.racquo.raccoonsco.entity.client.raccoon;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.racquo.raccoonsco.RaccoonsCo;
import net.racquo.raccoonsco.entity.custom.RaccoonEntity;
import net.racquo.raccoonsco.entity.custom.RaccoonVariant;
import org.jetbrains.annotations.NotNull;


import java.util.Map;

public class RaccoonRenderer extends MobRenderer<@NotNull RaccoonEntity,
        @NotNull RaccoonRenderState, @NotNull RaccoonModel> {

    private static final Map<RaccoonVariant, Identifier> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(RaccoonVariant.class), map -> {
                map.put(RaccoonVariant.DEFAULT,
                        Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, "textures/entity/raccoon/raccoon.png"));
                map.put(RaccoonVariant.GRAY,
                        Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, "textures/entity/raccoon/gray_raccoon.png"));
                map.put(RaccoonVariant.BROWN,
                        Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, "textures/entity/raccoon/brown_raccoon.png"));
                map.put(RaccoonVariant.CINNAMON,
                        Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, "textures/entity/raccoon/cinnamon_raccoon.png"));
                map.put(RaccoonVariant.BLOND,
                        Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, "textures/entity/raccoon/blond_raccoon.png"));
                map.put(RaccoonVariant.ALBINO,
                        Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, "textures/entity/raccoon/albino_raccoon.png"));
                map.put(RaccoonVariant.SWAMP,
                        Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, "textures/entity/raccoon/swamp_raccoon.png"));
                map.put(RaccoonVariant.BLACK,
                        Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, "textures/entity/raccoon/black_raccoon.png"));

            });

    public RaccoonRenderer(EntityRendererProvider.Context context) {
        super(context, new RaccoonModel(context.bakeLayer(RaccoonModel.RACCOON)), 0.5f);
    }

    @Override
    public @NotNull Identifier getTextureLocation(RaccoonRenderState state) {
        return LOCATION_BY_VARIANT.getOrDefault(
                state.variant,
                LOCATION_BY_VARIANT.get(RaccoonVariant.DEFAULT)
        );
    }

    @Override
    public RaccoonRenderState createRenderState() {
        return new RaccoonRenderState();
    }

    @Override
    public void extractRenderState(
            RaccoonEntity entity,
            RaccoonRenderState state,
            float partialTicks) {

        super.extractRenderState(entity, state, partialTicks);

        state.idleAnimationState.copyFrom(entity.idleAnimationState);
        state.sittingAnimationState.copyFrom(entity.sittingAnimationState);
        state.eatingAnimationState.copyFrom(entity.eatingAnimationState);
        state.sleepingAnimationState.copyFrom(entity.sleepingAnimationState);
        state.begAnimationState.copyFrom(entity.begAnimationState);
        state.grabAnimationState.copyFrom(entity.grabAnimationState);
        state.washAnimationState.copyFrom(entity.washAnimationState);

        state.variant = entity.getVariant();
    }

    @Override
    protected void scale(RaccoonRenderState state, @NotNull PoseStack poseStack) {
        if (state.isBaby) {
            poseStack.scale(0.7f, 0.7f, 0.7f);
        } else {
            poseStack.scale(1.0f, 1.0f, 1.0f);
        }
    }
}
