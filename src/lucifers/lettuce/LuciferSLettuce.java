/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucifers.lettuce;

import static Auxiliar.Auxiliar.*;
import Joint.Date;
import Joint.HTC;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author minimerlyn
 */
public class LuciferSLettuce {//900008
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
        System.out.println(toBlue("Bienvenido a Lucifer's lettuce"));
        while (op!= MAX_OPTIONS) {
            System.out.println("------------------------------");
            System.out.println("Que desea hacer:");
            System.out.println("1.- Ver Historial de interaciones."
                    + "\n2.- Ver grafica por tiempo."
                    + "\n3.- Nueva interracion."
                    + "\n4.- Continuar interraccion."
                    + "\n5.- Re-calibracion."
                    + "\n6.- Salir");
            op=leerOP(MAX_OPTIONS);
            
            switch (op) {
                case 1:
                    history.histoyOp();
                    break;
                case 2:
                    history.HighnessGraphycByTime();
                    break;
                case 3: history.addInteraction();
                    break;
                case 4: continuarIter(history);
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
    
    private static  void continuarIter(History history) {
        Date last=history.getLastInter().getDate();
        Calendar c= Calendar.getInstance();
        c.set(last.getYear(), last.getMonth(), last.getDay(), last.getHoure(), last.getMin(), 0);
        /*c.set(last.getYear(), Calendar.getInstance().get(Calendar.MONTH)
                , Calendar.getInstance().get(Calendar.DATE),Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                ,Calendar.getInstance().get(Calendar.MINUTE)-7, Calendar.getInstance().get(Calendar.SECOND));
        */
        int difT=history.getDifTiempo(c);
        //int aux=difT;
        if (difT > 7200000) {//2 horas, ya no se puede continuar
            System.out.println(toRed("No se puede continuar porque han pasado mas de 2 horas desde el inicio de la interaccion"));
        }else{
            c.set(last.getYear(), last.getMonth(), last.getDay(), history.getLastInter().getLastHTC().getHora(),
                    history.getLastInter().getLastHTC().getMinuto(), 0);
            //difT=history.getDifTiempo(c);
            //difT=aux;
            boolean detener=false;
            if (difT < 600000){  // 10 minutos
                detener=pause((int) ((600000-difT)*0.0000166667));
            }
            if (!detener)
            history.contInteracion();
        }
    }
    
    /**
     * 
     * @param minLeft tiempo de espera en min
     * @return true (1) si se detuvo a voluntad
     */
    private static boolean pause(int minLeft){
        int [] ratilla = new int[]{0,0};
        System.out.println("Escribe "+ toBlue("detener")+ " para volver al menu principal.");
        final int aux= (int) minLeft;
        Thread espera;
        espera = new Thread(new Runnable(){
            @Override
            public void run() {
                int tRest=aux*1000;
                int ant=tRest;
                System.out.println("Tiempo restante "+ toBlue(Long.toString(tRest/1000)) + " minutos.");
                while (tRest>0) {
                    if (tRest == (ant-1000)){ System.out.println("Tiempo restante "+ toBlue(Long.toString(tRest/1000))
                            + " minutos.");// si pasa un min avisar
                            ant=ant-1000;
                    }
                    try{
                        Thread.sleep(60);
                        tRest--;
                    }catch(InterruptedException ex){
                        System.out.println("Volviendo al menu");
                        tRest=0;                        
                    }
                }
                ratilla[0]=1;
            }
        });
        
        Thread cadena = new Thread( new Runnable(){
            @Override
            public void run(){
                boolean continuar=true;
                String cad="a";
                while (continuar) {
                    if (ratilla[0]==1) {
                        continuar=false;
                        
                    }else cad= leerCad();
                    if (cad.equalsIgnoreCase("detener")) {
                        continuar=false;
                        ratilla[1]=1;
                        espera.interrupt();
                    }
                }
            }
        });
        
        espera.start();
        cadena.start();
        while (ratilla[0]==0){
            try{
                Thread.sleep(1);
            }catch (InterruptedException exc){
                System.out.println(toRed("hilo prinicipal interrumpido"));
            }
        }
        System.out.println(toBlue("Introduce cualquier cosa y dale al enter."));
        //el proceso cadena tiene el teclado y hay que presionar enter para liberarlo
        return ratilla[1]==1;
        
    }
}
    
    
