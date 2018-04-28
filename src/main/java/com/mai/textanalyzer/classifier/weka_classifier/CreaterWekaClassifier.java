/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.classifier.weka_classifier;

import com.mai.textanalyzer.creater.*;
import com.mai.textanalyzer.classifier.common.ClassifierEnum;
import static com.mai.textanalyzer.creater.Creater.getDocForLearningFolder;
import com.mai.textanalyzer.indexing.common.BasicTextModel;
import com.mai.textanalyzer.indexing.common.Indexer;
import com.mai.textanalyzer.indexing.common.IndexingUtils;
import com.mai.textanalyzer.word_processing.RusUTF8FileLabelAwareIterator;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;

/**
 *
 * @author Sergey
 */
public class CreaterWekaClassifier {

    public static WekaClassifier getClassifier(ClassifierEnum classifier, Indexer indexer, File rootFolder) {
        AbstractClassifier abstractClassifier;
        if (classifier == ClassifierEnum.NAIVE_BAYES) {
            abstractClassifier = new NaiveBayes();
        } else if (classifier == ClassifierEnum.SVM) {
            abstractClassifier = new SMO();
        } else if (classifier == ClassifierEnum.IBK) {
            IBk iBk = new IBk();
            try {
                iBk.setOptions(new String[]{"-K", "10"});
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            abstractClassifier = iBk;
        } else {
            throw new UnsupportedOperationException("Classifier support for" + classifier.name() + " not yet added");
        }
        File folderWithDataForLearning = getDocForLearningFolder(rootFolder);
        List<String> topics = IndexingUtils.getTopics(folderWithDataForLearning);

        WekaClassifier wc = new WekaClassifier(abstractClassifier, indexer.getDimensionSize(), topics);
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

        return wc;

    }

}
