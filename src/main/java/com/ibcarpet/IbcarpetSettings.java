package com.ibcarpet;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import carpet.api.settings.Validators;
import static carpet.api.settings.RuleCategory.*;

import net.minecraft.commands.CommandSourceStack;

public class IbcarpetSettings {
    public static final String IB = "IBCarpet";

    public enum Distribution {
        UNIFORM,
        DISCRETE,
        BIMODAL,
    }
    @Rule(
            options = {"true", "false"},
            categories = {CREATIVE, IB}
    )
    public static boolean enabled = false;

    @Rule(
        categories = {CREATIVE, IB}
    )
    public static Distribution distribution = Distribution.UNIFORM;

    public static class ResolutionValidator extends Validator<Integer> {
        @Override
        public Integer validate(CommandSourceStack source, CarpetRule<Integer> currentRule, Integer newValue, String string) {
            return newValue >= 2 ? newValue : null;
        }

        @Override
        public String description() { return "You must pick an integer greater than 2";}
    }

    @Rule(
            validators = ResolutionValidator.class,
            categories = {CREATIVE, IB}
    )
    public static int discreteDistributionResolution = 4;

    @Rule(
            validators = Validators.Probablity.class,
            categories = {CREATIVE, IB}
    )
    public static double bimodalExtremeness = 0.5;
}
