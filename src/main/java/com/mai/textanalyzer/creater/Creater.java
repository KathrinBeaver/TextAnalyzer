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
import com.mai.textanalyzer.indexing.common.IndexerEnum;
import static com.mai.textanalyzer.indexing.common.IndexerEnum.DOC2VEC;
import static com.mai.textanalyzer.indexing.common.IndexerEnum.TF_IDF;
import com.mai.textanalyzer.indexing.common.IndexingUtils;
import com.mai.textanalyzer.indexing.doc2vec.Doc2Vec;
import com.mai.textanalyzer.indexing.doc2vec.Doc2VecUtils;
import com.mai.textanalyzer.indexing.tf_idf.TfIIdfUtils;
import com.mai.textanalyzer.indexing.tf_idf.TfIdf;
import com.mai.textanalyzer.word_processing.RusUTF8FileLabelAwareIterator;
import java.io.File;
import java.util.List;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.deeplearning4j.text.documentiterator.LabelsSource;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * утилиарный класс для удобного создания класфикаторов. Необхоима указыать
 * корневую папку. Корневая папка должна обзательно включать 3 каталога:
 * DocForLearning, DocForTest, SaveModel
 *
 *
 * @author Sergey
 */
public final class Creater {

    private final static String DOC_FOR_LEARNING_FOLDER = "DocForLearning"; // Папка с данными для обучения
    private final static String DOC_FOR_TEST_FOLDER = "DocForTest";//Папка с данными для тестирования
    private final static String SAVE_MODEL_FOLDER = "SaveModel";//Папка в которую модель будет сохранена после обучения

    private Creater() {
    }

