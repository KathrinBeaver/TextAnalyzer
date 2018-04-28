/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.dao.accuracy;

import com.mai.textanalyzer.classifier.common.ClassifierEnum;
import com.mai.textanalyzer.indexing.common.IndexerEnum;

/**
 *
 * @author Sergey
 */
public class Accuracy {

    private final ClassifierEnum classifierEnum;
    private final IndexerEnum indexerEnum;
    private final String topic;
    private final double accuracy;
    private final int docCount;

    public Accuracy(ClassifierEnum classifierEnum, IndexerEnum indexerEnum, String topic, double accuracy, int docCount) {
        this.classifierEnum = classifierEnum;
        this.indexerEnum = indexerEnum;
        this.topic = topic;
        this.accuracy = accuracy;
        this.docCount = docCount;
    }

    public ClassifierEnum getClassifierEnum() {
        return classifierEnum;
    }

    public IndexerEnum getIndexerEnum() {
        return indexerEnum;
    }

    public String getTopic() {
        return topic;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public int getDocCount() {
        return docCount;
    }

}
