package net.racquo.raccoonsco.entity.client.raccoon;


import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.racquo.raccoonsco.RaccoonsCo;

public class RaccoonModel extends EntityModel<RaccoonRenderState> {
    public static final ModelLayerLocation RACCOON =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(RaccoonsCo.MOD_ID, "raccoon"), "main");
    private final ModelPart root;
    private final ModelPart raccoon;
    private final ModelPart head;

    private final KeyframeAnimation walkingAnimation;
    private final KeyframeAnimation idlingAnimation;
    private final KeyframeAnimation sittingAnimation;
    private final KeyframeAnimation eatingAnimation;
    private final KeyframeAnimation sleepingAnimation;
    private final KeyframeAnimation begAnimation;
    private final KeyframeAnimation grabAnimation;
    private final KeyframeAnimation washAnimation;



    public RaccoonModel(ModelPart root) {
        super(root);
        this.root = root.getChild("root");
        this.raccoon = this.root.getChild("raccoon");
        this.head = this.raccoon.getChild("head");
        this.walkingAnimation = RaccoonAnimations.ANIM_RACCOON_WALK.bake(root);
        this.idlingAnimation = RaccoonAnimations.ANIM_RACCOON_IDLE.bake(root);
        this.sittingAnimation = RaccoonAnimations.ANIM_RACCOON_SIT.bake(root);
        this.eatingAnimation = RaccoonAnimations.ANIM_RACCOON_EATING.bake(root);
        this.sleepingAnimation = RaccoonAnimations.ANIM_RACCOON_SLEEPING.bake(root);
        this.begAnimation = RaccoonAnimations.ANIM_RACCOON_BEG.bake(root);
        this.grabAnimation = RaccoonAnimations.ANIM_RACCOON_GRAB.bake(root);
        this.washAnimation = RaccoonAnimations.ANIM_RACCOON_WASH.bake(root);

    }
    /*
    Note: Issue with Blockbench java exported file: turn .pivot -> .of and add 'default' rotation in radians?

     */

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition root = modelPartData.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition raccoon = root.addOrReplaceChild("raccoon", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition frontLegL = raccoon.addOrReplaceChild("frontLegL", CubeListBuilder.create().texOffs(0, 36).addBox(-0.5F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 2.0F, -4.0F));

        PartDefinition frontLegR = raccoon.addOrReplaceChild("frontLegR", CubeListBuilder.create().texOffs(8, 36).addBox(-1.5F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 2.0F, -4.0F));

        PartDefinition backLegL = raccoon.addOrReplaceChild("backLegL", CubeListBuilder.create().texOffs(16, 36).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 2.0F, 6.0F));

        PartDefinition backLegR = raccoon.addOrReplaceChild("backLegR", CubeListBuilder.create().texOffs(24, 36).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 2.0F, 6.0F));

        PartDefinition tail = raccoon.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 20).addBox(-1.7F, -2.0F, 0.5F, 4.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.7962F, -0.3656F, 6.83F));

        PartDefinition body = raccoon.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -5.5F, -2.0F, 10.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -2.0F));

        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(32, 31).addBox(-4.5F, -4.0F, -3.0F, 9.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -1.0F, -2.0F));

        PartDefinition head = raccoon.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 20).addBox(-4.0F, -2.5F, -3.0F, 8.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -1.0F, -8.0F));

        PartDefinition earL = head.addOrReplaceChild("earL", CubeListBuilder.create().texOffs(0, 44).addBox(-1.5F, -1.5F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -3.0F, -1.0F));

        PartDefinition earR = head.addOrReplaceChild("earR", CubeListBuilder.create().texOffs(44, 0).addBox(-0.5F, -1.5F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -3.0F, -1.0F));

        PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(32, 42).addBox(-2.0F, -0.5F, -1.5F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -3.5F));

        PartDefinition eyelids = head.addOrReplaceChild("eyelids", CubeListBuilder.create().texOffs(4, 4).addBox(1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(4, 4).addBox(-4.0F, -1.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 1.0F, -2.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(RaccoonRenderState state) {
        super.setupAnim(state);
        this.setHeadAngles(state.yRot, state.xRot);

        this.walkingAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 2f, 2.5f);
        this.idlingAnimation.apply(state.idleAnimationState, state.ageInTicks, 1f);
        this.sittingAnimation.apply(state.sittingAnimationState, state.ageInTicks, 1f);
        this.eatingAnimation.apply(state.eatingAnimationState, state.ageInTicks, 1f);
        this.sleepingAnimation.apply(state.sleepingAnimationState, state.ageInTicks, 1f);
        this.begAnimation.apply(state.begAnimationState, state.ageInTicks, 1f);
        this.grabAnimation.apply(state.grabAnimationState, state.ageInTicks, 1f);
        this.washAnimation.apply(state.washAnimationState, state.ageInTicks, 1f);


    }
    private void setHeadAngles(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30.0F, 30.0F);
        headPitch = Mth.clamp(headPitch, -25.0F, 45.0F);

        this.head.yRot = headYaw * 0.017453292F;
        this.head.xRot = headPitch * 0.017453292F;
    }

}
