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
import java.util.Map;

/**
 * Created by ymamakis on 6/20/16.
 */
public class Harvester {


    public static void main(String[] args) {
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
            for (Map.Entry<String, String> entry : q.queries.entrySet()) {
                if (!new File("dataset" + "/" + entry.getKey()).exists()) {
                    new File("dataset" + "/" + entry.getKey()).mkdir();
                }

                ModifiableSolrParams params = new ModifiableSolrParams();
                params.add("q", entry.getValue());
                String fl = "europeana_id";
                params.add("fl", fl);
                String cursorMark = CursorMarkParams.CURSOR_MARK_START;
                params.add(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
                params.add("rows", "10000");
                params.add("sort", fl + " " + SolrQuery.ORDER.asc);
                int i = 0;
                while (i < 100000) {
                    try {
                        params.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
                        QueryResponse resp = server.query(params);
                        String nextCursorMark = resp.getNextCursorMark();
                        List<SolrDocument> docs = resp.getResults();
                        for (SolrDocument doc : docs) {
                            FullBeanImpl fBean = (FullBeanImpl) mongoServer.getFullBean(doc.get("europeana_id").toString());

                            if (fBean != null) {
                                IOUtils.write(EdmUtils.toEDM(fBean, false), new FileOutputStream("dataset" + "/" + entry.getKey() + "/" + StringUtils.replace(fBean.getId(), "/", "_")));
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
            }
        } catch (IOException |MongoDBException e) {
            e.printStackTrace();
        }

    }
}
