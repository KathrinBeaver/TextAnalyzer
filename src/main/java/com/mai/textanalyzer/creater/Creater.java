/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.creater;

import com.mai.textanalyzer.classifier.weka_classifier.WekaClassifier;
import com.mai.textanalyzer.classifier.weka_classifier.WekaUtils;
import com.mai.textanalyzer.indexing.common.BasicTextModel;
import com.mai.textanalyzer.indexing.common.Indexer;
import com.mai.textanalyzer.indexing.common.IndexingUtils;
import com.mai.textanalyzer.indexing.doc2vec.Doc2Vec;
import com.mai.textanalyzer.indexing.doc2vec.Doc2VecUtils;
import com.mai.textanalyzer.indexing.tf_idf.TfIIdfUtils;
import com.mai.textanalyzer.indexing.tf_idf.TfIdf;
import com.mai.textanalyzer.word_processing.RusUTF8FileLabelAwareIterator;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.nd4j.linalg.api.ndarray.INDArray;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.lazy.IBk;

/**
 *
 * @author Sergey
 */
public class Creater {

    public static void main(String[] args) {
        File folderWithDataForLearning = new File("E:\\DocForTest\\DataForLearning");
        File folderWithtfIdf = new File("E:\\DocForTest\\SaveModel\\tfidf\\tfidfModel");
        File folderForSave = new File("E:\\DocForTest\\SaveModel\\NaiveBase\\NaiveBaseModel");

        TfIdf tfIdf = TfIIdfUtils.loadModel(folderWithtfIdf, folderWithDataForLearning);
//        createAndSaveWekaClassifier(new NaiveBayes(), tfIdf, folderWithDataForLearning, folderForSave);

        File folderWithDataForTest = new File("E:\\DocForTest\\DataForTest");
        RusUTF8FileLabelAwareIterator tearchingIteratorTest = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForTest)
                .build();
//        File folderForSave = new File("E:\\DocForTest\\MiniTest\\SaveModel\\WekaClassifier\\WekaClassifierModel");
//        File folderWithDoc2Vec = new File("E:\\DocForTest\\MiniTest\\SaveModel\\Doc2Vec\\Doc2VecModel");
        WekaClassifier wc = WekaUtils.loadModel(folderForSave);
//        Doc2Vec paragraphVectors = Doc2VecUtils.loadModel(folderWithDoc2Vec);
        int count = 0;
        while (tearchingIteratorTest.hasNext()) {
            LabelledDocument next = tearchingIteratorTest.next();
            INDArray matrixTextModel = tfIdf.getIndex(next.getContent());
            String predict = wc.classifyMessage(matrixTextModel);
            String topic = next.getLabel();
            if (topic.equals(predict)) {
                count++;
            }
            System.out.println(next.getLabel() + " - " + predict);
//            System.out.println(Arrays.toString(wc.getDistribution(matrixTextModel)));
        }
        System.out.println(count + "/" + tearchingIteratorTest.getSize());
    }

    public static void createAndSaveDoc2Vec(File folderWithDataForLearning, File folderForSave) {
        Doc2VecUtils.saveModel(Doc2VecUtils.createModel(folderWithDataForLearning), folderForSave);
    }

    public static void createAndSaveTfIIdf(File folderWithDataForLearning, File folderForSave) {
        TfIIdfUtils.saveModel(TfIIdfUtils.createModel(folderWithDataForLearning), folderForSave);
    }

    public static void createAndSaveWekaClassifier(AbstractClassifier classifier, Indexer indexer, File folderWithDataForLearning, File folderForSave) {
        List<String> topics = IndexingUtils.getTopics(folderWithDataForLearning);
        WekaClassifier wc = new WekaClassifier(classifier, indexer.getDimensionSize(), topics);

        RusUTF8FileLabelAwareIterator tearchingIterator = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForLearning)
                .build();
        int size = tearchingIterator.getSize();
        int count = 0;
        while (tearchingIterator.hasNext()) {
            LabelledDocument next = tearchingIterator.next();
            BasicTextModel basicTextModel = new BasicTextModel(next.getLabels().iterator().next(), indexer.getIndex(next.getContent()));
            wc.updateModel(basicTextModel);
            count++;
            System.out.println(count + "/" + size);
        }
        wc.buildClassifier();
        WekaUtils.saveModel(wc, folderForSave);
    }
}
