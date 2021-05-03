package com.AndroidTechCrew.technews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AndroidTechCrew.technews.fragments.Comment;
import com.AndroidTechCrew.technews.fragments.UserComments;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class userCommentAdapter extends RecyclerView.Adapter<userCommentAdapter.ViewHolder>{
    public static final String TAG = "userCommentsActivity";
    Context context;
    List<UserComments> comments;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    private String articleLink;

    public userCommentAdapter(Context context, List<UserComments> comments){
        this.context = context;
        this.comments = comments;
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();

    }

    @NonNull
    @Override
    public userCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_comment_item, parent, false);
        return new userCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userCommentAdapter.ViewHolder holder, int position) {
//        Comment comment = comments.get(position);
//        comment.setCommentArticleLink(getUserComment(currentUser));

        holder.bind(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profilePic;
        TextView UserCommentArticle;
        TextView Usercomment;
        TextView UsercommentTag;
        RelativeLayout UsercommentItemRL;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.ivUserCommentItemProfileImage);
            UserCommentArticle = itemView.findViewById(R.id.UserCommentArticle);
            Usercomment = itemView.findViewById(R.id.tvUserCommentItemComment);
            UsercommentItemRL = itemView.findViewById(R.id.UsercommentItemRL);
            UsercommentTag = itemView.findViewById(R.id.UserCommentTag);



        }

        void bind(UserComments c){
            //Uri myUri = Uri.parse("http://stackoverflow.com")
            //Uri uri = Uri.parse(c.getProfilePic());
            Glide.with(context).load(c.getProfilePic()).into(profilePic);
            UserCommentArticle.setText(c.getArticleTitle());
            Usercomment.setText(c.getComment());
            UsercommentItemRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: GO to the article of the page
                    Intent i = new Intent(context, CommentActivity.class);
                    i.putExtra("articleLink",c.getArticleLink());
                    i.putExtra("title",c.getArticleTitle());
                    i.putExtra("image",c.getImage());
                    i.putExtra("description",c.getDescription());
                    context.startActivity(i);
                }
            });

        }
    }
    public String getUserComment(FirebaseUser currentUser) {
        db.collection("users/" + currentUser.getUid() + "/comments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                articleLink = queryDocumentSnapshots.getDocuments().toString();

            }
        });

        return articleLink;
    }



}
