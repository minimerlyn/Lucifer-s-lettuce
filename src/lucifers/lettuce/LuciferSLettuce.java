/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucifers.lettuce;

import static Auxiliar.Auxiliar.*;
import Joint.HTC;
import java.util.ArrayList;

/**
 *
 * @author minimerlyn
 */
public class LuciferSLettuce {
    private static final int MAX_OPTIONS =6;
    private static ArrayList<HTC> htc;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        mainlc();
    }
    
    
    public static void mainlc(){
        History history = new History();
        int op=0;
        System.out.println("Bienvenido a Lucifer's lettuce");
        while (op!= MAX_OPTIONS) {
            System.out.println("------------------------------");
            System.out.println("Que desea hacer:");
            System.out.println("1.- Ver Historial de interaciones."
                    + "\n2.- Ver grafica por tiempo."
                    + "\n3.- Nueva interracion."
                    + "\n4.- Esperar 10 min."
                    + "\n5.- Re-calibracion."
                    + "\n6.- Salir");
            op=leerOP(MAX_OPTIONS);
            
            switch (op) {
                case 1:
                    history.histoyOp();
                    break;
                case 2:
                    history.QuantityGraphycByTime();
                    break;
                case 3: history.addInteraction();
                    break;
                case 4: pause(10);
                    history.contInteracion();
                    break;
                case 5: history.calibracion();
                    break;
                case 6:
                    System.out.println("Gracias por usar Lucifer's lettuce.\nHasta otra.");
                    break;
                default:
                    System.out.println(toRed("Valor de j no valido, avisar al administrador."));
            }
            history.toXML();
        }
        
    }
    
    private static void pause(long minLeft){
        while (minLeft>0 ) {
            try{
                System.out.println("Tiempo restante "+ minLeft+" min.");
                Thread.sleep(1000*60);
            }catch (InterruptedException x){
                System.out.println(toRed("Fallo al esperar el tiempo estimado."));
                minLeft=0;
            }
             minLeft--;
        }
    }
    
    
}
