package au.edu.unsw.infs3605.aboriginalplatform.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class UserExtendData implements Parcelable {

    private boolean isAusOriginal;
    private String userId;
    private String userName;
    private String userEmail;
    private String userProfile;
    private String id;

    public UserExtendData() {
    }

    protected UserExtendData(Parcel in) {
        isAusOriginal = in.readByte() != 0;
        userId = in.readString();
        userName = in.readString();
        userEmail = in.readString();
        userProfile = in.readString();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isAusOriginal ? 1 : 0));
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userEmail);
        dest.writeString(userProfile);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserExtendData> CREATOR = new Creator<UserExtendData>() {
        @Override
        public UserExtendData createFromParcel(Parcel in) {
            return new UserExtendData(in);
        }

        @Override
        public UserExtendData[] newArray(int size) {
            return new UserExtendData[size];
        }
    };

    public boolean isAusOriginal() {
        return isAusOriginal;
    }

    public void setAusOriginal(boolean ausOriginal) {
        isAusOriginal = ausOriginal;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public String toString() {
        return "UserExtendData{" +
                "isAusOriginal=" + isAusOriginal +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userProfile='" + userProfile + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
