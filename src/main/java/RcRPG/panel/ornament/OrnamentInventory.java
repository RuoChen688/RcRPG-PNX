package RcRPG.panel.ornament;

import RcRPG.Main;
import RcRPG.panel.lib.ChestFakeInventory;
import cn.nukkit.Player;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;

import java.util.LinkedHashMap;
import java.util.List;
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
        Map<Integer, Item> content = this.getContents();
        List<String> list = content.values().stream().map(item -> item.getNamedTag().getString("name") + ":" + item.getCount()).toList();
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
