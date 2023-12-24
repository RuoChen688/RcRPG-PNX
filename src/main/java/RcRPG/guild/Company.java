package RcRPG.guild;

import cn.nukkit.Player;

import java.util.HashMap;

public class Company {
    private String name;// 公司名字
    private int funds;// 公司总资金
    private int totalShares; // 公司总股数
    private int currentDate; // 当前日期
    private int stockPriceYesterday; // 昨日股价
    private int stockPrice; // 股价
    private float upperLimit = 1.5f; // 涨停比例
    private float lowerLimit = 0.5f;  // 跌停比例
    private HashMap<String, Integer> shareholders = new HashMap<>();

    public Company(String name, int funds, int totalShares) {
        this.name = name;
        this.funds = funds;
        this.totalShares = totalShares;

        this.stockPrice = funds / totalShares;
        this.currentDate = 1; // 初始日期为1
        this.stockPriceYesterday = this.stockPrice; // 初始昨日股价与当前股价相同
    }

    // 获取涨停价格
    public int getUpperLimitPrice() {
        return (int) (stockPriceYesterday * upperLimit);
    }

    // 获取跌停价格
    public int getLowerLimitPrice() {
        return (int) (stockPriceYesterday * lowerLimit);
    }

    public void addShareholder(Player player, int shares) {
        int remainingShares = totalShares - getShareholdersSum();

        if (remainingShares >= shares) {
            shareholders.put(player.getName(), shares);
        } else {
            // 在这里处理股权分配超过总股数的情况，可以抛出异常或采取其他处理方式
            System.out.println("Error: Not enough shares available to add shareholder.");
        }
    }

    public int getShareholder(Player player) {
        return shareholders.getOrDefault(player.getName(), 0);
    }

    public boolean transferShareholder(Player shareholder, Player recipient, int shares) {
        int shareholderShares = getShareholder(shareholder);

        if (shareholderShares >= shares) {
            shareholders.put(shareholder.getName(), shareholderShares - shares);
            shareholders.put(recipient.getName(), getShareholder(recipient) + shares);
            return true;
        } else {
            // 在这里处理股权转让超过股东所拥有的股数的情况，可以抛出异常或采取其他处理方式
            System.out.println("Error: Not enough shares to transfer.");
            return false;
        }
    }

    // 设置股价，并更新昨日股价
    public void setStockPrice(int newPrice) {
        if (newPrice <= getUpperLimitPrice() && newPrice >= getLowerLimitPrice()) {
            stockPriceYesterday = stockPrice; // 更新昨日股价
            stockPrice = newPrice;
        } else {
            // 在这里处理股价超过涨停或跌停的情况，可以抛出异常或采取其他处理方式
            System.out.println("Error: Stock price exceeds limit.");
        }
    }

    public void printInfo() {
        System.out.println("Company: " + name);
        System.out.println("Funds: " + funds);
        System.out.println("Total Shares: " + totalShares);
        System.out.println("Stock Price: " + stockPrice);
        System.out.println("Yesterday's Stock Price: " + stockPriceYesterday);
        System.out.println("Upper Limit Price: " + getUpperLimitPrice());
        System.out.println("Lower Limit Price: " + getLowerLimitPrice());
        System.out.println("Shareholders:");

        for (String playerName : shareholders.keySet()) {
            int shares = shareholders.get(playerName);
            System.out.println(playerName + ": " + shares + " shares");
        }
    }

    private int getShareholdersSum() {
        return shareholders.values().stream().mapToInt(Integer::intValue).sum();
    }
}
