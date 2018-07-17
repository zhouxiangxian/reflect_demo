package cn.mldn.vo;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings("serial")
public class Dept implements Serializable {
	private String dname;
	private Integer deptno;
	public Integer getDeptno() {
		return deptno;
	}
	public void setDeptno(Integer deptno) {
		this.deptno = deptno;
	}
	private Company company=new Company();
	private Integer[] loc;
	public Integer[] getLoc() {
		return loc;
	}
	public void setLoc(Integer[] loc) {
		this.loc = loc;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public Dept(){
		
	}
	@Override
	public String toString() {
		return "Dept [dname=" + dname + ", deptno=" + deptno + ", company="
				+ company + ", loc=" + Arrays.toString(loc) + "]";
	}
	

}
