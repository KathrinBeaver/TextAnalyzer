/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.indexing.tf_idf;

import com.mai.textanalyzer.indexing.common.Indexer;
import org.deeplearning4j.bagofwords.vectorizer.TfidfVectorizer;
import org.nd4j.linalg.api.ndarray.INDArray;
import weka.core.stemmers.SnowballStemmer;

/**
 *
 * @author Sergey
 */
public class TfIdf implements Indexer {

    private final TfidfVectorizer tfidfVectorizer;

    public TfIdf(TfidfVectorizer tfidfVectorizer) {
        this.tfidfVectorizer = tfidfVectorizer;
    }

    public TfidfVectorizer getTfidfVectorizer() {
        return tfidfVectorizer;
    }

    @Override
    public INDArray getIndex(String text) {
        return tfidfVectorizer.transform(text);
    }

//    public INDArray getSizeFitures() {
//        return tfidfVectorizer.transform(text);
//    }
    @Override
    public int getDimensionSize() {
        return tfidfVectorizer.getVocabCache().numWords();
    }

}
