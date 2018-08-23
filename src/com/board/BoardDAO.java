package com.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BoardDAO {

	private Connection conn;
	
	public BoardDAO(Connection conn){
		this.conn = conn;
	}
	
	public int getMaxNum(){
		
		int maxNum = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql = "select nvl(max((num), 0) from board";
			
			pstmt = conn.prepareStatement(sql);
			
			if(rs.next()){
				maxNum = rs.getInt(1);
				
				rs.close();
				pstmt.close();
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return maxNum;
		
	}
	
	public int insertData(BoardForm dto){
		
		int result = 0;
	
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "insert into board (num, name, pwd, email, subject, content, ipAddr, hitCount, created) ";
			sql += "values (?,?,?,?,?,?,?,0,sysdate)";
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
}
