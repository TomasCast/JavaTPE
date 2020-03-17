package dependencias;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;


public class Paquete {
    /**Clase que permite registrar el nombre, las dependencias y las clases que define un paquete.*/
    private String nombre;
    private Set<String> clases;
    private Set<String>dependencias;

    Paquete(String nombre){
        this.nombre = nombre;
        clases = new LinkedHashSet<String>();
        dependencias = new LinkedHashSet<String>();
    }

    /**@return Si el paquete define a la clase.
     * */
    public boolean define(String clase){
        return clases.contains(clase);
    }

    /**@return Si el paquete depende de la clase.*/
    public boolean depende(String clase){
        return dependencias.contains(clase);
    }

    /**@return Lista de clases de las que depende el paquete.*/
    public ArrayList<String> getDependencias(){
        return new ArrayList<String>(dependencias);
    }

    /**@return Lista de las clases que define el paquete.*/
    public ArrayList<String> getClases(){
        return new ArrayList<String>(clases);
    }

    /**Permite agregar una clase al paquete.
     * @param clase clase a agreggar.*/
    public void addClase(String clase){
        clases.add(clase);
    }

    /**Permite agregar dependencias al paquete.
     * @param clase clase a depender.*/
    public void addDependecia(String clase){
        dependencias.add(clase);
    }

    /**@return Nombre del paquete.*/
    public String getNombre(){
        return nombre;
    }

    /**Permite imprimir por consola el paquete, sus clases y dependencias.*/
    public void show(){  
        System.out.println("Paquete: "+nombre);
        System.out.println("\tClases: ");
        for(String s : clases)
            System.out.println("\t\t"+s);
        System.out.println("\tDependencias: ");
        for(String s : dependencias)
            System.out.println("\t\t"+s);
    }
}
