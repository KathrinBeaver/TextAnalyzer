/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.vectorization.common;

/**
 *
 * @author Sergey
 */
public enum VectorizationEnum {
    DOC2VEC(1, "Doc2Vec");

    private final int id;
    private final String rusName;

    private VectorizationEnum(int id, String rusName) {
        this.id = id;
        this.rusName = rusName;
    }

    public int getId() {
        return id;
    }

    public String getRusName() {
        return rusName;
    }

}
