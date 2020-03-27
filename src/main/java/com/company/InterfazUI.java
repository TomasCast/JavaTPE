package com.company;

import dependencias.Diccionario;
import dependencias.Paquete;
import dependencias.SalidaThread;
import dependencias.UtilidadesOdem;
import grafo.CiclosSimplesJohnson;
import grafo.Grafo;
import grafo.UtilidadesGrafo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class InterfazUI extends JFrame {
    private JButton seleccionarArchivoDeDependenciasButton;
    private JRadioButton solucionBonusTrackRadioButton;
    private JButton ejecutarButton;
    private JTextArea salida;
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
                if(((int)minCiclosSpinner.getValue()) <= ((int)maxCiclosSpinner.getValue())) {
                    CiclosSimplesJohnson.MIN_CICLOS = (int) minCiclosSpinner.getValue();
                    CiclosSimplesJohnson.MAX_CICLOS = (int) maxCiclosSpinner.getValue();

                    if (solucionBonusTrackRadioButton.isSelected()) {
                        salida.append("Buscando ciclos que contengan los nodos A y B especificados:\n");
                        salida.append("Generando grafo de dependencias...\n");
                        Grafo dependencias = UtilidadesGrafo.construirGrafo(diccionario);
                        String nodoA = (String) comboBoxA.getSelectedItem();
                        String nodoB = (String) comboBoxB.getSelectedItem();
                        salida.append("Buscando...\n");
                        if (c.hayCiclo(dependencias, diccionario.getNumero(nodoA), diccionario.getNumero(nodoB)))
                            salida.append("Hay ciclo simple que contiene a " + nodoA + " y a " + nodoB);
                        else
                            salida.append("No hay ciclo simple que contiene a " + nodoA + " y a " + nodoB);

                    } else { //correr el johnsons normal
                        salida.append("Dependencias de tamano " + CiclosSimplesJohnson.MIN_CICLOS + " a " + CiclosSimplesJohnson.MAX_CICLOS + "\n");
                        salida.append("Generando grafo de dependencias...\n");
                        Grafo dependencias = UtilidadesGrafo.construirGrafo(diccionario);
                        SalidaThread generadorSalida = new SalidaThread("ciclos.txt", diccionario);
                        Thread t = new Thread(generadorSalida);
                        t.start();
                        salida.append("Buscando ciclos...\n");
                        c.correrJohnson(dependencias, generadorSalida);
                        salida.append("Cantidad de ciclos encontrados: " + c.suma + "\n");
                        for (int i = 0; i < c.getCantidadCiclosTamano().length; i++) {
                            salida.append("Ciclos de tam " + (i + CiclosSimplesJohnson.MIN_CICLOS) + ": " + c.getCantidadCiclosTamano()[i] + "\n");
                        }
                        generadorSalida.finalizar();

                    }
                    ejecutarButton.setEnabled(true);
                }else{
                    salida.append("Min Ciclos ("+minCiclosSpinner.getValue()+") es mayor a Max ciclos("+maxCiclosSpinner.getValue()+")\n");
                }
            }
        });

        ejecutarButton.setEnabled(false);

        seleccionarArchivoDeDependenciasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File dirProyecto = new File(System.getProperty("user.dir"));
                JFileChooser jfc = new JFileChooser();
                jfc.setCurrentDirectory(dirProyecto);
                jfc.setDialogTitle("Seleccionar Archivo ODEM");
                jfc.setAcceptAllFileFilterUsed(true);
                FileNameExtensionFilter filtro = new FileNameExtensionFilter("extension .odem", "odem");
                jfc.addChoosableFileFilter(filtro);
                int ret = jfc.showOpenDialog(InterfazUI.this);
                if(ret == JFileChooser.APPROVE_OPTION){
                    diccionario = UtilidadesOdem.getPaquetes(jfc.getSelectedFile());
                    salida.setText(diccionario.mostrarPaquetes().toString());
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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch (Exception e){
            e.printStackTrace();
        }
        JFrame frame = new InterfazUI();
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
