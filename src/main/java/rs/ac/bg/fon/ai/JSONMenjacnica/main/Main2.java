package rs.ac.bg.fon.ai.JSONMenjacnica.main;


import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import rs.ac.bg.fon.ai.JSONMenjacnica.Transakcija;

public class Main2 {
	
	private static final String BASE_URL ="http://api.currencylayer.com/historical";
	private static final String API_KEY ="df16d11039ba38a999ce00721596ae99";
	private static final String DATE = "2020-12-10";
	


	public static void main(String[] args) {
		
		try {
			Gson gson = new Gson();
			
			URL url = new URL (BASE_URL + "?access_key=" + API_KEY +  "&date=" + DATE);
			
			System.out.println(url);
			
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			JsonObject rez = gson.fromJson(reader, JsonObject.class);
			
			System.out.println(rez);
			
			if (rez.get("success").getAsBoolean()) {
				
				double kursKanadskiDolari = rez.get("quotes").getAsJsonObject().get("USDCAD").getAsDouble();
				double kursEvri = rez.get("quotes").getAsJsonObject().get("USDEUR").getAsDouble();
				double kursSvajcarskiFranak = rez.get("quotes").getAsJsonObject().get("USDCHF").getAsDouble();
				
				Transakcija t1 = new Transakcija();
				String sDate1="10/12/2020";
				Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
				
				t1.setDatumTransakcije(date1);
				t1.setIzvornaValuta("USD");
				t1.setKrajnjaValuta("EUR");
				t1.setPocetniIznos(100);
				
				double r1 = kursEvri * t1.getPocetniIznos();
				t1.setKonvertovaniIznos(r1);
				
				Transakcija t2 = new Transakcija();
				t2.setDatumTransakcije(date1);
				t2.setIzvornaValuta("USD");
				t2.setKrajnjaValuta("CAD");
				t2.setPocetniIznos(100);
				
				double r2 = kursKanadskiDolari * t2.getPocetniIznos();
			
				t2.setKonvertovaniIznos(r2);
				
				Transakcija t3 = new Transakcija();
				t3.setDatumTransakcije(date1);
				t3.setIzvornaValuta("USD");
				t3.setKrajnjaValuta("CHF");
				t3.setPocetniIznos(100);
				
				double r3 = kursSvajcarskiFranak * t3.getPocetniIznos();
				t3.setKonvertovaniIznos(r3);
				
				List<Transakcija> transakcije = new LinkedList<Transakcija>();
				transakcije.add(t1);
				transakcije.add(t2);
				transakcije.add(t3);
				
				gson = new GsonBuilder().setPrettyPrinting().create();
				
				try (FileWriter file = new FileWriter("ostale_transakcije.json")){
					
					gson.toJson(transakcije, file);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
				
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
