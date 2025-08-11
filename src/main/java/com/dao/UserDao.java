package com.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.bean.UserBean;
import com.util.DBConnection;

public class UserDao {
	public ArrayList<UserBean> displayAllRecord() {
		String selectQry = "SELECT * FROM users";
		Connection conn = DBConnection.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<UserBean> list = new ArrayList<UserBean>();

		UserBean sbean = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(selectQry);

			while (rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				String name_hin = rs.getString(3);
				String name_guj = rs.getString(4);

				sbean = new UserBean(id, name, name_hin, name_guj);
				list.add(sbean);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

	}
	
	public ResultSet displayAllRecord2() {
		String selectQry = "SELECT * FROM users";
		Connection conn = DBConnection.getConnection();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(selectQry);


		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rs;

	}
}
