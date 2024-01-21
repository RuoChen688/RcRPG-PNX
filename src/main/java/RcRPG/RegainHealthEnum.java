package RcRPG;

import lombok.Getter;

@Getter
public enum RegainHealthEnum {
    LifeSteal(1001),
    HpPerNature(1002);

    private final int code;

    RegainHealthEnum(int code) {
        this.code = code;
    }

}
