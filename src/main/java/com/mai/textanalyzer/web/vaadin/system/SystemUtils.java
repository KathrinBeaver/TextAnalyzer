/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.web.vaadin.system;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;
import java.io.PrintStream;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author S.Belov
 */
public abstract class SystemUtils {

    public static final String CURRENT_THEME = "AnalyzerTheme";

    public static final PrintStream ORIG_TRACE_STREAM = System.out;

    public static volatile PrintStream TRACE_STREAM = ORIG_TRACE_STREAM;
    /**
     * Длительность сессии для авторизованных пользователей 30 мин
     */
    public static final int AUTORIZED_SESSION_INACTIVE_INTERVAL = 60 * 30;

    public static void setTraceStream(PrintStream newStream) {
        if (newStream == null) {
            return;
        }
        TRACE_STREAM = newStream;
    }

    public static Context createNamingContext() {
        try {
            return (Context) new InitialContext().lookup("java:comp/env");
        } catch (NamingException ex) {
            ex.printStackTrace(SystemUtils.TRACE_STREAM);
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Получение префикса приложения
     *
     * @return
     */
    public static String getContextPath() {
        return VaadinService.getCurrentRequest().getContextPath().replace("^\\/", "");
    }

    /**
     * Обертка для сохранения объекта в http сессию
     *
     * @param attribute ключ объекта в виде элемента enum
     * @param value значение
     */
//    public static void setAttributeToHttpSession(HttpSessionAttribute attribute, Object value) {
//        VaadinSession.getCurrent().getSession().setAttribute(attribute.getAttributeName(), value);
//    }
    /**
     * Обертка для получения объекта из http сессии
     *
     * @param attribute ключ объекта в виде элемента enum
     * @return значение
     */
//    public static Object getAttributeFromHttpSession(HttpSessionAttribute attribute) {
//        VaadinSession vaadinSession = VaadinSession.getCurrent();
//        if (vaadinSession == null) {
//            return null;
//        }
//        WrappedSession wrappedSession = vaadinSession.getSession();
//        if (wrappedSession == null) {
//            return null;
//        }
//        return wrappedSession.getAttribute(attribute.getAttributeName());
//    }
    /**
     * Обертка для удаления объекта из http сессии
     *
     * @param attribute ключ объекта в виде элемента enum
     */
//    public static void removeAttributeFromHttpSession(HttpSessionAttribute attribute) {
//        VaadinSession vaadinSession = VaadinSession.getCurrent();
//        if (vaadinSession == null) {
//            return;
//        }
//        vaadinSession.getSession().removeAttribute(attribute.getAttributeName());
//    }
    /**
     * Получение IP из Vaadin контекста
     *
     * @return
     */
    public static String getIp() {
        String ip = UI.getCurrent().getPage().getWebBrowser().getAddress();
        return ip.replaceFirst("^(0:){7}1$", "127.0.0.1");
    }

}
