package cn.mldn.vo;

import java.io.Serializable;

import cn.mldn.annotation.MyFlag;



@SuppressWarnings("serial")
@MyFlag(name="xiaoxian", value = "小贤")
public class LfEmp implements Serializable {
	@Override
	@Deprecated
	public String toString() {
		return "hello";
	}

}
