/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import com.mai.textanalyzer.indexing.tf_idf.TfIdfTextModel;
import java.util.List;
import org.nd4j.linalg.api.iter.NdIndexIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import weka.core.*;
import weka.classifiers.*;
import weka.classifiers.bayes.NaiveBayes;
/**
 *
 * @author Sergey
 */
public class TestWekaClassifier {

    /* The training data. */
    private Instances m_Data = null;
    /* The classifier. */
    private final Classifier m_Classifier = new NaiveBayes();

    public TestWekaClassifier(TfIdfTextModel modelExample, List<String> topics) {

        String nameOfDataset = "Classification";
        // Create numeric attributes.
        INDArray firstVectors = modelExample.getiNDArray();
        FastVector attributes = new FastVector(firstVectors.length() + 1);
        for (int i = 0; i < firstVectors.length(); i++) {
            attributes.addElement(new Attribute(String.valueOf(i)));
        }

        // Add class attribute.
        FastVector topicValues = new FastVector(topics.size());
        for (String topic : topics) {
            topicValues.addElement(topic);
        }

        attributes.addElement(new Attribute("topic", topicValues));
        // Create dataset with initial capacity of 100, and set index of class.
        m_Data = new Instances(nameOfDataset, attributes, 100);
        m_Data.setClassIndex(m_Data.numAttributes() - 1);

    }

    /**
     * Updates model using the given training message.
     *
     * @param textModel
     * @throws java.lang.Exception
     */
    public void updateModel(TfIdfTextModel textModel) throws Exception {
        // Convert message string into instance.
        Instance instance = makeInstance(textModel);
        m_Data.add(instance);
        Instances instances = new Instances(m_Data);
        m_Classifier.buildClassifier(instances);
    }

    /**
     * Method that converts a text message into an instance.
     */
    private Instance makeInstance(TfIdfTextModel textModel) {
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
        instance.setDataset(m_Data);
        return instance;
    }

    /**
     * Classifies a given message.
     *
     * @param matrixTextModel
     * @return
     * @throws java.lang.Exception
     */
    public String classifyMessage(INDArray matrixTextModel) throws Exception {
        // Check if classifier has been built.
        if (m_Data.numInstances() == 0) {
            throw new Exception("No classifier available.");
        }
        // Convert message string into instance.
        Instance instance = makeInstance(matrixTextModel);
        // Get index of predicted class value.
        double predicted = m_Classifier.classifyInstance(instance);
        // Classify instance.
        String topic = m_Data.classAttribute().value((int) predicted);
        System.out.println("Message classified as : " + topic);
        return topic;
    }

}
