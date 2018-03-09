/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testDoc2vec;

import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author Sergey
 */
public class TestTfIIdfUtils {

    String text = "скучный дождь";

    @Test
    public void testMethod() throws IOException {

//        TfidfVectorizer tfidfVectorizer = TfIIdfUtils.createModel();
//
//        System.out.println(tfidfVectorizer.transform(text));
//
//        VocabCache<VocabWord> vocabCache = tfidfVectorizer.getVocabCache();
//        vocabCache.saveVocab();
//        System.out.println("cache.numWords()- " + vocabCache.numWords());
//        System.out.println("cache.totalNumberOfDocs()- " + vocabCache.totalNumberOfDocs());
//
//        //Saving
//        TfIIdfUtils.saveModel(tfidfVectorizer);
//
//        TfidfVectorizer tfidfVectorizer2 = TfIIdfUtils.loadModel();
//        
////        VocabConstructor<VocabWord> constructor = new VocabConstructor.Builder<VocabWord>()
////                .setTargetVocabCache(vocabCache).setStopWords(new ArrayList<>())
////                .allowParallelTokenization(false).build();
////        VocabCache<VocabWord> buildCache = constructor.buildJointVocabulary(true, false);
//
//        System.out.println("buildCache.numWords()- " + tfidfVectorizer2.getVocabCache().numWords());
//        System.out.println("buildCache.totalNumberOfDocs()- " + tfidfVectorizer2.getVocabCache().totalNumberOfDocs());
//
//        System.out.println(tfidfVectorizer2.transform(text));
    }
}
