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
		SM("Menü");
		SM("================================================================");
		SM("0. Kilépés ");
		SM("1. Random Feltölt ");
		SM("2. Fileból feltölt ");
		SM("3. Kézi adatfelvitel");
		SM("4. Adatok törlése");
		SM("5. Módosítás");
		SM("6. Adott napi esetszám");
		SM("7. Idõtartam esetszám");
		String ms = ReadData("Add meg a választott menü számát: ");
		int m = -1;
		try {
			m = Integer.parseInt(ms);
		} catch (NumberFormatException e) {
			System.out.println(ms + "Nem szám.");
		}
		switch (m) {
		case 0:
			SM("================================================================");
			SM("A program leállt! ");
			System.exit(0);
			break;
		case 1:
			SM("================================================================");
			SM("Random Feltölt");
			dbm.feltolt_eset();
			dbm.feltolt_mentoegyseg();
			dbm.feltolt_mentes();
			break;
		case 2:
			SM("================================================================");
			SM("Fileból feltölt");
			dbm.all_feltolt_eset(ReadData("Add meg az esetfile elérési utat: "));
			dbm.all_feltolt_mentoegyseg(ReadData("Add meg az mentoegysegfile elérési utat: "));
			dbm.all_feltolt_mentes(ReadData("Add meg az mentesfile elérési utat: "));
			break;
		case 3:
			SM("================================================================");
			SM("Kézi adatfelvitel");
			Date currentDate = new Date(System.currentTimeMillis());
			dbm.man_felvetel_eset(ReadData("Hívó: "),ReadData("Telefonszam: "), ReadData("Cím: "),currentDate);
			dbm.man_felvetel_mentoegyseg(ReadData("Rendszam: "),ReadData("Tipus: "), ReadData("Állapot: "));
			dbm.man_felvetel_mentes(ReadInt("EsetId:"),ReadInt("MentõegységId:"), currentDate, ReadData("Állapot: "));
			break;
		case 4:
			SM("================================================================");
			SM("Adatok törlése");
			dbm.torles_eset(ReadInt("Törlendõ elem idje:"));
			dbm.torles_mentoegyseg(ReadInt("Törlendõ elem idje:"));
			dbm.torles_mentes(ReadInt("Törlendõ elem idje:"));
			break;
		case 5:
			SM("================================================================");
			SM("Módosítás");
			dbm.modositas_eset(ReadInt("EsetId:"), ReadData("Hívó: "), ReadData("Telefonszám: "), ReadData("Cím: "), ReadDate("Dátum:"));
			dbm.modositas_mentoegyseg(ReadInt("MentõegységId:"), ReadData("Rendszám: "), ReadData("Típus: "), ReadData("Állapot: "));
			dbm.modositas_mentes(ReadInt("MentésId:"), ReadInt("EsetId:"), ReadInt("MentõegységId:"), ReadDate("Dátum:"), ReadData("Állapot: "));
			break;
		case 6:
			SM("================================================================");
			SM("Adott napi esetszám");
			int num=dbm.esetek_szama(ReadDate("Adott nap:"));
			System.out.println(num);
			break;
		case 7:
			SM("================================================================");
			SM("Idõtartam esetszám");
			num=dbm.esetek_szama_szures(ReadData("Hívó: "), ReadDate("Kezdõ Dátum:"), ReadDate("Vég Dátum:")) ;
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
