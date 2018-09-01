package qthjen_dev.io.notes.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SerializedName("Id")
    @Expose
    private String id;

    @SerializedName("Email")
    @Expose
    private String email;

    @SerializedName("Pass")
    @Expose
    private String pass;

    protected User(Parcel in) {
        id = in.readString();
        email = in.readString();
        pass = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
return id;
}

    public void setId(String id) {
this.id = id;
}

    public String getEmail() {
return email;
}

    public void setEmail(String email) {
this.email = email;
}

    public String getPass() {
return pass;
}

    public void setPass(String pass) {
this.pass = pass;
}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(pass);
    }
}