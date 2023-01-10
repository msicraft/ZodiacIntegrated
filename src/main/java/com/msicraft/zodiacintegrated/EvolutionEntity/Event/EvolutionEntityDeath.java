package com.msicraft.zodiacintegrated.EvolutionEntity.Event;

import com.msicraft.zodiacintegrated.EvolutionEntity.EvolutionEntityUtil;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.MobExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EvolutionEntityDeath implements Listener {

    private final EvolutionEntityUtil evolutionMonsterUtil = new EvolutionEntityUtil();

    @EventHandler
    public void onEntityRegisterData(EntityDeathEvent e) {
        if (ZodiacIntegrated.evolutionConfig.getConfig().getBoolean("Setting.Enabled")) {
            if (e.getEntityType() != EntityType.PLAYER) {
                LivingEntity livingEntity = e.getEntity();
                if (livingEntity instanceof Monster monster) {
                    MobExecutor mobExecutor = MythicBukkit.inst().getMobManager();
                    if (!mobExecutor.isMythicMob(livingEntity) && !evolutionMonsterUtil.isExistData(livingEntity)) {
                        evolutionMonsterUtil.registerData(livingEntity);
                    }
                }
            }
        }
    }

}
