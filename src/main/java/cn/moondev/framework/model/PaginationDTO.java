package cn.moondev.framework.model;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class PaginationDTO<T> {
	private int pager;

	private int pages;

	private int size;

	private long total;

	private int offset;

	private List<T> list = Lists.newArrayList();
	//统计
	private Map sta;

	@SuppressWarnings("unused")
	public PaginationDTO() {
	}

	private PaginationDTO(int pager, int size) {
		if(pager < 0 || size <= 0){
			throw new RuntimeException("invalid pager: " + pager + " or size: " + size);
		}
		this.pager = pager;
		this.size = size;
	}

	public static PaginationDTO create(int pager, int size){
		return new PaginationDTO(pager, size);
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getPager() {
		return 0 == pager ? 1 : pager;
	}

	public int getSize() {
		return size;
	}

	public int getPages() {
		return Double.valueOf(Math.ceil((double)total / (double)size)).intValue();
	}

	public int getOffset() {
		return size * (getPager() -1);
	}

	public List<T> getList() {
		return list;
	}

	public long getTotal() {
		return total;
	}

	public Map getSta() {
		return sta;
	}

	public void setSta(Map sta) {
		this.sta = sta;
	}
	
	
}