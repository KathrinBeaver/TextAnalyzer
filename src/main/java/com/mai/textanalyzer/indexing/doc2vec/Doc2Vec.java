/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.indexing.doc2vec;

import com.mai.textanalyzer.indexing.common.Indexer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.exception.ND4JIllegalStateException;

/**
 *
 * @author Sergey
 */
public class Doc2Vec implements Indexer {

    public static int LAYER_SIZE = 100;
    private final ParagraphVectors paragraphVectors;

    public Doc2Vec(ParagraphVectors paragraphVectors) {
        this.paragraphVectors = paragraphVectors;
    }

    public ParagraphVectors getParagraphVectors() {
        return paragraphVectors;
    }

    @Override
    public INDArray getIndex(String text) {
        try {
            return paragraphVectors.inferVector(text);
        } catch (ND4JIllegalStateException notEnoughtInfoEx) {
            return null;
        }
    }

    @Override
    public int getDimensionSize() {
        return LAYER_SIZE;
    }

}
