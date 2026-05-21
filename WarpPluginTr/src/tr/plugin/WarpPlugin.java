package tr.moon.warp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class WarpPlugin extends JavaPlugin {

    private static WarpPlugin instance;
    private Map<String, WarpLocation> warps;
    private Map<UUID, Integer> playerPage;

    @Override
    public void onEnable() {
        instance = this;
        warps = new HashMap<>();
        playerPage = new HashMap<>();
        
        saveDefaultConfig();
        loadWarps();
        
        getServer().getPluginManager().registerEvents(new WarpGUIListener(), this);
        
        getLogger().info("§a[WarpPlugin] §2Başarıyla aktif edildi!");
    }

    @Override
    public void onDisable() {
        saveWarps();
        getLogger().info("§c[WarpPlugin] §4Devre dışı bırakıldı.");
    }

    public static WarpPlugin getInstance() {
        return instance;
    }

    public void loadWarps() {
        FileConfiguration config = getConfig();
        if (config.contains("warps")) {
            for (String name : config.getConfigurationSection("warps").getKeys(false)) {
                String worldName = config.getString("warps." + name + ".world");
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    double x = config.getDouble("warps." + name + ".x");
                    double y = config.getDouble("warps." + name + ".y");
                    double z = config.getDouble("warps." + name + ".z");
                    float yaw = (float) config.getDouble("warps." + name + ".yaw");
                    float pitch = (float) config.getDouble("warps." + name + ".pitch");
                    Location loc = new Location(world, x, y, z, yaw, pitch);
                    warps.put(name.toLowerCase(), new WarpLocation(name, loc));
                }
            }
        }
    }

    public void saveWarps() {
        FileConfiguration config = getConfig();
        config.set("warps", null);
        for (WarpLocation warp : warps.values()) {
            config.set("warps." + warp.getName() + ".world", warp.getLocation().getWorld().getName());
            config.set("warps." + warp.getName() + ".x", warp.getLocation().getX());
            config.set("warps." + warp.getName() + ".y", warp.getLocation().getY());
            config.set("warps." + warp.getName() + ".z", warp.getLocation().getZ());
            config.set("warps." + warp.getName() + ".yaw", warp.getLocation().getYaw());
            config.set("warps." + warp.getName() + ".pitch", warp.getLocation().getPitch());
        }
        saveConfig();
    }

    public void addWarp(String name, Player player) {
        warps.put(name.toLowerCase(), new WarpLocation(name, player.getLocation()));
        saveWarps();
        player.sendMessage(ChatColor.GREEN + "✅ Warp '" + name + "' oluşturuldu!");
    }

    public void removeWarp(String name, Player player) {
        if (warps.remove(name.toLowerCase()) != null) {
            saveWarps();
            player.sendMessage(ChatColor.RED + "❌ Warp '" + name + "' silindi!");
        } else {
            player.sendMessage(ChatColor.RED + "⚠️ Warp bulunamadı!");
        }
    }

    public void teleportToWarp(String name, Player player) {
        WarpLocation warp = warps.get(name.toLowerCase());
        if (warp != null) {
            player.teleport(warp.getLocation());
            player.sendMessage(ChatColor.AQUA + "✨ " + ChatColor.YELLOW + name + ChatColor.AQUA + " warp'ına ışınlandın!");
        } else {
            player.sendMessage(ChatColor.RED + "⚠️ Warp bulunamadı!");
        }
    }

    public Map<String, WarpLocation> getWarps() {
        return warps;
    }

    public int getPlayerPage(Player player) {
        return playerPage.getOrDefault(player.getUniqueId(), 0);
    }

    public void setPlayerPage(Player player, int page) {
        playerPage.put(player.getUniqueId(), page);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Bu komut sadece oyuncular içindir!");
            return true;
        }

        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("setwarp")) {
            if (args.length < 1) {
                p.sendMessage(ChatColor.RED + "❌ Kullanım: /setwarp <isim>");
                return true;
            }
            addWarp(args[0], p);
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("delwarp")) {
            if (args.length < 1) {
                p.sendMessage(ChatColor.RED + "❌ Kullanım: /delwarp <isim>");
                return true;
            }
            removeWarp(args[0], p);
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("warps")) {
            WarpGUI.open(p, 0);
            return true;
        }

        return false;
    }
}