package me.gethertv.cobwebthrow;

import me.gethertv.cobwebthrow.cmd.CobwebCmd;
import me.gethertv.cobwebthrow.data.Cuboid;
import me.gethertv.cobwebthrow.listeners.InteractionEvent;
import me.gethertv.cobwebthrow.scheduler.CleanCobwebTask;
import me.gethertv.cobwebthrow.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public final class CobwebThrow extends JavaPlugin {

    private static ItemStack cobwebItem;
    private static ItemStack removeItem;
    private static CobwebThrow instance;

    private static Cuboid cuboid;
    private static Cuboid denyCuboid;

    private static ItemStack selector;
    private static Location first;
    private static Location second;

    private CleanCobwebTask cleanCobwebTask;
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        loadItem();
        loadRemoveItem();
        loadSelector();

        new InteractionEvent(this);

        CobwebCmd cobwebCmd = new CobwebCmd();
        getCommand("getcobweb").setExecutor(cobwebCmd);
        getCommand("getcobweb").setTabCompleter(cobwebCmd);

        implementRegions();
        implementTask();
    }

    @Override
    public void onDisable() {

        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);

    }

    private void implementRegions() {
        if(getConfig().getLocation("loc.first")!=null && getConfig().getLocation("loc.second")!=null)
            cuboid = new Cuboid(getConfig().getLocation("loc.first"), getConfig().getLocation("loc.second"));

        if(getConfig().getLocation("deny.first")!=null && getConfig().getLocation("deny.second")!=null)
            denyCuboid = new Cuboid(getConfig().getLocation("deny.first"), getConfig().getLocation("deny.second"));
    }

    private void implementTask() {
        if(getConfig().isSet("delay-remove.status"))
        {
            if(getConfig().getBoolean("delay-remove.status"))
            {
                int second = getConfig().getInt("delay-remove.second");
                cleanCobwebTask = new CleanCobwebTask(this, second);
            }
        }
    }

    public void reloadCustomPlugin()
    {
        reloadConfig();

        loadItem();
        loadRemoveItem();
        loadSelector();

        if(getConfig().getLocation("loc.first")!=null && getConfig().getLocation("loc.second")!=null)
            cuboid = new Cuboid(getConfig().getLocation("loc.first"), getConfig().getLocation("loc.second"));

        if(getConfig().getLocation("deny.first")!=null && getConfig().getLocation("deny.second")!=null)
            denyCuboid = new Cuboid(getConfig().getLocation("deny.first"), getConfig().getLocation("deny.second"));
    }

    private void loadSelector() {
        selector = new ItemStack(Material.STICK);
        ItemMeta itemMeta = selector.getItemMeta();
        itemMeta.setDisplayName(Color.addColors("&aSelector"));
        selector.setItemMeta(itemMeta);
    }

    private void loadItem() {
        cobwebItem = new ItemStack(Material.valueOf(getConfig().getString("string.material").toUpperCase()));
        cobwebItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta itemMeta = cobwebItem.getItemMeta();
        itemMeta.setDisplayName(Color.addColors(getConfig().getString("string.displayname")));
        List<String> lore = new ArrayList<>();
        lore.addAll(getConfig().getStringList("string.lore"));
        itemMeta.setLore(Color.addColors(lore));
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        cobwebItem.setItemMeta(itemMeta);
    }

    private void loadRemoveItem() {
        removeItem = new ItemStack(Material.valueOf(getConfig().getString("soul.material").toUpperCase()));
        removeItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta itemMeta = removeItem.getItemMeta();
        itemMeta.setDisplayName(Color.addColors(getConfig().getString("soul.displayname")));
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore = new ArrayList<>();
        lore.addAll(getConfig().getStringList("soul.lore"));
        itemMeta.setLore(Color.addColors(lore));
        removeItem.setItemMeta(itemMeta);
    }



    public CleanCobwebTask getCleanCobwebTask() {
        return cleanCobwebTask;
    }

    public static ItemStack getRemoveItem() {
        return removeItem;
    }

    public static Cuboid getCuboid() {
        return cuboid;
    }

    public static ItemStack getSelector() {
        return selector;
    }

    public static void setCobwebItem(ItemStack cobwebItem) {
        CobwebThrow.cobwebItem = cobwebItem;
    }

    public static void setDenyCuboid(Cuboid denyCuboid) {
        CobwebThrow.denyCuboid = denyCuboid;
    }

    public static Cuboid getDenyCuboid() {
        return denyCuboid;
    }

    public static Location getFirst() {
        return first;
    }

    public static Location getSecond() {
        return second;
    }

    public static void setFirst(Location first) {
        CobwebThrow.first = first;
    }

    public static void setSecond(Location second) {
        CobwebThrow.second = second;
    }

    public static void setCuboid(Cuboid cuboid) {
        CobwebThrow.cuboid = cuboid;
    }

    public static CobwebThrow getInstance() {
        return instance;
    }

    public static ItemStack getCobwebItem() {
        return cobwebItem;
    }
}
