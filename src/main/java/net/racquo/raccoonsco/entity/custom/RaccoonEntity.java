
package net.racquo.raccoonsco.entity.custom;


import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Util;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.racquo.raccoonsco.entity.ModEntities;
import net.racquo.raccoonsco.entity.ai.raccoon.*;
import net.racquo.raccoonsco.item.ModItems;
import net.racquo.raccoonsco.mixin.ConcretePowderAccessor;
import net.racquo.raccoonsco.sound.ModSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.util.List;
import net.minecraft.util.RandomSource;
import org.slf4j.Logger;

/**
 * CURRENT ISSUES:
 *  Eating animation does not play
 *  Washing animation does not play
 */


public class RaccoonEntity extends TamableAnimal {


    private static final Logger log = LoggerFactory.getLogger(RaccoonEntity.class);

    /* ---------------- ANIMATION STATES ---------------- */
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState sittingAnimationState = new AnimationState();
    public final AnimationState eatingAnimationState = new AnimationState();
    public final AnimationState sleepingAnimationState = new AnimationState();
    public final AnimationState begAnimationState = new AnimationState();
    public final AnimationState grabAnimationState = new AnimationState();
    public final AnimationState washAnimationState = new AnimationState();

    /* ---------------- ENTITY STATES ---------------- */
    public enum RaccoonAction {
        IDLE,
        GRABBING,
        WALKING_TO_WATER,
        WASHING,
        EATING,
        SLEEPING,
        BEGGING,
        SITTING,
        PANIC
    }
    //ENTITY STATE TRACKER
    public static final EntityDataAccessor<Integer> DATA_ACTION =
            SynchedEntityData.defineId(RaccoonEntity.class, EntityDataSerializers.INT);


    /* ---------------- GRABBING & WASHING ---------------- */

    public ItemStack grabbedStack = ItemStack.EMPTY;
    private ItemEntity grabTarget = null;
    private boolean headingToWater = false;
    private BlockPos waterPos;

    public static final int MAX_GRAB_STACK = 16;
    private static final int GRABBING_COOLDOWN = 2;
    private static final int GRAB_DURATION_TICKS = 35;
    private static final int WATER_RETRY_TICKS = 40;
    private static final int WASH_DROP_TICK = 90;
    private static final int WASHING_DURATION_TICKS = 20 * 7;
    private static final int MAX_WASH_SEARCH_DISTANCE = 24;
    private static final int POST_WASH_COOLDOWN_TICKS = 40;

    private int grabbingCooldown = 0;
    private int washingStartDelay = 0;
    private int grabbingTicks = 0;
    private int washingTicks = 0;
    private int waterRetryTicks = 0;
    private int postWashCooldown = 0;
    /* ---------------- EATING ---------------- */

    private ItemStack currentEatingStack;

    private static final int EATING_COOLDOWN_TICKS = 20;
    private static final int EATING_ANIMATION_DURATION = 70;
    private static final int EATING_SOUND_START = 20;
    private static final int EATING_SOUND_END = 50;
    private static final int BURP_TICK = 60;

    private int eatingCooldownTicks = 0;
    private int eatingTicks = 0;

    /* ---------------- FULLNESS ---------------- */

    public static final int MAX_FULLNESS = 3;
    private static final int FULLNESS_COOLDOWN_TICKS = 20 * 300;

    //FULLNESS DATA TRACKER
    private static final EntityDataAccessor<Integer> DATA_FULLNESS =
            SynchedEntityData.defineId(RaccoonEntity.class, EntityDataSerializers.INT);

    private int fullnessCooldownTicks = 0;

    // FULLNESS TIMEOUT
    private static final int FULL_SLEEP_SEARCH_TICKS = 20 * 120; //2 minutes
    private int fullSleepSearchTicks = 0;

    //TAMED BUFF
    private int tamedBuffTicks = 0;
    private boolean hasTamedBuff = false;

    /* ---------------- PANIC MODE ---------------- */
    private static final int PANIC_TICKS = 20 * 8;
    private int panicTicks = 0;

    /* ---------------- SLEEPING ---------------- */
    private static final int SLEEP_DURATION_TICKS = 20 * 300; // 5 minutes
    private static final int MAX_SLEEP_LIGHT = 12;
    private int sleepTicks = 0;

