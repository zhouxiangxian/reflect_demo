package cn.mldn.factory;

import cn.mldn.dao.DeptDAOImpl;
import cn.mldn.dao.proxy.DAOProxy;

public class DAOFactory {
	public static Object getIDeptDAOInstance(){
		return new DAOProxy().bind(new DeptDAOImpl());
	}

}
