package RcRPG.RPG;

import RcRPG.Handle;
import RcRPG.Main;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AddEntityPacket;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Damage {

    public static LinkedHashMap<Player, LinkedList<Item>> playerMap = new LinkedHashMap<>();

    public static void getItem(Player player){
        Item item = player.getInventory().getItemInHand();
        LinkedList<Item> list = new LinkedList<>();
        if(!item.isNull() && Weapon.isWeapon(item)){
            list.add(item);
        }
        for(Item armour : player.getInventory().getArmorContents()){
            if(Armour.isArmour(armour)) list.add(armour);
        }
        Damage.playerMap.put(player,list);
    }

    /**
     * 效果处理
     * @param damager 攻击者
     * @param entity 受害者
     */
    public static void onDamage(Player damager, Entity entity){
        Item item = damager.getInventory().getItemInHand();
        if (!item.isNull() && Weapon.isWeapon(item)) {
            Weapon weapon = Main.loadWeapon.get(item.getNamedTag().getString("name"));
            if(entity instanceof Player) {// 当受害者是玩家时
                if (weapon.getFireRound() != 0) {// 火焰
                    if (Handle.random(1, 100) <= weapon.getFireRound()) {
                        entity.setOnFire(weapon.getFire());
                    }
                }
                if (weapon.getLightRound() != 0) {// 雷击
                    if (Handle.random(1, 100) <= weapon.getLightRound()) {
                        Damage.light(entity, weapon.getLighting());
                    }
                }
            }
            /**
            if(entity instanceof Player){// 当受害者是玩家时
                if(!weapon.getDamagedEffect().isEmpty()){// 受害者药水效果
                    for(Effect effect : weapon.getDamagedEffect()){
                        effect.add(entity);
                    }
                }
            }
            if(weapon.getGroupRound() != 0){// 群体回血
                if(Handle.random(1,100) <= weapon.getGroupRound()){
                    for(Player player:Damage.getPlayerAround(damager,5,true)){
                        if((player.getMaxHealth() - player.getHealth()) < weapon.getGroup()){
                            player.setHealth(player.getMaxHealth());
                        }else{
                            player.setHealth(player.getHealth()+weapon.getGroup());
                        }
                    }
                }
            }
            if(!weapon.getDamagerEffect().isEmpty()){// 攻击者药水效果
                for(Effect effect : weapon.getDamagerEffect()){
                    effect.add(damager);
                }
            }
            if(!weapon.getGroupEffect().isEmpty()){ // 群体药水效果
                for(Player player:Damage.getPlayerAround(damager,5,true)){
                    for(Effect effect :weapon.getGroupEffect()){
                        effect.add(player);
                    }
                }
            }*/
        }
    }

    /**
     * 闪电技能
     * @param entity
     * @param damage
     */
    public static void light(Entity entity, double damage){
        AddEntityPacket pk = new AddEntityPacket();

        pk.type = 93;

        pk.x = (float) entity.x;
        pk.y = (float) entity.y;
        pk.z = (float) entity.z;

        for(Player player : entity.level.getPlayers().values()){
            player.dataPacket(pk);
        }

        EntityDamageEvent ev = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.LIGHTNING, (float) damage);
        entity.attack(ev);
    }

    public static LinkedList<Player> getPlayerAround(Entity entity,double dis,boolean flag){
        LinkedList<Player> list = new LinkedList<>();
        for(Player player : entity.level.getPlayers().values()){
            if(entity.distance(player) <= dis) list.add(player);
        }
        if(flag) list.add((Player) entity);
        return list;
    }

    /**
     * 用于计算浮空字跳跃的向量
     * @param yaw
     * @param pitch
     * @param go
     * @return
     */
    public static Vector3 go(double yaw,double pitch,int go)
    {
        yaw = yaw % 360;
        double x = 0.0;
        double z = 0.0;
        if (0.0 <= yaw && yaw <= 90.0) { //第二象限
            x = -go * (yaw / 90.0);
            z = go * ((90.0 - yaw) / 90.0);
        } else if (90.0 < yaw && yaw <= 180.0) { //第三象限
            yaw = yaw % 90.0;
            x = -go * ((90.0 - yaw) / 90.0);
            z = -go * (yaw / 90.0);
        } else if (180.0 < yaw && yaw <= 270.0) { //第四象限
            yaw = yaw % 90.0;
            x = go * (yaw / 90.0);
            z = -go * ((90.0 - yaw) / 90.0);
        } else if (270.0 < yaw && yaw <= 360.0) { //第一象限
            yaw = yaw % 90.0;
            x = go * ((90.0 - yaw) / 90.0);
            z = go * (yaw / 90.0);
        }
        double y = 0.0;
        if (pitch < 0.0) { //向上
            pitch = -1.0 * pitch;
            y = go * (pitch / 90.0);
        }
        if (pitch > 0.0) { //向下
            pitch = -1.0 * pitch;
            y = go * (pitch / 90.0);
        }
        return new Vector3(x, y, z);
    }

}
