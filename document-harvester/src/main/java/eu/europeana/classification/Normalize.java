package eu.europeana.classification;

import eu.europeana.preprocessing.NormalizerThreads;

/**
 * Created by ymamakis on 6/21/16.
 */
public class Normalize {
    public static void main(String[] args){
        NormalizerThreads normalizer = new NormalizerThreads();
        normalizer.normalize();
    }
}
