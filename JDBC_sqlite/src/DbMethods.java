import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DbMethods {
	Scanner scanner = new Scanner(System.in);

	public String ReadData(String s) {
		String data = "";
		System.out.println(s);
		data = scanner.nextLine();
		return data;
	}

	public void closeScanner() {
		scanner.close();
	}

	public void CommandExec(String command) {
		Connection conn = Connect();
		String sqlp = command;
		SM("Command:" + sqlp);
		try {
			Statement s = conn.createStatement();
			s.execute(sqlp);
			SM("Command OK!");
		} catch (SQLException e) {
			SM("CommandExec:" + e.getMessage());
		}
		DisConnect(conn);
	}

	public Connection Connect() {
		Connection conn = null;
		String url = "jdbc:sqlite:szamitogep.db";
		try {
			conn = DriverManager.getConnection(url);
			// SM("Sikeres kapcsolódás");
			return conn;
		} catch (Exception ex) {
			SM(ex.getMessage());
			return conn;
		}
	}

	public void DisConnect(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				SM("Sikeres lekapcsolódás");
			} catch (Exception ex) {
				SM(ex.getMessage());
			}
		}
	}

	public void Reg() {
		try {
			Class.forName("org.sqlite.JDBC");
			SM("Sikeres driver regisztrálás");
		} catch (Exception ex) {
			SM(ex.getMessage());
		}
	}

	public void SM(String s) {
		System.out.println(s);
	}

	public int login() {
		System.out.println("Adja meg a felhasználó nevét:");
		final String[] usernames = { "attila", "tanar" };
		String user = scanner.nextLine();
		for (int i = 0; i < usernames.length; i++) {
			if (new String(usernames[i]).equals(user)) {
				String[] password = { "alma", "kutya" };
				System.out.println("Jelszó:");
				final String pass = scanner.nextLine();
				if (new String(password[i]).equals(pass)) {
					System.out.println("Sikeres bejelentkezés!");

					return 1;
				}
			}
		}
		System.out.println("Sikertelen belépés!");

		return 0;
	}

	public String[] getTableNames() {
		List<String> tableNames = new ArrayList<>();
		try (Connection conn = Connect()) {
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet rs = metaData.getTables(null, null, "%", null);
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				if (!tableName.contains("sqlite")) {
					tableNames.add(rs.getString("TABLE_NAME"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tableNames.toArray(new String[0]);
	}

	public String[] getTableColumnNames(String tableName) {
		List<String> columnNames = new ArrayList<>();
		try (Connection conn = Connect()) {
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet rs = metaData.getColumns(null, null, tableName, null);
			while (rs.next()) {
				columnNames.add(rs.getString("COLUMN_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return columnNames.toArray(new String[0]);
	}

	public void insertDataToTable() {
		try (Connection conn = Connect()) {
			String tableName = "";

			// lekérjük az összes tábla nevét
			String[] tableNames = getTableNames();
			System.out.println("Elérhető táblák:");
			for (String tableName1 : tableNames) {
				System.out.println(tableName1);
			}

			// addig kérdezzük a felhasználót, míg nem ad meg egy létező tábla nevet
			while (tableName.equals("")) {
				System.out.println("Melyik táblába szeretnél új adatot felvinni?");
				tableName = scanner.nextLine();
				if (!Arrays.asList(tableNames).contains(tableName)) {
					System.out.println("A megadott tábla nem létezik. Kérlek, adj meg egy létező tábla nevet.");
					tableName = "";
				}
			}

			// lekérjük az adott tábla mezőinek nevét
			String[] columnNames = getTableColumnNames(tableName);

			// bekérjük a felhasználótól az új adatokat
			String[] values = new String[columnNames.length];
			for (int i = 0; i < columnNames.length; i++) {
				System.out.println(columnNames[i] + ": ");
				String value = scanner.nextLine();
				values[i] = "'" + value + "'";
			}

			// összeállítjuk az SQL utasítást és végrehajtjuk
			String sql = "INSERT INTO " + tableName + " (" + String.join(", ", columnNames) + ") VALUES ("
					+ String.join(", ", values) + ")";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			int rows = pstmt.executeUpdate();
			System.out.println(rows + " sor hozzáadva a táblához: " + tableName);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void deleteData() {
		String[] tableNames = getTableNames();
		System.out.println("Elérhető táblák:");
		for (String tableName : tableNames) {
			System.out.println(tableName);
		}
		System.out.println("Melyik táblából szeretne adatot törölni? Adja meg a tábla nevét:");
		String tableName = scanner.nextLine();
		if (!Arrays.asList(tableNames).contains(tableName)) {
			System.out.println("Hiba: a megadott tábla nem létezik.");

			return;
		}
		String[] columnNames = getTableColumnNames(tableName);
		System.out.println("A(z) " + tableName + " tábla elérhető mezői:");
		for (String columnName : columnNames) {
			System.out.println(columnName);
		}
		System.out.println("Kérem adja meg azon mező nevét, amely alapján törölni szeretne:");
		String columnName = scanner.nextLine();
		if (!Arrays.asList(columnNames).contains(columnName)) {
			System.out.println("Hiba: a megadott mező nem létezik a(z) " + tableName + " táblában.");

			return;
		}
		System.out.println("Kérem adja meg azon mező értékét, amely alapján törölni szeretne:");
		String columnValue = scanner.nextLine();

		try {
			Connection conn = Connect();
			Statement stmt = conn.createStatement();
			String sql = "DELETE FROM " + tableName + " WHERE " + columnName + "='" + columnValue + "';";
			stmt.executeUpdate(sql);
			SM("Adatok törölve: " + sql);
		} catch (Exception ex) {
			SM(ex.getMessage());
		}
	}

	public String[] getTableData(String tableName) {
		List<String> records = new ArrayList<>();
		try (Connection conn = Connect()) {
			Statement statement = conn.createStatement();
			String query = "SELECT * FROM " + tableName;
			ResultSet rs = statement.executeQuery(query);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			// Adding column names to the list
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i <= columnCount; i++) {
				sb.append(metaData.getColumnName(i)).append("\t");
			}
			records.add(sb.toString());

			// Adding record data to the list
			while (rs.next()) {
				sb = new StringBuilder();
				for (int i = 1; i <= columnCount; i++) {
					sb.append(rs.getString(i)).append("\t");
				}
				records.add(sb.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return records.toArray(new String[0]);
	}

	public void exportTableDataToTxt(String tableName) {
		try (Connection conn = Connect()) {
			// String[] columns = getTableColumnNames(tableName);
			String[] rows = getTableData(tableName);

			// ha van tartalom
			if (rows.length > 1) {
				// adatok mentése a számítógépre
				String fileName = tableName + ".txt";
				BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
				// fejléc kiírása
				// writer.write(String.join(",", columns));
				// writer.newLine();
				// adatsorok kiírása
				for (String row : rows) {
					writer.write(row);
					writer.newLine();
				}
				writer.close();
				SM("Az adatokat sikeresen elmentettük a " + fileName + " fájlba.");
			} else {
				SM("A " + tableName + " tábla üres.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			SM("Hiba történt az adatok mentése során: " + ex.getMessage());
		}
	}

	public void exportAllTableDataToTxt() {
		String[] tableNames = getTableNames();
		for (String tableName : tableNames) {
			exportTableDataToTxt(tableName);
		}
	}

	public void modifyComputer() {
		try (Connection conn = Connect()) {
			// Felhasználói input bekérése
			System.out.print("Kérem a módosítandó számítógép nevét: ");
			String name = scanner.nextLine().trim();
			System.out.print("Kérem az új darabszámot: ");
			int newQuantity = Integer.parseInt(scanner.nextLine().trim());

			// Tranzakció kezelése
			conn.setAutoCommit(false);

			try {
				// SQL query előkészítése és paraméterek hozzáadása
				String sql = "UPDATE SZAMITOGEP SET Darab = ? WHERE Nev = ?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, newQuantity);
				pstmt.setString(2, name);

				// Lekérdezés futtatása és eredmények feldolgozása
				int rowsAffected = pstmt.executeUpdate();
				if (rowsAffected == 0) {
					SM("Nincs olyan számítógép a rendszerben, amelynek a neve " + name + " lenne.");
				} else {
					SM(rowsAffected + " sor módosítva.");
				}

				// Tranzakció commitolása
				conn.commit();
			} catch (SQLException e) {
				// Tranzakció rollback és hibaüzenet kiírása
				conn.rollback();
				SM("Hiba történt az adatbáziskapcsolat során: " + e.getMessage());
			}

		} catch (SQLException e) {
			SM("Hiba történt az adatbáziskapcsolat során: " + e.getMessage());
		}
	}

	public void getTableDataByField() {
		try (Connection conn = Connect()) {
			// Felhasználói input bekérése
			String[] tableNames = getTableNames();
			System.out.println("Elérhető táblák:");
			for (String tableName : tableNames) {
				System.out.println(tableName);
			}
			System.out.print("Melyik táblából szeretne lekérdezni? ");
			String tableName = scanner.nextLine().trim();
			String[] columnNames = getTableColumnNames(tableName);
			System.out.println("A(z) " + tableName + " tábla elérhető mezői:");
			for (String columnName : columnNames) {
				System.out.println(columnName);
			}
			System.out.print("Melyik mező szerint szeretne keresni? ");
			String columnName = scanner.nextLine().trim();
			System.out.print("Kérem a keresési értéket: ");
			String columnValue = scanner.nextLine().trim();

			// SQL query előkészítése és paraméterek hozzáadása
			String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, columnValue);

			// Lekérdezés futtatása és eredmények feldolgozása
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			int count = 0;
			while (rs.next()) {
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i <= columnCount; i++) {
					sb.append(metaData.getColumnName(i)).append(": ").append(rs.getString(i)).append("\t");
				}
				count++;
				SM(sb.toString());
			}
			if (count == 0)
				System.out.println("Nincs ilyen rekord.");

		} catch (SQLException e) {
			SM("Hiba történt az adatbáziskapcsolat során: " + e.getMessage());
		}
	}

	public void getTableDataByFields() {
		try (Connection conn = Connect()) {
			// Felhasználói input kérdezése
			String[] tableNames = getTableNames();
			System.out.println("Elérhető táblák:");
			for (String tableName : tableNames) {
				System.out.println(tableName);
			}
			System.out.println("Kérlek add meg a tábla nevét:");
			String tableName = scanner.nextLine();
			String[] columnNames = getTableColumnNames(tableName);
			System.out.println("A(z) " + tableName + " tábla elérhető mezői:");
			for (String columnName : columnNames) {
				System.out.println(columnName);
			}
			System.out.println("Kérlek add meg a mezők nevét, vesszővel elválasztva:");
			String columns = scanner.nextLine();

			// Lekérdezés összeállítása
			String[] columnArr = columns.split(",");
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < columnArr.length; i++) {
				if (i > 0) {
					sb.append(" AND ");
				}
				sb.append(columnArr[i]).append(" = ?");
			}
			String query = "SELECT * FROM " + tableName + " WHERE " + sb.toString();

			// Prepared statement előkészítése és értékek beállítása
			PreparedStatement ps = conn.prepareStatement(query);
			for (int i = 0; i < columnArr.length; i++) {
				System.out.println("Kérlek add meg a(z) " + columnArr[i] + " mező értékét:");
				String value = scanner.nextLine();
				ps.setString(i + 1, value);
			}

			// Lekérdezés végrehajtása és eredmények kiírása
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			int count = 0;
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					System.out.print(metaData.getColumnName(i) + ": " + rs.getString(i) + "\t");
				}
				count++;
				System.out.println();
			}
			if (count == 0)
				System.out.println("Nincs ilyen rekord.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getPurchaseInfoByBuyerName() {
		try (Connection conn = Connect()) {
			System.out.print("Kérem a vásárló nevét: ");
			String buyerName = scanner.nextLine().trim();
			String sql = "SELECT VNEV, SZAMITOGEP.NEV, DATUM " + "FROM VASARLO " + "JOIN VASARLAS_KAPCSOLO USING(VID) "
					+ "JOIN SZAMITOGEP USING(SZID) " + "WHERE VNEV = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, buyerName);

			ResultSet rs = pstmt.executeQuery();
			int count = 0;
			while (rs.next()) {
				String vnev = rs.getString("VNEV");
				String szamitogepNev = rs.getString("NEV");
				String datum = rs.getString("DATUM");
				count++;
				System.out.println(vnev + " " + szamitogepNev + " " + datum);
			}
			if (count == 0)
				System.out.println("Nincs ilyen rekord.");

		} catch (SQLException e) {
			System.out.println("Hiba történt az adatbáziskapcsolat során: " + e.getMessage());
		}
	}

	public void getUser() {

		try (Connection conn = Connect()) {
			System.out.print("Kérem a bank nevét: ");
			String bank = scanner.nextLine().trim();
			System.out.print("Kérem a telefonszámot: ");
			String telefonSzam = scanner.nextLine().trim();

			String sql = "SELECT VNEV FROM VASARLO JOIN BANKKARTYA USING(VID) JOIN VASARLO_TELEFONSZAM USING(VID) WHERE BANK=? AND TELEFONSZAM=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bank);
			pstmt.setString(2, telefonSzam);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println("A keresett vásárló neve: " + rs.getString("VNEV"));
			} else {
				System.out.println("Nincs találat a megadott kritériumokra.");
			}

		} catch (SQLException e) {
			SM("Hiba történt az adatbáziskapcsolat során: " + e.getMessage());
		}
	}

}
