/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.web.vaadin.pages.main;

import com.mai.textanalyzer.vectorization.ui.VectorizationPanel;
import com.mai.textanalyzer.web.vaadin.pages.AbstractPageComponent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

/**
 *
 * @author S.Belov
 */
public class MainComponent extends AbstractPageComponent {

    public static final String VIEW_NAME = "main";
    public static final String VIEW_HEADER = "Главная";

    @Override
    protected void initInnerPage(ViewChangeListener.ViewChangeEvent event) {
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        VectorizationPanel panel = new VectorizationPanel();
        layout.addComponents(panel);
        layout.setMargin(true);
        layout.setSpacing(true);
        innerLayoutV.addComponent(layout);
    }

    @Override
    protected void initPageTitle(ViewChangeListener.ViewChangeEvent event) {
        setInnerLabelText(VIEW_HEADER);
    }

    public static void navigateToView() {
        String uriFragment = VIEW_NAME;
        UI.getCurrent().getNavigator().navigateTo(uriFragment);
    }

}
