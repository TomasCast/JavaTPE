package grafo;

import dependencias.Diccionario;
import dependencias.Paquete;
import it.unimi.dsi.fastutil.ints.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.function.Predicate;

public class UtilidadesGrafo {

    /**
     * @return Un conjunto de conjuntos de vertices, con las componentes fuertemente conectadas del grafo g
     * */
    public static LinkedHashSet<IntLinkedOpenHashSet> componentesFuertementeConectadas(Grafo g, IntLinkedOpenHashSet vertices){
        //Algoritmo de Kosaraju
        IntArrayList pila = new IntArrayList(g.getCantVertices()); //pila inicializada con el tamaño del grafo
        DFS_Forest(g,vertices,pila);
        return construirComponentes(g.getReversa(),vertices,pila);
    }

    /*Permite crear la pila que contendra los nodos del grafo en orden de finalizacion, la cual sera
    * utilizada posteriormente por el algoritmo
    * */
    private static void DFS_Forest(Grafo g,IntLinkedOpenHashSet verticesC, IntStack pila){
        IntLinkedOpenHashSet visitados = new IntLinkedOpenHashSet(verticesC.size());
        for (int v: verticesC) {
            if(!visitados.contains(v)){
                System.out.println("Procesando "+v+"...");
                visitados.add(v);
                DFS(g,verticesC,v,visitados,pila);
            }
        }
        System.out.println(pila);
    }

    /*DFS recursivo, usado por DFS_Forest*/
    private static void DFS(Grafo g,IntLinkedOpenHashSet verticesC, int vertice, IntLinkedOpenHashSet visitados, IntStack pila){

        /*IntLinkedOpenHashSet adyacentes = g.getAdyacentes(vertice);
        adyacentes.removeIf(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return verticesC.contains(integer);
            }
        });*/
        IntLinkedOpenHashSet adyacentes = new IntLinkedOpenHashSet();
        for (int i:verticesC){
            if(g.esAdyacente(vertice,i))
                adyacentes.add(i);
        }
        for (int ady: adyacentes) {
            if(!visitados.contains(ady)) {
                visitados.add(ady);
                DFS(g, verticesC, ady, visitados, pila);
            }
        }
        pila.push(vertice);
    }

    /*Construye las componentes fuertemente conectadas del grafo, explorandolo en el orden resultante
    * de desapilar los elementos de la pila
    * */
    private static LinkedHashSet<IntLinkedOpenHashSet> construirComponentes(boolean[][] reverso,IntLinkedOpenHashSet verticesC, IntStack pila){
        LinkedHashSet<IntLinkedOpenHashSet> componentes = new LinkedHashSet<>();
        IntLinkedOpenHashSet visitados = new IntLinkedOpenHashSet(verticesC.size());
        while(!pila.isEmpty()){
            int vertice = pila.popInt();
            if(!visitados.contains(vertice)){
                visitados.add(vertice);
                IntLinkedOpenHashSet componente= new IntLinkedOpenHashSet();
                DFS_componentes(reverso,verticesC, vertice, componente, visitados);
                componentes.add(componente);
            }
        }

        return componentes;
    }

    /*Similar al DFS pero, a medida que va recorriendo, agrega los vertices a la componente.*/
    private static void DFS_componentes(boolean[][]matrizR,IntLinkedOpenHashSet verticesC, int vertice_actual, IntLinkedOpenHashSet componente, IntLinkedOpenHashSet visitados){
        componente.add(vertice_actual);
        /*IntLinkedOpenHashSet adyacentes = reverso.getAdyacentes(vertice_actual);*/
        IntLinkedOpenHashSet adyacentes = new IntLinkedOpenHashSet();
        for (int i:verticesC){
            if(matrizR[vertice_actual][i])
                adyacentes.add(i);
        }
        for(int ady : adyacentes){
            if(!visitados.contains(ady)){
                visitados.add(ady);
                DFS_componentes(matrizR,verticesC,ady,componente,visitados);
            }
        }
    }

    public static Grafo construirGrafo(Diccionario dic){
        Grafo grafo = new Grafo(dic.getCantPaquetes());

        //agrego los vertices
        for(int i=0; i<dic.getCantPaquetes(); i++){
            grafo.agregarVertice(i);
        }

        //agrego los arcos (dependencias)
        for(int i = 0; i < grafo.getCantVertices(); i++){
            //por cada paquete i, obtengo las dependencias
            ArrayList<String> dependencias = dic.getPaquete(i).getDependencias(); //clases
            for (String c_dep: dependencias) {
                if(dic.buscarPaquete(c_dep)!=null && i != dic.getNumero(dic.buscarPaquete(c_dep).getNombre())) {
                    grafo.agregarArco(i, dic.getNumero(dic.buscarPaquete(c_dep).getNombre()));
                }
            }
        }

        return grafo;
    }

}
