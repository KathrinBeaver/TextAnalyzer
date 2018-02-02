/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.web.vaadin.pages;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author A.Kumanyaev
 */
public class FooterComponent extends CustomComponent {

    public FooterComponent() {
        initComponents();
    }

    private void initComponents() {
//        WebApplicationContext context = ContextLoaderListener.getCurrentWebApplicationContext();
//        Version version = context.getBean(Version.class);
//        Label footerLabel = new Label("<table><tr><td align='right' nowrap>" + version.toString() + ", " + version.getCopyright() + "&nbsp;&nbsp;</td></tr></table>");
        Label footerLabel = new Label("<table><tr><td align='right' nowrap>" + "unknown" + "&nbsp;&nbsp;</td></tr></table>");
        footerLabel.setContentMode(ContentMode.HTML);
        footerLabel.setSizeUndefined();

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        mainLayout.setSizeUndefined();
        mainLayout.setWidth("100%");
        mainLayout.addComponent(footerLabel);
        setCompositionRoot(mainLayout);
    }

}
