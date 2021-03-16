package Krishagni.CPR.ApiCaller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class CprApiCaller {

	public static String usernamePassword = "admin:Login@123";
	public static final String API_URL = "http://localhost:8080/openspecimen/rest/ng/collection-protocol-registrations/";
	public static String basicUrlAuthentication = "Basic "
			+ Base64.getEncoder().encodeToString(usernamePassword.getBytes());
	public static URL serverUrl = null;
	public static HttpURLConnection urlConnection = null;
	public static BufferedReader httpResponseReader = null;

	public static void main(String args[]) throws IOException {
		serverUrl = new URL(API_URL);
		urlConnection = (HttpURLConnection) serverUrl.openConnection();
		urlConnection.addRequestProperty("Authorization", basicUrlAuthentication);
		methodsCaller();
	}

	private static void methodsCaller() throws IOException {
		String REGISTRATION_PAYLOAD = RegistrationPaylaod.getPaylaod();
		registerNewParticipant(REGISTRATION_PAYLOAD);
	}

	private static void registerNewParticipant(String POST_PARAMS) throws IOException {
		urlConnection = (HttpURLConnection) serverUrl.openConnection();
		urlConnection.setRequestMethod("POST");
		urlConnection.addRequestProperty("Authorization", basicUrlAuthentication);

		urlConnection.setRequestProperty("Content-Type", "application/json");
		urlConnection.setDoOutput(true);
		OutputStream outputStream = urlConnection.getOutputStream();
		outputStream.write(POST_PARAMS.getBytes());
		int responseCode = urlConnection.getResponseCode();
		System.out.println("\nPOST Response Code :  " + responseCode);
		
		if (responseCode == HttpURLConnection.HTTP_OK) {
			httpResponseReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			StringBuffer response = new StringBuffer();
			String readLine;
			while ((readLine = httpResponseReader.readLine()) != null) {
				response.append(readLine);
			}
			httpResponseReader.close();
			System.out.println("\nNew Created User is:" + response.toString());
		} else {
			System.err.println("\n Unable to create CPR on the Server: CHECK os.log");
		}
	}

}
