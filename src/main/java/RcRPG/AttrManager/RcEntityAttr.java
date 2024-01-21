package RcRPG.AttrManager;

import java.util.Map;

public class RcEntityAttr extends Manager {

    public RcEntityAttr(Map<String, float[]> attr) {
        this.mainAttr = attr;
    }

    private Map<String, float[]> mainAttr;

    public Map<String, float[]> getMainAttr() {
        return mainAttr;
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

    public float getItemAttr(String attrName) {
        Map<String, float[]> mainAttrMap = mainAttr;
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
        float[] data;
        if (mainAttr.containsKey(attrName)) {
            data = mainAttr.get(attrName);
        } else {
            data = new float[] {0, 0};
        }
        assert index == 0 || index == 1 : "Index should be 0 or 1";
        return data[index];
    }

    //激进向 (9)

    @Override
    public float[] getPvpAttackPower() {
        if (mainAttr.containsKey("攻击力")) {
            return mainAttr.get("攻击力");
        } else if (mainAttr.containsKey("PVP攻击力")) {
            return mainAttr.get("PVP攻击力");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getPveAttackPower() {
        if (mainAttr.containsKey("攻击力")) {
            return mainAttr.get("攻击力");
        } else if (mainAttr.containsKey("PVE攻击力")) {
            return mainAttr.get("PVE攻击力");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getPvpAttackMultiplier() {
        if (mainAttr.containsKey("攻击加成")) {
            return mainAttr.get("攻击加成");
        } else if (mainAttr.containsKey("PVP攻击加成")) {
            return mainAttr.get("PVP攻击加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getPveAttackMultiplier() {
        if (mainAttr.containsKey("攻击加成")) {
            return mainAttr.get("攻击加成");
        } else if (mainAttr.containsKey("PVE攻击加成")) {
            return mainAttr.get("PVE攻击加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getCritChance() {
        if (mainAttr.containsKey("暴击率")) {
            return mainAttr.get("暴击率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getCriticalStrikeMultiplier() {
        if (mainAttr.containsKey("暴击倍率")) {
            return mainAttr.get("暴击倍率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getLifestealChance() {
        if (mainAttr.containsKey("吸血率")) {
            return mainAttr.get("吸血率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getLifestealMultiplier() {
        if (mainAttr.containsKey("吸血倍率")) {
            return mainAttr.get("吸血倍率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getDefensePenetrationChance() {
        if (mainAttr.containsKey("破防率")) {
            return mainAttr.get("破防率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getDefensePenetrationValue() {
        if (mainAttr.containsKey("破防攻击")) {
            return mainAttr.get("破防攻击");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getArmorPenetrationChance() {
        if (mainAttr.containsKey("破甲率")) {
            return mainAttr.get("破甲率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getArmorPenetrationValue() {
        if (mainAttr.containsKey("破甲强度")) {
            return mainAttr.get("破甲强度");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getHitChance() {
        if (mainAttr.containsKey("命中率")) {
            return mainAttr.get("命中率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getDamageMultiplier() {
        if (mainAttr.containsKey("伤害加成")) {
            return mainAttr.get("伤害加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }

//保守向 (9)

    @Override
    public float[] getDodgeChance() {
        if (mainAttr.containsKey("闪避率")) {
            return mainAttr.get("闪避率");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getCritResistance() {
        if (mainAttr.containsKey("暴击抵抗")) {
            return mainAttr.get("暴击抵抗");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getLifestealResistance() {
        if (mainAttr.containsKey("吸血抵抗")) {
            return mainAttr.get("吸血抵抗");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getHp() {
        if (mainAttr.containsKey("血量值")) {
            return mainAttr.get("血量值");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getDefense() {
        if (mainAttr.containsKey("防御力")) {
            return mainAttr.get("防御力");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getMaxHpMultiplier() {
        if (mainAttr.containsKey("血量加成")) {
            return mainAttr.get("血量加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getDefenseMultiplier() {
        if (mainAttr.containsKey("防御加成")) {
            return mainAttr.get("防御加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getHpRegenMultiplier() {
        if (mainAttr.containsKey("生命加成")) {
            return mainAttr.get("生命加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getArmorStrengthMultiplier() {
        if (mainAttr.containsKey("护甲强度")) {
            return mainAttr.get("护甲强度");
        }
        return new float[]{ 0.0f, 0.0f };
    }

//辅助增益向 (3)

    @Override
    public float[] getExperienceGainMultiplier() {
        if (mainAttr.containsKey("经验加成")) {
            return mainAttr.get("经验加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getHpPerSecond() {
        if (mainAttr.containsKey("每秒恢复")) {
            return mainAttr.get("每秒恢复");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getHpPerNature() {
        if (mainAttr.containsKey("生命恢复")) {
            return mainAttr.get("生命恢复");
        }
        return new float[]{ 0.0f, 0.0f };
    }

    @Override
    public float[] getMovementSpeedMultiplier() {
        if (mainAttr.containsKey("移速加成")) {
            return mainAttr.get("移速加成");
        }
        return new float[]{ 0.0f, 0.0f };
    }

}