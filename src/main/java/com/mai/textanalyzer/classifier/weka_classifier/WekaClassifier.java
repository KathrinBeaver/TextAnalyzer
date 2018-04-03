/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.classifier.weka_classifier;

import com.mai.textanalyzer.indexing.common.BasicTextModel;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nd4j.linalg.api.iter.NdIndexIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import weka.core.*;
import weka.classifiers.*;

/**
 *
 * @author Sergey
 */
public class WekaClassifier implements Serializable {

    /* The training data. */
    private Instances data = null;
    /* The classifier. */
    private final Classifier classifier;

    public WekaClassifier(AbstractClassifier classifier, int iNDArrayLength, List<String> topics) {
        this.classifier = classifier;

        String nameOfDataset = "classification";
        // Create numeric attributes.
        FastVector attributes = new FastVector(iNDArrayLength + 1);
        for (int i = 0; i < iNDArrayLength; i++) {
            attributes.addElement(new Attribute(String.valueOf(i)));
        }

        // Add class attribute.
        FastVector topicValues = new FastVector(topics.size());
        for (String topic : topics) {
            topicValues.addElement(topic);
        }

        attributes.addElement(new Attribute("topic", topicValues));
        // Create dataset with initial capacity of 100, and set index of class.
        data = new Instances(nameOfDataset, attributes, 100);
        data.setClassIndex(data.numAttributes() - 1);

    }

    /**
     * Updates model using the given training message.
     *
     * @param textModel
     */
    public void updateModel(BasicTextModel textModel) {
        // Convert message string into instance.
        Instance instance = makeInstance(textModel);
        data.add(instance);
        Instances instances = new Instances(data);
        try {
            classifier.buildClassifier(instances);
        } catch (Exception ex) {
            Logger.getLogger(WekaClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method that converts a text message into an instance.
     */
    private Instance makeInstance(BasicTextModel textModel) {
        Instance instance = makeInstance(textModel.getiNDArray());
        instance.setClassValue(textModel.getTopic());
        return instance;
    }

    /**
     * Method that converts a text message into an instance.
     */
    private Instance makeInstance(INDArray iNDArray) {

        Instance instance = new DenseInstance(iNDArray.length() + 1);

        NdIndexIterator iter = new NdIndexIterator(iNDArray.shape());
        int counter = 0;
        while (iter.hasNext()) {
            int[] nextIndex = iter.next();
            instance.setValue(counter, iNDArray.getDouble(nextIndex));
            counter++;
        }
        // Give instance access to attribute information from the dataset.
        instance.setDataset(data);
        return instance;
    }

    /**
     * Classifies a given message.
     *
     * @param matrixTextModel
     * @return
     * @throws java.lang.Exception
     */
    public String classifyMessage(INDArray matrixTextModel) {
        try {
            // Check if classifier has been built.
            if (data.numInstances() == 0) {
                return null;
//            throw new Exception("No classifier available.");
            }
            // Convert message string into instance.
            Instance instance = makeInstance(matrixTextModel);
            // Get index of predicted class value.
            double predicted = classifier.classifyInstance(instance);
            // Classify instance.
            String topic = data.classAttribute().value((int) predicted);
            System.out.println("Message classified as : " + topic);
            return topic;
        } catch (Exception ex) {
            Logger.getLogger(WekaClassifier.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
