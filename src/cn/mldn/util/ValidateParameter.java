package cn.mldn.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import com.jspsmart.upload.SmartUpload;

public class ValidateParameter {
	private HttpServletRequest request;
	private SmartUpload smart;
	private String rule;
	private Object currentObject;
	// 保存所有的错误信息，其中key保存的是参数名称，value保存的是内容信息
	private Map<String, String> map = new HashMap<String, String>();
	private ResourceBundle msg = null;

	/**
	 * 进行数据验证的设置操作
	 * 
	 * @param request
	 *            需要接收所有的请求参数，所以需要request对象
	 * @param rule
	 *            验证规则
	 */
	public ValidateParameter(Object currentObject, HttpServletRequest request,
			SmartUpload smart, String rule, ResourceBundle msg) {
		this.request = request;
		this.smart = smart;
		this.rule = rule;
		this.currentObject = currentObject;
		this.msg = msg;
	}

	public boolean validate() {
		boolean flag = true;
		String result[] = this.rule.split("\\|");
		for (int x = 0; x < result.length; x++) {
			AttributeType at = new AttributeType(this.currentObject, result[x]);
			String type = at.getFiledType().toLowerCase();
			if (type.contains("[]")) {
				String value[] = null;
				if(this.request.getContentType()!=null){
					if (this.request.getContentType().contains(
							"multipart/form-data")) {
						value = smart.getRequest()
								.getParameterValues(result[x]);
					} else {
						value = request.getParameterValues(result[x]);
					}
				}
				if (value == null) { // 现在没有数据提交
					this.map.put(result[x],
							this.msg.getString("validate.string"));
				} else {
					if ("int[]".equals(type) || "integer[]".equals(type)) {
						for (int y = 0; y < value.length; y++) {
							if (!ValidateUtil.validateRegex(value[y], "\\d+")) {
								this.map.put(result[x],
										this.msg.getString("validate.int"));
							}
						}
					} else if ("double[]".equals(type)) {
						for (int y = 0; y < value.length; y++) {
							if (!ValidateUtil.validateRegex(value[y],
									"\\d+(\\.\\d+)?")) {
								this.map.put(result[x],
										this.msg.getString("validate.double"));
							}
						}
					}
				}
			} else {
				String value = null;
				if(this.request.getContentType()!=null){
					if (this.request.getContentType().contains(
							"multipart/form-data")) {
						value = smart.getRequest().getParameter(result[x]);
					} else {
						value = request.getParameter(result[x]);
					}
				}
				if ("string".equals(type)) {
					if (!ValidateUtil.validateEmpty(value)) { // 数据现在为空
						this.map.put(result[x],
								this.msg.getString("validate.string"));
					}
				} else if ("int".equals(type) || "integer".equals(type)) {
					if (!ValidateUtil.validateRegex(value, "\\d+")) {
						this.map.put(result[x],
								this.msg.getString("validate.int"));
					}
				} else if ("double".equals(type)) {
					if (!ValidateUtil.validateRegex(value, "\\d+(\\.\\d+)?")) {
						this.map.put(result[x],
								this.msg.getString("validate.double"));
					}
				} else if ("date".equals(type)) {
					if (!ValidateUtil.validateRegex(value,
							"\\d{4}-\\d{2}-\\d{2}")) {
						if (!ValidateUtil.validateRegex(value,
								"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
							this.map.put(result[x],
									this.msg.getString("validate.date"));
						}
					}
				}
			}
		}
		if (this.map.size() > 0) { // 有错误
			flag = false;
		}
		return flag;
	}

	public Map<String, String> getErrors() {
		return this.map;
	}
}
