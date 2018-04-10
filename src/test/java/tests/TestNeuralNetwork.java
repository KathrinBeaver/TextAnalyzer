package tests;

/**
 * Created by KIT Solutions (www.kitsol.com) on 9/28/2016.
 */
import com.mai.textanalyzer.indexing.doc2vec.Doc2Vec;
import com.mai.textanalyzer.indexing.doc2vec.Doc2VecUtils;
import com.mai.textanalyzer.word_processing.RusUTF8FileLabelAwareIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.primitives.Pair;

/**
 * - Notes: - Data files are stored at following location
 * .\dl4j-0.4-examples-master\dl4j-examples\src\main\resources\PredictGender\Data
 * folder
 */
public class TestNeuralNetwork {

    public static void main(String args[]) {

        TestNeuralNetwork dg = new TestNeuralNetwork();
        dg.train();
    }

    /**
     * This function uses GenderRecordReader and passes it to
     * RecordReaderDataSetIterator for further training.
     */
    public void train() {
        int seed = 123456;
        double learningRate = 0.005;// was .01 but often got errors: "o.d.optimize.solvers.BaseOptimizer - Hit termination condition on iteration 0"
        int nEpochs = 100;
        int numInputs = 300;
        int numOutputs = 2;
        int numHiddenNodes = numInputs * 2;

        long st = System.currentTimeMillis();
        System.out.println("Preprocessing start time : " + st);

        File doc2VecModel = new File("E:\\DocForTest\\MiniTest\\SaveModel\\Doc2Vec\\Doc2VecModel");
        Doc2Vec doc2Vec = Doc2VecUtils.loadModel(doc2VecModel);

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .iterations(1)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .learningRate(learningRate)
                .updater(Updater.NESTEROVS) //To configure: .updater(new Nesterovs(0.9))
                .list()
                .layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(numHiddenNodes)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.RELU)
                        .build())
                .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER)
                        .nIn(numHiddenNodes).nOut(numOutputs).build())
                .pretrain(false).backprop(true).build();
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

//        UIServer uiServer = UIServer.getInstance();
//        StatsStorage statsStorage = new InMemoryStatsStorage();
//        uiServer.attach(statsStorage);
//        model.setListeners(new StatsListener(statsStorage));
        File folderWithDataForLearn = new File("E:\\DocForTest\\MiniTest\\DataLearning");
        RusUTF8FileLabelAwareIterator tearchingIteratorLearn = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForLearn)
                .build();
//data/labels
        List<Pair<INDArray, INDArray>> data = new ArrayList<>();
        while (tearchingIteratorLearn.hasNext()) {
            LabelledDocument document = tearchingIteratorLearn.next();
            data.add(new Pair<>(doc2Vec.getIndex(document.getContent()), trasformLabels(document.getLabels().iterator().next())));
        }
        for (int n = 0; n < nEpochs; n++) {
            for (Pair<INDArray, INDArray> pair : data) {
                model.fit(pair.getFirst(), pair.getSecond());
            }
        }

//        ModelSerializer.writeModel(model, this.filePath + "PredictGender.net", true);
        File folderWithDataForTest = new File("E:\\DocForTest\\MiniTest\\DataTest");
        RusUTF8FileLabelAwareIterator tearchingIteratorTest = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForTest)
                .build();

        System.out.println("Evaluate model....");
        Evaluation eval = new Evaluation(numOutputs);
        while (tearchingIteratorTest.hasNext()) {
            LabelledDocument document = tearchingIteratorTest.next();
            INDArray features = doc2Vec.getIndex(document.getContent());
            INDArray lables = trasformLabels(document.getLabels().iterator().next());
            INDArray predicted = model.output(features, false);

            eval.eval(lables, predicted);

        }

        //Print the evaluation statistics
        System.out.println(eval.stats());
    }

    public INDArray trasformLabels(String topic) {
        if (topic.equals("Астрономия")) {
            return Nd4j.create(new double[]{1, 0});
        } else {
            return Nd4j.create(new double[]{0, 1});
        }
    }
}
