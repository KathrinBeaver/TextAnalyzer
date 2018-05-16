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

    NAIVE_BAYES(1, "Naive Bayes", "Наивный баиесовский классификатор", ClassifierTypeEnum.WEKA_CLASSIFIER),
    SVM(2, "Support Vector Machine", "свм", ClassifierTypeEnum.WEKA_CLASSIFIER),
    IBK(3, "K-nearest Neighbours", "к-ближайших", ClassifierTypeEnum.WEKA_CLASSIFIER),
    LR(4, "Logistic Regression", "логистичекая регрессия", ClassifierTypeEnum.WEKA_CLASSIFIER),
    RF(5, "Random Forest", "дерево принятия решений", ClassifierTypeEnum.WEKA_CLASSIFIER),
    NEURAL_NETWORK(6, "Neural_Network", "неронная сеть", ClassifierTypeEnum.NEURAL_NETWORK_CLASSIFIER),
    MYLTI_CLASSIFIER(7, "Multi Classifier", "мульти классификатор", ClassifierTypeEnum.MYLTI_CLASSIFIER),
    BAGGING(8, "Bagging", "беггинг", ClassifierTypeEnum.WEKA_CLASSIFIER),
    BOOSTING(9, "Boostring", "бустинг", ClassifierTypeEnum.WEKA_CLASSIFIER),
    STACKING(10, "Stacking", "стекинг", ClassifierTypeEnum.WEKA_CLASSIFIER);
    private final int id;
    private final String nameModelForSave;
    private final String ruName;
    private final ClassifierTypeEnum classifierType;

    private ClassifierEnum(int id, String nameModel, String ruName, ClassifierTypeEnum classifierType) {
        this.id = id;
        this.nameModelForSave = nameModel;
        this.ruName = ruName;
        this.classifierType = classifierType;
    }

    public int getId() {
        return id;
    }

    public String getModelName() {
        return nameModelForSave;
    }

    public String getRuName() {
        return ruName;
    }

    public ClassifierTypeEnum getClassifierType() {
        return classifierType;
    }

    public static String getFullNameModel(ClassifierEnum classifierEnum, IndexerEnum indexerEnum) {
        return classifierEnum.getModelName() + indexerEnum.getModelName() + (classifierEnum == NEURAL_NETWORK ? ".zip" : "");
    }

    public static ClassifierEnum getClassifierEnumById(int id) {
        for (ClassifierEnum ce : ClassifierEnum.values()) {
            if (ce.getId() == id) {
                return ce;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return nameModelForSave;
    }

}
