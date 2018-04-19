/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.creater;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;

/**
 *
 * @author Sergey
 */
public class CreaterClassifier {

    public static AbstractClassifier getClassifier(ClassifierEnum classifier) {
        if (classifier == ClassifierEnum.NAIVE_BAYES) {
            return new NaiveBayes();
        }
        if (classifier == ClassifierEnum.SVM) {
            return new SMO();
        }
        throw new UnsupportedOperationException("Classifier support for" + classifier.name() + " not yet added");
    }
}
