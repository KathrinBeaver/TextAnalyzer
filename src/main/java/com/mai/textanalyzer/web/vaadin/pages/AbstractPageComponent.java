/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.web.vaadin.pages;

import com.mai.textanalyzer.web.vaadin.system.SystemUtils;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Абстрактный компонент, используемый как скелет страницы. Поддерживает
 * проверку на включенный режим техобслуживания. См. методы. По умолчанию не
 * проверяется.
 *
 * @author S.Belov
 */
public abstract class AbstractPageComponent extends CustomComponent implements View {

    /**
     * лейаут для основных данных страницы
     */
    protected VerticalLayout innerLayoutV;
    /**
     * Заголовок для основных данных страницы
     */
    protected Label innerLabel;
    protected Navigator navigator = null;
    protected transient Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Инициализация базовых компонентов. Вызывайте его на каждом переключении
     * вьюшки.
     *
     */
    protected void initComponents() {
        innerLabel = new Label();
        innerLabel.setContentMode(ContentMode.HTML);
        innerLabel.setSizeUndefined();

        innerLayoutV = new VerticalLayout();
        innerLayoutV.setSizeFull();
        innerLayoutV.setMargin(new LeftRightMarginInfo());

//        MenuComponent menuComponent = new MenuComponent(navigator);
        HeaderComponent headerComponent = new HeaderComponent();
        FooterComponent footerComponent = new FooterComponent();

        HorizontalLayout innerLabelLayoutH = new HorizontalLayout();
        innerLabelLayoutH.setSizeFull();
        innerLabelLayoutH.setHeight("40px");
        innerLabelLayoutH.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        innerLabelLayoutH.addComponent(innerLabel);

        VerticalLayout mainLayoutV = new VerticalLayout();
        mainLayoutV.setSizeFull();
        mainLayoutV.addComponent(headerComponent);
        mainLayoutV.setExpandRatio(headerComponent, 0);
//        mainLayoutV.addComponent(menuComponent);
//        mainLayoutV.setExpandRatio(menuComponent, 0);
        mainLayoutV.addComponent(innerLabelLayoutH);
        mainLayoutV.setExpandRatio(innerLabelLayoutH, 0);
        mainLayoutV.addComponent(innerLayoutV);
        mainLayoutV.setExpandRatio(innerLayoutV, 1);
        mainLayoutV.addComponent(footerComponent);
        mainLayoutV.setExpandRatio(footerComponent, 0);

        setCompositionRoot(mainLayoutV);
        setSizeFull();
    }

    /**
     * Создание кнопки, для перехода на вьюшку
     *
     * @param buttonName
     * @param viewName
     * @return
     */
    protected Button createMoveToViewButton(String buttonName, final String viewName) {
        return createMoveToViewButton(buttonName, new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                navigator.navigateTo(viewName);
            }
        });
    }

    /**
     * Создание кнопки, для перехода на вьюшку
     *
     * @param buttonName
     * @param clickListener
     * @return
     */
    protected Button createMoveToViewButton(String buttonName, final Button.ClickListener clickListener) {
        final Button button = new Button(buttonName);
        button.setStyleName(ValoTheme.BUTTON_LINK);
        button.addClickListener(clickListener);
        return button;
    }

    /**
     * Вид странички при техобслуживании
     */
    protected void initMaintenancePage() {
        innerLayoutV.addComponent(new Label("<span style='color:#aa0000;'>Данный функционал временно недоступен</span>", ContentMode.HTML));
    }

    /**
     * Наполнение вложенной страницы
     *
     * @param event
     */
    protected abstract void initInnerPage(ViewChangeEvent event);

    /**
     * Наполнение вложенной страницы
     *
     * @param event
     */
    protected abstract void initPageTitle(ViewChangeEvent event);

    protected void initPage(ViewChangeEvent event) {
        initPageTitle(event);
        initInnerPage(event);
    }

    /**
     * Установка заголовка у нашего окна
     *
     * @param text
     */
    protected void setInnerLabelText(final String text) {
        if (text == null) {
            throw new IllegalArgumentException();
        }
        if (innerLabel == null) {
            return;
        }
        innerLabel.setValue("<span style='font-size: 20px; display: inline-block;'>" + text + "</span>");
        Page.getCurrent().setTitle("Текстовый анализатор / " + text);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        if (navigator == null) {
            navigator = event.getNavigator();
        }
        initComponents();
        initPage(event);
    }

    @Override
    public void detach() {
        setCompositionRoot(null);
        super.detach();
        innerLabel = null;
        innerLayoutV = null;
    }

    /**
     * Обработка десериализации. Не удалять!!!
     *
     * @param in
     */
    private void readObject(ObjectInputStream in) {
        try {
            in.defaultReadObject();
            //т.к. поле финальное, перезапишем его через рефлексию
            Field roadField = AbstractPageComponent.class.getDeclaredField("logger");
            roadField.setAccessible(true);
            roadField.set(this, LoggerFactory.getLogger(this.getClass()));
            roadField.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace(SystemUtils.TRACE_STREAM);
            throw new RuntimeException(e);
        }
    }

}
