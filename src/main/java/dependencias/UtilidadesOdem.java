package dependencias;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

public class UtilidadesOdem {
    /*Clase que posee utilidades para leer y procesar archivos con extension "odem".*/

    /**@param ruta Ubicacion del archivo odem.
     * @return document resultante de parsear el archivo*/
    public static Document procesarArchivo(String ruta){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dBuilder = factory.newDocumentBuilder(); //creo un document  builder
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }

        File inputFile = new File(ruta);
        Document document = null;
        try {
            document = dBuilder.parse(inputFile); // creo el documento a partir del archivo
        }catch (Exception e){
            e.printStackTrace();
        }
        return document;
    }

    /**Levanta los paquetes de un archivo odem junto con sus clases y dependencias, y los coloca en un Diccionario.*/
    public static Diccionario getPaquetes(String ruta){
        Diccionario salida = new Diccionario();
        Document document = procesarArchivo(ruta);
        Element root = document.getDocumentElement();
        Node container= ((Element) root.getElementsByTagName("context").item(0)).getElementsByTagName("container").item(0);

        NodeList paquetes= ((Element)container).getElementsByTagName("namespace");
        for(int i=0; i<paquetes.getLength(); i++){
            Node nodo_paquete = paquetes.item(i);
            if(nodo_paquete.getNodeType() == Node.ELEMENT_NODE){
                Element paquete = (Element) nodo_paquete;
                Paquete p = new Paquete(paquete.getAttribute("name"));
                NodeList clases = paquete.getElementsByTagName("type");

                for(int j=0; j<clases.getLength(); j++){
                    Node nodo_clase = clases.item(j);
                    if(nodo_clase.getNodeType() == Node.ELEMENT_NODE){
                        Element clase = (Element) nodo_clase;
                        p.addClase(clase.getAttribute("name"));
                        NodeList dependencias = clase.getElementsByTagName("depends-on");

                        for(int k=0; k<dependencias.getLength(); k++){
                            Node nodo_dependencia = dependencias.item(k);
                            if(nodo_dependencia.getNodeType() == Node.ELEMENT_NODE && !((Element) nodo_dependencia).getAttribute("name").startsWith("java")){
                                Element dependencia = (Element) nodo_dependencia;
                                p.addDependecia(dependencia.getAttribute("name"));
                            }
                        }
                    }
                }
                salida.addPaq(p);
            }
        }
        return salida;
    }

}
