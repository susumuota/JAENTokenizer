/**
 * Copyright 2012 Susumu OTA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.xii.fn.jaen;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Tiny wrapper to integrate Japanese morphological analyzer and English POS tagger.
 * 
 * @author Susumu OTA
 */
public class JAENTokenizer {
	private Tokenizer tokenizer;
	private MaxentTagger tagger;

	/**
	 * Tiny wrapper to integrate Japanese morphological analyzer and English POS tagger.
	 * @param tokenizer Kuromoji Tokenizer, a Japanese morphological analyzer
	 * @param tagger Stanford POS Tagger, a English POS tagger
	 */
	public JAENTokenizer(Tokenizer tokenizer, MaxentTagger tagger) {
		this.tokenizer = tokenizer;
		this.tagger = tagger;
	}

	/**
	 * tokenize Japanese string
	 * @param str
	 * @return token list
	 */
	public List<JAENToken> tokenizeJA(String str) {
		List<JAENToken> results = new ArrayList<JAENToken>();
		for (Token token : tokenizer.tokenize(str)) {
			results.add(new JAENToken(token));
		}
		return results;
	}

	/**
	 * tokenize English string
	 * @param str
	 * @return token list
	 */
	public List<JAENToken> tokenizeEN(String str) {
		List<JAENToken> results = new ArrayList<JAENToken>();
		for (List<HasWord> sentence : MaxentTagger.tokenizeText(new StringReader(str))) {
			for (TaggedWord tw : tagger.tagSentence(sentence)) {
				results.add(new JAENToken(tw.word(), tw.tag()));
			}
		}
		return results;
	}

	/**
	 * tokenize Japanese/English string
	 * @param str
	 * @return token list
	 */
	public List<JAENToken> tokenize(String str) {
		if (null == tagger) return tokenizeJA(str);
		if (null == tokenizer) return tokenizeEN(str);
		List<JAENToken> results = new ArrayList<JAENToken>();
		List<Token> asciibuf = new ArrayList<Token>();
		List<Token> tokens = tokenizer.tokenize(str); // at first, tokenize as japanese text
		for (Token token : tokens) {
			if (token.isUnknown() && isAsciiPrintable(token.getSurfaceForm())) { // english token
				asciibuf.add(token);
			} else if (0 < asciibuf.size()) { // not english token => japanese token
				String surfaces = getSurfaceForms(asciibuf);
				if (isAlphabet(surfaces)) { // english sentence
					results.addAll(tokenizeEN(surfaces));
				} else { // ascii without alphabet == number or symbol => japanese
					for (Token ascii : asciibuf) {
						results.add(new JAENToken(ascii));
					}
				}
				asciibuf.clear();
				results.add(new JAENToken(token)); // current token is japanese
			} else { // japanese token
				results.add(new JAENToken(token));
			}
		}
		// flush buffer
		if (0 < asciibuf.size()) {
			String surfaces = getSurfaceForms(asciibuf);
			if (isAlphabet(surfaces)) {
				results.addAll(tokenizeEN(surfaces));
			} else {
				for (Token ascii : asciibuf) {
					results.add(new JAENToken(ascii));
				}
			}
			asciibuf.clear();
		}
		return results;
	}

	private static final Pattern ASCII_PRINTABLE_PATTERN = Pattern.compile("[ -~]+"); // ch >= 32 && ch < 127;
	private static final Pattern ALPHABET_PATTERN = Pattern.compile(".*[a-zA-Z]+.*");

	private boolean isAsciiPrintable(String str) {
		return ASCII_PRINTABLE_PATTERN.matcher(str).matches();
	}

	private boolean isAlphabet(String str) {
		return ALPHABET_PATTERN.matcher(str).matches();
	}

	private static String getSurfaceForms(List<Token> tokens) {
		StringBuilder buf = new StringBuilder();
		for (Token token : tokens) {
			buf.append(token.getSurfaceForm());
		}
		return buf.toString();
	}
}
