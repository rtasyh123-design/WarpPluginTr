package tr.moon.warp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

public class WarpGUI {

    private static final int GUI_SIZE = 54;
    private static final int ITEMS_PER_PAGE = 45;
    private static final int PREV_SLOT = 45;
    private static final int NEXT_SLOT = 49;
    private static final int CLOSE_SLOT = 53;

    public static void open(Player p, int page) {
        WarpPlugin plugin = WarpPlugin.getInstance();
        Map<String, WarpLocation> warps = plugin.getWarps();
        List<String> warpNames = new ArrayList<>(warps.keySet());
        
        int totalPages = (int) Math.ceil(warpNames.size() / (double) ITEMS_PER_PAGE);
        if (totalPages == 0) totalPages = 1;
        
        if (page < 0) page = 0;
        if (page >= totalPages) page = totalPages - 1;
        
        plugin.setPlayerPage(p, page);
        
        String title = ChatColor.DARK_BLUE + "⭐ GELİŞMİŞ WARPLAR ⭐ " + ChatColor.GOLD + "Sayfa: " + (page + 1) + "/" + totalPages;
        Inventory inv = Bukkit.createInventory(null, GUI_SIZE, title);
        
        int start = page * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, warpNames.size());
        
        for (int i = start; i < end; i++) {
            String warpName = warpNames.get(i);
            WarpLocation warp = warps.get(warpName);
            
            ItemStack item = new ItemStack(Material.ENDER_PEARL);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "✨ " + ChatColor.YELLOW + warpName.toUpperCase() + ChatColor.LIGHT_PURPLE + " ✨");
            
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "──────────────────");
            lore.add(ChatColor.AQUA + "🌍 Dünya: " + ChatColor.WHITE + warp.getLocation().getWorld().getName());
            lore.add(ChatColor.AQUA + "📍 X: " + ChatColor.WHITE + warp.getLocation().getBlockX());
            lore.add(ChatColor.AQUA + "📍 Y: " + ChatColor.WHITE + warp.getLocation().getBlockY());
            lore.add(ChatColor.AQUA + "📍 Z: " + ChatColor.WHITE + warp.getLocation().getBlockZ());
            lore.add(ChatColor.GRAY + "──────────────────");
            lore.add(ChatColor.GREEN + "🖱️ Tıkla ve Işınlan!");
            
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(i - start, item);
        }
        
        // Önceki sayfa butonu
        ItemStack prevItem = new ItemStack(Material.ARROW);
        ItemMeta prevMeta = prevItem.getItemMeta();
        prevMeta.setDisplayName(ChatColor.YELLOW + "◀ ÖNCEKİ SAYFA");
        prevItem.setItemMeta(prevMeta);
        inv.setItem(PREV_SLOT, prevItem);
        
        // Sonraki sayfa butonu
        ItemStack nextItem = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextItem.getItemMeta();
        nextMeta.setDisplayName(ChatColor.YELLOW + "SONRAKİ SAYFA ▶");
        nextItem.setItemMeta(nextMeta);
        inv.setItem(NEXT_SLOT, nextItem);
        
        // Kapat butonu
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RED + "❌ MENÜYÜ KAPAT");
        closeItem.setItemMeta(closeMeta);
        inv.setItem(CLOSE_SLOT, closeItem);
        
        p.openInventory(inv);
    }
}