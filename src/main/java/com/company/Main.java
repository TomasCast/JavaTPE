package com.company;

import dependencias.Diccionario;
import dependencias.SalidaThread;
import dependencias.UtilidadesOdem;
import grafo.CiclosSimplesJohnson;
import grafo.Grafo;
import grafo.UtilidadesGrafo;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void mostrarGrafo(Grafo g){
        IntLinkedOpenHashSet vertices = g.getVertices();
        System.out.println("__________________________________________________");
        System.out.println("Cantidad de vertices: "+g.getCantVertices());
        System.out.println("Cantidad de arcos: "+g.getCantArcos());

        for (int v: vertices) {
            System.out.println("vertice "+v);
            System.out.println("\t adyacentes: "+g.getAdyacentes(v));
        }
        System.out.println("__________________________________________________");
    }

    public static BufferedWriter crearBufferedWriter(String rutaArchivo){
        FileWriter fw = null;
        try {
            fw = new FileWriter(rutaArchivo);
        }catch (IOException e){
            System.out.println("ruta de archivo ("+rutaArchivo+") no valida...");
            e.printStackTrace();
        }
        if(fw != null)
            return new BufferedWriter(fw);
        else
            return null;
    }

    public static void main(String[] args) throws InterruptedException {
//        Diccionario diccionario = UtilidadesOdem.getPaquetes(args[0]);
//        SalidaThread generadorSalida = new SalidaThread(crearBufferedWriter("ciclos.txt"), diccionario);
//        Thread t = new Thread(generadorSalida);
//        t.start();
//
//        CiclosSimplesJohnson c = new CiclosSimplesJohnson();
//
//
//        Grafo dependencias = UtilidadesGrafo.construirGrafo(diccionario);
//
//        c.correrJohnson(dependencias, generadorSalida);
//        System.out.println(c.suma);
//
//
//        generadorSalida.finalizar();
//        t.join();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch (Exception e){
            e.printStackTrace();
        }
        JFrame frame = new InterfazUI();
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



    }
}
