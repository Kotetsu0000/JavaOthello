import java.io.IOException;

public class GameDisplay extends Display{

    GameInfo gameInfo;
    Display title, setting, playGame, levelSelection;
    BGM bgm;

    String titleBGM = "bgm\\yume.wav";
    String settingBGM = "bgm\\houkagonoyuzora.wav";

    GameDisplay(){
        this.gameInfo = new GameInfo();

        this.bgm = new BGM(gameInfo);
        this.bgm.changeBGM(titleBGM);
        this.bgm.start();

        this.title = new Title(this.gameInfo);
        this.setting = new Setting(this.gameInfo);
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
        float bgmVolume;
        //AI cpu;
        int cpuPlayer = 0;//CPUの手番. 1:先手, 2:後手
        GameInfo(){
            othello = new Othello();
            this.bgmVolume = 5;
        }
        void goScene(Display Scene){GameDisplay.current = Scene;}
        void goTitle(){
            bgm.clip.close();
            bgm.changeBGM(titleBGM);
            goScene(title);
        }
        void goSetting(){
            bgm.clip.close();
            bgm.changeBGM(settingBGM);
            goScene(setting);
        }
    }
}
