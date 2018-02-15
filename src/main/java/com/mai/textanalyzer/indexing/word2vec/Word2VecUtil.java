/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.indexing.word2vec;

import java.io.File;
import java.io.FileNotFoundException;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.apache.log4j.Logger;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.plot.BarnesHutTsne;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;

/**
 *
 * @author Sergey
 */
public class Word2VecUtil {

    private static Logger log = Logger.getLogger(Word2VecUtil.class);

    public static void main(String[] args) throws FileNotFoundException {
        log.info("Load data....");
        SentenceIterator iter = new LineSentenceIterator(new File("D:\\testCopyDir\\dictionary.txt"));
        iter.setPreProcessor(new SentencePreProcessor() {
            @Override
            public String preProcess(String sentence) {
                return sentence.toLowerCase();
            }
        });

        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        log.info("Fitting Word2Vec model....");
        vec.fit();

//        Scanner scaner = new Scanner(System.in);
//        while (true) {
//            String nextWord = scaner.nextLine();
//            if (nextWord.isEmpty()) {
//                break;
//            }
//            System.out.println("10 Words closest to " + nextWord + "': " + vec.wordsNearest(nextWord, 10));
//        }
//        int batchSize = 64;     //Number of examples in each minibatch
//        int vectorSize = 300;   //Size of the word vectors. 300 in the Google News model
//        int nEpochs = 1;        //Number of epochs (full passes of training data) to train on
//        int truncateReviewsToLength = 256;  //Truncate reviews with length (# words) greater than this
//
//        Nd4j.getMemoryManager().setAutoGcWindow(10000);  //https://deeplearning4j.org/workspaces
//
//        //Set up network configuration
//        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
//            .updater(Updater.ADAM)  //To configure: .updater(Adam.builder().beta1(0.9).beta2(0.999).build())
//            .regularization(true).l2(1e-5)
//            .weightInit(WeightInit.XAVIER)
//            .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue).gradientNormalizationThreshold(1.0)
//            .learningRate(2e-2)
//            .trainingWorkspaceMode(WorkspaceMode.SEPARATE).inferenceWorkspaceMode(WorkspaceMode.SEPARATE)   //https://deeplearning4j.org/workspaces
//            .list()
//            .layer(0, new GravesLSTM.Builder().nIn(vectorSize).nOut(256)
//                .activation(Activation.TANH).build())
//            .layer(1, new RnnOutputLayer.Builder().activation(Activation.SOFTMAX)
//                .lossFunction(LossFunctions.LossFunction.MCXENT).nIn(256).nOut(2).build())
//            .pretrain(false).backprop(true).build();
//
//        MultiLayerNetwork net = new MultiLayerNetwork(conf);
//        net.init();
//        net.setListeners(new ScoreIterationListener(1));
//
//        System.out.println("Starting training");
//        for (int i = 0; i < nEpochs; i++) {
//            net.fit(vec.);
//            System.out.println("Epoch " + i + " complete. Starting evaluation:");
//
//            Evaluation evaluation = net.evaluate(test);
//            System.out.println(evaluation.stats());
//        }
    }

    public static void saveModel(Word2Vec word2Vec, File file) {
        WordVectorSerializer.writeWord2VecModel(word2Vec, file);
    }

    public static Word2Vec loadModel(File file) {
        return WordVectorSerializer.readWord2VecModel(file);
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

}
