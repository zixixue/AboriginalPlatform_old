package au.edu.unsw.infs3605.aboriginalplatform.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public final class Conversation implements Parcelable {
    private String id;
    private String userId;
    private UserExtendData userExtendData;
    private String topic;
    private String title;
    private String content;
    private List<String> likeIds;
    private long commentNum;
    private long clickNum;
    private long publishTime;
    private String address;
    private String imageUrl;

    public Conversation() {
    }


    protected Conversation(Parcel in) {
        id = in.readString();
        userId = in.readString();
        userExtendData = in.readParcelable(UserExtendData.class.getClassLoader());
        topic = in.readString();
        title = in.readString();
        content = in.readString();
        likeIds = in.createStringArrayList();
        commentNum = in.readLong();
        clickNum = in.readLong();
        publishTime = in.readLong();
        address = in.readString();
        imageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeParcelable(userExtendData, flags);
        dest.writeString(topic);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeStringList(likeIds);
        dest.writeLong(commentNum);
        dest.writeLong(clickNum);
        dest.writeLong(publishTime);
        dest.writeString(address);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Conversation> CREATOR = new Creator<Conversation>() {
        @Override
        public Conversation createFromParcel(Parcel in) {
            return new Conversation(in);
        }

        @Override
        public Conversation[] newArray(int size) {
            return new Conversation[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getLikeIds() {
        return likeIds;
    }

    public void setLikeIds(List<String> likeIds) {
        this.likeIds = likeIds;
    }

    public long getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(long commentNum) {
        this.commentNum = commentNum;
    }

    public long getClickNum() {
        return clickNum;
    }

    public void setClickNum(long clickNum) {
        this.clickNum = clickNum;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserExtendData getUserExtendData() {
        return userExtendData;
    }

    public void setUserExtendData(UserExtendData userExtendData) {
        this.userExtendData = userExtendData;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", topic='" + topic + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", likeIds=" + likeIds +
                ", commentNum=" + commentNum +
                ", clickNum=" + clickNum +
                ", publishTime=" + publishTime +
                ", address='" + address + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
