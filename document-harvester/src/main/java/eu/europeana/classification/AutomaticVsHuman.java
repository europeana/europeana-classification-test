package eu.europeana.classification;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by ymamakis on 6/24/16.
 */
public class AutomaticVsHuman {

    public static void main (String[] args){
        try {
            List<String> links = IOUtils.readLines(new FileInputStream("/root/links"));
            List<String> correlation = IOUtils.readLines(new FileInputStream("/root/evaluation"));
            List<String> results = IOUtils.readLines(new FileInputStream("/root/results"));
            Map<String,String> correlationMap = splitMap(correlation,"!!!!!","inverse");
            Map<String,String> resultsMap = splitMap(results,":","normal");
            Map<String,String> urlResults = new TreeMap<>(Comparator.<String>naturalOrder());
            for(String link:links){
                if(StringUtils.isNotBlank(link)){
                    String objId =correlationMap.get(link);
                    String res = resultsMap.get(objId);
                    urlResults.put(link,res);
                }
            }

            for(Map.Entry<String,String> entry:urlResults.entrySet()){
                FileUtils.write(new File("correlationResults"),entry.getKey()+":"+entry.getValue()+"\n",true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Map<String,String> splitMap(List<String> correlation,String separator,String order) {
        Map<String,String> map = new HashMap<>();
        for(String s: correlation){
            String[] sSplit= s.split(separator);
            if(StringUtils.equals(order,"inverse")){
                map.put(sSplit[1],sSplit[0]);
            } else {
                map.put(sSplit[0],sSplit[1]);
            }

        }
        return map;
    }
}
