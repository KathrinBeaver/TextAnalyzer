/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.bag_of_words.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.bagofwords.vectorizer.BagOfWordsVectorizer;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.text.sentenceiterator.labelaware.LabelAwareFileSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.labelaware.LabelAwareSentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 *
 * @author Sergey
 */
public class TestBagOfWords {

    @Test
    public void test() throws FileNotFoundException {
        //test
        File rootDir = new ClassPathResource("testFile.txt").getFile();
        LabelAwareSentenceIterator iter = new LabelAwareFileSentenceIterator(rootDir);
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        BagOfWordsVectorizer vectorizer = new BagOfWordsVectorizer.Builder()
                .setMinWordFrequency(1)
                .setStopWords(new ArrayList<String>())
                .setTokenizerFactory(tokenizerFactory)
                .setIterator(iter)
                //                .labels(labels)
                //                .cleanup(true)
                .build();

        vectorizer.fit();
        VocabWord word = vectorizer.getVocabCache().wordFor("file.");
        // vectorizer.vectorize();
        INDArray array = vectorizer.transform("very data cat dog xzxz xz xz xz");
        System.out.println(array);
    }

}
