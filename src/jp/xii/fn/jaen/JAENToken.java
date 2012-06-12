package jp.xii.fn.jaen;

import org.atilika.kuromoji.Token;

public class JAENToken {
	private Token token;
	private String surfaceForm;
	private String pos;

	public JAENToken(Token token) {
		this.token = token;
		this.surfaceForm = null;
		this.pos = null;
	}

	public JAENToken(String surfaceForm, String pos) {
		this.token = null;
		this.surfaceForm = surfaceForm;
		this.pos = pos;
	}

	public boolean isJA() {
		return null != token;
	}

	public boolean isEN() {
		return !isJA();
	}

	public String getSurfaceForm() {
		return isJA() ? token.getSurfaceForm() : surfaceForm;
	}

	public String getBaseForm() {
		return isJA() ? token.getBaseForm() : null;
	}

	public String getAllFeatures() {
		return isJA() ? token.getAllFeatures() : null;
	}

	public String[] getAllFeaturesArray() {
		return isJA() ? token.getAllFeaturesArray() : null;
	}

	public String getReading() {
		return isJA() ? token.getReading() : null;
	}

	public String getPartOfSpeech() {
		return isJA() ? token.getPartOfSpeech() : pos;
	}

	public boolean isKnown() {
		return isJA() ? token.isKnown() : null;
	}

	public boolean isUnknown() {
		return isJA() ? token.isUnknown() : null;
	}

	public boolean isUser() {
		return isJA() ? token.isUser() : null;
	}

	public int getPosition() {
		return isJA() ? token.getPosition() : null;
	}

	public String toString() {
		return getSurfaceForm() + "/" + getPartOfSpeech();
	}
}
