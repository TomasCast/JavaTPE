package dependencias;

import java.util.ArrayList;


public class Paquete {
    /**Clase que permite registrar el nombre, las dependencias y las clases que define un paquete.*/
    private String nombre;
    private ArrayList<String> clases;
    private ArrayList<String>dependencias;

    Paquete(String nombre){
        this.nombre = nombre;
        clases = new ArrayList<String>();
        dependencias = new ArrayList<String>();
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
    public String getNombre() {
        return nombre;
    }
}
