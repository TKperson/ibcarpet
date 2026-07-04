package com.ibcarpet.mixin;

/*
 * This file is part of the JoaCarpet project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Joa and contributors
 *
 * JoaCarpet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JoaCarpet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JoaCarpet.  If not, see <https://www.gnu.org/licenses/>.
 */


import com.ibcarpet.IBCarpetSettings;
import com.ibcarpet.distributions.Distribution;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.Containers;
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
            double _originX,
            double _originY,
            double _originZ,
            ItemStack _itemStack
    ) {
        if (!IBCarpetSettings.insaneBehaviors) {
            original.call(itemEntity, originalVelocityX, originalVelocityY, originalVelocityZ);
            return;
        }

        Distribution distribution = Distribution.getCurrentDistribution();

        Vec3 newVelocity = new Vec3(
                distribution.sample() * 0.11485000171139836,
                distribution.sample() * 0.11485000171139836 + 0.2,
                distribution.sample() * 0.11485000171139836
        );

        original.call(itemEntity, newVelocity.x, newVelocity.y, newVelocity.z);
    }
}