package RcRPG.RPG;

import RcRPG.AttrManager.ItemAttr;
import RcRPG.RcRPGMain;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Suit {

    public static Map<String, SuitConfig> suitInfo = new HashMap<>();

    public static void init() {
        Config cfg = new Config(RcRPGMain.getInstance().getDataFolder() + "/SuitPlan.yml");
        cfg.getAll().keySet().forEach(suitName -> {
            RcRPGMain.getInstance().getLogger().info("suitName: "+suitName);
            suitInfo.put(suitName, new SuitConfig(cfg.getSection(suitName)));
        });
    }

    public static ItemAttr getSuitAttr(String name, int count) {
        if (!suitInfo.containsKey(name)) return null;
        SuitConfig suitCfg = suitInfo.get(name);
        ArrayList<Integer> compList = suitCfg.getCompList();
        for (int i = 0; i < compList.size(); i++) {
            if (count < compList.get(i)) continue;
            return suitCfg.getAttrList().get(i);
        }
        return null;
    }

    @Getter
    public static class SuitConfig {
        public ArrayList<Integer> compList = new ArrayList<>();
        public ArrayList<ItemAttr> attrList = new ArrayList<>();
        public SuitConfig(ConfigSection cfg) {
            cfg.forEach((quantity, effects) -> {
                compList.add(Integer.parseInt(quantity));
                ItemAttr attr = new ItemAttr();
                attr.setItemAttrConfig(effects);
                attrList.add(attr);
            });
        }
    }
}
