/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.creater;

/**
 *
 * @author Sergey
 */
public class SaveModelException extends RuntimeException {

    public SaveModelException() {
    }

    public SaveModelException(String string) {
        super(string);
    }

    public SaveModelException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

}
