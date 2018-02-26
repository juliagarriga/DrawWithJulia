package proves.julia.drawwithjulia;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.byox.drawview.enums.BackgroundScale;
import com.byox.drawview.enums.BackgroundType;
import com.byox.drawview.enums.DrawingMode;
import com.byox.drawview.enums.DrawingTool;
import com.byox.drawview.views.DrawView;

import java.io.File;

/**
 * Created by julia on 2/14/18.
 */

public class EditImageActivity extends Activity {

    private DrawView myDrawView;
    private FrameLayout parentLayout;
    private LinearLayout pencilButton, undoButton, redoButton, eraseButton, textButton,
            figuresButton, rightLayout, lineButton, arrowButton, rectangleButton,
            circleButton, ellipseButton;
    private ImageView figuresImage;
    private TextView figuresText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        Bitmap bitmap;

        setLayouts();

        try {

            final String filepath = getIntent().getStringExtra("path");

            myDrawView.post(new Runnable() {
                @Override
                public void run() {

                    if (filepath != null && !filepath.isEmpty())
                        myDrawView.setBackgroundImage(new File(filepath), BackgroundType.FILE, BackgroundScale.CENTER_CROP);
                }
            });


        } catch (NullPointerException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setLayouts() {

        myDrawView = (DrawView) findViewById(R.id.draw_view);

        parentLayout = (FrameLayout) findViewById(R.id.parentLayout);
        rightLayout = (LinearLayout) findViewById(R.id.rightLayout);
        pencilButton = (LinearLayout) findViewById(R.id.pencilButton);
        undoButton = (LinearLayout) findViewById(R.id.undoButton);
        redoButton = (LinearLayout) findViewById(R.id.redoButton);
        eraseButton = (LinearLayout) findViewById(R.id.eraseButton);
        textButton = (LinearLayout) findViewById(R.id.textButton);
        figuresButton = (LinearLayout) findViewById(R.id.figuresButton);
        lineButton = (LinearLayout) findViewById(R.id.lineButton);
        arrowButton = (LinearLayout) findViewById(R.id.arrowButton);
        rectangleButton = (LinearLayout) findViewById(R.id.rectangleButton);
        circleButton = (LinearLayout) findViewById(R.id.circleButton);
        ellipseButton = (LinearLayout) findViewById(R.id.ellipseButton);

        figuresImage = (ImageView) findViewById(R.id.figuresImage);
        figuresText = (TextView) findViewById(R.id.figuresText);

        parentLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        pencilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDrawView.setDrawingMode(DrawingMode.DRAW);
                myDrawView.setDrawingTool(DrawingTool.PEN);
                figuresImage.setImageResource(R.drawable.group);
                figuresText.setText(getResources().getString(R.string.figures));
            }
        });

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For make Undo action
                if (myDrawView.canUndo())
                    myDrawView.undo();

            }
        });

        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For make Redo action
                if (myDrawView.canRedo())
                    myDrawView.redo();
            }
        });

        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingMode(DrawingMode.ERASER);
            }
        });

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingMode(DrawingMode.TEXT);
            }
        });

        figuresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invertRightLayout();
            }
        });

        lineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingTool(DrawingTool.LINE);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.line);
                figuresText.setText(getResources().getString(R.string.line));
            }
        });

        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingTool(DrawingTool.ARROW);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.arrow);
                figuresText.setText(getResources().getString(R.string.arrow));
            }
        });

        rectangleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingTool(DrawingTool.RECTANGLE);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.rectangle);
                figuresText.setText(getResources().getString(R.string.rectangle));
            }
        });

        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingTool(DrawingTool.CIRCLE);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.circle);
                figuresText.setText(getResources().getString(R.string.circle));
            }
        });

        ellipseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingTool(DrawingTool.ELLIPSE);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.ellipse);
                figuresText.setText(getResources().getString(R.string.ellipse));
            }
        });
    }

    private void invertRightLayout() {

        ViewGroup.LayoutParams params = rightLayout.getLayoutParams();

        params.height = params.height == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : 0;

        rightLayout.setLayoutParams(params);
    }
}
