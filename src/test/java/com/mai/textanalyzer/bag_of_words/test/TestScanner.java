/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.bag_of_words.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

/**
 *
 * @author Sergey
 */
public class TestScanner {

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
//        System.setProperty("console.encoding", "Cp866");
//        System.setIn(new InputStream(System.out, true, "Cp866"));
//        System.setOut(new java.io.PrintStream(System.out, true, "Cp866"));

//        OutputStreamWriter bw = new OutputStreamWriter(new FileOutputStream("D:\\test.txt"));
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        while (true) {
//            String s = br.readLine();
//            if (s.isEmpty()) {
//                bw.flush();
//                bw.close();
//                return;
//            }
//            bw.write(s);
//            System.out.println("write:" + s);
//        }
        Scanner scaner = new Scanner(System.in,"UTF-8");
        System.out.println("start");
        while (true) {
            System.out.println(scaner.nextLine());
        }

//        System.out.println(Arrays.toString(str.getBytes("Cp866")));
//        System.out.println(Arrays.toString(str.getBytes("Cp1251")));
//        System.out.println(Arrays.toString(str.getBytes("utf-8")));
//            if (nextWord.isEmpty()) {
//                break;
//            }
//            System.out.println("next word " + nextWord);
    }
}
