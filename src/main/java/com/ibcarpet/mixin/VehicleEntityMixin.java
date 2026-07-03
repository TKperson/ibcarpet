package com.ibcarpet.mixin;

import com.ibcarpet.IbcarpetSettings;
import com.ibcarpet.distributions.Distribution;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VehicleEntity.class)
public class VehicleEntity {
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
            Operation<ItemEntity> original,
    ) {
        if (!IbcarpetSettings.enabled) {
            return original.call(instance, level, itemStack);
        }

        Distribution distribution = Distribution.getCurrentDistribution();

        Vec3 velocity = new Vec3(
                (distribution.sample() + 1.0) / 2.0 * 0.2 - 0.1,
                0.2,
                (distribution.sample() + 1.0) / 2.0 * 0.2 - 0.1
        );

        Level level = instance.level();

        ItemEntity itemEntity = new ItemEntity(level, instance.getX(), instance.getY(), instance.getZ(), itemStack, velocity.x, velocity.y, velocity.z);
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);
        return itemEntity;
    }

}