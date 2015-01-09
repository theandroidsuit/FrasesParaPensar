package com.theandroidsuit.frasesparapensar.bean;

/**
 * Created by Virginia Hernández on 6/01/15.
 */
public class Pensamiento {
    private String frase;
    private String autor;
    private String extra;


    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public boolean hasExtra(){
        if (null == extra || extra.trim().length() == 0){
            return false;
        }

        return true;
    }
}
