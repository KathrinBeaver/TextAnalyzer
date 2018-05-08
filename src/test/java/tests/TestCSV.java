/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.datavec.api.records.reader.impl.csv.CSVSequenceRecordReader;
import org.datavec.api.writable.Writable;
import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author Sergey
 */
public class TestCSV {

    public static void main(String[] args) throws Exception {
        String csv = "C:\\Users\\Sergey\\Desktop\\data.csv";

        CSVWriter writer = new CSVWriter(new FileWriter(csv));
        //Create record
        List<String> row1 = new ArrayList<>();
        for (String str : "4,David,Miller,Australia,30".split(",")) {
            row1.add(str);
        }
        List<String> row2 = new ArrayList<>();
        for (String str : "4,David,Miller,Australia,31".split(",")) {
            row2.add(str);
        }
        List<String> row3 = new ArrayList<>();
        for (String str : "4,David,Miller,Australia,32".split(",")) {
            row3.add(str);
        }
        //Write the record to file
        writer.writeNext(row1.toArray(new String[row1.size()]));
        writer.writeNext(row2.toArray(new String[row2.size()]));
        writer.writeNext(row3.toArray(new String[row3.size()]));
        //close the writer
        writer.close();

        //Build reader instance
        CSVReader reader = new CSVReader(new FileReader(csv), ',', '"', 0);
        //Read all rows at once
        List<String[]> allRows = reader.readAll();
        //Read CSV line by line and use the string array as you want
        for (String[] row : allRows) {
            System.out.println(Arrays.toString(row));
        }
    }

//    public static void main(String[] args) throws IOException, Exception {
//        Instances data2 = DataSource.read("/some/where/dataset.csv");
////        List<Writable> collection = new ArrayList<>();
////        collection.add(new ArrayWritableText("12"));
////        collection.add(new Text("13"));
////        collection.add(new Text("14"));
//
//        CSVSaver saver = new CSVSaver();
//        saver.setInstances(data);
//        saver.setFile(new File("/some/where/data.csv"));
//        saver.writeBatch();
//    }
}
