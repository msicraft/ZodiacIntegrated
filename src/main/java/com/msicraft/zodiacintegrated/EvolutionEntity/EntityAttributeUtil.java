package com.msicraft.zodiacintegrated.EvolutionEntity;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

public class EntityAttributeUtil {

    public double getBaseHealth(LivingEntity livingEntity) {
        double value = 20;
        AttributeInstance instance = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (instance != null) {
            double base = instance.getBaseValue();
            value = (Math.round(base*100)/100.0);
        }
        return value;
    }

    public double getBaseDamage(LivingEntity livingEntity) {
        double value = 0;
        AttributeInstance instance = livingEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (instance != null) {
            double base = instance.getBaseValue();
            value = (Math.round(base*100)/100.0);
        }
        return value;
    }

    public double getBaseArmor(LivingEntity livingEntity) {
        double value = 1;
        AttributeInstance instance = livingEntity.getAttribute(Attribute.GENERIC_ARMOR);
        if (instance != null) {
            double base = instance.getBaseValue();
            value = (Math.round(base*100)/100.0);
        }
        return value;
    }

    public double getBaseArmorToughness(LivingEntity livingEntity) {
        double value = 0;
        AttributeInstance instance = livingEntity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
        if (instance != null) {
            double base = instance.getBaseValue();
            value = (Math.round(base*100)/100.0);
        }
        return value;
    }

    public double getBaseMovementSpeed(LivingEntity livingEntity) {
        double value = 0.2;
        AttributeInstance instance = livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (instance != null) {
            double base = instance.getBaseValue();
            value = (Math.round(base*100)/100.0);
        }
        return value;
    }

    public double getKnockbackResistance(LivingEntity livingEntity){
        double value = 0;
        AttributeInstance instance = livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (instance != null) {
            double base = instance.getBaseValue();
            value = (Math.round(base*100)/100.0);
        }
        return value;
    }

}
