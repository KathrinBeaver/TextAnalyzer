/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.indexing.common;

/**
 *
 * @author Sergey
 */
public enum IndexerEnum {
    DOC2VEC(1, "Doc2Vec", "Doc2VecModel"),
    TF_IDF(2, "TfIdf", "TfIdfModel");

    private final int id;
    private final String rusName;
    private final String modelName;

    private IndexerEnum(int id, String rusName, String modelName) {
        this.id = id;
        this.rusName = rusName;
        this.modelName = modelName;
    }

    public int getId() {
        return id;
    }

    public String getRusName() {
        return rusName;
    }

    public String getModelName() {
        return modelName;
    }
    public static IndexerEnum getIndexerEnumById(int id) {
        for (IndexerEnum ce : IndexerEnum.values()) {
            if (ce.getId() == id) {
                return ce;
            }
        }
        return null;
    }

}
