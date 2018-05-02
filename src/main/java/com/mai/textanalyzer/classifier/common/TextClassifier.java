/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.classifier.common;

import java.util.List;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 *
 * @author Sergey
 */
public interface TextClassifier {

    ClassifierEnum getClassifierEnum();

    List<String> getTopicList();

    String classifyMessage(INDArray matrixTextModel);

    List<Prediction> getDistribution(INDArray matrixTextModel);
}
