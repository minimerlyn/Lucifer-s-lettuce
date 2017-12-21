/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucifer.s.lettuce;

import Joint.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import static Auxiliar.Auxiliar.*;
import java.util.Calendar;

/**
 *
 * @author reyga
 */
public class History {
    private static final String FILE_NAME="History.xml";
    private ArrayList<Interaccion> inter ;
    //0- suma, 1- mult, 2 cadenas
    private int [] tiempoRespuesta = new int[4];//4 juegos distintos en milisegundos
    
    public History() {
        
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = (Document) db.parse(new File(FILE_NAME));
            //falta acabar
        } catch (FileNotFoundException exc){
            System.out.println("------------------------------------------");
            System.out.println("No se encutran archivos anteriores."
                    + "\nAsi que bienvenido en su primera vez");
            System.out.println("------------------------------------------");
            inter= new ArrayList<>(10);
            System.out.println(toBlue("Pasemos a calibrar los tiempos de respuesta: "));
            calibracion();
        } catch (IOException ex) {
            System.out.println(toRed("Error IOException."));
        } catch (ParserConfigurationException ex) {
            System.out.println(toRed("Error ParserConfigurationException"));
        } catch (SAXException ex) {
            System.out.println(toRed("Error ParserConfigurationException"));
        }
        
    }

    public void addInteraction() {
        clear();
        Interaccion nueva = newInteraccion();
        int i=0;
        while (i<inter.size() && !nueva.getDate().masReciente(inter.get(i).getDate())) {
            i++;
        }
        inter.add(i, nueva);
    }
    
    private Interaccion newInteraccion(){
        float peso;
        float pesoTotal;
        do{
            peso=leerNumFloat("Introduce el "+ toGreen("peso de la lechuga")+" en gramos: ");
            System.out.println("-------------------------");
            pesoTotal=leerNumFloat("Introduce el "+toGreen("peso total")+": ");
            System.out.println("-------------------------");
            if (pesoTotal/peso<1) {
                System.out.println(toRed("El tamaño total no puede ser inferior al de la lechuga solo."
                        + "\nVuelve a introducirlos."));
            }
        }while(pesoTotal/peso<1);
        int tipoFiltro=0;
        do{
            System.out.println("Que tipo de "+toGreen("filtro")+" es?"
                    + "\n1.-Carton."
                    + "\n2.-Filtro de carbono.");
            tipoFiltro= leerOP(2);
        }while(tipoFiltro>2 || tipoFiltro<=0);
        System.out.println("-------------------------");
        boolean horaValida=false;
        String momento;
        int hora,min;
        do {
            momento = leerCad("A que "+toGreen("hora")+" te lo has fumado? \nIntroduce \":\" si es ahora. (Formato de 24h)(x:y)\n->");
            hora=getMomento(momento,0);
            min=getMomento(momento,1);
        } while (hora==-1 || min == -1);
       
        int dia, mes;
        do{
            if (momento.equals(":")) {
                dia=getMomento(momento,2);
                mes=getMomento(momento,3);
            }else{
                System.out.println("-------------------------");
                momento = leerCad("Que "+toGreen("dia")+" te lo has fumado? \nIntroduce \":\" si es hoy. (x/y)\n->");
                dia=getMomento(momento,2);
                mes=getMomento(momento,3);
            }
        }while(dia==-1 || mes == -1);
                
        return new Interaccion(peso,pesoTotal,tipoFiltro,new Date(dia,mes,hora,min));
    }
    
    /**
     * 
     * @param st cadena con los valores
     * @param j j=0 hora o j=1 min j=2 dia o j=3 mes
     * @return el valor de la cadena dependiendo de j
     */
    private int getMomento(String st,int j){
        if (st.equals(":")) {//hora del sistema
            Calendar cal = Calendar.getInstance();
            switch (j) {
                case 0:
                    return cal.get(Calendar.HOUR_OF_DAY);
                case 1:
                    return cal.get(Calendar.MINUTE);
                case 2:
                    return cal.get(Calendar.DATE);
                case 3:
                    return cal.get(Calendar.MONTH)+1;
                default:
                    System.out.println(toRed("Valor de j no valido, avisar al administrador."));
                    return -1;
            }
            
        }else{//hora del usuario
            StringBuilder toret= new StringBuilder();
            int i=0;
            if (j==0 || j==2) {//j=0 o j=2 coge el valor antes de :
                while (i<st.length() && charToInt(st.charAt(i)) != -1) {
                    toret.append(st.charAt(i++));
                }
                if (j==0) {
                    j=24;//minimo valor del maximo no posible
                }else j=32; // se simplifican todos los meses en 31 dias, el usuario tiene que saber el maximo
                
            }else{//j=1 0 3, coge el valor de despues de :
                boolean found=false;
                while (i<st.length()) {//cambio el uso de j para saber si llego a los 2 puntos
                    if (found) {
                        toret.append(st.charAt(i));
                    }else{
                        if (charToInt(st.charAt(i)) == -1) {
                            found=true;
                        }
                    }
                    i++;
                }
                if (j==1) {
                    j=60;//minimo valor del maximo no posible
                }else j=13;
            }
            int numberToret = parseInt(toret.toString());
                if ( numberToret<0 || numberToret>=j) {// si esta fuera de rango
                    numberToret= -1;
                    System.out.println(toRed("Hora no posible."));
                }
            return numberToret;
        }
    }
    
    private void calibracion(){
        Calendar cal;
        int i,j,k,time;
        boolean correcto;
        System.out.println("Presiona enter cuando quieras empezar la prueba de "+toBlue("sumar")+":");
        waitTillEnter();
        do{
            k=0;
            time=0;
            cal=Calendar.getInstance();
            correcto=true;
            while (k<5 && correcto) {
                i = (int) Math.floor(Math.random()*9);
                j = (int) Math.floor(Math.random()*9);
                correcto= resultCorrect(i,j,i+j);
                time+=getDifTiempo(cal);
                k++;
                if (!correcto) {
                    System.out.println("Es necesaro que respondas "+toRed("correctamente")+" a todas las  preguntas: ");
                    System.out.println("Tiempo reiniciado");
                }
            }
        }while(!correcto);
        System.out.println("tiempo total: "+time/1000+" seg. Tiempo medio: "+time/3/1000+"seg");
        tiempoRespuesta[0]=time/5;
        
        System.out.println("Presiona enter cuando quieras empezar la prueba de "+toBlue("multiplicar")+":");
        waitTillEnter();
        do{
            k=0;
            time=0;
            cal=Calendar.getInstance();
            correcto=true;
            while (k<3 && correcto) {
                i = (int) Math.floor(Math.random()*9);
                j = (int) Math.floor(Math.random()*9);
                correcto= resultCorrect(i,j,i*j);
                time+=getDifTiempo(cal);
                k++;
                if (!correcto) {
                    System.out.println("Es necesaro que respondas "+toRed("correctamente")+" a todas las  preguntas: ");
                    System.out.println("Tiempo reiniciado");
                }
            }
        }while(!correcto);
        System.out.println("tiempo total: "+time/1000+" seg. Tiempo medio: "+time/3/1000+"seg");
        tiempoRespuesta[1]=time/5;
        
        System.out.println("Presiona enter cuando quieras empezar la prueba de "+toBlue("escritura")+":");
        waitTillEnter();
        do{
            k=0;
            time=0;
            cal=Calendar.getInstance();
            correcto=true;
            while (k<2 && correcto) {
                System.out.println("Escribe la siguiente cadena-");
                i=(int) Math.floor(Math.random()*posiblesCadenas.length);
                System.out.println(posiblesCadenas[i]);
                correcto= posiblesCadenas[i].equals(leerCad("->"));
                time+=getDifTiempo(cal);
                k++;
                if (!correcto) {
                    System.out.println("Es necesaro que respondas "+toRed("correctamente")+" a todas las  preguntas: ");
                    System.out.println("Tiempo reiniciado");
                }
            }
        }while(!correcto);
        System.out.println("tiempo total: "+time/1000+" seg. Tiempo medio: "+time/3/1000+"seg");
        tiempoRespuesta[2]=time/5;
    }
    
    private int getDifTiempo(Calendar c1){
        Calendar c2=Calendar.getInstance();
        int res=(c2.get(Calendar.HOUR_OF_DAY)-c1.get(Calendar.HOUR_OF_DAY))*120*1000;
        res+=(c2.get(Calendar.MINUTE)-c1.get(Calendar.MINUTE))*60*1000;
        res+=(c2.get(Calendar.SECOND)-c1.get(Calendar.SECOND))*1000;
        res+=(c2.get(Calendar.MILLISECOND)-c1.get(Calendar.MILLISECOND));
        return res;
    }
    
    private boolean resultCorrect(int i, int j,int res){
        return (leerNum(Integer.toString(i)+" * "+Integer.toString(j)+" = ")) == res;
    }
    
    private void pause(int i){
        do {
            try{
                Thread.sleep(1000);
            }catch(InterruptedException exc){
                System.out.println(toRed("Error en el metodo History.pause"));
            }
            i--;
        } while (i>0);
        
        
    }
    
    public void histoyOp() {
        clear();
        if (inter.size()>0) {
            System.out.println(this.toString());
            boolean cont=true;
            int op;
            do{
                op=leerNum("Introduce un numero para ver mas detalles o 0 para volver.- ");
                if (op>inter.size() || op<=0) {
                    System.out.println("Numero no valido.");
                }else if(op<inter.size() || op>0){
                    System.out.println(inter.get(op-1).toString());
                }
            }while(op!=0);
        }else   System.out.println("No hay ninguna interaccion.");
        //1-dia x has fumado tanta maria
        //2-dia y has fumado tanta maria
        
        //introduce un numero para ver mas detalles o 0 para volver
    }
    
    /**
     * 
     * @param d1 compra a mirar
     * @param d2 compra anterior
     * @return 
     */
    private boolean esNuevaCompra(Date d1, Date d2){
        if (d1.getMonth()==d2.getMonth()) {
            return (d2.getDay()-d1.getDay())>=3;
        }
        return true;
    }

    public void graphycByTime() {
        
        /*
        
        6|                  _
        5|           _/----/|      _                    _             _
        4|    _______|      |      |      _             |             |      
        3|    |      |      |      |      |      _      |             |      
        2|    |      |      |      |      |      |      |             |      _      _
        1|____|______|______|______|______|______|______|_____________|______|______|
            12:23  12:33  12:33  12:33  12:33  12:33  12:33  12:33  12:33  12:33  12:33
        
        */
        System.out.println("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void contInteracion() {
        //esperar confirmacion del usuario
        //si el tiempo desde el ultimo porro introducido es menor qu 20 min añade un nuevo dato a la ultima interraccin
        
        System.out.println("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString(){
        StringBuilder toret = new StringBuilder();
        System.out.println(toBlue("\nInteraccion mas reciente arriba"));
            float cant=0;
            for (int i = 0; i < inter.size(); i++) {
                toret.append("\n");
                    if ( i>0 && esNuevaCompra(inter.get(i).getDate(),inter.get(i-1).getDate()) ) {
                        toret.append("\nTotal fumado en esta compra: ").append(toGreen(Float.toString(cant)));
                        cant=0;
                        toret.append("\n------------------------------\n");
                        toret.append(toGreen("Nueva compra."));
                        toret.append("\n");
                    }
                cant+=inter.get(i).getPeso();
                toret.append(toBlue(Integer.toString(i+1))).append(toBlue(".- ")).append(inter.get(i).toStringResumen());
            }
            toret.append("\nTotal fumado en esta compra: ").append(toGreen(Float.toString(cant)));
            return toret.toString();
    }
}