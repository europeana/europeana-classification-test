package eu.europeana.classification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Mongo;
import eu.europeana.corelib.edm.exceptions.MongoDBException;
import eu.europeana.corelib.edm.utils.EdmUtils;
import eu.europeana.corelib.mongo.server.EdmMongoServer;
import eu.europeana.corelib.mongo.server.impl.EdmMongoServerImpl;
import eu.europeana.corelib.solr.bean.impl.FullBeanImpl;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.CursorMarkParams;
import org.apache.solr.common.params.ModifiableSolrParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by ymamakis on 6/20/16.
 */
public class HarvesterTesting {


    public static void main(String[] args) throws IOException, MongoDBException, SolrServerException {
        SolrServer server = new HttpSolrServer("http://sol8.eanadev.org:9191/solr/search");
        Mongo mongo = null;
        try {
            mongo = new Mongo("reindexing1.eanadev.org", 27017);

            EdmMongoServer mongoServer = null;

            mongoServer = new EdmMongoServerImpl(mongo, "europeana", null, null);

            Queries q = new Queries();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            if (!new File("dataset").exists()) {
                new File("dataset").mkdir();
            }

            ModifiableSolrParams params = new ModifiableSolrParams();
            params.add("q", "text");
            String fl = "europeana_id";
            params.add("fl", fl);
            String cursorMark = CursorMarkParams.CURSOR_MARK_START;
            params.add(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
            params.add("rows", "10000");
            params.add("sort", fl + " " + SolrQuery.ORDER.desc);
            int i = 0;
            while (i < 1000000) {
                try {
                    params.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
                    System.out.println(params.toString());
                    QueryResponse resp = server.query(params);
                    System.out.println(resp.getResponse());
                    String nextCursorMark = resp.getNextCursorMark();
                    List<SolrDocument> docs = resp.getResults();

                    for (SolrDocument doc : docs) {
                        FullBeanImpl fBean = (FullBeanImpl) mongoServer.getFullBean(doc.get("europeana_id").toString());

                        if (fBean != null) {
                            IOUtils.write(EdmUtils.toEDM(fBean, false), new FileOutputStream("dataset" + "/test/" + StringUtils.replace(fBean.getId(), "/", "_")));
                        }
                    }
                    i += 10000;
                    if (cursorMark.equals(nextCursorMark)) {
                        break;
                    }
                    cursorMark = nextCursorMark;

                } catch (MongoDBException | SolrServerException e) {
                    e.printStackTrace();
                }

                System.out.println("Read " + i + " documents");
            }

        } catch (IOException | MongoDBException e) {
            e.printStackTrace();
        }


    }
}
