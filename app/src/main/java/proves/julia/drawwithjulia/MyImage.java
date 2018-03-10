package proves.julia.drawwithjulia;

import java.io.File;

/**
 * Created by julia on 26/08/16.
 * <p>
 * Class used to save information of the images shown in the gallery.
 * In addition to saving its file, it saves whether it has been long clicked
 * and if it has been ticked (the clicked images can be ticked, the ticked images can be deleted).
 */
public class MyImage {

    private File file;
    private boolean visible;
    private boolean ticked;

    public MyImage(File file) {

        this.file = file;
        visible = false;
        ticked = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }

    public File getFile() {
        return file;
    }

    public boolean isTicked() {
        return ticked;
    }

    public void setTicked(boolean ticked) {
        this.ticked = ticked;
    }

}
