/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.tester;

import com.mai.textanalyzer.classifier.common.TextClassifier;
import com.mai.textanalyzer.classifier.common.ClassifierEnum;
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
import com.mai.textanalyzer.dao.accuracy.Accuracy;
import com.mai.textanalyzer.dao.accuracy.IAccuracyDao;
import com.mai.textanalyzer.dao.accuracy.IAccuracyService;
import com.mai.textanalyzer.dao.common.ApplicationContextHolder;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Sergey
 */
public class Tester {

    private static final Logger log = Logger.getLogger(Tester.class);

    public static void main(String[] args) {
        ApplicationContextHolder.initializeApplicationContext();

        IAccuracyDao iAccuracyDao = ApplicationContextHolder.getApplicationContext().getBean(IAccuracyDao.class);
//        iAccuracyDao.deleteAllDataFromAccuracy();

        File rootFolder = new File("E:\\DataForClassifier\\RootFolderSize62407");
        createModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.NAIVE_BAYES);
        testModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.NAIVE_BAYES, true);

        createModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.IBK);
        testModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.IBK, true);

        createModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.SVM);
        testModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.SVM, true);

        createModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.LR);
        testModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.LR, true);

        createModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.RF);
        testModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.RF, true);

        testModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.MYLTI_CLASSIFIER, false);
    }

    private static void createModel(File rootFolder, IndexerEnum indexerEnum, ClassifierEnum classifierEnum) {
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
                Creater.createAndSaveClassifier(rootFolder, classifierEnum, indexerEnum);
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

    private static void testModel(File rootFolder, IndexerEnum indexerEnum, ClassifierEnum classifierEnum, boolean updateInfoInDB) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File(Creater.getSaveModelFolder(rootFolder), ClassifierEnum.getFullNameModel(classifierEnum, indexerEnum) + "Log.txt"));
            String info = "Test " + ClassifierEnum.getFullNameModel(classifierEnum, indexerEnum);
            log.info(info);
            os.write(info.getBytes(), 0, info.length());
            Indexer indexer = Creater.loadIndexer(indexerEnum, rootFolder);
            info = "DimensionSize: " + indexer.getDimensionSize();
            log.info(info);
            os.write(info.getBytes(), 0, info.length());
            TextClassifier wc = Creater.loadClassifier(rootFolder, classifierEnum, indexerEnum);
            RusUTF8FileLabelAwareIterator testingIteratorTest = new RusUTF8FileLabelAwareIterator.Builder()
                    .addSourceFolder(Creater.getDocForTestFolder(rootFolder))
                    .build();
            LabelsSource labelsSource = testingIteratorTest.getLabelsSource();
            int size = testingIteratorTest.getSize();
            Evaluation eval = new Evaluation(labelsSource.size());
            int count = 0;
            while (testingIteratorTest.hasNext()) {
                LabelledDocument next = testingIteratorTest.next();
                INDArray matrixTextModel = indexer.getIndex(next.getContent());
                String predict = wc.classifyMessage(matrixTextModel);
                String topic = next.getLabel();
                count++;
                System.out.println(count + "/" + size + ": " + topic + " - " + predict);
                eval.eval(labelsSource.indexOf(predict), labelsSource.indexOf(topic));
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
            for (String label : labelsSource.getLabels()) {
                double accuracy = eval.f1(labelsSource.indexOf(label));
                if (updateInfoInDB) {
                    ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
                    IAccuracyService accuracyService = applicationContext.getBean(IAccuracyService.class);
                    accuracyService.inserOrUpdateAccyracy(new Accuracy(classifierEnum, indexerEnum, label, accuracy, learningIteratorTest.getDocumentsSize(label)));
                }
                info = label + ": " + accuracy + "\n";
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
