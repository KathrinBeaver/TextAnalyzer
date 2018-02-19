/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.web.vaadin.ui;

import com.mai.textanalyzer.ui.vectorization.VectorizationPanel;
import com.mai.textanalyzer.web.vaadin.pages.main.MainComponent;
import com.mai.textanalyzer.web.vaadin.system.SystemUtils;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 *
 * @author Sergey
 */
@Theme(SystemUtils.CURRENT_THEME)
@Title("Текстовый анализатор")
public class PageUI extends AbstractAnalyzerUI {

    public static final String URL_PREFIX = "PageUI";
    public static final String DEFAULT_VIEW = MainComponent.VIEW_NAME;

    private Navigator navigator;

    public PageUI() {
        super();
    }

    @Override
    protected void init(VaadinRequest request) {
        super.init(request);
        initComponents();
        String redirectPrefix = SystemUtils.getContextPath() + "/" + URL_PREFIX + "#!";
        UI.getCurrent().getPage().setLocation(redirectPrefix + PageUI.DEFAULT_VIEW);
    }

    private void initComponents() {
        navigator = new Navigator(this, this);
//        navigator.setErrorView(new PageNotFoundViewComponent());

//        User user = SystemUtils.getUserFromVaadinSession();
//        if (user == null) {
//            return;
//        }
        navigator.addView(MainComponent.VIEW_NAME, new MainComponent());
        navigator.addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeListener.ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeListener.ViewChangeEvent event) {
                for (Window window : getWindows()) {
                    if (window.isModal()) {
                        window.close();
                    }
                }
            }
        });
    }

    @VaadinServletConfiguration(ui = PageUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

}
