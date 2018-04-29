/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.classifier.multi_classifier;

import com.mai.textanalyzer.classifier.common.AccuracyPredicion;
import com.mai.textanalyzer.classifier.common.ClassifierEnum;
import com.mai.textanalyzer.classifier.common.Prediction;
import com.mai.textanalyzer.classifier.common.TextClassifier;
import com.mai.textanalyzer.indexing.common.IndexerEnum;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 *
 * @author Sergey
 */
public class MultiClassifier implements TextClassifier {

    private final static IndexerEnum INDEXER_ENUM = IndexerEnum.DOC2VEC;
    private final List<AccuracyClassifier> classifierList = new ArrayList<>();

    public MultiClassifier(File rootDir) {
        classifierList.add(new AccuracyClassifier(rootDir, ClassifierEnum.NAIVE_BAYES, INDEXER_ENUM));
        classifierList.add(new AccuracyClassifier(rootDir, ClassifierEnum.IBK, INDEXER_ENUM));
        classifierList.add(new AccuracyClassifier(rootDir, ClassifierEnum.SVM, INDEXER_ENUM));
    }

    @Override
    public String classifyMessage(INDArray matrixTextModel) {
        List<Prediction> predictions = normalizePredicition(calcPredicition(matrixTextModel));
        if (predictions.isEmpty()) {
            return null;
        }
        Iterator<Prediction> it = predictions.iterator();
        Prediction maxAccuracyPrediction = it.next();
        while (it.hasNext()) {
            Prediction nextPrediction = it.next();
            if (nextPrediction.getValue() > maxAccuracyPrediction.getValue()) {
                maxAccuracyPrediction = nextPrediction;
            }
        }
        return maxAccuracyPrediction.getTopic();
    }

    @Override
    public List<Prediction> getDistribution(INDArray matrixTextModel) {
        return normalizePredicition(calcPredicition(matrixTextModel));
    }

    @Override
    public List<String> getTopicList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<AccuracyPredicion> calcPredicition(INDArray iNDArray) {
        Map<String, AccuracyPredicion> accuracyMap = new HashMap<>();
        classifierList.forEach((classifier) -> {
            classifier.getAccuracyDistribution(iNDArray).forEach((accuracyPredicion) -> {
                AccuracyPredicion savePredicition = accuracyMap.get(accuracyPredicion.getTopic());
                if (savePredicition == null || accuracyPredicion.getAccuracy() > savePredicition.getAccuracy()) {
                    accuracyMap.put(accuracyPredicion.getTopic(), accuracyPredicion);
                }
            });
        });
        List<AccuracyPredicion> accuracyPredicions = new ArrayList<>(accuracyMap.values());
        Collections.sort(accuracyPredicions);
        return accuracyPredicions;
    }

    private List<Prediction> normalizePredicition(List<AccuracyPredicion> accuracyPredicions) {
        double percent = 0;
        for (AccuracyPredicion ap : accuracyPredicions) {
            percent += calcFactorAcc(ap);
        }
        percent = percent / 100;//значение для одного процента
        List<Prediction> resultList = new ArrayList<>();
        for (AccuracyPredicion ap : accuracyPredicions) {
            resultList.add(new Prediction(ap.getTopic(), calcFactorAcc(ap) / percent));
        }
        return resultList;
    }

    private double calcFactorAcc(AccuracyPredicion ap) {
        return ap.getAccuracy() * ap.getValue();
    }

}
