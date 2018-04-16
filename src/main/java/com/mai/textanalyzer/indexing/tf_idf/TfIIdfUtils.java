/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.indexing.tf_idf;

import com.mai.textanalyzer.indexing.common.IndexingUtils;
import com.mai.textanalyzer.indexing.common.StopWords;
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

    private static final int MIN_WORD_FREQUENCY = 3;
    private final static Logger log = Logger.getLogger(TfIIdfUtils.class.getName());

    public static TfIdf createModel(File folderWithDataForLearning) {
        TfidfVectorizer vectorizer = new TfidfVectorizer.Builder()
                .setMinWordFrequency(MIN_WORD_FREQUENCY)
                .setStopWords(StopWords.getStopWords())
                .setTokenizerFactory(IndexingUtils.getTokenizerFactory())
                .setIterator(IndexingUtils.getLabelAwareIterator(folderWithDataForLearning))
                .allowParallelTokenization(true)
                .build();
        vectorizer.fit();
        return new TfIdf(vectorizer);
    }

    public static boolean saveModel(TfIdf tfIdf, File folderForSave) {
        try {
            WordVectorSerializer.writeVocabCache(tfIdf.getTfidfVectorizer().getVocabCache(), folderForSave);
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
    public static TfIdf loadModel(File folderWithModel, File folderWithDataForLearning) {
        VocabCache<VocabWord> loadCache;
        try {
            loadCache = WordVectorSerializer.readVocabCache(folderWithModel);
        } catch (IOException ex) {
            log.info(ex.toString());
            return null;
        }
        RusUTF8FileLabelAwareIterator iterator = IndexingUtils.getLabelAwareIterator(folderWithDataForLearning);
        loadCache.incrementTotalDocCount(iterator.getSize());

        return new TfIdf(new TfidfVectorizer.Builder()
                .setMinWordFrequency(MIN_WORD_FREQUENCY)
                .setStopWords(new ArrayList<>())
                .setVocab(loadCache)
                .setTokenizerFactory(IndexingUtils.getTokenizerFactory())
                .setIterator(iterator)
                .allowParallelTokenization(true)
                .build()
        );
    }

}
