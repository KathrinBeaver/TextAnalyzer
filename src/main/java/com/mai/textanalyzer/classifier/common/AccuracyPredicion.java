/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.classifier.common;

/**
 *
 * @author Sergey
 */
public class AccuracyPredicion extends Prediction implements Comparable<AccuracyPredicion> {

    private final double accuracy;
    private final int docCount;

    public AccuracyPredicion(Prediction prediction, double accuracy, int docCount) {
        super(prediction.getTopic(), prediction.getValue());
        this.accuracy = accuracy;
        this.docCount = docCount;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public int getDocCount() {
        return docCount;
    }

    @Override
    public int compareTo(AccuracyPredicion o) {
        return getTopic().compareTo(o.getTopic());
    }

}
