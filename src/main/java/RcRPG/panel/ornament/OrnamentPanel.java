package RcRPG.panel.ornament;

import RcRPG.Main;
import RcRPG.RPG.Ornament;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class OrnamentPanel implements InventoryHolder {

    public static Map<Integer, Item> getPanel(Player player) {
        Map<Integer, Item> panel = new LinkedHashMap<>();
        String name = player.getName();
        if (Main.getInstance().ornamentConfig.exists(name)) {
            ArrayList<String> list = (ArrayList<String>) Main.getInstance().ornamentConfig.getStringList(name);
            for (int i = 0; i < list.size(); i++) {
                String[] s = list.get(i).split(":");
                Item item = Ornament.getItem(s[0], Integer.parseInt(s[1])).clone();
                panel.put(i, item);
            }
        }
        return panel;
    }

    public void sendPanel(Player player) {
        Map<Integer, Item> panel = getPanel(player);
        displayPlayer(player, panel);
    }

    public void displayPlayer(Player player, Map<Integer, Item> itemMap) {
        OrnamentInventory inv = new OrnamentInventory(this, "饰品背包");
        inv.setContents(itemMap);
        inv.id = Entity.entityCount++;
        player.addWindow(inv);
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
