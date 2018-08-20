package cn.mldn.demo;

import cn.mldn.dao.IDeptDAO;
import cn.mldn.factory.DAOFactory;
import cn.mldn.vo.Dept;

public class Hello {
	public static void main(String args[]) throws Exception{
		IDeptDAO dao=(IDeptDAO) DAOFactory.getIDeptDAOInstance();
		System.out.println(dao.doCreate(new Dept()));
	}

}
