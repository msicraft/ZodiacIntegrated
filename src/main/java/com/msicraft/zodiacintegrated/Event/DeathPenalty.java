package com.msicraft.zodiacintegrated.Event;

import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DeathPenalty implements Listener {

    private final Random random = new Random();

    private final ArrayList<EquipmentSlot> equipmentSlotArrayList = new ArrayList<>(Arrays.asList(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.OFF_HAND));

    @EventHandler
    public void onPlayerDeathPenalty(PlayerDeathEvent e) {
        if (ZodiacIntegrated.getPlugin().getConfig().getBoolean("DeathPenalty.Enabled")) {
            Player player = e.getPlayer();
            Location deathLocation = player.getLocation();
            World world = deathLocation.getWorld();
            if (ZodiacIntegrated.getPlugin().getConfig().getStringList("DeathPenalty.Whitelist").contains(world.getName())) {
                int maxLose = ZodiacIntegrated.getPlugin().getConfig().getInt("DeathPenalty.Max-LoseCount");
                int maxTryCount = ZodiacIntegrated.getPlugin().getConfig().getInt("DeathPenalty.Max-TryCount");
                int maxDropCount = random.nextInt(maxLose);
                int dropCount = 0;
                ItemStack airItemStack = new ItemStack(Material.AIR, 1);
                for (int a = 0; a<maxTryCount; a++) {
                    if (dropCount < maxDropCount) {
                        int randomSlot = random.nextInt(9);
                        ItemStack itemStack = player.getInventory().getItem(randomSlot);
                        if (itemStack != null && itemStack.getType() != Material.AIR) {
                            player.getInventory().setItem(randomSlot, airItemStack);
                            world.dropItemNaturally(deathLocation, itemStack);
                            dropCount++;
                        }
                    } else {
                        break;
                    }
                }
                if (dropCount <= maxDropCount) {
                    double weightPercent = ZodiacIntegrated.getPlugin().getConfig().getDouble("DeathPenalty.Additional-Percent");
                    if (random.nextDouble() <= weightPercent) {
                        int a = random.nextInt(equipmentSlotArrayList.size());
                        ItemStack itemStack = player.getInventory().getItem(equipmentSlotArrayList.get(a));
                        if (itemStack.getType() != Material.AIR) {
                            player.getInventory().setItem(equipmentSlotArrayList.get(a), airItemStack);
                            world.dropItemNaturally(deathLocation, itemStack);
                        }
                    }
                }
            }
        }
    }

}
