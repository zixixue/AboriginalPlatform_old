package au.edu.unsw.infs3605.aboriginalplatform.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
    private String id;
    private String conversationId;
    /**
     * comment
     */
    private Comment comment;

    /**
     * reply
     */
    private String commentUserId;
    private UserExtendData commentUserExtendData;
    private long commentTime;
    private String content;
    private String imgUrl;
    private String address;

    public Comment() {
    }


    protected Comment(Parcel in) {
        id = in.readString();
        conversationId = in.readString();
        comment = in.readParcelable(Comment.class.getClassLoader());
        commentUserId = in.readString();
        commentUserExtendData = in.readParcelable(UserExtendData.class.getClassLoader());
        commentTime = in.readLong();
        content = in.readString();
        imgUrl = in.readString();
        address = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(conversationId);
        dest.writeParcelable(comment, flags);
        dest.writeString(commentUserId);
        dest.writeParcelable(commentUserExtendData, flags);
        dest.writeLong(commentTime);
        dest.writeString(content);
        dest.writeString(imgUrl);
        dest.writeString(address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public UserExtendData getCommentUserExtendData() {
        return commentUserExtendData;
    }

    public void setCommentUserExtendData(UserExtendData commentUserExtendData) {
        this.commentUserExtendData = commentUserExtendData;
    }

    public long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", conversationId='" + conversationId + '\'' +
                ", comment=" + comment +
                ", commentUserId='" + commentUserId + '\'' +
                ", commentUserExtendData=" + commentUserExtendData +
                ", commentTime=" + commentTime +
                ", content='" + content + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
