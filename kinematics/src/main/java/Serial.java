import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;


class Serial {
    private static final int TIME_OUT = 3000;
    private static final int DATA_RATE = 9600;
    private SerialPort serialPort;
    private OutputStream out;

    Serial() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            if (currPortId.getName().equals("COM3")) {
                portId = currPortId;
                break;
            }
        }
        if (portId == null) {
            System.err.println("Could not find port.");
            System.exit(1);
        }

        try {
            serialPort = (SerialPort) portId.open("Kinematics",
                    TIME_OUT);

            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        assert serialPort != null;

        try {
            out = serialPort.getOutputStream();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    void write(String message) {
        try {
            out.write(message.getBytes());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /*
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }
    */
}