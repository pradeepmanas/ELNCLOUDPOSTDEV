package com.agaram.eln.primary.model.methodsetup;

/**
 * This class is used to hold the fields that are to be used while applying remove techniques
 * to a raw data file.
 * @version 1.0.0
 * @since   21- Apr- 2020
 */
public class RemoveItem {
	
	private int excludeIndex;
	private int startIndex;
	private int endIndex;
	public int getExcludeIndex() {
		return excludeIndex;
	}
	public void setExcludeIndex(int excludeIndex) {
		this.excludeIndex = excludeIndex;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	public RemoveItem(int excludeIndex, int startIndex, int endIndex) {
		super();
		this.excludeIndex = excludeIndex;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	public RemoveItem() {}
}
