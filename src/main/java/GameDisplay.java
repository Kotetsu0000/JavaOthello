import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;

import java.io.IOException;

public class GameDisplay extends Display{

    GameInfo gameInfo;
    Display title, setting, selectMode, levelSelection, playGame;
    BGM bgm;

    String titleBGM = "bgm\\yume.wav";
    String settingBGM = "bgm\\houkagonoyuzora.wav";
    String selectModeBGM = "bgm\\houkagonoyuzora.wav";
    String levelSelectionBGM = "bgm\\houkagonoyuzora.wav";
    String playGameBGM = "bgm\\houkagonoyuzora.wav";

    GameDisplay(){
        this.gameInfo = new GameInfo();

        this.bgm = new BGM(gameInfo);
        this.bgm.changeBGM(titleBGM);
        this.bgm.start();

        this.title = new Title(this.gameInfo);
        this.setting = new Setting(this.gameInfo);
        this.selectMode = new SelectMode(this.gameInfo);
        this.levelSelection = new GameLvelSelection(this.gameInfo);
        this.playGame = new PlayGame(this.gameInfo);
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
        float bgmVolume = 3;
        int gameMode = 1;//強さの選択
        int AILookAhead = 3;//AIの予測手数
        AI cpu;
        int cpuPlayer = 1;//CPUの手番. 1:先手, 2:後手
        double[] predDisplay;
        GameInfo(){
            othello = new Othello();
            try {
                cpu = new AI();
                cpu.start();
                System.out.println("CPU Start !!");
                cpu.othello = othello;
                predDisplay = cpu.predict(othello.board);
            } catch (IOException | InvalidKerasConfigurationException | UnsupportedKerasConfigurationException e) {
                System.err.println(e.getMessage());
            }
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
        void goSelectMode(){
            bgm.clip.close();
            bgm.changeBGM(selectModeBGM);
            goScene(selectMode);
        }
        void goLevelSelection(){
            bgm.clip.close();
            bgm.changeBGM(levelSelectionBGM);
            goScene(levelSelection);
        }
        void goPlayGame(){
            bgm.clip.close();
            bgm.changeBGM(playGameBGM);
            goScene(playGame);
        }

        //リストの中に要素が入っているかの判定
        boolean listIn(int[] place, int[][] list){
            System.out.println(list.length);
            for (int i =0;i<list.length;i++){
                if (list[i][0]==place[0]&list[i][1]==place[1]){
                    return true;
                }
            }
            return false;
        }
    }
}
