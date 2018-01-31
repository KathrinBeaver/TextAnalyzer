/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.ui;

import com.mai.textanalyzer.vectorization.ui.VectorizationPanel;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

/**
 *
 * @author Sergey
 */
@Theme("NewTh")
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        VectorizationPanel panel = new VectorizationPanel();
        layout.addComponents(panel);
        layout.setMargin(true);
//        layout.setSpacing(true);
         
        setContent(layout);
    }

    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

}
