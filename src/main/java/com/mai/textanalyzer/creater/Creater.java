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
import com.mai.textanalyzer.word_processing.RusUTF8FileLabelAwareIterator;
import java.io.File;
import java.util.List;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import weka.classifiers.AbstractClassifier;

/**
 *
 * @author Sergey
 */
public class Creater {

    public static int LAYER_SIZE = 300;

    public static void main(String[] args) {
        File folderWithDataForTest = new File("E:\\DocForTest\\MiniTest\\DataTest");
        File folderForSave = new File("E:\\DocForTest\\MiniTest\\SaveModel\\WekaClassifier\\WekaClassifierModel");

        WekaClassifier wc = WekaUtils.loadModel(folderForSave);

        RusUTF8FileLabelAwareIterator tearchingIteratorTest = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForTest)
                .build();

        File folderWithDoc2Vec = new File("E:\\DocForTest\\MiniTest\\SaveModel\\Doc2Vec\\Doc2VecModel");
        Doc2Vec paragraphVectors = Doc2VecUtils.loadModel(folderWithDoc2Vec);
        while (tearchingIteratorTest.hasNext()) {
            LabelledDocument next = tearchingIteratorTest.next();
            wc.classifyMessage(paragraphVectors.getIndex(next.getContent()));
        }
    }

    public static void createAndSaveDoc2Vec(File folderWithDataForLearning, File folderForSave) {
        Doc2VecUtils.saveModel(Doc2VecUtils.createModel(folderWithDataForLearning), folderForSave);
    }

    public static void createAndSaveTfIIdf(File folderWithDataForLearning, File folderForSave) {
        TfIIdfUtils.saveModel(TfIIdfUtils.createModel(folderWithDataForLearning), folderForSave);
    }

    public static void createAndSaveWekaClassifier(AbstractClassifier classifier, Indexer indexer, File folderWithDataForLearning, File folderForSave) {
        List<String> topics = IndexingUtils.getTopics(folderWithDataForLearning);
        WekaClassifier wc = new WekaClassifier(classifier, LAYER_SIZE, topics);
        RusUTF8FileLabelAwareIterator tearchingIterator = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForLearning)
                .build();
        while (tearchingIterator.hasNext()) {
            LabelledDocument next = tearchingIterator.next();
            BasicTextModel basicTextModel = new BasicTextModel(next.getLabels().iterator().next(), indexer.getIndex(next.getContent()));
            wc.updateModel(basicTextModel);
        }
        WekaUtils.saveModel(wc, folderForSave);
    }
}
