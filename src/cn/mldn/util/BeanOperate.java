package cn.mldn.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

public class BeanOperate {
	private Object currentObj;//当前程序的保存对象
	private String attribute;//要操作的属性
	private String value;//要操作的内容
	private Field field;//表示要操作的成员对象
	private String arrayValue[];//要操作的数组内容
	/**
	 * 
	 * @param currentObje
	 * @param attribute
	 * @param arrayValue
	 */
	public BeanOperate(Object currentObj,String attribute,String arrayValue[]){
		this.currentObj=currentObj;
		this.attribute=attribute;
		this.arrayValue=arrayValue;
	    this.handleParameter();
	    this.setValue();
	}
	/**
	 * 进行参数的保存
	 * @param currentObj    表示当前要操作此功能的类对象
	 * @param attribute   "对象.属性.属性.属性...."字符串
	 * @param value       表示属性的内容
	 */
	public BeanOperate(Object currentObj,String attribute,String value){
		this.currentObj=currentObj;
		this.attribute=attribute;
		this.value=value;
		this.handleParameter();//处理参数
		//设置内容
		this.setValue();
	}
	
	private void handleParameter(){
		try {
			String result[]=this.attribute.split("\\.");
			if(result.length==2){
				//单级操作
				Method getMet=this.currentObj.getClass().getMethod("get"+StringUtils.initcap(result[0]));
				this.currentObj=getMet.invoke(this.currentObj);//调用getter方法
				this.field=this.currentObj.getClass().getDeclaredField(result[1]);//取得对象成员
			}else{
				//多级操作
				for(int x=0;x<result.length;x++){
					//获取当前操作的成员对象
					this.field=this.currentObj.getClass().getDeclaredField(result[x]);
					if(x<result.length-1){
						Method met=this.currentObj.getClass().getMethod("get"+StringUtils.initcap(result[x]));
						this.currentObj=met.invoke(this.currentObj);
					}
				}
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void setValue(){
		try {
			Method setMet=this.currentObj.getClass().getMethod("set"+StringUtils.initcap(this.field.getName()), this.field.getType());//获取设置的方法
			//取得数据类型
			String type=this.field.getType().getSimpleName();
			if("int".equalsIgnoreCase(type)||"Integer".equalsIgnoreCase(type)){
				if(this.value.matches("\\d+")){//数字
					//调用方法设置内容
					setMet.invoke(this.currentObj, Integer.parseInt(this.value));
				}
			}else if("double".equalsIgnoreCase(type)){
				if(this.value.matches("\\d+(\\.\\d+)?")){//匹配数字
					setMet.invoke(this.currentObj, Double.parseDouble(this.value));
				}
			}else if("string".equalsIgnoreCase(type)){
				setMet.invoke(this.currentObj,this.value);
			}else if("date".equalsIgnoreCase(type)){
				//日期格式yyyy-MM-dd
				if(this.value.equalsIgnoreCase("\\d{4}-\\d{2}-\\d{2}")){
					setMet.invoke(this.currentObj, new SimpleDateFormat("yyyy-MM-dd").parse(this.value));
				}
				//日期格式yyyy-MM-dd HH:mm:ss
				if(this.value.equalsIgnoreCase("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")){
					setMet.invoke(this.currentObj,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.value));
				}
			}else if("string[]".equalsIgnoreCase(type)){
				//字符串数组
				setMet.invoke(this.currentObj,new Object[]{this.arrayValue});
			}else if("int []".equalsIgnoreCase(type)){
				//准备遍历
				int temp[]=new int[this.arrayValue.length];
				for(int x=0;x<temp.length;x++){
					if(this.arrayValue[x].matches("\\d+")){
						temp[x]=Integer.parseInt(this.arrayValue[x]);
					}
				}
				setMet.invoke(this.currentObj,new Object[]{temp});
			}else if("integer[]".equalsIgnoreCase(type)){
				//准备遍历
			    Integer temp[]=new Integer[this.arrayValue.length];
			    for(int x=0;x<temp.length;x++){
			    	if(this.arrayValue[x].matches("\\d+")){
			    		temp[x]=Integer.parseInt(this.arrayValue[x]);
			    	}
			    }
			    setMet.invoke(this.currentObj,new Object[]{temp});
			}else if("double[]".equalsIgnoreCase(type)){
				//准备遍历
				double temp[]=new double[this.arrayValue.length];
				for(int x=0;x<temp.length;x++){
					if(this.arrayValue[x].matches("\\d+(\\.\\d+)?")){
						temp[x]=Double.parseDouble(this.arrayValue[x]);
					}
				}
				setMet.invoke(this.currentObj,new Object[]{temp});
			}else if("Double[]".equalsIgnoreCase(type)){
				//准备遍历
				Double temp[]=new Double[this.arrayValue.length];
				for(int x=0;x<temp.length;x++){
					if(this.arrayValue[x].matches("\\d+(\\.\\d+)?")){
						temp[x]=Double.parseDouble(this.arrayValue[x]);
					}
				}
				setMet.invoke(this.currentObj,new Object[]{temp});
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}

}
