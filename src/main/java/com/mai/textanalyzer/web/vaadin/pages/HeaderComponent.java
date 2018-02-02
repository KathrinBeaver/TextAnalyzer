/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.web.vaadin.pages;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 *
 * @author S.Belov
 */
public class HeaderComponent extends CustomComponent {

    private final Label analyzerLabel = new Label();

    public HeaderComponent() {
        init();
    }

    private void init() {

        Label spacer = new Label();
        spacer.setWidth(10, Unit.PIXELS);
        spacer.setHeight(1, Unit.PIXELS);
        analyzerLabel.setContentMode(ContentMode.HTML);
        analyzerLabel.setValue("<h2 style='color:black;'>Текстовый анализатор.</h2>");

        HorizontalLayout mainLayoutH = new HorizontalLayout();
        mainLayoutH.setWidth("100%");
        mainLayoutH.setSpacing(true);
        mainLayoutH.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        mainLayoutH.addComponent(spacer);
        mainLayoutH.setExpandRatio(spacer, 0);
//        mainLayoutH.addComponent(warnImage);
//        mainLayoutH.setExpandRatio(warnImage, 0);
        mainLayoutH.addComponent(analyzerLabel);
        mainLayoutH.setExpandRatio(analyzerLabel, 1);

        setCompositionRoot(mainLayoutH);
    }

}
