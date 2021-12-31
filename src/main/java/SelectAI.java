import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class SelectAI extends Display{
    GameDisplay.GameInfo gameInfo;
    SelectAI(GameDisplay.GameInfo gameInfo){
        this.gameInfo = gameInfo;
    }
    @Override
    public void show(GraphicsInfo ginfo) {
        //「AI選択」の文字
        String str = "AI選択";
        //左上座標
        float strX = 80;
        float strY = 130;

        int fontSize = 60;
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);
        ginfo.g.setFont(mfont);

        ginfo.g.setColor(Color.BLACK);
        ginfo.g.drawString(str, strX, strY);

        this.ValueMiniMax(ginfo,500, 100, 190);
        this.ValueMCTS(ginfo, 500, 100, 320);
        this.PolicyValueMCTS(ginfo, 500, 100, 450);

    }

    @Override
    public void loadMedia() throws IOException {

    }

    void ValueMiniMax(GraphicsInfo ginfo, int boxWidth, int boxHeight, float boxY){
        //ひとりプレイ
        String str = "V-MiniMax";

        //左上座標
        float boxX;//計算で出すので代入はしない

        int fontSize = 70;
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
            this.gameInfo.AIMode = 1;
            this.gameInfo.click();
            this.gameInfo.goLevelSelection(false);
        }
    }

    void ValueMCTS(GraphicsInfo ginfo, int boxWidth, int boxHeight, float boxY){
        //ひとりプレイ
        String str = "V-MCTS";

        //左上座標
        float boxX;//計算で出すので代入はしない

        int fontSize = 70;
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
            this.gameInfo.AIMode = 2;
            this.gameInfo.click();
            this.gameInfo.goLevelSelection(false);
        }
    }

    void PolicyValueMCTS(GraphicsInfo ginfo, int boxWidth, int boxHeight, float boxY){
        //ひとりプレイ
        String str = "PV-MCTS";

        //左上座標
        float boxX;//計算で出すので代入はしない

        int fontSize = 70;
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
            this.gameInfo.AIMode = 3;
            this.gameInfo.click();
            this.gameInfo.goLevelSelection(false);
        }
    }

}
