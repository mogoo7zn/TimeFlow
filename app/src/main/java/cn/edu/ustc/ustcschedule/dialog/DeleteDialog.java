package cn.edu.ustc.ustcschedule.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.timeflow.R;


public class DeleteDialog extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    int event_id;
    String table_name;

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public interface DeleteDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }


    // Use this instance of the interface to deliver action events
    DeleteDialogListener listener;

    public void setListener(DeleteDialogListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete_title)
                .setMessage(R.string.delete_text)
                .setPositiveButton(R.string.delete, (dialog, id) -> listener.onDialogPositiveClick(DeleteDialog.this))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> listener.onDialogNegativeClick(DeleteDialog.this));
        // Create the AlertDialog object and return it

        return builder.create();
    }


}
