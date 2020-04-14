package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore objectFirebaseFirestore;
    private static String hospital_details = "hospital";
    private Dialog objectDialog;

    private CollectionReference collectionReferenceObj;

    private EditText documentIDET, noOfRoomsET, nameOfHospitalET, locationOfHospitalET;
    private TextView collectionValuesTV;
    private TextView collectionValuesTV1;

    private DocumentReference objectDocumnetReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            objectFirebaseFirestore = FirebaseFirestore.getInstance();
            objectDialog = new Dialog(this);

            objectDialog.setContentView(R.layout.please_wait_dailog_file);
            objectDialog.setCancelable(false);

            documentIDET = findViewById(R.id.documentIDET);
            noOfRoomsET = findViewById(R.id.noOfRoomsET);

            nameOfHospitalET = findViewById(R.id.nameOfHospitalET);
            locationOfHospitalET = findViewById(R.id.locationOfHospitalET);

            collectionValuesTV = findViewById(R.id.collectionValuesTV);
            collectionValuesTV1=findViewById(R.id.collectionValuesTV1);
        } catch (Exception e) {
            Toast.makeText(this, "onCreate:" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public void addValuesToFireStore(View v) {

        try {
            if (!documentIDET.getText().toString().isEmpty()
                    && !noOfRoomsET.getText().toString().isEmpty()
                    && !nameOfHospitalET.getText().toString().isEmpty()
                    && !locationOfHospitalET.getText().toString().isEmpty()) {
                objectDialog.show();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("noofrooms", noOfRoomsET.getText().toString());
                objectMap.put("nameofhospital", nameOfHospitalET.getText().toString());
                objectMap.put("locationofhosptal", locationOfHospitalET.getText().toString());
                objectFirebaseFirestore.collection(hospital_details)
                        .document(documentIDET.getText().toString())
                        .set(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();

                                documentIDET.setText("");
                                noOfRoomsET.setText("");

                                nameOfHospitalET.setText("");
                                locationOfHospitalET.setText("");

                                documentIDET.requestFocus();

                                Toast.makeText(MainActivity.this, "Data added Sussesfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        objectDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Fail To ADD Data", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(MainActivity.this, "Fill All Fields", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            objectDialog.dismiss();
            Toast.makeText(this, "addValuuesToFireStore:" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public void getValuesFromCollection(View v) {
        try {

            if (!documentIDET.getText().toString().isEmpty()) {
                objectDialog.show();
                objectDocumnetReference = objectFirebaseFirestore.collection(hospital_details)
                        .document(documentIDET.getText().toString());
                objectDocumnetReference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                objectDialog.dismiss();
                                if (!documentSnapshot.exists()) {
                                    Toast.makeText(MainActivity.this, "No Value retrieve", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                collectionValuesTV.setText("");

                                String noOFRooms = documentSnapshot.getString("noofrooms");
                                String nameOfHospital = documentSnapshot.getString("nameofhospital");
                                String locationOfHospital = documentSnapshot.getString("locationofhospital");

                                String documentid = documentSnapshot.getId();

                                String completeDocument = "Hospital Name:" + documentid + "\n" +
                                        "No Of Rooms:" + noOFRooms + "\n" +
                                        "Name Of Hospital:" + nameOfHospital + "\n" +
                                        "Location of Hospital:" + locationOfHospital;

                                collectionValuesTV.setText(completeDocument);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        objectDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Fail To Retrieve the data" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "enter valid document id:", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            objectDialog.dismiss();
            Toast.makeText(this, "getValuesFromCollection:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateValue(View v) {
        try {
            if(!documentIDET.getText().toString().isEmpty()&&!noOfRoomsET.getText().toString().isEmpty()){

            objectDialog.show();
            Map<String,Object> objectMap=new HashMap<>();
            objectMap.put("noofrooms",noOfRoomsET.getText().toString());
            objectDocumnetReference = objectFirebaseFirestore.collection(hospital_details)
                    .document(documentIDET.getText().toString());
            objectDocumnetReference.update(objectMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            objectDialog.dismiss();
                            Toast.makeText(MainActivity.this, "data updated success:", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    objectDialog.dismiss();
                    Toast.makeText(MainActivity.this, "fail to updated data:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            }
            else {
                Toast.makeText(MainActivity.this, "fill all fields:"
                        , Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

            Toast.makeText(this, "updateValue:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteValue(View v) {
        try {
            if(!documentIDET.getText().toString().isEmpty()){

                objectDialog.show();
                Map<String,Object> objectMap=new HashMap<>();
                objectMap.put("noofrooms", FieldValue.delete());
                objectDocumnetReference = objectFirebaseFirestore.collection(hospital_details)
                        .document(documentIDET.getText().toString());
                objectDocumnetReference.update(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "data deleted success:", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        objectDialog.dismiss();
                        Toast.makeText(MainActivity.this, "fail to delete data:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(MainActivity.this, "fill all fields:"
                        , Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

            Toast.makeText(this, "delete Value:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void documentdelete(View v)
    {try {
        if(!documentIDET.getText().toString().isEmpty()) {

            objectDialog.show();
            objectFirebaseFirestore.collection(hospital_details).document(documentIDET.getText().toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    objectDialog.dismiss();
                    Toast.makeText(MainActivity.this, "document deleted success:", Toast.LENGTH_SHORT).show();
                }
            });
        }else
        {
            Toast.makeText(MainActivity.this, "fill the field first:"
                    , Toast.LENGTH_SHORT).show();

        }
    }catch (Exception e) {
        Toast.makeText(this, "delete process is not complete" + e.getMessage(), Toast.LENGTH_SHORT).show();

    }
    }
    public void GetValues(View v){
        try{
            collectionReferenceObj=objectFirebaseFirestore.collection(hospital_details);
            collectionReferenceObj.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if(e!=null){
                        return;
                    }
                    else {
                        String collection="";
                        for(QueryDocumentSnapshot queryDocumentSnapshotObj:queryDocumentSnapshots) {
                        String documentId=queryDocumentSnapshotObj.getId();
                        String rooms=queryDocumentSnapshotObj.getString("noofrooms");
                        String name=queryDocumentSnapshotObj.getString("nameofhospital");
                        String location=queryDocumentSnapshotObj.getString("locationofhospital");
                        collection+="Document ID : "+documentId+"\nRooms are : "+rooms+"\nhospital name : "+name+"\nlocation of hospital : "+location+"\n\n";
                        objectDialog.dismiss();
                        collectionValuesTV1.setText(collection);


                        }



                    }
                }
            });

        }
        catch (Exception e){
            Toast.makeText(this, "value not found" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


}









