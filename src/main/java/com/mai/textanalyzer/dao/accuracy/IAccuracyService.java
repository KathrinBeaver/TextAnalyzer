/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.dao.accuracy;

import com.mai.textanalyzer.classifier.common.ClassifierEnum;
import com.mai.textanalyzer.indexing.common.IndexerEnum;
import java.util.Map;

/**
 *
 * @author Sergey
 */
public interface IAccuracyService {

    Map<String, Accuracy> getMapAccyracy(ClassifierEnum classifierEnum, IndexerEnum indexerEnum);

    public void inserOrUpdateAccyracy(Accuracy accuracy);

}
