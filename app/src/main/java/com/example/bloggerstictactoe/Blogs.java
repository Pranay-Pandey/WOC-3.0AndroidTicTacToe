package com.example.bloggerstictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Blogs extends AppCompatActivity  {

    private FirebaseFirestore fstore;
    private String userID;
    private FirebaseAuth auth;
    private  FirestoreRecyclerAdapter adapter;
    private RecyclerView blogsrecyclerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogs);

        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        fstore = FirebaseFirestore.getInstance();
        blogsrecyclerview = findViewById(R.id.savedblogsfromfirestore);


        Query query = fstore.collection("blogs"+userID);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateNewBlog.class));
            }
        });

        FirestoreRecyclerOptions<BlogsModel> options = new FirestoreRecyclerOptions.Builder<BlogsModel>()
                .setQuery(query,BlogsModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<BlogsModel, BlogsViewHolder>(options) {
            @NonNull
            @Override
            public BlogsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blogsviewer,parent,false);
                return new BlogsViewHolder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull BlogsViewHolder holder, int position, @NonNull BlogsModel model) {
                holder.texttitle.setText(model.getTitle());
                holder.textcontent.setText(model.getContent());
                holder.likeimage.setVisibility(View.INVISIBLE);

                fstore.collection("blogs"+userID).whereEqualTo("title",model.getTitle()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String documentID = documentSnapshot.getId();
                        DocumentReference docref = fstore.collection("blogs"+userID).document(documentID);
                        docref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                holder.likecount.setText(value.getLong("likes").toString());
                            }
                        });
                    }
                });

                holder.optionsimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                             fstore.collection("blogs"+userID).whereEqualTo("title",model.getTitle())
                                     .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                 @Override
                                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                     DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                     String documentID = documentSnapshot.getId();
                                     fstore.collection("blogs"+userID).document(documentID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                         @Override
                                         public void onSuccess(Void aVoid) {
                                             Toast.makeText(Blogs.this, "Blog Deleted successfully", Toast.LENGTH_SHORT).show();
                                         }
                                     }).addOnFailureListener(new OnFailureListener() {
                                         @Override
                                         public void onFailure(@NonNull Exception e) {
                                             Toast.makeText(Blogs.this, "Failed! Try again", Toast.LENGTH_SHORT).show();
                                         }
                                     });
                                 }
                             });
                                return false;
                            }
                        });
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent i = new Intent(v.getContext(),EditBlog.class);
                                i.putExtra("title",model.getTitle());
                                i.putExtra("content",model.getContent());
                                i.putExtra("useridofblogowner",userID);
                                v.getContext().startActivity(i);
                                return false;
                            }
                        });
                        if (model.getRequestid()!=null && model.getApprove().equals("false")){
                            popupMenu.getMenu().add("approve "+model.getName()+"'s Request")
                                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem item) {
                                            Map<String,Object> approve = new HashMap<>();
                                            approve.put("approve","true");
                                            fstore.collection("blogs"+userID).whereEqualTo("title",model.getTitle())
                                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                                    String documentID = documentSnapshot.getId();
                                                    fstore.collection("blogs"+userID).document(documentID).update(approve).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(Blogs.this, model.getName()+"'s Request Approved", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            });
                                            return false;
                                        }
                                    });
                        }
                        popupMenu.show();
                    }
                });


            }
        };

        blogsrecyclerview.setHasFixedSize(true);
        blogsrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        blogsrecyclerview.setAdapter(adapter);

    }




    private class BlogsViewHolder extends RecyclerView.ViewHolder {
            private TextView texttitle;
            private TextView textcontent;
            private ImageView optionsimage;
            private ImageView likeimage;
            private TextView likecount;

        public BlogsViewHolder(@NonNull View itemView) {
            super(itemView);
            texttitle = itemView.findViewById(R.id.titleforblog);
            textcontent = itemView.findViewById(R.id.contentofblog);
            optionsimage = itemView.findViewById(R.id.optionsimage);
            likeimage = itemView.findViewById(R.id.likeimage);
            likecount = itemView.findViewById(R.id.likecount);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
       /* if (auth.getCurrentUser()!= null){
            updateuserstat("offline");}*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
       /* if (auth.getCurrentUser()!= null){
            updateuserstat("online");
        }*/
    }

   /* private void updateuserstat(String state){

        Map<String,Object> status = new HashMap<>();
        status.put("status",state);
        fstore.collection("users").document(userID).update(status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (auth.getCurrentUser()!= null){
            updateuserstat("offline");}
    }*/
}