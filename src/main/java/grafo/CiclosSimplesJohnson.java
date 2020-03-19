package grafo;

import dependencias.SalidaThread;
import it.unimi.dsi.fastutil.ints.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.function.Predicate;

public class CiclosSimplesJohnson {

    public static int MAX_CICLOS = 9;
    public static int MIN_CICLOS = 3;
    public int suma = 0;

    private IntLinkedOpenHashSet blockedSet = new IntLinkedOpenHashSet();
    private Int2ObjectMap<IntLinkedOpenHashSet> blockedMap = new Int2ObjectLinkedOpenHashMap<>();
    private IntArrayList pila = new IntArrayList();

    private void desbloquear(int vertice){
        blockedSet.remove(vertice);
        IntLinkedOpenHashSet bloqueados = blockedMap.get(vertice);
        if(bloqueados != null) {
            for (int w : bloqueados) {
                if (blockedSet.contains(w))
                    desbloquear(w);
            }
            blockedMap.remove(vertice);
        }
    }

    public void correrJohnson(Grafo g, SalidaThread out){
        ArrayList<IntLinkedOpenHashSet> componentes = new ArrayList<IntLinkedOpenHashSet>(UtilidadesGrafo.componentesFuertementeConectadas(g,g.getVertices()));
        componentes.removeIf(new Predicate<IntLinkedOpenHashSet>() { //remuevo los de tamaño menor que MIN_CICLOS
            @Override
            public boolean test(IntLinkedOpenHashSet integers) {
                return integers.size() < MIN_CICLOS;
            }
        });

        while (!componentes.isEmpty() && g.getCantVertices() > 0){
            IntLinkedOpenHashSet componente = componentes.get(0);
            int minVertice = getMinVerticeComponentes(componente);
//            System.out.print(componentes);
//            System.out.println("  "+minVertice);
            componentes.remove(0);
            this.ciclosSimplesComponente(g, componente, minVertice, minVertice, out);
            g.eliminarVertice(minVertice);
            componente.remove(minVertice);
            pila.clear();
            blockedSet.clear();
            blockedMap.clear();

            LinkedHashSet<IntLinkedOpenHashSet> componentesNuevas = UtilidadesGrafo.componentesFuertementeConectadas(g, componente);
            for (IntLinkedOpenHashSet nueva:componentesNuevas) {
                componentes.add(componentes.size(),nueva);
            }
        }

    }

    //todo cambiar a private
    public boolean ciclosSimplesComponente(Grafo g, IntLinkedOpenHashSet verticesComponente, int verticeInicio, int verticeActual, SalidaThread out){
        boolean hayCiclo = false;
        pila.push(verticeActual);
        blockedSet.add(verticeActual);
        IntLinkedOpenHashSet adyacentes = new IntLinkedOpenHashSet();

        for (int vertice:verticesComponente) { // agrego los adyacentes de la componente.
            if(g.esAdyacente(verticeActual, vertice))
                adyacentes.add(vertice);
        }

        for (int ady : adyacentes) {
            if(ady == verticeInicio){
                if (pila.size() >= MIN_CICLOS) {
                    out.agregarCiclo(pila.clone());
                    //System.out.println(pila);
                    suma++;
                }
                hayCiclo = true;
            }else{
                if(!blockedSet.contains(ady)) {
                    if (pila.size() < MAX_CICLOS){
                        if (ciclosSimplesComponente(g, verticesComponente, verticeInicio, ady, out))
                            hayCiclo = true;
                    }else{
                        hayCiclo = true;
                    }
                }
            }
        }

        if(hayCiclo)
            desbloquear(verticeActual);
        else{
            for (int ady : adyacentes) {
                if(blockedMap.get(ady) == null)
                    blockedMap.put(ady, new IntLinkedOpenHashSet());

                if(!blockedMap.get(ady).contains(verticeActual))
                    blockedMap.get(ady).add(verticeActual);
            }
        }

        pila.popInt();
        return hayCiclo;
    }

    private int getMinVerticeComponentes(IntLinkedOpenHashSet componente){
        int min = Integer.MAX_VALUE;
        for (int v : componente) {
            if(v < min)
                min = v;
        }
        return min;
    }


}
