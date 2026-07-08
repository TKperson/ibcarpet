package com.ibcarpet.mixin;

import com.ibcarpet.IBCarpetSettings;
import com.ibcarpet.distributions.Distribution;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Containers.class)
public class ContainersMixin {

    @WrapOperation(method = "dropItemStack", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"
    ))
    private static void setDeltaMovement(
            ItemEntity itemEntity,
            double originalVelocityX,
            double originalVelocityY,
            double originalVelocityZ,
            Operation<Void> original,
            Level level,
            double originalX,
            double originalY,
            double originalZ,
            ItemStack _itemStack
    ) {
        if (!IBCarpetSettings.insaneBehaviors) {
            original.call(itemEntity, originalVelocityX, originalVelocityY, originalVelocityZ);
            return;
        }

        Distribution distribution = Distribution.getCurrentDistribution();

        // setting velocity of itemEntity
        Vec3 newVelocity = new Vec3(
                distribution.sample() * 0.11485000171139836,
                distribution.sample() * 0.11485000171139836 + 0.2,
                distribution.sample() * 0.11485000171139836
        );
        itemEntity.setDeltaMovement(newVelocity);

        // setting position of itemEntity
        double size = EntityTypes.ITEM.getWidth();
        double centerRange = 1.0 - size;
        double halfSize = size / 2.0;
        Vec3 newPos = new Vec3(
                Math.floor(originalX) + distribution.sampleUnit() * centerRange + halfSize,
                Math.floor(originalY) + distribution.sampleUnit() * centerRange,
                Math.floor(originalZ) + distribution.sampleUnit() * centerRange + halfSize
        );
        itemEntity.setPos(newPos);
    }
}