/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.vectorization.ui;

import com.mai.textanalyzer.vectorization.common.VectorizationEnum;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;

/**
 *
 * @author Sergey
 */
public class VectorizationPanel extends CustomComponent {

    public VectorizationPanel() {
        ComboBox<VectorizationEnum> cb = new ComboBox<>();
        cb.setItems(VectorizationEnum.values());
        Panel panel = new Panel();
        panel.setCaption("Выберете способ векторизации:");
        panel.setContent(cb);
        setCompositionRoot(panel);
    }

}
