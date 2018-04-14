/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.indexing.tf_idf;

import java.util.Enumeration;
import weka.core.stemmers.SnowballStemmer;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 *
 * @author Sergey
 */
public class Test {

    public static void main(String[] args) {

        StringToWordVector stwv = new StringToWordVector();

        Enumeration<String> e = SnowballStemmer.listStemmers();
        while (e.hasMoreElements()) {
            System.out.println(e.nextElement());

        }

    }
}
