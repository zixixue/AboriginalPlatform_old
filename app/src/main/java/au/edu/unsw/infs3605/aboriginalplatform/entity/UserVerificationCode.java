package au.edu.unsw.infs3605.aboriginalplatform.entity;

public class UserVerificationCode {

    private String userEmail;
    private String verifyCode;
    private long createTime;
    private long invalidTime;
    private String id;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(long invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserVerificationCode{" +
                "userEmail='" + userEmail + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", createTime=" + createTime +
                ", invalidTime=" + invalidTime +
                ", id='" + id + '\'' +
                '}';
    }
}
