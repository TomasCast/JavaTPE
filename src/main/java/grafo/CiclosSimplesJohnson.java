package grafo;

import it.unimi.dsi.fastutil.ints.*;

import java.util.LinkedHashSet;

public class CiclosSimplesJohnson {

    private IntLinkedOpenHashSet blockedSet = new IntLinkedOpenHashSet();
    private Int2ObjectMap<IntLinkedOpenHashSet> blockedMap = new Int2ObjectLinkedOpenHashMap<>();
    private IntStack pila = new IntArrayList();

    private void desbloquear(int vertice){
        blockedSet.remove(vertice);
        IntLinkedOpenHashSet bloqueados = blockedMap.get(vertice);
        if(bloqueados != null)
            for (int w : bloqueados) {
                bloqueados.remove(w);
                if(blockedSet.contains(w))
                    desbloquear(w);
            }
    }

    public void correrJohnson(Grafo G){
        LinkedHashSet<IntLinkedOpenHashSet> componentes = UtilidadesGrafo.componentesFuertementeConectadas(G,G.getVertices());
        while (!componentes.isEmpty()){
        }
    }

    //todo cambiar a private
    public boolean ciclosSimplesComponente(Grafo componente, int verticeInicio, int verticeActual){
        boolean hayCiclo = false;
        pila.push(verticeActual);
        blockedSet.add(verticeActual);

        for (int ady : componente.getAdyacentes(verticeActual)) {
            if(ady == verticeInicio){
                System.out.println("hay ciclo: "+pila); //aca hay que imprimir en el archivo y demas
                hayCiclo = true;
            }else{
                if(!blockedSet.contains(ady))
                    if(ciclosSimplesComponente(componente,verticeInicio,ady))
                        hayCiclo = true;
            }
        }

        if(hayCiclo)
            desbloquear(verticeActual);
        else{
            for (int ady : componente.getAdyacentes(verticeActual)) {
                if(blockedMap.get(ady) == null)
                    blockedMap.put(ady, new IntLinkedOpenHashSet());
                if(!blockedMap.get(ady).contains(verticeActual))
                    blockedMap.get(ady).add(verticeActual);
            }
        }

        pila.popInt();
        return hayCiclo;
    }
}
