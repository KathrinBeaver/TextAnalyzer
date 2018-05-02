/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.ui.file_upload;

import com.mai.textanalyzer.web.vaadin.pages.classification.ClassificationComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Sergey
 */
public class UploadPanel extends CustomComponent {

    private final DocUploader receiver = new DocUploader();
    private final Upload upload = new Upload("", receiver);
    private final Label labelInfo = new Label();
    private final TextArea area = new TextArea("Big Area");
    private final Button buttonArea = new Button("Загрузить текст");
    private String document = "";

    public UploadPanel() {

        area.setWidth("650");
        area.setHeight("350");
        area.setWordwrap(false);
        upload.setButtonCaption("загрузить текст");
        upload.addSucceededListener(receiver);

        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(area);
        layout.addComponent(buttonArea);
        layout.addComponent(upload);
        layout.addComponent(labelInfo);

        Panel uploadPanel = new Panel("Загрузка текста");
        uploadPanel.setContent(layout);
        initListeners();
        this.setCompositionRoot(uploadPanel);
    }

    private void initListeners() {
        buttonArea.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                String text = area.getValue();
                if (text == null || text.isEmpty()) {
                    labelInfo.setCaption("Область пустая");
                    return;
                }
                document = text;
                labelInfo.setCaption("текст был загружен из TextArea");
            }
        });
        upload.addProgressListener(new Upload.ProgressListener() {
            private static final long serialVersionUID = 4728847902678459488L;

            @Override
            public void updateProgress(long readBytes, long contentLength) {
                labelInfo.setCaption("Загрузка документа " + readBytes);
                Notification.show("Загрузка", readBytes + "", Notification.Type.TRAY_NOTIFICATION);
            }
        });
        upload.addFinishedListener((event) -> {
            Notification.show("Загрузка", "Загрузка завершена", Notification.Type.HUMANIZED_MESSAGE);
            try {
                document = receiver.getDoc();
            } catch (UnsupportedEncodingException ex) {
                labelInfo.setCaption("Возникла ошибка при загрузке");
            }
            labelInfo.setCaption("документ был загружен");
        });
    }

    public String getDocument() {
        return document;
    }

}
