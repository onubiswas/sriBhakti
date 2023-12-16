package co.sribhakti.api;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class DatabaseProvider {

    @Bean(name="firebase-firestore")
    public static Firestore providerFirestore() throws IOException {
        InputStream serviceAccount = new FileInputStream("/Users/onu/projects/myProject/springProject/api/secrets/sribhakti-firebase-secret.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId("sribhakti-d3c9f")
                .setCredentials(credentials)
                .build();

        FirebaseApp.initializeApp(options);
        return FirestoreClient.getFirestore();
    }

}
