package com.natwest.transaction.model;

import lombok.Data;

@Data
public class Category {

    private int mcc;
    private String merchantBrandName;
    private String merchantCategory1Name;
    private String merchantCategory2Name;
    private String merchantCategory3Name;
    private String fcaCode;
    private String fcaCategory;
}
