/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.classifier.neural_network;

import java.io.File;
import java.io.IOException;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

/**
 *
 * @author Sergey
 */
public final class NeuralNetworkUtils {

    private NeuralNetworkUtils() {
    }

    public static MultiLayerNetwork createNeuralNetwork() {
        return null;
    }

    public static void saveNeuralNetwork(File pathForSave, MultiLayerNetwork model) {
        try {
            ModelSerializer.writeModel(model, pathForSave, false);
        } catch (IOException ex) {
            throw new RuntimeException("Error saving model");
        }
    }

    public static MultiLayerNetwork loadNeuralNetwork(File filePath) {
        try {
            return ModelSerializer.restoreMultiLayerNetwork(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading model");
        }
    }

}
