package dependencias;

import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import java.util.ArrayList;

public class Diccionario {
    /**Esta clase tiene como objetivo agrupar un conjunto de paquetes y agregar funcionalidades sobre estos*/

    private Object2IntLinkedOpenHashMap<String> dic = new Object2IntLinkedOpenHashMap<String>();
    private ArrayList<Paquete> paquetes = new ArrayList<>();

    /**Agrega un paquete al diccionario
     * @param paq paquete a agregar.*/
    public void addPaq(Paquete paq){
        paquetes.add(paq);
        dic.put(paq.getNombre(),paquetes.size()-1);
    }

    /**@returns retorna la cantidad de paquetes contenidos.*/
    public int getCantPaquetes(){
        return paquetes.size();
    }

    /**Permite aceder a un paquete que esta en la posicion indicada en indice*/
    public Paquete getPaquete(int indice){
        if(indice < paquetes.size())
            return paquetes.get(indice);
        return null;
    }

    /**Devuelve el int que corrsponde al nombre de un paquete
     * @param nombre nombre del paquete del
     *               cual se quiere conocer su
     *               numero.*/
    public int getNumero(String nombre){
        return dic.getInt(nombre);
    }

    /**Devuelve el nombre del paquete que corresponde a un numero
     * @param numero numero del paquete del cual
     *               se quiere conocer su nombre.*/
    public String getNombre(int numero){
        return paquetes.get(numero).getNombre();
    }

    /**Permite conocer que paquete del diccionario define esa clase
     * @param nombreClase nombre de la clase de la cual
     *                    quiero conocer el paquete que
     *                    la define.
     * @returns paquete que define nombreClase. retorna null si este
     * no existe.*/
    public Paquete buscarPaquete(String nombreClase){
        for(int i = 0; i < paquetes.size(); i++){
            if(paquetes.get(i).define(nombreClase)){
                return paquetes.get(i);
            }
        }
        return null;
    }

    /**Permite traducir de integer a string un conjunto de numeros de paquete
     * @return el correspondiente conjunto pero con los nombres de paquete que
     * corresponden a cada numero*/
    public ArrayList<String> traducirIntAString(IntLinkedOpenHashSet set){
        ArrayList<String> s = new ArrayList<>();
        for(int i : set){
            s.add(getNombre(i));
        }
        return s;
    }

    /**@returns Los paquetes contenidos en el Diccionario.*/
    public ArrayList<Paquete> getPaquetes(){
        return paquetes;
    }

    public StringBuilder mostrarPaquetes(){
        StringBuilder salida = new StringBuilder();
        for (Paquete p:paquetes) {
            salida.append(p.getNombre());
            salida.append("\n Clases:\n");
            for (String clase:p.getClases()){
                salida.append("\t");
                salida.append(clase);
                salida.append("\n");
            }

            salida.append("Dependencias:\n");
            for (String dependencia:p.getDependencias()) {
                salida.append("\t");
                salida.append(dependencia);
                salida.append("\n");
            }
        }
        return salida;
    }

}
