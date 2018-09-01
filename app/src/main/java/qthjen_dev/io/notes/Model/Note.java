package qthjen_dev.io.notes.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Note implements Parcelable {

    @SerializedName("nid")
    @Expose
    private String nid;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("user")
    @Expose
    private String user;

    protected Note(Parcel in) {
        nid = in.readString();
        note = in.readString();
        timestamp = in.readString();
        user = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nid);
        dest.writeString(note);
        dest.writeString(timestamp);
        dest.writeString(user);
    }
}
