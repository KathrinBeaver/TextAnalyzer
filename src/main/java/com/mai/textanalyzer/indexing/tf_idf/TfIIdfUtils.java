/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.indexing.tf_idf;

import java.io.File;
import java.util.ArrayList;
import org.datavec.nlp.reader.TfidfRecordReader;
import org.deeplearning4j.bagofwords.vectorizer.TfidfVectorizer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.text.documentiterator.FileLabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

/**
 *
 * @author Sergey
 */
public class TfIIdfUtils {

    public static TfidfVectorizer createModel(File folderWithDataForLearning) {
        LabelAwareIterator iterator = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForLearning)
                .build();
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
        TfidfVectorizer vectorizer = new TfidfVectorizer.Builder()
                .setMinWordFrequency(1)
                .setStopWords(new ArrayList<>())
                .setTokenizerFactory(tokenizerFactory)
                .setIterator(iterator)
                //                .labels(labels)
                //                .cleanup(true)
                .build();
        vectorizer.fit();
        return vectorizer;
    }

}
