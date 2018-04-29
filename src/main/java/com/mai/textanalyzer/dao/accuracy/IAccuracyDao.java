/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.dao.accuracy;

import com.mai.textanalyzer.classifier.common.ClassifierEnum;
import com.mai.textanalyzer.indexing.common.IndexerEnum;
import java.util.List;

/**
 *
 * @author Sergey
 */
public interface IAccuracyDao {

    void deleteAllDataFromAccuracy();

    List<Accuracy> getListAccuracy(ClassifierEnum classifierEnum, IndexerEnum indexerEnum);

    void inserOrUpdateAccyracy(ClassifierEnum classifierEnum, IndexerEnum indexerEnum, int topic_id, int docCount, double accuracy);
}
