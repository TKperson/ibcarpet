package com.ibcarpet;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;

import static carpet.api.settings.RuleCategory.*;

import net.minecraft.commands.CommandSourceStack;

public class IBCarpetSettings {
    public static final String IB = "IBCarpet";

    public enum DistributionType {
        UNIFORM,
        DISCRETE,
        BIMODAL,
    }
    @Rule(
            options = {"true", "false"},
            categories = {CREATIVE, IB}
    )
    public static boolean insaneBehaviors = false;

    @Rule(
        categories = {CREATIVE, IB}
    )
    public static DistributionType ibDistribution = DistributionType.UNIFORM;

    public static class ResolutionValidator extends Validator<Integer> {
        @Override
        public Integer validate(CommandSourceStack source, CarpetRule<Integer> currentRule, Integer newValue, String string) {
            return newValue >= 0 ? newValue : null;
        }

        @Override
        public String description() { return "Must pick a non-negative integer.";}
    }

    @Rule(
            validators = ResolutionValidator.class,
            categories = {CREATIVE, IB}
    )
    public static int ibDiscreteCuts = 4;

    public static class BimodalExtremenessValidator<T extends Number> extends Validator<T> {
        public BimodalExtremenessValidator() {
        }

        public T validate(CommandSourceStack source, CarpetRule<T> currentRule, T newValue, String string) {
            return (newValue.doubleValue() >= (double)0.0F && newValue.doubleValue() < (double)1.0F ? newValue : null);
        }

        public String description() {
            return "Must be between 0 and 1, but can't be exactly 1.";
        }
    }

    @Rule(
            validators = BimodalExtremenessValidator.class,
            categories = {CREATIVE, IB}
    )
    public static double ibBimodalExtremeness = 0.8;
}
