package qthjen_dev.io.notes.View.ViewInterface;

public interface EditViewInterface {
    void editSuccess(int position); // positon for notifyItemChanged
    void editError(String e);
}
