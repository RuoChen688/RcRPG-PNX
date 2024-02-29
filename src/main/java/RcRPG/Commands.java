package RcRPG;

import RcRPG.AttrManager.PlayerAttr;
import RcRPG.Form.guildForm;
import RcRPG.Form.inlayForm;
import RcRPG.Form.prefixForm;
import RcRPG.RPG.*;
import RcRPG.Society.Money;
import RcRPG.Society.Points;
import RcRPG.Society.Prefix;
import RcRPG.panel.dismantle.DismantlePanel;
import RcRPG.panel.ornament.OrnamentPanel;
import RcRPG.window.*;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import cn.nukkit.lang.PluginI18n;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.util.ArrayList;

import static RcRPG.RcRPGMain.disablePrefix;

public class Commands extends PluginCommand<RcRPGMain> {
    protected RcRPGMain api;
    protected PluginI18n i18n;
    public Commands(String cmdName) {
        super(cmdName, RcRPGMain.getInstance());
        this.setDescription("RcRPG指令");
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
            add("ornament");
        }};
        String[] list = new String[]{"weapon","armour","magic","stone","box","ornament","guild","prefix","money","point","exp","shop"};
        this.addCommandParameters("help",new CommandParameter[]{
                CommandParameter.newEnum("help",new String[]{"help"})
        });
        this.addCommandParameters("admin",new CommandParameter[]{
                CommandParameter.newEnum("admin",new String[]{"admin"})
        });
        this.addCommandParameters("reload",new CommandParameter[]{
                CommandParameter.newEnum("reload",new String[]{"reload"})
        });
        this.addCommandParameters("inlay",new CommandParameter[]{
                CommandParameter.newEnum("inlay",new String[]{"inlay"})
        });
        this.addCommandParameters("dismantle",new CommandParameter[]{
                CommandParameter.newEnum("dismantle",new String[]{"dismantle"})
        });
        this.addCommandParameters("check",new CommandParameter[]{
                CommandParameter.newEnum("check", new String[]{"check"}),
                CommandParameter.newEnum("type",new String[]{"attr"}),
                CommandParameter.newType("player", CommandParamType.TARGET),
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
                if(name.equals("ornament")){
                    this.addCommandParameters(name + "_my",new CommandParameter[]{
                            CommandParameter.newEnum(name,new String[]{name}),
                            CommandParameter.newEnum("my",new String[]{"my"})
                    });
                }
                this.addCommandParameters(name + "_admin",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("admin",new String[]{"admin"})
                });
                this.addCommandParameters(name + "_add",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("add",new String[]{"add"}),
                        CommandParameter.newType(name.toLowerCase(),CommandParamType.STRING)
                });
                this.addCommandParameters(name + "_del",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("del",new String[]{"del"}),
                        CommandParameter.newType(name.toLowerCase(),CommandParamType.STRING)
                });
                this.addCommandParameters(name + "_help",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("help",new String[]{"help"})
                });
                this.addCommandParameters(name + "_give",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("give",new String[]{"give", "drop"}),
                        CommandParameter.newType("PlayerName",CommandParamType.STRING),
                        CommandParameter.newType(name.toLowerCase(),CommandParamType.STRING),
                        CommandParameter.newType("Count",true,CommandParamType.INT)
                });
            }else if(name.equals("prefix")){
                this.addCommandParameters(name + "_give",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("give",new String[]{"give"}),
                        CommandParameter.newType("player", CommandParamType.TARGET),
                        CommandParameter.newType(name.toLowerCase(),CommandParamType.STRING)
                });
                this.addCommandParameters(name + "_remove",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("remove",new String[]{"remove"}),
                        CommandParameter.newType("player", CommandParamType.TARGET),
                        CommandParameter.newType(name.toLowerCase(),CommandParamType.STRING)
                });
                this.addCommandParameters(name + "_help",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("help",new String[]{"help"})
                });
                this.addCommandParameters(name + "_my",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("my",new String[]{"my"})
                });
            } else if(society.contains(name)) {// money, point
                this.addCommandParameters(name + "_add",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("add",new String[]{"add"}),
                        CommandParameter.newType("player", CommandParamType.TARGET),
                        CommandParameter.newType(name.toLowerCase(),CommandParamType.INT)
                });
                this.addCommandParameters(name + "_del",new CommandParameter[]{
                        CommandParameter.newEnum(name,new String[]{name}),
                        CommandParameter.newEnum("del",new String[]{"del"}),
                        CommandParameter.newType("PlayerName",CommandParamType.STRING),
                        CommandParameter.newType(name.toLowerCase(),CommandParamType.INT)
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
        api = RcRPGMain.getInstance();
        i18n = RcRPGMain.getI18n();
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!sender.isOp()){
            sender.sendMessage(TextFormat.RED+"权限不足");
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(TextFormat.RED+"缺少参数");
            return false;
        }
        switch(args[0]) {
            case "help":
                sender.sendMessage("/rpg check attr [playerName]   查询属性命令");
                sender.sendMessage("/rpg inlay   镶嵌宝石的命令 (GUI)");
                sender.sendMessage("/rpg dismantle   分解装备的命令 (GUI)");
                sender.sendMessage("/rpg weapon help   武器指令");
                sender.sendMessage("/rpg armour help   盔甲指令");
                sender.sendMessage("/rpg stone help   宝石指令");
                sender.sendMessage("/rpg magic help   魔法物品指令");
                sender.sendMessage("/rpg box help   箱子指令");
                sender.sendMessage("/rpg ornament help   饰品指令");
                sender.sendMessage("/rpg prefix help   称号指令");
                sender.sendMessage("/rpg guild   公会指令");
                sender.sendMessage("/rpg exp help   经验指令");
                sender.sendMessage("/rpg money help   金币指令");
                sender.sendMessage("/rpg point help   点券指令");
                sender.sendMessage("/rpg shop help   商店指令");
                break;
            case "admin": {
                if (!sender.isPlayer()) {
                    sender.sendMessage(TextFormat.RED+"本命令必须是玩家执行");
                    return false;
                }
                new RcRPGAdminWin((Player) sender);
                return true;
            }
            case "reload": {
                RcRPGMain.getInstance().init();
                sender.sendMessage(TextFormat.GREEN+"rpg.commands.reloaded");
                return true;
            }
            case "dismantle": {
                if (!sender.isPlayer()) {
                    sender.sendMessage(TextFormat.RED+"本命令必须是玩家执行");
                    return false;
                }
                DismantlePanel panel = new DismantlePanel();
                panel.sendPanel((Player) sender);
                return true;
            }
            case "inlay": {
                if (!sender.isPlayer()) {
                    sender.sendMessage(TextFormat.RED+"本命令必须是玩家执行");
                    return false;
                }
                Item item = ((Player) sender).getInventory().getItemInHand();
                if (item.isNull()) {
                    sender.sendMessage(TextFormat.RED+"未手持装备");
                    return false;
                }
                if (!Weapon.isWeapon(item) && !Armour.isArmour(item)) {
                    sender.sendMessage(TextFormat.RED+"请手持有效装备");
                    return false;
                }
                new inlayForm().makeInlayForm((Player) sender, item);
                return true;
            }
            case "check":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(TextFormat.RED+"仅允许玩家执行");
                    return false;
                }

                if (args.length < 2) {
                    sender.sendMessage(TextFormat.RED+"缺少第2个参数");
                    return false;
                }
                if (args[1].equals("attr")) {

                    if (args.length < 3) {
                        sender.sendMessage(TextFormat.RED+"缺少第3个参数");
                        return false;
                    }
                    Player player = api.getServer().getPlayer(args[2]);
                    if (!player.isValid()) {
                        sender.sendMessage("没有匹配的目标");
                        return false;
                    }
                    PlayerAttr pAttr = PlayerAttr.getPlayerAttr((Player) sender);
                    if (pAttr == null) {
                        return false;
                    }
                    pAttr.showAttrWindow(player);
                    return true;
                }
            case "guild":
                guildForm.make_one((Player) sender);
                break;
            case "shop":
                if (args.length < 2) {
                    sender.sendMessage(TextFormat.RED+"缺少第2个参数");
                    return false;
                }
                if(args[1].equals("help")){
                    sender.sendMessage("/rpg shop [Name]   创建一个名为Name的商店");
                    return true;
                }
                Events.playerShop.put((Player) sender, args[1]);
                sender.sendMessage("点击一个木牌");
                break;
            case "exp":
                switch (args[1]) {
                    case "help" -> {
                        sender.sendMessage("/rpg exp give [Player] [Exp] 给予玩家经验");
                    }
                    case "give" -> {
                        if (args.length < 4) {
                            sender.sendMessage(TextFormat.RED + "缺少第4个参数");
                            return false;
                        }
                        Player player = Server.getInstance().getPlayer(args[2]);
                        if (player == null) return false;
                        int expValue = Integer.parseInt(args[3]);
                        if (Level.addExp(player, expValue)) {
                            if (sender.isPlayer()) sender.sendMessage("给予成功");
                        } else {
                            if (sender.isPlayer()) sender.sendMessage("给予失败");
                        }
                    }
                }
                sender.sendMessage(TextFormat.RED+"错误的命令，请使用：/rpg help");
                return false;
            case "money": {
                if (args.length < 2) {
                    sender.sendMessage(TextFormat.RED+"缺少第2个参数");
                    return false;
                }
                Player player = api.getServer().getPlayer(args[2]);
                if (!player.isValid()) {
                    sender.sendMessage("没有匹配的目标");
                    return false;
                }
                int money = 0;
                if (args.length > 3) {
                    money = Integer.parseInt(args[3]);
                }
                switch (args[1]) {
                    case "help" -> {
                        sender.sendMessage("/rpg money add [Player] [Money] 给予玩家金币");
                        sender.sendMessage("/rpg money del [Player] [Money] 扣除玩家金币");
                        sender.sendMessage("/rpg money my 查看自身金币");
                    }
                    case "add" -> {
                        if (player == null) return false;
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
                if (args.length < 2) {
                    sender.sendMessage(TextFormat.RED+"缺少第2个参数");
                    return false;
                }
                Player player = null;
                if (args.length > 2) {
                    player = api.getServer().getPlayer(args[2]);
                    if (!player.isValid()) {
                        sender.sendMessage("没有匹配的目标");
                        return false;
                    }
                }
                int point = 0;
                if (args.length > 3) {
                    point = Integer.parseInt(args[3]);
                }
                switch (args[1]) {
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
                if (args.length < 2) {
                    sender.sendMessage(TextFormat.RED+"缺少第2个参数");
                    return false;
                }
                if (disablePrefix) {
                    sender.sendMessage(TextFormat.RED+"RcRPG 称号已被禁用");
                    return false;
                }
                Player player = null;
                if (args.length > 2) {
                    player = api.getServer().getPlayer(args[2]);
                    if (!player.isValid()) {
                        sender.sendMessage("没有匹配的目标");
                        return false;
                    }
                }
                String prefix = "";
                if (args.length > 3) {
                    prefix = args[3];
                }
                switch (args[1]) {
                    case "help" -> {
                        sender.sendMessage("/rpg prefix give [Player] [Prefix] 给予玩家称号");
                        sender.sendMessage("/rpg prefix remove [Player] [Prefix] 扣除玩家称号");
                        sender.sendMessage("/rpg prefix my 查看自身称号");
                    }
                    case "give" -> {
                        if (args.length < 4) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        if (Prefix.givePrefix(player, prefix)) {
                            sender.sendMessage("给予成功");
                        } else {
                            sender.sendMessage("给予失败");
                        }
                    }
                    case "remove" -> {
                        if (args.length < 4) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        if (Prefix.delPrefix(player, prefix)) {
                            sender.sendMessage("移除成功");
                        } else {
                            sender.sendMessage("移除失败");
                        }
                    }
                    case "my" -> prefixForm.makeForm((Player) sender);
                }
                break;
            }
            case "weapon": {
                if (args.length < 2) {
                    sender.sendMessage(TextFormat.RED+"缺少第2个参数");
                    return false;
                }
                switch (args[1]) {
                    case "help" -> {
                        sender.sendMessage("/rpg weapon admin 管理武器列表");
                        sender.sendMessage("/rpg weapon add [Name] 创建一个名为Name的武器配置");
                        sender.sendMessage("/rpg weapon del [Name] 删除一个名为Name的武器配置");
                        sender.sendMessage("/rpg weapon give [Player] [Name] [Count] 给予玩家一定数量的武器");
                        sender.sendMessage("/rpg weapon inlay 打开武器镶嵌面板");
                    }
                    case "admin" -> {
                        if (!sender.isPlayer()) {
                            sender.sendMessage(TextFormat.RED+"本命令必须是玩家执行");
                            return false;
                        }
                        new SendWeaponAdminWin((Player) sender);
                    }
                    case "add" -> {
                        if (args.length < 3) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        Item item = ((Player) sender).getInventory().getItemInHand();
                        String id;
                        if (item.isNull()) {
                            id = "minecraft:iron_sword";
                        } else {
                            id = item.getNamespaceId();
                        }
                        Config config;
                        String weaponName = args[2];
                        if ((config = Weapon.addWeaponConfig(weaponName, id)) != null) {
                            Weapon weapon = Weapon.loadWeapon(weaponName, config);
                            RcRPGMain.loadWeapon.put(weaponName, weapon);
                            sender.sendMessage("添加成功");
                        } else {
                            sender.sendMessage("添加失败");
                        }
                    }
                    case "del" -> {
                        if (args.length < 3) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        String weaponName = args[2];
                        if (Weapon.delWeaponConfig(weaponName)) {
                            RcRPGMain.loadWeapon.remove(weaponName);
                            sender.sendMessage("删除成功");
                        } else {
                            sender.sendMessage("删除失败");
                        }
                    }
                    case "drop", "give" -> {
                        String playerName = args[2];
                        String weaponName = args[3];
                        int count = args.length > 4 ? Integer.parseInt(args[4]) : 1;
                        Player player = RcRPGMain.instance.getServer().getPlayer(playerName);
                        if (!player.isOnline()) {
                            sender.sendMessage("玩家不在线");
                            return false;
                        }
                        if (args[1].equals("drop")) {
                            Item item = Weapon.getItem(weaponName, count);
                            if (item == null) {
                                RcRPGMain.getInstance().getLogger().warning("weapon drop失败："+weaponName);
                            } else {
                                player.getLevel().dropItem(player.getPosition(), item);
                            }
                        } else {
                            if (Weapon.giveWeapon(player, weaponName, count)) {
                                if (sender.isPlayer()) sender.sendMessage("给予成功");
                            } else {
                                if (sender.isPlayer()) sender.sendMessage("给予失败");
                            }
                        }
                    }
                }
                break;
            }
            case "armour": {
                if (args.length < 2) {
                    sender.sendMessage(TextFormat.RED+"缺少第2个参数");
                    return false;
                }
                switch (args[1]) {
                    case "help" -> {
                        sender.sendMessage("/rpg armour admin 管理盔甲列表");
                        sender.sendMessage("/rpg armour add [Name] 创建一个名为Name的盔甲配置");
                        sender.sendMessage("/rpg armour del [Name] 删除一个名为Name的盔甲配置");
                        sender.sendMessage("/rpg armour give [Player] [Name] [Count] 给予玩家一定数量的盔甲");
                        sender.sendMessage("/rpg armour inlay 打开盔甲镶嵌面板");
                    }
                    case "admin" -> {
                        if (!sender.isPlayer()) {
                            sender.sendMessage(TextFormat.RED+"本命令必须是玩家执行");
                            return false;
                        }
                        new SendArmourAdminWin((Player) sender);
                    }
                    case "add" -> {
                        if (args.length < 3) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        Item item = ((Player) sender).getInventory().getItemInHand();
                        String id;
                        if (item.isNull()) {
                            id = "minecraft:iron_chestplate";
                        } else {
                            id = item.getNamespaceId();
                        }
                        Config config;
                        String armorName = args[2];
                        if ((config = Armour.addArmourConfig(armorName, id)) != null) {
                            Armour armour = Armour.loadArmour(armorName, config);
                            RcRPGMain.loadArmour.put(armorName, armour);
                            sender.sendMessage("添加成功");
                        } else {
                            sender.sendMessage("添加失败");
                        }
                    }
                    case "del" -> {
                        if (args.length < 3) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        String armorName = args[2];
                        if (Armour.delArmourConfig(armorName)) {
                            RcRPGMain.loadArmour.remove(armorName);
                            sender.sendMessage("删除成功");
                        } else {
                            sender.sendMessage("删除失败");
                        }
                    }
                    case "drop", "give" -> {
                        String playerName = args[2];
                        String armorName = args[3];
                        int count = args.length > 4 ? Integer.parseInt(args[4]) : 1;
                        Player player = RcRPGMain.instance.getServer().getPlayer(playerName);
                        if (!player.isOnline()) {
                            sender.sendMessage("玩家不在线");
                            return false;
                        }
                        if (args[1].equals("drop")) {
                            Item item = Armour.getItem(armorName, count);
                            if (item == null) {
                                RcRPGMain.getInstance().getLogger().warning("armour drop失败："+armorName);
                            } else {
                                player.getLevel().dropItem(player.getPosition(), item);
                            }
                        } else {
                            if (Armour.giveArmour(player, armorName, count)) {
                                if (sender.isPlayer()) sender.sendMessage("给予成功");
                            } else {
                                if (sender.isPlayer()) sender.sendMessage("给予失败");
                            }
                        }
                    }
                }
                break;
            }
            case "stone": {
                if (args.length < 2) {
                    sender.sendMessage(TextFormat.RED+"缺少第2个参数");
                    return false;
                }
                switch (args[1]) {
                    case "help" -> {
                        sender.sendMessage("/rpg stone admin 管理宝石列表");
                        sender.sendMessage("/rpg stone add [Name] 创建一个名为Name的宝石配置");
                        sender.sendMessage("/rpg stone del [Name] 删除一个名为Name的宝石配置");
                        sender.sendMessage("/rpg stone give [Player] [Name] [Count] 给予玩家一定数量的宝石");
                    }
                    case "admin" -> {
                        if (!sender.isPlayer()) {
                            sender.sendMessage(TextFormat.RED+"本命令必须是玩家执行");
                            return false;
                        }
                        new SendStoneAdminWin((Player) sender);
                    }
                    case "add" -> {
                        if (args.length < 3) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        Item item = ((Player) sender).getInventory().getItemInHand();
                        String id;
                        if (item.isNull()) {
                            id = "minecraft:emerald";
                        } else {
                            id = item.getNamespaceId();
                        }
                        String stoneName = args[2];
                        Config config;
                        if ((config = Stone.addStoneConfig(stoneName, id)) != null) {
                            Stone stone = Stone.loadStone(stoneName, config);
                            RcRPGMain.loadStone.put(stoneName, stone);
                            sender.sendMessage("添加成功");
                        } else {
                            sender.sendMessage("添加失败");
                        }
                    }
                    case "del" -> {
                        if (args.length < 3) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        String stoneName = args[2];
                        if (Stone.delStoneConfig(stoneName)) {
                            RcRPGMain.loadStone.remove(stoneName);
                            sender.sendMessage("删除成功");
                        } else {
                            sender.sendMessage("删除失败");
                        }
                    }
                    case "give" -> {
                        String playerName = args[2];
                        String stoneName = args[3];
                        int count = args.length > 4 ? Integer.parseInt(args[4]) : 1;
                        Player player = RcRPGMain.instance.getServer().getPlayer(playerName);
                        if (!player.isOnline()) {
                            sender.sendMessage("玩家不在线");
                            return false;
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
                if (args.length < 2) {
                    sender.sendMessage(TextFormat.RED+"缺少第2个参数");
                    return false;
                }
                switch (args[1]) {
                    case "help" -> {
                        sender.sendMessage("/rpg magic admin 管理魔法物品");
                        sender.sendMessage("/rpg magic add [Name] 创建一个名为Name的魔法物品配置");
                        sender.sendMessage("/rpg magic del [Name] 删除一个名为Name的魔法物品配置");
                        sender.sendMessage("/rpg magic give [Player] [Name] [Count] 给予玩家一定数量的魔法物品");
                    }
                    case "add" -> {
                        if (args.length < 3) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        Item item = ((Player) sender).getInventory().getItemInHand();
                        String id;
                        if (item.isNull()) {
                            id = "minecraft:apple";
                        } else {
                            id = item.getNamespaceId();
                        }
                        String magicName = args[2];
                        Config config;
                        if ((config = Magic.addMagicConfig(magicName, id)) != null) {
                            Magic magic = Magic.loadMagic(magicName, config);
                            RcRPGMain.loadMagic.put(magicName, magic);
                            sender.sendMessage("添加成功");
                        } else {
                            sender.sendMessage("添加失败");
                        }
                    }
                    case "del" -> {
                        if (args.length < 3) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        String magicName = args[2];
                        if (Magic.delMagicConfig(magicName)) {
                            RcRPGMain.loadMagic.remove(magicName);
                            sender.sendMessage("删除成功");
                        } else {
                            sender.sendMessage("删除失败");
                        }
                    }
                    case "give" -> {
                        String playerName = args[2];
                        String magicName = args[3];
                        int count = args.length > 4 ? Integer.parseInt(args[4]) : 1;
                        Player player = RcRPGMain.instance.getServer().getPlayer(playerName);
                        if (!player.isOnline()) {
                            sender.sendMessage("玩家不在线");
                            return false;
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
                if (args.length < 2) {
                    sender.sendMessage(TextFormat.RED+"缺少第2个参数");
                    return false;
                }
                switch (args[1]) {
                    case "help" -> {
                        sender.sendMessage("/rpg box add [Name] 创建一个名为Name的箱子配置");
                        sender.sendMessage("/rpg box del [Name] 删除一个名为Name的箱子配置");
                        sender.sendMessage("/rpg box give [Player] [Name] [Count] 给予玩家一定数量的箱子");
                    }
                    case "add" -> {
                        if (args.length < 3) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        Item item = ((Player) sender).getInventory().getItemInHand();
                        String id;
                        if (item.isNull()) {
                            id = "minecraft:egg";
                        } else {
                            id = item.getNamespaceId();
                        }
                        String boxName = args[2];
                        Config config;
                        if ((config = Box.addBoxConfig(boxName, id)) != null) {
                            Box box = Box.loadBox(boxName, config);
                            RcRPGMain.loadBox.put(boxName, box);
                            sender.sendMessage("添加成功");
                        } else {
                            sender.sendMessage("添加失败");
                        }
                    }
                    case "del" -> {
                        if (args.length < 3) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        String boxName = args[2];
                        if (Box.delBoxConfig(boxName)) {
                            RcRPGMain.loadBox.remove(boxName);
                            sender.sendMessage("删除成功");
                        } else {
                            sender.sendMessage("删除失败");
                        }
                    }
                    case "give" -> {
                        String playerName = args[2];
                        String boxName = args[3];
                        int count = args.length > 4 ? Integer.parseInt(args[4]) : 1;
                        Player player = RcRPGMain.instance.getServer().getPlayer(playerName);
                        if (!player.isOnline()) {
                            sender.sendMessage("玩家不在线");
                            return false;
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
            case "ornament": {
                if (args.length < 2) {
                    sender.sendMessage(TextFormat.RED+"缺少第2个参数");
                    return false;
                }
                switch (args[1]) {
                    case "help" -> {
                        sender.sendMessage("/rpg ornament add [Name] 创建一个名为Name的饰品配置");
                        sender.sendMessage("/rpg ornament del [Name] 删除一个名为Name的饰品配置");
                        sender.sendMessage("/rpg ornament give [Player] [Name] [Count] 给予玩家一定数量的饰品");
                        sender.sendMessage("/rpg ornament my 打开自己的饰品背包");
                    }
                    case "admin" -> {
                        if (!sender.isPlayer()) {
                            sender.sendMessage(TextFormat.RED+"本命令必须是玩家执行");
                            return false;
                        }
                        new SendOrnamentAdminWin((Player) sender);
                    }
                    case "my" -> {
                        if (args.length < 2) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        OrnamentPanel panel = new OrnamentPanel();
                        panel.sendPanel((Player) sender);
                    }
                    case "add" -> {
                        if (args.length < 3) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        Item item = ((Player) sender).getInventory().getItemInHand();
                        String id;
                        if (item.isNull()) {
                            id = "minecraft:emerald";
                        } else {
                            id = item.getNamespaceId();
                        }
                        String ornamentName = args[2];
                        Config config;
                        if ((config = Ornament.addOrnamentConfig(ornamentName,id)) != null) {
                            Ornament ornament = Ornament.loadOrnament(ornamentName,config);
                            RcRPGMain.loadOrnament.put(ornamentName,ornament);
                            sender.sendMessage("添加成功");
                        } else {
                            sender.sendMessage("添加失败");
                        }
                    }
                    case "del" -> {
                        if (args.length < 3) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        String ornamentName = args[2];
                        if (Ornament.delOrnamentConfig(ornamentName)) {
                            RcRPGMain.loadOrnament.remove(ornamentName);
                            sender.sendMessage("删除成功");
                        } else {
                            sender.sendMessage("删除失败");
                        }
                    }
                    case "give" -> {
                        String playerName = args[2];
                        String ornamentName = args[3];
                        int count = args.length > 4 ? Integer.parseInt(args[4]) : 1;
                        Player player = RcRPGMain.instance.getServer().getPlayer(playerName);
                        if (!player.isOnline()) {
                            sender.sendMessage("玩家不在线");
                            return false;
                        }
                        if (Ornament.giveOrnament(player, ornamentName, count)) {
                            if (sender.isPlayer()) sender.sendMessage("给予成功");
                        } else {
                            if (sender.isPlayer()) sender.sendMessage("给予失败");
                        }
                    }
                }
                break;
            }
        }
        return false;
    }

}