package com.ibcarpet.mixin;

import com.ibcarpet.IBCarpetSettings;
import com.ibcarpet.distributions.Distribution;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VehicleEntity.class)
public class VehicleEntityMixin {
    @WrapOperation(
            method = "destroy(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/Item;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/vehicle/VehicleEntity;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
            )
    )
    private ItemEntity spawnAtLocation(
            VehicleEntity instance,
            ServerLevel level,
            ItemStack itemStack,
            Operation<ItemEntity> original
    ) {
        if (!IBCarpetSettings.insaneBehaviors) {
            return original.call(instance, level, itemStack);
        }

        if (itemStack.isEmpty()) {
            return null;
        }

        Distribution distribution = Distribution.getCurrentDistribution();

        ItemEntity entity = new ItemEntity(
                level,
                instance.getX(),
                instance.getY(),
                instance.getZ(),
                itemStack,
                distribution.sampleUnit() * 0.2 - 0.1,
                0.2,
                distribution.sampleUnit() * 0.2 - 0.1
        );

        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);

        return entity;
    }
}