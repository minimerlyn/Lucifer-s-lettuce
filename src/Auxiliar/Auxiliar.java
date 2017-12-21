/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auxiliar;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author reyga
 */
public class Auxiliar {
    public static String [] posiblesCadenas = {"Me comi una zapatilla sin aceitunas", "Mi perro es verde porque se enfado",
                                               "Estoy tan drogado que no se leer bien", "Cada dia me como al menos un huevo",
                                                "Me encanta jugar con el ordenador apagado", "Tonto el que lo lea al reves",
                                                "No se por que hago esta mierda ahora", "Suelo beber pegamento por las mañanas"};
    
    private static void imprimirCad(String ... st){
        StringBuilder cad = new StringBuilder();
        for (String st1 : st) {
            cad.append(st1);
        }
        System.out.print(cad.toString());
    }
    
    /**
     * 
     * @param n numero maximo de opciones
     * @return integer, opcion deseada
     */
    public static int leerOP(int n){
        boolean validNumber=false;
        int op;
        do {
            op  = leerNum("Introduzca opcion.- ");
            if (op>0 && op<=n) {
                validNumber=true;
            }else{
                System.out.println(toRed("Opcion no valida."));
            }
        } while (!validNumber);
        return op;
        
        
    }
    /**
     * 
     * @param st cadena que se va a mostrar antes de solicitar valor
     * @return cadena introducida
     */
    public static String leerCad(String ... st){
        Scanner scan = new Scanner(System.in);
        imprimirCad(st);
        return scan.nextLine().trim();
    }

    /**
     * 
     * @param st chain which is going to be sended to leerCad
     * @return chain parsed to integer.
     */
    public  static int leerNum(String ... st) {
        String conv=leerCad(st);
        try{
            return Integer.parseInt(conv);
        }catch (NumberFormatException exc){
            System.out.println(toRed("Introduce un numero sin letra"));
            return -1;
        }
    }
    public static float leerNumFloat(String ... st){
        String conv=leerCad(st);
        try{
            return Float.parseFloat(conv);
        }catch (NumberFormatException exc){
            System.out.println(toRed("Introduce un numero sin letra"));
            return -1;
        }
    }
    
    public static int parseInt(String st){ //prvate y non static
        int toret;
        try{
            toret=Integer.parseInt(st);
            
        }catch(NumberFormatException exc){
            System.out.println(toRed("Numero introducido no es valido."));
            toret=-1;
        }
        return toret;
    }
    
    public static int charToInt(char c){
        StringBuilder chain= new StringBuilder();
        chain.append(c);
        int toret;
        try{
            toret=Integer.parseInt(chain.toString());
        }catch(NumberFormatException exc){
            toret=-1;
        }
        return toret;
    }
    
    public static void clear(){
        for (int i = 0; i < 10; i++) {
            System.out.println("\n");
        }
    }
    
    public static void waitTillEnter( ) {
        try{
            while( ( System.in.read() ) != '\n' );
        }catch( IOException exc){
            System.out.println(toRed("Error en waitTillEnter()"));
        }
    }
    
    /*
        System.out.println("\033[30mEste texto es Negro"); por defecto
        System.out.println("\033[31mEste texto es Rojo"); toRed(String)
        System.out.println("\033[32mEste texto es Verde"); toGreen(String)
        System.out.println("\033[33mEste texto es Amarillo");
        System.out.println("\033[34mEste texto es Azul"); toBlue(String)
        System.out.println("\033[35mEste texto es Magenta");
        System.out.println("\033[36mEste texto es Cyan");
        System.out.println("\033[37mEste texto es Blanco")
    */
    
    public static String toGreen(String st){
         StringBuilder toret = new StringBuilder();
        toret.append("\033[32m").append(st).append("\033[30m");
        return toret.toString();
    }
    public static String toRed(String st){
        StringBuilder toret = new StringBuilder();
        toret.append("\033[31m").append(st).append("\033[30m");
        return toret.toString();
    }
    
    public static String toBlue(String st){
        StringBuilder toret = new StringBuilder();
        toret.append("\033[34m").append(st).append("\033[30m");
        return toret.toString();
    }
}