    /* ---------------- TEMPTABLES ---------------- */
    private static final List<ItemLike> RACCOON_EATABLES =
            List.of(ModItems.COOKED_CRAWFISH, ModItems.RAW_CRAWFISH, Items.SWEET_BERRIES, Items.COOKIE, Items.APPLE, Items.MELON_SLICE,
                    Items.GLOW_BERRIES, Items.PUMPKIN_PIE, Items.POTATO, Items.BAKED_POTATO, Items.SALMON, Items.COOKED_SALMON,
                    Items.COD, Items.COOKED_COD, Items.CHICKEN, Items.COOKED_CHICKEN);

    private static final Ingredient RACCOON_TEMPT_INGREDIENT = Ingredient.of(RACCOON_EATABLES.toArray(ItemLike[]::new));


    /* ---------------- VARIANTS ---------------- */
    private static final RaccoonVariant[] RARE_VARIANTS = {
            RaccoonVariant.ALBINO
    };

    private static final RaccoonVariant[] UNCOMMON_VARIANTS = {
            RaccoonVariant.BLOND,
            RaccoonVariant.BLACK,
            RaccoonVariant.CINNAMON,
    };

    //VARIANT DATA TRACKER
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(RaccoonEntity.class, EntityDataSerializers.INT);


    /* ---------------- CONSTRUCTOR ---------------- */
    public RaccoonEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.POWDER_SNOW, -1.0F);
        this.setPathfindingMalus(PathType.ON_TOP_OF_POWDER_SNOW, -1.0F);
    }

    /* ---------------- ENTITY GOALS ---------------- */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TamableAnimalPanicGoal(1.5D, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Chicken.class, true ));
        this.goalSelector.addGoal(4, new RaccoonWashConcreteGoal(this, 1.2D));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.6D));
        this.goalSelector.addGoal(5, new RaccoonSeekShadeSleepGoal(this, 1.2D));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.2D, 10.0F, 2.0F));
        this.goalSelector.addGoal(7, new RaccoonAttackGoal(this, 1.4D));

        this.goalSelector.addGoal(8, new BreedGoal(this, 1.15D));
        this.goalSelector.addGoal(9, new RaccoonEatFoodGoal(this));
        this.goalSelector.addGoal(10, new RaccoonTemptGoal(this, 1.25D,
                RACCOON_TEMPT_INGREDIENT));

        this.goalSelector.addGoal(11, new RaccoonFollowParentGoal(this, 1.1D));

        this.goalSelector.addGoal(12, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(13, new LookAtPlayerGoal(this, Player.class, 4.0F));
        this.goalSelector.addGoal(14, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean canAttack(@Nullable LivingEntity target) {
        if (isGrabbing() || isWashing()) return false;
        return super.canAttack(target);
    }


    /* ---------------- ENTITY ATTRIBUTES ---------------- */
    public static AttributeSupplier.Builder createAttributes(){
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.TEMPT_RANGE, 12)
                .add(Attributes.SAFE_FALL_DISTANCE, 5.0F)
                .add(Attributes.ATTACK_DAMAGE, 1.5D);
    }

    /* ---------------- RACCOON INIT ---------------- */
    @Override
    public SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            EntitySpawnReason spawnReason,
            @Nullable SpawnGroupData entityData
    ) {
        var biomeKey = level.getBiome(this.blockPosition()).unwrapKey().orElse(null);

        setVariant(pickVariant(this.getRandom(), biomeKey));

        return super.finalizeSpawn(level, difficulty, spawnReason, entityData);
    }

    /* ---------------- ACTION DATA TRACKER ---------------- */
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        builder.define(DATA_ACTION, RaccoonAction.IDLE.ordinal());
        builder.define(DATA_FULLNESS, 0);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
    }


    public RaccoonAction getAction() {
        return RaccoonAction.values()[this.getEntityData().get(DATA_ACTION)];
    }
    private void setAction(RaccoonAction action) {
        if (getAction() == action) return;
        this.getEntityData().set(DATA_ACTION, action.ordinal());
    }

    public boolean isAction(RaccoonAction action) {
        return getAction() == action;
    }

    /* ---------------- ANIMATION STATE SETUP---------------- */
    @Nullable
    private AnimationState currentAnimation = null;

    private void setupAnimationStates() {
        switch (getAction()) {
            case SLEEPING -> switchTo(sleepingAnimationState);
            case WASHING -> switchTo(washAnimationState);
            case GRABBING -> switchTo(grabAnimationState);
            case EATING -> switchTo(eatingAnimationState);
            case BEGGING -> switchTo(begAnimationState);
            case SITTING -> switchTo(sittingAnimationState);
            default -> switchTo(idleAnimationState);
        }
    }

    //ANIMATION SETUP HELPER
    private void switchTo(AnimationState next) {
        if (currentAnimation == next) return;

        if (currentAnimation != null) {
            currentAnimation.stop();
        }

        currentAnimation = next;
        next.start(this.tickCount);
    }

    /* ---------------- TICKING ---------------- */


    @Override
    public void tick() {
        super.tick();

        final boolean isServer = !this.level().isClientSide();

        // PANIC MODE
        if (isServer && isAction(RaccoonAction.PANIC)) {
            if (--panicTicks <= 0) {
                panicTicks = 0;
                setAction(RaccoonAction.IDLE);
                log.info("Raccoon panic mode ended");
            }
        }

        // TAMED BUFF
        if (isServer && hasTamedBuff && --tamedBuffTicks <= 0) {
            endTamedBuff();
        }

        if (isServer && postWashCooldown > 0) {
            postWashCooldown--;
        }

        // GRABBING
        if (isServer && isAction(RaccoonAction.GRABBING)) {
            handleGrabbingTick();
        }

        // RETRY WATER SEARCHING
        if (isServer
                && !isAction(RaccoonAction.WASHING)
                && !isAction(RaccoonAction.GRABBING)
                && !isAction(RaccoonAction.WALKING_TO_WATER)
                && !grabbedStack.isEmpty()) {

            if (++waterRetryTicks >= WATER_RETRY_TICKS) {
                waterRetryTicks = 0;
                attemptFindWater();
            }
        }

        // ARRIVED AT WATER
        if (isServer && headingToWater && waterPos != null) {
            if (this.blockPosition().closerThan(waterPos, 1.5)) {
                headingToWater = false;
                setAction(RaccoonAction.IDLE);
                washingStartDelay = 1;
                washingTicks = 0;
                getNavigation().stop();
            }
        }

        // START WASHING AFTER DELAY
        if (isServer && washingStartDelay > 0
                && --washingStartDelay == 0) {

            if (!isAction(RaccoonAction.WASHING)
                    && !grabbedStack.isEmpty()) {
                setAction(RaccoonAction.WASHING);
                washingTicks = 0;
            }
        }

        // WASHING
        if (isServer && isAction(RaccoonAction.WASHING)) {
            handleWashingTick();
        }

        // EATING COOLDOWN
        if (isServer && eatingCooldownTicks > 0) {
            eatingCooldownTicks--;
        }

        // EATING
        if (isServer && isEating()) {
            handleEatingTick();
        }

        // FULLNESS COOLDOWN
        if (isServer && isTame()
                && fullnessCooldownTicks > 0
                && --fullnessCooldownTicks == 0) {
            resetFullness();
        }

        // SLEEP SEARCH
        if (isServer && shouldSearchForSleep()) {
            if (++fullSleepSearchTicks >= FULL_SLEEP_SEARCH_TICKS) {
                resetFullness();
            }
        } else if (isServer) {
            fullSleepSearchTicks = 0;
        }

        // SLEEPING
        if (isServer && isSleeping() && !isTame()) {
            sleepTicks++;

            if (sleepTicks >= SLEEP_DURATION_TICKS) {
                wakeNatural();
            }
        }

        // FREEZE MOVEMENT WHILE SLEEPING
        if (isSleeping() && !isTame()) {
            getNavigation().stop();
            setDeltaMovement(0, getDeltaMovement().y, 0);
        }

        // CLIENT ANIMATIONS
        if (!isServer) {
            setupAnimationStates();
        }
    }


    /* ---------------- CREATE CHILD ---------------- */

    @Override
    public RaccoonEntity getBreedOffspring(ServerLevel level, AgeableMob partner) {
        RaccoonEntity baby = ModEntities.RACCOON.create(
                level,
                EntitySpawnReason.BREEDING
        );

        if (baby == null) return null;

        ResourceKey<Biome> biomeKey =
                level.getBiome(this.blockPosition()).unwrapKey().orElse(null);

        baby.setVariant(pickVariant(this.random, biomeKey));

        return baby;
    }


    /* ---------------- INTERACTING WITH RACCOON ---------------- */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {

        if (isSleeping()) return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);

        //HANDLE TAMING
        if (tryHandleTaming(player, stack)) return InteractionResult.SUCCESS;

        //HANDLE HEALING
        if (tryHandleHealing(player, hand, stack)) return InteractionResult.SUCCESS;

        //HANDLE SITTING
        InteractionResult result = super.mobInteract(player, hand);
        if (result == InteractionResult.PASS && this.isOwnedBy(player)) {
            toggleSitting();
            return InteractionResult.SUCCESS;
        }

        return result;
    }

    /* ---------------- TAMING RACCOON ---------------- */

    private boolean tryHandleTaming(Player player, ItemStack stack) {
        if (isTame()) return false;
        if (!stack.is(ModItems.RAW_CRAWFISH) && !stack.is(ModItems.COOKED_CRAWFISH)) return false;

        if (!level().isClientSide()) {
            stack.consume(1, player);
            tryTame(player, stack);
        }
        return true;
    }

    /* ---------------- TAMED RACCOON HEALING ---------------- */

    private boolean tryHandleHealing(Player player, InteractionHand hand, ItemStack stack) {
        if (!isTame()) return false;
        if (!RACCOON_TEMPT_INGREDIENT.test(stack)) return false;
        if (getHealth() >= getMaxHealth()) return false;

        usePlayerItem(player, hand, stack);
        eatingEffect(stack);
        healFromFood(stack);
        return true;
    }

    private void healFromFood(ItemStack stack) {
        FoodProperties food = stack.get(DataComponents.FOOD);
        this.heal(2.0F * (food != null ? food.nutrition() : 1.0F));
    }

    private void eatingEffect(ItemStack itemStack){
        this.playSound(ModSounds.RACCOON_EATS, 0.7F, 1.1F);

        if (!this.level().isClientSide()) {
            ItemStackTemplate template = ItemStackTemplate.fromNonEmptyStack(itemStack);

            ((ServerLevel) this.level()).sendParticles(
                    new ItemParticleOption(ParticleTypes.ITEM, template),
                    this.getX(),
                    this.getY(0.6),
                    this.getZ(),
                    6,
                    0.2, 0.2, 0.2,
                    0.05
            );
        }
    }

    /* ---------------- TAMED RACCOON GRABBING  ---------------- */

    public boolean isGrabbing(){
        return isAction(RaccoonAction.GRABBING);    }

    // GRAB SETTER
    public void setGrabTarget(ItemEntity target) {
        this.grabTarget = target;
    }

    public boolean canGrab(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (postWashCooldown > 0) return false;
        if(this.isInSittingPose()) return false;
        if(this.isSleeping()) return false;
        if(this.isEating()) return false;
        if(this.isWashing()) return false;

        if (!(stack.getItem() instanceof BlockItem blockItem)) return false;
        if (!(blockItem.getBlock() instanceof ConcretePowderBlock)) return false;

        if (grabbedStack.isEmpty()) return true;

        // Only grab same color
        return grabbedStack.is(stack.getItem()) && grabbedStack.getCount() < MAX_GRAB_STACK;
    }

    public void addToInventory(ItemStack stack) {
        if (stack.isEmpty()) return;

        if (grabbedStack.isEmpty()) {
            grabbedStack = stack.copy();
            grabbedStack.setCount(Math.min(stack.getCount(), MAX_GRAB_STACK));
            return;
        }

        if (!grabbedStack.is(stack.getItem())) return;

        int space = MAX_GRAB_STACK - grabbedStack.getCount();
        if (space <= 0) return;

        grabbedStack.grow(Math.min(space, stack.getCount()));
    }


    public void startGrabSequence() {
        if (isGrabbing()) return;
        if (grabTarget == null) return;
        setAction(RaccoonAction.GRABBING);
        grabbingTicks = 0;
        grabbingCooldown = GRABBING_COOLDOWN;
    }


    private void resumeConcreteSearch() {
        this.headingToWater = false;
        this.waterPos = null;
        this.grabbingTicks = 0;
    }

    // GRABBING TICK HANDLER
    private void handleGrabbingTick() {
        grabbingTicks++;

        // Stop movement during grab
        getNavigation().stop();
        setDeltaMovement(0, getDeltaMovement().y, 0);
        this.setYBodyRot(this.getYRot());
        this.setYHeadRot(this.getYRot());

        if (grabbingTicks == 10) playSound(ModSounds.RACCOON_SNIFFS, 1.0F, 1.0F);

        if (grabbingCooldown > 0) {
            grabbingCooldown--;
            return;
        }

        if (grabTarget == null || !grabTarget.isAlive()) {
            // No valid target, stop grabbing
            setAction(RaccoonAction.IDLE);
            return;
        }

        // Only pick up if canGrab
        if (grabbingTicks >= GRAB_DURATION_TICKS && canGrab(grabTarget.getItem())) {
            ItemStack stack = grabTarget.getItem();

            int spaceLeft = MAX_GRAB_STACK - grabbedStack.getCount();
            int toGrab = Math.min(spaceLeft, stack.getCount());
            if (toGrab > 0) {
                ItemStack taken = stack.split(toGrab);
                addToInventory(taken);

                if (stack.isEmpty()) grabTarget.discard();
            }

            grabTarget = null;
            attemptFindWater();
            setAction(RaccoonAction.IDLE);
            grabbingTicks = 0;
        }
    }



    /* ---------------- TAMED RACCOON SEEK WATER  ---------------- */
    public void attemptFindWater() {
        waterPos = findNearestWater();

        if (waterPos == null || grabbedStack.isEmpty()) return;

        waterRetryTicks = 0;
        headingToWater = true;

        setAction(RaccoonAction.WALKING_TO_WATER);

        getNavigation().moveTo(
                waterPos.getX() + 0.5,
                waterPos.getY(),
                waterPos.getZ() + 0.5,
                1.1D
        );
    }


    @Nullable
    private BlockPos findNearestWater() {
        BlockPos origin = blockPosition();

        for (BlockPos pos : BlockPos.withinManhattan(origin, MAX_WASH_SEARCH_DISTANCE, 2, MAX_WASH_SEARCH_DISTANCE)) {
            if (level().getBlockState(pos).is(Blocks.WATER)) {
                return pos;
            }
        }
        return null;
    }


    /* ---------------- TAMED RACCOON WASHING  ---------------- */

    // WASHING TICK HANDLER
    public boolean isWashing(){
        return isAction(RaccoonAction.WASHING);
    }

    private void handleWashingTick() {
        washingTicks++;

        getNavigation().stop();
        setDeltaMovement(0, getDeltaMovement().y, 0);

        setYBodyRot(getYRot());
        setYHeadRot(getYRot());

        // Splash sounds and particles
        if (washingTicks % 20 == 0
                && washingTicks < WASH_DROP_TICK) {

            playSound(SoundEvents.GENERIC_SPLASH, 0.5F, 1.0F);

            ((ServerLevel) level()).sendParticles(
                    ParticleTypes.SPLASH,
                    getX(),
                    getY(0.5),
                    getZ(),
                    3,
                    0.2, 0.2, 0.2,
                    0.01
            );
        }

        // Drop hardened concrete
        if (washingTicks == WASH_DROP_TICK) {
            playSound(SoundEvents.ITEM_PICKUP);
            dropWashedConcrete();
        }

        // Finish washing
        if (washingTicks >= WASHING_DURATION_TICKS) {
            washingTicks = 0;

            setAction(RaccoonAction.IDLE);

            waterPos = null;
            grabbedStack = ItemStack.EMPTY;

            getNavigation().stop();
            setDeltaMovement(0, getDeltaMovement().y, 0);

            resumeConcreteSearch();
            postWashCooldown = POST_WASH_COOLDOWN_TICKS;
        }
    }

    private void dropWashedConcrete() {
        if (grabbedStack.isEmpty()) return;
        if (!(grabbedStack.getItem() instanceof BlockItem blockItem)) return;
        if (!(blockItem.getBlock() instanceof ConcretePowderBlock powder)) return;

        //mixin used here :>
        Block hardened = ((ConcretePowderAccessor) powder).raccoonsCo$getHardenedBlock();
        ItemStack output = new ItemStack(hardened.asItem(), grabbedStack.getCount());

        ItemEntity entity = new ItemEntity(
                level(),
                getX(),
                getY(0.5),
                getZ(),
                output
        );
        level().addFreshEntity(entity);

        grabbedStack = ItemStack.EMPTY;
    }

    public boolean isPostWashCoolingDown() {
        return postWashCooldown > 0;
    }


    @Override
    public void die(DamageSource source) {
        super.die(source);
        dropInventory(); // drop any concrete powder or washed blocks
    }


    private void dropInventory() {
        if (grabbedStack.isEmpty()) return;

        ItemStack toDrop = grabbedStack;
        grabbedStack = ItemStack.EMPTY;

        ItemEntity entity = new ItemEntity(
                level(),
                getX(),
                getY(0.5),
                getZ(),
                toDrop
        );

        level().addFreshEntity(entity);
    }


    /* ---------------- TAMED RACCOON SITTING ---------------- */

    private void toggleSitting() {
        if (isGrabbing() || isWashing()) return;

        boolean newState = !isInSittingPose();
        setInSittingPose(newState);

        jumping = false;
        navigation.stop();
        setTarget(null);

        if (newState) {
            setAction(RaccoonAction.SITTING);
            dropInventory();
        } else {
            setAction(RaccoonAction.IDLE);
        }
    }

    /* ---------------- TAMING ATTEMPT METHOD  ---------------- */
    private void tryTame(Player player, ItemStack itemStack) {
        this.eatingEffect(itemStack);
        if (this.random.nextInt(3) == 0) {
            this.tame(player);
            this.navigation.stop();
            this.setTarget(null);
            this.setInSittingPose(true);
            this.level().broadcastEntityEvent(this, (byte) 7);
        } else {
            this.level().broadcastEntityEvent(this, (byte) 6);
        }
    }

    /* ---------------- BREEDING ---------------- */
    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return this.isTame() && this.getHealth() >= this.getMaxHealth()
                && RACCOON_TEMPT_INGREDIENT.test(stack);
    }

    /* ---------------- TAMED RACCOON BUFFING ---------------- */

    private void endTamedBuff() {
        hasTamedBuff = false;

        var attr = this.getAttribute(Attributes.MAX_HEALTH);
        if (attr != null && attr.getBaseValue() != 10.0D) {
            attr.setBaseValue(10.0D);
        }
        //RESET HEALTH
        if (this.getHealth() > 10.0F) {
            this.setHealth(10.0F);
        }
        resetFullness();
    }

    /* ---------------- EATING ITEM HELPERS ---------------- */
    public void startEating(ItemStack stack) {
        if (level().isClientSide()) return;
        if (isEating()) return;

        currentEatingStack = stack.copyWithCount(1);
        eatingTicks = 0;

        setAction(RaccoonAction.EATING);
        stopBegging();

        getNavigation().stop();
    }

    //EATING TICK HANDLER
    private void handleEatingTick() {
        eatingTicks++;

        getNavigation().stop();

        if (eatingTicks == 1) {
            playSound(ModSounds.RACCOON_SNIFFS, 1.0F, 1.0F);
        }

        if (eatingTicks >= EATING_SOUND_START
                && eatingTicks <= EATING_SOUND_END
                && eatingTicks % 5 == 0) {
            if (currentEatingStack != null
                    && !currentEatingStack.isEmpty()) {
                eatingEffect(currentEatingStack);
            }
        }

        if (eatingTicks == BURP_TICK) {
            playSound(SoundEvents.PLAYER_BURP, 1.0F, 1.0F);
        }

        if (eatingTicks >= EATING_ANIMATION_DURATION) {
            finishEating();
        }
    }

    //ITEM EATING ELIGIBILITY HELPER
    public boolean canEatItem(ItemStack itemStack) {
        return RACCOON_TEMPT_INGREDIENT.test(itemStack);
    }

    //ITEM EATING COOLDOWN HELPER
    public boolean canEatDroppedFood() {
        if (eatingCooldownTicks > 0) return false;
        if (isSleeping()) return false;

        return !isTame() || fullnessCooldownTicks == 0;
    }


    //EATING GETTER
    public boolean isEating() {
        return isAction(RaccoonAction.EATING);
    }

    //STOP EATING
    private void finishEating() {
        setAction(RaccoonAction.IDLE);
        this.stopBegging();
        this.currentEatingStack = ItemStack.EMPTY;
        increaseFullness();

        // Start eating cooldown after finishing the animation
        this.eatingCooldownTicks = EATING_COOLDOWN_TICKS;
    }


    //EAT DROPPED ITEM METHOD:
    public void consumeDroppedFood(ItemEntity itemEntity) {
        if (this.level().isClientSide()) return;

        // Already eating or cooldown not finished
        if (this.isEating() || this.eatingCooldownTicks > 0) return;

        if (this.isInSittingPose()) return;
        if (this.isFull()) return;

        ItemStack itemStack = itemEntity.getItem();

        if (!canEatItem(itemStack)) return; // ensure it's a valid food item

        // Start eating animation
        this.startEating(itemStack);

        // Remove 1 item from stack
        itemStack.shrink(1);
        if (itemStack.isEmpty()) {
            itemEntity.discard();
        }
    }


    /* ---------------- FULLNESS HELPERS ---------------- */

    //FULLNESS GETTER
    public int getFullness(){
        return this.getEntityData().get(DATA_FULLNESS);
    }

    //IS FULLNESS
    public boolean isFull(){
        return getFullness() >= MAX_FULLNESS;
    }

    //FULLNESS SETTER
    public void increaseFullness() {
        if (isFull()) return;

        int before = getFullness();
        int after = before + 1;
        this.getEntityData().set(DATA_FULLNESS, after);

        // If tamed and now full, start buff period
        if (this.isTame() && after >= MAX_FULLNESS) {
            hasTamedBuff = true;
            tamedBuffTicks = FULLNESS_COOLDOWN_TICKS;

            AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);

            if (maxHealth != null) {
                maxHealth.setBaseValue(20.0D);
            }

            if (this.getHealth() < 20.0F) {
                this.setHealth(20.0F);
            }

            playSound(SoundEvents.PLAYER_LEVELUP, 1.0F, 1.0F);

            if (!this.level().isClientSide()) {
                log.info("Tamed raccoon reached full fullness: resistance period started");
            }
        }
    }

    /* ---------------- SLEEPING HELPERS ---------------- */
    @Override
    public boolean isImmobile() {
        return this.isSleeping() || super.isImmobile();
    }

    public boolean isPanic(){

        return isAction(RaccoonAction.PANIC);
    }

    public boolean isSleeping() {

        return isAction(RaccoonAction.SLEEPING);
    }



    /* ---------------- START SLEEPING METHOD ---------------- */

    //INIT SLEEP SEARCH CRITERIA
    private boolean shouldSearchForSleep() {
        return !isTame()
                && isFull()
                && !isSleeping()
                && !isPanic();
    }

    // INIT SLEEP CRITERIA
    public boolean canSleepHere() {
        if (this.isTame()) return false;
        if (isAction(RaccoonAction.PANIC)) return false;
        if (!this.isFull()) return false;

        int light = this.level().getMaxLocalRawBrightness(this.blockPosition());
        return light <= MAX_SLEEP_LIGHT;
    }

    //START SLEEPING METHOD
    public void startSleeping() {

        if(!canSleepHere()) return;

        this.sleepTicks = 0;
        this.fullSleepSearchTicks = 0;
        setAction(RaccoonAction.SLEEPING);
        this.setInSittingPose(false);
        this.getNavigation().stop();
        this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
    }

    /* ---------------- WAKING METHOD ---------------- */

    //RACCOON SLEEP WAKE
    public void wakeNatural() {
        if(!isSleeping()) return;

        this.sleepTicks = 0;
        setAction(RaccoonAction.IDLE);

        // Fully rested : reset fullness & timer
        resetFullness();
        this.getNavigation().stop();
    }

    //RACCOON SLEEP INTERRUPTED
    public void wakeInterrupted(){
        if(!isSleeping()) return;

        sleepTicks = 0;
        setAction(RaccoonAction.IDLE);

        this.fullSleepSearchTicks = 0;

        getNavigation().stop();
    }

    /* ---------------- WAKE WHEN DAMAGE ---------------- */
    @Override
    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        boolean result = super.hurtServer(world, source, amount);

        if (result) {
            // Enter panic mode
            wakeInterrupted();
            setAction(RaccoonAction.PANIC);
            this.panicTicks = PANIC_TICKS;
        }

        return result;
    }
    /* ---------------- FULLNESS RESET ---------------- */

    private void resetFullness() {
        this.getEntityData().set(DATA_FULLNESS, 0);
        this.fullSleepSearchTicks = 0;
        this.fullnessCooldownTicks = 0;

        if (!this.level().isClientSide()) {
            log.info("Raccoon fullness reset");
        }
    }

    /* ---------------- ATTACK HELPERS ---------------- */

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        LivingEntity previous = this.getTarget();
        super.setTarget(target);

        if (!this.level().isClientSide()
                && target instanceof Chicken
                && previous == null
                && !this.isTame()
                && !this.isFull()) {

            this.playSound(ModSounds.RACCOON_GROWLS, 1.0F, 1.0F);
        }
    }

    /* ---------------- BEGGING HELPERS ---------------- */

    @Nullable
    private RaccoonAction actionBeforeBegging = null;

    public void startBegging() {
        if (isAction(RaccoonAction.BEGGING)) return;
        if (isEating() || isSleeping() || isGrabbing() || isWashing()) return;

        actionBeforeBegging = getAction();
        setAction(RaccoonAction.BEGGING);
    }

    public void stopBegging() {
        if (!isAction(RaccoonAction.BEGGING)) return;

        setAction(actionBeforeBegging != null ? actionBeforeBegging : RaccoonAction.IDLE);
        actionBeforeBegging = null;
    }
    /* ---------------- SOUNDS ---------------- */

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if(this.isSleeping()){
            return ModSounds.RACCOON_SLEEPS;
        }
        return ModSounds.RACCOON_IDLE;
    }

    @Nullable @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.RACCOON_HURT;
    }

    @Nullable @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.RACCOON_DEATH;
    }

    /* ---------------- VARIANT HELPERS ---------------- */
    public RaccoonVariant getVariant(){
        return RaccoonVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant(){
        return this.getEntityData().get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(RaccoonVariant variant){
        this.getEntityData().set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    /* ---------------- VARIANT DATA METHODS ---------------- */

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("Variant", this.getTypeVariant());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.getEntityData().set(
                DATA_ID_TYPE_VARIANT,
                input.getIntOr("Variant", 0)
        );
    }

    /* ---------------- PICK VARIANT METHOD ---------------- */

    private static RaccoonVariant pickVariant(RandomSource random, @Nullable ResourceKey<Biome> biomeKey){
        if(random.nextFloat() < 0.02F){return Util.getRandom(RARE_VARIANTS, random);}

        if(random.nextFloat() < 0.1F){return Util.getRandom(UNCOMMON_VARIANTS, random);}

        if(biomeKey == null){return RaccoonVariant.DEFAULT;}

        if (biomeKey.equals(Biomes.TAIGA)) {
            return random.nextBoolean()
                    ? RaccoonVariant.DEFAULT
                    : RaccoonVariant.GRAY;

        } else if (biomeKey.equals(Biomes.FOREST)) {
            return random.nextBoolean()
                    ? RaccoonVariant.GRAY
                    : RaccoonVariant.BROWN;

        } else if (biomeKey.equals(Biomes.SWAMP)) {
            return RaccoonVariant.SWAMP;

        } else {return RaccoonVariant.DEFAULT;}
    }
}