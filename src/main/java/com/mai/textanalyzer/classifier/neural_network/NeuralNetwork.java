/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.classifier.neural_network;

import com.mai.textanalyzer.classifier.common.ClassifierEnum;
import com.mai.textanalyzer.classifier.weka_classifier.WekaClassifier;
import com.mai.textanalyzer.creater.Creater;
import com.mai.textanalyzer.csv.CSVUtils;
import com.mai.textanalyzer.csv.DataType;
import com.mai.textanalyzer.indexing.common.BasicTextModel;
import com.mai.textanalyzer.indexing.common.IndexerEnum;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.deeplearning4j.datasets.iterator.INDArrayDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.primitives.Pair;

/**
 *
 * @author Sergey
 */
public class NeuralNetwork {

    public static void main(String[] args) throws Exception {
        NeuralNetwork nn = new NeuralNetwork();
    }

    public NeuralNetwork() throws Exception {

        // PLEASE NOTE: For CUDA FP16 precision support is available
        // Nd4j.setDataType(DataBuffer.Type.HALF);
//        Nd4jBackend.BACKEND_PRIORITY_GPU 
        // temp workaround for backend initialization
//        CudaEnvironment.getInstance().getConfiguration()
//                // key option enabled
//                .allowMultiGPU(true)
//                .setMaximumGridSize(512)
//                .setMaximumBlockSize(512)
//                // we're allowing larger memory caches
//                .setMaximumDeviceCacheableLength(1024 * 1024 * 1024L)
//                .setMaximumDeviceCache(6L * 1024 * 1024 * 1024L)
//                .setMaximumHostCacheableLength(1024 * 1024 * 1024L)
//                .setMaximumHostCache(6L * 1024 * 1024 * 1024L)
//                // cross-device access is used for faster model averaging over pcie
//                .allowCrossDeviceAccess(true);
        int seed = 123456;
        double learningRate = 0.001;// was .01 but often got errors: "o.d.optimize.solvers.BaseOptimizer - Hit termination condition on iteration 0"
        int nEpochs = 500;
        int numInputs = 100;
//        int numInputs = 315;
        int numOutputs = 105;//105;
        int numHiddenNodes = 250;//

        int batchSize = 50;

        long st = System.currentTimeMillis();
        System.out.println("Preprocessing start time : " + st);

        File rootFolder = new File("E:\\DataForClassifier\\RootFolderSize62407");
//        File rootFolder = new File("E:\\DataForClassifier\\RootFolderSizeBalance");

//        List<WekaClassifier> classifierList = new ArrayList<>();
//        classifierList.add(Creater.loadWekaClassifier(rootFolder, ClassifierEnum.NAIVE_BAYES, IndexerEnum.DOC2VEC));
//        classifierList.add(Creater.loadWekaClassifier(rootFolder, ClassifierEnum.SVM, IndexerEnum.DOC2VEC));
//        classifierList.add(Creater.loadWekaClassifier(rootFolder, ClassifierEnum.RF, IndexerEnum.DOC2VEC));
        List<BasicTextModel> learningData = CSVUtils.readCSVData(CSVUtils.getDataCSVFile(rootFolder, IndexerEnum.DOC2VEC, DataType.LEARNING));
        Map<String, Integer> allowTopicList = new HashMap<>();
        for (BasicTextModel textModel : learningData) {
            Integer count = allowTopicList.get(textModel.getTopic());
            if (count == null) {
                allowTopicList.put(textModel.getTopic(), 600);
            }
        }
        List<String> labelsSource = new ArrayList<>(allowTopicList.keySet());
        Collections.sort(labelsSource);

        List<Pair<INDArray, INDArray>> trainData = new ArrayList();
        boolean yeatAdd = true;
        do {
            for (BasicTextModel textModel : learningData) {
                String key = textModel.getTopic();
                Integer allowSize = allowTopicList.get(key);
                if (allowSize != null && allowSize > 0) {
//                    trainData.add(new Pair(getINDArrayFromClassifier(classifierList, textModel.getiNDArray()), getINDArrayLabel(labelsSource, textModel.getTopic())));
                    trainData.add(new Pair(textModel.getiNDArray(), getINDArrayLabel(labelsSource, textModel.getTopic())));
                    if (allowSize <= 1) {
                        allowTopicList.remove(key);
                    } else {
                        allowSize--;
                        allowTopicList.put(key, allowSize);
                    }
                } else {
                    allowTopicList.remove(key);
                }
            }
            if (allowTopicList.isEmpty()) {
                yeatAdd = false;
            }
        } while (yeatAdd);

        Collections.sort(labelsSource);
        Collections.shuffle(learningData, new Random(123));

        DataSetIterator trainIter = new INDArrayDataSetIterator(trainData, batchSize);

        List<BasicTextModel> testModelList = CSVUtils.readCSVData(CSVUtils.getDataCSVFile(rootFolder, IndexerEnum.DOC2VEC, DataType.TEST));
        Collections.shuffle(testModelList, new Random(123));
        List<Pair<INDArray, INDArray>> testData = new ArrayList();
        for (BasicTextModel textModel : testModelList) {
//            testData.add(new Pair(getINDArrayFromClassifier(classifierList, textModel.getiNDArray()), getINDArrayLabel(labelsSource, textModel.getTopic())));
            testData.add(new Pair(textModel.getiNDArray(), getINDArrayLabel(labelsSource, textModel.getTopic())));
        }

        DataSetIterator testIter = new INDArrayDataSetIterator(testData, batchSize);

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .learningRate(learningRate)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                //                .updater(Updater.NESTEROVS) //To configure: .updater(new Nesterovs(0.9))
                .list()
                .layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(numHiddenNodes)
                        .weightInit(WeightInit.RELU)
                        .activation(Activation.LEAKYRELU)
                        .updater(Updater.ADAM)
                        .build())
                .layer(1, new DenseLayer.Builder().nIn(numHiddenNodes).nOut(numHiddenNodes)
                        .weightInit(WeightInit.RELU)
                        .activation(Activation.LEAKYRELU)
                        .updater(Updater.ADAM)
                        .build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.L2)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER)
                        .updater(Updater.ADAM)
                        .nIn(numHiddenNodes).nOut(numOutputs).build())
                .pretrain(false).backprop(true).build();
        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();
        OutputStream os = null;
