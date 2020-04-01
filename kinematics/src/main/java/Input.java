import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;


public class Input {
    private final String INITIAL_KEY = "curPos";
    private final String FINAL_KEY = "goalPos";

    private final String REMOVE_STR = "*remove";
    private final String CROWN_STR = "*crown";

    private final int REMOVE_VAL = -1;
    private final int CROWN_VAL = -2;

    public int[][] readJSON(File file) {
        int[][] move = new int[2][2];

        try {
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(file);
            String tmp;

            JSONObject posObj = (JSONObject) jsonParser.parse(reader);

            tmp = (String) posObj.get(INITIAL_KEY);
            move[0][0] = Integer.parseInt(tmp.substring(0, 1));
            move[0][1] = Integer.parseInt(tmp.substring(1, 2));

            tmp = (String) posObj.get(FINAL_KEY);
            if (tmp.startsWith("*")) {
                if (tmp.equals(REMOVE_STR)) {
                    move[1][0] = REMOVE_VAL;
                    move[1][1] = REMOVE_VAL;
                } else if (tmp.equals(CROWN_STR)) {
                    move[1][0] = CROWN_VAL;
                    move[1][1] = CROWN_VAL;
                }
            } else {
                move[1][0] = Integer.parseInt(tmp.substring(0, 1));
                move[1][1] = Integer.parseInt(tmp.substring(1, 2));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return move;
    }
}
