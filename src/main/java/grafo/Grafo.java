package grafo;

import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;

import java.util.LinkedHashSet;


public class Grafo {
    private final int TAM_DEFECTO = 1000;
    private final int FACTOR_CRECIMIENTO = 2;

    private boolean [][] matriz; //matriz de adyacencia
    private boolean [] borrado;  //indica que vertice fue borrado
    private int maxCantidadVertices = 0; //indica la cantidad maxima de vertices que se encontraron activos en el grafo
    private int cantidadVerticesActivos = 0; // indica la cantidad de vertices que no estan borrados (en boorado[])
    private int cantidadArcos = 0;


    /*todo Cosas importantes que hablar en el informe:
    *  --> El grafo esta implementado con matriz de adyacencia
    *  --> Cuando se llena la matriz crece segun un factor de crecimiento (2)
    *  --> Puedo agregar vertices con numero (su id) pero este no puede ser mas grande
    *  que el tamaño de la matriz. Si es igual al tamaño, se agranda la matriz segun el
    *  factor de crecimiento*/

    /**
     * @return true si destino es adyacente a origen*/
    public boolean esAdyacente(int origen, int destino){
        if(origen<borrado.length && destino<borrado.length)
            return matriz[origen][destino];
        return false;
    }

    public boolean esAdyacenteReverso(int origen, int destino){
        if(origen<matriz.length && destino<matriz.length)
            return matriz[destino][origen];
        return false;
    }

    /**
     * Constructor de Grafo en base a sus atributos.
     * @param borrados contiene la informacion sobre el borrado logico de los vertices
     * @param matriz matriz de adyacencia del grafo
     * @param maxCantidadVertices cantidad de vertices maxima que ha tenido el grafo
     * @param cantidadVerticesActivos cantidad de vertices activos que posee el grafo
     * */
    private Grafo(boolean [] borrados, boolean [][] matriz, int maxCantidadVertices, int cantidadVerticesActivos,int cantidadArcos){
        this.borrado = borrados;
        this.matriz = matriz;
        this.maxCantidadVertices = maxCantidadVertices;
        this.cantidadVerticesActivos = cantidadVerticesActivos;
        this.cantidadArcos = cantidadArcos;
    }

    public Grafo(){
        matriz = new boolean [TAM_DEFECTO][TAM_DEFECTO];
        borrado = new boolean[TAM_DEFECTO];
        inicializarEstructuras(this.borrado, this.matriz);
    }

    public Grafo(int tamanio){
        matriz = new boolean[tamanio][tamanio];
        borrado = new boolean[tamanio];
        inicializarEstructuras(this.borrado, this.matriz);
    }

    private void inicializarEstructuras(boolean [] borrado, boolean[][] matriz){
        for(int i=0; i<matriz.length; i++){
            borrado[i] = true;
            for(int j=0; j< matriz.length; j++)
                matriz[i][j] = false;
        }
    }

    /*
    * Agrega un vertice al grafo.
    * todo revisar
    * */
    public void agregarVertice(int nuevo){
        if(nuevo >= borrado.length || borrado[nuevo])
            if(nuevo <= matriz.length){ // puedo agregar siempre un vertice que entre en la matriz o que exceda su tamaño por 1
                cantidadVerticesActivos++;
                if(cantidadVerticesActivos > maxCantidadVertices) {
                    maxCantidadVertices++;// si supero la cantidad maxima de vertices, entonces debo actualizar maxCantVertices
                    if(maxCantidadVertices >= matriz.length){ // si excedo el tamaño de la matriz, tengo que agrandar
                        boolean [] nuevoBorrado = new boolean[borrado.length*FACTOR_CRECIMIENTO];
                        boolean [][] nuevaMatriz = new boolean[matriz.length*FACTOR_CRECIMIENTO][matriz.length*FACTOR_CRECIMIENTO];
                        inicializarEstructuras(nuevoBorrado, nuevaMatriz);
                        for(int i=0; i<matriz.length;i++){
                            nuevoBorrado[i] = borrado[i];
                            for(int j=0; j<matriz.length; j++){
                                nuevaMatriz[i][j] = matriz[i][j];
                            }
                        }
                        matriz = nuevaMatriz;
                        borrado = nuevoBorrado;
                    }
                }
                borrado[nuevo] = false;
            }

        /*
        * Esto quiere decir que si agrego 4 vertices, (1,2,3,4) y elimino el 3, tendre (1,2,4). Si agrego uno mas,
        * agregare el 5 (1,2,4,5)
        * */
    }

    public void eliminarVertice(int vertice){
        borrado[vertice] = true;
        cantidadVerticesActivos--;
        for(int i=0; i<matriz.length; i++) { // elimino todos los arcos que tienen que ver con ese vertice
            matriz[i][vertice] = false;
            matriz[vertice][i] = false;
        }
    }

    public int getCantVertices() {
        return cantidadVerticesActivos;
    }

    public int getCantArcos(){
        return cantidadArcos;
    }

    public void agregarArco(int origen, int destino){
        if(!borrado[origen] && !borrado[destino] &&!matriz[origen][destino]) {
            matriz[origen][destino] = true;
            cantidadArcos++;
        }
    }

    public void eliminarArco(int origen, int destino){
        if(!borrado[origen] && !borrado[destino])
            matriz[origen][destino] = false;
    }

    public IntLinkedOpenHashSet getVertices(){
        int [] vertices = new int[cantidadVerticesActivos];
        int agregados = 0;
        int verticeActual = 0;
        while (agregados < cantidadVerticesActivos){
            if(!borrado[verticeActual]){
              vertices[agregados] = verticeActual;
              agregados++;
            }
            verticeActual++;
        }
        return new IntLinkedOpenHashSet(vertices);
    }

    public IntLinkedOpenHashSet getAdyacentes(int vertice){
        if(vertice<borrado.length && !borrado[vertice]) {
            IntLinkedOpenHashSet adyacentes = new IntLinkedOpenHashSet();
            int agregados = 0;
            int verticeActual = 0;
            while (verticeActual <= maxCantidadVertices && agregados < cantidadVerticesActivos) {
                if (!borrado[verticeActual] && matriz[vertice][verticeActual]) {
                    adyacentes.add(verticeActual);
                    agregados++;
                }
                verticeActual++;
            }

            return adyacentes;
        }
        return null;
    }
    /*
        /**
         * @param g Grafo a convertir en reverso.
         * @return Grafo reverso de g.*/
    /*public static Grafo transformarReverso(Grafo g){
        boolean [][] matriz = new boolean[g.matriz.length][g.matriz.length];
        boolean [] borrado = new boolean[g.borrado.length];

        for(int i=0; i<matriz.length; i++){ // revierto la matriz
            borrado[i] = g.borrado[i];
            for(int j=0; j<matriz.length; j++)
                matriz[i][j] = g.matriz[j][i];
        }
        return new Grafo(borrado, matriz, g.maxCantidadVertices, g.cantidadVerticesActivos, g.cantidadArcos);
    }*/


}
