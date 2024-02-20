package RcRPG.panel.ornament;

import RcRPG.Main;
import RcRPG.panel.lib.ChestFakeInventory;
import cn.nukkit.Player;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class OrnamentInventory extends ChestFakeInventory {

    public long id;

    public OrnamentInventory(InventoryHolder holder,String name) {
        super(InventoryType.CHEST, holder, name);
        this.setName(name);
    }

    @Override
    public void onOpen(Player who) {
        super.onOpen(who);
        ContainerOpenPacket pk = new ContainerOpenPacket();
        pk.windowId = who.getWindowId(this);
        pk.entityId = id;
        pk.type = InventoryType.CHEST.getNetworkType();
        who.dataPacket(pk);
    }

    @Override
    public void onClose(Player who) {
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = id;
        who.dataPacket(pk);
        super.onClose(who);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < Main.getInstance().config.getInt("饰品生效格数"); i++) {
            Item item = this.getItem(i);
            if (item.isNull()) {
                list.add(i, "");
            } else {
                list.add(i, item.getNamedTag().getString("name") + ":" + item.getCount());
            }
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).isEmpty()) {
                list.remove(i);
            } else {
                break; // 遇到第一个非空字符串，停止移除操作
            }
        }
        String name = who.getName();
        if(Main.getInstance().ornamentConfig.exists(name)){
            Main.getInstance().ornamentConfig.set(name,list);
        }else {
            Map<String,Object> map = Main.getInstance().ornamentConfig.getAll();
            map.put(name,list);
            Main.getInstance().ornamentConfig.setAll((LinkedHashMap<String, Object>) map);
        }
        Main.getInstance().ornamentConfig.save();
    }


}
