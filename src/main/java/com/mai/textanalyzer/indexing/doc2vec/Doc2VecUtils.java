/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.indexing.doc2vec;

import static com.mai.textanalyzer.creater.Creater.LAYER_SIZE;
import com.mai.textanalyzer.indexing.common.IndexingUtils;
import com.mai.textanalyzer.indexing.common.StopWords;
import com.mai.textanalyzer.indexing.doc2vec.tools.LabelSeeker;
import com.mai.textanalyzer.indexing.doc2vec.tools.MeansBuilder;
import com.mai.textanalyzer.word_processing.MyPreprocessor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.apache.log4j.Logger;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.plot.BarnesHutTsne;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.primitives.Pair;

/**
 *
 * @author Sergey
 */
public class Doc2VecUtils {

    private static final Logger log = Logger.getLogger(Doc2VecUtils.class);

//    File file = new File("D:\\testClassDoc");
    public static Doc2Vec createModel(File folderWithDataForLearning) {
        LabelAwareIterator iterator = IndexingUtils.getLabelAwareIterator(folderWithDataForLearning);
        ParagraphVectors paragraphVectors = new ParagraphVectors.Builder()
                .learningRate(0.025)
                .minLearningRate(0.001)
                .layerSize(LAYER_SIZE)
                .batchSize(1000)
                .epochs(20)
                .stopWords(StopWords.getStopWords())
                .iterate(iterator)
                .trainWordVectors(true)
                .tokenizerFactory(IndexingUtils.getTokenizerFactory())
                .build();

        // Start model training
        paragraphVectors.fit();

        return new Doc2Vec(paragraphVectors);
    }

    public static void saveModel(Doc2Vec doc2Vec, File file) {
        WordVectorSerializer.writeParagraphVectors(doc2Vec.getParagraphVectors(), file);
    }

    public static Doc2Vec loadModel(File file) {
        ParagraphVectors pv;
        try {
            pv = WordVectorSerializer.readParagraphVectors(file);
            pv.setTokenizerFactory(IndexingUtils.getTokenizerFactory());
        } catch (IOException e) {
            log.info(e);
            return null;
        }
        return new Doc2Vec(pv);
    }

    public static Doc2Vec loadModel(InputStream inputStream) {
        ParagraphVectors pv;
        try {
            pv = WordVectorSerializer.readParagraphVectors(inputStream);
        } catch (IOException e) {
            log.info(e);
            return null;
        }
        return new Doc2Vec(pv);
    }

    public static void visualizingModel(ParagraphVectors pv, File outPutFile) {
        BarnesHutTsne tsne = new BarnesHutTsne.Builder()
                .setMaxIter(1000)
                .stopLyingIteration(250)
                .learningRate(500)
                .useAdaGrad(false)
                .theta(0.5)
                .setMomentum(0.5)
                .normalize(true)
                //                .usePca(false)
                .build();
        pv.getLookupTable().plotVocab(pv.getLookupTable().layerSize(), outPutFile);
    }

    public static List<Pair<String, Double>> getTopics(Doc2Vec doc2Vec, String document) {
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new MyPreprocessor());

        ParagraphVectors pv = doc2Vec.getParagraphVectors();
        MeansBuilder meansBuilder = new MeansBuilder(
                (InMemoryLookupTable<VocabWord>) pv.getLookupTable(),
                tokenizerFactory);

        Class c = pv.getClass();
        Field field;
        List<String> labelsList = new ArrayList<>();
        try {
            field = c.getDeclaredField("labelsList");
            field.setAccessible(true);
            List<VocabWord> templist = (List<VocabWord>) field.get(pv);
            templist.stream().forEach((vw) -> {
                labelsList.add(vw.getLabel());
            });
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        LabelSeeker seeker = new LabelSeeker(labelsList, (InMemoryLookupTable<VocabWord>) pv.getLookupTable());
        INDArray documentAsCentroid = meansBuilder.documentAsVector(document);
        List<Pair<String, Double>> scores = seeker.getScores(documentAsCentroid);
        return scores;
    }
}
