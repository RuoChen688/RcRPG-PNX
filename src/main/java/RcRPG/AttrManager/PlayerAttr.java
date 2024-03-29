package RcRPG.AttrManager;

import RcRPG.Main;
import RcRPG.RPG.*;
import RcRPG.panel.ornament.OrnamentPanel;
import cn.nukkit.Player;
import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.item.Item;

import java.text.DecimalFormat;
import java.util.*;

public class PlayerAttr extends Manager {

    private final Player player;
    private ArrayList<String> labelList = new ArrayList<>();
    public PlayerAttr(Player player) {
        this.player = player;
        myAttr.put("Main", new HashMap<>());
    }
    public static LinkedHashMap<Player, PlayerAttr> playerlist = new LinkedHashMap<>();

    public static PlayerAttr getPlayerAttr(Player player) {
        if (!playerlist.containsKey(player)) {
            return null;
        }
        return playerlist.get(player);
    }
    public static void setPlayerAttr(Player player) {
        playerlist.put(player, new PlayerAttr(player));
    }

    public void update() {
        // 等级加点
        if (Main.instance.config.exists("等级增加血量")) {
            String[] s = Main.instance.config.getString("等级增加血量").split(":");
            int lvAddHealth;
            if (Level.enable) {
                lvAddHealth = Level.getLevel(player) / Integer.parseInt(s[0]) * Integer.parseInt(s[1]);
            } else {
                lvAddHealth = player.getExperienceLevel() / Integer.parseInt(s[0]) * Integer.parseInt(s[1]);
            }
            if (lvAddHealth > 0) {
                PlayerAttr pAttr = PlayerAttr.getPlayerAttr(player);
                Map<String, float[]> attr = new HashMap<>();
                attr.put("血量值", new float[]{lvAddHealth, lvAddHealth});
                if (pAttr != null) {
                    pAttr.setItemAttrConfig(Main.getI18n().tr(player.getLanguageCode(), "rcrpg.playerattr.lv"), attr);
                }
            }
        }

        ArrayList<String> beforeLabel = new ArrayList<>(labelList);
        labelList.clear();

        Map<String, Integer> suitMap = new HashMap<>();// _声明套装Map

        ArrayList<Item> itemList = new ArrayList<>();
        // 主手
        itemList.add(player.getInventory().getItemInHand());
        // 副手
        itemList.add(player.getOffhandInventory().getItem(0));
        for (Item rcItem : itemList) {
            if (rcItem.getNamedTag() == null) continue;
            Weapon weapon = Main.loadWeapon.get(rcItem.getNamedTag().getString("name"));
            if (weapon == null) continue;

            if (!weapon.getSuit().isEmpty()) {
                int count = suitMap.getOrDefault(weapon.getSuit(), 0) + 1;
                suitMap.put(weapon.getSuit(), count);
            }

            setItemAttrConfig(weapon.getLabel(), weapon.getMainAttr());
            checkItemStoneAttr(weapon.getLabel(), Weapon.getStones(rcItem), beforeLabel, labelList);

            beforeLabel.remove(weapon.getLabel());
            labelList.add(weapon.getLabel());
        }
        // 护甲栏
        for (Item rcItem : player.getInventory().getArmorContents()) {
            if (rcItem.getNamedTag() == null) continue;
            Armour armour = Main.loadArmour.get(rcItem.getNamedTag().getString("name"));
            if (armour == null) continue;
            setItemAttrConfig(armour.getLabel(), armour.getMainAttr());
            checkItemStoneAttr(armour.getLabel(), Armour.getStones(rcItem), beforeLabel, labelList);

            if (!armour.getSuit().isEmpty()) {
                int count = suitMap.getOrDefault(armour.getSuit(), 0) + 1;
                suitMap.put(armour.getSuit(), count);
            }

            beforeLabel.remove(armour.getLabel());
            labelList.add(armour.getLabel());
        }
        // 饰品
        Map<Integer,Item> map = OrnamentPanel.getPanel(player);
        if (!map.isEmpty()) {
            Map<String, float[]> attr = new HashMap<>();
            for(int i = 0; i < Math.min(Main.getInstance().config.getInt("饰品生效格数"), map.size()); i++){
                if (!map.get(i).hasCompoundTag()) continue;
                Ornament ornament = Main.loadOrnament.get(map.get(i).getNamedTag().getString("name"));
                if (ornament == null) continue;
                if (!ornament.isValidSlot(i)) continue;
                OverAttr(attr,ornament.getMainAttr());
                setItemAttrConfig(ornament.getLabel(), attr);

                if (!ornament.getSuit().isEmpty()) {
                    int count = suitMap.getOrDefault(ornament.getSuit(), 0) + 1;
                    suitMap.put(ornament.getSuit(), count);
                }

                beforeLabel.remove(ornament.getLabel());
                labelList.add(ornament.getLabel());
            }
        }

        // 套装
        suitMap.keySet().forEach(name -> {
            int count = suitMap.get(name);
            ItemAttr suitAttr = Suit.getSuitAttr(name, count);
            if (suitAttr == null) return;
            String label = Main.getI18n().tr(player.getLanguageCode(), "rcrpg.playerattr.suit.label", name, count);
            setItemAttrConfig(label, suitAttr.getMainAttr());
            if (!beforeLabel.contains(label)) {
                player.sendActionBar(Main.getI18n().tr(player.getLanguageCode(), "rcrpg.playerattr.set_suit_message", name, count));
            }
            beforeLabel.remove(label);
            labelList.add(label);
        });

        beforeLabel.forEach(label -> {
            setItemAttrConfig(label, new HashMap<String, float[]>());
        });
    }

