package com.mycompany.stackmgr;

//import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        
        /*Pila nombres = new Pila();
        Scanner scanner = new Scanner(System.in);
        
        for (int i = 1; i <= 10; i++) {
            System.out.println("Introduzca un nombre: ");
            String nombre = scanner.nextLine();
            nombres.apilar(nombre);
        }*/
        
        AppGUI.launch(AppGUI.class, args);
    }
}