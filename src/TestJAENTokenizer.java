import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import jp.xii.fn.jaen.JAENToken;
import jp.xii.fn.jaen.JAENTokenizer;

import org.atilika.kuromoji.Tokenizer;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class TestJAENTokenizer {
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		MaxentTagger tagger = new MaxentTagger(0 < args.length ? args[0] : "models/wsj-0-18-left3words.tagger"); // "models/wsj-0-18-bidirectional-distsim.tagger"
		Tokenizer tokenizer = Tokenizer.builder().build();
		JAENTokenizer jaen = new JAENTokenizer(tokenizer, tagger);
		// BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader reader = new BufferedReader(new StringReader("この件につきご検討いただければ幸いです。Your kind consideration of this matter would be sincerely appreciated."));
		String line = null;
		while (null != (line = reader.readLine())) {
			for (JAENToken token : jaen.tokenize(line)) {
				System.out.println(token.getSurfaceForm() + "\t" + token.getPartOfSpeech());
			}
		}
	}
}
