/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.tester;

import com.mai.textanalyzer.classifier.common.TextClassifier;
import com.mai.textanalyzer.classifier.common.ClassifierEnum;
import com.mai.textanalyzer.classifier.neural_network.NeuralNetwork;
import com.mai.textanalyzer.creater.Creater;
import com.mai.textanalyzer.indexing.common.Indexer;
import com.mai.textanalyzer.indexing.common.IndexerEnum;
import com.mai.textanalyzer.word_processing.RusUTF8FileLabelAwareIterator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.log4j.Logger;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.deeplearning4j.text.documentiterator.LabelsSource;
import org.nd4j.linalg.api.ndarray.INDArray;
import com.mai.textanalyzer.creater.SaveModelException;
import com.mai.textanalyzer.csv.CSVUtils;
import com.mai.textanalyzer.csv.DataType;
import com.mai.textanalyzer.dao.accuracy.Accuracy;
import com.mai.textanalyzer.dao.accuracy.IAccuracyDao;
import com.mai.textanalyzer.dao.accuracy.IAccuracyService;
import com.mai.textanalyzer.dao.common.ApplicationContextHolder;
import com.mai.textanalyzer.indexing.common.BasicTextModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Sergey
 */
public class Tester {

    private static final Logger log = Logger.getLogger(Tester.class);

    public static void main(String[] args) {
        ApplicationContextHolder.initializeApplicationContext();

//        IAccuracyDao iAccuracyDao = ApplicationContextHolder.getApplicationContext().getBean(IAccuracyDao.class);
//        iAccuracyDao.deleteAllDataFromAccuracy();
        File rootFolder1 = new File("E:\\DataForClassifier\\RootFolderSize62407");
        File rootFolder2 = new File("E:\\DataForClassifier\\RootFolderSizeBalance");
        List<File> files = new ArrayList<>();
        files.add(rootFolder1);
        files.add(rootFolder2);
        for (File rootFolder : files) {

//        CSVUtils.createCSVData(rootFolder, IndexerEnum.DOC2VEC, DataType.LEARNING);
//        CSVUtils.createCSVData(rootFolder, IndexerEnum.DOC2VEC, DataType.TEST);
//
//        createModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.NAIVE_BAYES, true);
            //         testModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.NAIVE_BAYES, false, true);
//        createModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.IBK, true);
            //           testModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.IBK, false, true);
//        createModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.LR, true);
//            testModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.LR, false, true);
//        createModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.RF, true);
            //          testModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.RF, false, true);
//        createModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.SVM, true);
            //           testModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.SVM, false, true);
//        createModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.BAGGING, true);
//            testModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.BAGGING, false, true);
//        createModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.BOOSTING, true);
//            testModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.BOOSTING, false, true);
            testModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.MYLTI_CLASSIFIER, false, true);
        }
    }

    private static void createModel(File rootFolder, IndexerEnum indexerEnum, ClassifierEnum classifierEnum, boolean useCSV) {
        OutputStream os = null;
        try {
            log.info("Start create " + indexerEnum);
            os = new FileOutputStream(new File(Creater.getSaveModelFolder(rootFolder), "Create" + ClassifierEnum.getFullNameModel(classifierEnum, indexerEnum) + "Log.txt"));
            Long curTime = System.currentTimeMillis();
            String info;
            try {
                Creater.createAndSaveIndexer(indexerEnum, rootFolder);
                info = "Create time for " + indexerEnum + ": " + ((double) (System.currentTimeMillis() - curTime) / 1000) + " c.\n";
            } catch (SaveModelException ignore) {
                info = "Сохраненая модель " + indexerEnum + " уже существует\n";
            }
            log.info(info);
            os.write(info.getBytes(), 0, info.length());
            curTime = System.currentTimeMillis();
            log.info("Start create " + classifierEnum);
            try {
                Creater.createAndSaveClassifier(rootFolder, classifierEnum, indexerEnum, useCSV);
                info = "Create time for " + classifierEnum + ": " + ((double) (System.currentTimeMillis() - curTime) / 1000) + " c.";
            } catch (SaveModelException ignore) {
                info = "Сохраненая модель " + classifierEnum + " уже существует\n";
            }
            log.info(info);
            os.write(info.getBytes(), 0, info.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void testModel(File rootFolder, IndexerEnum indexerEnum, ClassifierEnum classifierEnum, boolean updateInfoInDB, boolean useCSV) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File(Creater.getSaveModelFolder(rootFolder), ClassifierEnum.getFullNameModel(classifierEnum, indexerEnum) + "Log.txt"));
            String info = "Test " + ClassifierEnum.getFullNameModel(classifierEnum, indexerEnum);
            log.info(info);
            os.write(info.getBytes(), 0, info.length());
            TextClassifier wc = Creater.loadClassifier(rootFolder, classifierEnum, indexerEnum);
            List<String> labelsSource = wc.getTopicList();
            Evaluation eval = new Evaluation(labelsSource, 4);
            if (useCSV) {
                List<BasicTextModel> dataList = CSVUtils.readCSVData(CSVUtils.getDataCSVFile(rootFolder, indexerEnum, DataType.TEST));
//                int size = dataList.size();
//                int count = 0;
                for (BasicTextModel textModel : dataList) {
//                    String predict = wc.classifyMessage(textModel.getiNDArray());
//                    count++;
//                    System.out.println(count + "/" + size + ": " + textModel.getTopic() + " - " + predict);
                    eval.eval(NeuralNetwork.getINDArrayLabel(labelsSource, textModel.getTopic()), wc.getDistributionAsINDArray(textModel.getiNDArray()));
                }
            } else {
                RusUTF8FileLabelAwareIterator testingIteratorTest = new RusUTF8FileLabelAwareIterator.Builder()
                        .addSourceFolder(Creater.getDocForTestFolder(rootFolder))
                        .build();
                Indexer indexer = Creater.loadIndexer(indexerEnum, rootFolder);
                int size = testingIteratorTest.getSize();
                int count = 0;
                while (testingIteratorTest.hasNext()) {
                    LabelledDocument next = testingIteratorTest.next();
                    INDArray matrixTextModel = indexer.getIndex(next.getContent());
                    String predict = wc.classifyMessage(matrixTextModel);
                    String topic = next.getLabel();
                    count++;
                    System.out.println(count + "/" + size + ": " + topic + " - " + predict);
                    eval.eval(NeuralNetwork.getINDArrayLabel(labelsSource, topic), wc.getDistributionAsINDArray(matrixTextModel));
                }
            }
            info = eval.stats(true) + "\n";
            log.info(info);
            os.write(info.getBytes(), 0, info.length());

            RusUTF8FileLabelAwareIterator learningIteratorTest = null;
            if (updateInfoInDB) {
                learningIteratorTest = new RusUTF8FileLabelAwareIterator.Builder()
                        .addSourceFolder(Creater.getDocForLearningFolder(rootFolder))
                        .build();
            }
            int count = 0;
            for (String label : labelsSource) {
                double accuracy = eval.f1(labelsSource.indexOf(label));
                if (updateInfoInDB) {
                    ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
                    IAccuracyService accuracyService = applicationContext.getBean(IAccuracyService.class);
                    accuracyService.inserOrUpdateAccyracy(new Accuracy(classifierEnum, indexerEnum, label, accuracy, learningIteratorTest.getDocumentsSize(label)));
                }
                info = count + "." + label + ": " + accuracy + "\n";
                count++;
                log.info(info);
                os.write(info.getBytes(), 0, info.length());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
