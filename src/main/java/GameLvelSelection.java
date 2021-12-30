import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class GameLvelSelection extends Display {
    GameDisplay.GameInfo gameInfo;

    GameLvelSelection(GameDisplay.GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    @Override
    public void show(GraphicsInfo ginfo) {
        ginfo.g.setColor(new Color(230, 230, 230));
        ginfo.g.fill(new Rectangle2D.Double(50, 70, 700, 400));
        if(this.gameInfo.AIMode==1){
            AILevel(ginfo);
            AILookAheadNumber(ginfo);
            cpuPlayerSet(ginfo);
        }
        else if(this.gameInfo.AIMode==2){
            AITime(ginfo);
            AILookAheadNumber(ginfo);
            cpuPlayerSet(ginfo);
        }
        else if(this.gameInfo.AIMode==3){
            AITime(ginfo);
            AILookAheadNumber(ginfo);
            cpuPlayerSet(ginfo);
        }
        gamePlay(ginfo);
        gameInfo.cpu.AIPlace = null;
    }

    @Override
    public void loadMedia() {

    }

    void AILevel(GraphicsInfo ginfo) {
        //gameInfo.gameMode
        int fontSize = 50;
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);
        ginfo.g.setFont(mfont);
        FontMetrics fm = ginfo.g.getFontMetrics();

        String down = "◀";
        String up = "▶";

        int boxWidth = 50;
        int boxHeight = 50;

        float boxXDown = 400;
        float boxXUp = boxXDown + 250;
        float boxY = 110;
        float strX = 100;

        //UPボタン
        float strw = fm.stringWidth(up) / 2;
        float strh = fm.getHeight();
        if (ginfo.cursorX > boxXUp & ginfo.cursorX < boxXUp + boxWidth & ginfo.cursorY > boxY & ginfo.cursorY < boxY + boxHeight) {
            ginfo.g.setColor(Color.YELLOW);
        } else {
            ginfo.g.setColor(Color.WHITE);
        }
        ginfo.g.fill(new Rectangle2D.Double(boxXUp, boxY, boxWidth, boxHeight));
        ginfo.g.setColor(Color.BLACK);
        //ginfo.g.draw(new Rectangle2D.Double(boxXUp,boxY, boxWidth,boxHeight));
        ginfo.g.drawString(up, boxXUp + boxWidth / 2 - strw, boxY + boxHeight / 2 + strh / 4);
        if (ginfo.clickX > boxXUp & ginfo.clickX < boxXUp + boxWidth & ginfo.clickY > boxY & ginfo.clickY < boxY + boxHeight) {
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            if (this.gameInfo.gameMode < 2) {
                this.gameInfo.gameMode++;
            }
        }

        //Downボタン
        strw = fm.stringWidth(down) / 2;
        strh = fm.getHeight();
        if (ginfo.cursorX > boxXDown & ginfo.cursorX < boxXDown + boxWidth & ginfo.cursorY > boxY & ginfo.cursorY < boxY + boxHeight) {
            ginfo.g.setColor(Color.YELLOW);
        } else {
            ginfo.g.setColor(Color.WHITE);
        }
        ginfo.g.fill(new Rectangle2D.Double(boxXDown, boxY, boxWidth, boxHeight));
        ginfo.g.setColor(Color.BLACK);
        ginfo.g.drawString(down, boxXDown + boxWidth / 2 - strw, boxY + boxHeight / 2 + strh / 4);
        if (ginfo.clickX > boxXDown & ginfo.clickX < boxXDown + boxWidth & ginfo.clickY > boxY & ginfo.clickY < boxY + boxHeight) {
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            if (this.gameInfo.gameMode > 0) {
                this.gameInfo.gameMode--;
            }
        }

        if (this.gameInfo.gameMode == 0) {
            strw = fm.stringWidth("よわい") / 2;
            ginfo.g.drawString("よわい", (boxXUp + boxXDown + boxWidth) / 2 - strw, boxY + boxHeight / 2 + strh / 4);
        } else if (this.gameInfo.gameMode == 1) {
            strw = fm.stringWidth("ふつう") / 2;
            ginfo.g.drawString("ふつう", (boxXUp + boxXDown + boxWidth) / 2 - strw, boxY + boxHeight / 2 + strh / 4);
        } else if (this.gameInfo.gameMode == 2) {
            strw = fm.stringWidth("つよい") / 2;
            ginfo.g.drawString("つよい", (boxXUp + boxXDown + boxWidth) / 2 - strw, boxY + boxHeight / 2 + strh / 4);
        }
        ginfo.g.drawString("つよさ : ", strX, boxY + boxHeight / 2 + strh / 4);
    }

    void AILookAheadNumber(GraphicsInfo ginfo) {
        //gameInfo.gameMode
        int fontSize = 50;
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);
        ginfo.g.setFont(mfont);
        FontMetrics fm = ginfo.g.getFontMetrics();

        String down = "◀";
        String up = "▶";

        int boxWidth = 50;
        int boxHeight = 50;

        float boxXDown = 400;
        float boxXUp = boxXDown + 250;
        float boxY = 240;
        float strX = 80;

        //UPボタン
        float strw = fm.stringWidth(up) / 2;
        float strh = fm.getHeight();
        if (ginfo.cursorX > boxXUp & ginfo.cursorX < boxXUp + boxWidth & ginfo.cursorY > boxY & ginfo.cursorY < boxY + boxHeight) {
            ginfo.g.setColor(Color.YELLOW);
        } else {
            ginfo.g.setColor(Color.WHITE);
        }
        ginfo.g.fill(new Rectangle2D.Double(boxXUp, boxY, boxWidth, boxHeight));
        ginfo.g.setColor(Color.BLACK);
        //ginfo.g.draw(new Rectangle2D.Double(boxXUp,boxY, boxWidth,boxHeight));
        ginfo.g.drawString(up, boxXUp + boxWidth / 2 - strw, boxY + boxHeight / 2 + strh / 4);
        if (ginfo.clickX > boxXUp & ginfo.clickX < boxXUp + boxWidth & ginfo.clickY > boxY & ginfo.clickY < boxY + boxHeight) {
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            if (this.gameInfo.AILookAhead < 5) {
                this.gameInfo.AILookAhead++;
            }
        }

        //Downボタン
        strw = fm.stringWidth(down) / 2;
        strh = fm.getHeight();
        if (ginfo.cursorX > boxXDown & ginfo.cursorX < boxXDown + boxWidth & ginfo.cursorY > boxY & ginfo.cursorY < boxY + boxHeight) {
            ginfo.g.setColor(Color.YELLOW);
        } else {
            ginfo.g.setColor(Color.WHITE);
        }
        ginfo.g.fill(new Rectangle2D.Double(boxXDown, boxY, boxWidth, boxHeight));
        ginfo.g.setColor(Color.BLACK);
        ginfo.g.drawString(down, boxXDown + boxWidth / 2 - strw, boxY + boxHeight / 2 + strh / 4);
        if (ginfo.clickX > boxXDown & ginfo.clickX < boxXDown + boxWidth & ginfo.clickY > boxY & ginfo.clickY < boxY + boxHeight) {
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            if (this.gameInfo.AILookAhead > 1) {
                this.gameInfo.AILookAhead--;
            }
        }

        //数字部分の表示
        strw = fm.stringWidth(String.valueOf(this.gameInfo.AILookAhead)) / 2;
        ginfo.g.drawString(String.valueOf(this.gameInfo.AILookAhead), (boxXUp + boxXDown + boxWidth) / 2 - strw, boxY + boxHeight / 2 + strh / 4);
        ginfo.g.drawString("先読み手数 : ", strX, boxY + boxHeight / 2 + strh / 4);
    }

    void cpuPlayerSet(GraphicsInfo ginfo){
        int fontSize = 50;
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);
        ginfo.g.setFont(mfont);
        FontMetrics fm = ginfo.g.getFontMetrics();

        String down = "◀";
        String up = "▶";

        int boxWidth = 50;
        int boxHeight = 50;

        float boxXDown = 400;
        float boxXUp = boxXDown+250;
        float boxY = 370;
        float strX = 80;

        //UPボタン
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
        //ginfo.g.draw(new Rectangle2D.Double(boxXUp,boxY, boxWidth,boxHeight));
        ginfo.g.drawString(up, boxXUp + boxWidth/2 - strw, boxY + boxHeight/2 + strh / 4);
        if (ginfo.clickX>boxXUp&ginfo.clickX<boxXUp+boxWidth&ginfo.clickY>boxY&ginfo.clickY<boxY+boxHeight){
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            this.gameInfo.cpuPlayer = this.gameInfo.cpuPlayer%2+1;
        }

        //Downボタン
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
            this.gameInfo.cpuPlayer = this.gameInfo.cpuPlayer%2+1;
        }

        //数字部分の表示
        if(this.gameInfo.cpuPlayer==1){
            strw = fm.stringWidth("先手")/2;
            ginfo.g.drawString("先手", (boxXUp + boxXDown + boxWidth)/2 - strw, boxY + boxHeight/2 + strh / 4);
        }
        else if(this.gameInfo.cpuPlayer==2){
            strw = fm.stringWidth("後手")/2;
            ginfo.g.drawString("後手", (boxXUp + boxXDown + boxWidth)/2 - strw, boxY + boxHeight/2 + strh / 4);
        }
        ginfo.g.drawString("CPUの手番 : ", strX, boxY + boxHeight/2 + strh / 4);
    }

    void gamePlay(GraphicsInfo ginfo){
        String str = "Game Start！";
        int fontSize = 70;
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);
        int boxWidth = 700;
        int boxHeight = 100;
        //左上座標
        float boxX;//計算で出すので代入はしない
        float boxY =500;

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
            gameInfo.goPlayGame();
        }
    }

    void AITime(GraphicsInfo ginfo) {
        //gameInfo.gameMode
        int fontSize = 50;
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);
        ginfo.g.setFont(mfont);
        FontMetrics fm = ginfo.g.getFontMetrics();

        String down = "◀";
        String up = "▶";

        int boxWidth = 50;
        int boxHeight = 50;

        float boxXDown = 400;
        float boxXUp = boxXDown + 250;
        float boxY = 110;
        float strX = 100;

        //UPボタン
        float strw = fm.stringWidth(up) / 2;
        float strh = fm.getHeight();
        if (ginfo.cursorX > boxXUp & ginfo.cursorX < boxXUp + boxWidth & ginfo.cursorY > boxY & ginfo.cursorY < boxY + boxHeight) {
            ginfo.g.setColor(Color.YELLOW);
        } else {
            ginfo.g.setColor(Color.WHITE);
        }
        ginfo.g.fill(new Rectangle2D.Double(boxXUp, boxY, boxWidth, boxHeight));
        ginfo.g.setColor(Color.BLACK);
        //ginfo.g.draw(new Rectangle2D.Double(boxXUp,boxY, boxWidth,boxHeight));
        ginfo.g.drawString(up, boxXUp + boxWidth / 2 - strw, boxY + boxHeight / 2 + strh / 4);
        if (ginfo.clickX > boxXUp & ginfo.clickX < boxXUp + boxWidth & ginfo.clickY > boxY & ginfo.clickY < boxY + boxHeight) {
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            if (this.gameInfo.timeMode < 3) {
                this.gameInfo.timeMode++;
            }
        }

        //Downボタン
        strw = fm.stringWidth(down) / 2;
        strh = fm.getHeight();
        if (ginfo.cursorX > boxXDown & ginfo.cursorX < boxXDown + boxWidth & ginfo.cursorY > boxY & ginfo.cursorY < boxY + boxHeight) {
            ginfo.g.setColor(Color.YELLOW);
        } else {
            ginfo.g.setColor(Color.WHITE);
        }
        ginfo.g.fill(new Rectangle2D.Double(boxXDown, boxY, boxWidth, boxHeight));
        ginfo.g.setColor(Color.BLACK);
        ginfo.g.drawString(down, boxXDown + boxWidth / 2 - strw, boxY + boxHeight / 2 + strh / 4);
        if (ginfo.clickX > boxXDown & ginfo.clickX < boxXDown + boxWidth & ginfo.clickY > boxY & ginfo.clickY < boxY + boxHeight) {
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            if (this.gameInfo.timeMode > 0) {
                this.gameInfo.timeMode--;
            }
        }

        if (this.gameInfo.timeMode == 0) {
            gameInfo.cpu.maxTime = 10 * 1000;
            strw = fm.stringWidth("10 [s]") / 2;
            ginfo.g.drawString("10 [s]", (boxXUp + boxXDown + boxWidth) / 2 - strw, boxY + boxHeight / 2 + strh / 4);
        } else if (this.gameInfo.timeMode == 1) {
            gameInfo.cpu.maxTime = 20 * 1000;
            strw = fm.stringWidth("20 [s]") / 2;
            ginfo.g.drawString("20 [s]", (boxXUp + boxXDown + boxWidth) / 2 - strw, boxY + boxHeight / 2 + strh / 4);
        } else if (this.gameInfo.timeMode == 2) {
            gameInfo.cpu.maxTime = 30 * 1000;
            strw = fm.stringWidth("30 [s]") / 2;
            ginfo.g.drawString("30 [s]", (boxXUp + boxXDown + boxWidth) / 2 - strw, boxY + boxHeight / 2 + strh / 4);
        }
        else if (this.gameInfo.timeMode == 3) {
            gameInfo.cpu.maxTime = 60 * 1000;
            strw = fm.stringWidth("60 [s]") / 2;
            ginfo.g.drawString("60 [s]", (boxXUp + boxXDown + boxWidth) / 2 - strw, boxY + boxHeight / 2 + strh / 4);
        }

        ginfo.g.drawString("思考時間 : ", strX, boxY + boxHeight / 2 + strh / 4);
    }

}
    //this.gameInfo
