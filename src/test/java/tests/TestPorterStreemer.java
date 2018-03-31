/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import com.mai.textanalyzer.word_processing.PorterStremmer;

/**
 *
 * @author Sergey
 */
public class TestPorterStreemer {

    public static void main(String[] args) {
        System.out.println(PorterStremmer.stem("колонизация"));
        System.out.println(PorterStremmer.stem("колонизацию"));
        System.out.println(PorterStremmer.stem("покупаешь"));
        System.out.println(PorterStremmer.stem("покупаем"));
        System.out.println(PorterStremmer.stem("покупают"));

    }
}
