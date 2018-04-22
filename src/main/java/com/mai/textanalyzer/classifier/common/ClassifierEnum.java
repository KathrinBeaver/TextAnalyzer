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

    NAIVE_BAYES("NaiveBayes", ClassifierTypeEnum.WEKA_CLASSIFIER),
    SVM("Support Vector Machine", ClassifierTypeEnum.WEKA_CLASSIFIER),
    IBK("K-nearest neighbours", ClassifierTypeEnum.WEKA_CLASSIFIER);

    private final String nameModel;
    private final ClassifierTypeEnum classifierType;

    private ClassifierEnum(String nameModel, ClassifierTypeEnum classifierType) {
        this.nameModel = nameModel;
        this.classifierType = classifierType;
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

}
