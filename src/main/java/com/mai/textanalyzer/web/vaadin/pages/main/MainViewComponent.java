/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.web.vaadin.pages.main;

import com.mai.textanalyzer.web.vaadin.pages.AbstractPageComponent;
import com.mai.textanalyzer.web.vaadin.pages.classification.ClassificationComponent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.UI;

/**
 *
 * @author S.Belov
 */
public class MainViewComponent extends AbstractPageComponent {

    public static final String VIEW_NAME = "main";
    public static final String VIEW_HEADER = "Главная";

    @Override
    protected void initInnerPage(ViewChangeListener.ViewChangeEvent event) {
        ClassificationComponent classificationComponent = new ClassificationComponent();
        classificationComponent.setSizeFull();
        innerLayoutV.addComponent(classificationComponent);
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
