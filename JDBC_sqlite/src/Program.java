
public class Program {
	static DbMethods dbm = new DbMethods();

	public static void main(String[] args) {
		if (dbm.login() == 1) {
			dbm.Reg();
			while (1 != 0) {
				menu();

			}

		}
		dbm.closeScanner();

	}

	static void menu() {
		dbm.SM("\n");
		dbm.SM("Men�");
		dbm.SM("================================================================");
		dbm.SM("0. Kil�p�s ");
		dbm.SM("1. Besz�r�s ");
		dbm.SM("2. T�rl�s ");
		dbm.SM("3. Ki�r�s Tx_be ");
		dbm.SM("4. List�z�s egy mez� szerint");
		dbm.SM("5. List�z�s t�bb mez� szerint");
		dbm.SM("6. A v�s�rl� �ltal v�s�rolt g�pek list�ja");
		dbm.SM("7. A v�s�rl� nev�nek lek�rdez�se Bank n�v �s telefonsz�m alapj�n");
		dbm.SM("8. M�dos�tja a sz�m�t�g�pek darabsz�m�t");
		String ms = dbm.ReadData("Add meg a v�lasztott men� sz�m�t: ");
		int m = -1;
		try {
			m = Integer.parseInt(ms);
		} catch (NumberFormatException e) {
			System.out.println(ms + "Nem sz�m.");
		}
		switch (m) {
		case 0:
			dbm.SM("================================================================");
			dbm.SM("A program le�llt! ");
			dbm.DisConnect(dbm.Connect());
			System.exit(0);
			break;
		case 1:
			dbm.SM("================================================================");
			dbm.SM("Besz�r�s v�grehajt�sa");
			dbm.insertDataToTable();
			break;
		case 2:
			dbm.SM("================================================================");
			dbm.SM("T�rl�s v�grehajt�sa");
			dbm.deleteData();
			break;
		case 3:
			dbm.SM("================================================================");
			dbm.SM("Ki�r�s Txt_be");
			dbm.exportAllTableDataToTxt();
			break;
		case 4:
			dbm.SM("================================================================");
			dbm.SM("List�z�s egy mez� szerint");
			dbm.getTableDataByField();
			break;
		case 5:
			dbm.SM("================================================================");
			dbm.SM("List�z�s t�bb mez� szerint");
			dbm.getTableDataByFields();
			break;
		case 6:
			dbm.SM("================================================================");
			dbm.SM("A v�s�rl� �ltal v�s�rolt g�pek list�ja");
			dbm.getPurchaseInfoByBuyerName();
			break;
		case 7:
			dbm.SM("================================================================");
			dbm.SM("A v�s�rl� nev�nek lek�rdez�se Bank n�v �s telefonsz�m alapj�n");
			dbm.getUser();
			break;
		case 8:
			dbm.SM("================================================================");
			dbm.SM("M�dos�tja a sz�m�t�g�pek darabsz�m�t");
			dbm.modifyComputer();
			break;
		}
	}

}
