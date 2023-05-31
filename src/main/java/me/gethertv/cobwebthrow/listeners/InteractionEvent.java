package me.gethertv.cobwebthrow.listeners;

import me.gethertv.cobwebthrow.CobwebThrow;
import me.gethertv.cobwebthrow.data.Cuboid;
import me.gethertv.cobwebthrow.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class InteractionEvent implements Listener {

    private static List<Fireball> fireballList = new ArrayList<>();

    @EventHandler
    public void onClick(PlayerInteractEvent event)
    {
        if (event.getHand()!=null && event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        if(event.getAction()==Action.LEFT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR)
                return;

            if (player.getItemInHand().isSimilar(CobwebThrow.getSelector())) {
                if (!player.hasPermission("selector.admin"))
                    return;

                event.setCancelled(true);
                CobwebThrow.setFirst(event.getClickedBlock().getLocation());
                player.sendMessage(Color.addColors("&aPomyslnie ustawiono LEWY"));
                return;
            }
        }
        if(event.getAction()==Action.RIGHT_CLICK_BLOCK)
        {

            Player player = event.getPlayer();
            if(player.getItemInHand()==null || player.getItemInHand().getType()== Material.AIR)
                return;

            if(player.getItemInHand().isSimilar(CobwebThrow.getSelector()))
            {
                if(!player.hasPermission("selector.admin"))
                    return;

                event.setCancelled(true);
                CobwebThrow.setSecond(event.getClickedBlock().getLocation());
                player.sendMessage(Color.addColors("&aPomyslnie ustawiono PRAWY"));
                return;
            }
        }

        if(event.getAction()== Action.RIGHT_CLICK_AIR || event.getAction()==Action.RIGHT_CLICK_BLOCK)
        {
            Player player = event.getPlayer();
            if(player.getItemInHand()==null || player.getItemInHand().getType()== Material.AIR)
                return;


            if(player.getItemInHand().isSimilar(CobwebThrow.getCobwebItem())) {

                event.setCancelled(true);
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);

                Fireball fireball;
                int fb_speed = 1;
                final Vector fb_direction = player.getEyeLocation().getDirection().multiply(fb_speed);
                player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 10, 1);
                fireball = player.getWorld().spawn(player.getEyeLocation().add(fb_direction.getX(), fb_direction.getY(), fb_direction.getZ()), Fireball.class);
                fireball.setShooter(player);
                fireball.setVelocity(fb_direction);
                fireball.setBounce(false);
                fireball.setIsIncendiary(false);
                fireball.setYield(0);
                fireballList.add(fireball);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (fireball.isDead()) {
                            cancel();
                            Location loc = fireball.getLocation();
                            spawnCobweb(loc);
                        }
                    }
                }.runTaskTimer(CobwebThrow.getInstance(), 0L, 2L);
                //Bukkit.getServer().getPluginManager().callEvent(new EntityExplodeEvent(fireball));
                return;
            }

            if(player.getItemInHand().isSimilar(CobwebThrow.getRemoveItem()))
            {
                event.setCancelled(true);
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                removeItem(player);
            }
        }
    }

    private void removeItem(Player player) {
        Location loc = player.getLocation();

        if(CobwebThrow.getCuboid()!=null) {
            if (!CobwebThrow.getCuboid().contains(loc))
                return;

        }
        if(CobwebThrow.getDenyCuboid()!=null) {
            if (CobwebThrow.getDenyCuboid().contains(loc))
                return;
        }

        if(loc.clone().getBlock().getType()==Material.AIR)
            loc.clone().getBlock().setType(Material.AIR);

        for(int i = 0; i<3; i++) {
            if (loc.clone().add(-2, i, -2).getBlock().getType() == Material.COBWEB)
                loc.clone().add(-2, i, -2).getBlock().setType(Material.AIR);
            if (loc.clone().add(-2, i, -1).getBlock().getType() == Material.COBWEB)
                loc.clone().add(-2, i, -1).getBlock().setType(Material.AIR);
            if (loc.clone().add(-2, i, 0).getBlock().getType() == Material.COBWEB)
                loc.clone().add(-2, i, 0).getBlock().setType(Material.AIR);
            if (loc.clone().add(-2, i, 1).getBlock().getType() == Material.COBWEB)
                loc.clone().add(-2, i, 1).getBlock().setType(Material.AIR);
            if (loc.clone().add(-2, i, 2).getBlock().getType() == Material.COBWEB)
                loc.clone().add(-2, i, 2).getBlock().setType(Material.AIR);

            if (loc.clone().add(-1, i, -2).getBlock().getType() == Material.COBWEB)
                loc.clone().add(-1, i, -2).getBlock().setType(Material.AIR);
            if (loc.clone().add(-1, i, -1).getBlock().getType() == Material.COBWEB)
                loc.clone().add(-1, i, -1).getBlock().setType(Material.AIR);
            if (loc.clone().add(-1, i, 0).getBlock().getType() == Material.COBWEB)
                loc.clone().add(-1, i, 0).getBlock().setType(Material.AIR);
            if (loc.clone().add(-1, i, 1).getBlock().getType() == Material.COBWEB)
                loc.clone().add(-1, i, 1).getBlock().setType(Material.AIR);
            if (loc.clone().add(-1, i, 2).getBlock().getType() == Material.COBWEB)
                loc.clone().add(-1, i, 2).getBlock().setType(Material.AIR);

            if (loc.clone().add(0, i, -2).getBlock().getType() == Material.COBWEB)
                loc.clone().add(0, i, -2).getBlock().setType(Material.AIR);
            if (loc.clone().add(0, i, -1).getBlock().getType() == Material.COBWEB)
                loc.clone().add(0, i, -1).getBlock().setType(Material.AIR);
            if (loc.clone().add(0, i, 0).getBlock().getType() == Material.COBWEB)
                loc.clone().add(0, i, 0).getBlock().setType(Material.AIR);
            if (loc.clone().add(0, i, 1).getBlock().getType() == Material.COBWEB)
                loc.clone().add(0, i, 1).getBlock().setType(Material.AIR);
            if (loc.clone().add(0, i, 2).getBlock().getType() == Material.COBWEB)
                loc.clone().add(0, i, 2).getBlock().setType(Material.AIR);

            if (loc.clone().add(1, i, -2).getBlock().getType() == Material.COBWEB)
                loc.clone().add(1, i, -2).getBlock().setType(Material.AIR);
            if (loc.clone().add(1, i, -1).getBlock().getType() == Material.COBWEB)
                loc.clone().add(1, i, -1).getBlock().setType(Material.AIR);
            if (loc.clone().add(1, i, 0).getBlock().getType() == Material.COBWEB)
                loc.clone().add(1, i, 0).getBlock().setType(Material.AIR);
            if (loc.clone().add(1, i, 1).getBlock().getType() == Material.COBWEB)
                loc.clone().add(1, i, 1).getBlock().setType(Material.AIR);
            if (loc.clone().add(1, i, 2).getBlock().getType() == Material.COBWEB)
                loc.clone().add(1, i, 2).getBlock().setType(Material.AIR);

            if (loc.clone().add(2, i, -2).getBlock().getType() == Material.COBWEB)
                loc.clone().add(2, i, -2).getBlock().setType(Material.AIR);
            if (loc.clone().add(2, i, -1).getBlock().getType() == Material.COBWEB)
                loc.clone().add(2, i, -1).getBlock().setType(Material.AIR);
            if (loc.clone().add(2, i, 0).getBlock().getType() == Material.COBWEB)
                loc.clone().add(2, i, 0).getBlock().setType(Material.AIR);
            if (loc.clone().add(2, i, 1).getBlock().getType() == Material.COBWEB)
                loc.clone().add(2, i, 1).getBlock().setType(Material.AIR);
            if (loc.clone().add(2, i, 2).getBlock().getType() == Material.COBWEB)
                loc.clone().add(2, i, 2).getBlock().setType(Material.AIR);
        }
    }

    private void spawnCobweb(Location loc) {

        if(CobwebThrow.getCuboid()!=null) {
            if (!CobwebThrow.getCuboid().contains(loc))
                return;
        }
        if(CobwebThrow.getDenyCuboid()!=null) {
            if (CobwebThrow.getDenyCuboid().contains(loc))
                return;
        }

        if(loc.clone().getBlock().getType()==Material.AIR)
            loc.clone().getBlock().setType(Material.AIR);


        for(int i = 0; i<3; i++)
        {
            if(loc.clone().add(-2, i, -2).getBlock().getType()==Material.AIR)
                loc.clone().add(-2, i, -2).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(-2, i, -1).getBlock().getType()==Material.AIR)
                loc.clone().add(-2, i, -1).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(-2, i, 0).getBlock().getType()==Material.AIR)
                loc.clone().add(-2, i, 0).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(-2, i, 1).getBlock().getType()==Material.AIR)
                loc.clone().add(-2, i, 1).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(-2, i, 2).getBlock().getType()==Material.AIR)
                loc.clone().add(-2, i, 2).getBlock().setType(Material.COBWEB);

            if(loc.clone().add(-1, i, -2).getBlock().getType()==Material.AIR)
                loc.clone().add(-1, i, -2).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(-1, i, -1).getBlock().getType()==Material.AIR)
                loc.clone().add(-1, i, -1).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(-1, i, 0).getBlock().getType()==Material.AIR)
                loc.clone().add(-1, i, 0).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(-1, i, 1).getBlock().getType()==Material.AIR)
                loc.clone().add(-1, i, 1).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(-1, i, 2).getBlock().getType()==Material.AIR)
                loc.clone().add(-1, i, 2).getBlock().setType(Material.COBWEB);

            if(loc.clone().add(0, i, -2).getBlock().getType()==Material.AIR)
                loc.clone().add(0, i, -2).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(0, i, -1).getBlock().getType()==Material.AIR)
                loc.clone().add(0, i, -1).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(0, i, 0).getBlock().getType()==Material.AIR)
                loc.clone().add(0, i, 0).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(0, i, 1).getBlock().getType()==Material.AIR)
                loc.clone().add(0, i, 1).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(0, i, 2).getBlock().getType()==Material.AIR)
                loc.clone().add(0, i, 2).getBlock().setType(Material.COBWEB);

            if(loc.clone().add(1, i, -2).getBlock().getType()==Material.AIR)
            loc.clone().add(1, i, -2).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(1, i, -1).getBlock().getType()==Material.AIR)
                loc.clone().add(1, i, -1).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(1, i, 0).getBlock().getType()==Material.AIR)
                loc.clone().add(1, i, 0).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(1, i, 1).getBlock().getType()==Material.AIR)
                loc.clone().add(1, i, 1).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(1, i, 2).getBlock().getType()==Material.AIR)
                loc.clone().add(1, i, 2).getBlock().setType(Material.COBWEB);

            if(loc.clone().add(2, i, -2).getBlock().getType()==Material.AIR)
                loc.clone().add(2, i, -2).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(2, i, -1).getBlock().getType()==Material.AIR)
                loc.clone().add(2, i, -1).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(2, i, 0).getBlock().getType()==Material.AIR)
                loc.clone().add(2, i, 0).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(2, i, 1).getBlock().getType()==Material.AIR)
                loc.clone().add(2, i, 1).getBlock().setType(Material.COBWEB);
            if(loc.clone().add(2, i, 2).getBlock().getType()==Material.AIR)
                loc.clone().add(2, i, 2).getBlock().setType(Material.COBWEB);

        }




    }

    public static List<Fireball> getFireballList() {
        return fireballList;
    }
}
