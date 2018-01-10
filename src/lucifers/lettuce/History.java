/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucifers.lettuce;

import Joint.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import static Auxiliar.Auxiliar.*;
import java.io.FileOutputStream;
import java.util.Calendar;
import nu.xom.*;

/**
 *
 * @author reyga
 */
public class History {
    private static final String FILE_NAME="History.xml";
    private static final String RESPONSE_TIME="RESPONSE_TIME";  
    private static final String INTERACTIONS="INTERACTIONS";
    
    
    private ArrayList<Interaccion> inter ;
    //0- suma, 1- mult, 2 cadenas
    private int [] tiempoRespuesta = new int[4];//4 juegos distintos en milisegundos
    
    public History() {
        
        try {
            Builder parser = new Builder();
            Document doc = parser.build(new File( FILE_NAME ));
            Element data = doc.getRootElement();
            Element eltoTR=data.getFirstChildElement(RESPONSE_TIME);
            Element eltoInter = data.getFirstChildElement(INTERACTIONS);

            if (eltoTR == null) {
                System.out.println(toRed("No se encuentran los tiempos de respuesta, pasando a calibracion: "));
                calibracion();
            }else{
                
                Elements tiempos=eltoTR.getChildElements(RESPONSE_TIME);
                for (int i = 1; i < tiempos.size(); i++) {
                    tiempoRespuesta[i]=Integer.parseInt(tiempos.get(i).getValue());
                }
                System.out.println(toGreen("Tiempos de respuesta detectados y añadidos."));
            }
            
            if (eltoInter == null) {
                System.out.println(toRed("No se encuentran interracciones anteriores."));
            }else{
                Elements inters = eltoInter.getChildElements(INTERACTIONS);
                for (int i = 0; i < inters.size(); i++) {
                    inter.add(new Interaccion(inters.get(i)));
                }
                System.out.println(toGreen("Interacciones detectadas y añadidas."));
            }
            
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
        } catch (ParsingException ex) {
            System.out.println(toRed("Error ParsinsException"));
        } 
        
    }

    public void addInteraction() {
        clear();
        Interaccion nueva = newInteraccion();
        
        int i=0;
        System.out.println("nueva inter "+inter.size());
        if (inter.size()>i) {
            System.out.println("si");
        }else System.out.println("no");
        nueva.getDate();
        
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
        int i,j,k;
        double time;
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
                correcto= leerNum(Integer.toString(i)+" + "+Integer.toString(j)+" = ") == i+j;
                k++;
                if (!correcto) {
                    System.out.println("Es necesaro que respondas "+toRed("correctamente")+" a todas las  preguntas: ");
                    System.out.println("Tiempo reiniciado");
                }
            }
        }while(!correcto);
        time+=getDifTiempo(cal);
        System.out.println("tiempo total: "+time/1000+" seg. Tiempo medio: "+time/5/1000+" seg");
        tiempoRespuesta[0]=(int)(time/5);// se guarda en milisegundos
        
        System.out.println("Presiona enter cuando quieras empezar la prueba de "+toBlue("multiplicar")+":");
        waitTillEnter();
        do{
            k=0;
            time=0;
            cal=Calendar.getInstance();
            correcto=true;
            while (k<5 && correcto) {
                i = (int) Math.floor(Math.random()*9);
                j = (int) Math.floor(Math.random()*9);
                correcto= leerNum(Integer.toString(i)+" * "+Integer.toString(j)+" = ") == i*j;
                k++;
                if (!correcto) {
                    System.out.println("Es necesaro que respondas "+toRed("correctamente")+" a todas las  preguntas: ");
                    System.out.println("Tiempo reiniciado");
                }
            }
        }while(!correcto);
        time+=getDifTiempo(cal);
        System.out.println("tiempo total: "+time/1000+" seg. Tiempo medio: "+time/k/1000+" seg");
        tiempoRespuesta[1]=(int)(time/5);// se guarda en milisegundos
        
        System.out.println("Presiona enter cuando quieras empezar la prueba de "+toBlue("escritura")+":");
        waitTillEnter();
        do{
            cal=Calendar.getInstance();
            k=0;
            time=0;
            correcto=true;
            j=0;
            while (k<3 && correcto) {
                System.out.println("Escribe la siguiente cadena:");
                i=(int) Math.floor(Math.random()*posiblesCadenas.length);
                System.out.println("->"+posiblesCadenas[i]);
                correcto= posiblesCadenas[i].toLowerCase().equals(leerCad("->").toLowerCase());
                time+=getDifTiempo(cal);
                k++;
                j+=posiblesCadenas[i].length();
                if (!correcto) {
                    System.out.println("Es necesaro que respondas "+toRed("correctamente")+" a todas las  preguntas: ");
                    System.out.println("Tiempo reiniciado");
                }
            }
        }while(!correcto);
        System.out.println("tiempo total: "+time/1000+" seg. Pulsaciones: "+j+" Tiempo medio: "+(time/j)/1000+"seg");
        tiempoRespuesta[2]=time/j;
    }
    
    
    private int getDifTiempo(Calendar c1){//HORRIBLE POR FAVOR EDITAR
        Calendar c2= Calendar.getInstance();
        int res;
        if (c2.get(Calendar.HOUR_OF_DAY)<c1.get(Calendar.HOUR_OF_DAY)) {
            res= (c2.get(Calendar.HOUR_OF_DAY)+24) - c1.get(Calendar.HOUR_OF_DAY);
        } else res=(c2.get(Calendar.HOUR_OF_DAY)-c1.get(Calendar.HOUR_OF_DAY));
        res*=3600000;
        
        if (c2.get(Calendar.MINUTE)<c1.get(Calendar.MINUTE)) {
            res= (c2.get(Calendar.MINUTE)+60) - c1.get(Calendar.MINUTE);
        } else res=(c2.get(Calendar.MINUTE)-c1.get(Calendar.MINUTE));
        res*=60000;
        
        if (c2.get(Calendar.SECOND)<c1.get(Calendar.SECOND)) {
            res= (c2.get(Calendar.SECOND)+60) - c1.get(Calendar.SECOND);
        } else res=(c2.get(Calendar.SECOND)-c1.get(Calendar.SECOND));
        res*=1000;
        
        return res;
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
        this.toXML();
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
    
    public Element toDom(){
        Element raiz = new Element("HISTORY");
        Element eltoResponseTime= new Element(RESPONSE_TIME);
        Element eltoInter = new Element(INTERACTIONS);
        
        Element eltoT;
        for (int i = 1; i < tiempoRespuesta.length; i++) {
            eltoT =new Element(INTERACTIONS);
            eltoT.appendChild(Double.toString(tiempoRespuesta[i]));
            eltoResponseTime.appendChild(eltoT);    
        }
        
        for (Interaccion interc: inter) {
            eltoInter.appendChild(interc.toDom());
        }
        
        raiz.appendChild(eltoResponseTime);
        raiz.appendChild(eltoInter);
        
        return raiz;
    }
    
    public void toXML(){
        try{
            FileOutputStream f = new FileOutputStream( FILE_NAME );
            Serializer serial = new Serializer( f );
            Document doc = new Document( this.toDom() );
            serial.write(doc);
        }catch(IOException exc){
            System.out.println(toRed("Error en toXML"));
        }
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