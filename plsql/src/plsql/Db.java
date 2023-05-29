package plsql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.time.format.DateTimeFormatter;

public class Db {
	private String connectionString;
	private String password;
	private String userName;
	private DateTimeFormatter dtf;
	
	public int esetek_szama_szures(String hivo, Date startDate, Date endDate) {
	    int esetekSzama = 0;
	    try (Connection conn = connect();
	         CallableStatement stmt = conn.prepareCall("{ ? = call esetek_szama_szures(?, ?, ?) }")) {

	        stmt.registerOutParameter(1, Types.NUMERIC);
	        stmt.setString(2, hivo);
	        stmt.setDate(3, startDate);
	        stmt.setDate(4, endDate);

	        stmt.execute();

	        esetekSzama = stmt.getInt(1);
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return esetekSzama;
	}
	
	public int esetek_szama(Date datum) {
	    int esetekSzama = 0;
	    try (Connection conn = connect();
	         CallableStatement stmt = conn.prepareCall("{ ? = call esetek_szama(?) }")) {

	        stmt.registerOutParameter(1, Types.NUMERIC);
	        stmt.setDate(2, datum);

	        stmt.execute();

	        esetekSzama = stmt.getInt(1);
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return esetekSzama;
	}
	
	// Eset tábla adatok módosítása
	public void modositas_eset(int esetId, String hivo, String telefonszam, String cim, Date hivasideje) {
	    try (Connection conn = connect();
	         CallableStatement stmt = conn.prepareCall("{ call modositas_eset(?, ?, ?, ?, ?) }")) {

	        stmt.setInt(1, esetId);
	        stmt.setString(2, hivo);
	        stmt.setString(3, telefonszam);
	        stmt.setString(4, cim);
	        stmt.setDate(5, hivasideje);

	        stmt.execute();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}

	// Mentoegyseg tábla adatok módosítása
	public void modositas_mentoegyseg(int mentoegysegId, String rendszam, String tipus, String allapot) {
	    try (Connection conn = connect();
	         CallableStatement stmt = conn.prepareCall("{ call modositas_mentoegyseg(?, ?, ?, ?) }")) {

	        stmt.setInt(1, mentoegysegId);
	        stmt.setString(2, rendszam);
	        stmt.setString(3, tipus);
	        stmt.setString(4, allapot);

	        stmt.execute();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}

	// Mentes tábla adatok módosítása
	public void modositas_mentes(int mentesId, int esetId, int mentoegysegId, java.sql.Date erkezes, String szemelyallapot) {
	    try (Connection conn = connect();
	         CallableStatement stmt = conn.prepareCall("{ call modositas_mentes(?, ?, ?, ?, ?) }")) {

	        stmt.setInt(1, mentesId);
	        stmt.setInt(2, esetId);
	        stmt.setInt(3, mentoegysegId);
	        stmt.setDate(4, erkezes);
	        stmt.setString(5, szemelyallapot);

	        stmt.execute();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
	
	// Eset tábla adatok törlése
	public void torles_eset(int esetId) {
	    try (Connection conn = connect();
	         CallableStatement stmt = conn.prepareCall("{ call torles_eset(?) }")) {

	        stmt.setInt(1, esetId);

	        stmt.execute();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}

	// Mentoegyseg tábla adatok törlése
	public void torles_mentoegyseg(int mentoegysegId) {
	    try (Connection conn = connect();
	         CallableStatement stmt = conn.prepareCall("{ call torles_mentoegyseg(?) }")) {

	        stmt.setInt(1, mentoegysegId);

	        stmt.execute();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}

	// Mentes tábla adatok törlése
	public void torles_mentes(int mentesId) {
	    try (Connection conn = connect();
	         CallableStatement stmt = conn.prepareCall("{ call torles_mentes(?) }")) {

	        stmt.setInt(1, mentesId);

	        stmt.execute();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}

	
	public void man_felvetel_eset(String hivo, String telefonszam, String cim, java.sql.Date hivasideje) {
	    try (Connection conn = connect();
	         CallableStatement stmt = conn.prepareCall("{ call man_felvetel_eset(?, ?, ?, ?) }")) {

	        stmt.setString(1, hivo);
	        stmt.setString(2, telefonszam);
	        stmt.setString(3, cim);
	        stmt.setDate(4, hivasideje);

	        stmt.execute();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
	
	public void man_felvetel_mentes(int esetId, int mentoegysegId, java.sql.Date erkezes, String szemelyallapot) {
        Connection connection = null;
        CallableStatement statement = null;
        try {
            connection = connect();
            String sql = "{ call man_felvetel_mentes(?, ?, ?, ?) }";
            statement = connection.prepareCall(sql);

            statement.setInt(1, esetId);
            statement.setInt(2, mentoegysegId);
            statement.setDate(3, erkezes);
            statement.setString(4, szemelyallapot);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            disconnect(connection);
        }
    }
	
	public void man_felvetel_mentoegyseg(String rendszam, String tipus, String allapot) {
        Connection connection = null;
        CallableStatement statement = null;
        try {
            connection = connect();
            String sql = "{ call man_felvetel_mentoegyseg(?, ?, ?) }";
            statement = connection.prepareCall(sql);

            statement.setString(1, rendszam);
            statement.setString(2, tipus);
            statement.setString(3, allapot);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            disconnect(connection);
        }
    }
	
	public void all_feltolt_mentes(String filePath) {
        Connection connection = null;
        CallableStatement statement = null;
        try {
            connection = connect();
            String sql = "{ call all_feltolt_mentes(?) }";
            statement = connection.prepareCall(sql);

            statement.setString(1, filePath);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            disconnect(connection);
        }
    }
	
	public void all_feltolt_mentoegyseg(String filePath) {
        Connection connection = null;
        CallableStatement statement = null;
        try {
            connection = connect();
            String sql = "{ call all_feltolt_mentoegyseg(?) }";
            statement = connection.prepareCall(sql);

            statement.setString(1, filePath);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            disconnect(connection);
        }
    }
	
	 public void all_feltolt_eset(String filePath) {
	        Connection connection = null;
	        CallableStatement statement = null;
	        try {
	            connection = connect();
	            String sql = "{ call all_feltolt_eset(?) }";
	            statement = connection.prepareCall(sql);

	            statement.setString(1, filePath);

	            statement.execute();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            if (statement != null) {
	                try {
	                    statement.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            disconnect(connection);
	        }
	    }
	
	public void feltolt_mentes() {
        Connection connection = null;
        CallableStatement statement = null;
        try {
            connection = connect();
            String sql = "{ call feltolt_mentes }";
            statement = connection.prepareCall(sql);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            disconnect(connection);
        }
    }
	
	public void feltolt_mentoegyseg() {
        Connection connection = null;
        CallableStatement statement = null;
        try {
            connection = connect();
            String sql = "{ call feltolt_mentoegyseg }";
            statement = connection.prepareCall(sql);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            disconnect(connection);
        }
    }
	
	public void feltolt_eset() {
        Connection connection = null;
        CallableStatement statement = null;
        try {
            connection = connect();
            String sql = "{ call feltolt_eset }";
            statement = connection.prepareCall(sql);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            disconnect(connection);
        }
    }
	
	public Db(String connectionString, String userName, String password, String format){
		this.connectionString = connectionString;
		this.userName=userName;
		this.password=password;
		this.dtf = DateTimeFormatter.ofPattern(format);
	}
	private Connection connect() {
		Connection conn=null;
		try {
			conn=DriverManager.getConnection(connectionString, userName, password);
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return conn;
		
	}
	
	private void disconnect(Connection conn) {
		try {
			if(conn!=null) {
				conn.close();
			}
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
	public DateTimeFormatter getDtf() {
		return dtf;
	}

	public void SM(String s) {
		System.out.println(s);
	}
}
