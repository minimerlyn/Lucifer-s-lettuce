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
    private static final String HTCs="HTCs";
    private static final String HTC="HTC";
    
    private float peso;
    private float pesoTotal;
    private Date date;
    private int tipoFiltro;//puede ser 1 o 2, si es 0 es valor desconocido, 1 carton, 2 carbono
    private ArrayList<HTC> htc;
    
    public Interaccion(float peso,float pesoTotal,int tipoFiltro, Date date) {
        this.peso=peso;
        this.pesoTotal=pesoTotal;
        this.tipoFiltro=tipoFiltro;
        this.htc = new ArrayList<>();
        this.date = date;
    }
    
    public Interaccion(Element elem){
        if (elem.getFirstChildElement(WEIGHT) == null) {
            peso=0;
            System.out.println(toRed("Error en el peso de la lechuga"));
        }else this.peso = Float.parseFloat(elem.getFirstChildElement(WEIGHT).getValue());
        
        if (elem.getFirstChildElement(TOTAL_WEIGHT) == null) {
            pesoTotal=peso;
            System.out.println(toRed("Error en el peso total"));
        }else this.pesoTotal= Float.parseFloat(elem.getFirstChildElement(TOTAL_WEIGHT).getValue());
        
        if (elem.getFirstChildElement(FILTER) == null) {
            tipoFiltro=0;
            System.out.println(toRed("Error en el tipo del filtro."));
        }else this.tipoFiltro = Integer.parseInt(elem.getFirstChildElement(FILTER).getValue());
        
        if (elem.getFirstChildElement(DATE) == null) {
            date= new Date(0,0,0,0);
            System.out.println(toRed("Error en el date."));
        }else date= new Date(elem.getFirstChildElement(DATE));
        
        Element eltoHTCs = elem.getFirstChildElement(HTCs);
        
        htc = new ArrayList<>();
        if (eltoHTCs == null) {
            System.out.println(toRed("htcs esta vacio o hay un error"));
        }else{
            Elements htcs = eltoHTCs.getChildElements(HTC);
            if (htcs.size()!=0) 
                for (int i = 0; i < htcs.size(); i++) 
                    htc.add(new HTC(htcs.get(i)));
        }
        
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
    
    public float [] getTiemposTotales(){
        float [] toret= new float[htc.size()];
        
        for (int i=0; i<htc.size(); i++) {
            toret[i]= htc.get(i).getTiempoTotal();
        }
        return toret;
    }
    
    public int [] getHoras(){
        int [] toret= new int[htc.size()];
        
        for (int i=0; i<htc.size(); i++) {
            toret[i]= htc.get(i).getHora();
        }
        return toret;
    }
    
    public int [] getMinutos(){
        int [] toret= new int[htc.size()];
        
        for (int i=0; i<htc.size(); i++) {
            toret[i]= htc.get(i).getMinuto();
        }
        return toret;
    }
    
    
    
    
    /**
     * 
     * @param n 0- menor tiempo, 1- mayor tiempo
     * @return el menor/maypr tiempo
     */
    private float getTiempo(int n){
        float toret;
        float provisional;
        if (n==0) {
            toret=Integer.MAX_VALUE;
        }else toret=0;
        for (int i = 0; i < htc.size(); i++) {
            provisional=htc.get(i).getTiempoTotal();
            
            switch (n) {
                case 0:
                    toret=toret>provisional?provisional:toret;
                    break;
                case 1:
                    toret=toret<provisional?provisional:toret;
                    break;
                default:
                    System.out.println(toRed("getTiempo(int n), parametro demasiado alto o incorrecto."));
            }
        }
        return toret;
    }
    
    public  float getMedioTiempo(){
        float toret=0;
        for (int i = 0; i < htc.size(); i++) {
            toret=htc.get(i).getTiempoTotal();
        }
        return toret/htc.size();
    }
    
    
    
    public Element toDom(){
        Element raiz= new Element(INTERACTION);
        Element eltoPeso = new Element(WEIGHT);
        Element eltoPesoTotal = new Element(TOTAL_WEIGHT);
        Element eltoTipoFiltro = new Element(FILTER);
        Element eltoHtc = new Element(HTCs);
        
        eltoPeso.appendChild(Float.toString(peso));
        eltoPesoTotal.appendChild(Float.toString(pesoTotal));
        eltoTipoFiltro.appendChild(Integer.toString(tipoFiltro));
        
        raiz.appendChild(eltoPeso);
        raiz.appendChild(eltoPesoTotal);
        raiz.appendChild(eltoTipoFiltro);
        raiz.appendChild(date.toDom());
        for (int i = 0; i < htc.size(); i++) {
            eltoHtc.appendChild(htc.get(i).toDom());
        }
        raiz.appendChild(eltoHtc);
        
        return raiz;
    }
    
    public String toStringResumen(){
        StringBuilder toret = new StringBuilder();
        toret.append(getDay()).append("\t").
                            append("Peso Maria/Peso total: ").append(getPeso()).append("/").append(getPesoTotal()).
                            append("\tPorcentaje:").append((peso * 100)/pesoTotal).append("%");
        return toret.toString();
    }
    
    @Override
    public String toString(){
        StringBuilder toret = new StringBuilder();
        toret.append(getDay());
        toret.append("\n\tPeso Maria/Peso total: ").append(getPeso()).append("/").append(getPesoTotal());
        toret.append("\n\tPorcentaje:").append((peso * 100)/pesoTotal).append("%");
        toret.append("\nTipo de filtro: ");
        switch (tipoFiltro) {
            case 1:
                toret.append(" carton.");
                break;
            case 2:
                toret.append(" filtro de carbono.");
                break;
            default:
                toret.append(" desconocido.");
                break;
        }
        toret.append("\n");
        return toret.toString();
    } 
}
