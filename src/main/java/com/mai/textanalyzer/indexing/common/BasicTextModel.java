/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.indexing.common;

import org.nd4j.linalg.api.ndarray.INDArray;

/**
 *
 * @author Sergey
 */
public class BasicTextModel {

    private final String topic;
    private final INDArray iNDArray;

    public BasicTextModel(String topic, INDArray iNDArray) {
        this.topic = topic;
        this.iNDArray = iNDArray;
    }

    public String getTopic() {
        return topic;
    }

    public INDArray getiNDArray() {
        return iNDArray;
    }

}
