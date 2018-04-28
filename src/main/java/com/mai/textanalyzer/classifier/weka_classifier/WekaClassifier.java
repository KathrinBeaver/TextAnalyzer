/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.classifier.weka_classifier;

import com.mai.textanalyzer.classifier.common.Prediction;
import com.mai.textanalyzer.classifier.common.TextClassifier;
import com.mai.textanalyzer.indexing.common.BasicTextModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nd4j.linalg.api.iter.NdIndexIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;

/**
 *
 * @author Sergey
 */
public class WekaClassifier implements Serializable, TextClassifier {

    /* The training data. */
    private Instances data = null;
    /* The classifier. */
    private final AbstractClassifier classifier;

    private final List<String> topics;

    public WekaClassifier(AbstractClassifier classifier, int iNDArrayLength, List<String> topics) {
        this.classifier = classifier;
        this.topics = topics;
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

    }

    public void buildClassifier() {
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

        double[] atr = new double[iNDArray.length() + 1];
        NdIndexIterator iter2 = new NdIndexIterator(iNDArray.shape());
        int counter = 0;
        while (iter2.hasNext()) {
            int[] nextIndex = iter2.next();
            atr[counter] = iNDArray.getDouble(nextIndex);
            counter++;
        }
        Instance instance = new SparseInstance(1, atr);
        instance.setDataset(data);
        return instance;
    }

    /**
     * Classifies a given message.
     *
     * @param matrixTextModel
     * @return
     */
    @Override
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
//            System.out.println("Message classified as : " + topic);
            return topic;
        } catch (Exception ex) {
            Logger.getLogger(WekaClassifier.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Prediction> getDistribution(INDArray matrixTextModel) {
        try {
            if (data.numInstances() == 0) {
                return null;
            }
            Instance instance = makeInstance(matrixTextModel);
            List<Prediction> predictions = new ArrayList<>();
            double[] predictValue = classifier.distributionForInstance(instance);
            int count = 0;
            for (String topic : topics) {
                predictions.add(new Prediction(topic, predictValue[count]));
                count++;
            }
            return predictions;
        } catch (Exception ex) {
            Logger.getLogger(WekaClassifier.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<String> getTopicList() {
        return new ArrayList<>(topics);
    }
}
