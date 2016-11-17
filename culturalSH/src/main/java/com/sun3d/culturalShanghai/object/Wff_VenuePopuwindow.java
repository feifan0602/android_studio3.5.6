package com.sun3d.culturalShanghai.object;

import org.json.JSONArray;

public class Wff_VenuePopuwindow {
	String tagId;
	String tagName;
	String dictId;
	String dictName;
	String dictCode;
	JSONArray dictList;
	String id;
	String name;
	

	public Wff_VenuePopuwindow(String dictName) {
		super();
		this.dictName = dictName;
	}


	public Wff_VenuePopuwindow(String dictId, String dictName, String dictCode,
			JSONArray dictList) {
		super();
		this.dictId = dictId;
		this.dictName = dictName;
		this.dictCode = dictCode;
		this.dictList = dictList;
	}

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public JSONArray getDictList() {
		return dictList;
	}

	public void setDictList(JSONArray dictList) {
		this.dictList = dictList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Wff_VenuePopuwindow(String tagId, String tagName) {
		super();
		this.tagId = tagId;
		this.tagName = tagName;
	}

}
