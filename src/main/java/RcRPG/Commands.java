package RcRPG;

import RcRPG.AttrManager.PlayerAttr;
import RcRPG.Form.guildForm;
import RcRPG.Form.inlayForm;
import RcRPG.Form.prefixForm;
import RcRPG.RPG.*;
import RcRPG.Society.Money;
import RcRPG.Society.Points;
import RcRPG.Society.Prefix;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.tree.node.PlayersNode;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Commands extends PluginCommand<Main> {

    public Commands(String cmdName) {
        super(cmdName, "RPG指令", Main.getInstance());
        this.setPermission("plugin.rcrpg");
        this.commandParameters.clear();
        ArrayList<String> society = new ArrayList<>(){{
            add("money");
            add("point");
        }};
        ArrayList<String> item = new ArrayList<>(){{
            add("weapon");
            add("armour");
            add("magic");
            add("stone");
            add("box");
        }};
        String[] list = new String[]{"weapon","armour","magic","stone","box","guild","prefix","money","point","exp","shop"};
        this.addCommandParameters("help",new CommandParameter[]{
                CommandParameter.newEnum("help",new String[]{"help"})
        });
        this.addCommandParameters("check",new CommandParameter[]{
                CommandParameter.newEnum("check", new String[]{"check"}),
                CommandParameter.newEnum("type",new String[]{"attr"}),
                CommandParameter.newType("player", CommandParamType.TARGET, new PlayersNode()),
        });
        this.addCommandParameters("guild",new CommandParameter[]{
                CommandParameter.newEnum("guild",new String[]{"guild"})
        });
        this.addCommandParameters("exp",new CommandParameter[]{
                CommandParameter.newEnum("exp",new String[]{"exp"}),
                CommandParameter.newEnum("give",new String[]{"give"}),
                CommandParameter.newType("PlayerName",CommandParamType.STRING),
                CommandParameter.newType("Exp",CommandParamType.INT)
        });
        this.addCommandParameters("shop",new CommandParameter[]{
                CommandParameter.newEnum("shop",new String[]{"shop"}),
                CommandParameter.newType("ShopName",CommandParamType.STRING)
        });
        for (String name : list) {
            if(item.contains(name)){
                if(name.equals("weapon") || name.equals("armour")){
                    this.addCommandParameters(name + "_inlay",new CommandParameter[]{
                            CommandParameter.newEnum(name,new String[]{name}),
                            CommandParameter.newEnum("inlay",new String[]{"inlay"})
                    });
                }
                this.addCommandParameters(name + "_add",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("add",new String[]{"add"}),
                        CommandParameter.newType(StringUtils.capitalize(name),CommandParamType.STRING)
                });
                this.addCommandParameters(name + "_del",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("del",new String[]{"del"}),
                        CommandParameter.newType(StringUtils.capitalize(name),CommandParamType.STRING)
                });
                this.addCommandParameters(name + "_help",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("help",new String[]{"help"})
                });
                this.addCommandParameters(name + "_give",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("give",new String[]{"give"}),
                        CommandParameter.newType("PlayerName",CommandParamType.STRING),
                        CommandParameter.newType(StringUtils.capitalize(name),CommandParamType.STRING),
                        CommandParameter.newType("Count",CommandParamType.INT)
                });
            }else if(name.equals("prefix")){
                this.addCommandParameters(name + "_give",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("give",new String[]{"give"}),
                        CommandParameter.newType("player", CommandParamType.TARGET, new PlayersNode()),
                        CommandParameter.newType(StringUtils.capitalize(name),CommandParamType.STRING)
                });
                this.addCommandParameters(name + "_remove",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("remove",new String[]{"remove"}),
                        CommandParameter.newType("player", CommandParamType.TARGET, new PlayersNode()),
                        CommandParameter.newType(StringUtils.capitalize(name),CommandParamType.STRING)
                });
                this.addCommandParameters(name + "_help",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("help",new String[]{"help"})
                });
                this.addCommandParameters(name + "_my",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("my",new String[]{"my"})
                });
            } else if(society.contains(name)) {
                this.addCommandParameters(name + "_add",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("add",new String[]{"add"}),
                        CommandParameter.newType("player", CommandParamType.TARGET, new PlayersNode()),
                        CommandParameter.newType(StringUtils.capitalize(name),CommandParamType.INT)
                });
                this.addCommandParameters(name + "_del",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("del",new String[]{"del"}),
                        CommandParameter.newType("PlayerName",CommandParamType.STRING),
                        CommandParameter.newType(StringUtils.capitalize(name),CommandParamType.INT)
                });
                this.addCommandParameters(name + "_help",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("help",new String[]{"help"})
                });
                this.addCommandParameters(name + "_my",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("my",new String[]{"my"})
                });
            }
        }
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        if(!sender.isOp()){
            log.addError("权限不足").output();
            return 0;
        }
        var args = result.getValue();
        String arg1 = "";
        if (args.hasResult(1)) {
            arg1 = args.getResult(1);
        }
        switch((String) args.getResult(0)){
            case "help":
                sender.sendMessage("/rpg check attr [playerName]   查询属性命令");
                sender.sendMessage("/rpg weapon help   武器指令");
                sender.sendMessage("/rpg armour help   盔甲指令");
                sender.sendMessage("/rpg stone help   宝石指令");
                sender.sendMessage("/rpg magic help   魔法物品指令");
                sender.sendMessage("/rpg prefix help   称号指令");
                sender.sendMessage("/rpg guild   公会指令");
                sender.sendMessage("/rpg exp help   经验指令");
                sender.sendMessage("/rpg money help   金币指令");
                sender.sendMessage("/rpg point help   点券指令");
                sender.sendMessage("/rpg shop help   商店指令");
                break;
            case "check":
                if (!(sender instanceof Player)) {
                    log.addError("仅允许玩家执行").output();
                    return 0;
                }
                if (args.hasResult(1)) {
                    if (args.getResult(1).equals("attr")) {
                        if (args.hasResult(2)) {
                            List<Player> players = args.getResult(2);
                            if (players.isEmpty()) {
                                log.addNoTargetMatch().output();
                                return 0;
                            }
                            Player player = players.get(0);
                            PlayerAttr pAttr = PlayerAttr.getPlayerAttr((Player) sender);
                            if (pAttr == null) {
                                return 0;
                            }
                            pAttr.showAttrWindow(player);
                            return 1;
                        }
                    }
                    return 0;
                }
            case "guild":
                guildForm.make_one((Player) sender);
                break;
            case "shop":
                if(args.size() != 2){
                    sender.sendMessage("参数错误");
                    return 0;
                }
                if(arg1.equals("help")){
                    sender.sendMessage("/rpg shop [Name] 创建一个名为Name的商店");
                    return 1;
                }
                Events.playerShop.put((Player) sender, args.getResult(1));
                sender.sendMessage("点击一个木牌");
                break;
            case "exp":
                if(arg1.equals("give")){
                    if(args.size() != 4){
                        sender.sendMessage("参数错误");
                        return 0;
                    }
                    Player player = Server.getInstance().getPlayer((String) args.getResult(2));
                    if(player == null) return 0;
                    int expValue = args.getResult(3);
                    if(Level.addExp(player, expValue)){
                        if(sender.isPlayer()) sender.sendMessage("给予成功");
                    } else {
                        if(sender.isPlayer()) sender.sendMessage("给予失败");
                    }
                } else if (arg1.equals("help")) {
                    sender.sendMessage("/rpg exp give [Player] [Exp] 给予玩家经验");
                }
                break;
            case "money": {
                if (!args.hasResult(1)) {
                    return 0;
                }
                String arg = args.getResult(1);
                Player player = null;
                if (args.hasResult(2)) {
                    List<Player> players = args.getResult(2);
                    if (!players.isEmpty()) {
                        player = players.get(0);
                    }
                }
                int money = 0;
                if (args.hasResult(3)) {
                    money = args.getResult(3);
                }
                switch (arg) {
                    case "help" -> {
                        sender.sendMessage("/rpg money add [Player] [Money] 给予玩家金币");
                        sender.sendMessage("/rpg money del [Player] [Money] 扣除玩家金币");
                        sender.sendMessage("/rpg money my 查看自身金币");
                    }
                    case "add" -> {
                        if (player == null) return 0;
                        if (Money.addMoney(player, money)) {
                            if (sender.isPlayer()) sender.sendMessage("给予成功");
                        } else {
                            if (sender.isPlayer()) sender.sendMessage("给予失败");
                        }
                    }
                    case "del" -> {
                        if (Money.delMoney(player, money)) {
                            if (sender.isPlayer()) sender.sendMessage("扣除成功");
                        } else {
                            if (sender.isPlayer()) sender.sendMessage("扣除失败");
                        }
                    }
                    case "my" -> sender.sendMessage(String.valueOf(Money.getMoney((Player) sender)));
                }
                break;
            }
            case "point": {
                if (!args.hasResult(1)) {
                    return 0;
                }
                String arg = args.getResult(1);
                Player player = null;
                if (args.hasResult(2)) {
                    List<Player> players = args.getResult(2);
                    if (!players.isEmpty()) {
                        player = players.get(0);
                    }
                }
                int point = 0;
                if (args.hasResult(3)) {
                    point = args.getResult(3);
                }
                switch (arg) {
                    case "help" -> {
                        sender.sendMessage("/rpg point add [Player] [Point] 给予玩家点券");
                        sender.sendMessage("/rpg point del [Player] [Point] 扣除玩家点券");
                        sender.sendMessage("/rpg point my 查看自身点券");
                    }
                    case "add" -> {
                        if (Points.addPoint(player, point)) {
                            if (sender.isPlayer()) sender.sendMessage("给予成功");
                        } else {
                            if (sender.isPlayer()) sender.sendMessage("给予失败");
                        }
                    }
                    case "del" -> {
                        if (Points.delPoint(player, point)) {
                            if (sender.isPlayer()) sender.sendMessage("扣除成功");
                        } else {
                            if (sender.isPlayer()) sender.sendMessage("扣除失败");
                        }
                    }
                    case "my" -> sender.sendMessage(String.valueOf(Points.getPoint((Player) sender)));
                }
                break;
            }
            case "prefix": {
                if (!args.hasResult(1)) {
                    return 0;
                }
                String arg = args.getResult(1);
                Player player = null;
                if (args.hasResult(2)) {
                    List<Player> players = args.getResult(2);
                    if (players.isEmpty()) {
                        log.addNoTargetMatch().output();
                        return 0;
                    } else {
                        player = players.get(0);
                    }
                }
                String prefix = "";
                if (args.hasResult(3)) {
                    prefix = args.getResult(3);
                }
                switch (arg) {
                    case "help" -> {
                        sender.sendMessage("/rpg prefix give [Player] [Prefix] 给予玩家称号");
                        sender.sendMessage("/rpg prefix remove [Player] [Prefix] 扣除玩家称号");
                        sender.sendMessage("/rpg prefix my 查看自身称号");
                    }
                    case "give" -> {
                        if (args.size() != 4) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        if (Prefix.givePrefix(player, prefix)) {
                            sender.sendMessage("给予成功");
                        } else {
                            sender.sendMessage("给予失败");
                        }
                    }
                    case "remove" -> {
                        if (args.size() != 4) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        if (Prefix.delPrefix(player, prefix)) {
                            sender.sendMessage("给予成功");
                        } else {
                            sender.sendMessage("给予失败");
                        }
                    }
                    case "my" -> prefixForm.makeForm((Player) sender);
                }
                break;
            }
            case "weapon": {
                String arg = args.getResult(1);
                switch (arg) {
                    case "help" -> {
                        sender.sendMessage("/rpg weapon add [Name] 创建一个名为Name的武器配置");
                        sender.sendMessage("/rpg weapon del [Name] 删除一个名为Name的武器配置");
                        sender.sendMessage("/rpg weapon give [Player] [Name] [Count] 给予玩家一定数量的武器");
                        sender.sendMessage("/rpg weapon inlay 打开武器镶嵌面板");
                    }
                    case "add" -> {
                        if (args.size() != 3) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        Item item = ((Player) sender).getInventory().getItemInHand();
                        String id;
                        if (item.isNull()) {
                            id = "minecraft:iron_sword";
                        } else {
                            id = item.getNamespaceId();
                        }
                        Config config;
                        String weaponName = args.getResult(2);
                        if ((config = Weapon.addWeaponConfig(weaponName, id)) != null) {
                            Weapon weapon = Weapon.loadWeapon(weaponName, config);
                            Main.loadWeapon.put(weaponName, weapon);
                            sender.sendMessage("添加成功");
                        } else {
                            sender.sendMessage("添加失败");
                        }
                    }
                    case "del" -> {
                        if (args.size() != 3) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        String weaponName = args.getResult(2);
                        if (Weapon.delWeaponConfig(weaponName)) {
                            Main.loadWeapon.remove(weaponName);
                            sender.sendMessage("删除成功");
                        } else {
                            sender.sendMessage("删除失败");
                        }
                    }
                    case "give" -> {
                        if (args.size() != 5) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        String playerName = args.getResult(2);
                        String weaponName = args.getResult(3);
                        int count = args.getResult(4);
                        Player player = Main.instance.getServer().getPlayer(playerName);
                        if (!player.isOnline()) {
                            sender.sendMessage("玩家不在线");
                            return 0;
                        }
                        if (Weapon.giveWeapon(player, weaponName, count)) {
                            if (sender.isPlayer()) sender.sendMessage("给予成功");
                        } else {
                            if (sender.isPlayer()) sender.sendMessage("给予失败");
                        }
                    }
                    case "inlay" -> {
                        Item item = ((Player) sender).getInventory().getItemInHand();
                        if (!item.isNull()) {
                            if (Weapon.isWeapon(item)) {
                                inlayForm.makeForm_one_weapon(((Player) sender), item);
                            } else {
                                sender.sendMessage("请手持武器");
                                return 0;
                            }
                        } else {
                            sender.sendMessage("未手持武器");
                            return 0;
                        }
                    }
                }
                break;
            }
            case "armour": {
                String arg = args.getResult(1);
                switch (arg) {
                    case "help" -> {
                        sender.sendMessage("/rpg armour add [Name] 创建一个名为Name的盔甲配置");
                        sender.sendMessage("/rpg armour del [Name] 删除一个名为Name的盔甲配置");
                        sender.sendMessage("/rpg armour give [Player] [Name] [Count] 给予玩家一定数量的盔甲");
                        sender.sendMessage("/rpg armour inlay 打开盔甲镶嵌面板");
                    }
                    case "add" -> {
                        if (args.size() != 3) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        Item item = ((Player) sender).getInventory().getItemInHand();
                        String id;
                        if (item.isNull()) {
                            id = "minecraft:iron_chestplate";
                        } else {
                            id = item.getNamespaceId();
                        }
                        Config config;
                        String armorName = args.getResult(2);
                        if ((config = Armour.addArmourConfig(armorName, id)) != null) {
                            Armour armour = Armour.loadArmour(armorName, config);
                            Main.loadArmour.put(armorName, armour);
                            sender.sendMessage("添加成功");
                        } else {
                            sender.sendMessage("添加失败");
                        }
                    }
                    case "del" -> {
                        if (args.size() != 3) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        String armorName = args.getResult(2);
                        if (Armour.delArmourConfig(armorName)) {
                            Main.loadArmour.remove(armorName);
                            sender.sendMessage("删除成功");
                        } else {
                            sender.sendMessage("删除失败");
                        }
                    }
                    case "give" -> {
                        if (args.size() != 5) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        String playerName = args.getResult(2);
                        String armorName = args.getResult(3);
                        int count = args.getResult(4);
                        Player player = Main.instance.getServer().getPlayer(playerName);
                        if (!player.isOnline()) {
                            sender.sendMessage("玩家不在线");
                            return 0;
                        }
                        if (Armour.giveArmour(player, armorName, count)) {
                            if (sender.isPlayer()) sender.sendMessage("给予成功");
                        } else {
                            if (sender.isPlayer()) sender.sendMessage("给予失败");
                        }
                    }
                    case "inlay" -> {
                        Item item = ((Player) sender).getInventory().getItemInHand();
                        if (!item.isNull()) {
                            if (Armour.isArmour(item)) {
                                inlayForm.makeForm_one_armour(((Player) sender), item);
                            } else {
                                sender.sendMessage("请手持盔甲");
                                return 0;
                            }
                        } else {
                            sender.sendMessage("未手持盔甲");
                            return 0;
                        }
                    }
                }
                break;
            }
            case "stone": {
                String arg = args.getResult(1);
                switch (arg) {
                    case "help" -> {
                        sender.sendMessage("/rpg stone add [Name] 创建一个名为Name的宝石配置");
                        sender.sendMessage("/rpg stone del [Name] 删除一个名为Name的宝石配置");
                        sender.sendMessage("/rpg stone give [Player] [Name] [Count] 给予玩家一定数量的宝石");
                    }
                    case "add" -> {
                        if (args.size() != 3) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        Item item = ((Player) sender).getInventory().getItemInHand();
                        String id;
                        if (item.isNull()) {
                            id = "minecraft:emerald";
                        } else {
                            id = item.getNamespaceId();
                        }
                        String stoneName = args.getResult(2);
                        Config config;
                        if ((config = Stone.addStoneConfig(stoneName, id)) != null) {
                            Stone stone = Stone.loadStone(stoneName, config);
                            Main.loadStone.put(stoneName, stone);
                            sender.sendMessage("添加成功");
                        } else {
                            sender.sendMessage("添加失败");
                        }
                    }
                    case "del" -> {
                        if (args.size() != 3) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        String stoneName = args.getResult(2);
                        if (Stone.delStoneConfig(stoneName)) {
                            Main.loadStone.remove(stoneName);
                            sender.sendMessage("删除成功");
                        } else {
                            sender.sendMessage("删除失败");
                        }
                    }
                    case "give" -> {
                        if (args.size() != 5) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        String playerName = args.getResult(2);
                        String stoneName = args.getResult(3);
                        int count = args.getResult(4);
                        Player player = Main.instance.getServer().getPlayer(playerName);
                        if (!player.isOnline()) {
                            sender.sendMessage("玩家不在线");
                            return 0;
                        }
                        if (Stone.giveStone(player, stoneName, count)) {
                            if (sender.isPlayer()) sender.sendMessage("给予成功");
                        } else {
                            if (sender.isPlayer()) sender.sendMessage("给予失败");
                        }
                    }
                }
                break;
            }
            case "magic": {
                String arg = args.getResult(1);
                switch (arg) {
                    case "help" -> {
                        sender.sendMessage("/rpg magic add [Name] 创建一个名为Name的魔法物品配置");
                        sender.sendMessage("/rpg magic del [Name] 删除一个名为Name的魔法物品配置");
                        sender.sendMessage("/rpg magic give [Player] [Name] [Count] 给予玩家一定数量的魔法物品");
                    }
                    case "add" -> {
                        if (args.size() != 3) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        Item item = ((Player) sender).getInventory().getItemInHand();
                        String id;
                        if (item.isNull()) {
                            id = "minecraft:apple";
                        } else {
                            id = item.getNamespaceId();
                        }
                        String magicName = args.getResult(2);
                        Config config;
                        if ((config = Magic.addMagicConfig(magicName, id)) != null) {
                            Magic magic = Magic.loadMagic(magicName, config);
                            Main.loadMagic.put(magicName, magic);
                            sender.sendMessage("添加成功");
                        } else {
                            sender.sendMessage("添加失败");
                        }
                    }
                    case "del" -> {
                        if (args.size() != 3) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        String magicName = args.getResult(2);
                        if (Magic.delMagicConfig(magicName)) {
                            Main.loadMagic.remove(magicName);
                            sender.sendMessage("删除成功");
                        } else {
                            sender.sendMessage("删除失败");
                        }
                    }
                    case "give" -> {
                        if (args.size() != 5) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        String playerName = args.getResult(2);
                        String magicName = args.getResult(3);
                        int count = args.getResult(4);
                        Player player = Main.instance.getServer().getPlayer(playerName);
                        if (!player.isOnline()) {
                            sender.sendMessage("玩家不在线");
                            return 0;
                        }
                        if (Magic.giveMagic(player, magicName, count)) {
                            if (sender.isPlayer()) sender.sendMessage("给予成功");
                        } else {
                            if (sender.isPlayer()) sender.sendMessage("给予失败");
                        }
                    }
                }
                break;
            }
            case "box": {
                String arg = args.getResult(1);
                switch (arg) {
                    case "help" -> {
                        sender.sendMessage("/rpg box add [Name] 创建一个名为Name的箱子配置");
                        sender.sendMessage("/rpg box del [Name] 删除一个名为Name的箱子配置");
                        sender.sendMessage("/rpg box give [Player] [Name] [Count] 给予玩家一定数量的箱子");
                    }
                    case "add" -> {
                        if (args.size() != 3) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        Item item = ((Player) sender).getInventory().getItemInHand();
                        String id;
                        if (item.isNull()) {
                            id = "minecraft:egg";
                        } else {
                            id = item.getNamespaceId();
                        }
                        String boxName = args.getResult(2);
                        Config config;
                        if ((config = Box.addBoxConfig(boxName, id)) != null) {
                            Box box = Box.loadBox(boxName, config);
                            Main.loadBox.put(boxName, box);
                            sender.sendMessage("添加成功");
                        } else {
                            sender.sendMessage("添加失败");
                        }
                    }
                    case "del" -> {
                        if (args.size() != 3) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        String boxName = args.getResult(2);
                        if (Box.delBoxConfig(boxName)) {
                            Main.loadBox.remove(boxName);
                            sender.sendMessage("删除成功");
                        } else {
                            sender.sendMessage("删除失败");
                        }
                    }
                    case "give" -> {
                        if (args.size() != 5) {
                            sender.sendMessage("参数错误");
                            return 0;
                        }
                        String playerName = args.getResult(2);
                        String boxName = args.getResult(3);
                        int count = args.getResult(4);
                        Player player = Main.instance.getServer().getPlayer(playerName);
                        if (!player.isOnline()) {
                            sender.sendMessage("玩家不在线");
                            return 0;
                        }
                        if (Box.giveBox(player, boxName, count)) {
                            if (sender.isPlayer()) sender.sendMessage("给予成功");
                        } else {
                            if (sender.isPlayer()) sender.sendMessage("给予失败");
                        }
                    }
                }
                break;
            }
        }
        return 0;
    }

}