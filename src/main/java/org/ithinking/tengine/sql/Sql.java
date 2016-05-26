package org.ithinking.tengine.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.ithinking.tengine.core.Context;


public class Sql{

	private SqlBuilder builder = new SqlBuilder();
	
	public Column[] getColumns(){
		return null;
	}
	
	public String getSqlText(){
		return null;
	}
	
	public String buildSql(Connection conn, Context context){
		return null;
	}
	

	public Statement createStatement(Connection conn, Context context) throws SQLException{
		String sql = this.buildSql(conn, context);
		PreparedStatement stmt = conn.prepareStatement(sql);
		return stmt;
	}
	
}
