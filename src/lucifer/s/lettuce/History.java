/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucifer.s.lettuce;

import Joint.Interaccion;
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
import Joint.Date;
import java.util.Calendar;

/**
 *
 * @author reyga
 */
public class History {
    private static final String FILE_NAME="History.xml";
    private ArrayList<Interaccion> inter ;
    
    public History() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = (Document) db.parse(new File(FILE_NAME));
        } catch (FileNotFoundException exc){
            System.out.println("------------------------------------------");
            System.out.println("No se encutran archivos anteriores."
                    + "\nAsi que bienvenido en su primera vez");
            System.out.println("------------------------------------------");
            inter= new ArrayList<>(10);
        } catch (IOException ex) {
            System.err.println("Error IOException.");
        } catch (ParserConfigurationException ex) {
            System.err.println("Error ParserConfigurationException");
        } catch (SAXException ex) {
            System.err.println("Error ParserConfigurationException");
        }
        
    }

    public void addInteraction() {
        Interaccion nueva = newInteraccion();
        int i=0;
        while () {
            
        }
        inter.add(0, nueva);
    }
    
    private Interaccion newInteraccion(){
        float peso=leerNumFloat("Introduce peso de la maria en gramos: ");
        System.out.println("-------------------------");
        float pesoTotal=leerNumFloat("Introduce el peso total: ");
        System.out.println("-------------------------");
        int tipoFiltro=0;
        do{
            System.out.println("Que tipo de filtro es?"
                    + "\n1.-Carton."
                    + "\n2.-Filtro de carbono.");
            tipoFiltro= leerOP(2);
        }while(tipoFiltro>2 || tipoFiltro<=0);
        System.out.println("-------------------------");
        boolean horaValida=false;
        String momento;
        int hora,min;
        do {
            momento = leerCad("A que hora te lo has fumado? \nIntroduce \":\" si es ahora. (Formato de 24h)(x:y)\n->");
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
                momento = leerCad("Que dia te lo has fumado? \nIntroduce \":\" si es hoy. (x/y)\n->");
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
                    return cal.get(Calendar.MONTH);
                default:
                    System.err.println("Valor de j no valido, avisar al administrador.");
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
                    System.err.println("Hora no posible.");
                }
            return numberToret;
        }
    }
    
   
    
    
    void histoyOp() {
        StringBuilder toret = new StringBuilder();
        if (inter.size()>0) {
            System.out.println("Interaccion mas reciente arriba");
            for (int i = 0; i < inter.size(); i++) {
                    if ( i>0 && inter.get(i).getDate().getDay() >= inter.get(i-1).getDate().getDay()+3 ) {
                        System.out.println("------------------------------");
                        toGreen("Nueva compra.");
                    }
                toret.append(i).append(".- ").append(inter.get(i).toStringResumen()).append("\n");
            }
            System.out.println(toret);
            boolean cont=true;
            int op;
            do{
                op=leerNum("Introduce un numero para ver mas detalles o 0 para volver.- ");
                if (op>inter.size() || op<=0) {
                    System.out.println("Numero no valido.");
                }else if(op>inter.size() || op<0){
                    System.out.println(inter.get(op).toString());
                }
            }while(op!=0);
        }else   System.out.println("No hay ninguna interaccion.");
        //1-dia x has fumado tanta maria
        //2-dia y has fumado tanta maria
        
        //introduce un numero para ver mas detalles o 0 para volver
    }

    void graphycByTime() {
        /*
        |
        |
        |            |
        |        |   |   
        |    |   |   |   |   |
        |____|___|___|___|___|___
        */
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void contInteracion() {
        //si el tiempo desde el ultimo porro introducido es menor qu 20 añade un nuevo dato a la ultima interraccin
        System.out.println("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
}
