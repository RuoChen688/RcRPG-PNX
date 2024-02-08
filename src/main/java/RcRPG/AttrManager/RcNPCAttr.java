package RcRPG.AttrManager;

import RcNPC.Config.RcNPCConfig;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class RcNPCAttr extends Manager {

    public RcNPCAttr(RcNPCConfig cfg) {
        mainAttr.put("血量值", new float[]{ cfg.getHealth(), cfg.getHealth() });
        mainAttr.put("攻击力", new float[]{ cfg.getDamage(), cfg.getDamage() });
    }

    @Getter
    private Map<String, float[]> mainAttr = new HashMap<>();

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
        return mainAttr.get("攻击力");
    }

    @Override
    public float[] getPveAttackPower() {
        return mainAttr.get("攻击力");
    }

    @Override
    public float[] getHp() {
        if (mainAttr.containsKey("血量值")) {
            return mainAttr.get("血量值");
        }
        return new float[]{ 0.0f, 0.0f };
    }


}