package cn.mldn.util.servlet;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.mldn.util.AttributeType;
import cn.mldn.util.BeanOperate;
import cn.mldn.util.ValidateParameter;

import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

public abstract class DispatcherServlet extends HttpServlet {

	private SmartUpload smart = null;

	private static final String PAGES_BASENAME = "Pages";
	private static final String MESSAGES_BASENAME = "Messages";

	private ResourceBundle pagesResource;
	private ResourceBundle messagesResource;
	protected HttpServletRequest request;
	protected HttpServletResponse response;

	private Integer currentPage = 1;
	private Integer lineSize = 5 ;
	private String keyWord;
	private String column;

	public void init() throws ServletException {
		this.pagesResource = ResourceBundle.getBundle(PAGES_BASENAME,
				Locale.getDefault());
		this.messagesResource = ResourceBundle.getBundle(MESSAGES_BASENAME,
				Locale.getDefault());
	};

	public void handleSplit() throws UnsupportedEncodingException {
		try {
			this.currentPage = Integer
					.parseInt(this.request.getParameter("cp"));
		} catch (Exception e) {
		}
		try {
			this.lineSize = Integer.parseInt(this.request.getParameter("ls"));
		} catch (Exception e) {
		}
		this.column = request.getParameter("col");
		this.keyWord = new String(request.getParameter("kw").getBytes("iso-8859-1"),"UTF-8");
		if (this.column == null) {
			this.column = this.getDefaultColumn();
		}
		if (this.keyWord == null) {
			this.keyWord = ""; // 表示查询全部
		}
		this.request.setAttribute("currentPage", this.currentPage);
		this.request.setAttribute("lineSize", this.lineSize);
		this.request.setAttribute("column", this.column);
		this.request.setAttribute("keyWord", this.keyWord);
		this.request.setAttribute("columnData", this.getColumnData());
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public Integer getLineSize() {
		return lineSize;
	}

	public String getColumn() {
		return column;
	}

	public String getKeyWord() {
		return keyWord;
	}

	/**
	 * 取得在Pages.properties文件里面定义的访问路径
	 * 
	 * @param key
	 *            访问路径的key
	 * @return 配置文件中的路径内容，如果没有返回null
	 */
	public String getPath(String key) {
		return this.pagesResource.getString(key);
	}

	/**
	 * 取得Messages.properties文件中的配置文字信息
	 * 
	 * @param key
	 *            访问文字信息的key
	 * @param args
	 *            所有占位符的内容
	 * @return 配置文件中的内容，并且是组合后的结果，如果没有返回null
	 */
	public String getMsg(String key, String... args) {
		String note = this.messagesResource.getString(key);
		if (args.length > 0 || this.getTitle() == null) { // 传递了参数内容
			return MessageFormat.format(note, args);
		} else {
			return MessageFormat.format(note, this.getTitle());
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//解决乱码
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		this.request = request;
		this.response = response;
		String path = this.getPath("errors.page");
		//获取方法名称
		String status = request.getRequestURI().substring(
				request.getRequestURI().lastIndexOf("/") + 1);
		//文件上传处理
		if (request.getContentType() != null) {
			if (request.getContentType().contains("multipart/form-data")) {
				this.smart = new SmartUpload();
				try {
					this.smart.initialize(super.getServletConfig(), request,
							response);
					this.smart.upload();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// 现在可以找到当前类对象this，以及要调用的方法名称status，那么可以利用反射进行调用
		if (status != null && status.length() > 0) {
			// 在进行参数的处理之前，需要对提交数据进行验证
			if (this.validateRequest(status)) {
				this.handleRequest();// 处理参数与简单Java类之间的自动赋值
				try { // 只有将对应的数据都准备完毕了，才可以执行以下方法
					Method method = this.getClass().getMethod(status);
					path = this.getPath((String) method.invoke(this));// 反射调用方法
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		//服务器端的跳转
		request.getRequestDispatcher(path).forward(request, response);
	}

	/**
	 * 首先一定要取得操作状态
	 * 
	 * @param status
	 * @return
	 */
	public boolean validateRequest(String status) {
		boolean flag = false;
		String rule = null;
		try {
			Field field = this.getClass().getDeclaredField(status + "Vlidate");
			field.setAccessible(true);
			rule = (String) field.get(this); // 取出验证规则的数据
		} catch (Exception e) { // 表示数据现在不存在
			flag = true;
		}
		// 需要针对于给定的内容进行指定格式的验证
		if (rule == null || "".equals(rule)) { // 现在没有规则
			flag = true;
		} else { // 现在有规则
			ValidateParameter vp = new ValidateParameter(this, request,
					this.smart, rule, this.messagesResource);
			flag = vp.validate(); // 进行验证操作
			this.request.setAttribute("errors", vp.getErrors());
		}
		return flag;
	}

	/**
	 * 此方法主要是判断是否有文件上传
	 * 
	 * @return 没有文件上传返回false
	 */
	public boolean isUpload() {
		try {
			return this.smart.getFiles().getSize() > 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 取得上传文件的个数
	 * 
	 * @return
	 */
	public int getUploadCount() {
		return this.smart.getFiles().getCount();
	}

	/**
	 * 专门负责文件的保存操作
	 * 
	 * @param index
	 *            SmartUpload操作索引
	 * @param fileName
	 *            文件名称
	 * @throws SmartUploadException
	 * @throws IOException
	 */
	private void saveFile(int index, String fileName) throws IOException,
			SmartUploadException {
		String filePath = super.getServletContext().getRealPath(
				this.getUploadDirectory())
				+ fileName;
		File file = new File(filePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (this.smart.getFiles().getFile(index).getContentType()
				.contains("image")) {
			this.smart.getFiles().getFile(index).saveAs(filePath);
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 */
	public void deleteFile(String fileName) {
		String filePath = super.getServletContext().getRealPath(
				this.getUploadDirectory())
				+ fileName;
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 批量删除上传文件
	 * 
	 * @param all
	 */
	public void deleteFile(Set<String> all) {
		Iterator<String> iter = all.iterator();
		while (iter.hasNext()) {
			this.deleteFile(iter.next());
		}
	}

	/**
	 * 存放一个文件信息
	 * 
	 * @param fileName
	 */
	public void upload(String fileName) {
		try {
			this.saveFile(0, fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmartUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void upload(Map<Integer, String> fileName) {
		Iterator<Map.Entry<Integer, String>> iter = fileName.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer, String> me = iter.next();
			try {
				this.saveFile(me.getKey(), me.getValue());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SmartUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建一个新的文件名称
	 * 
	 * @return
	 */
	public String createSingleFileName() {
		String fileName = null;
		if (this.isUpload()) {
			System.out.println("########"+this.smart.getFiles().getFile(0));
			if (this.smart.getFiles().getFile(0).getContentType()
					.contains("image")) {
				fileName = UUID.randomUUID() + "."
						+ this.smart.getFiles().getFile(0).getFileExt();
			}
		}
		return fileName;
	}

	public Map<Integer, String> createMultiFileName() {
		Map<Integer, String> all = new HashMap<Integer, String>();
		if (this.isUpload()) {
			for (int x = 0; x < this.smart.getFiles().getCount(); x++) {
				if (this.smart.getFiles().getFile(x).getContentType()
						.contains("image")) {
					String fileName = UUID.randomUUID() + "."
							+ this.smart.getFiles().getFile(x).getFileExt();
					all.put(x, fileName);
				}
			}
		}
		return all;
	}

	public SmartUpload getSmart() {
		return this.smart;
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
    /**
     *   将参数内容赋值到java 的vo对象中
     */
	private void handleRequest() {
		if (request.getContentType() != null) {
			// 当前使用了表单封装，意味着是有文件上传，应该使用SmartUpload接收数据
			if (request.getContentType().contains("multipart/form-data")) {
				// 取得全部的请求参数名称，之所以需要名称，主要是确定自动赋值的操作
				Enumeration<String> enu = this.smart.getRequest()
						.getParameterNames();
				while (enu.hasMoreElements()) { // 循环所有的参数名称
					String paramName = enu.nextElement();
					if (paramName.contains(".")) { // 按照简单Java类处理
						AttributeType at = new AttributeType(this, paramName);
						if (at.getFiledType().contains("[]")) { // 按照数组的方式进行处理
							BeanOperate bo = new BeanOperate(this, paramName,
									this.smart.getRequest().getParameterValues(
											paramName));
						} else { // 按照单个字符串的方式进行处理
							BeanOperate bo = new BeanOperate(this, paramName,
									this.smart.getRequest().getParameter(
											paramName));
						}
					}
				}
			}
		} else { // 应该使用普通的request对象进行数据的接收
			// 取得全部的请求参数名称，之所以需要名称，主要是确定自动赋值的操作
			Enumeration<String> enu = request.getParameterNames();
			while (enu.hasMoreElements()) { // 循环所有的参数名称
				String paramName = enu.nextElement();
				if (paramName.contains(".")) { // 按照简单Java类处理
					AttributeType at = new AttributeType(this, paramName);
					if (at.getFiledType().contains("[]")) { // 按照数组的方式进行处理
						BeanOperate bo = new BeanOperate(this, paramName,
								request.getParameterValues(paramName));
					} else { // 按照单个字符串的方式进行处理
						BeanOperate bo = new BeanOperate(this, paramName,
								request.getParameter(paramName));
					}
				}
			}
		}
	}
	
	public void setMsgAndUrl(String msgKey,String urlKey) {
		this.request.setAttribute("msg", this.getMsg(msgKey,this.getTitle()));
		this.request.setAttribute("url", this.getPath(urlKey));
	}

	/**
	 * 交由不同的子类来实现，可以由子类设置统一的占位符提示信息名称标记
	 * 
	 * @return 返回不同子类的描述信息
	 */
	public abstract String getTitle();

	/**
	 * 取得要进行文件上传保存的目录
	 * 
	 * @return
	 */
	public abstract String getUploadDirectory();

	/**
	 * 得到分页搜索条的数据列
	 * 
	 * @return
	 */
	public abstract String getColumnData();

	/**
	 * 得到默认查询的数据列
	 * 
	 * @return
	 */
	public abstract String getDefaultColumn();
}
