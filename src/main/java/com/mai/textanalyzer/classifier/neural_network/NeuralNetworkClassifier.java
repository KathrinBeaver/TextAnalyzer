/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.classifier.neural_network;

import com.mai.textanalyzer.classifier.common.ClassifierEnum;
import com.mai.textanalyzer.classifier.common.Prediction;
import com.mai.textanalyzer.classifier.common.TextClassifier;
import java.util.List;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 *
 * @author Sergey
 */
public class NeuralNetworkClassifier implements TextClassifier {

    private final MultiLayerNetwork net;
    private final List<String> topics;

    public NeuralNetworkClassifier(MultiLayerNetwork net, List<String> topics) {
        this.net = net;
        this.topics = topics;
    }

    @Override
    public ClassifierEnum getClassifierEnum() {
        return ClassifierEnum.NEURAL_NETWORK;
    }

    @Override
    public List<String> getTopicList() {
        return topics;
    }

    @Override
    public String classifyMessage(INDArray matrixTextModel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Prediction> getDistribution(INDArray matrixTextModel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public INDArray getDistributionAsINDArray(INDArray matrixTextModel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
