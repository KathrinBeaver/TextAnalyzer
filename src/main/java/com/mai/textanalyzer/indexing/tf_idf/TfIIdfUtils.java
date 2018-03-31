/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.indexing.tf_idf;

import com.mai.textanalyzer.word_processing.MyPreprocessor;
import com.mai.textanalyzer.word_processing.RusUTF8FileLabelAwareIterator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.deeplearning4j.bagofwords.vectorizer.TfidfVectorizer;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

/**
 *
 * @author Sergey
 */
public class TfIIdfUtils {

    private final static Logger log = Logger.getLogger(TfIIdfUtils.class.getName());

    private final static File saveTfIdf = new File("G:\\DocForTest\\SaveModel");

    private final static RusUTF8FileLabelAwareIterator iterator;
    private final static TokenizerFactory tokenizerFactory;

    static {
        File folderWithDataForLearning = new File("G:\\DocForTest\\DataForLearning");

        iterator = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForLearning)
                .build();

        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new MyPreprocessor());
    }

    public static TfidfVectorizer createModel() {
        TfidfVectorizer vectorizer = new TfidfVectorizer.Builder()
                .setMinWordFrequency(0)
                .setStopWords(new ArrayList<>())
                .setTokenizerFactory(tokenizerFactory)
                .setIterator(iterator)
                .allowParallelTokenization(true)
                .build();
        vectorizer.fit();
        return vectorizer;
    }

    public static boolean saveModel(TfidfVectorizer tfidfVectorizer) {
        try {
            WordVectorSerializer.writeVocabCache(tfidfVectorizer.getVocabCache(), saveTfIdf);
        } catch (IOException ex) {
            log.info(ex.toString());
            return false;
        }
        return true;
    }

    /**
     * @return null if an error occured while loading model
     */
    public static TfidfVectorizer loadModel() {
        VocabCache<VocabWord> loadCache;
        try {
            loadCache = WordVectorSerializer.readVocabCache(saveTfIdf);
        } catch (IOException ex) {
            log.info(ex.toString());
            return null;
        }
        loadCache.incrementTotalDocCount(iterator.getSize());

        return new TfidfVectorizer.Builder()
                .setMinWordFrequency(0)
                .setStopWords(new ArrayList<>())
                .setVocab(loadCache)
                .setTokenizerFactory(tokenizerFactory)
                .setIterator(iterator)
                .allowParallelTokenization(true)
                .build();
    }

}
