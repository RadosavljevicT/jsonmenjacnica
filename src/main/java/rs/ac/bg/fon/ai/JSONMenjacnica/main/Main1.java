package rs.ac.bg.fon.ai.JSONMenjacnica.main;


import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.GregorianCalendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import rs.ac.bg.fon.ai.JSONMenjacnica.Transakcija;

public class Main1 {

	
	private static final String BASE_URL ="http://api.currencylayer.com";
	private static final String API_KEY ="df16d11039ba38a999ce00721596ae99";
	private static final String SOURCE = "USD";
	private static final String CURRENCIES = "CAD";
	
	public static void main(String[] args) {
		
		Transakcija t = new Transakcija();
		
		t.setPocetniIznos(328);
		t.setIzvornaValuta(SOURCE);
		t.setKrajnjaValuta(CURRENCIES);
		
		GregorianCalendar calendar = new GregorianCalendar();
		t.setDatumTransakcije(calendar.getTime());
		
		try {
			Gson gson = new Gson();
			
			URL url = new URL (BASE_URL + "/live?access_key=" + API_KEY +  "&source=" + SOURCE + "&currencies=" + CURRENCIES);
			
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			JsonObject rez = gson.fromJson(reader, JsonObject.class);
			System.out.println(rez);
			
			if (rez.get("success").getAsBoolean()) {
			
				double kurs = rez.get("quotes").getAsJsonObject().get("USDCAD").getAsDouble();
				double resenje =kurs * t.getPocetniIznos();
				
				t.setKonvertovaniIznos(resenje);
				
		
				
					try(FileWriter file = new FileWriter("prva_transakcija.json")) {
						
						

						
						Gson gson1 = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
						gson1.toJson(t, file);
						
					} catch (Exception e) {
						
						e.printStackTrace();
					}
			
			}
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		
		
	}

}
