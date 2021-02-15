import java.sql.*;

class DBMatter {
	private static final String ClassName = "DBMatter";

	public String Message = "", DBServer = "", DBBase = "", DBUser = "", DBPsw = "";
	
	public ResultSet resultSet = null;
	
	private Connection conn = null;
	
	public int runCommand(String sSQL) {
		int N = 0;
		
		try {
			if (conn == null) {
				Message = ClassName+": runCommand: No connection";
				return -1;
			}
			if (conn.isClosed()) {
				Message = ClassName+": runCommand: Connection closed";
				return -1;
			}
		
			Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			N = statement.executeUpdate(sSQL);
		} catch (SQLException e) {
			Message = ClassName+": runCommand: "+e.getMessage();
			return -1;
		}
		return N;
	}
	
	public int runQuery(String sSQL) {
		int N = 0;
		
		try {
			if (conn == null) {
				Message = ClassName+": runQuery: No connection";
				return -1;
			}
			if (conn.isClosed()) {
				Message = ClassName+": runQuery: Connection closed";
				return -1;
			}
		
			Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sSQL);

			if(resultSet == null) {
				Message = ClassName+": runQuery: No result set";
				return -1;
			}
			if (resultSet.last()) {
				N = resultSet.getRow();
				resultSet.beforeFirst();
			}
		} catch (SQLException e) {
			Message = ClassName+": runQuery: "+e.getMessage();
			return -1;
		}
		return N;
	}
	
	public int connect(String sDBPsw) {
		Message = "";
		this.DBPsw = sDBPsw;
		
		try {
/*
Instead of String url = "jdbc:sqlserver://192.168.0.95\instance;databaseName=sampleDB";
use String url = "jdbc:sqlserver://192.168.0.95;databaseName=sampleDB";
*/
			//conn = DriverManager.getConnection("jdbc:sqlserver://"+DBServer+";databaseName="+DBBase+";user="+DBUser+";password="+DBPsw,DBUser,DBPsw);
			conn = DriverManager.getConnection("jdbc:sqlserver://"+DBServer+";databaseName="+DBBase,DBUser,DBPsw);
			if (conn == null) {
				Message = ClassName+": connect: No connection";
				return -1;
			}
			DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
			System.out.println("Driver name: " + dm.getDriverName());
			System.out.println("Driver version: " + dm.getDriverVersion());
			System.out.println("Product name: " + dm.getDatabaseProductName());
			System.out.println("Product version: " + dm.getDatabaseProductVersion());
		} catch (SQLException e) {
			Message = ClassName+": connect: "+e.getMessage();
			return -1;
		}
		return 0;
	}
	
	public int closeConnection() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			Message = ClassName+": closeConnection: "+e.getMessage();
			return -1;
		}
		return 0;
	}
	
	DBMatter (String sDBServer, String sDBBase, String sDBUser) {
		this.DBServer = sDBServer;
		this.DBBase = sDBBase;
		this.DBUser = sDBUser;
	}
}