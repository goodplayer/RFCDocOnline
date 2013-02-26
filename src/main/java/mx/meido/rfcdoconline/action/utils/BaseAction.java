package mx.meido.rfcdoconline.action.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;

import com.ghosthack.turismo.action.Action;

public abstract class BaseAction extends Action {

	private QueryRunner queryRunner;
	private DataSource dataSource;
	
	protected QueryRunner dbquery(){
		if(queryRunner == null)
			this.queryRunner = new QueryRunner((DataSource)ctx().getAttribute("dataSource"));
		return this.queryRunner;
	}
	protected Connection dbconn() {
		if(dataSource == null)
			this.dataSource = (DataSource) ctx().getAttribute("dataSource");
		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	protected void dbtrans(TransactionManager tx) {
		TransStatus status = new TransStatus();
		try {
			Connection conn = dbconn();
			conn.setAutoCommit(false);
			
			status.setConn(conn);
			tx.onResult(status, tx.doDbOperation(conn));
			
			conn.setAutoCommit(true);
			conn.close();
		} catch (Exception e) {
			tx.onException(status, e);
		}
	}
	
	@Override
	public abstract void run();
	
	public static abstract class TransactionManager{
		protected abstract int doDbOperation(Connection conn) throws SQLException;
		protected abstract void onResult(TransStatus status, int result);
		protected abstract void onException(TransStatus status, Exception e);
		protected QueryRunner createQueryRunner(){
			return new QueryRunner();
		}
	}
	public static abstract class SimpleTransManager extends TransactionManager{
		@Override
		protected void onException(TransStatus status, Exception e) {
			e.printStackTrace();
			status.rollback();
		}
		@Override
		protected void onResult(TransStatus status, int result) {
			status.commit();
		}
		@Override
		protected int doDbOperation(Connection conn) throws SQLException {
			this.doDb(conn);
			return 0;
		}
		protected abstract void doDb(Connection conn) throws SQLException;
	}
	public static class TransStatus{
		private Connection conn;
		public void commit(){
			try {
				conn.commit();
			} catch (SQLException e) {
			}
		}
		public void rollback(){
			try {
				conn.rollback();
			} catch (SQLException e) {
			}
		}
		void setConn(Connection conn){
			this.conn = conn;
		}
		Connection getConn(){
			return this.conn;
		}
	}

}
