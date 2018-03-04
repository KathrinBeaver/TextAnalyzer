/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.indexing.doc2vec;

import com.mai.textanalyzer.indexing.doc2vec.tools.LabelSeeker;
import com.mai.textanalyzer.indexing.doc2vec.tools.MeansBuilder;
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
import org.deeplearning4j.text.documentiterator.FileLabelAwareIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.primitives.Pair;

/**
 *
 * @author Sergey
 */
public class Doc2VecUtils {

//    LabelAwareIterator iterator;
//    TokenizerFactory tokenizerFactory;
    private static final Logger log = Logger.getLogger(Doc2VecUtils.class);

    public static void main(String[] args) throws Exception {
//        try {
//            Loader.load(TestDoc2Vec.class);
//        } catch (UnsatisfiedLinkError e) {
//            String path = Loader.cacheResource(TestDoc2Vec.class, "C:\\Users\\Sergey\\.javacpp\\cache\\cuda-8.0-6.0-1.3-windows-x86_64.jar\\org\\bytedeco\\javacpp\\windows-x86_64\\jnicuda.dll").getPath();
//            new ProcessBuilder("C:\\Users\\Sergey\\Desktop\\DependsWalcker\\depends.exe", path).start().waitFor();
//        }

    }

//    File file = new File("D:\\testClassDoc");
    public static ParagraphVectors createModel(File folderWithDataForLearning) {
        // build a iterator for our dataset
        LabelAwareIterator iterator = new FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForLearning)
                .build();

        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        /*
        CommonPreprocessor will apply the following regex to each token:
        [\d\.:,"'\(\)\[\]|/?!;]+
        So, effectively all numbers, punctuation symbols and some special symbols
        are stripped off.
        Additionally it forces lower case for all tokens.
         */
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
        // ParagraphVectors training configuration
        ParagraphVectors paragraphVectors = new ParagraphVectors.Builder()
                .learningRate(0.025)
                .minLearningRate(0.001)
                .batchSize(1000)
                .epochs(20)
                .iterate(iterator)
                .trainWordVectors(true)
                .tokenizerFactory(tokenizerFactory)
                .build();

        // Start model training
        paragraphVectors.fit();
        return paragraphVectors;
    }

    public static void saveModel(ParagraphVectors paragraphVectors, File file) {
        WordVectorSerializer.writeParagraphVectors(paragraphVectors, file);
    }

    public static ParagraphVectors loadModel(File file) {
        ParagraphVectors pv;
        try {
            pv = WordVectorSerializer.readParagraphVectors(file);
        } catch (IOException e) {
            log.info(e);
            return null;
        }
        return pv;
    }

    public static ParagraphVectors loadModel(InputStream inputStream) {
        ParagraphVectors pv;
        try {
            pv = WordVectorSerializer.readParagraphVectors(inputStream);
        } catch (IOException e) {
            log.info(e);
            return null;
        }
        return pv;
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

    public static List<Pair<String, Double>> getTopics(ParagraphVectors pv, String document) {
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

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
//        LabelSeeker seeker = new LabelSeeker(LabelsEnum.getListLabels(), (InMemoryLookupTable<VocabWord>) pv.getLookupTable());
        INDArray documentAsCentroid = meansBuilder.documentAsVector(document);
        List<Pair<String, Double>> scores = seeker.getScores(documentAsCentroid);
        return scores;
    }

//    void checkUnlabeledData() throws FileNotFoundException {
//        paragraphVectors.inferVector(text);
//
//        /*
//      At this point we assume that we have model built and we can check
//      which categories our unlabeled document falls into.
//      So we'll start loading our unlabeled documents and checking them
//         */
//        File file = new File("D:\\testUnClassDoc");
//        FileLabelAwareIterator unClassifiedIterator = new FileLabelAwareIterator.Builder()
//                .addSourceFolder(file)
//                .build();
//
//        /*
//      Now we'll iterate over unlabeled data, and check which label it could be assigned to
//      Please note: for many domains it's normal to have 1 document fall into few labels at once,
//      with different "weight" for each.
//         */
//        MeansBuilder meansBuilder = new MeansBuilder(
//                (InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable(),
//                tokenizerFactory);
//        LabelSeeker seeker = new LabelSeeker(iterator.getLabelsSource().getLabels(),
//                (InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable());
//
//        while (unClassifiedIterator.hasNextDocument()) {
//            LabelledDocument document = unClassifiedIterator.nextDocument();
//            INDArray documentAsCentroid = meansBuilder.documentAsVector(document);
//            List<Pair<String, Double>> scores = seeker.getScores(documentAsCentroid);
//
//            /*
//          please note, document.getLabel() is used just to show which document we're looking at now,
//          as a substitute for printing out the whole document name.
//          So, labels on these two documents are used like titles,
//          just to visualize our classification done properly
//             */
//            log.info("Document '" + document.getLabels() + "' falls into the following categories: ");
//            for (Pair<String, Double> score : scores) {
//                log.info("        " + score.getFirst() + ": " + score.getSecond());
//            }
//        }
//
//    }
}
