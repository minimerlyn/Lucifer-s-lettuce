/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Joint;

import static Auxiliar.Auxiliar.*;
import java.util.ArrayList;
import nu.xom.Element;
import nu.xom.Elements;

/**
 *
 * @author reyga
 */
public class Interaccion extends Object{
    
    private static final String INTERACTION="INTERACTION";
    private static final String WEIGHT="WEIGHT";
    private static final String TOTAL_WEIGHT="TOTAL_WEIGHT";
    private static final String FILTER="FILTER";
    private static final String DATE="DATE";
    private static final String HTC="HTC";
    
    private float peso;
    private float pesoTotal;
    private Date date;
    private int tipoFiltro;//puede ser 1 o 2, si es 0 es valor desconocido
    private ArrayList<HTC> htc;
    
    public Interaccion(float peso,float pesoTotal,int tipoFiltro, Date date) {
        this.peso=peso;
        this.pesoTotal=pesoTotal;
        this.tipoFiltro=tipoFiltro;
        this.htc = new ArrayList<>();
        this.date = date;
    }
    
    public Interaccion(float peso,float pesoTotal,int tipoFiltro, Date date, HTC htc){
        this.peso=peso;
        this.pesoTotal=pesoTotal;
        this.tipoFiltro=tipoFiltro;
        this.htc = new ArrayList<>();
        this.date = date;
        this.htc.add(htc);
    }
    
    public Interaccion(Element elem){
        if (elem.getFirstChildElement(WEIGHT) == null) {
            peso=0;
            System.out.println(toRed("Error en el peso de la lechuga"));
        }else this.peso= Integer.parseInt(elem.getFirstChildElement(WEIGHT).getValue());
        
        if (elem.getFirstChildElement(TOTAL_WEIGHT) == null) {
            pesoTotal=peso;
            System.out.println(toRed("Error en el peso total"));
        }else this.pesoTotal= Integer.parseInt(elem.getFirstChildElement(TOTAL_WEIGHT).getValue());
        
        if (elem.getFirstChildElement(FILTER) == null) {
            tipoFiltro=0;
            System.out.println(toRed("Error en el tipo del filtro."));
        }else this.tipoFiltro = Integer.parseInt(elem.getFirstChildElement(FILTER).getValue());
        
        htc = new ArrayList<>();
        if (elem.getFirstChildElement(HTC) == null) {
            System.out.println(toRed("htc esta vacio o hay un error"));
        }else{
            Elements htcs = elem.getChildElements(HTC);
            for (int i = 0; i < htcs.size(); i++) 
                htc.add(new HTC(htcs.get(i)));
            
        }
        
        if (elem.getFirstChildElement(DATE) == null) {
            date= new Date(0,0,0,0);
            System.out.println(toRed("Error en el date."));
        }else date= new Date(elem.getFirstChildElement(DATE));
    }
    
    public float getPeso(){
        return peso;
    }
    
    public float getPesoTotal(){
        return pesoTotal;
    }

    public ArrayList<HTC> getHtc() {
        return htc;
    }

    public void setHtc(ArrayList<HTC> htc) {
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
    
    public void addHtcInteracion(HTC i){
        htc.add(i);
    }
    public void addHtcInteracion(int hora, int min, int level){
        htc.add(new HTC(hora,min,level));
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
        toret.append("sin acabar");
        //toret.append("\n).append(graphycByTime());
        return toret.toString();
        
    } 
    
    public Element toDom(){
        Element raiz= new Element(INTERACTION);
        Element eltoPeso = new Element(WEIGHT);
        Element eltoPesoTotal = new Element(TOTAL_WEIGHT);
        Element eltoTipoFiltro = new Element(FILTER);
        Element eltoDate = new Element(DATE);
        Element eltoHtc = new Element(HTC);
        
        eltoPeso.appendChild(Float.toString(peso));
        eltoPesoTotal.appendChild(Float.toString(pesoTotal));
        eltoTipoFiltro.appendChild(Integer.toString(tipoFiltro));
        
        raiz.appendChild(eltoPeso);
        raiz.appendChild(eltoPeso);
        raiz.appendChild(eltoTipoFiltro);
        raiz.appendChild(date.toDom());
        for (int i = 0; i < htc.size(); i++) {
            raiz.appendChild(htc.get(i).toDom());
        }
        
        return raiz;
    }
}
