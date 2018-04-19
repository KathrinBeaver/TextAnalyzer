/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.web.vaadin.pages.classification;

import com.mai.textanalyzer.indexing.doc2vec.Doc2Vec;
import com.mai.textanalyzer.indexing.doc2vec.Doc2VecUtils;
import com.mai.textanalyzer.ui.file_upload.DocUploader;
import com.mai.textanalyzer.ui.indexing.VectorizationPanel;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.nd4j.linalg.primitives.Pair;

/**
 *
 * @author s.belov
 */
public class ClassificationComponent extends CustomComponent {

    private final File saveModelFile = new File("G:/DocForTest/SaveModel/1/Doc2Vec");

    private final VectorizationPanel panel = new VectorizationPanel();
    private Doc2Vec doc2Vec;
    private final DocUploader receiver = new DocUploader();
    private final Upload upload = new Upload("Загрузите текст здесь", receiver);
    private final Button startClassificationButton = new Button("Начать классификацию");
    private static final Logger LOG = Logger.getLogger(ClassificationComponent.class);
    private final Table topicTable = new Table("Совпадения с темами:");

    public ClassificationComponent() {
        initComponents();
        initListiners(upload);
    }

    private void initComponents() {
        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setSpacing(true);

        upload.setButtonCaption("Начать загрузку");
        upload.addSucceededListener(receiver);
        startClassificationButton.setEnabled(true);
        hLayout.addComponents(panel);
        hLayout.addComponents(upload);
        hLayout.addComponents(startClassificationButton);
        startClassificationButton.setEnabled(false);
        hLayout.setMargin(true);
        hLayout.setSpacing(true);

        topicTable.addContainerProperty("Имя категории", String.class, null);
        topicTable.addContainerProperty("Близость к категории [-1,1]", Double.class, null);

        VerticalLayout vLayout = new VerticalLayout();
        vLayout.addComponent(hLayout);
        vLayout.addComponent(topicTable);

        setCompositionRoot(vLayout);

//        doc2Vec = Doc2VecUtils.loadModel(saveModelFile);
    }

    private void initListiners(Upload upload) {
        upload.addProgressListener(new Upload.ProgressListener() {
            private static final long serialVersionUID = 4728847902678459488L;

            @Override
            public void updateProgress(long readBytes, long contentLength) {
                Notification.show("Загрузка", readBytes + "", Notification.Type.TRAY_NOTIFICATION);
            }
        });

        upload.addFinishedListener((event) -> {
            Notification.show("Загрузка", "Загрузка завершена", Notification.Type.HUMANIZED_MESSAGE);
//            receiver.getStream();
            startClassificationButton.setEnabled(true);
        });
        startClassificationButton.addClickListener((Button.ClickEvent event) -> {
            Notification.show("Обработка", "Началась обработка текста", Notification.Type.HUMANIZED_MESSAGE);
            String document = null;
            try {
                document = receiver.getDoc();
            } catch (UnsupportedEncodingException ex) {
                java.util.logging.Logger.getLogger(ClassificationComponent.class.getName()).log(Level.SEVERE, null, ex);
            }
            int count = 1;
            for (Pair<String, Double> pairs : Doc2VecUtils.getTopics(doc2Vec, document)) {
                topicTable.addItem(new Object[]{pairs.getFirst(), pairs.getSecond()}, count);
                count++;
            }
            topicTable.setPageLength(topicTable.size());
        });

    }

}
