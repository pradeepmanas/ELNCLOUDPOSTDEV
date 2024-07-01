package com.agaram.eln.primary.model.syncwordconverter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpellCheckJsonData {
	@JsonProperty("LanguageID")
	public int languageID;
	@JsonProperty("TexttoCheck")
	public String texttoCheck;
	@JsonProperty("CheckSpelling")
	public boolean checkSpelling;
	@JsonProperty("CheckSuggestion")
	public boolean checkSuggestion;
	@JsonProperty("AddWord")
	public boolean addWord;
}
