package org.shirdrn.document.processor.component.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.shirdrn.document.processor.common.AbstractComponent;
import org.shirdrn.document.processor.common.Context;
import org.shirdrn.document.processor.common.Term;

/**
 * Load term and label vector files generated by TRAIN data component chain
 * organized in {@link TrainDocumentProcessorDriver} to process TEST data for
 * fitting libSVM.
 *  
 * @author Shirdrn
 */
public class LoadFeatureTermVector extends AbstractComponent {

	private static final Log LOG = LogFactory.getLog(LoadFeatureTermVector.class);
	
	public LoadFeatureTermVector(Context context) {
		super(context);
	}
	
	@Override
	public void fire() {
		// load CHI term vector
		loadChiTermVector();
	}
	
	private void loadChiTermVector() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(
					context.getFDMetadata().getChiTermVectorFile()), charSet));
			String line = null;
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(!line.isEmpty()) {
					String[] aWord = line.split("\\s+");//一个以上的空格为分隔符
					if(aWord.length == 2) {
						String word = aWord[0];
						int wordId = Integer.parseInt(aWord[1]);
						Term term = new Term(word);
						term.setId(wordId);
						LOG.info("Load CHI term: word=" + word + ", wordId=" + wordId);
						context.getVectorMetadata().addChiMergedTerm(word, term);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}

}
