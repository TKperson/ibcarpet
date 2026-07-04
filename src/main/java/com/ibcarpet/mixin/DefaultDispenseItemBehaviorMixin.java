package com.ibcarpet.mixin;

import com.ibcarpet.IBCarpetSettings;
import com.ibcarpet.distributions.Distribution;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DefaultDispenseItemBehavior.class)
public class DefaultDispenseItemBehaviorMixin {

    @WrapOperation(method = "spawnItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"
    ))
    private static void setDeltaMovement(
            ItemEntity itemEntity,
            double originalVelocityX,
            double originalVelocityY,
            double originalVelocityZ,
            Operation<ItemEntity> original,
            Level level,
            ItemStack _itemStack,
            int accuracy,
            Direction direction,
            Position _position
    ) {
        if (!IBCarpetSettings.insaneBehaviors) {
            original.call(itemEntity, originalVelocityX, originalVelocityY, originalVelocityZ);
            return;
        }

        Distribution distribution = Distribution.getCurrentDistribution();
        double pow = (distribution.sample() + 1) / 2 * 0.1 + 0.2;

        Vec3 newVelocity = new Vec3(
                direction.getStepX() * pow + distribution.sample() * accuracy * 0.0172275,
                0.2 + distribution.sample() * accuracy * 0.0172275,
                direction.getStepZ() * pow + distribution.sample() * accuracy * 0.0172275
        );

        original.call(itemEntity, newVelocity.x, newVelocity.y, newVelocity.z);
    }
}