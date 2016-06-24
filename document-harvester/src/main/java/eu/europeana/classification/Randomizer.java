package eu.europeana.classification;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymamakis on 6/23/16.
 */
public class Randomizer {

    public static void main(String[] args){
        try {
            List<String> records = IOUtils.readLines(new FileInputStream("results"));
            int i=0;
            List<String> evaluation = new ArrayList<>();
            while (i<200000){
                records.get(i);

                String[] objId = records.get(i).split(":");
                File f = new File("/root/dataset/test/"+objId[0]);
                DocumentBuilder b = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = b.parse(f);
                Element element = doc.getDocumentElement();
                NodeList cho = element.getElementsByTagName("edm:ProvidedCHO");
                Element elem = (Element)cho.item(0);
                String id = elem.getAttribute("rdf:about");
                evaluation.add(objId[0]+"!!!!!"+"http://www.europeana.eu/portal/record"+ StringUtils.replace(id,"http://data.europeana.eu/item","")+".html");
                i+=200;
            }

            IOUtils.writeLines(evaluation,"\n",new FileOutputStream("evaluation"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
