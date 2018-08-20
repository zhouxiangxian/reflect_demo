package cn.mldn.dao;

import java.util.List;

import cn.mldn.vo.Dept;

public interface IDeptDAO {
	public boolean doCreate(Dept vo)throws Exception;
	
	public List<Dept> findAll()throws Exception;

}
