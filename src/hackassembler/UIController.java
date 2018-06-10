package hackassembler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class UIController implements Initializable {

    @FXML
    private TextArea assemblyTextArea;
    @FXML
    private TextArea machineTextArea;

    private Parse parser;
    private ArrayList<String> translation;
    private int index = 0;
    private File file;
    private String path;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        assemblyTextArea.setEditable(false);
        machineTextArea.setEditable(false);

    }

    @FXML
    private void assembleNext(ActionEvent event) {

        if (index < translation.size()) {

            machineTextArea.appendText(translation.get(index) + "\n");

            index++;

        }

    }

    @FXML
    private void assembleAll(ActionEvent event) throws IOException{

        String tempFile = path.substring(0, path.indexOf('.')) + ".tp";

        PrintWriter pw = new PrintWriter(tempFile);

        for (String s : translation) {
            pw.println(s);
        }

        pw.close();

        machineTextArea.appendText(new String(Files.readAllBytes(Paths.get(tempFile))));

        file = new File(tempFile);

        file.delete();

        index = translation.size();

    }

    @FXML
    private void loadFile(ActionEvent event) throws IOException {
        assemblyTextArea.clear();
        machineTextArea.clear();
        FileChooser fc = new FileChooser();
        file = fc.showOpenDialog(new Stage());
        path = file.getPath();
        assemblyTextArea.appendText(new String(Files.readAllBytes(Paths.get(file.getPath()))));
        parser = new Parse(new ArrayList(Arrays.asList(assemblyTextArea.getText().split("\n"))));
        translation = Lex.translate(parser.parseFile());
    }

    @FXML
    private void saveFile(ActionEvent event) {

        path = path.substring(0, path.indexOf('.')) + ".hack";

        try {

            String[] line = machineTextArea.getText().split("\n");
            PrintWriter pw = new PrintWriter(path);

            for (String s : line) {
                pw.println(s);
            }

            pw.close();

        } catch (FileNotFoundException ex) { ex.printStackTrace(); }

    }

}
