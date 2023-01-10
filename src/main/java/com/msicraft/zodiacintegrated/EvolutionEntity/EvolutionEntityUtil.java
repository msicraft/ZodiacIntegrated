package com.msicraft.zodiacintegrated.EvolutionEntity;

import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

public class EvolutionEntityUtil {

    private final EntityAttributeUtil attributeUtil = new EntityAttributeUtil();

    public ArrayList<String> getRegisteredEntities() {
        ArrayList<String> list = new ArrayList<>();
        ConfigurationSection section = ZodiacIntegrated.evolutionDataConfig.getConfig().getConfigurationSection("Entity");
        if (section != null) {
            Set<String> entityList = section.getKeys(false);
            list.addAll(entityList);
        }
        return list;
    }

    public ArrayList<String> getEntityBlackList() {
        ArrayList<String> list = new ArrayList<>();
        for (String a : ZodiacIntegrated.evolutionConfig.getConfig().getStringList("Setting.BlackList")) {
            list.add(a.toUpperCase());
        }
        return list;
    }

    public boolean isExistData(LivingEntity livingEntity) {
        return getRegisteredEntities().contains(livingEntity.getType().toString().toUpperCase());
    }

    public void registerData(LivingEntity livingEntity) {
        double baseHealth = attributeUtil.getBaseHealth(livingEntity);
        double baseDamage = attributeUtil.getBaseDamage(livingEntity);
        double baseArmor = attributeUtil.getBaseArmor(livingEntity);
        double baseArmorToughness = attributeUtil.getBaseArmorToughness(livingEntity);
        double baseMovementSpeed = attributeUtil.getBaseMovementSpeed(livingEntity);
        double baseKnockbackResistance = attributeUtil.getKnockbackResistance(livingEntity);
        String type = livingEntity.getType().toString().toUpperCase();
        ZodiacIntegrated.evolutionDataConfig.getConfig().set("Entity." + type + ".Health", baseHealth);
        ZodiacIntegrated.evolutionDataConfig.getConfig().set("Entity." + type + ".Damage", baseDamage);
        ZodiacIntegrated.evolutionDataConfig.getConfig().set("Entity." + type + ".Armor", baseArmor);
        ZodiacIntegrated.evolutionDataConfig.getConfig().set("Entity." + type + ".ArmorToughness", baseArmorToughness);
        ZodiacIntegrated.evolutionDataConfig.getConfig().set("Entity." + type + ".MovementSpeed", baseMovementSpeed);
        ZodiacIntegrated.evolutionDataConfig.getConfig().set("Entity." + type + ".KnockbackResistance", baseKnockbackResistance);
        ZodiacIntegrated.evolutionDataConfig.getConfig().set("Entity." + type + ".Count", 0);
        ZodiacIntegrated.evolutionDataConfig.saveConfig();
    }

    public boolean hasNearbyPlayer(LivingEntity livingEntity) {
        boolean check = false;
        double value = ZodiacIntegrated.evolutionConfig.getConfig().getDouble("Setting.Check-Nearby-Player.Radius");
        for (Entity entity : livingEntity.getNearbyEntities(value, value, value)) {
            if (entity instanceof Player) {
                check = true;
                break;
            }
        }
        return check;
    }

}
