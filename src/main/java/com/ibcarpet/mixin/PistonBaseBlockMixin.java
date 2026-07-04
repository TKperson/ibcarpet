package com.ibcarpet.mixin;

import com.ibcarpet.IBCarpetSettings;
import com.ibcarpet.distributions.Distribution;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(PistonBaseBlock.class)
public class PistonBaseBlockMixin {
    @WrapOperation(method = "moveBlocks", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/piston/PistonBaseBlock;dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;)V"
    ))
    private void dropResources(
            BlockState blockState,
            LevelAccessor level,
            BlockPos blockPos,
            @Nullable BlockEntity blockEntity,
            Operation<Void> original
    ) {
        if (!IBCarpetSettings.insaneBehaviors) {
            original.call(blockState, level, blockPos, blockEntity);
            return;
        }

        if (level instanceof ServerLevel serverLevel) {
            Block.getDrops(blockState, serverLevel, blockPos, blockEntity).forEach(stack -> customPopResource(serverLevel, blockPos, stack));
            blockState.spawnAfterBreak(serverLevel, blockPos, ItemStack.EMPTY, true);
        }
    }

    @Unique
    private static void customPopResource(final Level level, final BlockPos pos, final ItemStack itemStack) {
        double halfHeight = EntityType.ITEM.getHeight() / 2.0;
        Distribution distribution = Distribution.getCurrentDistribution();

        double x = pos.getX() + 0.5 + distribution.sample() * 0.25;
        double y = pos.getY() + 0.5 + distribution.sample() * 0.25 - halfHeight;
        double z = pos.getZ() + 0.5 + distribution.sample() * 0.25;

        double velocityX = distribution.sampleUnit() * 0.2 - 0.1;
        double velocityY = 0.2;
        double velocityZ = distribution.sampleUnit() * 0.2 - 0.1;

        Supplier<ItemEntity> entityFactory = () -> new ItemEntity(level, x, y, z, itemStack, velocityX, velocityY, velocityZ);

        if (level instanceof ServerLevel serverLevel && !itemStack.isEmpty() && serverLevel.getGameRules().get(GameRules.BLOCK_DROPS)) {
            ItemEntity entity = entityFactory.get();
            entity.setDefaultPickUpDelay();
            level.addFreshEntity(entity);
        }
    }
}