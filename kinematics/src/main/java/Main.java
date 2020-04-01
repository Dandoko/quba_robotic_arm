import java.io.File;


public class Main {

    private static final String PATH = "src/main/moves/";
    private static final String FILE = "move";

    private static final int REMOVE_VAL = -1;
    private static final int CROWN_VAL = -2;

    private static final int WAIT = 500; // Milliseconds

    public static void main(String[] args) throws InterruptedException {

        Input input = new Input();
        Kinematics kinematics = new Kinematics();
        Serial serial = new Serial();

        int counter = 0;
        while (true) {
            File file = new File(PATH + FILE + counter + ".json");
            if (file.exists()) {
                System.out.println("Move: " + (counter + 1));

                int[][] move = input.readJSON(file);

                int timer = 0;
                if (move[1][1] == REMOVE_VAL) {
                    timer += 5 * WAIT;
                } else if (move[1][1] == CROWN_VAL) {
                    timer += 10 * WAIT;
                } else {
                    timer += Math.round(Math.sqrt(Math.pow(move[0][0] - move[1][0], 2) + Math.pow(move[0][1] - move[1][1], 2)) * 1.75 * WAIT);
                }

                String[] commands = kinematics.motorCommands(move);

                for (String command : commands) {
                    System.out.print(command);
                    serial.write(command);
                    Thread.sleep(2 * WAIT);
                }

                Thread.sleep(timer / 2);

                counter++;
            }

            Thread.sleep(1000);
        }

    }

}
