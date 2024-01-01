package RcRPG.RPG;

import RcRPG.AttrManager.ItemAttr;
import RcRPG.Handle;
import RcRPG.Main;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.RuntimeItems;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Config;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

@Getter
@Setter
public class Ornament extends ItemAttr {

    private Config config;

    private String name;

    private String label;

    private String showName;

    private Item item;

    private int level;

    private Object attr;

    private String tipText;

    private String myMessage;

    private String serverMessage;

    private String message;

    private ArrayList<String> loreList = new ArrayList<>();

    public Ornament(String name,Config config){
        this.name = name;
        this.config = config;
    }

    public static Ornament loadOrnament(String name, Config config){
        try{
            Ornament ornament = new Ornament(name, config);

            ornament.setLabel(config.getString("标签"));
            ornament.setShowName(config.getString("显示名称"));
            ornament.setItem(RuntimeItems.getRuntimeMapping().getItemByNamespaceId(config.getString("物品ID"),1));
            ornament.setLevel(config.getInt("最低使用等级"));

            if (config.exists("属性")) {
                ornament.setAttr(config.get("属性"));
            }

            ornament.setMessage(config.getString("介绍"));
            ornament.setShowName(config.getString("显示名称"));

            ArrayList<String> list = new ArrayList<>();
            for(String lore : config.getStringList("显示")){
                list.add(lore);
            }
            ornament.setLoreList(list);

            ornament.setTipText(config.getString("底部显示"));
            ornament.setMyMessage(config.getString("个人通知"));
            ornament.setServerMessage(config.getString("全服通知"));
            return ornament;
        }catch(Exception e){
            Main.instance.getLogger().error("加载饰品"+name+"配置文件失败");
            return null;
        }
    }

    public static Config getOrnamentConfig(String name){
        File file = new File(Main.instance.getDataFolder()+"/Ornament/"+name+".yml");
        Config config;
        if(file.exists()){
            config = new Config(file,Config.YAML);
        }else{
            return null;
        }
        return config;
    }

    public static Config addOrnamentConfig(String name,String id){
        if(getOrnamentConfig(name) == null){
            Main.instance.saveResource("Ornament.yml","/Ornament/"+name+".yml",false);
            Config config = new Config(Main.instance.getOrnamentFile()+"/"+name+".yml");
            config.set("物品ID",id);
            config.save();
            return config;
        }
        return null;
    }

    public static boolean delOrnamentConfig(String name){
        if(getOrnamentConfig(name) != null){
            File file = new File(Main.instance.getOrnamentFile(),"/"+name+".yml");
            file.delete();
            return true;
        }
        return false;
    }

    public static Item getItem(String name,int count) {
        Ornament ornament = Main.loadOrnament.get(name);
        Item item = ornament.getItem();
        item.setCount(count);
        CompoundTag tag = item.getNamedTag();
        if(tag == null){
            tag = new CompoundTag();
        }
        tag.putString("type","ornament");
        tag.putString("name",name);
        tag.putByte("Unbreakable",1);

        item.setNamedTag(tag);
        item.setCustomName(ornament.getShowName());
        Ornament.setOrnamentLore(item);
        return item;
    }

    public static boolean giveOrnament(Player player, String name, int count){
        if(!Main.loadOrnament.containsKey(name)) {
            return false;
        }
        Ornament ornament = Main.loadOrnament.get(name);
        player.getInventory().addItem(getItem(name, count));
        if(!ornament.getMyMessage().equals("")){
            String text = ornament.getMyMessage();
            if(text.contains("@player")) text = text.replace("@player", player.getName());
            if(text.contains("@item")) text = text.replace("@item", ornament.getLabel());
            player.sendMessage(text);
        }
        if(!ornament.getServerMessage().equals("")){
            String text = ornament.getServerMessage();
            if(text.contains("@player")) text = text.replace("@player", player.getName());
            if(text.contains("@item")) text = text.replace("@item", ornament.getLabel());
            Main.instance.getServer().broadcastMessage(text);
        }
        return true;
    }

    public static boolean isOrnament(Item item){
        if(item.getNamedTag() == null) {
            return false;
        }
        if(!item.getNamedTag().contains("type")){
            return false;
        }
        return item.getNamedTag().getString("type").equals("ornament");
    }

    public static Item setOrnamentLore(Item item){
        if(Ornament.isOrnament(item)){
            Ornament ornament = Main.loadOrnament.get(item.getNamedTag().getString("name"));
            ArrayList<String> lore;
            lore = (ArrayList<String>) ornament.getLoreList().clone();
            for (int i = 0;i < lore.size();i++) {
                String s = lore.get(i);
                if(s.contains("@message")) s = s.replace("@message",ornament.getMessage());
                s = ornament.replaceAttrTemplate(s);// 替换属性的值
                lore.set(i, s);
            }
            item.setLore(lore.toArray(new String[0]));
        }
        return item;
    }

    /**
     * 仅作为属性分类的标识
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * 物品名，替代源label用法
     * @return
     */
    public String getShowName() {
        return showName;
    }

    public Object getAttr() {
        return attr;
    }

    public void setAttr(Object attr) {
        this.attr = attr;
        setItemAttrConfig(attr);
    }

}
