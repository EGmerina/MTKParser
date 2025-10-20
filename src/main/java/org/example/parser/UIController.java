package org.example.parser;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static javafx.scene.paint.Color.RED;


public class UIController {

    @FXML
    private TextArea contentTextArea, listTextArea;

    @FXML
    private Slider fontSizeSlider;

    @FXML
    private Parent root;

    @FXML
    private MenuItem close, save, clear, open;

    @FXML
    private MenuItem language, grammar, usage, Aprogram, report;

    @FXML
    private Label percent;

    @FXML
    private TextFlow textFlow;

    @FXML
    private Button bsave, bopen, bclear, binfo, babout, run;


    @FXML
    public void initialize() {
        setupLineNumbering();
        setupMenuBar();
        setupButtons();
        setupSlider();
    }

    private void setupButtons() {
        FontIcon save = new FontIcon(Feather.SAVE);
        FontIcon open = new FontIcon(Feather.FOLDER);
        FontIcon clear = new FontIcon(Feather.FILE);
        FontIcon info = new FontIcon(Feather.INFO);
        FontIcon about = new FontIcon(Feather.HELP_CIRCLE);
        FontIcon brun = new FontIcon(Feather.PLAY);

        bsave.setGraphic(save);
        bopen.setGraphic(open);
        binfo.setGraphic(info);
        babout.setGraphic(about);
        bclear.setGraphic(clear);
        run.setGraphic(brun);

        bclear.setOnAction(event -> {
            contentTextArea.clear();
            listTextArea.clear();
            textFlow.getChildren().clear();
        });
        bopen.setOnAction(event -> {
            openFile(event);
        });
        bsave.setOnAction(event -> {
            saveFile(event);
        });
        binfo.setOnAction(event -> {
            openDescriptionFile("info.txt");
        });
        babout.setOnAction(event -> {
            openDescriptionFile("usage.txt");
        });

    }

    @FXML
    public void clickRunButton() {
        String string = contentTextArea.getText();
        Parser parser = new Parser(string);
        String result = parser.parse();
        System.out.println(result);
        Text text = new Text(result);
        text.setFill(RED);
        textFlow.getChildren().add(text);
    }

    private void setupSlider() {
        fontSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int fontSize = newVal.intValue();
            root.setStyle("-fx-font-size: " + fontSize + "px;");
            contentTextArea.setStyle("-fx-font-size: " + fontSize + "px;");
            listTextArea.setStyle("-fx-font-size: " + fontSize + "px;");
            double avg = (fontSizeSlider.getMin() + fontSizeSlider.getMax()) / 2;
            int per = (int) (fontSize / avg * 100);
            percent.setText(Integer.toString(per) + " %");
        });

    }


    private void setupMenuBar() {
        close.setOnAction(event -> {
            Platform.exit();
        });
        clear.setOnAction(event -> {
            contentTextArea.clear();
            listTextArea.clear();
            textFlow.getChildren().clear();
        });
        open.setOnAction(event -> {
            openFile(event);
        });
        save.setOnAction(event -> {
            saveFile(event);
        });
        language.setOnAction(event -> {
            openDescriptionFile("language.txt");
        });
        grammar.setOnAction(event -> {
            openDescriptionFile("grammar.txt");
        });
        Aprogram.setOnAction(event -> {
            openDescriptionFile("info.txt");
        });
        usage.setOnAction(event -> {
            openDescriptionFile("usage.txt");
        });
        report.setOnAction(event -> {
            openReport();
        });
    }

    private void openReport() {
        File wordFile = new File("src/main/resources/org/example/parser/отчет_Гмерина.docx");

        if (!wordFile.exists()) {
            showFileNotFoundAlert(wordFile.getAbsolutePath());
            return;
        }

        Thread openThread = new Thread(() -> {
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(wordFile);
            } catch (IOException e) {
                Platform.runLater(() -> showErrorAlert("error when open file", e.getMessage()));
            }
        });

        openThread.setDaemon(true);
        openThread.start();
    }

    private void showFileNotFoundAlert(String filePath) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("file not found");
        alert.setHeaderText("cant find the file");
        alert.setContentText("path: " + filePath);
        alert.showAndWait();
    }

    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void openDescriptionFile(String file) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/parser/description-view.fxml"));
            Parent root = loader.load();
            DescriptionController controller = loader.getController();
            controller.loadTextFromFile(file);
            Stage stage = new Stage();
            stage.setTitle(file);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("save file");
        fileChooser.setInitialFileName("file.txt");
        Window window = contentTextArea.getScene().getWindow();
        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            try {
                Files.writeString(file.toPath(), contentTextArea.getText());
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Saving error");
                alert.setHeaderText("can't save the file");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open txt file");

        Window window = contentTextArea.getScene().getWindow();
        java.io.File selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile != null) {
            String content = null;
            try {
                content = Files.readString(Paths.get(selectedFile.toURI()));
                contentTextArea.setText(content);
            } catch (IOException e) {
                e.printStackTrace();
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("can't open file");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }


    private void setupLineNumbering() {
        contentTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            updateLineNumbers(newValue);
        });

        syncScrollBars();
    }

    private void updateLineNumbers(String text) {

        int lineCount = 1;
        if (!text.isEmpty()) {
            int newLineCount = 0;
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == '\n') {
                    newLineCount++;
                }
            }
            lineCount = newLineCount + 1;

        }
        String list = new String();
        for (int i = 1; i <= lineCount; i++) {
            list += (i + System.lineSeparator());
        }
        listTextArea.setText(list);
        // syncSelection();
    }

    private void syncScrollBars() {
        ScrollBar textAreaScrollBar = getVerticalScrollBar(contentTextArea);
        ScrollBar listScrollBar = getVerticalScrollBar(listTextArea);

        if (textAreaScrollBar != null && listScrollBar != null) {
            textAreaScrollBar.valueProperty().addListener((obs, oldVal, newVal) -> {
                listScrollBar.setValue(newVal.doubleValue());
            });
        } else {
            Platform.runLater(() -> syncScrollBars());
        }
    }


    private ScrollBar getVerticalScrollBar(Control control) {
        for (Node node : control.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar && ((ScrollBar) node).getOrientation() == Orientation.VERTICAL) {
                return (ScrollBar) node;
            }
        }
        return null;
    }

}
