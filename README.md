JAENTokenizer
=============

Tiny wrapper to integrate Japanese morphological analyzer and English POS tagger.

## Usage
	import java.io.*;
	import jp.xii.fn.jaen.JAENToken;
	import jp.xii.fn.jaen.JAENTokenizer;
	import org.atilika.kuromoji.Tokenizer;
	import edu.stanford.nlp.tagger.maxent.MaxentTagger;
	public class TestJAENMorphol {
		public static void main(String[] args) throws ClassNotFoundException, IOException {
			MaxentTagger tagger = new MaxentTagger("models/wsj-0-18-left3words.tagger"); // Stanford POS Tagger 
			Tokenizer tokenizer = Tokenizer.builder().build();                           // Kuromoji Tokenizer
			JAENTokenizer jaen = new JAENTokenizer(tokenizer, tagger);
			// BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader reader = new BufferedReader(new StringReader("この件につきご検討いただければ幸いです。Your kind consideration of this matter would be sincerely appreciated."));
			String line = null;
			while (null != (line = reader.readLine())) {
				for (JAENToken token : jaen.tokenize(line)) { // JAENToken is a wrapper for Kuromoji's Token
					System.out.println(token.getSurfaceForm() + "\t" + token.getPartOfSpeech());
				}
			}
		}
	}


	この	連体詞,*,*,*
	件	名詞,一般,*,*
	につき	助詞,格助詞,連語,*
	ご	接頭詞,名詞接続,*,*
	検討	名詞,サ変接続,*,*
	いただけれ	動詞,自立,*,*
	ば	助詞,接続助詞,*,*
	幸い	名詞,形容動詞語幹,*,*
	です	助動詞,*,*,*
	。	記号,句点,*,*
	Your	PRP$
	kind	NN
	consideration	NN
	of	IN
	this	DT
	matter	NN
	would	MD
	be	VB
	sincerely	RB
	appreciated	VBN
	.	.


## Link
* Kuromoji  
<http://www.atilika.org/>  
<https://github.com/atilika/kuromoji>

* Stanford Log-linear Part-Of-Speech Tagger  
<http://nlp.stanford.edu/software/tagger.shtml>

## Author
Susumu OTA
