package plsql;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;


public class Program {
	Scanner scanner = new Scanner(System.in);
	static Db dbm = new Db("jdbc:oracle:thin:@localhost:1521/", "demo", "demo", "yyyy.MM.dd. HH:mm");

	public void main(String[] args) {
		menu();
	}

	void menu() {
		SM("\n");
		SM("Men�");
		SM("================================================================");
		SM("0. Kil�p�s ");
		SM("1. Random Felt�lt ");
		SM("2. Fileb�l felt�lt ");
		SM("3. K�zi adatfelvitel");
		SM("4. Adatok t�rl�se");
		SM("5. M�dos�t�s");
		SM("6. Adott napi esetsz�m");
		SM("7. Id�tartam esetsz�m");
		String ms = ReadData("Add meg a v�lasztott men� sz�m�t: ");
		int m = -1;
		try {
			m = Integer.parseInt(ms);
		} catch (NumberFormatException e) {
			System.out.println(ms + "Nem sz�m.");
		}
		switch (m) {
		case 0:
			SM("================================================================");
			SM("A program le�llt! ");
			System.exit(0);
			break;
		case 1:
			SM("================================================================");
			SM("Random Felt�lt");
			dbm.feltolt_eset();
			dbm.feltolt_mentoegyseg();
			dbm.feltolt_mentes();
			break;
		case 2:
			SM("================================================================");
			SM("Fileb�l felt�lt");
			dbm.all_feltolt_eset(ReadData("Add meg az esetfile el�r�si utat: "));
			dbm.all_feltolt_mentoegyseg(ReadData("Add meg az mentoegysegfile el�r�si utat: "));
			dbm.all_feltolt_mentes(ReadData("Add meg az mentesfile el�r�si utat: "));
			break;
		case 3:
			SM("================================================================");
			SM("K�zi adatfelvitel");
			Date currentDate = new Date(System.currentTimeMillis());
			dbm.man_felvetel_eset(ReadData("H�v�: "),ReadData("Telefonszam: "), ReadData("C�m: "),currentDate);
			dbm.man_felvetel_mentoegyseg(ReadData("Rendszam: "),ReadData("Tipus: "), ReadData("�llapot: "));
			dbm.man_felvetel_mentes(ReadInt("EsetId:"),ReadInt("Ment�egys�gId:"), currentDate, ReadData("�llapot: "));
			break;
		case 4:
			SM("================================================================");
			SM("Adatok t�rl�se");
			dbm.torles_eset(ReadInt("T�rlend� elem idje:"));
			dbm.torles_mentoegyseg(ReadInt("T�rlend� elem idje:"));
			dbm.torles_mentes(ReadInt("T�rlend� elem idje:"));
			break;
		case 5:
			SM("================================================================");
			SM("M�dos�t�s");
			dbm.modositas_eset(ReadInt("EsetId:"), ReadData("H�v�: "), ReadData("Telefonsz�m: "), ReadData("C�m: "), ReadDate("D�tum:"));
			dbm.modositas_mentoegyseg(ReadInt("Ment�egys�gId:"), ReadData("Rendsz�m: "), ReadData("T�pus: "), ReadData("�llapot: "));
			dbm.modositas_mentes(ReadInt("Ment�sId:"), ReadInt("EsetId:"), ReadInt("Ment�egys�gId:"), ReadDate("D�tum:"), ReadData("�llapot: "));
			break;
		case 6:
			SM("================================================================");
			SM("Adott napi esetsz�m");
			int num=dbm.esetek_szama(ReadDate("Adott nap:"));
			System.out.println(num);
			break;
		case 7:
			SM("================================================================");
			SM("Id�tartam esetsz�m");
			num=dbm.esetek_szama_szures(ReadData("H�v�: "), ReadDate("Kezd� D�tum:"), ReadDate("V�g D�tum:")) ;
			System.out.println(num);
			break;
		}
	}

	public void SM(String s) {
		System.out.println(s);
	}

	public String ReadData(String s) {
		String data = "";
		System.out.println(s);
		data = scanner.nextLine();
		return data;
	}

	public int ReadInt(String prompt) {
		System.out.print(prompt);
		int data = scanner.nextInt();
		return data;
	}

	public Date ReadDate(String prompt) {
		System.out.print(prompt);
		String input = scanner.nextLine();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;

		try {
			date = (Date) dateFormat.parse(input);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}
}
