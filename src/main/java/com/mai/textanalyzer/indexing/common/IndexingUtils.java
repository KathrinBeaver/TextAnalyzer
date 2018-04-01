/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.indexing.common;

import com.mai.textanalyzer.word_processing.MyPreprocessor;
import com.mai.textanalyzer.word_processing.RusUTF8FileLabelAwareIterator;
import java.io.File;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

/**
 *
 * @author Sergey
 */
public final class IndexingUtils {

    private IndexingUtils() {
    }

    public static TokenizerFactory getTokenizerFactory() {
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new MyPreprocessor());
        return tokenizerFactory;
    }

    public static RusUTF8FileLabelAwareIterator getLabelAwareIterator(File folderWithDataForLearning) {
        RusUTF8FileLabelAwareIterator iterator = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForLearning)
                .build();
        return iterator;
    }

}
