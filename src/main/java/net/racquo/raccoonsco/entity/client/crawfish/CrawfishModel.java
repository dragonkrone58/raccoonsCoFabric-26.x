package net.racquo.raccoonsco.entity.client.crawfish;

import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.racquo.raccoonsco.RaccoonsCo;
import org.jetbrains.annotations.NotNull;


public class CrawfishModel extends EntityModel<@NotNull CrawfishRenderState> {
    public static final ModelLayerLocation CRAWFISH = 
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, "crawfish"), "main");

    private final KeyframeAnimation idlingAnimation;

    public CrawfishModel(ModelPart root) {
        super(root);
        ModelPart root1 = root.getChild("root");
        ModelPart crawfish = root1.getChild("crawfish");
        this.idlingAnimation = CrawfishAnimations.ANIM_CRAWFISH_IDLE.bake(root);

    }
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition root = modelPartData.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition crawfish = root.addOrReplaceChild("crawfish", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition antennaR = crawfish.addOrReplaceChild("antennaR", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -1.5F, -4.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -1.5F, -4.0F));

        PartDefinition antennaL = crawfish.addOrReplaceChild("antennaL", CubeListBuilder.create().texOffs(16, 0).addBox(0.0F, -1.5F, -4.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -1.5F, -4.0F));

        PartDefinition body = crawfish.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -2.5F, -4.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tergum = body.addOrReplaceChild("tergum", CubeListBuilder.create().texOffs(14, 13).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 1.0F));

        PartDefinition tail = tergum.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 13).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

        PartDefinition clawR = crawfish.addOrReplaceChild("clawR", CubeListBuilder.create().texOffs(12, 7).addBox(-3.0F, -0.5F, -3.5F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(8, 16).addBox(-1.0F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -1.5F, -1.5F));

        PartDefinition clawL = crawfish.addOrReplaceChild("clawL", CubeListBuilder.create().texOffs(0, 7).addBox(1.0F, -0.5F, -3.5F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 6).addBox(0.0F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -1.5F, -1.5F));
        return LayerDefinition.create(modelData, 32, 32);
    }
    @Override
    public void setupAnim(CrawfishRenderState state) {
        super.setupAnim(state);

        this.idlingAnimation.apply(state.idleAnimationState, state.ageInTicks, 1f);
    }

}