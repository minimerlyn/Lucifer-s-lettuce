/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Joint;

/**
 *
 * @author reyga
 */
public class Date {
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
    
    public boolean masReciente(Date d){
        if () {
            
        }
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
}
