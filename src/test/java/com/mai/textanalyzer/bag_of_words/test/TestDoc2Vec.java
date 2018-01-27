/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.bag_of_words.test;

import com.mai.textanalyzer.bag_of_words.test.tools.LabelSeeker;
import com.mai.textanalyzer.bag_of_words.test.tools.MeansBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.apache.log4j.Logger;
import org.bytedeco.javacpp.Loader;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.text.documentiterator.FileLabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.primitives.Pair;

/**
 *
 * @author Sergey
 */
public class TestDoc2Vec {

    ParagraphVectors paragraphVectors;
    LabelAwareIterator iterator;
    TokenizerFactory tokenizerFactory;

    private static final Logger log = Logger.getLogger(TestDoc2Vec.class);

    public static void main(String[] args) throws Exception {
//        try {
//            Loader.load(TestDoc2Vec.class);
//        } catch (UnsatisfiedLinkError e) {
//            String path = Loader.cacheResource(TestDoc2Vec.class, "C:\\Users\\Sergey\\.javacpp\\cache\\cuda-8.0-6.0-1.3-windows-x86_64.jar\\org\\bytedeco\\javacpp\\windows-x86_64\\jnicuda.dll").getPath();
//            new ProcessBuilder("C:\\Users\\Sergey\\Desktop\\DependsWalcker\\depends.exe", path).start().waitFor();
//        }
        TestDoc2Vec app = new TestDoc2Vec();
        app.makeParagraphVectors();
        app.checkUnlabeledData();

    }

    void makeParagraphVectors() throws Exception {
        File file = new File("D:\\testClassDoc");

//        log.info("Class file:");
//        ClassPathResource resource = new ClassPathResource("D:\\testClassDoc");
        // build a iterator for our dataset
        iterator = new FileLabelAwareIterator.Builder()
                .addSourceFolder(file)
                .build();

        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        // ParagraphVectors training configuration
        paragraphVectors = new ParagraphVectors.Builder()
                .learningRate(0.025)
                .minLearningRate(0.001)
                .batchSize(1000)
                .epochs(5)
                .iterate(iterator)
                .trainWordVectors(true)
                .tokenizerFactory(tokenizerFactory)
                .build();

        // Start model training
        paragraphVectors.fit();
    }

    void checkUnlabeledData() throws FileNotFoundException {
        /*
      At this point we assume that we have model built and we can check
      which categories our unlabeled document falls into.
      So we'll start loading our unlabeled documents and checking them
         */
        File file = new File("D:\\testUnClassDoc");
//        ClassPathResource unClassifiedResource = new ClassPathResource("D:\\testUnClassDoc");
        FileLabelAwareIterator unClassifiedIterator = new FileLabelAwareIterator.Builder()
                .addSourceFolder(file)
                .build();

        /*
      Now we'll iterate over unlabeled data, and check which label it could be assigned to
      Please note: for many domains it's normal to have 1 document fall into few labels at once,
      with different "weight" for each.
         */
        MeansBuilder meansBuilder = new MeansBuilder(
                (InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable(),
                tokenizerFactory);
        LabelSeeker seeker = new LabelSeeker(iterator.getLabelsSource().getLabels(),
                (InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable());

        while (unClassifiedIterator.hasNextDocument()) {
            LabelledDocument document = unClassifiedIterator.nextDocument();
            INDArray documentAsCentroid = meansBuilder.documentAsVector(document);
            List<Pair<String, Double>> scores = seeker.getScores(documentAsCentroid);

            /*
          please note, document.getLabel() is used just to show which document we're looking at now,
          as a substitute for printing out the whole document name.
          So, labels on these two documents are used like titles,
          just to visualize our classification done properly
             */
            log.info("Document '" + document.getLabels() + "' falls into the following categories: ");
            for (Pair<String, Double> score : scores) {
                log.info("        " + score.getFirst() + ": " + score.getSecond());
            }
        }

    }

}
