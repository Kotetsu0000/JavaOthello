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
        ginfo.g.setColor(new Color(230, 230, 230));
        ginfo.g.fill(new Rectangle2D.Double(50, 70, 700, 410));
        BGMVolumeSet(ginfo);
        SEVolumeSet(ginfo);
        returnTitle(ginfo);
    }

    @Override
    public void loadMedia() throws IOException {

    }

    void BGMVolumeSet(GraphicsInfo ginfo){
        int fontSize = 70;
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);
        ginfo.g.setFont(mfont);
        FontMetrics fm = ginfo.g.getFontMetrics();

        String down = "◀";
        String up = "▶";

        int boxWidth = 70;
        int boxHeight = 70;

        float boxXDown = 400;
        float boxXUp = 600;
        float boxY = 160;
        float strX = 100;

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
                this.gameInfo.click();
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
                this.gameInfo.click();
                this.gameInfo.bgmVolume--;
            }
        }

        ginfo.g.drawString("BGM : ", strX, boxY + boxHeight / 2 + strh / 4);
        //数字部分の表示
        strw = fm.stringWidth(String.valueOf((int)this.gameInfo.bgmVolume))/2;
        ginfo.g.drawString(String.valueOf((int)this.gameInfo.bgmVolume), (boxXUp + boxXDown + boxWidth)/2 - strw, boxY + boxHeight/2 + strh / 4);
    }

    void SEVolumeSet(GraphicsInfo ginfo){
        int fontSize = 70;
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);
        ginfo.g.setFont(mfont);
        FontMetrics fm = ginfo.g.getFontMetrics();

        String down = "◀";
        String up = "▶";

        int boxWidth = 70;
        int boxHeight = 70;

        float boxXDown = 400;
        float boxXUp = 600;
        float boxY = 320;
        float strX = 100;

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
            if (this.gameInfo.seVolume<10){
                this.gameInfo.seVolume++;
                this.gameInfo.click();
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
            if (this.gameInfo.seVolume>0){
                this.gameInfo.seVolume--;
                this.gameInfo.click();
            }
        }

        ginfo.g.drawString("SE : ", strX, boxY + boxHeight / 2 + strh / 4);
        //数字部分の表示
        strw = fm.stringWidth(String.valueOf((int)this.gameInfo.seVolume))/2;
        ginfo.g.drawString(String.valueOf((int)this.gameInfo.seVolume), (boxXUp + boxXDown + boxWidth)/2 - strw, boxY + boxHeight/2 + strh / 4);
    }

    void returnTitle(GraphicsInfo ginfo){
        //戻るボタン
        String str = "戻る";
        int boxWidth = 150;
        int boxHeight = 70;
        //左上座標
        float boxX =600;
        float boxY =520;

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
            this.gameInfo.click();
            this.gameInfo.goSelectMode();
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
