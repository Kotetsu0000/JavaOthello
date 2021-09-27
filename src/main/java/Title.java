import org.nd4j.common.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Title extends Display{
    GameDisplay.GameInfo gameInfo;
    private BufferedImage imgTitle =null;
    private BufferedImage imgIcon =null;

    Title(GameDisplay.GameInfo gameInfo){
        this.gameInfo = gameInfo;
    }

    @Override
    public void show(GraphicsInfo ginfo) {
        ginfo.g.setBackground(new Color(0,128,0));
        ginfo.g.clearRect(0,0,(int) ginfo.windowWidth, (int) ginfo.windowHeight);

        ginfo.g.drawImage(this.imgTitle, (int)(ginfo.windowWidth-this.imgTitle.getTileWidth())/2, 200, null);
        ginfo.g.drawImage(this.imgIcon, (int)(ginfo.windowWidth-this.imgTitle.getTileWidth())/2, 300, null);

        singlePlay(ginfo);
        setting(ginfo);
    }

    @Override
    public void loadMedia() throws IOException {
        this.imgTitle = ImageIO.read(new File(new ClassPathResource("img\\title.png").getFile().getPath()));
        this.imgIcon = ImageIO.read(new File(new ClassPathResource("img\\icon.png").getFile().getPath()));
    }

    void singlePlay(GraphicsInfo ginfo){
        //ひとりプレイ
        String str = "1 Player";
        int boxWidth = 300;
        int boxHeight = 70;
        //左上座標
        float boxX;//計算で出すので代入はしない
        float boxY =600;

        int fontSize = 50;
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);

        ginfo.g.setFont(mfont);
        FontMetrics fm = ginfo.g.getFontMetrics();
        float strw = fm.stringWidth(str) / 2;
        float strh = fm.getHeight();

        boxX = ginfo.windowWidth/2 - boxWidth / 2;

        if (ginfo.cursorX>boxX & ginfo.cursorX<boxX + boxWidth & ginfo.cursorY > boxY & ginfo.cursorY < boxY + boxHeight){
            ginfo.g.setColor(Color.YELLOW);
        }
        else{
            ginfo.g.setColor(Color.WHITE);
        }
        Rectangle2D.Double play1 = new Rectangle2D.Double(boxX,boxY, boxWidth,boxHeight);
        ginfo.g.fill(play1);

        ginfo.g.setColor(Color.BLACK);
        ginfo.g.drawString(str, ginfo.windowWidth/2 - strw, boxY + boxHeight/2 + strh / 4);

        if (ginfo.clickX>boxX&ginfo.clickX<boxX+boxWidth&ginfo.clickY>boxY&ginfo.clickY<boxY+boxHeight){
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            //cpuPlayer = 1;
            //goPlaygame();
        }
    }

    void setting(GraphicsInfo ginfo){
        //設定画面
        String str = "Setting";
        int boxWidth = 300;
        int boxHeight = 70;
        //左上座標
        float boxX;//計算で出すので代入はしない
        float boxY =700;

        int fontSize = 50;
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);

        ginfo.g.setFont(mfont);
        FontMetrics fm = ginfo.g.getFontMetrics();
        float strw = fm.stringWidth(str) / 2;
        float strh = fm.getHeight();

        boxX = ginfo.windowWidth/2 - boxWidth / 2;

        if (ginfo.cursorX>boxX & ginfo.cursorX<boxX + boxWidth & ginfo.cursorY > boxY & ginfo.cursorY < boxY + boxHeight){
            ginfo.g.setColor(Color.YELLOW);
        }
        else{
            ginfo.g.setColor(Color.WHITE);
        }
        Rectangle2D.Double play1 = new Rectangle2D.Double(boxX,boxY, boxWidth,boxHeight);
        ginfo.g.fill(play1);

        ginfo.g.setColor(Color.BLACK);
        ginfo.g.drawString(str, ginfo.windowWidth/2 - strw, boxY + boxHeight/2 + strh / 4);

        if (ginfo.clickX>boxX&ginfo.clickX<boxX+boxWidth&ginfo.clickY>boxY&ginfo.clickY<boxY+boxHeight){
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            this.gameInfo.goSetting();
        }
    }
}
