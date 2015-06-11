package reteSensori;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.Objects;

/**
 * Created by Alessandra on 16/05/15.
 */

public class MainRete {

    public static String tipoSink;
    public static String frequenceSink;
    public static String batteryTemp, batteryLight, batteryPIR1, batteryPIR2;

    public static void main(String[] args) throws Exception {
        tipoSink = args[0];
        frequenceSink = args[1];
        batteryTemp = args[2];
        batteryLight = args[3];
        batteryPIR1 = args[4];
        batteryPIR2 = args[5];

        //lanciare classi per i nodi > passo: tipo, batteria, boolSink, freq

        File temp = new File("/Users/Alessandra/Documents/workspace/ProgettoSensori/logs/temp.txt");
        File lum = new File("/Users/Alessandra/Documents/workspace/ProgettoSensori/logs/lum.txt");
        File pir1 = new File("/Users/Alessandra/Documents/workspace/ProgettoSensori/logs/pir1.txt");
        File pir2 = new File("/Users/Alessandra/Documents/workspace/ProgettoSensori/logs/pir2.txt");

        String pathGson = "/Users/Alessandra/Documents/workspace/librerie/gson-2.3.1.jar";

        String[] tipi = {"temperatura", "luminosita", "PIR1", "PIR2"};

        for (String t : tipi) {
            String line;
            List<String> command = new ArrayList<>();
            command.add("java");
            command.add("-cp");
            command.add(pathGson + ":" + "/Users/Alessandra/Documents/workspace/ProgettoSensori/target/classes/");
            command.add("reteSensori.MainNodo");
            command.add(t);
            switch (t) {
                case "temperatura":
                    command.add(batteryTemp);
                    break;
                case "luminosita":
                    command.add(batteryLight);
                    break;
                case "PIR1":
                    command.add(batteryPIR1);
                    break;
                case "PIR2":
                    command.add(batteryPIR2);
                    break;
                default:
                    System.out.println("Tipo non specificato.");
            }
            if (Objects.equals(tipoSink, t)) {
                command.add("true");
            } else {
                command.add("false");
            }
            command.add(frequenceSink);
            ProcessBuilder builder = new ProcessBuilder(command);

            switch (t) {
                case "temperatura":
                    builder
                            .redirectOutput(temp)
                            .redirectError(temp)
                            .redirectErrorStream(true);
                    break;
                case "luminosita":
                    builder.redirectOutput(lum)
                            .redirectError(lum)
                            .redirectErrorStream(true);

                    break;
                case "PIR1":
                    builder.redirectOutput(pir1)
                            .redirectError(pir1)
                            .redirectErrorStream(true);
                    break;
                case "PIR2":
                    builder.redirectOutput(pir2)
                            .redirectError(pir2)
                            .redirectErrorStream(true);
                    break;
                default:
                    System.out.println("Tipo non specificato.");
            }

            Process process = builder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                System.out.println(line);

            }

        }
    }
}
