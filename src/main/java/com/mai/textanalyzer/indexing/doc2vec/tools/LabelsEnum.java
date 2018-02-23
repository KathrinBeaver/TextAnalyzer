/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.indexing.doc2vec.tools;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sergey
 */
public enum LabelsEnum {
    AVIA("Авиация и космонавтика"),
    ARCHITECTURE("Архитектура"),
    ASTRONOMY("Астрономия"),
    BIOLOGY("Биология"),
    COMPUTER_SINCE("Компьютерные науки"),
    MATHEMATICS("Математика"),
    ECONOMY("Экономика");
    String name;

    private LabelsEnum(String name) {
        this.name = name;
    }

    public static List<String> getListLabels() {
        List<String> list = new ArrayList();
        for (LabelsEnum labels : values()) {
            list.add(labels.name);
        }
        return list;
    }

}
