/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.creater;

/**
 *
 * @author Sergey
 */
public enum ClassifierEnum {

    NAIVE_BAYES("NaiveBayesModel");

    private final String nameModel;

    private ClassifierEnum(String nameModel) {
        this.nameModel = nameModel;
    }

    public String getModelName() {
        return nameModel;
    }

}
