
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
		dbm.SM("Menü");
		dbm.SM("================================================================");
		dbm.SM("0. Kilépés ");
		dbm.SM("1. Beszúrás ");
		dbm.SM("2. Törlés ");
		dbm.SM("3. Kiírás Tx_be ");
		dbm.SM("4. Listázás egy mezõ szerint");
		dbm.SM("5. Listázás több mezõ szerint");
		dbm.SM("6. A vásárló által vásárolt gépek listája");
		dbm.SM("7. A vásárló nevének lekérdezése Bank név és telefonszám alapján");
		dbm.SM("8. Módosítja a számítógépek darabszámát");
		String ms = dbm.ReadData("Add meg a választott menü számát: ");
		int m = -1;
		try {
			m = Integer.parseInt(ms);
		} catch (NumberFormatException e) {
			System.out.println(ms + "Nem szám.");
		}
		switch (m) {
		case 0:
			dbm.SM("================================================================");
			dbm.SM("A program leállt! ");
			dbm.DisConnect(dbm.Connect());
			System.exit(0);
			break;
		case 1:
			dbm.SM("================================================================");
			dbm.SM("Beszúrás végrehajtása");
			dbm.insertDataToTable();
			break;
		case 2:
			dbm.SM("================================================================");
			dbm.SM("Törlés végrehajtása");
			dbm.deleteData();
			break;
		case 3:
			dbm.SM("================================================================");
			dbm.SM("Kiírás Txt_be");
			dbm.exportAllTableDataToTxt();
			break;
		case 4:
			dbm.SM("================================================================");
			dbm.SM("Listázás egy mezõ szerint");
			dbm.getTableDataByField();
			break;
		case 5:
			dbm.SM("================================================================");
			dbm.SM("Listázás több mezõ szerint");
			dbm.getTableDataByFields();
			break;
		case 6:
			dbm.SM("================================================================");
			dbm.SM("A vásárló által vásárolt gépek listája");
			dbm.getPurchaseInfoByBuyerName();
			break;
		case 7:
			dbm.SM("================================================================");
			dbm.SM("A vásárló nevének lekérdezése Bank név és telefonszám alapján");
			dbm.getUser();
			break;
		case 8:
			dbm.SM("================================================================");
			dbm.SM("Módosítja a számítógépek darabszámát");
			dbm.modifyComputer();
			break;
		}
	}

}
