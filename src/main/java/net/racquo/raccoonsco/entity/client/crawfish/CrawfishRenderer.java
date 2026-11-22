
package net.racquo.raccoonsco.entity.client.crawfish;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import net.racquo.raccoonsco.RaccoonsCo;
import net.racquo.raccoonsco.entity.custom.CrawfishEntity;
import net.racquo.raccoonsco.entity.custom.CrawfishVariant;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CrawfishRenderer extends MobRenderer< @NotNull CrawfishEntity,
        @NotNull CrawfishRenderState, @NotNull CrawfishModel> {

    private static final Map<CrawfishVariant, Identifier> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(CrawfishVariant.class), map -> {

                map.put(
                        CrawfishVariant.DEFAULT,
                        Identifier.fromNamespaceAndPath(
                                RaccoonsCo.MOD_ID,
                                "textures/entity/crawfish/crawfish_brown.png"
                        )
                );

                map.put(
                        CrawfishVariant.BLUE,
                        Identifier.fromNamespaceAndPath(
                                RaccoonsCo.MOD_ID,
                                "textures/entity/crawfish/crawfish_blue.png"
                        )
                );
            });

    public CrawfishRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new CrawfishModel(
                        context.bakeLayer(CrawfishModel.CRAWFISH)
                ),
                0.1f
        );
    }

    @Override
    protected void scale(CrawfishRenderState state, @NotNull PoseStack poseStack) {
        if (state.isBaby) {
            poseStack.scale(0.7f, 0.7f, 0.7f);
        }
    }

    @Override
    public @NotNull Identifier getTextureLocation(CrawfishRenderState state) {
        return LOCATION_BY_VARIANT.getOrDefault(
                state.variant,
                LOCATION_BY_VARIANT.get(CrawfishVariant.DEFAULT)
        );
    }

    @Override
    public CrawfishRenderState createRenderState() {
        return new CrawfishRenderState();
    }

    @Override
    public void extractRenderState(
            CrawfishEntity entity,
            CrawfishRenderState state,
            float partialTicks
    ) {
        super.extractRenderState(entity, state, partialTicks);

        state.idleAnimationState.copyFrom(entity.idleAnimationState);

        state.variant = entity.getVariant();
    }
}
