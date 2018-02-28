/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Joint;

import static Auxiliar.Auxiliar.*;
import nu.xom.*;

/**
 *
 * @author reyga
 */
public class HTC {
    
    private static final String HTC = "HTC";
    private static final String HOURE = "HOURE";
    private static final String MINUTE = "MINUTE";
    private static final String TIMES = "TIMES";
    private static final String TIME = "TIME";
    private static final String MISTAKES = "MISTAKES";
    private static final String MISTAKE = "MISTAKE";
    
    private int hora;
    private int minuto;
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
        Element eltoErrores = e.getFirstChildElement( MISTAKES );
        Element eltoTimes = e.getFirstChildElement( TIMES );
        
        if ( eltoHora == null ) {
            hora=0;
            System.out.println(toRed("error con la hora en HTC."));
        }else hora=Integer.parseInt(eltoHora.getValue());
        
        if ( eltoMinuto == null ) {
            minuto=0;
            System.out.println(toRed( "Falta minuto en HTC" ));
        }else minuto=Integer.parseInt(eltoMinuto.getValue());
        
        errores=new int[INTERACTION_TIMES];
        if ( eltoErrores == null ) {
            for (int i = 0; i < INTERACTION_TIMES; i++) 
                errores[i]=0;
            
            System.out.println(toRed( "Error con los errores del HTC con hora: "+hora+":"+minuto ));
        }else {
            Elements eltoSubErrores = eltoErrores.getChildElements(MISTAKE);
            if (eltoSubErrores.size()!=0) 
                for (int i = 0; i < eltoSubErrores.size(); i++) 
                    errores[i]=Integer.parseInt(eltoSubErrores.get(i).getValue());
        }
        
        tiempos= new float[INTERACTION_TIMES];
        if (eltoTimes == null ) {
            for (int i = 0; i < INTERACTION_TIMES; i++) {
                tiempos[i]=0;
            }
            System.out.println("Error con los tiempos del HTC con hora: "+hora+":"+minuto);
        }else {
            Elements eltoSubTiempos = eltoTimes.getChildElements(TIME);
            if (eltoSubTiempos.size()!=0) 
                for (int i = 0; i < eltoSubTiempos.size(); i++) 
                    tiempos[i]=Float.parseFloat(eltoSubTiempos.get(i).getValue());
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
    
    public float getTiempoTotal(){
        float torret=0;
        float auxiliar;
        int penalizacion;
        for (int j = 0; j < INTERACTION_TIMES; j++) {
                auxiliar=tiempos[j];
                if (errores[j]>0) {
                    switch (j) {
                        case 0:
                        case 1:
                            penalizacion=PENALTY_FOR_MISTAKES_ARITHMETIC+ ((errores[j]-1)*ENCREASE_MULTIPLE_MISTAKES_ARITHMETIC);
                            break;
                        case 2:
                            penalizacion=PENALTY_FOR_MISTAKES_STRING+ ((errores[j]-1)*ENCREASE_MULTIPLE_MISTAKES_STRING);
                            break;
                        default: System.out.println(toRed("getTiempo, 2 for, j is overextending."));
                            penalizacion=0;
                    }
                }else penalizacion=0;
                torret+=auxiliar + ((auxiliar*penalizacion)/100);
            }
         return torret;
    }
    
    public Element toDom(){
        Element raiz= new Element(HTC);
        
        Element eltoHoure = new Element(HOURE);
        Element eltoMinute = new Element(MINUTE);
        Element eltoTiempos = new Element(TIMES);
        Element eltoErrores = new Element(MISTAKES);
        
        eltoHoure.appendChild(Integer.toString(hora));
        eltoMinute.appendChild(Integer.toString(minuto));
        
        Element eltoAux;
        for (int i = 0; i < tiempos.length; i++) {
            eltoAux = new Element(TIME);
            eltoAux.appendChild(Float.toString(tiempos[i]));
            eltoTiempos.appendChild(eltoAux);
        }
        
        for (int i = 0; i < errores.length; i++) {
            eltoAux = new Element(MISTAKE);
            eltoAux.appendChild(Integer.toString(errores[i]));
            eltoErrores.appendChild(eltoAux);
        }
        
        raiz.appendChild(eltoHoure);
        raiz.appendChild(eltoMinute);
        raiz.appendChild(eltoTiempos);
        raiz.appendChild(eltoErrores);
        
        return raiz;
    }
    
}
