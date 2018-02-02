/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.web.vaadin.system;

import java.io.Serializable;

/**
 *
 * @author A.Kumanyaev
 */
public class Version implements Serializable {
    
    private static final long serialVersionUID = 13242414571L;
    private String version = null;
    private String date = null;
    private String copyright = null;

    public String getVersion() {
        if (version == null)
            return "";
        return version;
    }

    public void setVersion(String version) {
        if (this.version == null) {
            this.version = version;
        }
    }

    public String getDate() {
        if (date == null)
            return "";
        return date;
    }

    public void setDate(String date) {
        if (this.date == null) {
            this.date = date;
        }
    }

    public String getCopyright() {
        if (copyright == null)
            return "";        
        return copyright;
    }

    public void setCopyright(String copyright) {
        if (this.copyright == null) {
            this.copyright = copyright;
        }
    }

    @Override
    public String toString() {
        return "Версия " + getVersion() + " от " + getDate();
    }
}
