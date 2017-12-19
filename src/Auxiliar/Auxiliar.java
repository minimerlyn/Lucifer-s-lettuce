/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auxiliar;

import java.util.Scanner;

/**
 *
 * @author reyga
 */
public class Auxiliar {
    
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
                System.out.println("Opcion no valida.");
            }
        } while (!validNumber);
        return op;
        
        
    }
    /**
     * 
     * @param st cadena que se va a mostrar antes de solicitar valor
     * @return cadena introducida
     */
    public static String leerCad(String st){
        Scanner scan = new Scanner(System.in);
        System.out.print(st);
        return scan.nextLine().trim();
    }

    /**
     * 
     * @param st chain which is going to be sended to leerCad
     * @return chain parsed to integer.
     */
    public  static int leerNum(String st) {
        String conv=leerCad(st);
        try{
            return Integer.parseInt(conv);
        }catch (NumberFormatException exc){
            System.err.println("Introduce un numero sin letra");
            return -1;
        }
    }
    public static float leerNumFloat(String st){
        String conv=leerCad(st);
        try{
            return Float.parseFloat(conv);
        }catch (NumberFormatException exc){
            System.err.println("Introduce un numero sin letra");
            return -1;
        }
    }
    
    public static int parseInt(String st){ //prvate y non static
        int toret;
        try{
            toret=Integer.parseInt(st);
            
        }catch(NumberFormatException exc){
            System.err.println("Numero introducido no es valido.");
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
    /*
        System.out.println("\033[30mEste texto es Negro");
        System.out.println("\033[31mEste texto es Rojo");
        System.out.println("\033[32mEste texto es Verde");
        System.out.println("\033[33mEste texto es Amarillo");
        System.out.println("\033[34mEste texto es Azul");
        System.out.println("\033[35mEste texto es Magenta");
        System.out.println("\033[36mEste texto es Cyan");
        System.out.println("\033[37mEste texto es Blanco")
    */
    
    public static void toGreen(String st){
        System.out.println("\\033[32m"+st+"\033[30m");
    }
}
