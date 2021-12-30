import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class Setting extends Display{
    GameDisplay.GameInfo gameInfo;

    Setting(GameDisplay.GameInfo gameInfo){
        this.gameInfo = gameInfo;
    }

    @Override
    public void show(GraphicsInfo ginfo) {
        volumeSet(ginfo);
        returnTitle(ginfo);
    }

    @Override
    public void loadMedia() throws IOException {

    }

    void volumeSet(GraphicsInfo ginfo){
        int fontSize = 70;
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);
        ginfo.g.setFont(mfont);
        FontMetrics fm = ginfo.g.getFontMetrics();

        String down = "◀";
        String up = "▶";

        int boxWidth = 70;
        int boxHeight = 70;

        float boxXDown = 200;
        float boxXUp = 400;
        float boxY = 200;

        //音量UPボタン
        float strw = fm.stringWidth(up)/2;
        float strh = fm.getHeight();
        if (ginfo.cursorX>boxXUp & ginfo.cursorX<boxXUp + boxWidth & ginfo.cursorY > boxY & ginfo.cursorY < boxY + boxHeight){
            ginfo.g.setColor(Color.YELLOW);
        }
        else{
            ginfo.g.setColor(Color.WHITE);
        }
        ginfo.g.fill(new Rectangle2D.Double(boxXUp,boxY, boxWidth,boxHeight));
        ginfo.g.setColor(Color.BLACK);
        ginfo.g.drawString(up, boxXUp + boxWidth/2 - strw, boxY + boxHeight/2 + strh / 4);
        if (ginfo.clickX>boxXUp&ginfo.clickX<boxXUp+boxWidth&ginfo.clickY>boxY&ginfo.clickY<boxY+boxHeight){
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            if (this.gameInfo.bgmVolume<10){
                this.gameInfo.bgmVolume++;
            }
        }

        //音量Downボタン
        strw = fm.stringWidth(down)/2;
        strh = fm.getHeight();
        if (ginfo.cursorX>boxXDown & ginfo.cursorX<boxXDown + boxWidth & ginfo.cursorY > boxY & ginfo.cursorY < boxY + boxHeight){
            ginfo.g.setColor(Color.YELLOW);
        }
        else{
            ginfo.g.setColor(Color.WHITE);
        }
        ginfo.g.fill(new Rectangle2D.Double(boxXDown,boxY, boxWidth,boxHeight));
        ginfo.g.setColor(Color.BLACK);
        ginfo.g.drawString(down, boxXDown + boxWidth/2 - strw, boxY + boxHeight/2 + strh / 4);
        if (ginfo.clickX>boxXDown&ginfo.clickX<boxXDown+boxWidth&ginfo.clickY>boxY&ginfo.clickY<boxY+boxHeight){
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            if (this.gameInfo.bgmVolume>0){
                this.gameInfo.bgmVolume--;
            }
        }

        //数字部分の表示
        strw = fm.stringWidth(String.valueOf((int)this.gameInfo.bgmVolume))/2;
        ginfo.g.drawString(String.valueOf((int)this.gameInfo.bgmVolume), (boxXUp + boxXDown + boxWidth)/2 - strw, boxY + boxHeight/2 + strh / 4);
    }

    void returnTitle(GraphicsInfo ginfo){
        //戻るボタン
        String str = "戻る";
        int boxWidth = 150;
        int boxHeight = 70;
        //左上座標
        float boxX =900;//計算で出すので代入はしない
        float boxY =750;

        int fontSize = 50;
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);

        ginfo.g.setFont(mfont);
        FontMetrics fm = ginfo.g.getFontMetrics();
        float strw = fm.stringWidth(str)/2;
        float strh = fm.getHeight();

        if (ginfo.cursorX>boxX & ginfo.cursorX<boxX + boxWidth & ginfo.cursorY > boxY & ginfo.cursorY < boxY + boxHeight){
            ginfo.g.setColor(Color.YELLOW);
        }
        else{
            ginfo.g.setColor(Color.WHITE);
        }
        Rectangle2D.Double play1 = new Rectangle2D.Double(boxX,boxY, boxWidth,boxHeight);
        ginfo.g.fill(play1);

        ginfo.g.setColor(Color.BLACK);
        ginfo.g.drawString(str, boxX + boxWidth/2 - strw, boxY + boxHeight/2 + strh / 4);

        if (ginfo.clickX>boxX&ginfo.clickX<boxX+boxWidth&ginfo.clickY>boxY&ginfo.clickY<boxY+boxHeight){
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            this.gameInfo.goTitle();
        }
    }

    void setButton(GraphicsInfo ginfo, String str, int boxWidth, int boxHeight, float boxX, float boxY,int fontSize){
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);

        ginfo.g.setFont(mfont);
        FontMetrics fm = ginfo.g.getFontMetrics();
        float strw = fm.stringWidth(str)/2;
        float strh = fm.getHeight();

        if (ginfo.cursorX>boxX & ginfo.cursorX<boxX + boxWidth & ginfo.cursorY > boxY & ginfo.cursorY < boxY + boxHeight){
            ginfo.g.setColor(Color.YELLOW);
        }
        else{
            ginfo.g.setColor(Color.WHITE);
        }
        Rectangle2D.Double play1 = new Rectangle2D.Double(boxX,boxY, boxWidth,boxHeight);
        ginfo.g.fill(play1);

        ginfo.g.setColor(Color.BLACK);
        ginfo.g.drawString(str, boxX + boxWidth/2 - strw, boxY + boxHeight/2 + strh / 4);

        if (ginfo.clickX>boxX&ginfo.clickX<boxX+boxWidth&ginfo.clickY>boxY&ginfo.clickY<boxY+boxHeight){
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            this.gameInfo.goTitle();
        }
    }
}
