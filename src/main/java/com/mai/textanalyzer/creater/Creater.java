/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.creater;

import com.mai.textanalyzer.classifier.common.ClassifierEnum;
import com.mai.textanalyzer.classifier.common.ClassifierTypeEnum;
import com.mai.textanalyzer.classifier.common.TextClassifier;
import com.mai.textanalyzer.classifier.multi_classifier.MultiClassifier;
import com.mai.textanalyzer.classifier.weka_classifier.CreaterWekaClassifier;
import com.mai.textanalyzer.classifier.weka_classifier.WekaClassifier;
import com.mai.textanalyzer.classifier.weka_classifier.WekaUtils;
import com.mai.textanalyzer.indexing.common.Indexer;
import com.mai.textanalyzer.indexing.common.IndexerEnum;
import static com.mai.textanalyzer.indexing.common.IndexerEnum.DOC2VEC;
import static com.mai.textanalyzer.indexing.common.IndexerEnum.TF_IDF;
import com.mai.textanalyzer.indexing.doc2vec.Doc2Vec;
import com.mai.textanalyzer.indexing.doc2vec.Doc2VecUtils;
import com.mai.textanalyzer.indexing.tf_idf.TfIIdfUtils;
import com.mai.textanalyzer.indexing.tf_idf.TfIdf;
import java.io.File;

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
        checkRootFolderStructure(rootFolder, modelName, true);
        File folderWithDataForLearning = getDocForLearningFolder(rootFolder);
        File folderForSave = new File(getSaveModelFolder(rootFolder), modelName);
        Doc2Vec doc2Vec = Doc2VecUtils.createModel(folderWithDataForLearning);
        Doc2VecUtils.saveModel(doc2Vec, folderForSave);
        return doc2Vec;
    }

    private static Indexer createAndSaveTfIIdf(File rootFolder) {
        String modelName = TF_IDF.getModelName();
        checkRootFolderStructure(rootFolder, modelName, true);
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
        checkRootFolderStructure(rootFolder, modelName, false);
        return Doc2VecUtils.loadModel(new File(getSaveModelFolder(rootFolder), modelName));
    }

    private static Indexer loadTfIIdf(File rootFolder) {
        String modelName = TF_IDF.getModelName();
        checkRootFolderStructure(rootFolder, modelName, false);
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

    public static void createAndSaveClassifier(File rootFolder, ClassifierEnum classifier, IndexerEnum indexingEnum, boolean useCSV) {
        String modelName = ClassifierEnum.getFullNameModel(classifier, indexingEnum);
        checkRootFolderStructure(rootFolder, modelName, true);
        Indexer indexer = loadIndexer(indexingEnum, rootFolder);
        if (classifier.getClassifierType() == ClassifierTypeEnum.WEKA_CLASSIFIER) {
            WekaClassifier wc = CreaterWekaClassifier.getClassifier(classifier, indexer, rootFolder, useCSV);
            File folderForSave = new File(getSaveModelFolder(rootFolder), modelName);
            WekaUtils.saveModel(wc, folderForSave);
        } else if (classifier.getClassifierType() == ClassifierTypeEnum.MYLTI_CLASSIFIER) {
            //не сохраняется, а собирается из остальных
        } else {
            throw new UnsupportedOperationException("Classifier support for" + classifier.getClassifierType() + " not yet added");
        }
    }

    public static TextClassifier loadClassifier(File rootFolder, ClassifierEnum classifier, IndexerEnum indexingEnum) {
        String modelName = ClassifierEnum.getFullNameModel(classifier, indexingEnum);
        if (classifier.getClassifierType() == ClassifierTypeEnum.WEKA_CLASSIFIER) {
            checkRootFolderStructure(rootFolder, modelName, false);
            return WekaUtils.loadModel(new File(getSaveModelFolder(rootFolder), modelName));
        } else if (classifier.getClassifierType() == ClassifierTypeEnum.MYLTI_CLASSIFIER) {
            checkRootFolderStructure(rootFolder, modelName, null);
            return new MultiClassifier(rootFolder);
        } else {
            throw new UnsupportedOperationException("UnsupportedOperation");
        }
    }

    /**
     *
     *
     * @param rootFolder
     * @param modelName
     * @param forSave true - if save model, false - if load model, if null -
     * dont check presence model
     */
    public static void checkRootFolderStructure(File rootFolder, String modelName, Boolean forSave) {
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
        if (forSave != null) {
            File saveModel = new File(checkingFile, modelName);
            if (forSave && saveModel.exists()) {
                throw new SaveModelException("Уже существует сохраненая модель: " + saveModel.getPath());
            } else if (!forSave && !saveModel.exists()) {
                throw new SaveModelException("Модель " + saveModel.getPath() + "для загруки не найдена");
            }
        }
    }

    public static boolean checkExistIndexerModel(File rootFolder, IndexerEnum indexingEnum) {
        return new File(getSaveModelFolder(rootFolder), indexingEnum.getModelName()).exists();

    }

    public static boolean checkExistClassifierModel(File rootFolder, ClassifierEnum classifierEnum, IndexerEnum indexingEnum) {
        if (classifierEnum == ClassifierEnum.MYLTI_CLASSIFIER) {
            for (ClassifierEnum ce : ClassifierEnum.values()) {
                if (ce != ClassifierEnum.MYLTI_CLASSIFIER && !new File(getSaveModelFolder(rootFolder), ClassifierEnum.getFullNameModel(ce, indexingEnum)).exists()) {
                    return false;
                }
            }
            return true;
        }
        return new File(getSaveModelFolder(rootFolder), ClassifierEnum.getFullNameModel(classifierEnum, indexingEnum)).exists();
    }

}
