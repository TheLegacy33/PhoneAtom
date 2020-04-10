package net.devatom.phoneAtom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	private final String TAG = "PhoneAtom";

	private TextView ttField;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ttField = (TextView) findViewById(R.id.textView);
		ttField.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getFirestoreContent();
			}
		});
	}

	private void getFirestoreContent(){
		FirebaseFirestore db = FirebaseFirestore.getInstance();

		CollectionReference collRef = db.collection("contacts");

		db.collection("contacts")
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()){
							ttField.setText("");
							for (QueryDocumentSnapshot document : task.getResult()) {
								Log.d(TAG, document.getId() + "=>" + document.getData());
								Date dte = document.getTimestamp("date_naissance").toDate();
								StringBuilder sb = new StringBuilder("")
									.append(document.getString("nom"))
									.append("\n")
									.append(document.getString("prenom"))
									.append("\n")
									.append(new SimpleDateFormat("dd/MM/yyyy").format(dte));

								ttField.setText(sb.toString());
							}
						} else {
							Log.w(TAG, "Error getting documents.", task.getException());
						}
					}
				});
	}
}
