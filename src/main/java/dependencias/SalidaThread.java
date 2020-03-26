package dependencias;

import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SalidaThread implements Runnable {
    private ArrayList<IntArrayList> ciclos = new ArrayList<>();
    private BufferedWriter bwSalida;
    private int primero;
    private boolean seguir = true;

    public SalidaThread(BufferedWriter bw, Diccionario d){
        this.bwSalida = bw;
        try {
            this.imprimirTablaMapeos(d);
        } catch (IOException e) {
            System.out.println("Error al escribir la tabla de mapeo");
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        while(seguir || ciclos.size() > 0){
            try {
                IntArrayList cicloActual = obtenerSiguiente();
                int primero = cicloActual.getInt(0);
                for (int i=0; i < cicloActual.size(); i++) {
                    bwSalida.write(Integer.toString(cicloActual.getInt(i)));
                    bwSalida.write(",");
                }

                bwSalida.write(Integer.toString(primero)); // para cerrar el ciclo
                bwSalida.newLine();
                //imprimir ciclo
            }catch (IOException e){
                System.out.println("Error al escribir en el archivo...");
                e.printStackTrace();
            }
        }

        try {
            bwSalida.flush();
            bwSalida.close();
        }catch (IOException e){
            System.out.println("Error al cerrar el archivo...");
            e.printStackTrace();
        }
    }

    /**Imprime la tabla de mapeos de integer a nombre de paquete. */
    private void imprimirTablaMapeos(Diccionario d) throws IOException {
        ArrayList<Paquete> paquetes = d.getPaquetes();
        for(int i=0; i<paquetes.size(); i++){
            bwSalida.write(Integer.toString(i));
            bwSalida.write("-");
            bwSalida.write(paquetes.get(i).getNombre());
            bwSalida.newLine();
        }
    }

    public synchronized void agregarCiclo(IntArrayList ciclo){
       ciclos.add(ciclos.size(), ciclo);
       if(ciclos.size() == 1)
           this.notify(); // si hay un nuevo ciclo, notifico para empezar a imprimir
    }

    public synchronized void finalizar(){
        this.seguir = false;
    }

    private synchronized IntArrayList obtenerSiguiente(){
        if(ciclos.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return ciclos.remove(0);
    }
}
