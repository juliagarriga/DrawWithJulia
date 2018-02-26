package proves.julia.drawwithjulia;

import android.graphics.Path;

/**
 * Created by julia on 2/20/18.
 */

public class MyPath extends Path {

    private boolean isErased;
    private int id;

    public MyPath(int id) {
        super();

        this.id = id;
        this.isErased = false;
    }

    public void erase() {

        this.isErased =  true;
    }

    public void unerase() {

        this.isErased = false;
    }

    public boolean isErased() {

        return isErased;
    }

    public int getId() {

        return id;
    }


}
