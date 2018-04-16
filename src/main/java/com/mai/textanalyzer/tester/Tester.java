/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.tester;

import com.mai.textanalyzer.classifier.weka_classifier.WekaClassifier;
import com.mai.textanalyzer.creater.ClassifierEnum;
import com.mai.textanalyzer.creater.Creater;
import com.mai.textanalyzer.indexing.common.Indexer;
import com.mai.textanalyzer.indexing.common.IndexerEnum;
import com.mai.textanalyzer.word_processing.RusUTF8FileLabelAwareIterator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.deeplearning4j.text.documentiterator.LabelsSource;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 *
 * @author Sergey
 */
public class Tester {

    private static final Logger log = Logger.getLogger(Tester.class);

    public static void main(String[] args) {
        List<File> testingRootFolders = new ArrayList<>();
        testingRootFolders.add(new File("E:\\DataForClassifier\\RootFolderSize200"));
//        testingRootFolders.add(new File("E:\\DataForClassifier\\RootFolderSize567"));

        testingRootFolders.forEach((rootFolder) -> {
            createModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.NAIVE_BAYES);
            testModel(rootFolder, IndexerEnum.DOC2VEC, ClassifierEnum.NAIVE_BAYES);
//            createModel(rootFolder, IndexerEnum.TF_IDF, ClassifierEnum.NAIVE_BAYES);
//            testModel(rootFolder, IndexerEnum.TF_IDF, ClassifierEnum.NAIVE_BAYES);
        });
    }

    private static void createModel(File rootFolder, IndexerEnum indexerEnum, ClassifierEnum classifierEnum) {
        OutputStream os = null;
        try {
            log.info("Start create " + indexerEnum);
            os = new FileOutputStream(new File(Creater.getSaveModelFolder(rootFolder), "Create" + ClassifierEnum.getFullNameModel(classifierEnum, indexerEnum) + "Log.txt"));
            Long curTime = System.currentTimeMillis();
            Creater.createAndSaveIndexer(indexerEnum, rootFolder);
            String info = "Create time for " + indexerEnum + ": " + ((double) (System.currentTimeMillis() - curTime) / 1000) + " c.\n";
            log.info(info);
            os.write(info.getBytes(), 0, info.length());
            curTime = System.currentTimeMillis();
            log.info("Start create " + classifierEnum);
            Creater.createAndSaveWekaClassifier(rootFolder, classifierEnum, indexerEnum);
            info = "Create time for " + classifierEnum + ": " + ((double) (System.currentTimeMillis() - curTime) / 1000) + " c.";
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

    private static void testModel(File rootFolder, IndexerEnum indexerEnum, ClassifierEnum classifierEnum) {
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
            WekaClassifier wc = Creater.loadWekaClassifier(rootFolder, classifierEnum, indexerEnum);
            RusUTF8FileLabelAwareIterator tearchingIteratorTest = new RusUTF8FileLabelAwareIterator.Builder()
                    .addSourceFolder(Creater.getDocForTestFolder(rootFolder))
                    .build();
            LabelsSource labelsSource = tearchingIteratorTest.getLabelsSource();
            int size = tearchingIteratorTest.getSize();
            Evaluation eval = new Evaluation(labelsSource.size());
            int count = 0;
            while (tearchingIteratorTest.hasNext()) {
                LabelledDocument next = tearchingIteratorTest.next();
                INDArray matrixTextModel = indexer.getIndex(next.getContent());
                String predict = wc.classifyMessage(matrixTextModel);
                String topic = next.getLabel();
                count++;
                System.out.println(count + "/" + size + ": " + topic + " - " + predict);
                eval.eval(labelsSource.indexOf(predict), labelsSource.indexOf(topic));
            }
            info = eval.stats(true);
            log.info(info);
            os.write(info.getBytes(), 0, info.length());
            System.out.println("\n");
            for (String label : labelsSource.getLabels()) {
                info = label + ": " + eval.f1(labelsSource.indexOf(label)) + "\n";
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
