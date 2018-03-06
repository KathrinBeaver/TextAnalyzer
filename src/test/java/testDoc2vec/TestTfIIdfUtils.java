/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testDoc2vec;

import com.mai.textanalyzer.indexing.tf_idf.*;
import java.io.File;
import org.deeplearning4j.bagofwords.vectorizer.TfidfVectorizer;
import org.junit.Test;

/**
 *
 * @author Sergey
 */
public class TestTfIIdfUtils {

    @Test
    public void testMethod() {
        File folderWithDataForLearning = new File("D:\\testClassDoc");
        TfidfVectorizer tfidfVectorizer = TfIIdfUtils.createModel(folderWithDataForLearning);
        System.err.println("" + tfidfVectorizer.toString());
//          TfidfRecordReader tfidfRecordReader = new TfidfRecordReader();
    }

}
