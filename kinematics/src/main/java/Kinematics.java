import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import java.util.Arrays;

class Kinematics {

    private final double STEP_SIZE = 1.8; // Degrees

    private final double VER_LEN = 140.7; // Millimeters
    private final double INIT_VER = 90.0; // Degrees
    private final double VER_FACTOR = -2.0 / 5.0;

    private final double HOR_LEN = 147.0; // Millimeters
    private final double INIT_HOR = 0.0; // Degrees
    private final double HOR_FACTOR = 2.0 / 5.0;

    private final double CLAW_X = 43.0; // Millimeters
    private final double CLAW_Y = -63.0; // Millimeters

    private final double BASE_FACTOR = -2.0 / 11.0;

    private final double GRID_UNIT = 18.0; // Millimeters

    private final double MIN_X = 74.5; // Millimeters
    private final double MIN_Y = 80.5; // Millimeters

    private final double GRAB_HEIGHT = 15.0; // Millimeters
    private final double MOVE_HEIGHT = 45.0; // Millimeters

    private final int REMOVE_VAL = -1;
    private final int CROWN_VAL = -2;

    private final String ORIGIN_COMMAND = "0000000000000\n";

    String[] motorCommands(int[][] move) {
        double[][] coords = endpoints(move);
        boolean crown = coords[1][1] == CROWN_VAL;
        String[] commands;

        if (crown) {
            List<String> all = new ArrayList<String>(14);

            coords[1][0] = REMOVE_VAL;

            Collections.addAll(all, generateActions(coords));

            coords = endpoints(move);
            coords[1] = coords[0];
            coords[0][0] = CROWN_VAL;

            Collections.addAll(all, generateActions(coords));

            commands = all.toArray(new String[14]);
        } else {
            commands = generateActions(coords);
        }

        return commands;
    }

    private String[] generateActions(double[][] coords) {
        String[] commands = new String[7];

        // Move to transit safe piece pickup
        coords[0][1] = MOVE_HEIGHT - CLAW_Y - MIN_Y;
        commands[0] = posToString(solveXYZ(coords[0])) + "0\n";

        // Reach to grab game piece
        coords[0][1] = GRAB_HEIGHT - CLAW_Y - MIN_Y;
        commands[1] = posToString(solveXYZ(coords[0])) + "1\n";

        // Return to safe transit position
        coords[0][1] = MOVE_HEIGHT - CLAW_Y - MIN_Y;
        commands[2] = posToString(solveXYZ(coords[0])) + "0\n";

        // Proceed to destination above movement height
        coords[1][1] = MOVE_HEIGHT - CLAW_Y - MIN_Y;
        commands[3] = posToString(solveXYZ(coords[1])) + "0\n";

        // Deposit piece at goal
        coords[1][1] = GRAB_HEIGHT - CLAW_Y - MIN_Y;
        commands[4] = posToString(solveXYZ(coords[1])) + "2\n";

        // Return to safe transit position
        coords[1][1] = MOVE_HEIGHT - CLAW_Y - MIN_Y;
        commands[5] = posToString(solveXYZ(coords[1])) + "0\n";

        // Return to origin/initial position
        commands[6] = ORIGIN_COMMAND;

        return commands;
    }

    private String posToString(int[] positions) {
        StringBuilder output = new StringBuilder();
        for (int position : positions) {
            output.append(String.format("%04d", position));
        }
        return output.toString();
    }

    private double[][] endpoints(int[][] move) {
        double[][] coords = new double[2][3];

        coords[0][0] = (8 - move[0][1]) * GRID_UNIT - 0.5 * GRID_UNIT + MIN_X;
        coords[0][1] = 0;
        coords[0][2] = (move[0][0] - 3) * GRID_UNIT - 0.5 * GRID_UNIT;

        if (move[1][1] == REMOVE_VAL) {
            coords[1][0] = REMOVE_VAL;
            coords[1][1] = REMOVE_VAL;
            coords[1][2] = REMOVE_VAL;
        } else if (move[1][1] == CROWN_VAL) {
            coords[1][0] = CROWN_VAL;
            coords[1][1] = CROWN_VAL;
            coords[1][2] = CROWN_VAL;
        } else {
            coords[1][0] = (8 - move[1][1]) * GRID_UNIT - 0.5 * GRID_UNIT + MIN_X;
            coords[1][1] = 0;
            coords[1][2] = (move[0][0] - 3) * GRID_UNIT - 0.5 * GRID_UNIT;
        }

//        System.out.println(Arrays.deepToString(coords));

        return coords;
    }

    private int[] solveXY(double[] coords) {
        double dist = Math.sqrt(Math.pow(coords[0], 2) + Math.pow(coords[1], 2));

        double d1 = Math.atan2(coords[1], coords[0]); // Axis to distance ray (Radians)
//        System.out.println(Math.toDegrees(d1));
        double d2 = cosineLaw(dist, VER_LEN, HOR_LEN); // Distance ray to vertical arm (Radians)
//        System.out.println(Math.toDegrees(d2));

        double b2 = cosineLaw(VER_LEN, HOR_LEN, dist); // Angle between vertical arm and horizontal arm (Radians)
//        System.out.println(Math.toDegrees(b2));

        int[] positions = new int[2];
        // Vertical arm motor position from initial state (Degrees)
        positions[0] = (int) Math.round((INIT_VER - Math.toDegrees(d1 + d2)) / VER_FACTOR / STEP_SIZE);
//        System.out.println(Math.toDegrees(d1 + d2));
        // Horizontal arm motor position from initial state (Degrees)
        positions[1] = (int) Math.round((INIT_HOR + Math.toDegrees(b2 + d1 + d2) - 180) / HOR_FACTOR / STEP_SIZE);
//        System.out.println(Math.toDegrees(b2 + d1 + d2) - 180);

        return positions;
    }

    private int[] solveXYZ(double[] coords) {
        if (coords[0] == REMOVE_VAL) {
            coords[0] = 8 * GRID_UNIT + MIN_X;
            coords[2] = 6 * GRID_UNIT;
        } else if (coords[0] == CROWN_VAL) {
            coords[0] = 3.5 * GRID_UNIT + MIN_X;
            coords[2] = -4.5 * GRID_UNIT;
        }

        double actual = coords[0];
        coords[0] = Math.sqrt(Math.pow(coords[0], 2) + Math.pow(coords[2], 2));

        int[] xy = solveXY(coords);
        return new int[]{xy[0], xy[1], (int) Math.round(Math.toDegrees(Math.atan2(coords[2], actual)) / BASE_FACTOR / STEP_SIZE)};
    }

    private double cosineLaw(double a, double b, double c) {
        return Math.acos((Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2 * a * b));
    }

}
