package eu.europeana.preprocessing;

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by ymamakis on 6/21/16.
 */
public class LanguageDetector {

    private  com.optimaize.langdetect.LanguageDetector detector;


    private  com.optimaize.langdetect.LanguageDetector getDetector() {
        if (detector == null) {
            List<LanguageProfile> languageProfiles = null;
            try {
                languageProfiles = new LanguageProfileReader().readAllBuiltIn();
            } catch (IOException e) {
                e.printStackTrace();
            }

            detector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                    .withProfiles(languageProfiles)
                    .build();
        }
        return detector;
    }


    public String identifyLanguage(String text) {


        TextObjectFactory textObjectFactory;
        if (StringUtils.contains(text, " ")) {
            textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
        } else {
            textObjectFactory = CommonTextObjectFactories.forDetectingShortCleanText();
        }

        if (text != null) {
            TextObject textObject = textObjectFactory.forText(text);
            Optional<LdLocale> lang = getDetector().detect(textObject);
            if (lang.equals(Optional.absent())) {
                return null;
            }
            return LanguageEnum.getByTwoCode(lang.get().getLanguage());
        }
        return null;
    }
}
