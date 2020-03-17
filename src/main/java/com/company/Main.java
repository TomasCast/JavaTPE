package com.company;

import com.sun.deploy.util.ArrayUtil;
import dependencias.Diccionario;
import dependencias.UtilidadesOdem;
import grafo.CiclosSimplesJohnson;
import grafo.Grafo;
import grafo.UtilidadesGrafo;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import org.apache.commons.lang3.ArrayUtils;

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

    public static void main(String[] args) {
        Grafo g = new Grafo(6);
        g.agregarVertice(1);
        g.agregarVertice(2);
        g.agregarVertice(3);
        g.agregarVertice(4);
        g.agregarVertice(5);
        g.agregarVertice(6);

        g.agregarArco(1,2);
        g.agregarArco(1,5);
        g.agregarArco(2,3);
        g.agregarArco(3,2);
        g.agregarArco(3,1);
        g.agregarArco(3,4);
        g.agregarArco(3,6);
        g.agregarArco(4,5);
        g.agregarArco(5,2);
        g.agregarArco(6,4);

        CiclosSimplesJohnson c = new CiclosSimplesJohnson();
        c.ciclosSimplesComponente(g,1,1);

        g.eliminarVertice(1);
        c.ciclosSimplesComponente(g,2,2);











//        Diccionario diccionario = UtilidadesOdem.getPaquetes(args[0]);
//        Grafo dependencias = UtilidadesGrafo.construirGrafo(diccionario);
//
//        mostrarGrafo(dependencias);
//        System.out.println(UtilidadesGrafo.componentesFuertementeConectadas(dependencias));
//        Grafo g = new Grafo(2);
//        g.agregarVertice(1);
//        g.agregarVertice(2);
//        g.agregarVertice(3);
//        g.agregarVertice(4);
//        g.agregarVertice(5);
//        g.agregarVertice(6);
//        g.agregarVertice(7);
//        g.agregarVertice(8);
//        g.agregarVertice(9);
//        g.agregarVertice(10);
//        g.agregarVertice(11);
//
//        g.agregarArco(1,3);
//        g.agregarArco(2,1);
//        g.agregarArco(3,2);
//        g.agregarArco(3,4);
//        g.agregarArco(4,5);
//        g.agregarArco(5,6);
//        g.agregarArco(6,4);
//        g.agregarArco(7,6);
//        g.agregarArco(7,8);
//        g.agregarArco(8,9);
//        g.agregarArco(9,10);
//        g.agregarArco(10,7);
//        g.agregarArco(10,11);
//
//
//        System.out.println("Grafo g");
//        mostrarGrafo(g);
//
//        System.out.println(UtilidadesGrafo.componentesFuertementeConectadas(g));

    }
}
