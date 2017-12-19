/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Joint;

import java.util.ArrayList;

/**
 *
 * @author reyga
 */
public class Interaccion extends Object{
    private ArrayList<Integer> [] htc;
    private Date date;
    private float peso;
    private float pesoTotal;
    private int tipoFiltro;

    public Interaccion(float peso,float pesoTotal,int tipoFiltro, Date date) {
        this.peso=peso;
        this.pesoTotal=pesoTotal;
        this.tipoFiltro=tipoFiltro;
        htc = new ArrayList[6];
        this.date = date;
    }
    
    public float getPeso(){
        return peso;
    }
    
    public float getPesoTotal(){
        return pesoTotal;
    }

    public ArrayList<Integer>[] getHtc() {
        return htc;
    }

    public void setHtc(ArrayList<Integer>[] htc) {
        this.htc = htc;
    }

    public Date getDate() {
        return date;
    }
    
    public String getDay(){
        return date.toStringDay();
    }
    

    public void setDate(Date date) {
        this.date = date;
    }
    
    public String toStringResumen(){
        StringBuilder toret = new StringBuilder();
        toret.append(getDay()).append("\t").
                            append("Peso Maria/Peso total: ").append(getPeso()).append("/").append(getPesoTotal());
        return toret.toString();
    }
    @Override
    public String toString(){
        StringBuilder toret = new StringBuilder();
        toret.append("");
        //toret.append("\n).append(graphycByTime());
        return toret.toString();
        
    } 
}