    public static void main(String[] args) {
        File folderWithDataForLearning = new File("E:\\DocForTest\\DataForLearning");
        File folderWithtfIdf = new File("E:\\DocForTest\\SaveModel\\tfidf\\tfidfModel");
//        File folderForSave = new File("E:\\SaveModel\\TFIDF\\TfIdfModel");
//        createAndSaveTfIIdf(folderWithDataForLearning, folderForSave);
        TfIdf tfIdf = TfIIdfUtils.loadModel(folderWithtfIdf, folderWithDataForLearning);
//        createAndSaveWekaClassifier(new NaiveBayes(), tfIdf, folderWithDataForLearning, folderForSave);
        File folderWithDataForTest = new File("E:\\DocForTest\\DataForTest");
        RusUTF8FileLabelAwareIterator tearchingIteratorTest = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForTest)
                .build();
        File folderForSave = new File("E:\\DocForTest\\SaveModel\\NaiveBase\\NaiveBaseModel");
//        File folderWithDoc2Vec = new File("E:\\DocForTest\\MiniTest\\SaveModel\\Doc2Vec\\Doc2VecModel");
        WekaClassifier wc = WekaUtils.loadModel(folderForSave);
//        Doc2Vec paragraphVectors = Doc2VecUtils.loadModel(folderWithDoc2Vec);
        LabelsSource labelsSource = tearchingIteratorTest.getLabelsSource();
        Evaluation eval = new Evaluation(labelsSource.size());
        int count = 0;
        while (tearchingIteratorTest.hasNext()) {
            LabelledDocument next = tearchingIteratorTest.next();
            INDArray matrixTextModel = tfIdf.getIndex(next.getContent());
            String predict = wc.classifyMessage(matrixTextModel);
            String topic = next.getLabel();
            count++;
            System.out.println(count + ": " + topic + " - " + predict);
            eval.eval(labelsSource.indexOf(predict), labelsSource.indexOf(topic));
//            System.out.println(Arrays.toString(wc.getDistribution(matrixTextModel)));
        }
        System.out.println(eval.stats(true));
        for (String label : labelsSource.getLabels()) {
            System.out.println(label + " ------------------");
            eval.f1(labelsSource.indexOf(label));
        }
    }

    public static File getDocForLearningFolder(File rootDir) {
        return new File(rootDir, DOC_FOR_LEARNING_FOLDER);
    }

    public static File getDocForTestFolder(File rootDir) {
        return new File(rootDir, DOC_FOR_TEST_FOLDER);
    }

    public static File getSaveModelFolder(File rootDir) {
        return new File(rootDir, SAVE_MODEL_FOLDER);
    }

    private static Indexer createAndSaveDoc2Vec(File rootFolder) {
        String modelName = DOC2VEC.getModelName();
        checkRootFolderStracture(rootFolder, modelName, true);
        File folderWithDataForLearning = getDocForLearningFolder(rootFolder);
        File folderForSave = new File(getSaveModelFolder(rootFolder), modelName);
        Doc2Vec doc2Vec = Doc2VecUtils.createModel(folderWithDataForLearning);
        Doc2VecUtils.saveModel(doc2Vec, folderForSave);
        return doc2Vec;
    }

    private static Indexer createAndSaveTfIIdf(File rootFolder) {
        String modelName = TF_IDF.getModelName();
        checkRootFolderStracture(rootFolder, modelName, true);
        File folderWithDataForLearning = getDocForLearningFolder(rootFolder);
        File folderForSave = new File(getSaveModelFolder(rootFolder), modelName);
        TfIdf tfIdf = TfIIdfUtils.createModel(folderWithDataForLearning);
        TfIIdfUtils.saveModel(tfIdf, folderForSave);
        return tfIdf;
    }

    public static Indexer createAndSaveIndexer(IndexerEnum indexingEnum, File rootDir) {
        if (indexingEnum == DOC2VEC) {
            return createAndSaveDoc2Vec(rootDir);
        } else if (indexingEnum == TF_IDF) {
            return createAndSaveTfIIdf(rootDir);
        }
        throw new UnsupportedOperationException("Support " + indexingEnum + " yet not adding");
    }

    private static Indexer loadDoc2Vec(File rootFolder) {
        String modelName = DOC2VEC.getModelName();
        checkRootFolderStracture(rootFolder, modelName, false);
        return Doc2VecUtils.loadModel(new File(getSaveModelFolder(rootFolder), modelName));
    }

    private static Indexer loadTfIIdf(File rootFolder) {
        String modelName = TF_IDF.getModelName();
        checkRootFolderStracture(rootFolder, modelName, false);
        File folderWithDataForLearning = getDocForLearningFolder(rootFolder);
        File folderForSave = new File(getSaveModelFolder(rootFolder), modelName);
        return TfIIdfUtils.loadModel(folderForSave, folderWithDataForLearning);
        //TODO убрать папку для обучения 
    }

    public static Indexer loadIndexer(IndexerEnum indexingEnum, File rootDir) {
        if (indexingEnum == DOC2VEC) {
            return loadDoc2Vec(rootDir);
        } else if (indexingEnum == TF_IDF) {
            return loadTfIIdf(rootDir);
        }
        throw new UnsupportedOperationException("Support " + indexingEnum + " yet not adding");
    }

    public static void createAndSaveWekaClassifier(File rootFolder, ClassifierEnum classifier, IndexerEnum indexingEnum) {
        String modelName = ClassifierEnum.getFullNameModel(classifier, indexingEnum);
        checkRootFolderStracture(rootFolder, modelName, true);
        File folderWithDataForLearning = getDocForLearningFolder(rootFolder);
        List<String> topics = IndexingUtils.getTopics(folderWithDataForLearning);
        Indexer indexer = loadIndexer(indexingEnum, rootFolder);
        WekaClassifier wc = new WekaClassifier(CreaterClassifier.getClassifier(classifier), indexer.getDimensionSize(), topics);

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
        File folderForSave = new File(getSaveModelFolder(rootFolder), modelName);
        WekaUtils.saveModel(wc, folderForSave);
    }

    public static WekaClassifier loadWekaClassifier(File rootFolder, ClassifierEnum classifier, IndexerEnum indexingEnum) {
        String modelName = ClassifierEnum.getFullNameModel(classifier, indexingEnum);
        checkRootFolderStracture(rootFolder, modelName, false);
        return WekaUtils.loadModel(new File(getSaveModelFolder(rootFolder), modelName));
    }

    /**
     *
     *
     * @param rootFolder
     * @param modelName
     * @param forSave true - if save model, false - if load model
     */
    private static void checkRootFolderStracture(File rootFolder, String modelName, boolean forSave) {
        File checkingFile = getDocForLearningFolder(rootFolder);
        if (!checkingFile.exists() || !checkingFile.isDirectory()) {
            throw new RuntimeException("Не удалось найти: " + checkingFile.getPath());
        }
        checkingFile = getDocForTestFolder(rootFolder);
        if (!checkingFile.exists() || !checkingFile.isDirectory()) {
            throw new RuntimeException("Не удалось найти: " + checkingFile.getPath());
        }
        checkingFile = getSaveModelFolder(rootFolder);
        if (!checkingFile.exists() || !checkingFile.isDirectory()) {
            throw new RuntimeException("Не удалось найти: " + checkingFile.getPath());
        }
        File saveModel = new File(checkingFile, modelName);
        if (forSave && saveModel.exists()) {
            throw new RuntimeException("Уже существует сохраненая модель: " + saveModel.getPath());
        } else if (!forSave && !saveModel.exists()) {
            throw new RuntimeException("Модель " + saveModel.getPath() + "для загруки не найдена");
        }
    }

}
