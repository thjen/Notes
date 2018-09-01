package qthjen_dev.io.notes.View.ViewInterface;

import java.util.ArrayList;

import qthjen_dev.io.notes.Model.User;

public interface SignInViewInterface {
    void success(ArrayList<User> listUser);
    void error(String e);
}
