package hackassembler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class UIController implements Initializable {

    @FXML
    private TextArea assemblyTextArea;
    @FXML
    private TextArea machineTextArea;

    private String OS;
    private int index = 0, memLoc = 16, labelLoc;
    private ArrayList<String> assemblyText;
    private ArrayList<String> machineList = new ArrayList<String>();
    private HashMap<String, Integer> memTable = new HashMap<String, Integer>();
    private HashMap<String, Integer> labelTable = new HashMap<String, Integer>();
    private File file;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        OS = System.getProperty("os.name").toLowerCase();

        assemblyTextArea.setEditable(false);
        machineTextArea.setEditable(false);

    }

    @FXML
    private void assembleNext(ActionEvent event) {
        String line, machineText = "";
        int num, power = 15;

        if (index < assemblyText.size() && !assemblyText.isEmpty()) {

            line = assemblyText.get(index).trim();
            index++;

            char tempc = line.trim().charAt(1);

            if (line.contains("@")) {

                if (Character.isDigit(tempc)) {

                    num = Integer.parseInt(line.replaceAll("[^0-9]", ""));

                    while (power >= 0) {

                        if (num - Math.pow(2, power) < 0) {
                            machineText += "0";
                            power -= 1;
                        } else if (num - Math.pow(2, power) >= 0) {
                            machineText += "1";
                            num -= Math.pow(2, power);
                            power -= 1;
                        }

                    }

                } else if (Character.isLetter(tempc)) {
                    if (line.contains("SP")) {
                        line = line.replace("SP", "0");
                    } else if (line.contains("LCL")) {
                        line = line.replace("LCL", "1");
                    } else if (line.contains("ARG")) {
                        line = line.replace("ARG", "2");
                    } else if (line.contains("THIS")) {
                        line = line.replace("THIS", "3");
                    } else if (line.contains("THAT")) {
                        line = line.replace("THAT", "4");
                    } else if (line.contains("SCREEN")) {
                        line = line.replace("SCREEN", "16384");
                    } else if (line.contains("KBD")) {
                        line = line.replace("KBD", "24576");
                    } else if (line.contains("R") && Character.isDigit(line.charAt(2))) {
                        int registerNum = -1;

                        if (Character.isDigit(line.charAt(2))) {
                            registerNum = Integer.parseInt(line.replaceAll("[^0-9]", ""));
                        }

                        if (registerNum != -1) {
                            line = registerNum + "";
                        }
                    } else {

                        String label = line.substring(1, line.length());

                        if (labelTable.containsKey(label)) {
                            line = line.replace(label, labelTable.get(label) + "");
                        } else if (memTable.containsKey(label)) {
                            line = line.replace(label, memTable.get(label) + "");
                        } else {
                            line = line.replace(label, memLoc + "");
                            memTable.put(label, memLoc);
                            memLoc++;
                        }

                    }

                    num = Integer.parseInt(line.replaceAll("[^0-9]", ""));

                    while (power >= 0) {

                        if (num - Math.pow(2, power) < 0) {
                            machineText += "0";
                            power -= 1;
                        } else if (num - Math.pow(2, power) >= 0) {
                            machineText += "1";
                            num -= Math.pow(2, power);
                            power -= 1;
                        }

                    }
                }

            } else if (!line.contains("@") && !line.contains("(")) {

                machineText += "111";

                if (line.contains("=")) {

                    String before = line.substring(0, line.indexOf('='));
                    String after = line.substring(line.indexOf('=') + 1, line.length());

                    if (after.equals("0")) {
                        machineText += "0101010";
                    } else if (after.contains("!A") || after.contains("!M")) {
                        if (after.contains("M")) {
                            machineText += "1110001";
                        } else {
                            machineText += "0110001";
                        }
                    } else if (after.contains("D-A") || after.contains("D-M")) {
                        if (after.contains("M")) {
                            machineText += "1010011";
                        } else {
                            machineText += "0010011";
                        }
                    } else if (after.contains("A-D") || after.contains("M-D")) {
                        if (after.contains("M")) {
                            machineText += "1000111";
                        } else {
                            machineText += "0000111";
                        }
                    } else if (after.contains("A-1") || after.contains("M-1")) {
                        if (after.contains("M")) {
                            machineText += "1110010";
                        } else {
                            machineText += "0110010";
                        }
                    } else if (after.contains("A+1") || after.contains("M+1")) {
                        if (after.contains("M")) {
                            machineText += "1110111";
                        } else {
                            machineText += "0110111";
                        }
                    } else if (after.contains("D+A") || after.contains("D+M") || after.contains("A+D") || after.contains("M+D")) {
                        if (after.contains("M")) {
                            machineText += "1000010";
                        } else {
                            machineText += "0000010";
                        }
                    } else if (after.contains("D&A") || after.contains("D&M") || after.contains("A&D") || after.contains("M&D")) {
                        if (after.contains("M")) {
                            machineText += "1000000";
                        } else {
                            machineText += "0000000";
                        }
                    } else if (after.contains("D|A") || after.contains("D|M") || after.contains("A|D") || after.contains("M|D")) {
                        if (after.contains("M")) {
                            machineText += "1010101";
                        } else {
                            machineText += "0010101";
                        }
                    } else if (after.contains("-A") || after.contains("-M")) {
                        if (after.contains("M")) {
                            machineText += "1110011";
                        } else {
                            machineText += "0110011";
                        }
                    } else if (after.contains("!D")) {
                        machineText += "0001101";
                    } else if (after.contains("D+1")) {
                        machineText += "0011111";
                    } else if (after.contains("D-1")) {
                        machineText += "0001110";
                    } else if (after.trim().contains("-D")) {
                        machineText += "0001111";
                    } else if (after.contains("-1")) {
                        machineText += "0111010";
                    } else if (after.contains("1")) {
                        machineText += "0111111";
                    } else if (after.trim().contains("D")) {
                        machineText += "0001100";
                    } else if (after.contains("A") || after.contains("M")) {
                        if (after.contains("M")) {
                            machineText += "1110000";
                        } else {
                            machineText += "0110000";
                        }
                    }

                    if (before.contains("AMD") || before.contains("ADM") || before.contains("MAD") || before.contains("MDA") || before.contains("DAM") || before.contains("DMA")) {
                        machineText += "111";
                    } else if (before.contains("MD") || before.contains("DM")) {
                        machineText += "011";
                    } else if (before.contains("AM") || before.contains("MA")) {
                        machineText += "101";
                    } else if (before.contains("AD") || before.contains("DA")) {
                        machineText += "110";
                    } else if (before.contains("A")) {
                        machineText += "100";
                    } else if (before.contains("D")) {
                        machineText += "010";
                    } else if (before.contains("M")) {
                        machineText += "001";
                    }

                } else if (!line.contains("=")) {
                    if (line.charAt(0) == '0') {
                        machineText += "0101010";
                    } else if (line.charAt(0) == 'D') {
                        machineText += "0001100";
                    } else if (line.charAt(0) == 'A') {
                        machineText += "0110000";
                    } else if (line.charAt(0) == 'M') {
                        machineText += "1110000";
                    }
                    machineText += "000";
                }

                if (line.contains(";")) {
                    if (line.contains("JGT")) {
                        machineText += "001";
                    } else if (line.contains("JEG")) {
                        machineText += "010";
                    }  else if (line.contains("JGE")) {
                        machineText += "011";
                    } else if (line.contains("JLT")) {
                        machineText += "100";
                    } else if (line.contains("JNE")) {
                        machineText += "101";
                    } else if (line.contains("JLE")) {
                        machineText += "110";
                    } else if (line.contains("JMP")) {
                        machineText += "111";
                    }
                } else if (!line.contains(";")) {
                    machineText += "000";
                }

            }

            if (!machineText.trim().isEmpty()) {
                machineTextArea.appendText(machineText + "\n");
            }
        }
    }

    @FXML
    private void assembleAll(ActionEvent event) {

        if (assemblyText.isEmpty()) {
            return;
        }

        for (;index < assemblyText.size();) {
            String line, machineText = "";
            int num, power = 15;

            if (index < assemblyText.size() && !assemblyText.isEmpty()) {

                line = assemblyText.get(index).trim();
                index++;

                char tempc = line.trim().charAt(1);

                if (line.contains("@")) {

                    if (Character.isDigit(tempc)) {

                        num = Integer.parseInt(line.replaceAll("[^0-9]", ""));

                        while (power >= 0) {

                            if (num - Math.pow(2, power) < 0) {
                                machineText += "0";
                                power -= 1;
                            } else if (num - Math.pow(2, power) >= 0) {
                                machineText += "1";
                                num -= Math.pow(2, power);
                                power -= 1;
                            }

                        }

                    } else if (Character.isLetter(tempc)) {
                        if (line.contains("SP")) {
                            line = line.replace("SP", "0");
                        } else if (line.contains("LCL")) {
                            line = line.replace("LCL", "1");
                        } else if (line.contains("ARG")) {
                            line = line.replace("ARG", "2");
                        } else if (line.contains("THIS")) {
                            line = line.replace("THIS", "3");
                        } else if (line.contains("THAT")) {
                            line = line.replace("THAT", "4");
                        } else if (line.contains("SCREEN")) {
                            line = line.replace("SCREEN", "16384");
                        } else if (line.contains("KBD")) {
                            line = line.replace("KBD", "24576");
                        } else if (line.contains("R") && Character.isDigit(line.charAt(2))) {
                            int registerNum = -1;

                            if (Character.isDigit(line.charAt(2))) {
                                registerNum = Integer.parseInt(line.replaceAll("[^0-9]", ""));
                            }

                            if (registerNum != -1) {
                                line = registerNum + "";
                            }
                        } else {

                            String label = line.substring(1, line.length());

                            if (labelTable.containsKey(label)) {
                                line = line.replace(label, labelTable.get(label) + "");
                            } else if (memTable.containsKey(label)) {
                                line = line.replace(label, memTable.get(label) + "");
                            } else {
                                line = line.replace(label, memLoc + "");
                                memTable.put(label, memLoc);
                                memLoc++;
                            }

                        }

                        num = Integer.parseInt(line.replaceAll("[^0-9]", ""));

                        while (power >= 0) {

                            if (num - Math.pow(2, power) < 0) {
                                machineText += "0";
                                power -= 1;
                            } else if (num - Math.pow(2, power) >= 0) {
                                machineText += "1";
                                num -= Math.pow(2, power);
                                power -= 1;
                            }

                        }
                    }

                } else if (!line.contains("@") && !line.contains("(")) {

                    machineText += "111";

                    if (line.contains("=")) {

                        String before = line.substring(0, line.indexOf('='));
                        String after = line.substring(line.indexOf('=') + 1, line.length());

                        if (after.equals("0")) {
                            machineText += "0101010";
                        } else if (after.contains("!A") || after.contains("!M")) {
                            if (after.contains("M")) {
                                machineText += "1110001";
                            } else {
                                machineText += "0110001";
                            }
                        } else if (after.contains("D-A") || after.contains("D-M")) {
                            if (after.contains("M")) {
                                machineText += "1010011";
                            } else {
                                machineText += "0010011";
                            }
                        } else if (after.contains("A-D") || after.contains("M-D")) {
                            if (after.contains("M")) {
                                machineText += "1000111";
                            } else {
                                machineText += "0000111";
                            }
                        } else if (after.contains("A-1") || after.contains("M-1")) {
                            if (after.contains("M")) {
                                machineText += "1110010";
                            } else {
                                machineText += "0110010";
                            }
                        } else if (after.contains("A+1") || after.contains("M+1")) {
                            if (after.contains("M")) {
                                machineText += "1110111";
                            } else {
                                machineText += "0110111";
                            }
                        } else if (after.contains("D+A") || after.contains("D+M") || after.contains("A+D") || after.contains("M+D")) {
                            if (after.contains("M")) {
                                machineText += "1000010";
                            } else {
                                machineText += "0000010";
                            }
                        } else if (after.contains("D&A") || after.contains("D&M") || after.contains("A&D") || after.contains("M&D")) {
                            if (after.contains("M")) {
                                machineText += "1000000";
                            } else {
                                machineText += "0000000";
                            }
                        } else if (after.contains("D|A") || after.contains("D|M") || after.contains("A|D") || after.contains("M|D")) {
                            if (after.contains("M")) {
                                machineText += "1010101";
                            } else {
                                machineText += "0010101";
                            }
                        } else if (after.contains("-A") || after.contains("-M")) {
                            if (after.contains("M")) {
                                machineText += "1110011";
                            } else {
                                machineText += "0110011";
                            }
                        } else if (after.contains("!D")) {
                            machineText += "0001101";
                        } else if (after.contains("D+1")) {
                            machineText += "0011111";
                        } else if (after.contains("D-1")) {
                            machineText += "0001110";
                        } else if (after.trim().contains("-D")) {
                            machineText += "0001111";
                        } else if (after.contains("-1")) {
                            machineText += "0111010";
                        } else if (after.contains("1")) {
                            machineText += "0111111";
                        } else if (after.trim().contains("D")) {
                            machineText += "0001100";
                        } else if (after.contains("A") || after.contains("M")) {
                            if (after.contains("M")) {
                                machineText += "1110000";
                            } else {
                                machineText += "0110000";
                            }
                        }

                        if (before.contains("AMD") || before.contains("ADM") || before.contains("MAD") || before.contains("MDA") || before.contains("DAM") || before.contains("DMA")) {
                            machineText += "111";
                        } else if (before.contains("MD") || before.contains("DM")) {
                            machineText += "011";
                        } else if (before.contains("AM") || before.contains("MA")) {
                            machineText += "101";
                        } else if (before.contains("AD") || before.contains("DA")) {
                            machineText += "110";
                        } else if (before.contains("A")) {
                            machineText += "100";
                        } else if (before.contains("D")) {
                            machineText += "010";
                        } else if (before.contains("M")) {
                            machineText += "001";
                        }

                    } else if (!line.contains("=")) {
                        if (line.charAt(0) == '0') {
                            machineText += "0101010";
                        } else if (line.charAt(0) == 'D') {
                            machineText += "0001100";
                        } else if (line.charAt(0) == 'A') {
                            machineText += "0110000";
                        } else if (line.charAt(0) == 'M') {
                            machineText += "1110000";
                        }
                        machineText += "000";
                    }

                    if (line.contains(";")) {
                        if (line.contains("JGT")) {
                            machineText += "001";
                        } else if (line.contains("JEG")) {
                            machineText += "010";
                        }  else if (line.contains("JGE")) {
                            machineText += "011";
                        } else if (line.contains("JLT")) {
                            machineText += "100";
                        } else if (line.contains("JNE")) {
                            machineText += "101";
                        } else if (line.contains("JLE")) {
                            machineText += "110";
                        } else if (line.contains("JMP")) {
                            machineText += "111";
                        }
                    } else if (!line.contains(";")) {
                        machineText += "000";
                    }

                }

                if (!machineText.trim().isEmpty()) {
                    machineList.add(machineText);
                }
            }
        }

        String newFile = file.getPath().substring(0, file.getPath().indexOf('.')) + ".tp";

        machineTextArea.clear();

        try {
            Files.write(Paths.get(newFile), machineList, Charset.defaultCharset());
            machineTextArea.appendText(new String(Files.readAllBytes(Paths.get(newFile))));

            File tFile = new File(newFile);

            if (tFile.exists()) {
                tFile.delete();
            }

        } catch (IOException ex) { ex.printStackTrace(); }


    }

    @FXML
    private void loadFile(ActionEvent event) {

        labelTable.clear();
        memTable.clear();
        memLoc = 16;

        FileChooser fc = new FileChooser();
        fc.setTitle("Load Assembly File");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("ASSEMBLY FILE", "*.asm"));

        if (OS.equals("mac os x")) {
            fc.setInitialDirectory(new File("/Users/nicholas/OneDrive/Documents/nand2tetris/projects/06"));
        } else {
            fc.setInitialDirectory(new File("C:\\Users\\nzoel\\OneDrive\\Documents\\nand2tetris\\projects\\06"));
        }

        file = fc.showOpenDialog(new Stage());

        if (file == null) {
            return;
        }

        assemblyTextArea.clear();
        machineTextArea.clear();

        try {

            assemblyTextArea.appendText(new String(Files.readAllBytes(Paths.get(file.getPath()))));

            /*FileReader fr = new FileReader(file.getPath());

            BufferedReader br = new BufferedReader(fr);

            String line = null;

            while ((line = br.readLine()) != null) {
                assemblyTextArea.appendText(line + "\n");
            }

            br.close();*/

        } catch (IOException ex) { ex.printStackTrace(); }

        assemblyText = new ArrayList(Arrays.asList(assemblyTextArea.getText().split("\n")));
        machineList.clear();

        for (int i = 0; i < assemblyText.size(); i++) {
            String temp = assemblyText.get(i).trim();
            assemblyText.set(i, temp);
        }

        for (int i = 0; i < assemblyText.size();) {
            String temp = assemblyText.get(i);
            if (temp.contains("//")) {
                temp = temp.replace(temp.substring(temp.indexOf("/"), temp.length()), "");
                assemblyText.set(i, temp.trim());
            }
            if (temp.trim().isEmpty()) {
                assemblyText.remove(temp);
            } else {
                i++;
            }
        }

        findLabels();

        index = 0;

    }

    @FXML
    private void saveFile(ActionEvent event) {
        String name = file.getPath().substring(0, file.getPath().indexOf('.')) + ".hack";

        try {

            String[] line = machineTextArea.getText().split("\n");
            PrintWriter pw = new PrintWriter(name);

            for (String s : line) {
                pw.println(s);
            }

            pw.close();

        } catch (FileNotFoundException ex) { ex.printStackTrace(); }


    }

    public void findLabels() {

        String label;

        for (int i = 0; i < assemblyText.size();) {
            if (assemblyText.get(i).contains("(")) {
                label = assemblyText.get(i).replace("(", "");
                label = label.replace(")", "");
                labelLoc = assemblyText.indexOf("(" + label + ")");
                labelTable.put(label, labelLoc);
                assemblyText.remove(labelLoc);
            } else {
                i++;
            }
        }
    }

}
