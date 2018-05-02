/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.dao.accuracy;

import com.mai.textanalyzer.classifier.common.ClassifierEnum;
import com.mai.textanalyzer.dao.topic.ITopicService;
import com.mai.textanalyzer.indexing.common.IndexerEnum;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author Sergey
 */
public class AccuracyService implements IAccuracyService {

    private IAccuracyDao accuracyDao;

    private ITopicService topicService;

    @Required
    public void setAccuracyDao(IAccuracyDao accuracyDao) {
        this.accuracyDao = accuracyDao;
    }

    @Required
    public void setTopicService(ITopicService topicService) {
        this.topicService = topicService;
    }

    @Override
    public Map<String, Accuracy> getMapAccuracy(ClassifierEnum classifierEnum, IndexerEnum indexerEnum) {
        Map<String, Accuracy> map = new HashMap<>();
        for (Accuracy accuracy : accuracyDao.getListAccuracy(classifierEnum, indexerEnum)) {
            map.put(accuracy.getTopic(), accuracy);
        }
        return map;
    }

    @Override
    public void inserOrUpdateAccyracy(Accuracy accuracy) {
        int topicId = topicService.getOrInserteAndGetTopicByName(accuracy.getTopic());
        accuracyDao.inserOrUpdateAccyracy(accuracy.getClassifierEnum(), accuracy.getIndexerEnum(), topicId, accuracy.getDocCount(), accuracy.getAccuracy());
    }

}
