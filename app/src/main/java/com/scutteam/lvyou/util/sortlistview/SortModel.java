package com.scutteam.lvyou.util.sortlistview;


public class SortModel {

	private String name;   //显示的数据
	private String sortLetters;  //显示数据拼音的首字母
    private Long sort_place_id; //每个出发地的id

    public Long getSort_place_id() {
        return sort_place_id;
    }

    public void setSort_place_id(Long sort_place_id) {
        this.sort_place_id = sort_place_id;
    }

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
