package RcRPG;

import RcRPG.RPG.Box;
import cn.nukkit.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class PlayerStatus {

    final int MAX_SIZE = 50;
    public static LinkedHashMap<Player,LinkedHashMap<Box, ArrayList<Integer>>> playerBox = new LinkedHashMap<>();

    public static int getSize(Player player){
        if(PlayerStatus.playerBox.containsKey(player)){
            int size = 0;
            if(PlayerStatus.playerBox.get(player).isEmpty()) return 50;
            for(Box box : PlayerStatus.playerBox.get(player).keySet()){
                if(PlayerStatus.playerBox.get(player).get(box).isEmpty()) continue;
                for(int time : PlayerStatus.playerBox.get(player).get(box)){
                    size += box.getSize();
                }
            }
            return 50 - size;
        }else{
            return 50;
        }
    }

}