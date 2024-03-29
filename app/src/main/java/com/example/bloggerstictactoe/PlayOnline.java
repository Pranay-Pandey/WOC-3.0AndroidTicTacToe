package com.example.bloggerstictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

public class PlayOnline extends AppCompatActivity implements View.OnClickListener {

   private String senderid,receiverid;
   private FirebaseFirestore fstore;
   private FirebaseAuth auth;
    String userID;
   TextView rules;
   ImageView rulesimg;
    Button but1,but2,but3,but4,but5,but6,but7,but8,but9,loadmove,reset;
     TextView top;
     Boolean active = true;


    String mysymbol;
    String opponentsymbol;
    int filledposition[] = {-1,-1,-1,-1,-1,-1,-1,-1,-1};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_online);


        Intent data = getIntent();
        senderid = data.getStringExtra("senderid");
        receiverid = data.getStringExtra("receiverid");
        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();

        rules = findViewById(R.id.rules);
        rulesimg = findViewById(R.id.rulesimg);

        top = findViewById(R.id.top);
        reset = findViewById(R.id.reset);

       /* fstore.collection("play"+receiverid).document(receiverid+"-"+senderid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()){}
                else {startActivity(new Intent(getApplicationContext(),Play.class));
                return;}
            }
        });*/

      /*  final DocumentReference sfDocRef = fstore.collection("play" + receiverid).document(receiverid + "-" + senderid);
        fstore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);

                String filled0,filled1,filled2,filled3,filled4,filled5,filled6,filled7,filled8;
                filled0 = snapshot.getString("tag0");
                filled1 = snapshot.getString("tag1");
                filled2 = snapshot.getString("tag2");
                filled3 = snapshot.getString("tag3");
                filled4 = snapshot.getString("tag4");
                filled5 = snapshot.getString("tag5");
                filled6 = snapshot.getString("tag6");
                filled7 = snapshot.getString("tag7");
                filled8 = snapshot.getString("tag8");



                if (filled0.equals("1 ")){but1.setText("O");filledposition[0] = 1;}
                if (filled1.equals("1 ")){but2.setText("O");filledposition[1] = 1;}
                if (filled2.equals("1 ")){but3.setText("O");filledposition[2] = 1;}
                if (filled3.equals("1 ")){but4.setText("O");filledposition[3] = 1;}
                if (filled4.equals("1 ")){but5.setText("O");filledposition[4] = 1;}
                if (filled5.equals("1 ")){but6.setText("O");filledposition[5] = 1;}
                if (filled6.equals("1 ")){but7.setText("O");filledposition[6] = 1;}
                if (filled7.equals("1 ")){but8.setText("O");filledposition[7] = 1;}
                if (filled8.equals("1 ")){but9.setText("O");filledposition[8] = 1;}

                if (filled0.equals("0 ")){but1.setText("X");filledposition[0] = 0;}
                if (filled1.equals("0 ")){but2.setText("X");filledposition[1] = 0;}
                if (filled2.equals("0 ")){but3.setText("X");filledposition[2] = 0;}
                if (filled3.equals("0 ")){but4.setText("X");filledposition[3] = 0;}
                if (filled4.equals("0 ")){but5.setText("X");filledposition[4] = 0;}
                if (filled5.equals("0 ")){but6.setText("X");filledposition[5] = 0;}
                if (filled6.equals("0 ")){but7.setText("X");filledposition[6] = 0;}
                if (filled7.equals("0 ")){but8.setText("X");filledposition[7] = 0;}
                if (filled8.equals("0 ")){but9.setText("X");filledposition[8] = 0;}

                if (filled0.equals("-1 ")){but1.setText("");filledposition[0] = -1;}
                if (filled1.equals("-1 ")){but2.setText("");filledposition[1] = -1;}
                if (filled2.equals("-1 ")){but3.setText("");filledposition[2] = -1;}
                if (filled3.equals("-1 ")){but4.setText("");filledposition[3] = -1;}
                if (filled4.equals("-1 ")){but5.setText("");filledposition[4] = -1;}
                if (filled5.equals("-1 ")){but6.setText("");filledposition[5] = -1;}
                if (filled6.equals("-1 ")){but7.setText("");filledposition[6] = -1;}
                if (filled7.equals("-1 ")){but8.setText("");filledposition[7] = -1;}
                if (filled8.equals("-1 ")){but9.setText("");filledposition[8] = -1;}


                return null;
            }
        });*/
        
        
        

        if (senderid.equals(userID)) {
            mysymbol = "X";//0
            opponentsymbol = "O";//1

        }
        else {
            mysymbol = "O";//1
            opponentsymbol = "X";//0

        }

        fstore.collection("play"+receiverid).document(receiverid+"-"+senderid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
           String play = documentSnapshot.getString("approve");
           if (play.equals("true")){active = true;}
           else {active = false;}
            }
        });

        fstore.collection("play"+receiverid).document(receiverid+"-"+senderid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String officiallyconfirmeduser = value.getString("currentuser");

                        if (officiallyconfirmeduser.equals(userID)){
                            top.setText("Your Turn "+mysymbol);

                        }else {
                            top.setText("Opponent's turn "+ opponentsymbol);
                        }

                    }
                });

        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PlayOnlineRules.class));
            }
        });
        rulesimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PlayOnlineRules.class));
            }
        });



        but1 = findViewById(R.id.but1);
        but2 = findViewById(R.id.but2);
        but3 = findViewById(R.id.but3);
        but4 = findViewById(R.id.but4);
        but5 = findViewById(R.id.but5);
        but6 = findViewById(R.id.but6);
        but7 = findViewById(R.id.but7);
        but8 = findViewById(R.id.but8);
        but9 = findViewById(R.id.but9);
        top = findViewById(R.id.top);
        loadmove = findViewById(R.id.loadmove);


        but1.setOnClickListener(this);
        but2.setOnClickListener(this);
        but3.setOnClickListener(this);
        but4.setOnClickListener(this);
        but5.setOnClickListener(this);
        but6.setOnClickListener(this);
        but7.setOnClickListener(this);
        but8.setOnClickListener(this);
        but9.setOnClickListener(this);


        loadmove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettagfromfirestore();

                checkfordraw();
                checkforwin();


                if (active){fstore.collection("play"+receiverid).document(receiverid+"-"+senderid)
                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                String officiallyconfirmeduser = value.getString("currentuser");

                                if (officiallyconfirmeduser.equals(userID)){
                                    top.setText("Your Turn "+mysymbol);

                                }else {
                                    top.setText("Opponent's turn "+ opponentsymbol);
                                }

                            }
                        });}


                checkfordraw();
                checkforwin();

                fstore.collection("play"+receiverid).document(receiverid+"-"+senderid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String play = documentSnapshot.getString("approve");
                        if (play.equals("true")){active = true;}
                        else {active = false;}
                    }
                });


            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (active==false) {
                    fstore.runTransaction(new Transaction.Function<Void>() {
                        @Nullable
                        @Override
                        public Void apply(@Nullable Transaction transaction) throws FirebaseFirestoreException {
                            // DocumentSnapshot snapshot = transaction.get(sfDocRef);
                            if (transaction != null) {
                                Map<String, Object> tag = new HashMap<>();
                                tag.put("tag0", -1 + " ");
                                tag.put("tag1", -1 + " ");
                                tag.put("tag2", -1 + " ");
                                tag.put("tag3", -1 + " ");
                                tag.put("tag4", -1 + " ");
                                tag.put("tag5", -1 + " ");
                                tag.put("tag6", -1 + " ");
                                tag.put("tag7", -1 + " ");
                                tag.put("tag8", -1 + " ");
                                tag.put("approve","true");
                                tag.put("currentuser",userID);
                                transaction.update(fstore.collection("play" + senderid).document(receiverid + "-" + senderid), tag);
                                transaction.update(fstore.collection("play" + receiverid).document(receiverid + "-" + senderid), tag);
                            }
                            return null;
                        }
                    }).addOnCompleteListener(task -> {
                        Toast.makeText(PlayOnline.this, "Reset", Toast.LENGTH_SHORT).show();
                    });


                }
                active=true;


                /*{
                    final DocumentReference sfDocRef = fstore.collection("play" + receiverid).document(receiverid + "-" + senderid);
                fstore.runTransaction(new Transaction.Function<Void>() {
                    @Nullable
                    @Override
                    public Void apply(@Nullable Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(sfDocRef);

                        Map<String, Object> tag = new HashMap<>();
                        tag.put("tag0", -1 + " ");
                        tag.put("tag1", -1 + " ");
                        tag.put("tag2", -1 + " ");
                        tag.put("tag3", -1 + " ");
                        tag.put("tag4", -1 + " ");
                        tag.put("tag5", -1 + " ");
                        tag.put("tag6", -1 + " ");
                        tag.put("tag7", -1 + " ");
                        tag.put("tag8", -1 + " ");

                        transaction.update(sfDocRef,tag);




                        return null;
                    }
                });
            }
*/

                /*
                fstore.collection("play"+receiverid).document(receiverid+"-"+senderid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){}
                        else {startActivity(new Intent(getApplicationContext(),Play.class));}
                    }
                });

                if (active==false)
                {
                    fstore.collection("play"+receiverid).document(receiverid+"-"+senderid).delete();
                    fstore.collection("play"+senderid).document(receiverid+"-"+senderid).delete();

                    Map<String, Object> tag = new HashMap<>();
                    tag.put("senderid", senderid);
                    tag.put("receiverid", receiverid);
                    tag.put("approve", "true");
                    tag.put("currentuser", userID);
                    tag.put("tag0", -1 + " ");
                    tag.put("tag1", -1 + " ");
                    tag.put("tag2", -1 + " ");
                    tag.put("tag3", -1 + " ");
                    tag.put("tag4", -1 + " ");
                    tag.put("tag5", -1 + " ");
                    tag.put("tag6", -1 + " ");
                    tag.put("tag7", -1 + " ");
                    tag.put("tag8", -1 + " ");
                    fstore.collection("play" + senderid).document(receiverid + "-" + senderid).set(tag);
                    fstore.collection("play" + receiverid).document(receiverid + "-" + senderid).set(tag).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startActivity(new Intent(getApplicationContext(), Play.class));

                        }
                    });
                }
*/
            }
        });


    }

    private void checkforwin() {
        int arr[][] = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};

        for(int i=0; i<8;i++){

            if (filledposition[arr[i][0]] == filledposition[arr[i][1]] && filledposition[arr[i][1]] == filledposition[arr[i][2]]) {
                if (filledposition[arr[i][0]]!=-1){
                    if (filledposition[arr[i][0]]==0){
                             if (senderid.equals(userID)) {
                                top.setText("You Won");
                             }else {
                                top.setText("You Lost");
                             }

                        if (active){ fstore.collection("play" + receiverid).document(receiverid + "-" + senderid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                String gameon = value.getString("approve");
                                if (gameon.equals("true")){fstore.collection("users").document(senderid).update("wins",FieldValue.increment(1));
                                   }
                            }
                        });
                            }
                    }
                    else{
                            if (receiverid.equals(userID)){  top.setText("You Won");}
                            else {top.setText("You Lost");}
                        if (active){ fstore.collection("play" + receiverid).document(receiverid + "-" + senderid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                String gameon = value.getString("approve");
                                if (gameon.equals("true")){ fstore.collection("users").document(receiverid).update("wins",FieldValue.increment(1));}
                            }
                        });
                        }

                    }
                   if (active){  fstore.collection("play" + receiverid).document(receiverid + "-" + senderid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                       @Override
                       public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                           String gameon = value.getString("approve");
                           if (gameon.equals("true")){fstore.collection("users").document(senderid).update("played",FieldValue.increment(1));
                               fstore.collection("users").document(receiverid).update("played",FieldValue.increment(1));}
                       }
                   }); }
                   Map<String,Object> approve = new HashMap<>();
                   approve.put("approve","false");
                    fstore.collection("play" + receiverid).document(receiverid + "-" + senderid).update(approve).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PlayOnline.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    active = false;
                }

            }


        }
    }

    private void checkfordraw() {
        if(filledposition[0]!=-1&&filledposition[1]!=-1&&filledposition[2]!=-1&&filledposition[3]!=-1&&filledposition[4]!=-1&&filledposition[5]!=-1&&filledposition[6]!=-1&&filledposition[7]!=-1&&filledposition[8]!=-1){
            top.setText("Draw");
            if (active){fstore.collection("play" + receiverid).document(receiverid + "-" + senderid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    String gameon = value.getString("approve");
                    if (gameon.equals("true")){ fstore.collection("users").document(senderid).update("played",FieldValue.increment(1));
                        fstore.collection("users").document(receiverid).update("played",FieldValue.increment(1));}
                }
            });
                }
            Map<String,Object> approve = new HashMap<>();
            approve.put("approve","false");
            fstore.collection("play" + receiverid).document(receiverid + "-" + senderid).update(approve).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PlayOnline.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            active = false;
        }
    }

    private void gettagfromfirestore() {
        DocumentReference docref =  fstore.collection("play"+receiverid).document(receiverid+"-"+senderid);
        docref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String filled0,filled1,filled2,filled3,filled4,filled5,filled6,filled7,filled8;
                filled0 = value.getString("tag0");
                filled1 = value.getString("tag1");
                filled2 = value.getString("tag2");
                filled3 = value.getString("tag3");
                filled4 = value.getString("tag4");
                filled5 = value.getString("tag5");
                filled6 = value.getString("tag6");
                filled7 = value.getString("tag7");
                filled8 = value.getString("tag8");



                if (filled0.equals("1 ")){but1.setText("O");filledposition[0] = 1;}
                if (filled1.equals("1 ")){but2.setText("O");filledposition[1] = 1;}
                if (filled2.equals("1 ")){but3.setText("O");filledposition[2] = 1;}
                if (filled3.equals("1 ")){but4.setText("O");filledposition[3] = 1;}
                if (filled4.equals("1 ")){but5.setText("O");filledposition[4] = 1;}
                if (filled5.equals("1 ")){but6.setText("O");filledposition[5] = 1;}
                if (filled6.equals("1 ")){but7.setText("O");filledposition[6] = 1;}
                if (filled7.equals("1 ")){but8.setText("O");filledposition[7] = 1;}
                if (filled8.equals("1 ")){but9.setText("O");filledposition[8] = 1;}

                if (filled0.equals("0 ")){but1.setText("X");filledposition[0] = 0;}
                if (filled1.equals("0 ")){but2.setText("X");filledposition[1] = 0;}
                if (filled2.equals("0 ")){but3.setText("X");filledposition[2] = 0;}
                if (filled3.equals("0 ")){but4.setText("X");filledposition[3] = 0;}
                if (filled4.equals("0 ")){but5.setText("X");filledposition[4] = 0;}
                if (filled5.equals("0 ")){but6.setText("X");filledposition[5] = 0;}
                if (filled6.equals("0 ")){but7.setText("X");filledposition[6] = 0;}
                if (filled7.equals("0 ")){but8.setText("X");filledposition[7] = 0;}
                if (filled8.equals("0 ")){but9.setText("X");filledposition[8] = 0;}

                if (filled0.equals("-1 ")){but1.setText("");filledposition[0] = -1;}
                if (filled1.equals("-1 ")){but2.setText("");filledposition[1] = -1;}
                if (filled2.equals("-1 ")){but3.setText("");filledposition[2] = -1;}
                if (filled3.equals("-1 ")){but4.setText("");filledposition[3] = -1;}
                if (filled4.equals("-1 ")){but5.setText("");filledposition[4] = -1;}
                if (filled5.equals("-1 ")){but6.setText("");filledposition[5] = -1;}
                if (filled6.equals("-1 ")){but7.setText("");filledposition[6] = -1;}
                if (filled7.equals("-1 ")){but8.setText("");filledposition[7] = -1;}
                if (filled8.equals("-1 ")){but9.setText("");filledposition[8] = -1;}
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (active==false){return;}

        Button clickedbutton = findViewById(v.getId());
        int clickedtag = Integer.parseInt((v.getTag().toString()));

       if (filledposition[clickedtag] ==-1) {
         Task<DocumentSnapshot> docref =   fstore.collection("play"+receiverid).document(receiverid+"-"+senderid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
             @Override
             public void onSuccess(DocumentSnapshot documentSnapshot) {
                 String officiallyconfirmeduser = documentSnapshot.getString("currentuser");

                 if (officiallyconfirmeduser.equals(userID)){
                     if (userID.equals(senderid)) {
                         clickedbutton.setText(mysymbol);
                         filledposition[clickedtag] = 0;
                     } else {
                         clickedbutton.setText(mysymbol);
                         filledposition[clickedtag] = 1;

                     }
                     Map<String,Object> tag = new HashMap<>();
                     tag.put("tag"+clickedtag,filledposition[clickedtag]+" ");
                     fstore.collection("play"+receiverid).document(receiverid+"-"+senderid).update(tag);


                 }
             }
         });



                /*   .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                       @Override
                       public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                           String officiallyconfirmeduser = value.getString("currentuser");
                           if (officiallyconfirmeduser.equals(userID)) {
                               if (userID.equals(senderid)) {
                                   clickedbutton.setText(mysymbol);
                                   filledposition[clickedtag] = 0;
                               } else {
                                   clickedbutton.setText(mysymbol);
                                   filledposition[clickedtag] = 1;

                               }
                               Map<String,Object> tag = new HashMap<>();
                               tag.put("tag"+clickedtag,filledposition[clickedtag]+" ");
                               fstore.collection("play"+receiverid).document(receiverid+"-"+senderid).update(tag);


                           }
                       }
                   });*/

           if (userID.equals(senderid)){
               Map<String,Object> update = new HashMap<>();
               update.put("currentuser",receiverid);
               fstore.collection("play"+receiverid).document(receiverid+"-"+senderid)
                       .update(update);
           }
           else{
               Map<String,Object> update = new HashMap<>();
               update.put("currentuser",senderid);
               fstore.collection("play"+receiverid).document(receiverid+"-"+senderid)
                       .update(update);
           }

       }









        checkfordraw();
        checkforwin();
    }

    private void updatetag() {
        Map<String,Object> tag = new HashMap<>();
        tag.put("tag0",filledposition[0]+" ");
        tag.put("tag1",filledposition[1]+" ");
        tag.put("tag2",filledposition[2]+" ");
        tag.put("tag3",filledposition[3]+" ");
        tag.put("tag4",filledposition[4]+" ");
        tag.put("tag5",filledposition[5]+" ");
        tag.put("tag6",filledposition[6]+" ");
        tag.put("tag7",filledposition[7]+" ");
        tag.put("tag8",filledposition[8]+" ");
        fstore.collection("play"+receiverid).document(receiverid+"-"+senderid)
                .update(tag).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (active==false){
                    Toast.makeText(PlayOnline.this, "Game ended! To play again click RESET", Toast.LENGTH_SHORT).show();
                }
                else{ Toast.makeText(PlayOnline.this, "Your move recorded", Toast.LENGTH_SHORT).show();}
            }
        });
    }


    public void updatecurrentuser(String id){
        Map<String,Object> user = new HashMap<>();
        user.put("currentuser",id);
        fstore.collection("play"+receiverid).document(receiverid+"-"+senderid)
                .update(user);

    }


}