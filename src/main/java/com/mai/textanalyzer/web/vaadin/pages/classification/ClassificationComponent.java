/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.web.vaadin.pages.classification;

import com.mai.textanalyzer.indexing.doc2vec.Doc2VecUtils;
import com.mai.textanalyzer.ui.file_upload.DocUploader;
import com.mai.textanalyzer.ui.vectorization.VectorizationPanel;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;

/**
 *
 * @author s.belov
 */
public class ClassificationComponent extends CustomComponent {

    private final VectorizationPanel panel = new VectorizationPanel();
    private ParagraphVectors pv;
    private final DocUploader receiver = new DocUploader();
    private final Upload upload = new Upload("Загрузите текст здесь", receiver);
    private final Button startClassificationButton = new Button("Начать классификацию");
    private static final Logger LOG = Logger.getLogger(ClassificationComponent.class);

    public ClassificationComponent() {//https://dev.vaadin.com/svn/doc/book-examples/branches/vaadin-7/src/com/vaadin/book/examples/component/UploadExample.java
        initComponents();

    }

    private void initComponents() {
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        initListiners(upload);
        upload.setButtonCaption("Начать загрузку");
        upload.addSucceededListener(receiver);
        startClassificationButton.setEnabled(true);

        layout.addComponents(panel);
        layout.addComponents(upload);
        layout.addComponents(startClassificationButton);
        startClassificationButton.setEnabled(false);
        layout.setMargin(true);
        layout.setSpacing(true);
        setCompositionRoot(layout);
        LOG.info(new File("srs/main").isDirectory());
        LOG.info(new File("/srs/main").isDirectory());
        LOG.info(new File("main\\resours").isDirectory());
        LOG.info(new File("\\main\\resours").isDirectory());
        LOG.info(new File("resours\\save_model").isDirectory());
        LOG.info(new File("\\resours\\save_model").isDirectory());
        LOG.info(getClass().getResource("save_model/doc2vec/Doc2Vec") != null);
        LOG.info(getClass().getResourceAsStream("/save_model/doc2vec/Doc2Vec") != null);

        LOG.info(new File("G:\\DocForTest\\SaveModel\\Doc2Vec").exists());

        pv = Doc2VecUtils.loadModel(getClass().getResourceAsStream("/save_model/doc2vec/Doc2Vec"));
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
            LOG.info(Doc2VecUtils.getTopics(pv, document));
        });

    }

}
