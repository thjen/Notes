package qthjen_dev.io.notes.Utils.NetworkUtils;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;
import qthjen_dev.io.notes.Model.Note;
import qthjen_dev.io.notes.Model.User;
import qthjen_dev.io.notes.Model.UserName;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("insert.php")
    Single<String> InsertUser(@Field("user") String user, @Field("pass") String pass);

    @FormUrlEncoded
    @POST("login.php")
    Single<ArrayList<User>> Login(@Field("user") String user, @Field("pass") String pass);

    @FormUrlEncoded
    @POST("insertnote.php")
    Single<String> InserNote(@Field("note") String note, @Field("timestamp") String time, @Field("user") String user);

    @FormUrlEncoded
    @POST("fetchallnote.php")
    Single<ArrayList<Note>> FetchAllData(@Field("user") String user);

    @GET("fetchalluser.php")
    Single<ArrayList<UserName>> FetchAllUser();

    @FormUrlEncoded
    @POST("editnote.php")
    Completable UpdateNote(@Field("nid") int id, @Field("note") String note, @Field("timestamp") String timestamp
                           , @Field("user") String user);

    @FormUrlEncoded
    @POST("deletenote.php")
    Completable DeleteNote(@Field("nid") int nid);

    @FormUrlEncoded
    @POST("updatepass.php")
    Completable ChangePassword(@Field("user") String user, @Field("pass") String pass);
}
