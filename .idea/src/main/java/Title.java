import org.nd4j.common.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
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

        ginfo.g.drawImage(this.imgTitle, (int)(ginfo.windowWidth-this.imgTitle.getTileWidth())/2, 150, null);
        ginfo.g.drawImage(this.imgIcon, (int)(ginfo.windowWidth-this.imgIcon.getTileWidth())/2, 280, null);

        if(ginfo.clickX != -100 && ginfo.clickY != -100){
            ginfo.clickX = -100;
            ginfo.clickY = -100;
            this.gameInfo.goSelectMode();
        }

    }

    @Override
    public void loadMedia() throws IOException {
        this.imgTitle = ImageIO.read(new File(new ClassPathResource("img\\title.png").getFile().getPath()));
        this.imgIcon = ImageIO.read(new File(new ClassPathResource("img\\icon2.png").getFile().getPath()));
    }


}
