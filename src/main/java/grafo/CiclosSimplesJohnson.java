package grafo;

import dependencias.SalidaThread;
import it.unimi.dsi.fastutil.ints.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

public class CiclosSimplesJohnson {

    public static int MAX_CICLOS = 9;
    public static int MIN_CICLOS = 3;
    public int suma = 0;

    private IntLinkedOpenHashSet blockedSet = new IntLinkedOpenHashSet();
    private Int2ObjectMap<IntLinkedOpenHashSet> blockedMap = new Int2ObjectLinkedOpenHashMap<>();
    private IntArrayList pila = new IntArrayList();
    private int cantidadCiclosTamano [] = new int[MAX_CICLOS-MIN_CICLOS+1]; //guarda cantidad de ciclos por tamaño

    public CiclosSimplesJohnson(){
        for(int i=0; i<cantidadCiclosTamano.length; i++)
            cantidadCiclosTamano[i] = 0;
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
            componentes.remove(0);

            this.ciclosSimplesComponente(g, componente, minVertice, minVertice, out, new Predicate<IntArrayList>() {
                @Override
                public boolean test(IntArrayList integers) {
                    return false; // como estoy buscando todos los ciclos siempre retorno false (esto se cambia para el bonus track)
                }
            });

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
    /**@return Si hay ciclo en el grafo g que contenga los vertices 'a' y 'b'*/
    public boolean hayCiclo(Grafo g, int a, int b){
        /*1- Crear componentes de g
        * 2- Ver si en a y b estan en la misma componente
        *   2.1- Si no estan, return false
        *   2.2- si estan buscar ciclos*/
        LinkedHashSet<IntLinkedOpenHashSet> componentes = UtilidadesGrafo.componentesFuertementeConectadas(g, g.getVertices());
        IntLinkedOpenHashSet componente = buscarComponenteContiene(a,b,componentes);
        if(componente != null){
            return this.ciclosSimplesComponente(g, componente, a, a, null, new Predicate<IntArrayList>() {
                @Override
                public boolean test(IntArrayList integers) {
                    return integers.contains(a) && integers.contains(b); //condicion de corte para el johnsons sobre la componente
                }
            });
        }
        return false;
    }

    /**@return Busca la Componente que tiene los vertices a y b. En caso de que ninguna los contenga retorna null*/
    private IntLinkedOpenHashSet buscarComponenteContiene(int a, int b, LinkedHashSet<IntLinkedOpenHashSet> componentes){
        for (IntLinkedOpenHashSet componente : componentes) {
            if(componente.contains(a) && componente.contains(b))
                return componente;
        }
        return null;
    }

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

    private boolean ciclosSimplesComponente(Grafo g, IntLinkedOpenHashSet vComponente, int vInicio, int vActual, SalidaThread out, Predicate<IntArrayList> condBonusTrack){
        boolean hayCiclo = false;
        pila.push(vActual);
        blockedSet.add(vActual);
        IntLinkedOpenHashSet adyacentes = new IntLinkedOpenHashSet();

        for (int vertice:vComponente) { // agrego los adyacentes de la componente.
            if(g.esAdyacente(vActual, vertice))
                adyacentes.add(vertice);
        }

        for (int ady : adyacentes) {
            if(ady == vInicio){ // encontre un ciclo
                if (pila.size() >= MIN_CICLOS) {

                    if(condBonusTrack.test(pila)) // esto es para el bonus track, se chequea si ambos vertices a y b estan en el ciclo y se retorna true.
                        return true;

                    if(out != null) // esto es porque cuando hago al bonusTrack, le paso un null como parametro en out (porque no tengo que escribir en archivo)
                        out.agregarCiclo(pila.clone());

                    suma++;

                    cantidadCiclosTamano[pila.size() - MIN_CICLOS]++;
                }
                hayCiclo = true;
            }else{
                if(!blockedSet.contains(ady)) {
                    if (pila.size() < MAX_CICLOS){
                        if (ciclosSimplesComponente(g, vComponente, vInicio, ady, out, condBonusTrack))
                            hayCiclo = true;
                    }else{
                        hayCiclo = true;
                    }
                }
            }
        }

        if(hayCiclo)
            desbloquear(vActual);
        else{
            for (int ady : adyacentes) {
                if(blockedMap.get(ady) == null)
                    blockedMap.put(ady, new IntLinkedOpenHashSet());

                if(!blockedMap.get(ady).contains(vActual))
                    blockedMap.get(ady).add(vActual);
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

    public int[] getCantidadCiclosTamano(){
        return this.cantidadCiclosTamano;
    }


}
