package eu.europeana.preprocessing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * Created by ymamakis on 6/21/16.
 */
public class DictClient {

    private  TelnetClient client = new TelnetClient();
    private final static String TEMPLATE = "DEFINE fd-%s-eng %s\r\n";
    private final static String OK_CODE = "250";
    private final static String INIT_CODE = "220";
    private final static String MATCH_NOT_FOUND = "552";
    private final static String SYNTAX_ERROR = "501";
    private final static String MATCHFOUND ="150";
    private final static String INVALID_DB = "550";


    public String getTranslation(String word, String identifiedLanguage){
        try {
            //Default dict port
            if(!client.isConnected()) {
                client.connect("localhost", 2628);
            }
            PrintStream out = new PrintStream(client.getOutputStream());
            InputStream   in = client.getInputStream();


            String[] words = StringUtils.split(word," ");
            String finalString = "";
            boolean noMatchFound = false;
            for(String oneWord:words) {

                if(!noMatchFound) {
                    read(in, "\r\n");
                }

                out.println(String.format(TEMPLATE, identifiedLanguage, oneWord));
                //System.out.println(String.format(TEMPLATE, identifiedLanguage, oneWord));
                out.flush();

                String s = read(in,"\r\n");
                if(s.startsWith(MATCH_NOT_FOUND) || s.startsWith(SYNTAX_ERROR)||s.startsWith(INVALID_DB)){
                    finalString += oneWord +" ";
                    noMatchFound=true;

                } else if(s.startsWith(MATCHFOUND)) {
                    noMatchFound=false;
                    String t = read(in, ".\r\n");
                    String[] lines = t.split("\n");

                    if(lines.length>2) {
                        if (lines[2].startsWith(("1."))) {
                            finalString += StringUtils.substringBetween(lines[2].trim(), " 1. ", ";").trim() + " ";
                        } else {
                            finalString += StringUtils.substringBefore(lines[2].trim(), ";") + " ";
                        }
                    } else {
                        System.out.println("Match found for: " +oneWord  +"\r\n"+"Response was: "+t);
                        finalString+=oneWord + " ";
                    }

                }


            }
            //if(client.isConnected())
            //client.disconnect();
            return finalString.trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return word;
    }

    private String read(InputStream in, String pattern) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;

        while((c = in.read()) != -1) {
            char lastChar = pattern.charAt(pattern.length() - 1);
            char ch = (char) c;
            //System.out.print(ch);
            sb.append(ch);
            if(ch == lastChar) {
                String str = sb.toString();
                if(str.endsWith(pattern)) {
                    return str.substring(0, str.length() -
                            pattern.length());
                }
            }
        }
        System.out.println(sb.toString());
       return sb.toString();
    }
}
