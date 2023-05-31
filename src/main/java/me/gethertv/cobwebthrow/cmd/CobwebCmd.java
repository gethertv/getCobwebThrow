package me.gethertv.cobwebthrow.cmd;

import me.gethertv.cobwebthrow.CobwebThrow;
import me.gethertv.cobwebthrow.data.Cuboid;
import me.gethertv.cobwebthrow.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CobwebCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("cobweb.admin"))
            return false;

        if(args.length==1)
        {
            if(args[0].equalsIgnoreCase("reload"))
            {
                CobwebThrow.getInstance().reloadCustomPlugin();
                sender.sendMessage(Color.addColors("&aPomyslnie przeladowano plugin!"));
                return false;
            }
        }
        if(args.length==4)
        {
            if(!args[0].equalsIgnoreCase("give"))
                return false;

            if(!args[1].equalsIgnoreCase("cobweb") && !args[1].equalsIgnoreCase("antycobweb"))
            {
                sender.sendMessage(Color.addColors("&cMusisz podac cobweb lub antycobweb"));
                return false;
            }
            if(Bukkit.getPlayer(args[2])==null)
            {
                sender.sendMessage(Color.addColors("&cPodany gracz nie jest online!"));
                return false;
            }
            Player targer = Bukkit.getPlayer(args[2]);
            if(!isInt(args[3]))
            {
                sender.sendMessage(Color.addColors("&cMusisz podac liczbe calkowita!"));
                return false;
            }
            int amount = Integer.parseInt(args[3]);
            ItemStack itemStack = null;
            if(args[1].equalsIgnoreCase("cobweb"))
                itemStack=CobwebThrow.getCobwebItem().clone();
            else
                itemStack=CobwebThrow.getRemoveItem().clone();

            itemStack.setAmount(amount);
            targer.getInventory().addItem(itemStack);
            sender.sendMessage(Color.addColors("&aPomyslnie nadano przedmiot dla {player}".replace("{player}", targer.getName())));
            return true;
        }

        if(!(sender instanceof Player))
            return false;

        if(args.length==1)
        {
            if(args[0].equals("deny"))
            {
                if(CobwebThrow.getFirst()==null || CobwebThrow.getSelector()==null)
                {
                    sender.sendMessage(Color.addColors("&cUstaw /selector"));
                    return false;
                }
                CobwebThrow.getInstance().getConfig().set("deny.first", CobwebThrow.getFirst());
                CobwebThrow.getInstance().getConfig().set("deny.second", CobwebThrow.getSecond());
                CobwebThrow.getInstance().saveConfig();
                CobwebThrow.setDenyCuboid(new Cuboid(CobwebThrow.getFirst(), CobwebThrow.getSecond()));
                sender.sendMessage(Color.addColors("&aPomyslnie stworzono cuboid!"));
                return true;
            }
            if(args[0].equals("selector")) {
                Player player = (Player) sender;
                player.getInventory().addItem(CobwebThrow.getSelector().clone());
                player.sendMessage(Color.addColors("&aOtrzymales selector!"));
                return false;
            }
            if(args[0].equals("region"))
            {
                if(CobwebThrow.getFirst()==null || CobwebThrow.getSelector()==null)
                {
                    sender.sendMessage(Color.addColors("&cUstaw /selector"));
                    return false;
                }
                CobwebThrow.getInstance().getConfig().set("loc.first", CobwebThrow.getFirst());
                CobwebThrow.getInstance().getConfig().set("loc.second", CobwebThrow.getSecond());
                CobwebThrow.getInstance().saveConfig();
                CobwebThrow.setCuboid(new Cuboid(CobwebThrow.getFirst(), CobwebThrow.getSecond()));
                sender.sendMessage(Color.addColors("&aPomyslnie stworzono cuboid!"));
                return true;
            }

        }
        return false;
    }

    private boolean isInt(String arg) {
        try {
            int a = Integer.parseInt(arg);
            return true;
        } catch (NumberFormatException e) {}
        return false;
    }
}
