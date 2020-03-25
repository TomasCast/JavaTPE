package com.company;

import dependencias.Diccionario;
import dependencias.Paquete;
import dependencias.SalidaThread;
import dependencias.UtilidadesOdem;
import grafo.CiclosSimplesJohnson;
import grafo.Grafo;
import grafo.UtilidadesGrafo;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FilenameFilter;
import java.text.NumberFormat;

import static com.company.Main.crearBufferedWriter;

public class InterfazUI extends JFrame {
    private JButton seleccionarArchivoDeDependenciasButton;
    private JRadioButton solucionBonusTrackRadioButton;
    private JButton ejecutarButton;
    private JTextArea Salida;
    private JComboBox comboBoxA;
    private JComboBox comboBoxB;
    private JPanel panel1;
    private JSpinner minCiclosSpinner;
    private JSpinner maxCiclosSpinner;

    private Diccionario diccionario;

    public InterfazUI() {
        super();
        setContentPane(panel1);
        ejecutarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarButton.setEnabled(false);
                CiclosSimplesJohnson c = new CiclosSimplesJohnson();
                c.MIN_CICLOS = (int) minCiclosSpinner.getValue();
                c.MAX_CICLOS = (int) maxCiclosSpinner.getValue();
                if(solucionBonusTrackRadioButton.isSelected()){
                    Salida.append("Buscando ciclos que contengan los nodos A y B especificados:\n");
                    Salida.append("Generando grafo de dependencias...\n");
                    Grafo dependencias = UtilidadesGrafo.construirGrafo(diccionario);
                    String nodoA =(String)comboBoxA.getSelectedItem();
                    String nodoB =(String)comboBoxB.getSelectedItem();
                    Salida.append("Buscando...\n");
                    if(c.hayCiclo(dependencias, diccionario.getNumero(nodoA), diccionario.getNumero(nodoB)))
                        Salida.append("Hay ciclo simple que contiene a "+nodoA+" y a "+nodoB);
                    else
                        Salida.append("No hay ciclo simple que contiene a "+nodoA+" y a "+nodoB);

                }else{ //correr el johnsons normal
                    Salida.append("Dependencias de tamano"+c.MIN_CICLOS+" a "+c.MAX_CICLOS+"\n");
                    Salida.append("Generando grafo de dependencias...\n");
                    Grafo dependencias = UtilidadesGrafo.construirGrafo(diccionario);
                    SalidaThread generadorSalida = new SalidaThread(crearBufferedWriter("ciclos.txt"), diccionario);
                    Thread t = new Thread(generadorSalida);
                    t.start();
                    Salida.append("Buscando ciclos...\n");
                    c.correrJohnson(dependencias, generadorSalida);
                    Salida.append("Cantidad de ciclos encontrados: "+c.suma+"\n");
                    for (int i = 0; i < c.getCantidadCiclosTamano().length; i++) {
                        Salida.append("Ciclos de tam "+(i+c.MIN_CICLOS)+": "+c.getCantidadCiclosTamano()[i]+"\n");
                    }
                    generadorSalida.finalizar();

                }
                ejecutarButton.setEnabled(true);
            }
        });

        ejecutarButton.setEnabled(false);

        seleccionarArchivoDeDependenciasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Seleccionar Archivo ODEM");
                jfc.setAcceptAllFileFilterUsed(true);
                FileNameExtensionFilter filtro = new FileNameExtensionFilter("extension .odem", "odem");
                jfc.addChoosableFileFilter(filtro);
                int ret = jfc.showOpenDialog(InterfazUI.this);
                if(ret == JFileChooser.APPROVE_OPTION){
                    diccionario = UtilidadesOdem.getPaquetes(jfc.getSelectedFile());
                    Salida.setText(diccionario.mostrarPaquetes().toString());
                    for (Paquete paquete:diccionario.getPaquetes()) {
                        comboBoxB.addItem(paquete.getNombre());
                        comboBoxA.addItem(paquete.getNombre());
                    }
                    ejecutarButton.setEnabled(true);
                }
            }
        });

        minCiclosSpinner.setModel(new SpinnerNumberModel(3,2,100,1));
        maxCiclosSpinner.setModel(new SpinnerNumberModel(9,2,100,1));
    }
}
