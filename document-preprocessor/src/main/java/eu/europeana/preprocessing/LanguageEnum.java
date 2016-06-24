package eu.europeana.preprocessing;

import org.apache.commons.lang.StringUtils;

/**
 * Created by ymamakis on 6/21/16.
 */
public enum LanguageEnum {


    LT("lt","lit"),SV("sv","swe"),AR("ar","ara"),SR("sr","srp"),ES("es","spa"),CS("cs","ces"),SK("sk","slk"),
    HR("hr","hrv"),PT("pt","por"),IS("is","isl"),SW("sw","swa"),HU("hu","hun"),DE("de","deu"),NL("nl","nld"),
    GA("ga","gle"),AF("af","afr"),FR("fr","fra"),CY("cy","cym"),TR("tr", "tur"),IT("it","ita"),DA("da","dan");

    private String twoCode;
    private String threeCode;
    private LanguageEnum(String twoCode,String threeCode){
        this.twoCode = twoCode;
        this.threeCode = threeCode;
    }

    public static String getByTwoCode(String twoCode){
        for(LanguageEnum len : values()){
            if(StringUtils.equals(len.twoCode,twoCode)){
                return len.threeCode;
            }
        }
        return null;
    }
}
