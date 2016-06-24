package eu.europeana.classification;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by ymamakis on 6/24/16.
 */
public class EmptyDocumentsIdentifier {

    public static void main(String[] args){
        File[] files = new File("/root/normalized/test").listFiles();
        long empty=0;
        long nonempty=0;
        long total = files.length;
        for (File f : files){
            try {
                String s = FileUtils.readFileToString(f);
                if(StringUtils.isBlank(s.replaceAll("\r","").replaceAll("\n","").trim())){
                    empty+=1;
                } else {
                    nonempty+=1;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Empty were: " + empty);
        System.out.println("Non-Empty were: " + nonempty);
        System.out.println("Empty %: " +(empty*100/total));
    }
}
