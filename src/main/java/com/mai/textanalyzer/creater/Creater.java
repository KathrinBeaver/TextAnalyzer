/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.creater;

import com.mai.textanalyzer.indexing.doc2vec.Doc2VecUtils;
import com.mai.textanalyzer.indexing.tf_idf.TfIIdfUtils;
import java.io.File;

/**
 *
 * @author Sergey
 */
public class Creater {

    public static void main(String[] args) {

    }

    public static void createAndSaveDoc2Vec(File folderWithDataForLearning, File folderForSave) {
        Doc2VecUtils.saveModel(Doc2VecUtils.createModel(folderWithDataForLearning), folderForSave);
    }

    public static void createAndSaveTfIIdfUtils(File folderWithDataForLearning, File folderForSave) {
        TfIIdfUtils.saveModel(TfIIdfUtils.createModel(folderWithDataForLearning), folderForSave);
    }
}
