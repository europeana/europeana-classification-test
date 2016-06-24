package eu.europeana.classification;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ymamakis on 6/20/16.
 */
public class Queries {

    Map<String, String> queries;

    public Queries() {
        queries = new HashMap<>();

        queries.put("music", "(PROVIDER:\"DISMARC\") OR (PROVIDER: \"MIMO - Musical Instrument Museums Online\") " +
                "OR (DATA_PROVIDER:\"Sächsische Landesbibliothek - Staats- und Universitätsbibliothek Dresden\" " +
                "AND TYPE:SOUND) OR (PROVIDER:\"Europeana Sounds\" AND provider_aggregation_edm_isShownBy:*) OR " +
                "(DATA_PROVIDER:\"Netherlands Institute for Sound and Vision\" AND provider_aggregation_edm_isShownBy:*) " +
                "OR ((what:(music OR música OR musique OR musik OR musica OR muziek OR muzyka OR \"notated music\" " +
                "OR \"traditional and folk music\" OR \"western classical music\" OR \"folk songs\" OR jazz OR \"popular " +
                "music\" OR \"sheet music\" OR score OR \"musical Instrument\" OR partitur OR partituras OR gradual " +
                "OR libretto OR oper OR concerto OR symphony OR sonata OR fugue OR motet OR estampie OR \"gregorian " +
                "chant\" OR saltarello OR organum OR ballade OR chanson OR galliard OR laude OR madrigal OR pavane OR " +
                "ricercar OR tiento OR toccata OR cantata OR chaconne OR gavotte OR gigue OR minuet OR partita OR " +
                "passacaglia OR sarabande OR sinfonia OR lied OR mazurka OR \"music hall\" OR quartet " +
                "OR quintet OR requiem OR rhapsody OR rondo OR scherzo OR \"sinfonia concertante\" OR waltz OR zanger " +
                "OR singer OR chanteur OR chanteuse OR sång OR sångare OR sänger OR cantante OR sopran OR tenor OR " +
                "piosenkarz OR śpiewak OR sängerin OR composer OR komponist OR compositeur OR compositore OR compositor " +
                "OR orchestra OR orchester OR orkester OR orchestre)) AND (provider_aggregation_edm_isShownBy:*)) OR " +
                "(\"gieddes samling\") OR (musik AND DATA_PROVIDER:\"Universitätsbibliothek Heidelberg\") OR " +
                "(title:gradual AND europeana_collectionName: \"2021003_Ag_FI_NDL_fragmenta\") NOT (DATA_PROVIDER:" +
                " \"Progetto ArtPast- CulturaItalia\" OR DATA_PROVIDER:\"Internet Culturale\" OR " +
                "DATA_PROVIDER:\"Accademia Nazionale di Santa Cecilia\" OR DATA_PROVIDER:\"Regione Umbria\" OR " +
                "DATA_PROVIDER:\"Regione Emilia Romagna\" OR DATA_PROVIDER:\"Regione Lombardia\" OR " +
                "DATA_PROVIDER:\"Regione Piemonte\" OR DATA_PROVIDER:\"National Széchényi Library - Hungarian Electronic" +
                " Library\" OR DATA_PROVIDER:\"Rijksdienst voor het Cultureel Erfgoed\" OR " +
                "DATA_PROVIDER:\"Phonogrammarchiv - Österreichische Akademie der Wissenschaften; Austria\" OR " +
                "DATA_PROVIDER:\"Ministère de la culture et de la communication, Musées de France\" OR " +
                "DATA_PROVIDER:\"CER.ES: Red Digital de Colecciones de museos de España\" OR DATA_PROVIDER:\"National " +
                "Széchényi Library - Digital Archive of Pictures\" OR PROVIDER:\"OpenUp!\" OR RIGHTS:*rr-p* OR \"opere " +
                "d'arte visiva\")");
        queries.put("arthistory", "(DATA_PROVIDER:\"Östasiatiska museet\" NOT TYPE:TEXT) OR (DATA_PROVIDER:\"Medelhavsmuseet\") " +
                "OR (DATA_PROVIDER:\"Rijksmuseum\") OR (europeana_collectionName: \"91631_Ag_SE_SwedishNationalHeritage_shm_art\") " +
                "OR (DATA_PROVIDER:\"Bibliothèque municipale de Lyon\") OR (DATA_PROVIDER:\"Museu Nacional d'Art de Catalunya\") " +
                "OR (DATA_PROVIDER:\"Victoria and Albert Museum\") OR (DATA_PROVIDER:\"Slovak national gallery\") OR " +
                "(DATA_PROVIDER:\"Thyssen-Bornemisza Museum\") OR (DATA_PROVIDER:\"Museo Nacional del Prado\") OR " +
                "(DATA_PROVIDER:\"Statens Museum for Kunst\") OR (DATA_PROVIDER:\"Hungarian University of Fine Arts, Budapest\") " +
                "OR (DATA_PROVIDER:\"Hungarian National Museum\") OR (DATA_PROVIDER:\"Museum of Applied Arts, Budapest\") " +
                "OR (DATA_PROVIDER:\"Szépművészeti Múzeum\") OR (DATA_PROVIDER:\"Museum of Fine Arts - Hungarian National Gallery, Budapest\") " +
                "OR (DATA_PROVIDER:\"Schola Graphidis Art Collection. Hungarian University of Fine Arts - High School of Visual Arts, Budapest\") " +
                "OR (PROVIDER:\"Ville de Bourg-en-Bresse\") OR (DATA_PROVIDER:\"Universitätsbibliothek Heidelberg\") OR " +
                "((what:(\"fine art\" OR \"beaux arts\" OR \"bellas artes\" OR \"belle arti\" OR \"schone kunsten\" OR " +
                "konst OR \"bildende kunst\" OR \"Opere d'arte visiva\" OR \"decorative arts\" OR konsthantverk OR \"arts " +
                "décoratifs\" OR paintings OR schilderij OR pintura OR peinture OR dipinto OR malerei OR måleri OR målning " +
                "OR sculpture OR skulptur OR sculptuur OR beeldhouwwerk OR drawing OR poster OR tapestry OR gobelin OR " +
                "jewellery OR miniature OR prints OR träsnitt OR holzschnitt OR woodcut OR lithography OR chiaroscuro " +
                "OR \"old master print\" OR estampe OR porcelain OR mannerism OR rococo OR impressionism OR expressionism " +
                "OR romanticism OR \"Neo-Classicism\" OR \"Pre-Raphaelite\" OR Symbolism OR Surrealism OR Cubism OR \"Art Deco\" " +
                "OR \"Art Déco\" OR Dadaism OR \"De Stijl\" OR \"Pop Art\" OR \"art nouveau\" OR \"art history\" OR \"http://vocab.getty.edu/aat/300041273\" " +
                "OR \"histoire de l'art\" OR kunstgeschichte OR \"estudio de la historia del arte\" OR Kunstgeschiedenis " +
                "OR \"illuminated manuscript\" OR buchmalerei OR enluminure OR \"manuscrito illustrado\" OR \"manoscritto miniato\" " +
                "OR boekverluchting OR kalligrafi OR calligraphy OR exlibris)) AND (provider_aggregation_edm_isShownBy:*)) " +
                "NOT (what: \"printed serial\" OR what:\"printedbook\" OR \"printing paper\" OR \"printed music\" OR " +
                "DATA_PROVIDER:\"NALIS Foundation\" OR DATA_PROVIDER:\"Ministère de la culture et de la communication, " +
                "Musées de France\" OR DATA_PROVIDER:\"CER.ES: Red Digital de Colecciones de museos de España\" OR PROVIDER:\"OpenUp!\" " +
                "OR PROVIDER:\"BHL Europe\" OR PROVIDER:\"EFG - The European Film Gateway\" OR DATA_PROVIDER: \"Malta Aviation Museum Foundation\" " +
                "OR DATA_PROVIDER:\"National Széchényi Library - Digital Archive of Pictures\" OR PROVIDER:\"Swiss National Library\")");
        queries.put("fashion", "(PROVIDER: \"Europeana Fashion\") OR (what: Fashion) OR (what: mode) OR (what: moda) OR " +
                "(what: costume) OR (what: clothes) OR (what: shoes) OR (what: jewellery)");
        queries.put("naturalhistory", "(what:(\"natural history\" OR \"histoire naturelle\" OR naturgeschichte " +
                "OR \"historia naturalna\" OR \"historia natural\" OR biolog OR geology OR zoolog OR entomolog OR " +
                "ornitholog OR mycolog OR specimen OR fossil OR animal OR plants OR flower OR palaeontolog OR paleontolog " +
                "OR flora OR fauna OR evolution OR systematics OR systematik OR insect OR insekt OR herbarium) OR " +
                "((title:fauna AND TYPE:TEXT) OR (title:flora AND TYPE:TEXT) OR (linnaeus) OR (Carl von Linné) OR " +
                "(Gaius Plinius Secundus) OR (Leonhart Fuchs) OR (Otto Brunfels) OR (Hieronymus Bock) OR (Valerius Cordus) " +
                "OR (Konrad Gesner) OR (Frederik Ruysch) OR (Gaspard Bauhin) OR (Henry Walter Bates) OR (Charles Darwin) OR " +
                "(Alfred Russel Wallace) OR (Georges Buffon) OR (Jean-Baptiste de Lamarck) OR (Maria Sibylla Merian) OR " +
                "(de materia media) OR (historiae animalium) OR (systema naturae) OR (naturalis historia) OR (botanica) " +
                "OR (plantarum) OR (bestiarium) OR (Naturstudien) OR (Pflanzenstudien)) AND (provider_aggregation_edm_isShownBy:*)) " +
                "OR (PROVIDER:\"OpenUp!\") OR (PROVIDER:\"STERNA\") OR (PROVIDER:\"The Natural Europe Project\") " +
                "NOT (DATA_PROVIDER:\"askaboutireland.ie\")");
        queries.put("maps","what:maps OR what:cartography OR what:kartografi OR what:cartographic OR what:geography OR " +
                "what:geografi OR what:navigation OR what:chart OR what: portolan OR what: \"mappa mundi\" OR " +
                "what:cosmography OR what:kosmografi OR what:\"astronomical instrument\" OR what:\"celestial globe\" OR " +
                "title:cosmographia OR title:geographia OR title:geographica OR what:\"aerial photograph\" OR what:periplus " +
                "OR what:atlas OR what:\"armillary sphere\" OR what:\"terrestrial globe\" OR what:\"jordglob\" OR what:globus " +
                "NOT (PROVIDER:\"OpenUp!\")");
        queries.put("film","PROVIDER:\"EFG - The European Film Gateway\"");
        queries.put("religion","edm_datasetName:92039* OR Christ OR Jesus OR Jezus OR Mohammed");
        queries.put("photography","PROVIDER:EuropeanaPhotography");

    }

}
