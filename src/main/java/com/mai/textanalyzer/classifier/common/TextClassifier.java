/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.classifier.common;

import org.nd4j.linalg.api.ndarray.INDArray;

/**
 *
 * @author Sergey
 */
public interface TextClassifier {

    public String classifyMessage(INDArray matrixTextModel);

    public double[] getDistribution(INDArray matrixTextModel);
}
