
import java.lang.reflect.Array;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        UIManager uiManager = new UIManager();
        BorderPane root = uiManager.createUI();

        Scene scene = new Scene(root, 1000, 400);
        primaryStage.setTitle("Fifa League Table");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        /*int season = FileOperations.get_season_count();
        int[][] data_arr = new int[FileOperations.get_player_count()][9];
        if (season == 0) {
            FileOperations.create_new_season();
            ArrayOperations.initialize(data_arr);
        } else {
            FileOperations.fill_the_array(season, data_arr);
        }*/

        launch(args);
    }
}

class UIManager {

    public BorderPane createUI() {
        TableView<String[]> table = createTable();
        HBox rightPane = createRightPane();
        Button menuButton = createMenuButton();

        SplitPane splitPane = new SplitPane(table, rightPane);
        splitPane.setDividerPositions(0.7);

        HBox topBar = new HBox(10, menuButton);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(splitPane);
        applyStyles(root, table, rightPane, menuButton);

        return root;
    }

    private TableView<String[]> createTable() {
        TableView<String[]> table = new TableView<>();
        String[] columnNames = {"Oyuncu", "OM", "G", "B", "M", "P", "AG", "YG", "A"};

        for (int i = 0; i < columnNames.length; i++) {
            TableColumn<String[], String> column = new TableColumn<>(columnNames[i]);
            final int colIndex = i;
            column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[colIndex]));
            table.getColumns().add(column);
        }

        ObservableList<String[]> data = FXCollections.observableArrayList(
                new String[]{"Ahmet", "0", "0", "0", "0", "0", "0", "0", "0"},
                new String[]{"Kaan", "0", "0", "0", "0", "0", "0", "0", "0"},
                new String[]{"Mehmet", "0", "0", "0", "0", "0", "0", "0", "0"},
                new String[]{"Mustafa", "0", "0", "0", "0", "0", "0", "0", "0"},
                new String[]{"Yahya", "0", "0", "0", "0", "0", "0", "0", "0"}
        );
        table.setItems(data);

        return table;
    }

    private HBox createRightPane() {
        HBox rightPane = new HBox(10);
        rightPane.getChildren().add(new Label("Mustafa"));

        TextField tf1 = new TextField();
        tf1.setMaxWidth(25);
        tf1.setPrefHeight(20); // Yüksekliği küçült
        tf1.setStyle("-fx-font-size: 16px; -fx-padding: 2px;"); // Padding ve font ayarıyla daha küçük görünmesini sağla
        rightPane.getChildren().add(tf1);

        rightPane.getChildren().add(new Label("-"));

        TextField tf2 = new TextField();
        tf2.setMaxWidth(25);
        tf2.setPrefHeight(20);
        tf2.setStyle("-fx-font-size: 16px; -fx-padding: 2px;");
        rightPane.getChildren().add(tf2);

        rightPane.getChildren().add(new Label("Ahmet"));

        return rightPane;
    }

    private Button createMenuButton() {
        Button menuButton = new Button("Geçmiş Sezonlar");

        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Sezon 1");
        MenuItem item2 = new MenuItem("Sezon 2");
        MenuItem item3 = new MenuItem("Sezon 3");
        MenuItem item4 = new MenuItem("YENİ SEZON +");
        contextMenu.getItems().addAll(item1, item2, item3, item4);

        menuButton.setOnAction(e -> contextMenu.show(menuButton,
                menuButton.localToScreen(0, menuButton.getHeight()).getX(),
                menuButton.localToScreen(0, menuButton.getHeight()).getY()));

        return menuButton;
    }

    private void applyStyles(BorderPane root, TableView<String[]> table, HBox rightPane, Button menuButton) {
        String css = """
            -fx-background-color:rgb(233, 233, 233);
            -fx-control-inner-background:rgb(233, 233, 233);
            -fx-accent: #3D8D7A;
            -fx-focus-color: #B3D8A8;
            -fx-text-fill: #000000;
            -fx-font-size: 18px;
            -fx-font-family: 'Arial';
        """;

        root.setStyle(css);
        table.setStyle("-fx-background-color: #A3D1C6;");
        rightPane.setStyle("-fx-background-color: #A3D1C6;");
        menuButton.setStyle("-fx-background-color: #3D8D7A; -fx-text-fill:rgb(233, 233, 233);");
    }
}
