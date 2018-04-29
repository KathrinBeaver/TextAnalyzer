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
import com.mai.textanalyzer.creater.Creater;
import com.mai.textanalyzer.dao.accuracy.Accuracy;
import com.mai.textanalyzer.dao.accuracy.AccuracyService;
import com.mai.textanalyzer.dao.common.ApplicationContextHolder;
import com.mai.textanalyzer.indexing.common.IndexerEnum;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * модель классификатора со статистикой по каждому классу
 *
 * @author Sergey
 */
public class AccuracyClassifier implements TextClassifier {

    private final ClassifierEnum classifierEnum;
    private final TextClassifier classifier;
    private final Map<String, Accuracy> accuracyMap;

    private final AccuracyService accuracyService;

    public AccuracyClassifier(File rootFolder, ClassifierEnum classifierEnum, IndexerEnum indexerEnum) {
        this.accuracyService = ApplicationContextHolder.getApplicationContext().getBean(AccuracyService.class);
        this.classifierEnum = classifierEnum;
        this.accuracyMap = accuracyService.getMapAccuracy(classifierEnum, indexerEnum);
        this.classifier = Creater.loadClassifier(rootFolder, classifierEnum, indexerEnum);
    }

    public ClassifierEnum getClassifierEnum() {
        return classifierEnum;
    }

    @Override
    public String classifyMessage(INDArray matrixTextModel) {
        return classifier.classifyMessage(matrixTextModel);
    }

    @Override
    public List<Prediction> getDistribution(INDArray matrixTextModel) {
        return classifier.getDistribution(matrixTextModel);
    }

    public List<AccuracyPredicion> getAccuracyDistribution(INDArray matrixTextModel) {
        List<AccuracyPredicion> accuracyPredicions = new ArrayList<>();
        classifier.getDistribution(matrixTextModel).forEach((prediction) -> {
            Accuracy accuracy = accuracyMap.get(prediction.getTopic());
            if (accuracy != null) {
                accuracyPredicions.add(new AccuracyPredicion(prediction, accuracy.getAccuracy(), accuracy.getDocCount()));
            }
        });
        return accuracyPredicions;
    }

    @Override
    public List<String> getTopicList() {
        return classifier.getTopicList();
    }

}
