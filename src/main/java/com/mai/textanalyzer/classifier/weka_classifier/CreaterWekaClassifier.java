/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.classifier.weka_classifier;

import com.mai.textanalyzer.classifier.common.ClassifierEnum;
import static com.mai.textanalyzer.creater.Creater.getDocForLearningFolder;
import static com.mai.textanalyzer.creater.Creater.getSaveModelFolder;
import static com.mai.textanalyzer.csv.CSVUtils.getDataCSVFile;
import com.mai.textanalyzer.csv.DataType;
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
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.Stacking;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author Sergey
 */
public class CreaterWekaClassifier {

    public static WekaClassifier getClassifier(ClassifierEnum classifier, Indexer indexer, File rootFolder, boolean useCSV) {
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
        } else if (classifier == ClassifierEnum.LR) {
            abstractClassifier = new SimpleLogistic();
        } else if (classifier == ClassifierEnum.RF) {
            abstractClassifier = new J48();
        } else if (classifier == ClassifierEnum.BAGGING) {
            Bagging bagging = new Bagging();
            bagging.setClassifier(new SMO());
            abstractClassifier = bagging;
        } else if (classifier == ClassifierEnum.BOOSTING) {
            AdaBoostM1 adaBoost = new AdaBoostM1();
            adaBoost.setClassifier(new SMO());
            abstractClassifier = adaBoost;
        } else if (classifier == ClassifierEnum.STACKING) {
            WekaClassifier smo = WekaUtils.loadModel(new File(getSaveModelFolder(rootFolder), ClassifierEnum.getFullNameModel(ClassifierEnum.SVM, indexer.getIndexerEnum())));
            WekaClassifier nb = WekaUtils.loadModel(new File(getSaveModelFolder(rootFolder), ClassifierEnum.getFullNameModel(ClassifierEnum.NAIVE_BAYES, indexer.getIndexerEnum())));
            WekaClassifier iBk = WekaUtils.loadModel(new File(getSaveModelFolder(rootFolder), ClassifierEnum.getFullNameModel(ClassifierEnum.IBK, indexer.getIndexerEnum())));
            WekaClassifier j48tree = WekaUtils.loadModel(new File(getSaveModelFolder(rootFolder), ClassifierEnum.getFullNameModel(ClassifierEnum.IBK, indexer.getIndexerEnum())));
            Classifier[] stackoptions = new Classifier[]{smo.getClassifier(), nb.getClassifier(), iBk.getClassifier(), j48tree.getClassifier()};
            Stacking stacking = new Stacking();
            stacking.setClassifiers(stackoptions);
            abstractClassifier = stacking;
        } else {
            throw new UnsupportedOperationException("Classifier support for" + classifier.name() + " not yet added");
        }
        File folderWithDataForLearning = getDocForLearningFolder(rootFolder);
        List<String> topics = IndexingUtils.getTopics(folderWithDataForLearning);
        if (useCSV) {
            try {
                Instances data = DataSource.read(getDataCSVFile(rootFolder, indexer.getIndexerEnum(), DataType.LEARNING).getPath());
                System.out.println("Instances from csv loadeds");
                if (data.classIndex() == -1) {
                    data.setClassIndex(data.numAttributes() - 1);
                }
                return new WekaClassifier(classifier, abstractClassifier, data, topics);
            } catch (Exception ex) {
                Logger.getLogger(CreaterWekaClassifier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        WekaClassifier wc = new WekaClassifier(classifier, abstractClassifier, indexer.getDimensionSize(), topics);
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
