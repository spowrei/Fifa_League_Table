
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.util.List;
import java.util.ArrayList;

public class Main extends Application {

    private static int[][] data_arr;
    private static int season;
    private static int player_count;

    @Override
    public void start(Stage primaryStage) {
        UIManager uiManager = new UIManager(data_arr, primaryStage);
        BorderPane root = uiManager.createUI(season);

        Scene scene = new Scene(root, 720, 620);
        primaryStage.setTitle("Fifa League Table");
		uiManager.rename_stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        season = FileOperations.get_season_count();
        player_count = FileOperations.get_player_count();
        data_arr = new int[player_count][9];
        if (season == 0) {
			season = 1;
            FileOperations.set_data("main_data.flt", 1, 2, "1");
            FileOperations.create_new_season();
            ArrayOperations.initialize(data_arr);
            FileOperations.create_new_fixture(player_count);
        } else {
            FileOperations.fill_the_array(season, data_arr);
        }
        FileOperations.fill_season_data(season, data_arr); //*  gerek yok
        launch(args);
    }
}

class UIManager {

    private static int[][] data_arr; //static olabilir mi final olabilir mi
	private Button menuButton;
	private ContextMenu contextMenu;
	private int player_count;
	private Stage stage;
	private List<TextField> textFields;
	private String[] player_arr;

    UIManager(int[][] arr, Stage primaryStage) {
        data_arr = arr;
		player_count = FileOperations.get_player_count();
		stage = primaryStage;
		//? rename_stage();
    }

	public void rename_stage()
	{
		stage.setTitle("Fifa League Title (Season: " + FileOperations.get_season_count() + ")");
	}

    public BorderPane createUI(int season) {
        TableView<String[]> table = createTable();
        VBox rightPane = createRightPane(data_arr, table);
        menuButton = create_season_menu(season);

        SplitPane splitPane = new SplitPane(table, rightPane);
        splitPane.setDividerPositions(0.64);

        VBox topBar = new VBox(10, menuButton);

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

        ObservableList<String[]> data = FXCollections.observableArrayList();

        for (int i = 0; i < player_count; i++) {
            String[] row = new String[9];
            row[0] = FileOperations.get_player(i + 1);
            for (int j = 0; j < 8; j++) {
                row[j + 1] = String.valueOf(data_arr[i][j]);
            }
            data.add(row); // Satırı veriye ekle
        }

        table.setItems(data);

        return table;
    }

	private void updateTable(TableView<String[]> table) {
		ObservableList<String[]> data = FXCollections.observableArrayList();
	
		for (int i = 0; i < player_count; i++) {
			String[] row = new String[9];
			row[0] = FileOperations.get_player(i + 1);
			for (int j = 0; j < 8; j++) {
				row[j + 1] = String.valueOf(data_arr[i][j]);
			}
			data.add(row);
		}
	
		table.setItems(data);
	}
	
    private VBox createRightPane(int[][] array, TableView<String[]> table) {
        player_arr = FileOperations.create_player_array();
        int line_count = 0;

        for (int i = 1; i < player_count; i++) {
            line_count += i;
        }
        line_count *= 2;

        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(5, 0, 0, 0));

        textFields = new ArrayList<>();

