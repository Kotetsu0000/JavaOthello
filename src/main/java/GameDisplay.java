import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;

import java.io.IOException;

public class GameDisplay extends Display{

    GameInfo gameInfo;
    Display title, setting, selectAI, selectMode, levelSelection, playGame;
    BGM bgm;
    SE se;

    String titleBGM = "bgm\\yume.wav";
    String settingBGM = "bgm\\houkagonoyuzora.wav";
    String selectModeBGM = "bgm\\houkagonoyuzora.wav";
    String levelSelectionBGM = "bgm\\houkagonoyuzora.wav";
    String playGameBGM = "bgm\\houkagonoyuzora.wav";
    String selectAIBGM = "bgm\\houkagonoyuzora.wav";

    String click = "se\\click.wav";

    GameDisplay(){
        this.gameInfo = new GameInfo();

        this.bgm = new BGM(gameInfo);
        this.bgm.changeBGM(titleBGM);
        this.bgm.start();

        this.se = new SE(gameInfo);

        this.title = new Title(this.gameInfo);
        this.setting = new Setting(this.gameInfo);
        this.selectAI = new SelectAI(this.gameInfo);
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
        LoadJson config;
        float bgmVolume = 5;
        float seVolume = 5;
        int AIMode = 1;//AIの種類の選択. 1:ValueMiniMax, 2:ValueMCTS, 3:PolicyValueMCTS
        int gameMode = 1;//強さの選択
        int AILookAhead = 3;//AIの予測手数
        int timeMode = 0;//AIの探索時間モード選択用 0:10[s], 1:20[s], 2:30[s], 3:60[s]
        AI cpu;
        int cpuPlayer = 2;//CPUの手番. 1:先手, 2:後手
        double[] predDisplay;
        GameInfo(){
            othello = new Othello();
            try {
                cpu = new AI();
                cpu.start();
                System.out.println("CPU Start !!");
                cpu.othello = othello;
                predDisplay = cpu.boardPred.predict(othello.board);

                this.config = new LoadJson();
                this.bgmVolume = this.config.BGM;
                this.seVolume = this.config.SE;
            } catch (IOException | InvalidKerasConfigurationException | UnsupportedKerasConfigurationException e) {
                System.err.println(e.getMessage());
            }
        }
        void playSE(String filePath){
            se.playSE(filePath);
        }
        void click(){
            se.playSE(click);
        }

        void goScene(Display Scene){GameDisplay.current = Scene;}
        void goTitle(boolean changeBGM){
            if (changeBGM){
                bgm.clip.close();
                bgm.changeBGM(titleBGM);
            }
            goScene(title);
        }
        void goSetting(boolean changeBGM){
            if (changeBGM){
                bgm.clip.close();
                bgm.changeBGM(settingBGM);
            }
            goScene(setting);
        }
        void goSelectAI(boolean changeBGM){
            if (changeBGM){
                bgm.clip.close();
                bgm.changeBGM(selectAIBGM);
            }
            goScene(selectAI);
        }
        void goSelectMode(boolean changeBGM){
            if (changeBGM){
                bgm.clip.close();
                bgm.changeBGM(selectModeBGM);
            }
            goScene(selectMode);
        }
        void goLevelSelection(boolean changeBGM){
            if (changeBGM){
                bgm.clip.close();
                bgm.changeBGM(levelSelectionBGM);
            }
            goScene(levelSelection);
        }
        void goPlayGame(boolean changeBGM){
            if (changeBGM){
                bgm.clip.close();
                bgm.changeBGM(playGameBGM);
            }
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
