package RcRPG.AttrManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttrManager {

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
            }
        }
        if (!myAttr.containsKey("Main")) {
            Map<String, float[]> mainAttrMap_ = new HashMap<>();
            myAttr.put("Main", mainAttrMap_);
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

    /**
     * 获取指定属性值（随机后）
     * @param attrName 属性名
     * @return
     */
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
        if (index == -1) {
            return getItemAttr(attrName);
        }
        Map<String, float[]> mainAttrMap = myAttr.get("Main");
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
    public Map<String, float[]> getItemAttrMap(String type) {
        Map<String, float[]> data;
        data = myAttr.getOrDefault(type, null);
        return data;
    }

    /**
     * 返回 [最小值, 最大值] 的随机值
     * @param array
     * @return
     */
    public static float getRandomNum(float[] array) {
        int length = 0;
        if (array.length == 1 || array[0] == array[1]) {
            return array[0];
        }
        for (float v : array) {
            String last = String.valueOf(v).split("\\.")[1];
            if (last != null && length < last.length()) {
                length = last.length();
            }
        }
        length = (int) Math.pow(10, length + 2);
        float minNum = array[0] * length;
        float maxNum = array[1] * length;
        return (float) (Math.random() * (maxNum - minNum + 1) + minNum) / length;
    }

    //激进向 (9)

    public float[] getPvpAttackPower() {
        if (myAttr.containsKey("PVP攻击力")) {
            return myAttr.get("Main").get("PVP攻击力");
        }
        return new float[0];
    }

    public float[] getPveAttackPower() {
        if (myAttr.containsKey("PVE攻击力")) {
            return myAttr.get("Main").get("PVE攻击力");
        }
        return new float[0];
    }

    public float[] getCritChance() {
        if (myAttr.containsKey("暴击率")) {
            return myAttr.get("Main").get("暴击率");
        }
        return new float[0];
    }

    /**
     * 暴击倍率
     * @return
     */
    public float[] getCriticalStrikeMultiplier() {
        if (myAttr.containsKey("暴击倍率")) {
            return getItemAttrMap().get("暴击倍率");
        }
        return new float[0];
    }

    /**
     * 吸血倍率
     * @return
     */
    public float[] getLifestealMultiplier() {
        if (myAttr.containsKey("吸血倍率")) {
            return getItemAttrMap().get("吸血倍率");
        }
        return new float[0];
    }

    /**
     * 破防率
     * @return
     */
    public float[] getArmorPenetrationChance() {
        if (myAttr.containsKey("破防率")) {
            return getItemAttrMap().get("破防率");
        }
        return new float[0];
    }

    public float[] getArmorPenetrationValue() {
        if (myAttr.containsKey("破防攻击")) {
            return getItemAttrMap().get("破防攻击");
        }
        return new float[0];
    }

    public float[] getMagicResistancePenetrationChance() {
        if (myAttr.containsKey("破甲率")) {
            return getItemAttrMap().get("破甲率");
        }
        return new float[0];
    }

    public float[] getMagicResistancePenetrationValue() {
        if (myAttr.containsKey("破甲攻击")) {
            return getItemAttrMap().get("破甲攻击");
        }
        return new float[0];
    }

    public float[] getHitChance() {
        if (myAttr.containsKey("命中率")) {
            return getItemAttrMap().get("命中率");
        }
        return new float[0];
    }

    public float[] getDamageMultiplier() {
        if (myAttr.containsKey("伤害加成")) {
            return getItemAttrMap().get("伤害加成");
        }
        return new float[0];
    }

//保守向 (9)

    public float[] getDodgeChance() {
        if (myAttr.containsKey("闪避率")) {
            return getItemAttrMap().get("闪避率");
        }
        return new float[0];
    }

    public float[] getCritResistance() {
        if (myAttr.containsKey("暴击抵抗")) {
            return getItemAttrMap().get("暴击抵抗");
        }
        return new float[0];
    }

    public float[] getLifestealResistance() {
        if (myAttr.containsKey("吸血抵抗")) {
            return getItemAttrMap().get("吸血抵抗");
        }
        return new float[0];
    }

    public float[] getHp() {
        if (myAttr.containsKey("血量值")) {
            return getItemAttrMap().get("血量值");
        }
        return new float[0];
    }

    public float[] getDefense() {
        if (myAttr.containsKey("防御力")) {
            return getItemAttrMap().get("防御力");
        }
        return new float[0];
    }

    public float[] getMaxHpMultiplier() {
        if (myAttr.containsKey("血量加成")) {
            return getItemAttrMap().get("血量加成");
        }
        return new float[0];
    }

    public float[] getDefenseMultiplier() {
        if (myAttr.containsKey("防御加成")) {
            return getItemAttrMap().get("防御加成");
        }
        return new float[0];
    }

    public float[] getHpRegenMultiplier() {
        if (myAttr.containsKey("生命加成")) {
            return getItemAttrMap().get("生命加成");
        }
        return new float[0];
    }

    public float[] getArmorStrengthMultiplier() {
        if (myAttr.containsKey("护甲强度")) {
            return getItemAttrMap().get("护甲强度");
        }
        return new float[0];
    }

//辅助增益向 (3)

    public float[] getExperienceGainMultiplier() {
        if (myAttr.containsKey("经验加成")) {
            return getItemAttrMap().get("经验加成");
        }
        return new float[0];
    }

    public float[] getHpPerSecond() {
        if (myAttr.containsKey("每秒恢复")) {
            return getItemAttrMap().get("每秒恢复");
        }
        return new float[0];
    }

    public float[] getMovementSpeedMultiplier() {
        if (myAttr.containsKey("移速加成")) {
            return getItemAttrMap().get("移速加成");
        }
        return new float[0];
    }

}
