/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.web.vaadin.pages.classification;

import com.mai.textanalyzer.classifier.common.ClassifierEnum;
import com.mai.textanalyzer.classifier.common.Prediction;
import com.mai.textanalyzer.classifier.common.TextClassifier;
import com.mai.textanalyzer.indexing.common.Indexer;
import com.mai.textanalyzer.indexing.common.IndexerEnum;
import com.mai.textanalyzer.ui.classifier.ClassifierTable;
import com.mai.textanalyzer.ui.file_upload.UploadPanel;
import com.mai.textanalyzer.ui.indexing.VectorizationPanel;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 *
 * @author s.belov
 */
public class ClassificationComponent extends CustomComponent {

    private final VectorizationPanel indexerPanel = new VectorizationPanel();
    private final UploadPanel uploadPanel = new UploadPanel();
    private final Button startClassificationButton = new Button("Начать классификацию");
    private static final Logger LOG = Logger.getLogger(ClassificationComponent.class);
    private final ClassifierTable classifierTable = new ClassifierTable(LoadingComponents.getRootDir());
    private final Table topicTable = new Table("Совпадения с темами:") {
        @Override
        protected String formatPropertyValue(Object rowId,
                Object colId, Property property) {
            if (property.getType() == Prediction.class && property.getValue() != null) {
                return ((Prediction) property.getValue()).toString();
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    public ClassificationComponent() {
        initComponents();
        initListiners();
    }

    private void initComponents() {

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setSpacing(true);
        hLayout.setMargin(true);
        startClassificationButton.setEnabled(true);
        hLayout.addComponents(indexerPanel);
        hLayout.addComponents(classifierTable);
        hLayout.addComponents(uploadPanel);

        topicTable.addContainerProperty("Имя категории", String.class, null);
        topicTable.addContainerProperty("Вероятность принадлежности к категории", Prediction.class, null);

        VerticalLayout vLayout = new VerticalLayout();
        vLayout.setSizeFull();
        vLayout.addComponent(hLayout);
        vLayout.addComponent(startClassificationButton);
        topicTable.setSizeFull();
        vLayout.addComponent(topicTable);

        Panel resultPanel = new Panel("resultPanel");
        resultPanel.setSizeFull();
        resultPanel.setContent(vLayout);
        setCompositionRoot(resultPanel);
    }

    private void initListiners() {
        indexerPanel.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                classifierTable.setIndexerEnum(indexerPanel.getValue());
            }
        });

        startClassificationButton.addClickListener((Button.ClickEvent event) -> {
            topicTable.removeAllItems();
            String document = uploadPanel.getDocument();
            if (document == null || document.isEmpty()) {
                Notification.show("", "Документ не загружен", Notification.Type.HUMANIZED_MESSAGE);
                return;
            }
            ClassifierEnum selectedClassifier = classifierTable.getSelectedValue();
            if (selectedClassifier == null) {
                Notification.show("", "Не выбран классификатор", Notification.Type.HUMANIZED_MESSAGE);
                return;
            }
            IndexerEnum indexerEnum = indexerPanel.getValue();
            TextClassifier textClassifier = LoadingComponents.getClassifier(selectedClassifier, indexerEnum);
            if (textClassifier == null) {
                Notification.show("", "Выбранный классификатор еще не обучен", Notification.Type.HUMANIZED_MESSAGE);
                return;
            }
            Indexer indexer = LoadingComponents.getIndexer(indexerPanel.getValue());
            INDArray iNDArray = indexer.getIndex(document);
            if (iNDArray == null) {
                Notification.show("", "В тексте недостаточно информации для его классификации", Notification.Type.HUMANIZED_MESSAGE);
                return;
            }
            List<Prediction> predictions = textClassifier.getDistribution(iNDArray);
            int count = 1;
            for (Prediction prediction : predictions) {
                topicTable.addItem(new Object[]{prediction.getTopic(), prediction}, count);
                count++;
            }
            topicTable.setPageLength(topicTable.size());
            Object[] properties = {"Вероятность принадлежности к категории", "Имя категории"};
            boolean[] ordering = {false, true};
            topicTable.sort(properties, ordering);
        });

    }

}
