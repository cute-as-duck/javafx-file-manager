package fx.filemanager;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Controller{

    @FXML
    VBox leftPanel, rightPanel;

    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void copyBtnAction(ActionEvent actionEvent) {
        PanelController leftPC = (PanelController) leftPanel.getProperties().get("ctrl");
        PanelController rightPC = (PanelController) rightPanel.getProperties().get("ctrl");

        if(leftPC.getSelectedFilename() == null && rightPC.getSelectedFilename() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please choose a file", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        PanelController srcPC = null, dstPC = null;
        if(leftPC.getSelectedFilename() != null) {
            srcPC = leftPC;
            dstPC = rightPC;
        }
        if(rightPC.getSelectedFilename() != null) {
            srcPC = rightPC;
            dstPC = leftPC;
        }

        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFilename());
        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName().toString());

        try {
            Files.copy(srcPath, dstPath);
            dstPC.updateList(Paths.get(dstPC.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to copy file", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void moveBtnAction(ActionEvent actionEvent) {
        PanelController leftPC = (PanelController) leftPanel.getProperties().get("ctrl");
        PanelController rightPC = (PanelController) rightPanel.getProperties().get("ctrl");

        if(leftPC.getSelectedFilename() == null && rightPC.getSelectedFilename() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please choose a file", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        PanelController srcPC = null, dstPC = null;
        if(leftPC.getSelectedFilename() != null) {
            srcPC = leftPC;
            dstPC = rightPC;
        }
        if(rightPC.getSelectedFilename() != null) {
            srcPC = rightPC;
            dstPC = leftPC;
        }

        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFilename());
        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName().toString());

        try {
            Files.move(srcPath, dstPath);
            srcPC.updateList(Paths.get(srcPC.getCurrentPath()));
            dstPC.updateList(Paths.get(dstPC.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to move file", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void deleteBtnAction(ActionEvent actionEvent) {
        PanelController leftPC = (PanelController) leftPanel.getProperties().get("ctrl");
        PanelController rightPC = (PanelController) rightPanel.getProperties().get("ctrl");

        if(leftPC.getSelectedFilename() == null && rightPC.getSelectedFilename() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please choose a file", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        PanelController selectedPC = null;

        if(leftPC.getSelectedFilename() != null) {
            selectedPC = leftPC;
        }
        if(rightPC.getSelectedFilename() != null) {
            selectedPC = rightPC;
        }

        Path pathToDelete = Paths.get(selectedPC.getCurrentPath(), selectedPC.getSelectedFilename());

        if (Files.isDirectory(pathToDelete)) {
            deleteDirectory(pathToDelete);
            leftPC.updateList(Paths.get(leftPC.getCurrentPath()));
            rightPC.updateList(Paths.get(rightPC.getCurrentPath()));
        } else {
            try {
                Files.delete(pathToDelete);
                selectedPC.updateList(Paths.get(selectedPC.getCurrentPath()));
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to delete file", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    private static void deleteDirectory(Path path) {
        try {
            List<Path> pathes = Files.list(path).collect(Collectors.toList());
            for (Path p : pathes) {
                if (!Files.isDirectory(p) || Files.list(p).toList().isEmpty()) {
                    Files.delete(p);
                } else {
                    deleteDirectory(p);
                }
            }
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}