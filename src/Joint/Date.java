/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Joint;

import static Auxiliar.Auxiliar.toRed;
import nu.xom.Element;

/**
 *
 * @author reyga
 */
public class Date {
    private static final String DATE="DATE";
    private final static String DAY="DAY";
    private final static String MONTH="MONTH";
    private final static String HOURE="HOURE";
    private final static String MINUTE="MINUTE";
    
    private int day;
    private int month;
    private int houre;
    private int min;
    
    public Date(int d, int m, int h, int min){
        day= d;
        month = m;
        houre =h;
        this.min = min;
    }
    
    public Date(Element elem){
        if (elem.getFirstChildElement(DAY) == null) {
            System.out.println(toRed("Hay un error con el dia."));
        }else day = Integer.parseInt(elem.getFirstChildElement(DAY).getValue());
        
        if (elem.getFirstChildElement(MONTH) == null) {
            System.out.println(toRed("Hay un error con el mes."));
        }else month = Integer.parseInt(elem.getFirstChildElement(MONTH).getValue());
        
        if (elem.getFirstChildElement(HOURE) == null) {
            System.out.println(toRed("Hay un error con la hora."));
        }else houre = Integer.parseInt(elem.getFirstChildElement(HOURE).getValue());
        
        if (elem.getFirstChildElement(MINUTE) == null) {
            System.out.println(toRed("Hay un error con el dia."));
        }else day = Integer.parseInt(elem.getFirstChildElement(MINUTE).getValue());
    }
    
    

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getHoure() {
        return houre;
    }

    public void setHoure(int houre) {
        this.houre = houre;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
    
    /**
     * 
     * @param d fecha a comparar
     * @return devuelve true si la fecha enviada es posterior a la fecha que llama al metodo
     */
    public boolean masReciente(Date d){
        if (d.month == month) {
            if (d.day==day) {
                if (d.houre==houre) {
                    return min>d.min;
                }else return houre>d.houre;
            }else return day>d.day;
        }else return month>d.month;
    }
    
    public String toStringDay(){
        StringBuilder toret = new StringBuilder();
        toret.append("Dia-> ").append(day).append("/").append(month);
        return toret.toString();
    }
    
    @Override
    public String toString(){
        StringBuilder toret = new StringBuilder();
        toret.append("Dia.- ").append(day).append("/").append(month);
        toret.append("\nHora.- ").append(houre).append(":").append(min);
        return toret.toString();
        
    }
    
    public Element toDom(){
        Element raiz = new Element(DATE);
        
        Element eltoDay = new Element(DAY);
        Element eltoMonth = new Element(MONTH);
        Element eltoHoure = new Element(HOURE);
        Element eltoMinute = new Element(MINUTE);
        
        eltoDay.appendChild(Integer.toString(day));
        eltoMonth.appendChild(Integer.toString(month));
        eltoHoure.appendChild(Integer.toString(houre));
        eltoMinute.appendChild(Integer.toString(min));
        
        raiz.appendChild(eltoDay);
        raiz.appendChild(eltoMonth);
        raiz.appendChild(eltoHoure);
        raiz.appendChild(eltoMinute);
        
        return raiz;
    }
}
