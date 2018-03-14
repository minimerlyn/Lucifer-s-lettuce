/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Joint;

import static Auxiliar.Auxiliar.toRed;
import java.util.Calendar;
import nu.xom.Element;

/**
 *
 * @author reyga
 */
public class Date {
    private static final String DATE="DATE";
    private static final String YEAR="YEAR";
    private static final String MONTH="MONTH";
    private static final String DAY="DAY";
    private static final String HOURE="HOURE";
    private static final String MINUTE="MINUTE";
    
    private int year;
    private int month;
    private int day;
    private int houre;
    private int min;
    
    public Date(int y, int m, int d, int h, int min){
        
        year=y;
        month = m;
        day= d;
        houre =h;
        this.min = min;
    }
    
    public Date(Element elem){
        if(elem.getFirstChildElement(YEAR) == null){
            System.out.println(toRed("Hay un error con el año."));
            year= Calendar.getInstance().get(Calendar.YEAR);
        }else year = Integer.parseInt(elem.getFirstChildElement(YEAR).getValue());
        
        if (elem.getFirstChildElement(MONTH) == null) {
            System.out.println(toRed("Hay un error con el mes."));
            month=0;
        }else month = Integer.parseInt(elem.getFirstChildElement(MONTH).getValue());
        
        if (elem.getFirstChildElement(DAY) == null) {
            System.out.println(toRed("Hay un error con el dia."));
            day=0;
        }else day = Integer.parseInt(elem.getFirstChildElement(DAY).getValue());
        
        
        if (elem.getFirstChildElement(HOURE) == null) {
            System.out.println(toRed("Hay un error con la hora."));
            houre=0;
        }else houre = Integer.parseInt(elem.getFirstChildElement(HOURE).getValue());
        
        if (elem.getFirstChildElement(MINUTE) == null) {
            System.out.println(toRed("Hay un error con el minuto."));
            min=0;
        }else min = Integer.parseInt(elem.getFirstChildElement(MINUTE).getValue());
        
    }
    
    

    public int getYear(){
        return year;
    }
    
    public void setYear(int y){
        year=y;
    }
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
    
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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
        if(d.year == year){
            if (d.month == month) {
                if (d.day==day) {
                    if (d.houre==houre) {
                        return min>d.min;
                    }else return houre>d.houre;
                }else return day>d.day;
            }else return month>d.month;
        }else return year>d.year;
    }
    
    @Override
    public String toString(){
        StringBuilder toret = new StringBuilder();
        toret.append("Dia.- ").append(day).append("/").append(month)
                .append("/").append(year);
        toret.append("\tHora.- ").append(houre).append(":").append(min);
        toret.append("\n");
        return toret.toString();
    }
    
    public Element toDom(){
        Element raiz = new Element(DATE);
        
        Element eltoYear = new Element(YEAR);
        Element eltoMonth = new Element(MONTH);
        Element eltoDay = new Element(DAY);
        Element eltoHoure = new Element(HOURE);
        Element eltoMinute = new Element(MINUTE);
        
        eltoYear.appendChild(Integer.toString(year));
        eltoMonth.appendChild(Integer.toString(month));
        eltoDay.appendChild(Integer.toString(day));
        eltoHoure.appendChild(Integer.toString(houre));
        eltoMinute.appendChild(Integer.toString(min));
        
        raiz.appendChild(eltoYear);
        raiz.appendChild(eltoMonth);
        raiz.appendChild(eltoDay);
        raiz.appendChild(eltoHoure);
        raiz.appendChild(eltoMinute);
        
        return raiz;
    }
}
