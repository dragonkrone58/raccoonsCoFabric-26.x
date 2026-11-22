package net.racquo.raccoonsco.entity.custom;


import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathType;
import net.racquo.raccoonsco.item.ModItems;
import net.racquo.raccoonsco.sound.ModSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrawfishEntity extends WaterAnimal implements Bucketable {

    /* ---------------- DATA TRACKERS ---------------- */
    private static final EntityDataAccessor<@NotNull Boolean> FROM_BUCKET =
            SynchedEntityData.defineId(CrawfishEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<@NotNull Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(CrawfishEntity.class, EntityDataSerializers.INT);

    private static final double RARE_VARIANT_CHANCE = 0.05;

    public final AnimationState idleAnimationState = new AnimationState();

    /* ---------------- CONSTRUCTOR ---------------- */

    public CrawfishEntity(EntityType<? extends @NotNull WaterAnimal> type, Level world) {
        super(type, world);
        this.moveControl = new SmoothSwimmingMoveControl(
                this, 85,10,1.2F,0.5F,true
        );

        this.setPathfindingMalus(PathType.WATER, 0.0F);
    }
    /* ---------------- ANIMATION ---------------- */

    @Override
    public void tick() {
        super.tick();
        if (!this.idleAnimationState.isStarted()) {
            this.idleAnimationState.start(this.tickCount);
        }
    }

    /* ---------------- SPAWNING ---------------- */

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
                                        @NotNull EntitySpawnReason spawnReason, SpawnGroupData spawnData) {
        setVariant(pickVariant(this.random));

        return super.finalizeSpawn(level,difficulty,spawnReason,spawnData);
    }

    public static boolean canCrawfishSpawn(
            EntityType<?> type,
            LevelAccessor world,
            EntitySpawnReason reason,
            BlockPos pos,
            RandomSource random
    ) {
        return world.getBlockState(pos).is(Blocks.WATER)
                && world.getFluidState(pos).is(Fluids.WATER)
                && world.getFluidState(pos).isSource();
    }

    /* ---------------- ENTITY GOALS ---------------- */

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, (double)1.25F));
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));

        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6, 1.4, EntitySelector.NO_SPECTATORS));
        //this.goalSelector.addGoal(4, new FishSwimGoal(this));
        this.goalSelector.addGoal(0, new RandomSwimmingGoal(this, 1.3D, 20));

    }

    /* ---------------- ENTITY ATTRIBUTES ---------------- */

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 3.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1.0D);
    }

    /* ---------------- DATA TRACKER ---------------- */

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);

        builder.define(DATA_ID_TYPE_VARIANT, 0);
        builder.define(FROM_BUCKET, false);
    }

    /* ---------------- VARIANTS ---------------- */

    public CrawfishVariant getVariant() {
        return CrawfishVariant.byId(this.entityData.get(DATA_ID_TYPE_VARIANT));
    }

    private void setVariant(CrawfishVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId());
    }

    private static CrawfishVariant pickVariant(RandomSource random) {
        return random.nextDouble() < RARE_VARIANT_CHANCE
                ? CrawfishVariant.BLUE
                : CrawfishVariant.DEFAULT;
    }


    /* ---------------- MOVEMENT ---------------- */

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }
    @Override
    public int getMaxAirSupply() {
        return 4800;
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new AmphibiousPathNavigation(this, level);
    }


    /* ---------------- SOUNDS ---------------- */

    @Nullable @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSounds.CRAWFISH_HURT;
    }

    @Nullable @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.CRAWFISH_DEATH;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(
                SoundEvents.SAND_STEP,
                0.15F,
                this.getVoicePitch()
        );
    }

    /* ---------------- BUCKET ---------------- */

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(Items.BUCKET) || stack.is( Items.WATER_BUCKET)) {
            return Bucketable.bucketMobPickup(player, hand, this).orElse(InteractionResult .PASS);
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack stack) {

        Bucketable.saveDefaultDataToBucketTag(this, stack);

        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, nbt -> {
            nbt.putInt("Variant", this.getVariant().getId());

            nbt.putBoolean("IsBaby", this.isBaby());
        });
    }

    @Override
    public void loadFromBucketTag(CompoundTag nbt) {
        nbt.getInt("Variant").ifPresent(value -> this.setVariant(CrawfishVariant.byId(value)));
        nbt.getBoolean("IsBaby").ifPresent(this :: setBaby);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.CRAWFISH_BUCKET);
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL;
    }

}
