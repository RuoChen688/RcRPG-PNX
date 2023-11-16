package RcRPG.AttrManager;

public class AttrNameParse {
    private String result;
    private int number;

    public AttrNameParse(String result, int number) {
        this.result = result;
        this.number = number;
    }

    public String getResult() {
        return result;
    }

    public int getNumber() {
        return number;
    }

    public static AttrNameParse processString(String input) {
        if (input.startsWith("min_")) {
            String result = input.substring(4);
            return new AttrNameParse(result, 0);
        } else if (input.startsWith("max_")) {
            String result = input.substring(4);
            return new AttrNameParse(result, 1);
        } else {
            return new AttrNameParse(input, -1);// 什么也不是，直接返回输入
        }
    }
}