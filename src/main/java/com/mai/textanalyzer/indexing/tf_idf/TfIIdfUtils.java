/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.indexing.tf_idf;

import com.mai.textanalyzer.indexing.common.IndexingUtils;
import com.mai.textanalyzer.word_processing.RusUTF8FileLabelAwareIterator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.deeplearning4j.bagofwords.vectorizer.TfidfVectorizer;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;

/**
 *
 * @author Sergey
 */
public class TfIIdfUtils {

    private final static Logger log = Logger.getLogger(TfIIdfUtils.class.getName());

    public static TfidfVectorizer createModel(File folderWithDataForLearning) {
        TfidfVectorizer vectorizer = new TfidfVectorizer.Builder()
                .setMinWordFrequency(0)
                .setStopWords(new ArrayList<>())
                .setTokenizerFactory(IndexingUtils.getTokenizerFactory())
                .setIterator(IndexingUtils.getLabelAwareIterator(folderWithDataForLearning))
                .allowParallelTokenization(true)
                .build();
        vectorizer.fit();
        return vectorizer;
    }

    public static boolean saveModel(TfidfVectorizer tfidfVectorizer, File folderForSave) {
        try {
            WordVectorSerializer.writeVocabCache(tfidfVectorizer.getVocabCache(), folderForSave);
        } catch (IOException ex) {
            log.info(ex.toString());
            return false;
        }
        return true;
    }

    /**
     * @param folderWithModel
     * @param folderWithDataForLearning
     * @return null if an error occured while loading model
     */
    public static TfidfVectorizer loadModel(File folderWithModel, File folderWithDataForLearning) {
        VocabCache<VocabWord> loadCache;
        try {
            loadCache = WordVectorSerializer.readVocabCache(folderWithModel);
        } catch (IOException ex) {
            log.info(ex.toString());
            return null;
        }
        RusUTF8FileLabelAwareIterator iterator = IndexingUtils.getLabelAwareIterator(folderWithDataForLearning);
        loadCache.incrementTotalDocCount(iterator.getSize());

        return new TfidfVectorizer.Builder()
                .setMinWordFrequency(0)
                .setStopWords(new ArrayList<>())
                .setVocab(loadCache)
                .setTokenizerFactory(IndexingUtils.getTokenizerFactory())
                .setIterator(iterator)
                .allowParallelTokenization(true)
                .build();
    }

}
