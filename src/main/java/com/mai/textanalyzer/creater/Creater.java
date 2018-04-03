/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.creater;

import com.mai.textanalyzer.classifier.weka_classifier.WekaClassifier;
import com.mai.textanalyzer.classifier.weka_classifier.WekaUtils;
import com.mai.textanalyzer.indexing.common.BasicTextModel;
import com.mai.textanalyzer.indexing.doc2vec.Doc2VecUtils;
import com.mai.textanalyzer.indexing.tf_idf.TfIIdfUtils;
import com.mai.textanalyzer.word_processing.RusUTF8FileLabelAwareIterator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import weka.classifiers.bayes.NaiveBayes;

/**
 *
 * @author Sergey
 */
public class Creater {

    public static void main(String[] args) {
        File folderWithDataForTest = new File("E:\\DocForTest\\MiniTest\\DataTest");
        File folderForSave = new File("E:\\DocForTest\\MiniTest\\SaveModel\\WekaClassifier\\WekaClassifierModel");

        WekaClassifier wc = WekaUtils.loadModel(folderForSave);

        RusUTF8FileLabelAwareIterator tearchingIteratorTest = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForTest)
                .build();

        File folderWithDoc2Vec = new File("E:\\DocForTest\\MiniTest\\SaveModel\\Doc2Vec\\Doc2VecModel");
        ParagraphVectors paragraphVectors = Doc2VecUtils.loadModel(folderWithDoc2Vec);
        while (tearchingIteratorTest.hasNext()) {
            LabelledDocument next = tearchingIteratorTest.next();
            wc.classifyMessage(paragraphVectors.inferVector(next.getContent()));
        }
    }

    public static void createAndSaveDoc2Vec(File folderWithDataForLearning, File folderForSave) {
        Doc2VecUtils.saveModel(Doc2VecUtils.createModel(folderWithDataForLearning), folderForSave);
    }

    public static void createAndSaveTfIIdf(File folderWithDataForLearning, File folderForSave) {
        TfIIdfUtils.saveModel(TfIIdfUtils.createModel(folderWithDataForLearning), folderForSave);
    }

    public static void createAndSaveWekaClassifier(File folderWithDataForLearning, File folderForSave) {
        List<String> topics = new ArrayList<>();
        topics.add("Астрономия");
        topics.add("Биология");

        WekaClassifier wc = new WekaClassifier(new NaiveBayes(), 100, topics);

        File folderWithDoc2Vec = new File("E:\\DocForTest\\MiniTest\\SaveModel\\Doc2Vec\\Doc2VecModel");
        ParagraphVectors paragraphVectors = Doc2VecUtils.loadModel(folderWithDoc2Vec);
        RusUTF8FileLabelAwareIterator tearchingIterator = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForLearning)
                .build();
        while (tearchingIterator.hasNext()) {
            LabelledDocument next = tearchingIterator.next();
            BasicTextModel basicTextModel = new BasicTextModel(next.getLabels().iterator().next(), paragraphVectors.inferVector(next.getContent()));
            wc.updateModel(basicTextModel);
        }
        File folderWithDataForTest = new File("E:\\DocForTest\\MiniTest\\DataTest");
        RusUTF8FileLabelAwareIterator tearchingIteratorTest = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForTest)
                .build();

        while (tearchingIteratorTest.hasNext()) {
            LabelledDocument next = tearchingIteratorTest.next();
            wc.classifyMessage(paragraphVectors.inferVector(next.getContent()));
        }

        WekaUtils.saveModel(wc, folderForSave);

//        paragraphVectors.inferVector(text);
//        WekaClassifier
//        WekaUtils.
//        TfIIdfUtils.saveModel(TfIIdfUtils.createModel(folderWithDataForLearning), folderForSave);
    }
}
