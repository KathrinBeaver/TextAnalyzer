/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import java.io.*;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

/**
 *
 * @author Sergey
 */
public class TestDoc {

    public static void main(String[] args) {
        File file = null;
        WordExtractor extractor = null;
        try {

            file = new File("C:\\Users\\Sergey\\Desktop\\макеева.doc");
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            HWPFDocument document = new HWPFDocument(fis);
            extractor = new WordExtractor(document);
            String[] fileData = extractor.getParagraphText();
            for (int i = 0; i < fileData.length; i++) {
                if (fileData[i] != null) {
                    System.out.println(fileData[i]);
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
        }
    }
}
