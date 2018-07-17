package cn.mldn.servlet;

import java.io.File;
import java.io.UnsupportedEncodingException;


import cn.mldn.util.servlet.DispatcherServlet;
import cn.mldn.vo.Dept;

@SuppressWarnings("serial")
public class DeptServlet extends DispatcherServlet {
	private final String insertVlidate = "dept.deptno|dept.dname|dept.loc|dept.company.title" ; 
	private final String updateVlidate = "dept.deptno|dept.dname|dept.loc" ; 
	private Dept dept = new Dept() ;
	public String insert() {
		if (super.isUpload()) {	// 是否有文件上传
			String fileName = super.createSingleFileName() ;
			super.upload(fileName);
		}
		super.setMsgAndUrl("insert.success.msg", "index.page");
		return "forward.page" ;
	}
	public String list() throws UnsupportedEncodingException {
		super.handleSplit();// 处理分页
		
		// 还需要通过servlet传递url、allRecorders
		super.request.setAttribute("url", "/dept/list");
		super.request.setAttribute("allRecorders", 97);
		
		return "dept.list.page" ;
	}
	public String update() {
		return "" ;
	}
	@Override
	public String getTitle() {
		return "部门";
	}
	public Dept getDept() {
		return dept;
	}
	@Override
	public String getUploadDirectory() {
		return "/upload" + File.separator + "dept" + File.separator;
	}
	@Override
	public String getColumnData() {
		return "部门名称:dname|公司名称:title";
	}
	@Override
	public String getDefaultColumn() {
		return "dname";
	}
}
