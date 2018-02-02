/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.web.vaadin.ui;

import com.mai.textanalyzer.web.vaadin.system.SystemUtils;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.apache.log4j.Logger;

/**
 * Обертка vaadin UI для реализации механизмов, использующихся на всех vaadin
 * страницах приложения
 *
 * @author S.Belov
 */
@Theme(SystemUtils.CURRENT_THEME)
public abstract class AbstractAnalyzerUI extends UI {

    protected boolean haveInitExceptions = false;
//    protected ConnectorTracker tracker;
    protected Logger logger = Logger.getLogger(this.getClass());

    public AbstractAnalyzerUI() {
        super();
    }

    public AbstractAnalyzerUI(Component content) {
        super(content);
    }

    @Override
    protected void init(VaadinRequest request) {
        UI.getCurrent().getReconnectDialogConfiguration().setDialogText("Соединение с сервером потеряно, пытаемся восстановить...");
    }

//    @Override
//    public ConnectorTracker getConnectorTracker() {
//        if (this.tracker == null) {
//            this.tracker = new ConnectorTracker(this) {
//
//                @Override
//                public void registerConnector(ClientConnector connector) {
//                    try {
//                        super.registerConnector(connector);
//                    } catch (RuntimeException e) {
//                        logger.error("Failed connector: " + connector.getClass().getSimpleName());
//                        throw e;
//                    }
//                }
//
//            };
//        }
//
//        return tracker;
//    }
}
