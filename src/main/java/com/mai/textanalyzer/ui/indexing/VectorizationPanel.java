/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.ui.indexing;

import com.mai.textanalyzer.indexing.common.IndexerEnum;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import java.util.EnumSet;

/**
 *
 * @author Sergey
 */
public class VectorizationPanel extends CustomField<IndexerEnum> {

    private final ComboBox cb = new ComboBox();

    public VectorizationPanel() {
        super();
    }

    @Override
    public IndexerEnum getValue() {
        return (IndexerEnum) cb.getValue();
    }

    @Override
    public void setValue(IndexerEnum newFieldValue) throws ReadOnlyException, Converter.ConversionException {
        super.setValue(newFieldValue);
    }

    @Override
    protected Component initContent() {
        EnumSet<IndexerEnum> enumSet = EnumSet.allOf(IndexerEnum.class);
        cb.setNullSelectionAllowed(false);
        cb.addItems(enumSet);
        cb.select(IndexerEnum.DOC2VEC);
        cb.setWidth("250px");
        Panel panel = new Panel(new FormLayout(cb));
        panel.setCaption("Выберете способ векторизации:");

        cb.addValueChangeListener((event) -> {
            this.setValue((IndexerEnum) cb.getValue());
        });

        return panel;
    }

    @Override
    public Class<? extends IndexerEnum> getType() {
        return IndexerEnum.class;
    }

}
