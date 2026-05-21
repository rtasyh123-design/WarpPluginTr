package tr.moon.warp;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WarpGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (e.getView().getTitle() == null) return;
        if (!e.getView().getTitle().contains("GELİŞMİŞ WARPLAR")) return;
        
        e.setCancelled(true);
        
        Player p = (Player) e.getWhoClicked();
        int slot = e.getRawSlot();
        ItemStack clicked = e.getCurrentItem();
        
        if (clicked == null || !clicked.hasItemMeta()) return;
        
        String displayName = clicked.getItemMeta().getDisplayName();
        
        // Önceki sayfa
        if (slot == 45) {
            int currentPage = WarpPlugin.getInstance().getPlayerPage(p);
            WarpGUI.open(p, currentPage - 1);
            return;
        }
        
        // Sonraki sayfa
        if (slot == 49) {
            int currentPage = WarpPlugin.getInstance().getPlayerPage(p);
            WarpGUI.open(p, currentPage + 1);
            return;
        }
        
        // Kapat
        if (slot == 53) {
            p.closeInventory();
            p.sendMessage(ChatColor.GRAY + "🔒 Menü kapatıldı!");
            return;
        }
        
        // Warp'a ışınlan
        if (displayName.contains("✨")) {
            String warpName = ChatColor.stripColor(displayName)
                .replace("✨", "")
                .trim()
                .toLowerCase();
            
            WarpPlugin.getInstance().teleportToWarp(warpName, p);
            p.closeInventory();
        }
    }
}