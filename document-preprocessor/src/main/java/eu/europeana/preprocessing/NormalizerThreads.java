package eu.europeana.preprocessing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymamakis on 6/22/16.
 */
public class NormalizerThreads {

    public  void normalize(){
        List<String> folders = new ArrayList<>();

        //folders.add("arthistory");
        //folders.add("fashion");
        //folders.add("maps");
        //folders.add("music");
        //folders.add("naturalhistory");
        //folders.add("photography");
        //folders.add("religion");
        folders.add("test");

        for(String folder: folders){
            Normalizer normalizer = new Normalizer();
            normalizer.setFolder(folder);
            Thread thread = new Thread(normalizer);
            System.out.println("started thread for "+ folder);
            thread.start();
        }
    }
}
