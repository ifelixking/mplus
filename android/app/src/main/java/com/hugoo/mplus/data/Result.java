package com.hugoo.mplus.data;

/**
 * Created by liyh on 2017/11/23.
 */

public class Result<T> {
	public String status;
	public T detail;
	
	public boolean isSuccess(){
		return "success".equals(status);
	}

}
