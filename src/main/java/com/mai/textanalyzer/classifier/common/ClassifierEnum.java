/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.classifier.common;

import com.mai.textanalyzer.indexing.common.IndexerEnum;

/**
 *
 * @author Sergey
 */
public enum ClassifierEnum {

    NAIVE_BAYES(1, "NaiveBayes", ClassifierTypeEnum.WEKA_CLASSIFIER),
    SVM(2, "Support Vector Machine", ClassifierTypeEnum.WEKA_CLASSIFIER),
    IBK(3, "K-nearest neighbours", ClassifierTypeEnum.WEKA_CLASSIFIER);
    private final int id;
    private final String nameModel;
    private final ClassifierTypeEnum classifierType;

    private ClassifierEnum(int id, String nameModel, ClassifierTypeEnum classifierType) {
        this.id = id;
        this.nameModel = nameModel;
        this.classifierType = classifierType;
    }

    public int getId() {
        return id;
    }

    public String getModelName() {
        return nameModel;
    }

    public ClassifierTypeEnum getClassifierType() {
        return classifierType;
    }

    public static String getFullNameModel(ClassifierEnum classifierEnum, IndexerEnum indexerEnum) {
        return classifierEnum.getModelName() + indexerEnum.getModelName();
    }

    public static ClassifierEnum getClassifierEnumById(int id) {
        for (ClassifierEnum ce : ClassifierEnum.values()) {
            if (ce.getId() == id) {
                return ce;
            }
        }
        return null;
    }

}