//        try {

//            os = new FileOutputStream(new File(Creater.getSaveModelFolder(rootFolder), "neuronNet" + "Log.txt"));
        String info = "";
        for (int n = 0; n < nEpochs; n++) {
            System.out.println("Start " + n + " epoch");
            net.fit(trainIter);
            if (n % 2 == 0) {
                Evaluation eval = new Evaluation(labelsSource, 4);
                while (testIter.hasNext()) {
                    DataSet t = testIter.next();
                    INDArray features = t.getFeatureMatrix();
                    INDArray lables = t.getLabels();
                    INDArray predicted = net.output(features, false);
                    System.out.println(predicted);
                    eval.eval(lables, predicted);
                }
                testIter.reset();
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" + eval.stats());
//                    info = n + " epohe: " + eval.stats() + "\n";
//                    os.write(info.getBytes(), 0, info.length());
            }
        }
//        } catch (FileNotFoundException ex) {
//
//        }

//        //Save the model
//        File locationToSave = new File("E:\\DataForClassifier\\RootFolderSize200\\SaveModel\\MyMultiLayerNetwork.zip");      //Where to save the network. Note: the file is in .zip format - can be opened externally
//        ModelSerializer.writeModel(model, locationToSave, false);
//
//        //Load the model
//        MultiLayerNetwork restored = ModelSerializer.restoreMultiLayerNetwork(locationToSave);
    }

    public static INDArray getINDArrayLabel(List<String> labels, String label) {
        double[] features = new double[labels.size()];
        int index = labels.indexOf(label);
        if (index < 0) {
            System.out.println(label);
        }
        features[index] = 1;
        return Nd4j.create(features);
    }

    public static INDArray getINDArrayFromClassifier(List<WekaClassifier> classifierList, INDArray data) {
        Iterator<WekaClassifier> it = classifierList.iterator();
        INDArray result = it.next().getDistributionAsINDArray(data);
        while (it.hasNext()) {
            result = Nd4j.hstack(result, it.next().getDistributionAsINDArray(data));
        }
        return result;
    }

}
