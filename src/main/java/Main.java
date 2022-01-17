import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {
        Main game = new Main();
        game.start();
    }

    JFrame mainWindow;
    BufferStrategy strategy;
    GameDisplay display = new GameDisplay();
    mouseData cursor = new mouseData();
    GraphicsInfo ginfo = new GraphicsInfo();

    //インスタンス
    Main(){
        this.mainWindow = new JFrame("JavaOthello");
        this.mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainWindow.setSize(800,650);
        this.mainWindow.setLocationRelativeTo(null);
        this.mainWindow.setVisible(true);
        this.mainWindow.setResizable(false);
        this.mainWindow.addMouseListener(cursor);
        this.mainWindow.addMouseMotionListener(cursor);

        //バッファストラテジー
        this.mainWindow.setIgnoreRepaint(true);
        this.mainWindow.createBufferStrategy(2);
        this.strategy = this.mainWindow.getBufferStrategy();

        try {
            this.display.loadMedia();
        }catch (IOException e){
            JOptionPane.showMessageDialog(this.mainWindow,"タイトル画像読み込みエラー");
        }
    }

    void start(){
        Timer t = new Timer(true);
        t.schedule(new RenderTask(),0,16);
    }

    class RenderTask extends TimerTask {

        @Override
        public void run() {
            Main.this.render();
        }
    }

    void render(){
        Graphics2D g = (Graphics2D) this.strategy.getDrawGraphics();
        g.setBackground(new Color(0,128,0));
        g.clearRect(0,0,this.mainWindow.getWidth(), this.mainWindow.getHeight());
        ginfo.g = g;
        ginfo.windowWidth = this.mainWindow.getWidth();
        ginfo.windowHeight = this.mainWindow.getHeight();
        this.display.getCurrentDisplay().show(ginfo);
        g.dispose();
        this.strategy.show();
    }

    class mouseData extends JComponent implements MouseListener, MouseMotionListener {
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            ginfo.clickX = e.getX();
            ginfo.clickY = e.getY();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            ginfo.cursorX = e.getX();
            ginfo.cursorY = e.getY();
        }
    }
}
