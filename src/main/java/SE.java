import org.nd4j.common.io.ClassPathResource;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Timer;
import java.util.TimerTask;

public class SE extends Thread{

    Clip clip;
    GameDisplay.GameInfo gameInfo;

    //コンストラクタ
    SE(GameDisplay.GameInfo gameInfo){
        this.gameInfo = gameInfo;
    }
    public void run() {
        Timer t = new Timer(true);
        t.schedule(new RenderTask(),0,1);
    }

    public void playSE(String filePath){
        if(this.clip != null){this.clip.stop();}
        this.clip = createClip(filePath);
        FloatControl ctrl = (FloatControl)this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        ctrl.setValue((float)Math.log10(this.gameInfo.seVolume / 10)*20);
        this.clip.start();
        //this.clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    class RenderTask extends TimerTask {

        @Override
        public void run() {
            SE.this.render();
        }
    }

    void render(){
        FloatControl ctrl = (FloatControl)this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        ctrl.setValue((float)Math.log10(this.gameInfo.seVolume / 10)*20);
    }

    public static Clip createClip(String path) {
        //指定されたURLのオーディオ入力ストリームを取得
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(new File(new ClassPathResource(path).getFile().getPath()))){

            //ファイルの形式取得
            AudioFormat af = ais.getFormat();

            //単一のオーディオ形式を含む指定した情報からデータラインの情報オブジェクトを構築
            DataLine.Info dataLine = new DataLine.Info(Clip.class,af);

            //指定された Line.Info オブジェクトの記述に一致するラインを取得
            Clip c = (Clip)AudioSystem.getLine(dataLine);

            //再生準備完了
            c.open(ais);

            return c;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }
}
