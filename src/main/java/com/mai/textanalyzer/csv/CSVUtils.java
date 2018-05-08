/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.csv;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.mai.textanalyzer.creater.Creater;
import com.mai.textanalyzer.indexing.common.BasicTextModel;
import com.mai.textanalyzer.indexing.common.Indexer;
import com.mai.textanalyzer.indexing.common.IndexerEnum;
import com.mai.textanalyzer.word_processing.RusUTF8FileLabelAwareIterator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.nd4j.linalg.api.iter.NdIndexIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 *
 * @author Sergey
 */
public final class CSVUtils {

    private static final String CSV_NAME_FILE = "data.csv";
    public static final String CSV_SEPARATOR = ",";

    private CSVUtils() {
    }

    public static void writeCSVData(File dataFile, List<String[]> rows) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(dataFile));
            for (String[] row : rows) {
                writer.writeNext(row);
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(CSVUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addCSVData(File dataFile, List<String[]> rows) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(dataFile));
            for (String[] row : rows) {
                writer.writeNext(row);
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(CSVUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createCSVData(File rootFolder, IndexerEnum indexerEnum, DataType dataType) {
        Indexer indexer = Creater.loadIndexer(indexerEnum, rootFolder);
        File docsFolder;
        if (dataType == DataType.LEARNING) {
            docsFolder = Creater.getDocForLearningFolder(rootFolder);
        } else if (dataType == DataType.TEST) {
            docsFolder = Creater.getDocForTestFolder(rootFolder);
        } else {
            throw new UnsupportedOperationException();
        }
        RusUTF8FileLabelAwareIterator iteratorDoc = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(docsFolder)
                .build();
        int size = iteratorDoc.getSize();
        int countDoc = 0;
        List<String[]> rows = new ArrayList<>();
        while (iteratorDoc.hasNext()) {
            LabelledDocument next = iteratorDoc.next();
            INDArray iNDArray = indexer.getIndex(next.getContent());
            String[] csvRow = new String[iNDArray.length() + 1];
            NdIndexIterator iter2 = new NdIndexIterator(iNDArray.shape());
            int counter = 0;
            while (iter2.hasNext()) {
                int[] nextIndex = iter2.next();
                csvRow[counter] = iNDArray.getDouble(nextIndex) + "";
                counter++;
            }
            csvRow[csvRow.length - 1] = next.getLabel();
            rows.add(csvRow);
            countDoc++;
            System.out.println(countDoc + "/" + size);
        }
        writeCSVData(getDataCSVFile(rootFolder, indexerEnum, dataType), rows);
    }

    public static List<BasicTextModel> readCSVData(File dataFile) {
        List<BasicTextModel> result = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(dataFile), ',', '"', 0);
            //Read CSV line by line and use the string array as you want
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine != null) {
                    List<Double> featureArray = new ArrayList<>();
                    for (String feature : nextLine) {
                        try {
                            featureArray.add(Double.parseDouble(feature));
                        } catch (NumberFormatException ex) {
                            double[] features = new double[featureArray.size()];
                            int count = 0;
                            for (Double value : featureArray) {
                                features[count] = value;
                                count++;
                            }
                            INDArray iNDArray = Nd4j.create(features);
                            result.add(new BasicTextModel(feature, iNDArray));
                            break;
                        }
                    }
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CSVUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

//    public static String formRowForCSVFile(List<String> values) {
//        StringBuilder sb = new StringBuilder();
//        String delimiter = "";
//        for (String value : values) {
//            sb.append(delimiter).append(value);
//          delimiter  = CSV_SEPARATOR;
//        }
//        return sb.toString();
//    }
    public static File getDataCSVFile(File rootDir, IndexerEnum indexerEnum, DataType dataType) {
        return new File(Creater.getSaveModelFolder(rootDir), indexerEnum.getModelName() + "_" + dataType.getName() + "_" + CSV_NAME_FILE);
    }

}
