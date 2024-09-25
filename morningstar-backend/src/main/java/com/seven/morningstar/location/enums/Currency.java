package com.seven.morningstar.location.enums;

import lombok.Getter;

@Getter
public enum Currency {
    NGN('₦'),
    USD('$'),
    GBP('£');

    private char sign;
    Currency(char sign){
        this.sign = sign;
    }
}
