package proves.julia.drawwithjulia;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.byox.drawview.utils.SerializablePaint;

import java.util.Random;

/**
 * Created by Ing. Oscar G. Medina Cruz on 07/11/2016.
 */

public class DrawAttrDialog extends DialogFragment {

    // LISTENER
    private OnCustomViewDialogListener onCustomViewDialogListener;

    // VARS
    private Paint mPaint;

    private View view;
    private View previewColor;
    private AppCompatSeekBar seekBarRed;
    private AppCompatSeekBar seekBarGreen;
    private AppCompatSeekBar seekBarBlue;
    private TextView textViewRedValue;
    private TextView textViewGreenValue;
    private TextView textViewBlueValue;
    private AppCompatSeekBar seekBarStrokeWidth;
    private TextView textViewStrokeWidth;
    private AppCompatSeekBar seekBarOpacity;
    private TextView textViewOpacity;
    private AppCompatSeekBar seekBarFontSize;
    private TextView textViewFontSize;
    private Button resetSettingsButton;
    private AppCompatCheckBox appCompatCheckBoxAntiAlias;
    private AppCompatCheckBox appCompatCheckBoxDither;
    private AppCompatRadioButton appCompatRadioButtonFill;
    private AppCompatRadioButton appCompatRadioButtonFillStroke;
    private AppCompatRadioButton appCompatRadioButtonStroke;
    private AppCompatRadioButton appCompatRadioButtonButt;
    private AppCompatRadioButton appCompatRadioButtonRound;
    private AppCompatRadioButton appCompatRadioButtonSquare;
    private AppCompatRadioButton appCompatRadioButtonDefault;
    private AppCompatRadioButton appCompatRadioButtonMonospace;
    private AppCompatRadioButton appCompatRadioButtonSansSerif;
    private AppCompatRadioButton appCompatRadioButtonSerif;


    public DrawAttrDialog() {
    }

    public static DrawAttrDialog newInstance() {
        return new DrawAttrDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_draw_attr, null);

        previewColor = view.findViewById(R.id.preview_color);
        seekBarRed = (AppCompatSeekBar) view.findViewById(R.id.acsb_red);
        seekBarGreen = (AppCompatSeekBar) view.findViewById(R.id.acsb_green);
        seekBarBlue = (AppCompatSeekBar) view.findViewById(R.id.acsb_blue);
        textViewRedValue = (TextView) view.findViewById(R.id.tv_current_red);
        textViewGreenValue = (TextView) view.findViewById(R.id.tv_current_green);
        textViewBlueValue = (TextView) view.findViewById(R.id.tv_current_blue);
        seekBarStrokeWidth = (AppCompatSeekBar) view.findViewById(R.id.acsb_stroke_width);
        textViewStrokeWidth = (TextView) view.findViewById(R.id.tv_stroke_width);
        seekBarOpacity = (AppCompatSeekBar) view.findViewById(R.id.acsb_opacity);
        textViewOpacity = (TextView) view.findViewById(R.id.tv_opacity);
        seekBarFontSize = (AppCompatSeekBar) view.findViewById(R.id.acsb_font_size);
        textViewFontSize = (TextView) view.findViewById(R.id.tv_font_size);
        resetSettingsButton = view.findViewById(R.id.resetSettingsButton);
        appCompatCheckBoxAntiAlias = (AppCompatCheckBox) view.findViewById(R.id.chb_anti_alias);
        appCompatCheckBoxDither = (AppCompatCheckBox) view.findViewById(R.id.chb_dither);
        appCompatRadioButtonFill = (AppCompatRadioButton) view.findViewById(R.id.rb_fill);
        appCompatRadioButtonFillStroke = (AppCompatRadioButton) view.findViewById(R.id.rb_fill_stroke);
        appCompatRadioButtonStroke = (AppCompatRadioButton) view.findViewById(R.id.rb_stroke);
        appCompatRadioButtonButt = (AppCompatRadioButton) view.findViewById(R.id.rb_butt);
        appCompatRadioButtonRound = (AppCompatRadioButton) view.findViewById(R.id.rb_round);
        appCompatRadioButtonSquare = (AppCompatRadioButton) view.findViewById(R.id.rb_square);
        appCompatRadioButtonDefault = (AppCompatRadioButton) view.findViewById(R.id.rb_default);
        appCompatRadioButtonMonospace = (AppCompatRadioButton) view.findViewById(R.id.rb_monospace);
        appCompatRadioButtonSansSerif = (AppCompatRadioButton) view.findViewById(R.id.rb_sans_serif);
        appCompatRadioButtonSerif = (AppCompatRadioButton) view.findViewById(R.id.rb_serif);

        updateAttributes();

        AppCompatSeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mPaint.setColor(Color.rgb(
                        seekBarRed.getProgress(), seekBarGreen.getProgress(), seekBarBlue.getProgress()));
                previewColor.setBackgroundColor(mPaint.getColor());

