package io.github.mdillon95.scoutingpc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;

public class LoginDialog {
	private String firebaseUrl = "https://scouting-4c5e1.firebaseio.com/";
	
	ScoutingFrame sf;
	
	private FileInputStream serviceAccount;
	
	public LoginDialog() {
		Log.i(LoginDialog.class, "Authenticating...");
		try {
			serviceAccount = new FileInputStream("auth/serviceAccount.json");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		FirebaseOptions options = new FirebaseOptions.Builder().setCredential(FirebaseCredentials.fromCertificate(serviceAccount)).setDatabaseUrl(firebaseUrl).build();
		FirebaseApp app = FirebaseApp.initializeApp(options);
		Log.i(LoginDialog.class, "App " + app.getName() + " initialized.");
		Log.i(LoginDialog.class, "App initialized with URL " + firebaseUrl);
		Log.i(LoginDialog.class, "Created ScoutingFrame");		
		sf = new ScoutingFrame();
	}

}
