package com.ibcarpet.mixin;

import com.ibcarpet.IbcarpetSettings;
import com.ibcarpet.distributions.Distribution;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Projectile.class)
public class ProjectileMixin {
    @WrapOperation(
            method = "getMovementToShoot",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"
            )
    )
    private Vec3 add(
            Vec3 normalizedDeltaPositionInstance,
            double originalVelocityX,
            double originalVelocityY,
            double originalVelocityZ,
            Operation<Vec3> original,
            double xd,
            double yd,
            double zd,
            float _pow,
            float uncertainty
    ) {
        if (!IbcarpetSettings.enabled) {
            return original.call(normalizedDeltaPositionInstance, originalVelocityX, originalVelocityY, originalVelocityZ);
        }

        Distribution distribution = Distribution.getCurrentDistribution();

        Vec3 newVelocity = new Vec3(
                distribution.sample() * 0.0172275 * (double) uncertainty,
                distribution.sample() * 0.0172275 * (double) uncertainty,
                distribution.sample() * 0.0172275 * (double) uncertainty
        );

        return original.call(normalizedDeltaPositionInstance, newVelocity.x, newVelocity.y, newVelocity.z);
    }

}