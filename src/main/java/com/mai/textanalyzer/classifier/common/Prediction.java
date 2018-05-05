/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.classifier.common;

import java.util.Objects;

/**
 *
 * @author Sergey
 */
public class Prediction implements Comparable<Prediction> {

    private final String topic;
    private double value;

    public Prediction(String topic) {
        this.topic = topic;
    }

    public Prediction(String topic, double value) {
        this.topic = topic;
        this.value = value;
    }

    public String getTopic() {
        return topic;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.topic);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Prediction other = (Prediction) obj;
        if (!Objects.equals(this.topic, other.topic)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Prediction t) {
        return Double.compare(value, t.value);
    }

    @Override
    public String toString() {
        return String.format("%.2f", value * 100);
    }

}
