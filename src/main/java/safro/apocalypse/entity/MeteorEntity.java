package safro.apocalypse.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class MeteorEntity extends Entity {
    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.INT);
    private int life;

    public MeteorEntity(EntityType<? extends MeteorEntity> type, Level pLevel) {
        super(type, pLevel);
        this.entityData.set(TYPE, Mth.nextInt(this.level().random, 1, 4));
    }

    public MeteorEntity(Level level, double x, double y, double z) {
        this(ApocalypseEntities.METEOR.get(), level);
        this.life = 0;
        this.setPos(x, y, z);
        this.setDeltaMovement(this.random.triangle(0.0D, 0.002297D), -0.05D, this.random.triangle(0.0D, 0.002297D));
    }

    public void tick() {
        super.tick();
        boolean inGround = false;
        boolean flag = this.noPhysics;
        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level().getBlockState(blockpos);
        if (!blockstate.isAir() && !flag) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.level(), blockpos);
            if (!voxelshape.isEmpty()) {
                Vec3 vec31 = this.position();

                for (AABB aabb : voxelshape.toAabbs()) {
                    if (aabb.move(blockpos).contains(vec31)) {
                        inGround = true;
                        break;
                    }
                }
            }
        }

        if (inGround) {
            this.impact(blockpos);
        } else {
            Vec3 vec32 = this.position();
            Vec3 vec33 = vec32.add(this.getDeltaMovement());
            HitResult hitresult = this.level().clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (hitresult.getType() != HitResult.Type.MISS) {
                vec33 = hitresult.getLocation();
            }

            EntityHitResult entityhitresult = this.findHitEntity(vec32, vec33);
            if (entityhitresult != null) {
                hitresult = entityhitresult;
            }

            if (hitresult != null && hitresult.getType() != HitResult.Type.MISS && !flag) {
                this.onHit(hitresult);
                this.hasImpulse = true;
            }
        }

        this.life++;
        if (this.level().isClientSide) {
            for (int i = 0; i < 5; i++) {
                this.level().addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);
                if (this.random.nextFloat() <= 0.2F) {
                    this.level().addParticle(ParticleTypes.FALLING_LAVA, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);
                }
            }
        }

        Vec3 vec33 = this.getDeltaMovement();
        this.move(MoverType.SELF, vec33);
        this.setDeltaMovement(vec33.x, vec33.y - (double)0.05F, vec33.z);
        this.checkInsideBlocks();
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, pStartVec, pEndVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), p -> true);
    }

    protected void onHit(HitResult pResult) {
        if (pResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) pResult;
            if (entityHitResult.getEntity() instanceof LivingEntity target) {
                this.impact(target.blockPosition());
            }
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, pResult.getLocation(), GameEvent.Context.of(this, null));
        } else if (pResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockhitresult = (BlockHitResult) pResult;
            BlockPos blockpos = blockhitresult.getBlockPos();
            this.impact(blockhitresult.getBlockPos());
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, blockpos, GameEvent.Context.of(this, this.level().getBlockState(blockpos)));
        }
    }

    protected void impact(BlockPos pos) {
        if (!this.level().isClientSide()) {
            ServerLevel level = (ServerLevel) this.level();
            level.explode(this, pos.getX(), pos.getY(), pos.getZ(), 10.0F, true, Level.ExplosionInteraction.MOB);
            this.discard();
        }
    }

    public Block getMeteorType() {
        return switch (this.entityData.get(TYPE)) {
            case 2 -> Blocks.BLACKSTONE;
            case 3 -> Blocks.TUFF;
            case 4 -> Blocks.RED_SAND;
            default -> Blocks.CRYING_OBSIDIAN;
        };
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.life = pCompound.getInt("Life");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("Life", this.life);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(TYPE, 1);
    }
}