        for (int i = 1; i <= line_count; i++) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER);

            // Oyuncu adlarını hizalamak için sabit genişlik
            Label player1 = new Label(player_arr[Integer.parseInt(FileOperations.get_data("seasonf" + FileOperations.get_season_count() + ".flt", i, 1))]);
            player1.setMinWidth(80); // Genişlik belirle
            player1.setAlignment(Pos.CENTER_RIGHT);

            TextField tf1 = new TextField();
            tf1.setMaxWidth(25);
            tf1.setPrefWidth(25);
            tf1.setPrefHeight(20);
            tf1.setStyle("-fx-font-size: 16px; -fx-padding: 2px;");

            // TextField'i listeye ekle
            textFields.add(tf1);

            Label dash = new Label("-");
            dash.setMinWidth(10);
            dash.setAlignment(Pos.CENTER);

            TextField tf2 = new TextField();
            tf2.setMaxWidth(25);
            tf2.setPrefWidth(25);
            tf2.setPrefHeight(20);
            tf2.setStyle("-fx-font-size: 16px; -fx-padding: 2px;");

            // TextField'i listeye ekle
            textFields.add(tf2);

            Label player2 = new Label(player_arr[Integer.parseInt(FileOperations.get_data("seasonf" + FileOperations.get_season_count() + ".flt", i, 2))]);
            player2.setMinWidth(80);
            player2.setAlignment(Pos.CENTER_LEFT);

            row.getChildren().addAll(player1, tf1, dash, tf2, player2);
            vbox.getChildren().add(row);
        }

        // TextField'lerin verilerini döngüyle fonksiyona gönder
        for (int i = 0; i < textFields.size(); i++) {
            TextField tf = textFields.get(i);
            final int index = i;  // i'yi sabitlemek için final kullanıyoruz

            tf.textProperty().addListener((observable, oldValue, newValue) -> {
				String valueToSet = newValue;
				// \\d rakamıifade eder, \\d+ birden fazla rakamı ifade eder
				if (newValue == null || newValue.trim().isEmpty() || !newValue.matches("\\d+"))
					valueToSet = "-";
				FileOperations.set_data("seasonf" + FileOperations.get_season_count() + ".flt", (index/2) + 1, (index % 2) + 3, valueToSet);
				ArrayOperations.fixture_to_season_array(player_count, array);
				updateTable(table);
			});
        }
        return vbox;
    }

	private void reset_text_fields() {
		for (int i = 0; i < textFields.size(); i++) {
			textFields.get(i).setText("");
		}
	}
	
    private Button create_season_menu(int season) {
        menuButton = new Button("Sezonlar");
        contextMenu = new ContextMenu();

        update_season_menu(season);
        menuButton.setOnAction(e -> contextMenu.show(menuButton,
                menuButton.localToScreen(0, menuButton.getHeight()).getX(),
                menuButton.localToScreen(0, menuButton.getHeight()).getY()));
		return (menuButton);
    }

	private void update_season_menu(int season) {
        contextMenu.getItems().clear();
        int season_count = FileOperations.get_season_count();
        for (int i = 1; i <= season_count; i++) {
            MenuItem item = new MenuItem("Sezon " + i);
            contextMenu.getItems().add(item);
        }
        
        MenuItem newSeason = new MenuItem("YENİ SEZON +");
        newSeason.setOnAction(e -> {
            new_season(season);
        });
        
        contextMenu.getItems().add(newSeason);
    }

	private void new_season(int season)
	{
		FileOperations.fill_season_data(season, data_arr);
		season++;
		FileOperations.set_data("main_data.flt", 1, 2, String.valueOf(season));
		FileOperations.create_new_season();
		ArrayOperations.initialize(data_arr);
		FileOperations.fill_season_data(season, data_arr);
		FileOperations.create_new_fixture(player_count);
		player_arr = FileOperations.create_player_array(); //>  ismini değiş fonksiyonun
		update_season_menu(season);
		rename_stage();
		reset_text_fields();
	}

    private void applyStyles(BorderPane root, TableView<String[]> table, VBox rightPane, Button menuButton) {
        String css = """
            -fx-background-color:rgb(233, 233, 233);
            -fx-control-inner-background:rgb(233, 233, 233);
            -fx-accent:rgb(219, 10, 10);
            -fx-focus-color: #B3D8A8;
            -fx-text-fill: #000000;
            -fx-font-size: 18px;
            -fx-font-family: 'Arial';
        """;

        root.setStyle(css);
        table.setStyle("-fx-background-color:rgb(93, 146, 245); -fx-text-fill:rgb(233, 233, 233);");
        rightPane.setStyle("-fx-background-color:rgb(173, 175, 77); -fx-text-fill:rgb(233, 233, 233);");
        menuButton.setStyle("-fx-background-color:rgb(33, 206, 91); -fx-text-fill:rgb(233, 233, 233);");
    }
}