                textViewRedValue.setText(String.valueOf(seekBarRed.getProgress()));
                textViewGreenValue.setText(String.valueOf(seekBarGreen.getProgress()));
                textViewBlueValue.setText(String.valueOf(seekBarBlue.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        seekBarRed.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBarGreen.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBarBlue.setOnSeekBarChangeListener(onSeekBarChangeListener);

        seekBarStrokeWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mPaint.setStrokeWidth(i);
                textViewStrokeWidth.setText(getContext().getResources().getString(R.string.stroke_width, i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mPaint.setAlpha(i);
                textViewOpacity.setText(getContext().getResources().getString(R.string.opacity, i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mPaint.setTextSize(i);
                textViewFontSize.setText(getContext().getResources().getString(R.string.font_size, i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        appCompatCheckBoxAntiAlias.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mPaint.setAntiAlias(b);
            }
        });

        appCompatCheckBoxDither.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mPaint.setDither(b);
            }
        });

        appCompatRadioButtonFill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mPaint.setStyle(Paint.Style.FILL);
            }
        });

        appCompatRadioButtonFillStroke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            }
        });

        appCompatRadioButtonStroke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mPaint.setStyle(Paint.Style.STROKE);
            }
        });

        appCompatRadioButtonButt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mPaint.setStrokeCap(Paint.Cap.BUTT);
            }
        });

        appCompatRadioButtonRound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mPaint.setStrokeCap(Paint.Cap.ROUND);
            }
        });

        appCompatRadioButtonSquare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mPaint.setStrokeCap(Paint.Cap.SQUARE);
            }
        });

        appCompatRadioButtonDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mPaint.setTypeface(Typeface.DEFAULT);
            }
        });

        appCompatRadioButtonMonospace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mPaint.setTypeface(Typeface.MONOSPACE);
            }
        });

        appCompatRadioButtonSansSerif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mPaint.setTypeface(Typeface.SANS_SERIF);
            }
        });

        appCompatRadioButtonSerif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mPaint.setTypeface(Typeface.SERIF);
            }
        });



        final DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        resetAttributes();
                        updateAttributes();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        resetSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setMessage(getResources().getString(R.string.to_reset_settings))
                        .setPositiveButton(android.R.string.ok, clickListener)
                        .setNegativeButton(android.R.string.no, clickListener).show();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (onCustomViewDialogListener != null)
                            onCustomViewDialogListener.onRefreshPaint(mPaint);
                        dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    private void resetAttributes() {
        //Once entered, the user information must be saved to an internal, non-deleted source
        SerializablePaint paint = new SerializablePaint();
        paint.setColor(Color.BLACK);
        paint.setStyle(SerializablePaint.Style.STROKE);
        paint.setDither(true);
        paint.setStrokeWidth(3);
        paint.setAlpha(255);
        paint.setAntiAlias(true);
        paint.setStrokeCap(SerializablePaint.Cap.SQUARE);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(25);
        setPaint(paint);
    }

    // METHODS
    public void setPaint(Paint paint) {
        this.mPaint = paint;
    }

    private void updateAttributes() {

        int colorRed = Color.red(mPaint.getColor());
        int colorGreen = Color.green(mPaint.getColor());
        int colorBlue = Color.blue(mPaint.getColor());
        previewColor.setBackgroundColor(mPaint.getColor());

        seekBarRed.setProgress(colorRed);
        seekBarGreen.setProgress(colorGreen);
        seekBarBlue.setProgress(colorBlue);
        seekBarStrokeWidth.setProgress((int) mPaint.getStrokeWidth());
        seekBarOpacity.setProgress((int) mPaint.getAlpha());
        seekBarFontSize.setProgress((int) mPaint.getTextSize());

        textViewRedValue.setText(String.valueOf(Color.red(mPaint.getColor())));
        textViewGreenValue.setText(String.valueOf(Color.green(mPaint.getColor())));
        textViewBlueValue.setText(String.valueOf(Color.blue(mPaint.getColor())));
        textViewStrokeWidth.setText(getContext().getResources().getString(R.string.stroke_width, (int) mPaint.getStrokeWidth()));
        textViewOpacity.setText(getContext().getResources().getString(R.string.opacity, (int) mPaint.getAlpha()));
        textViewFontSize.setText(getContext().getResources().getString(R.string.font_size, (int) mPaint.getTextSize()));

        appCompatCheckBoxAntiAlias.setChecked(mPaint.isAntiAlias());
        appCompatCheckBoxDither.setChecked(mPaint.isDither());
        appCompatRadioButtonFill.setChecked(mPaint.getStyle() == Paint.Style.FILL);
        appCompatRadioButtonFillStroke.setChecked(mPaint.getStyle() == Paint.Style.FILL_AND_STROKE);
        appCompatRadioButtonStroke.setChecked(mPaint.getStyle() == Paint.Style.STROKE);
        appCompatRadioButtonButt.setChecked(mPaint.getStrokeCap() == Paint.Cap.BUTT);
        appCompatRadioButtonRound.setChecked(mPaint.getStrokeCap() == Paint.Cap.ROUND);
        appCompatRadioButtonSquare.setChecked(mPaint.getStrokeCap() == Paint.Cap.SQUARE);
        appCompatRadioButtonDefault.setChecked(mPaint.getTypeface() == Typeface.DEFAULT);
        appCompatRadioButtonMonospace.setChecked(mPaint.getTypeface() == Typeface.MONOSPACE);
        appCompatRadioButtonSansSerif.setChecked(mPaint.getTypeface() == Typeface.SANS_SERIF);
        appCompatRadioButtonSerif.setChecked(mPaint.getTypeface() == Typeface.SERIF);
    }

    // INTERFACE
    public void setOnCustomViewDialogListener(OnCustomViewDialogListener onCustomViewDialogListener) {
        this.onCustomViewDialogListener = onCustomViewDialogListener;
    }

    public interface OnCustomViewDialogListener {
        void onRefreshPaint(Paint newPaint);
    }
}

