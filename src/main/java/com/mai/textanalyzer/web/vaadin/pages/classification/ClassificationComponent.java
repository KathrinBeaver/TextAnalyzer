/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.web.vaadin.pages.classification;

import com.mai.textanalyzer.ui.file_upload.DocUploader;
import com.mai.textanalyzer.ui.vectorization.VectorizationPanel;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;

/**
 *
 * @author s.belov
 */
public class ClassificationComponent extends CustomComponent {

    private final VectorizationPanel panel = new VectorizationPanel();
    private final DocUploader receiver = new DocUploader();
    private final Upload upload = new Upload("Загрузите текст здесь", receiver);
    private final Button startClassification = new Button("Начать классификацию");

    public ClassificationComponent() {//https://dev.vaadin.com/svn/doc/book-examples/branches/vaadin-7/src/com/vaadin/book/examples/component/UploadExample.java
        initComponents();
        initListiners();
    }

    private void initComponents() {
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);

        upload.setButtonCaption("Начать загрузку");
        upload.addSucceededListener(receiver);
        startClassification.setEnabled(false);

        layout.addComponents(panel);
        layout.addComponents(upload);
        layout.addComponents(startClassification);

        layout.setMargin(true);
        layout.setSpacing(true);
        setCompositionRoot(layout);
    }

    private void initListiners() {
        upload.addProgressListener(new Upload.ProgressListener() {
            @Override
            public void updateProgress(long readBytes, long contentLength) {
                Notification.show("Загрузка", readBytes + "", Notification.Type.TRAY_NOTIFICATION);
                receiver.getStream();
                startClassification.setEnabled(true);
            }
        });

        upload.addFinishedListener((event) -> {
            Notification.show("Загрузка", "Загрузка завершена", Notification.Type.HUMANIZED_MESSAGE);
            receiver.getStream();
            startClassification.setEnabled(true);
        });
    }

}
