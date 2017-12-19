/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucifer.s.lettuce;

import static Auxiliar.Auxiliar.*;
import java.util.Calendar;

/**
 *
 * @author minimerlyn
 */
public class LuciferSLettuce {
    private static final int MAX_OPTIONS =5;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        mainlc();     
    }
    
    public static void mainlc(){
        History history = new History();
        boolean cont=true;
        int op;
        System.out.println("Bienvenido a Lucifer's lettuce");
        while (cont) {
            System.out.println("------------------------------");
            System.out.println("Que desea hacer:");
            System.out.println("1.- Ver Historial de interaciones."
                    + "\n2.- Ver grafica por tiempo."
                    + "\n3.- Nueva interracion."
                    + "\n4.- Esperar 10 min."
                    + "\n5.- Salir");
            op=leerOP(MAX_OPTIONS);
            
            switch (op) {
                case 1:
                    history.histoyOp();
                    break;
                case 2:
                    history.graphycByTime();
                    break;
                case 3: history.addInteraction();
                    break;
                case 4: pause(10);
                    history.contInteracion();
                    break;
                case 5:
                    System.out.println("Gracias por usar Lucifer's lettuce.\nHasta otra.");
                    cont=false;
                    break;
            }
            
        }
        
    }
    
    private static void pause(long l){
        long minLeft=l;
        while (minLeft>0) {
            try{
                System.out.println("Tiempo restante "+ minLeft+" min.");
                Thread.sleep(1000*60*minLeft);
                minLeft--;
            }catch (InterruptedException x){
               toRed("Fallo al esperar el tiempo estimado.");
            }
        }
    }
    
    
}
