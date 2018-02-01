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
    private static final String TIMES="TIME";  
    private static final String INTERACTIONS="INTERACTIONS";
    private static final String INTERACTION="INTERACTION";
    
    private ArrayList<Interaccion> inter ;//en la posicion 0 la mas reciente
    //0- suma, 1- mult, 2 cadenas
    private double [] tiempoRespuesta = new double[3];//3 juegos distintos en milisegundos 
    
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
                
                Elements tiempos=eltoTR.getChildElements(TIMES);
                if (tiempos.size()==0) {
                    tiempoRespuesta= new double[4];
                    System.out.println("No hay tiempos de respuesta anteriores. Pasando a calibracion:");
                    calibracion();
                }else{
                    for (int i = 0; i < tiempos.size(); i++) {
                        tiempoRespuesta[i]=Double.parseDouble(tiempos.get(i).getValue());
                    }
                    System.out.println(toGreen("Tiempos de respuesta detectados y añadidos."));
                }
            }
            
            if (eltoInter == null) {
                System.out.println(toRed("No se encuentran interracciones anteriores."));
            }else{
                Elements inters = eltoInter.getChildElements(INTERACTION);
                 inter = new ArrayList<>();
                if (inters.size()==0) { 
                    System.out.println("No hay ninguna interaccion.");
                }else{
                    for (int i = 0; i < inters.size(); i++) {
                        inter.add(new Interaccion(inters.get(i)));
                    }
                    System.out.println(toGreen("Interacciones detectadas y añadidas."));
                }
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
        while (i<inter.size() && !nueva.getDate().masReciente(inter.get(i).getDate())) {
            i++;
        }
        System.out.println("se añade en la posicion "+i);
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
        tiempoRespuesta[0]=(time/k);// se guarda en milisegundos
        
        System.out.println("Presiona enter cuando quieras empezar la prueba de "+toBlue("multiplicar")+":");
        waitTillEnter();
        do{
            k=0;
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
        time=getDifTiempo(cal);
        System.out.println("tiempo total: "+time/1000+" seg. Tiempo medio: "+time/k/1000+" seg");
        tiempoRespuesta[1]=(time/k);// se guarda en milisegundos
        
        System.out.println("Presiona enter cuando quieras empezar la prueba de "+toBlue("escritura")+":");
        waitTillEnter();
        ArrayList<Integer> cad;
        do{
            cal=Calendar.getInstance();
            k=j=0;
            i=-1;
            time=0;
            correcto=true;
            cad = new ArrayList<>(4);
            while (k<4 && correcto) {
                System.out.println("Escribe la siguiente cadena:");
                do {//para que no se repitan las cadenas.
                    i=(int) Math.floor(Math.random()*posiblesCadenas.length);
                } while (cad.contains(i));
                cad.add(i);
                System.out.println("->"+posiblesCadenas[i]);
                correcto= posiblesCadenas[i].toLowerCase().equals(leerCad("->").toLowerCase());
                k++;
                j+=posiblesCadenas[i].length();
                if (!correcto) {
                    System.out.println("Es necesaro que respondas "+toRed("correctamente")+" a todas las  preguntas: ");
                    System.out.println("Tiempo reiniciado");
                }
            }
        }while(!correcto);
        time=getDifTiempo(cal);
        System.out.println("tiempo total: "+time/1000+" seg. Pulsaciones: "+j+" Tiempo medio: "+(time/j)/1000+"seg");
        tiempoRespuesta[2]= (int) time/j;
    }
    
    
    private int getDifTiempo(Calendar c1){//HORRIBLE POR FAVOR EDITAR
        Calendar c2= Calendar.getInstance();
        int res;
        int t1=0;
        int t2=0;
        if (c1.get(Calendar.HOUR_OF_DAY)!=c2.get(Calendar.HOUR_OF_DAY)) {
            t1+=c1.get(Calendar.HOUR_OF_DAY)*3600;
            t2+=c2.get(Calendar.HOUR_OF_DAY)*3600;
        }
        if (c1.get(Calendar.MINUTE)!=c2.get(Calendar.MINUTE)) {
            t1+=c1.get(Calendar.MINUTE)*60;
            t2+=c2.get(Calendar.MINUTE)*60;
        }
        if (c1.get(Calendar.SECOND)!=c2.get(Calendar.SECOND)) {
            t1+=c1.get(Calendar.SECOND);
            t2+=c2.get(Calendar.SECOND);
        }
        //t1 y t2 estan en segundos
        t1*=1000;
        t2*=1000;
        //t1 y t2 estan en milisegundos
        if (c1.get(Calendar.MILLISECOND)!=c2.get(Calendar.MILLISECOND)) {
            t1+=c1.get(Calendar.MILLISECOND);
            t2+=c2.get(Calendar.MILLISECOND);
        }
        
        return t2-t1;
    }
    
    /*
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
    */
    
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

    /**
     *  @decrypted
     */
    public void QuantityGraphycByTime() {
        
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
    

    public void contInteracion() {//acabar
        //esperar confirmacion del usuario
        //si es la primera int no importa el tiempo de espera
        //si el tiempo desde la ultima interaccion es mayor que 10 ( la maquina ya ha esperado 10 min antes) hace una media entre la ultima int y esta
        Calendar c0=Calendar.getInstance();
        waitTillEnter();
        double t1=getDifTiempo(c0);
        System.out.println("Cometer "+toRed("errores")+ " supone una penalizacion de tiempo.");
        int i,j,k;
        k=0;
        float [] tiempos = new float[3];
        int [] errores=new int[3];
        
        boolean correcto;
        System.out.println("Pulsa enter para empeza la prueba de "+toBlue("suma")+":");
        errores[0]=0;
        Calendar c1=Calendar.getInstance();
        while (k<3) {
            i = (int) Math.floor(Math.random()*9);
            j = (int) Math.floor(Math.random()*9);
            correcto= leerNum(Integer.toString(i)+" + "+Integer.toString(j)+" = ") == i+j;
            if (!correcto) errores[0]++;
            k++;
        }
        tiempos[0]=getDifTiempo(c1);
        
        k=0;
        System.out.println("Pulsa enter para empeza la prueba de "+toBlue("multiplicacion")+":");
        errores[1]=0;
        Calendar c2=Calendar.getInstance();
        while (k<3) {
            i = (int) Math.floor(Math.random()*9);
            j = (int) Math.floor(Math.random()*9);
            correcto= leerNum(Integer.toString(i)+" + "+Integer.toString(j)+" = ") == i*j;
            if (!correcto) errores[1]++;
            k++;
        }
        tiempos[1]=getDifTiempo(c1);
        
        k=j=0;
        System.out.println("Pulsa enter para empeza la prueba de "+toBlue("escritura")+":");
        errores[2]=0;
        ArrayList<Integer> cad = new ArrayList<>();
        Calendar c3=Calendar.getInstance();
        while (k<2) {
            i = (int) Math.floor(Math.random()*9);
            System.out.println("Escribe la siguiente cadena:");
            do {//para que no se repitan las cadenas.
                i=(int) Math.floor(Math.random()*posiblesCadenas.length);
            } while (cad.contains(i));
            cad.add(i);
            System.out.println("->"+posiblesCadenas[i]);
            String answer =leerCad("->").toLowerCase();
            if (posiblesCadenas[i].toLowerCase().equals(answer)) {
                int letra=0;
                while (letra<posiblesCadenas[i].length()) {
                    if (posiblesCadenas[i].toLowerCase().charAt(letra)!=answer.toLowerCase().charAt(letra)) {
                        answer.
                    }
                }
            }
            k++;
            j+=posiblesCadenas[i].length();
            
            k++;
        }
        tiempos[0]=getDifTiempo(c1);
        
        //HTC actual= new Interaccion();
        if (inter.get(0).getHtc().size()>0) {//true- hay mas de un obj en thc
            if (getDifTiempo(c1)>600000) {//hacer aqui un while (mientras las diff de tiempo sean mayores que 20 min
                inter.get(0).addHtcInteracion(getHTCIntermedios(actual));
            }
        }
        inter.get(0).addHtcInteracion(actual);
        System.out.println("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private HTC getHTCIntermedios(HTC nueva){
        HTC anterior=inter.get(0).getHtc().get(inter.get(0).getHtc().size()-1);
        
        int aux=nueva.getHora();
        if (aux==0) aux=24;
        int hora=(anterior.getHora()+aux)/2;
        if (aux<anterior.getHora()) hora-=12;//ya tenemos la hora
        
        aux=nueva.getMinuto();
        if (aux==0) aux=60;
        int minuto=(anterior.getHora()+aux)/2;
        if (aux<anterior.getMinuto()) minuto-=60;
        
        int level = (anterior.getLevel()+nueva.getLevel())/2;
        
        return new HTC(hora,minuto,level);
    }
    
    public Element toDom(){
        Element raiz = new Element("HISTORY");
        Element eltoResponseTime= new Element(RESPONSE_TIME);
        Element eltoInter = new Element(INTERACTIONS);
        
        Element eltoT;
        for (int i = 0; i < tiempoRespuesta.length; i++) {
            eltoT =new Element(TIMES);
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