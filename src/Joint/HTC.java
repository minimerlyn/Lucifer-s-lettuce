/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Joint;

import static Auxiliar.Auxiliar.*;
import nu.xom.Element;

/**
 *
 * @author reyga
 */
public class HTC {
    
    private static final String HTC = "HTC";
    private static final String HOURE = "HOURE";
    private static final String MINUTE = "MINUTE";
    private static final String LEVEL = "LEVEL";
    private static final String TIMES = "TIMES";
    private static final String MISTAKES = "MISTAKES";
    
    private int hora;
    private int minuto;
    private int level;
    private float[] tiempos;
    private int [] errores;

    public HTC(int hora, int minuto, float [] ts, int[] er) {
        this.hora = hora;
        this.minuto = minuto;
        tiempos=ts;
        errores= er;
    }
    
    public HTC(Element e){
        Element eltoHora = e.getFirstChildElement( HOURE );
        Element eltoMinuto = e.getFirstChildElement( MINUTE );
        Element eltoLevel = e.getFirstChildElement( LEVEL );
        Element eltoTimes = e.getFirstChildElement( TIMES );
        
        
        if ( eltoHora == null ) {
            hora=0;
            System.out.println(toRed("error con la hora."));
        }else hora=Integer.parseInt(eltoHora.getValue());
        
        if ( eltoMinuto == null ) {
            minuto=0;
            System.out.println(toRed( "Falta minuto" ));
        }else minuto=Integer.parseInt(eltoMinuto.getValue());
        
        if ( eltoLevel == null ) {
            level=0;
            System.out.println(toRed( "Error con nivel." ));
        }else level=Integer.parseInt(toRed(eltoLevel.getValue()));
        
        if (eltoTimes == null ) {
            tiempos= new float[3];
            System.out.println("Error con los tiempos del HTC con hora: "+hora+":"+minuto);
        }else {
            
        }
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    
    public void setTiempos(float[] t){
        tiempos=t;
    }
    
    public float[] getTiempos(){
        return tiempos;
    } 
    
    public float getTiempo(int i){
        if (i<tiempos.length) {
            return tiempos[i];
        }
        System.out.println(toRed("ERROR EN getTiempo(int i), PARAM MAYOR QUE tiempos.LENGTH"));
        return -1;
    }
    public void setErrores(int[] e){
        errores=e;
    }
    public int[] getErrores(){
        return errores;
    }
    
    public int getError(int i){
        if (i<errores.length) {
            return errores[i];
        }
        System.out.println(toRed("ERROR EN getErrores(int i), PARAM MAYOR QUE ERRORES.LENGTH"));
        return -1;
    }
    
    public Element toDom(){
        Element raiz= new Element(HTC);
        
        Element eltoHoure = new Element(HOURE);
        Element eltoMinute = new Element(MINUTE);
        Element eltoLevel = new Element(LEVEL);
        
        eltoHoure.appendChild(Integer.toString(hora));
        eltoMinute.appendChild(Integer.toString(minuto));
        eltoLevel.appendChild(Integer.toString(level));
        
        raiz.appendChild(eltoHoure);
        raiz.appendChild(eltoMinute);
        raiz.appendChild(eltoLevel);
        
        return raiz;
    }
    
    public String toStringHora(){
        StringBuilder toret= new StringBuilder();
        
        toret.append(hora).append(":").append(minuto);
        return toret.toString();
    }
    
    public String toStringLevel(){
        StringBuilder toret= new StringBuilder();
        toret.append(level);
        return toret.toString();
    }
}
