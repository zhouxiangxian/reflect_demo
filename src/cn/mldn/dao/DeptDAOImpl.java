package cn.mldn.dao;

import java.util.List;

import cn.mldn.vo.Dept;

public class DeptDAOImpl implements IDeptDAO {

	public boolean doCreate(Dept vo) throws Exception {
		System.out.println("执行数据增加");
		return false;
	}

	public List<Dept> findAll() throws Exception {
		System.out.println("执行数据查询");
		return null;
	}

}
