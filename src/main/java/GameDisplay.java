import java.io.IOException;

public class GameDisplay extends Display{

    GameInfo gameInfo;
    Display title, setting, playGame, levelSelection;

    GameDisplay(){
        this.gameInfo = new GameInfo();
        this.title = new Title(this.gameInfo);
        GameDisplay.current = this.title;
    }

    @Override
    public void show(GraphicsInfo ginfo) {

    }

    @Override
    public void loadMedia() throws IOException {
        this.title.loadMedia();
    }

    class GameInfo{
        public Othello othello;
        //AI cpu;
        int cpuPlayer = 0;//CPUの手番. 1:先手, 2:後手
        GameInfo(){
            othello = new Othello();
        }
    }
}
