import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoadJson {
    JSONObject config;
    int BGM, SE;
    String filePath = "config.json";
    LoadJson() {
        try {
            Object ob = new JSONParser().parse(new FileReader(this.filePath));
            this.config = (JSONObject) ob;
            String temp = this.config.get("BGM").toString();
            this.BGM = Integer.parseInt(temp);
            temp = this.config.get("SE").toString();
            this.SE =  Integer.parseInt(temp);
            System.out.println("BGM : " + this.BGM);
            System.out.println("SE : " + this.SE);
        } catch (IOException | ParseException e) {
            try {
                new File(filePath).createNewFile();
                writeJson(5,5);
                this.BGM = 5;
                this.SE = 5;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            //e.printStackTrace();
        }
    }

    void writeJson(float BGM, float SE){
        JSONObject obj = new JSONObject();
        obj.put("BGM", (int)BGM);
        obj.put("SE", (int)SE);
        System.out.println(new File(this.filePath).getAbsolutePath());
        try (FileWriter file = new FileWriter(this.filePath)) {
            file.write(obj.toJSONString());
            file.flush();
            System.out.println("書き込み完了.\n BGM : " + BGM + "\n SE : " + SE);

        } catch (IOException e) {
            System.out.println(e);//.printStackTrace()
        }
    }
}
