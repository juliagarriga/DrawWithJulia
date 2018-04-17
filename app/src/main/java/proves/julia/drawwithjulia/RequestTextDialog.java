package proves.julia.drawwithjulia;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Ing. Oscar G. Medina Cruz on 09/11/2016.
 */

public class RequestTextDialog extends android.app.DialogFragment {

    private static final String REQ_TEXT = "REQ_TEXT";

    private OnRequestTextListener onRequestTextListener;

    // VARS
    private Bitmap mPreviewBitmap;

    public RequestTextDialog() {
    }

    public static RequestTextDialog newInstance(String currentText) {
        RequestTextDialog requestTextDialog = new RequestTextDialog();
        Bundle bundle = new Bundle();
        bundle.putString(REQ_TEXT, currentText);
        requestTextDialog.setArguments(bundle);
        return requestTextDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_request_text, null);
        final TextInputEditText textInputEditText = (TextInputEditText) view.findViewById(R.id.et_req_text);

        if (!getArguments().getString(REQ_TEXT).equals(""))
            textInputEditText.setText(getArguments().getShort(REQ_TEXT));

        textInputEditText.requestFocus();
        // Used to automatically open the keyboard
        //InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(textInputEditText, InputMethodManager.SHOW_IMPLICIT);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (onRequestTextListener != null)
                            onRequestTextListener.onRequestTextConfirmed(textInputEditText.getText().toString());
                        dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (onRequestTextListener != null)
                            onRequestTextListener.onRequestTextCancelled();
                        dismiss();
                    }
                });

        return builder.create();
    }

    // METHODS

    // LISTENER
    public void setOnRequestTextListener(OnRequestTextListener onRequestTextListener) {
        this.onRequestTextListener = onRequestTextListener;
    }

    public interface OnRequestTextListener {
        void onRequestTextConfirmed(String requestedText);

        void onRequestTextCancelled();
    }
}
