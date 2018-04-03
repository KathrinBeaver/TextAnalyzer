/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.indexing.doc2vec;

import com.mai.textanalyzer.indexing.common.Indexer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 *
 * @author Sergey
 */
public class Doc2Vec implements Indexer {

    private final ParagraphVectors paragraphVectors;

    public Doc2Vec(ParagraphVectors paragraphVectors) {
        this.paragraphVectors = paragraphVectors;
    }

    public ParagraphVectors getParagraphVectors() {
        return paragraphVectors;
    }

    @Override
    public INDArray getIndex(String text) {
        return paragraphVectors.inferVector(text);
    }

}
