package com.appbar.util;

public class SqlQueryString {
	private String sql = "";
	private Object[] args;

	public SqlQueryString() {
	}

	public SqlQueryString(String sql_str, Object[] args) {
		this.sql = sql_str;
		this.args = args;
	}

	public String pair() {
		int args_index = 0;
		String sql_str = sql;
		while (sql_str.indexOf("?") != -1) {
			try {
				String type = args[args_index].getClass().getName();
				if (type.equals("java.lang.String"))
					sql_str = sql_str
							.replace("?", "'" + args[args_index] + "'");
				else
					sql_str = sql_str.replace("?", args[args_index] + "");
				args_index++;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Arguments and sql syntax does not match.");
				break;
			}
		}
		return sql_str;
	}

	public SqlQueryString select(String field, String table) {
		if (field.equals("*"))
			this.sql = this.sql + "SELECT * FROM " + table + " ";
		else
			this.sql = this.sql + "SELECT " + field + " FROM " + table + " ";
		return this;
	}

	public SqlQueryString where(String where_cond) {
		this.sql = this.sql + "WHERE " + where_cond + " ";
		return this;
	}

	public SqlQueryString and(String cond) {
		this.sql = this.sql + "AND " + cond + " ";
		return this;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String toString() {
		return sql;
	}
}