    public void OverAttr(Map<String, float[]> map1,Map<String, float[]> map2){
        for(Map.Entry<String,float[]> entry : map2.entrySet()){
            String key = entry.getKey();
            float[] value = entry.getValue();
            if(value.length == 1){
                value = new float[]{value[0],value[0]};
            }
            if(!map1.containsKey(key)){
                map1.put(key,value);
            }
            else{
                map1.put(key,new float[]{map1.get(key)[0] + value[0],map1.get(key)[1] + value[1]});
            }
        }
    }

    public void checkItemStoneAttr(String mainItemName, LinkedList<Stone> list, ArrayList<String> beforLabel, ArrayList<String> labelList) {
        LinkedHashMap<String,Map<String,float[]>> map = new LinkedHashMap<>();
        Map<String, float[]> attr = new HashMap<>();
        for(Stone stone : list){
            if(stone == null) continue;
            if(!map.containsKey(stone.getLabel())){
                attr.clear();
                OverAttr(attr,stone.getMainAttr());
                setItemAttrConfig(mainItemName + " -> " + stone.getLabel(), attr);
                map.put(stone.getLabel(),attr);
            }else{
                OverAttr(map.get(stone.getLabel()),stone.getMainAttr());
                setItemAttrConfig(mainItemName + " -> " + stone.getLabel(), map.get(stone.getLabel()));
            }
            beforLabel.remove(mainItemName + " -> " + stone.getLabel());
            labelList.add(mainItemName + " -> " + stone.getLabel());
        }
    }

    /** 属性结构
     * {
     *     "Main": {
     *         "攻击力": [1,3]
     *     }
     * }
     * */
    public Map<String, Map<String, float[]>> myAttr = new HashMap<>();
    public void setItemAttrConfig(String id, Object newAttr) {
        Map<String, float[]> attrMap = new HashMap<>();
        Map<String, Object> attr = (Map<String, Object>) newAttr;
        for (Map.Entry<String, Object> entry : attr.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof List) {
                List<?> values = (List<?>) value;
                float[] floatValues = new float[values.size()];
                for (int i = 0; i < values.size(); i++) {
                    if (values.get(i) instanceof Double) {
                        floatValues[i] = ((Double) values.get(i)).floatValue();
                    } else if (values.get(i) instanceof Integer) {
                        floatValues[i] = ((Integer) values.get(i)).floatValue();
                    }
                }
                attrMap.put(key, floatValues);
            } else if (value instanceof float[]){
                float[] floatValue = (float[]) value;
                if (floatValue.length == 1) {
                    float[] newValue = { floatValue[0], floatValue[0] };
                    attrMap.put(key, newValue);
                } else {
                    attrMap.put(key, floatValue);
                }
            } else {
                Main.instance.getLogger().warning(key + "不知道是啥类型");
            }
        }

