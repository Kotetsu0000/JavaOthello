import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class PlayGame extends Display{
    GameDisplay.GameInfo gameInfo;

    //盤面サイズ
    int boardSize = 560;
    //左上座標
    int boardX = 20;
    int boardY = 55;
    boolean placeFlag = false;
    long placeFlagTime;

    //情報枠サイズ
    int infoWidth = 190;

    PlayGame(GameDisplay.GameInfo gameInfo){
        this.gameInfo = gameInfo;
    }
    @Override
    public void show(GraphicsInfo ginfo) {
        //描画部分
        boardBase(ginfo);
        if (gameInfo.cpuPlayer != gameInfo.othello.player){auxiliaryCircle(ginfo);}
        othelloPiece(ginfo);
        infomation(ginfo);
        exit(ginfo);

        //処理部分
        othelloPlay(ginfo);

        ginfo.clickX = -100;
        ginfo.clickY = -100;
    }

    @Override
    public void loadMedia() throws IOException {

    }

    //ボードの描画
    void boardBase(GraphicsInfo ginfo){
        //ボードの下地
        for (int i = 0; i < gameInfo.othello.board.length; i++){
            for (int j = 0; j < gameInfo.othello.board.length; j++){
                float lX = boardX + (boardSize / gameInfo.othello.board.length) * i;
                float rX = boardX + (boardSize / gameInfo.othello.board.length) * (i + 1);
                float uY = boardY + (boardSize / gameInfo.othello.board.length) * j;
                float dY = boardY + (boardSize / gameInfo.othello.board.length) * (j + 1);
                if (ginfo.cursorX > lX & ginfo.cursorX < rX & ginfo.cursorY > uY & ginfo.cursorY < dY){
                    ginfo.g.setColor(new Color(100,128,100));
                }
                else{
                    ginfo.g.setColor(new Color(0,128,0));
                }
                Rectangle2D.Double place = new Rectangle2D.Double(lX,uY,rX-lX,dY-uY);
                ginfo.g.fill(place);
            }
        }
        //周りの線
        ginfo.g.setColor(Color.BLACK);
        BasicStroke superwideStroke = new BasicStroke(3.0f);
        ginfo.g.setStroke(superwideStroke);
        ginfo.g.draw(new Rectangle2D.Double(boardX,boardY,boardSize,boardSize));
        for (int i = 1; i < gameInfo.othello.board.length; i++){
            ginfo.g.drawLine(boardX + (boardSize/gameInfo.othello.board.length) * i, boardY, boardX + (boardSize/gameInfo.othello.board.length) * i, boardSize + boardY);
            ginfo.g.drawLine(boardX, boardY + (boardSize/gameInfo.othello.board.length) * i, boardSize + boardX, boardY + (boardSize/gameInfo.othello.board.length) * i);
        }
    }

    //補助円の描画
    void auxiliaryCircle(GraphicsInfo ginfo){
        int[][] putPlace = gameInfo.othello.putPlace(gameInfo.othello.board, gameInfo.othello.player);
        ginfo.g.setColor(Color.BLACK);
        BasicStroke superwideStroke = new BasicStroke(1.5f);
        ginfo.g.setStroke(superwideStroke);
        //System.out.println(putPlace[0][0]+", "+putPlace[0][1]);
        for (int i = 0; i < gameInfo.othello.board.length; i++) {
            for (int j = 0; j < gameInfo.othello.board.length; j++) {
                for (int k = 0; k < putPlace.length; k++) {
                    if (putPlace[k][0] == i & putPlace[k][1] == j) {
                        ginfo.g.draw(new Ellipse2D.Double(boardX + (boardSize / gameInfo.othello.board.length) * i + 3.5, boardY + (boardSize / gameInfo.othello.board.length) * j + 3.5, boardSize / 8 - 7, boardSize / gameInfo.othello.board.length - 7));
                        break;
                    }
                }
            }
        }
    }

    //オセロの駒描画
    void othelloPiece(GraphicsInfo ginfo){
        //駒配置
        for (int i = 0; i < gameInfo.othello.board.length; i++) {
            for (int j = 0; j < gameInfo.othello.board.length; j++) {
                if (gameInfo.othello.board[i][j] == 1) {
                    ginfo.g.setColor(Color.BLACK);
                    ginfo.g.fill(new Ellipse2D.Double(boardX + (boardSize / gameInfo.othello.board.length) * i + 3.5, boardY + (boardSize / gameInfo.othello.board.length) * j + 3.5, boardSize / 8 - 7, boardSize / gameInfo.othello.board.length - 7));
                } else if (gameInfo.othello.board[i][j] == 2) {
                    ginfo.g.setColor(Color.white);
                    ginfo.g.fill(new Ellipse2D.Double(boardX + (boardSize / gameInfo.othello.board.length) * i + 3.5, boardY + (boardSize / 8) * j + 3.5, boardSize / gameInfo.othello.board.length - 7, boardSize / gameInfo.othello.board.length - 7));
                }
            }
        }
    }

    //情報部分
    void infomation(GraphicsInfo ginfo){
        //情報表示用の枠1
        ginfo.g.setColor(Color.WHITE);
        Rectangle2D.Double block = new Rectangle2D.Double(boardSize + boardX + 10, boardY, infoWidth,120);
        ginfo.g.fill(block);

        //情報表示用の枠2
        ginfo.g.setColor(Color.WHITE);
        block = new Rectangle2D.Double(boardSize + boardX + 10, boardY + 130, infoWidth,60);
        ginfo.g.fill(block);
    }

    //ゲーム終了時に表示されるもう一度ボタン
    void restart(GraphicsInfo ginfo){
        String str = "もう一度";
        int fontSize = 40;
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);
        ginfo.g.setFont(mfont);

        int boxWidth = infoWidth;
        int boxHeight = 70;
        //左上座標
        float boxX = boardSize + boardX + 10;
        float boxY = boardY + boardSize - boxHeight - 80;

        FontMetrics fm = ginfo.g.getFontMetrics();
        float strw = fm.stringWidth(str) / 2;
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
        ginfo.g.drawString(str, boxX+boxWidth/2 - strw, boxY + boxHeight/2 + strh / 4);

        if (ginfo.clickX>boxX&ginfo.clickX<boxX+boxWidth&ginfo.clickY>boxY&ginfo.clickY<boxY+boxHeight){
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            gameInfo.othello.reset();
        }
    }

    //やめるボタン
    void exit(GraphicsInfo ginfo){
        String str = "やめる";
        int fontSize = 40;
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);
        ginfo.g.setFont(mfont);

        int boxWidth = infoWidth;
        int boxHeight = 70;
        //左上座標
        float boxX = boardSize + boardX + 10;
        float boxY = boardY + boardSize - boxHeight;

        FontMetrics fm = ginfo.g.getFontMetrics();
        float strw = fm.stringWidth(str) / 2;
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
        ginfo.g.drawString(str, boxX+boxWidth/2 - strw, boxY + boxHeight/2 + strh / 4);

        if (ginfo.clickX>boxX&ginfo.clickX<boxX+boxWidth&ginfo.clickY>boxY&ginfo.clickY<boxY+boxHeight){
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            gameInfo.othello.reset();
            gameInfo.cpuPlayer = 2;//CPUの手番. 1:先手, 2:後手
            gameInfo.AILookAhead = 3;//AIの予測手数
            gameInfo.gameMode = 1;//強さの選択
            gameInfo.goSelectMode();
        }
    }

    //カーソル座標認識
    int[] cursorPlace(GraphicsInfo ginfo){
        for (int i = 0; i < gameInfo.othello.board.length; i++){
            for (int j = 0; j < gameInfo.othello.board.length; j++){
                float lX = 20 + (boardSize / gameInfo.othello.board.length) * i;
                float rX = 20 + (boardSize / gameInfo.othello.board.length) * (i + 1);
                float uY = 50 + (boardSize / gameInfo.othello.board.length) * j;
                float dY = 50 + (boardSize / gameInfo.othello.board.length) * (j + 1);
                if (ginfo.clickX > lX & ginfo.clickX < rX & ginfo.clickY > uY & ginfo.clickY < dY){
                    return new int[]{i,j};
                }
            }
        }
        return new int[]{-1,-1};
    }
    
    //オセロ処理部分
    void othelloPlay(GraphicsInfo ginfo) {
        int[][] placeLen = gameInfo.othello.putPlace(gameInfo.othello.board, gameInfo.othello.player);
        int[] place = cursorPlace(ginfo);
        if (gameInfo.othello.passFlag < 2){
            if (placeLen.length != 0){//置ける場合
                //AI
                if (gameInfo.cpuPlayer == gameInfo.othello.player){
                    String str = "AI思考中";
                    ginfo.g.setFont(new Font("Sanserif", Font.BOLD, 20));
                    FontMetrics fm = ginfo.g.getFontMetrics();
                    float strh = fm.getHeight();
                    ginfo.g.setColor(Color.BLACK);
                    ginfo.g.drawString(str, boardSize + boardX + 15, boardY+130+strh);

                    //ginfo.g.drawString("探索盤面数 : "+String.valueOf(gameInfo.cpu.thinkNumber),boardSize + boardX + 15, boardY+130+strh*2);

                    if (gameInfo.cpu.thinkFlag){
                        gameInfo.cpu.thinkFlag=false;
                        gameInfo.cpu.predBoard = gameInfo.othello.board;
                        gameInfo.cpu.predPlayer = gameInfo.othello.player;
                        gameInfo.cpu.predNumber = gameInfo.AILookAhead;
                        gameInfo.cpu.predMode = gameInfo.gameMode;
                        gameInfo.cpu.thinkNumber=0;
                        gameInfo.cpu.predStart = true;
                    }
                    else if(gameInfo.cpu.AIPlace!=null){
                        System.out.println("0:"+gameInfo.cpu.AIPlace[0]+", 1:"+gameInfo.cpu.AIPlace[1]+", 2:"+gameInfo.cpu.AIPlace[2]+", 3:"+gameInfo.cpu.AIPlace[3]+", 4:"+gameInfo.cpu.AIPlace[4]);
                        gameInfo.othello.board = gameInfo.othello.put(gameInfo.othello.board, new int[]{(int)gameInfo.cpu.AIPlace[0], (int)gameInfo.cpu.AIPlace[1]}, gameInfo.othello.player);
                        gameInfo.othello.passFlag = 0;
                        gameInfo.othello.player %= 2;
                        gameInfo.othello.player += 1;
                        gameInfo.predDisplay = gameInfo.cpu.predict(gameInfo.othello.board);
                        gameInfo.cpu.AIPlace = null;
                        gameInfo.cpu.thinkFlag=true;
                    }
                }
                //Player VS Player
                else if(gameInfo.cpuPlayer == 0){
                    //黒番
                    if (gameInfo.othello.player == 1){
                        String str = "黒の番です。";
                        ginfo.g.setFont(new Font("Sanserif", Font.BOLD, 20));
                        FontMetrics fm = ginfo.g.getFontMetrics();
                        float strh = fm.getHeight();
                        ginfo.g.setColor(Color.BLACK);
                        ginfo.g.drawString(str, boardSize + boardX + 15, boardY+130+strh);
                    }
                    //白番
                    else{
                        String str = "白の番です。";
                        ginfo.g.setFont(new Font("Sanserif", Font.BOLD, 20));
                        FontMetrics fm = ginfo.g.getFontMetrics();
                        float strh = fm.getHeight();
                        ginfo.g.setColor(Color.BLACK);
                        ginfo.g.drawString(str, boardSize + boardX + 15, boardY+130+strh);
                    }

                    if (place[0]!=-1 && place[1]!=-1){
                        //クリックした場所が置ける場合
                        if (gameInfo.listIn(place,placeLen)){
                            gameInfo.othello.board = gameInfo.othello.put(gameInfo.othello.board, place, gameInfo.othello.player);
                            gameInfo.othello.passFlag = 0;
                            gameInfo.othello.player %= 2;
                            gameInfo.othello.player += 1;
                            gameInfo.predDisplay = gameInfo.cpu.predict(gameInfo.othello.board);
                            placeFlag = false;
                        }
                        //クリックした場所が置けない場合
                        else{
                            placeFlagTime = System.currentTimeMillis();
                            placeFlag = true;
                        }
                    }
                }
                //Player VS AI
                else{
                    String str = "あなたの番です。";
                    ginfo.g.setFont(new Font("Sanserif", Font.BOLD, 20));
                    FontMetrics fm = ginfo.g.getFontMetrics();
                    float strh = fm.getHeight();
                    ginfo.g.setColor(Color.BLACK);
                    ginfo.g.drawString(str, boardSize + boardX + 15, boardY+130+strh);
                    if (place[0]!=-1 && place[1]!=-1){
                        //クリックした場所が置ける場合
                        if (gameInfo.listIn(place,placeLen)){
                            gameInfo.othello.board = gameInfo.othello.put(gameInfo.othello.board, place, gameInfo.othello.player);
                            gameInfo.othello.passFlag = 0;
                            gameInfo.othello.player %= 2;
                            gameInfo.othello.player += 1;
                            gameInfo.predDisplay = gameInfo.cpu.predict(gameInfo.othello.board);
                            placeFlag = false;
                        }
                        //クリックした場所が置けない場合
                        else{
                            placeFlag = true;
                        }
                    }
                }
            }
            else {
                gameInfo.othello.passFlag += 1;
                gameInfo.othello.player %= 2;
                gameInfo.othello.player += 1;
            }
        }
        else{
            othelloResult(ginfo);
        }

        if (placeFlag){
            String str = "そこには置けません。";
            ginfo.g.setFont(new Font("Sanserif", Font.BOLD, 20));
            FontMetrics fm = ginfo.g.getFontMetrics();
            float strh = fm.getHeight();
            ginfo.g.setColor(Color.BLACK);
            ginfo.g.drawString(str, boardSize + boardX + 15, boardY+130+strh*2);
            if (System.currentTimeMillis() - placeFlagTime > 2000){
                placeFlag = false;
            }
        }
    }

    //結果表示
    void othelloResult(GraphicsInfo ginfo){
        //情報表示用の枠
        ginfo.g.setColor(Color.WHITE);
        Rectangle2D.Double block = new Rectangle2D.Double(boardSize + boardX + 10, boardY+200, infoWidth,60);
        ginfo.g.fill(block);

        ginfo.g.setFont(new Font("Sanserif", Font.BOLD, 20));
        FontMetrics fm = ginfo.g.getFontMetrics();
        float strh = fm.getHeight();
        ginfo.g.setColor(Color.BLACK);
        int[] stone = gameInfo.othello.result(gameInfo.othello.board);
        //黒番(先手)の勝ち
        if (stone[0]>stone[1]){
            String str = "黒の勝ち!!";
            String str2 = "黒 : " + String.format("%d", stone[0]) + ", 白 : " + String.format("%d", stone[1]);
            ginfo.g.drawString(str, boardSize + boardX + 15, boardY+200+strh);
            ginfo.g.drawString(str2, boardSize + boardX + 15, boardY+200+strh*2);
        }
        //白番(後手)の勝ち
        else if (stone[0]<stone[1]){
            String str = "白の勝ち!!";
            String str2 = "黒 : " + String.format("%d", stone[0]) + ", 白 : " + String.format("%d", stone[1]);
            ginfo.g.drawString(str, boardSize + boardX + 15, boardY+200+strh);
            ginfo.g.drawString(str2, boardSize + boardX + 15, boardY+200+strh*2);
        }
        //引き分け
        else{
            String str = "引き分け!!";
            String str2 = "黒 : " + String.format("%d", stone[0]) + ", 白 : " + String.format("%d", stone[1]);
            ginfo.g.drawString(str, boardSize + boardX + 15, boardY+200+strh);
            ginfo.g.drawString(str2, boardSize + boardX + 15, boardY+200+strh*2);
        }
        restart(ginfo);
        String str = "ゲーム終了";
        int fontSize = 35;
        Font mfont = new Font("Sanserif", Font.BOLD, fontSize);
        ginfo.g.setFont(mfont);
        fm = ginfo.g.getFontMetrics();
        strh = fm.getHeight();
        ginfo.g.setColor(Color.BLACK);
        ginfo.g.drawString(str, boardSize + boardX + 15, (int)(boardY+130+strh));
    }
}
