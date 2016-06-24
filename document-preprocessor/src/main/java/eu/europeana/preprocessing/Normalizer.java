package eu.europeana.preprocessing;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


/**
 * Created by ymamakis on 6/21/16.
 */
public class Normalizer implements Runnable {

    private String folder;
    private LanguageDetector detector = new LanguageDetector();
    private DictClient client = new DictClient();
    public void setFolder(String folder){
        this.folder = folder;
    }

    public void normalize(){

        if (!new File("normalized").exists()) {
            new File("normalized").mkdir();
        }
       // for (String folder : folders) {
            for (File file : new File("dataset/" + folder).listFiles()) {
                try {
                    System.out.println("Reading file: "+ file.getName());
                    String s = "";
                    DocumentBuilder b = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document doc = b.parse(file);
                    Element element = doc.getDocumentElement();
                    NodeList proxies = element.getElementsByTagName("ore:Proxy");
                    Node proxy = proxies.item(0);
                    NodeList titles = ((Element) proxy).getElementsByTagName("dc:title");
                    if (titles.getLength() > 0) {
                        String titleString = "";
                        String titleStringNoLang = "";
                        for (int i = 0; i < titles.getLength(); i++) {
                            Element title = (Element) titles.item(i);
                            if (title.hasAttribute("xml:lang") && StringUtils.equals(title.getAttribute("xml:lang"), "en")) {
                                titleString += title.getFirstChild().getNodeValue() + "\n";
                            }
                            if (!title.hasAttribute("xml:lang")&& title.hasChildNodes()) {
                                String lang = detector.identifyLanguage(title.getFirstChild().getNodeValue());
                                if (lang != null) {
                                    titleStringNoLang += client.getTranslation(title.getFirstChild().getNodeValue(), lang) + "\n";
                                }
                            }
                        }

                        if (StringUtils.isEmpty(titleString) && StringUtils.isEmpty(titleStringNoLang)) {
                            String lang = detector.identifyLanguage(titles.item(0).getFirstChild().getNodeValue());
                            if (lang != null) {
                                titleStringNoLang += client.getTranslation(titles.item(0).getFirstChild().getNodeValue(), lang) + "\n";
                            } else {
                                titleStringNoLang += titles.item(0).getFirstChild().getNodeValue() + "\n";
                            }
                        }

                        s += titleString;
                        s += titleStringNoLang;
                    }

                    NodeList descriptions = ((Element) proxy).getElementsByTagName("dc:description");
                    if (descriptions.getLength() > 0) {
                        String descriptionString = "";
                        String descriptionStringNoLang = "";
                        for (int i = 0; i < descriptions.getLength(); i++) {
                            Element description = (Element) descriptions.item(i);
                            if (description.hasAttribute("xml:lang") && StringUtils.equals(description.getAttribute("xml:lang"), "en")) {
                                descriptionString += description.getFirstChild().getNodeValue() + "\n";
                            }
                            if (!description.hasAttribute("xml:lang")&& description.hasChildNodes()) {
                                String lang = detector.identifyLanguage(description.getFirstChild().getNodeValue());
                                if (lang != null) {
                                    descriptionString += client.getTranslation(description.getFirstChild().getNodeValue(), lang) + "\n";
                                }
                            }
                        }

                        if (StringUtils.isEmpty(descriptionString) && StringUtils.isEmpty(descriptionStringNoLang)&& descriptions.item(0).hasChildNodes()) {
                            String lang = detector.identifyLanguage(descriptions.item(0).getFirstChild().getNodeValue());
                            if (lang != null) {
                                descriptionStringNoLang += client.getTranslation(descriptions.item(0).getFirstChild().getNodeValue(), lang) + "\n";
                            } else {
                                descriptionStringNoLang += descriptions.item(0).getFirstChild().getNodeValue() + "\n";
                            }
                        }

                        s += descriptionString;
                        s += descriptionStringNoLang;
                    }

                    NodeList subjects = ((Element) proxy).getElementsByTagName("dc:subject");
                    if (subjects.getLength() > 0) {
                        String subjectString = "";
                        String subjectStringNoLang = "";
                        for (int i = 0; i < subjects.getLength(); i++) {
                            Element subject = (Element) subjects.item(i);
                            if (subject.hasAttribute("xml:lang") && StringUtils.equals(subject.getAttribute("xml:lang"), "en")&& subject.hasChildNodes()) {
                                subjectString += subject.getFirstChild().getNodeValue() + "\n";
                            }
                            if (!subject.hasAttribute("xml:lang")&& subject.hasChildNodes()) {
                                String lang = detector.identifyLanguage(subject.getFirstChild().getNodeValue());
                                if (lang != null) {
                                    subjectString += client.getTranslation(subject.getFirstChild().getNodeValue(), lang) + "\n";
                                }
                            }
                        }

                        if (StringUtils.isEmpty(subjectString) && StringUtils.isEmpty(subjectStringNoLang) && subjects.item(0).hasChildNodes()) {
                            String lang = detector.identifyLanguage(subjects.item(0).getFirstChild().getNodeValue());
                            if (lang != null) {
                                subjectStringNoLang += client.getTranslation(subjects.item(0).getFirstChild().getNodeValue(), lang) + "\n";
                            } else {
                                subjectStringNoLang += subjects.item(0).getFirstChild().getNodeValue() + "\n";
                            }
                        }

                        s += subjectString;
                        s += subjectStringNoLang;
                    }

                    String prefLabelAgent = "";
                    NodeList agents = element.getElementsByTagName("edm:Agent");
                    if (agents.getLength() > 0) {
                        for (int i = 0; i < agents.getLength(); i++) {
                            Element agent = (Element) agents.item(i);
                            NodeList prefs = agent.getElementsByTagName("skos:prefLabel");
                            for (int k = 0; k < prefs.getLength(); k++) {
                                Element pref = (Element) prefs.item(k);
                                if (pref.hasAttribute("xml:lang") && StringUtils.equals(pref.getAttribute("xml:lang"), "en")) {
                                    prefLabelAgent += pref.getFirstChild().getNodeValue() + "\n";
                                }
                            }
                        }
                    }
                    s += prefLabelAgent;

                    String prefLabelConcept = "";
                    NodeList concepts = element.getElementsByTagName("skos:Concept");
                    if (concepts.getLength() > 0) {
                        for (int i = 0; i < concepts.getLength(); i++) {
                            Element concept = (Element) concepts.item(i);
                            NodeList prefs = concept.getElementsByTagName("skos:prefLabel");
                            for (int k = 0; k < prefs.getLength(); k++) {
                                Element pref = (Element) prefs.item(k);
                                if (pref.hasAttribute("xml:lang") && StringUtils.equals(pref.getAttribute("xml:lang"), "en")) {
                                    prefLabelConcept += pref.getFirstChild().getNodeValue() + "\n";
                                }
                            }
                        }
                    }
                    s += prefLabelConcept;
                    if (!new File("normalized/" + folder).exists()) {
                        new File("normalized/" + folder).mkdir();
                    }

                    FileUtils.write(new File("normalized/" + folder + "/" + file.getName()), s);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
      //  }
    }




    @Override
    public void run() {
        normalize();
    }
}