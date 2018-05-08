/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.csv;

/**
 *
 * @author Sergey
 */
public enum DataType {
    LEARNING("learning"),
    TEST("test");
    private final String name;

    private DataType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
