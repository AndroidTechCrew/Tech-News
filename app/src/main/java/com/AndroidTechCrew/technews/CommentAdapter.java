package com.AndroidTechCrew.technews;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AndroidTechCrew.technews.fragments.Comment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    public static final String TAG = "CommentAdapter";
    Context context;
    List<Comment> comments;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;

    public CommentAdapter(Context context, List<Comment> comments){
        this.context = context;
        this.comments = comments;
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        holder.bind(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profilePic;
        TextView username;
        TextView comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.ivCommentItemProfileImage);
            username = itemView.findViewById(R.id.tvCommentItemUsername);
            comment= itemView.findViewById(R.id.tvCommentItemComment);

        }

        void bind(Comment c){
            //Uri myUri = Uri.parse("http://stackoverflow.com")
            //Uri uri = Uri.parse(c.getProfilePic());
            Glide.with(context).load(c.getProfilePic()).centerCrop().into(profilePic);
            username.setText(c.getUsername());
            comment.setText(c.getComment());
        }
    }

}
