/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.classifier.weka_classifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sergey
 */
public final class WekaUtils {

    private static Logger log = Logger.getLogger(WekaUtils.class.getName());

    public static WekaClassifier loadModel(File wekaClassifierLocation) {
        WekaClassifier classifier = null;
        try {
            FileInputStream modelInFile = new FileInputStream(wekaClassifierLocation);
            ObjectInputStream modelInObjectFile = new ObjectInputStream(modelInFile);
            classifier = (WekaClassifier) modelInObjectFile.readObject();
            modelInFile.close();
        } catch (IOException | ClassNotFoundException ex) {
            log.log(Level.WARNING, ex.getMessage());
            ex.printStackTrace(System.err);
        }
        return classifier;
    }

    public static void saveModel(WekaClassifier wc, File wekaClassifierLocation) {
        try {
            try (FileOutputStream modelOutFile = new FileOutputStream(wekaClassifierLocation)) {
                ObjectOutputStream modelOutObjectFile = new ObjectOutputStream(modelOutFile);
                modelOutObjectFile.writeObject(wc);
                modelOutObjectFile.flush();
            }
        } catch (IOException ex) {
            log.log(Level.WARNING, ex.getMessage());
            ex.printStackTrace(System.err);
        }

    }

}
