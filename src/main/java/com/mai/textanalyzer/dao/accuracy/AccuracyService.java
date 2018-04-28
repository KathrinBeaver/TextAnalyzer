/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.dao.accuracy;

import com.mai.textanalyzer.classifier.common.ClassifierEnum;
import com.mai.textanalyzer.indexing.common.IndexerEnum;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sergey
 */
public class AccuracyService implements IAccuracyService {

    private IAccuracyDao accuracyDao;

    public void setAccuracyDao(IAccuracyDao accuracyDao) {
        this.accuracyDao = accuracyDao;
    }

    @Override
    public Map<String, Accuracy> getMapAccyracy(ClassifierEnum classifierEnum, IndexerEnum indexerEnum) {
        Map<String, Accuracy> map = new HashMap<>();
        for (Accuracy accuracy : accuracyDao.getListAccyracy(classifierEnum, indexerEnum)) {
            map.put(accuracy.getTopic(), accuracy);
        }
        return map;
    }

}
