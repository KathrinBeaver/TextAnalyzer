/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.ui.indexing;

import com.mai.textanalyzer.indexing.common.IndexerEnum;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import java.util.EnumSet;

/**
 *
 * @author Sergey
 */
public class VectorizationPanel extends CustomComponent {

    public VectorizationPanel() {
        EnumSet<IndexerEnum> enumSet = EnumSet.allOf(IndexerEnum.class);
        ComboBox cb = new ComboBox();
//        cb.setItems(enumSet);
        cb.addItems(enumSet);
        cb.setWidth("250px");
        Panel panel = new Panel(new FormLayout(cb));
        panel.setCaption("Выберете способ векторизации:");
        panel.setWidth("100%");
        setCompositionRoot(panel);
    }

}
