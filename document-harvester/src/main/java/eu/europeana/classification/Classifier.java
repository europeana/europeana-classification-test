package eu.europeana.classification;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.mahout.classifier.naivebayes.ComplementaryNaiveBayesClassifier;
import org.apache.mahout.classifier.naivebayes.NaiveBayesModel;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileIterable;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.TFIDF;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ymamakis on 6/23/16.
 */
public class Classifier {
    public static void main(String[] args) {

        try {

            Configuration conf = new Configuration();
            NaiveBayesModel model = NaiveBayesModel.materialize(new Path("/root/model"),conf);
            ComplementaryNaiveBayesClassifier nb = new ComplementaryNaiveBayesClassifier(model);

            Map<String, Integer> dictionary = readDictionnary(conf, new Path("/root/norm-vectors/dictionary.file-0"));
            Map<Integer, Long> documentFrequency = readDocumentFrequency(conf, new Path("/root/norm-vectors/df-count/part-r-00000"));
            Analyzer analyzer  = new SimpleAnalyzer();
            File[] files = new File("/root/normalized/test").listFiles();
            int i=1;
            Map<Integer,Integer> totals = new HashMap<>();

            File results = new File("results");
            for(File file: files){
                System.out.println("Read " + i+ " files");
                String str = FileUtils.readFileToString(file);
                int category = classify(str,documentFrequency,dictionary,analyzer,nb);
                int k=1;
                if (totals.containsKey(category)) {
                    k= totals.get(category)+1;
                }
                totals.put(category,k);
                FileUtils.write(results, file.getName()+ ":"+category+"\n",true);
                i++;
            }

            for(Map.Entry<Integer,Integer> entry:totals.entrySet()){
                FileUtils.write(new File("totals"),entry.getKey() +" had "+ entry.getValue()+"\n",true);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int classify(String text, Map<Integer, Long> documentFrequency, Map<String, Integer> dictionary,
                        Analyzer analyzer,ComplementaryNaiveBayesClassifier nb) throws IOException {
        int documentCount = documentFrequency.get(-1).intValue();

        Multiset words = ConcurrentHashMultiset.create();

        // extract words from tweet
        TokenStream ts = analyzer.tokenStream("text", new StringReader(text));
        CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
        ts.reset();
        int wordCount = 0;
        while (ts.incrementToken()) {
            if (termAtt.length() > 0) {
                String word = ts.getAttribute(CharTermAttribute.class).toString();
                Integer wordId = dictionary.get(word);
                // if the word is not in the dictionary, skip it
                if (wordId != null) {
                    words.add(word);
                    wordCount++;
                }
            }
        }

        // create vector wordId => weight using tfidf
        Vector vector = new RandomAccessSparseVector(10000);
        TFIDF tfidf = new TFIDF();
        for (Object entry:words.entrySet()) {
            String word = ((Multiset.Entry) entry).getElement().toString();
            int count = ((Multiset.Entry) entry).getCount();
            Integer wordId = dictionary.get(word);
            Long freq = documentFrequency.get(wordId);
            double tfIdfValue = tfidf.calculate(count, freq.intValue(), wordCount, documentCount);
            vector.setQuick(wordId, tfIdfValue);
        }
        // With the classifier, we get one score for each label
        // The label with the highest score is the one the tweet is more likely to
        // be associated to
        Vector resultVector = nb.classifyFull(vector);
        double bestScore = -Double.MAX_VALUE;
        int bestCategoryId = -1;
        for(Vector.Element element: resultVector.all()) {
            int categoryId = element.index();
            double score = element.get();
            if (score > bestScore) {
                bestScore = score;
                bestCategoryId = categoryId;
            }
        }
        ts.close();

        return bestCategoryId;
    }

    private static Map<String, Integer> readDictionnary(Configuration conf, Path dictionaryPath) {
        Map<String, Integer> dictionnary = new HashMap<>();
        for (Pair<Text, IntWritable> pair : new SequenceFileIterable<Text, IntWritable>(dictionaryPath, true, conf)) {
            dictionnary.put(pair.getFirst().toString(), pair.getSecond().get());
        }
        return dictionnary;
    }

    private static Map<Integer, Long> readDocumentFrequency(Configuration conf, Path documentFrequencyPath) {
        Map<Integer, Long> documentFrequency = new HashMap<>();
        for (Pair<IntWritable, LongWritable> pair : new SequenceFileIterable<IntWritable, LongWritable>(documentFrequencyPath, true, conf)) {
            documentFrequency.put(pair.getFirst().get(), pair.getSecond().get());
        }
        return documentFrequency;
    }
}
