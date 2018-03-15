/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import com.mai.textanalyzer.indexing.doc2vec.Doc2VecUtils;
import java.io.File;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;

/**
 *
 * @author Sergey
 */
public class TestDoc2VecUtils {

    private static final File saveModel = new File("G:\\DocForTest\\SaveModel\\Doc2Vec");

    public static void main(String[] args) {
        File file = new File("G:\\DocForTest\\DataForLearning");
//        ParagraphVectors pv = Doc2VecUtils.createModel(file);
//        Doc2VecUtils.saveModel(pv, saveModel);

        ParagraphVectors loadPv = Doc2VecUtils.loadModel(saveModel);
        System.out.println("loadPv != null " + loadPv != null);

    }

}
