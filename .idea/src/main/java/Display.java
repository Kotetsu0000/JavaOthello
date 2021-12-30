import java.io.IOException;
public abstract class Display {
    protected static Display current = null;

    //現在のディスプレイを返す
    public Display getCurrentDisplay(){
        return Display.current;
    }

    //このディスプレイを表示
    public abstract void show(GraphicsInfo ginfo);

    public abstract void loadMedia() throws IOException;
}
