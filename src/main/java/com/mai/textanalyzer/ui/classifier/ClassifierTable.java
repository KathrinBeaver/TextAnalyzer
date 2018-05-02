/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.ui.classifier;

import com.mai.textanalyzer.classifier.common.ClassifierEnum;
import com.mai.textanalyzer.creater.Creater;
import com.mai.textanalyzer.indexing.common.IndexerEnum;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import java.io.File;

/**
 *
 * @author Sergey
 */
public class ClassifierTable extends CustomComponent {

    private final Table classifierTable = new Table("Классификаторы:");
    private IndexerEnum indexerEnum = IndexerEnum.DOC2VEC;
    private final File rootDir;

    public ClassifierTable(File rootDir) {
        this.rootDir = rootDir;
        classifierTable.setSelectable(true);
        classifierTable.addContainerProperty("Классификатор", ClassifierEnum.class, null);
        classifierTable.addContainerProperty("Наличие", Component.class, null);
        refreshData();
        this.setCompositionRoot(classifierTable);
    }

    public void setIndexerEnum(IndexerEnum indexerEnum) {
        this.indexerEnum = indexerEnum;
        refreshData();
    }

    public ClassifierEnum getSelectedValue() {
        Object rowId = classifierTable.getValue();
        if (rowId == null) {
            return null;
        }
        return (ClassifierEnum) classifierTable.getContainerProperty(rowId, "Классификатор").getValue();
    }

    public void refreshData() {
        classifierTable.removeAllItems();
        int count = 0;
        for (ClassifierEnum classifierEnum : ClassifierEnum.values()) {
            classifierTable.addItem(new Object[]{classifierEnum, getIconForClassifier(classifierEnum)}, count);
            count++;
        }
        classifierTable.setPageLength(classifierTable.size());
    }

    private Component getIconForClassifier(ClassifierEnum classifierEnum) {
        if (Creater.checkExistClassifierModel(rootDir, classifierEnum, indexerEnum)) {
            return new Embedded("", new ThemeResource("icons/" + "ok" + ".png"));
        } else {
            return new Embedded("", new ThemeResource("icons/" + "no" + ".png"));
        }
    }

}