        Map<String, float[]> mainAttrMap = myAttr.get("Main");
        Map<String, float[]> oldAttrMap = deepCopyMap(myAttr.get(id));
        myAttr.put(id, attrMap);
        // 处理newAttr属性
        for (Map.Entry<String, float[]> entry : attrMap.entrySet()) {
            String key = entry.getKey();
            float[] mainValues = new float[]{ 0.0f, 0.0f };
            if (mainAttrMap.containsKey(key)) {
                mainValues = mainAttrMap.get(key);
            }
            float[] values = attrMap.get(key);
            mainValues[0] = mainValues[0] - (oldAttrMap.containsKey(key) ? oldAttrMap.get(key)[0] : 0) + values[0];
            mainValues[1] = mainValues[1] - (oldAttrMap.containsKey(key) ? oldAttrMap.get(key)[1] : 0) + values[1];

            mainAttrMap.put(key, mainValues);
        }

        // 副作用回收
        // 处理oldAttr有但是newAttr没有的属性
        for (Map.Entry<String, float[]> entry : oldAttrMap.entrySet()) {
            String key = entry.getKey();
            if (!attrMap.containsKey(key)) {
                float[] mainValues = mainAttrMap.get(key);
                float[] values = oldAttrMap.get(key);
                mainValues[0] = mainValues[0] - values[0];
                mainValues[1] = mainValues[1] - values[1];
                mainAttrMap.put(key, mainValues);
            }
        }
    }
    public void setItemAttr(String id, Object newAttr) {
        Map<String, float[]> attrMap = new HashMap<>();
        Map<String, Object> attr = (Map<String, Object>) newAttr;
        for (Map.Entry<String, Object> entry : attr.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof List) {
                List<Float> values = (List<Float>) value;
                float[] floatValues = new float[values.size()];
                for (int i = 0; i < values.size(); i++) {
                    floatValues[i] = values.get(i);
                }
                attrMap.put(key, floatValues);
            } else if (value instanceof float[]){
                attrMap.put(key, (float[]) value);
            } else {
                Main.instance.getLogger().warning(key + "不知道是啥类型");
            }
        }
        Map<String, float[]> mainAttrMap = myAttr.get("Main");
        for (Map.Entry<String, float[]> entry : mainAttrMap.entrySet()) {
            String key = entry.getKey();
            if (attrMap.containsKey(key)) {
                float[] mainValues = mainAttrMap.get(key);
                float[] values = attrMap.get(key);
                mainValues[0] -= values[0];
                mainValues[1] -= values[1];
                mainAttrMap.put(key, mainValues);
            } else {
                mainAttrMap.remove(key);
            }
        }
        myAttr.put(id, attrMap);
    }

    public float getItemAttr(String attrName) {
        Map<String, float[]> mainAttrMap = myAttr.get("Main");
        float[] data;
        if (mainAttrMap.containsKey(attrName)) {
            data = mainAttrMap.get(attrName);
        } else {
            data = new float[] {0, 0};
        }
        return getRandomNum(data);
    }

    /**
     * 获取指定属性的原始值
     * @param attrName 属性名
     * @param index 索引，0为min，1为max。内部可能传入-1
     * @return
     */
    public float getItemAttr(String attrName, int index) {
        return getItemAttr("Main", attrName, index);
    }

    public void showAttrWindow(Player p) {
        PlayerAttr pAttr = PlayerAttr.getPlayerAttr(p);

        if (pAttr == null) {
            player.sendMessage("[NWeapon] 没有玩家§8" + p.getName() + "§f的数据");
            return;
        }
        String str = "";
        Map<String, Map<String, float[]>> data = pAttr.myAttr;

        ArrayList<Element> list = new ArrayList<>();

        for (String i : data.get("Main").keySet()) {
            float[] value = data.get("Main").get(i);
            String valueString = valueToString(value, i);

            if (valueString.equals("0")) {
                continue;
            }

            str += " " + i + ": " + valueString + "\n";
        }

        list.add(new ElementLabel("§l§a### 总属性§r\n" + str));

        for (String i : data.keySet()) {
            if (i.equals("Effect") || i.equals("EffectSuit") || i.equals("Main")) {
                continue;
            }

            str = "";

            for (String n : data.get(i).keySet()) {
                float[] value = data.get(i).get(n);
                String valueString = valueToString(value, n);

                if (valueString.equals("0")) {
                    continue;
                }

                str += "  " + n + ": " + valueString + "\n";
            }

            if (!str.equals("")) {// 如果没有属性就不显示了
                list.add(new ElementLabel(" §a# " + i + "§r\n" + str));
            }
        }

        /**
        str = "";
        long nowTime = (System.currentTimeMillis() / 1000);
        for (String i : data.get("Effect").keySet()) {
            float[] effectData = data.get("Effect").get(i);
            long time = (long) effectData[0];
            int level = (int) effectData[1];
            str += "  " + i + " (" + (time - nowTime) + "s): " + level + "\n";
        }

        if (!str.equals("")) {
            list.add(new ElementLabel(" §a# 临时效果§r\n" + str));
        }
        */
        FormWindowCustom win = new FormWindowCustom("玩家属性 - " + p.getName(), list);
        player.showFormWindow(win);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("属性结构:\n");
        result.append("{\n");

        for (Map.Entry<String, Map<String, float[]>> entry : myAttr.entrySet()) {
            String outerKey = entry.getKey();
            Map<String, float[]> innerMapValue = entry.getValue();

            result.append("    \"" + outerKey + "\": {\n");

            for (Map.Entry<String, float[]> innerEntry : innerMapValue.entrySet()) {
                String innerKey = innerEntry.getKey();
                float[] innerArray = innerEntry.getValue();

                result.append("        \"" + innerKey + "\": " + "[" + innerArray[0] + ", " + innerArray[1] + "]\n");
            }

            result.append("    }\n");
        }
        result.append("}\n");

        return result.toString();
    }
    /**
     * 将数据可视化，输入data,属性输出min-max或x%
      */

    public static String valueToString(float[] data, String attribute) {
        List<String> attrDisplayPercent = Main.instance.attrDisplayPercentConfig;
        String back = "";
        if (data.length == 2 && data[0] == data[1]) {
            data = new float[]{data[0]};
        } else if (data.length == 1) {
            data = new float[]{data[0]};
        } else {
            return data[0] + " - " + data[1];
        }
        if (attrDisplayPercent.contains(attribute)) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            float formattedData = data[0] * 100;
            if (decimalFormat.format(formattedData).endsWith(".00")) {
                data = new float[]{(int) formattedData};
            } else {
                data = new float[]{formattedData};
            }
            back = data[0] + "%%";
        } else {
            back = Float.toString(data[0]);
        }
        if (data[0] == 0) {
            back = "0";
        }
        return back;
    }

    /**
     * 获取指定属性的原始值
     * @param label 标签名
     * @param attrName 属性名
     * @param index 索引，0为min，1为max。内部可能传入-1
     * @return
     */
    public float getItemAttr(String label, String attrName, int index) {
        if (index == -1) {
            return getItemAttr(attrName);
        }
        Map<String, float[]> mainAttrMap = myAttr.get(label);
        float[] data;
        if (mainAttrMap.containsKey(attrName)) {
            data = mainAttrMap.get(attrName);
        } else {
            data = new float[] {0, 0};
        }
        assert index == 0 || index == 1 : "Index should be 0 or 1";
        return data[index];
    }

    public Map<String, float[]> getItemAttrMap() {
        return getItemAttrMap("Main");
    }
    public Map<String, float[]> getItemAttrMap(String label) {
        Map<String, float[]> data;
        data = myAttr.getOrDefault(label, null);
        return data;
    }

    //激进向 (9)
    @Override
    public float[] getPvpAttackPower() {
        if (getItemAttrMap().containsKey("PVP攻击力")) {
            return getItemAttrMap().get("PVP攻击力");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getPveAttackPower() {
        if (getItemAttrMap().containsKey("PVE攻击力")) {
            return getItemAttrMap().get("PVE攻击力");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getPvpAttackMultiplier() {
        if (getItemAttrMap().containsKey("PVP攻击加成")) {
            return getItemAttrMap().get("PVP攻击加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getPveAttackMultiplier() {
        if (getItemAttrMap().containsKey("PVE攻击加成")) {
            return getItemAttrMap().get("PVE攻击加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getCritChance() {
        if (getItemAttrMap().containsKey("暴击率")) {
            return getItemAttrMap().get("暴击率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getCriticalStrikeMultiplier() {
        if (getItemAttrMap().containsKey("暴击倍率")) {
            return getItemAttrMap().get("暴击倍率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getLifestealChance() {
        if (getItemAttrMap().containsKey("吸血率")) {
            return getItemAttrMap().get("吸血率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getLifestealMultiplier() {
        if (getItemAttrMap().containsKey("吸血倍率")) {
            return getItemAttrMap().get("吸血倍率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getDefensePenetrationChance() {
        if (getItemAttrMap().containsKey("破防率")) {
            return getItemAttrMap().get("破防率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getDefensePenetrationValue() {
        if (getItemAttrMap().containsKey("破防攻击")) {
            return getItemAttrMap().get("破防攻击");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getArmorPenetrationChance() {
        if (getItemAttrMap().containsKey("破甲率")) {
            return getItemAttrMap().get("破甲率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getArmorPenetrationValue() {
        if (getItemAttrMap().containsKey("破甲强度")) {
            return getItemAttrMap().get("破甲强度");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getHitChance() {
        if (getItemAttrMap().containsKey("命中率")) {
            return getItemAttrMap().get("命中率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getDamageMultiplier() {
        if (getItemAttrMap().containsKey("伤害加成")) {
            return getItemAttrMap().get("伤害加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    //保守向 (9)
    @Override
    public float[] getDodgeChance() {
        if (getItemAttrMap().containsKey("闪避率")) {
            return getItemAttrMap().get("闪避率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getCritResistance() {
        if (getItemAttrMap().containsKey("暴击抵抗")) {
            return getItemAttrMap().get("暴击抵抗");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getLifestealResistance() {
        if (getItemAttrMap().containsKey("吸血抵抗")) {
            return getItemAttrMap().get("吸血抵抗");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getHp() {
        if (getItemAttrMap().containsKey("血量值")) {
            return getItemAttrMap().get("血量值");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getDefense() {
        if (getItemAttrMap().containsKey("防御力")) {
            return getItemAttrMap().get("防御力");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getMaxHpMultiplier() {
        if (getItemAttrMap().containsKey("血量加成")) {
            return getItemAttrMap().get("血量加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getDefenseMultiplier() {
        if (getItemAttrMap().containsKey("防御加成")) {
            return getItemAttrMap().get("防御加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getHpRegenMultiplier() {
        if (getItemAttrMap().containsKey("生命加成")) {
            return getItemAttrMap().get("生命加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getArmorStrengthMultiplier() {
        if (getItemAttrMap().containsKey("护甲强度")) {
            return getItemAttrMap().get("护甲强度");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    //辅助增益向 (3)
    @Override
    public float[] getExperienceGainMultiplier() {
        if (getItemAttrMap().containsKey("经验加成")) {
            return getItemAttrMap().get("经验加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getHpPerSecond() {
        if (getItemAttrMap().containsKey("每秒恢复")) {
            return getItemAttrMap().get("每秒恢复");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getHpPerNature() {
        if (getItemAttrMap().containsKey("生命恢复")) {
            return getItemAttrMap().get("生命恢复");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getMovementSpeedMultiplier() {
        if (getItemAttrMap().containsKey("移速加成")) {
            return getItemAttrMap().get("移速加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }


}
