
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

	private static int[][] data_arr;
	private static int season;
	private static int player_count;

	@Override
	public void start(Stage primaryStage) {
		UIManager uiManager = new UIManager(data_arr, primaryStage);
		BorderPane root = uiManager.createUI();
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
		launch(args);
	}
}

class UIManager {

	private static int[][] data_arr;
	private Button menuButton;
	private ContextMenu contextMenu;
	private int player_count;
	private Stage stage;
	private List<TextField> textFields;
	private String[] player_arr;
	private VBox vbox;
	private TableView<String[]> table;
	private int season;

	UIManager(int[][] arr, Stage primaryStage) {
		data_arr = arr;
		player_arr = FileOperations.create_player_array();
		player_count = FileOperations.get_player_count();
		stage = primaryStage;
		season = FileOperations.get_season_count();
	}

	public void rename_stage() {
		stage.setTitle("Fifa League Title (Season: " + season + ")");
	}

	public BorderPane createUI() {
		table = create_table();
		update_table();
		VBox rightPane = create_right_pane();
		menuButton = create_season_menu();

		SplitPane splitPane = new SplitPane(table, rightPane);
		splitPane.setDividerPositions(0.64);
		VBox topBar = new VBox(10, menuButton);
		BorderPane root = new BorderPane();
		root.setTop(topBar);
		root.setCenter(splitPane);
		apply_styles(root, rightPane, menuButton);
		return root;
	}

	private TableView<String[]> create_table() {
		TableView<String[]> new_table = new TableView<>();
		String[] columnNames = { "Oyuncu", "OM", "G", "B", "M", "P", "AG", "YG", "A" };

		for (int i = 0; i < columnNames.length; i++) {
			TableColumn<String[], String> column = new TableColumn<>(columnNames[i]);
			final int colIndex = i;
			column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[colIndex]));
			new_table.getColumns().add(column);
		}
		return new_table;
	}

	private void update_table() {
		ObservableList<String[]> data = FXCollections.observableArrayList();

		ArrayOperations.fixture_to_season_array(data_arr, player_count, season);
		for (int i = 0; i < player_count; i++) {
			String[] row = new String[9];
			row[0] = player_arr[i];
			for (int j = 0; j < 8; j++) {
				row[j + 1] = String.valueOf(data_arr[i][j]);
			}
			data.add(row);
		}
		table.setItems(data);
	}

	private void sort_table() {
		int[] temp_arr = new int[player_count];

		for (int i = 0; i < player_count; i++) {
			temp_arr[i] = i;
		}
		for (int i = 0; i < player_count - 1; i++) {
			for (int j = i + 1; j < player_count; j++) {
				int i_point = data_arr[temp_arr[i]][4] * 10000
						+ data_arr[temp_arr[i]][7] * 100
						+ data_arr[temp_arr[i]][5];
				int j_point = data_arr[temp_arr[j]][4] * 10000
						+ data_arr[temp_arr[j]][7] * 100
						+ data_arr[temp_arr[j]][5];
				if (i_point < j_point) {
					int temp = temp_arr[i];
					temp_arr[i] = temp_arr[j];
					temp_arr[j] = temp;
				}
			}
		}
		System.out.println(
				temp_arr[0] + " " + temp_arr[1] + " " + temp_arr[2] + " " + temp_arr[3] + " " + temp_arr[4] + " ");
		ObservableList<String[]> sortedList = FXCollections.observableArrayList();
		ObservableList<String[]> items = table.getItems();
		for (int i = 0; i < player_count; i++) {
			sortedList.add(items.get(temp_arr[i]));
		}
		table.setItems(sortedList);
	}

	private void update_right_pane() {
		int line_count = 0;

		for (int i = 1; i < player_count; i++) {
			line_count += i;
		}
		line_count *= 2;
		vbox.getChildren().clear();
		textFields.clear();
		for (int i = 1; i <= line_count; i++) {
			HBox row = new HBox(10);
			row.setAlignment(Pos.CENTER);
			Label player1 = new Label(player_arr[Integer.parseInt(
					FileOperations.get_data("seasonf" + season + ".flt", i, 1))]);
			player1.setMinWidth(80);
			player1.setAlignment(Pos.CENTER_RIGHT);
			TextField tf1 = new TextField();
			tf1.setMaxWidth(25);
			tf1.setPrefWidth(25);
			tf1.setPrefHeight(20);
			tf1.setStyle("-fx-font-size: 16px; -fx-padding: 2px;");
			tf1.setTextFormatter(
					new TextFormatter<>(change -> change.getControlNewText().length() <= 2 ? change : null));
			textFields.add(tf1);
			Label dash = new Label("-");
			dash.setMinWidth(10);
			dash.setAlignment(Pos.CENTER);
			TextField tf2 = new TextField();
			tf2.setMaxWidth(25);
			tf2.setPrefWidth(25);
			tf2.setPrefHeight(20);
			tf2.setStyle("-fx-font-size: 16px; -fx-padding: 2px;");
			textFields.add(tf2);
			tf2.setTextFormatter(
					new TextFormatter<>(change -> change.getControlNewText().length() <= 2 ? change : null));
			Label player2 = new Label(
					player_arr[Integer.parseInt(FileOperations.get_data("seasonf" + season + ".flt", i, 2))]);
			player2.setMinWidth(80);
			player2.setAlignment(Pos.CENTER_LEFT);
			row.getChildren().addAll(player1, tf1, dash, tf2, player2);
			vbox.getChildren().add(row);
		}
		for (int i = 0; i < textFields.size(); i++) {
			TextField tf = textFields.get(i);
			final int index = i;
			String file_name = "seasonf" + season + ".flt";
			String value = FileOperations.get_data(file_name, index / 2 + 1, index % 2 + 3);

			if (!value.equals("-") && textFields.get(i).getText().isEmpty()) {
				Platform.runLater(() -> textFields.get(index).setText(value));
			}
			tf.textProperty().addListener((observable, oldValue, newValue) -> {
				String valueToSet = newValue;
				if (newValue == null || newValue.trim().isEmpty() || !newValue.matches("\\d+")) {
					valueToSet = "-";
				}
				FileOperations.set_data(file_name, (index / 2) + 1, (index % 2) + 3, valueToSet);
				ArrayOperations.fixture_to_season_array(data_arr, player_count, season);
				update_table();
				sort_table();
			});
		}
	}

	private VBox create_right_pane() {
		vbox = new VBox(5);
		vbox.setPadding(new Insets(5, 0, 0, 0));
		textFields = new ArrayList<>();
		update_right_pane();
		return vbox;
	}

	private Button create_season_menu() {
		menuButton = new Button("Sezonlar");
		contextMenu = new ContextMenu();

		update_season_menu();
		menuButton.setOnAction(e -> contextMenu.show(menuButton,
				menuButton.localToScreen(0, menuButton.getHeight()).getX(),
				menuButton.localToScreen(0, menuButton.getHeight()).getY()));
		return (menuButton);
	}

	private void update_season_menu() {
		contextMenu.getItems().clear();
		for (int i = 1; i <= FileOperations.get_season_count(); i++) {
			final int final_i = i;
			MenuItem item = new MenuItem("Sezon " + i);
			item.setOnAction(e -> {
				this_season(final_i);
			});
			contextMenu.getItems().add(item);
		}
		MenuItem newSeason = new MenuItem("YENİ SEZON +");
		newSeason.setOnAction(e -> {
			new_season();
		});
		contextMenu.getItems().add(newSeason);
	}

	private void this_season(int i) {
		season = i;
		ArrayOperations.fixture_to_season_array(data_arr, player_count, season);
		update_right_pane();
		update_table();
		rename_stage();
	}

	private void new_season() {
		season = FileOperations.get_season_count() + 1;
		FileOperations.set_data("main_data.flt", 1, 2, String.valueOf(season));
		FileOperations.create_new_season();
		FileOperations.create_new_fixture(player_count);
		ArrayOperations.initialize(data_arr);
		ArrayOperations.fixture_to_season_array(data_arr, player_count, season);
		update_table();
		update_season_menu();
		rename_stage();
		update_right_pane();
	}

	private void apply_styles(BorderPane root, VBox rightPane, Button menuButton) {
		String css = """
					-fx-background-color:rgb(233, 233, 233);
					-fx-control-inner-background:rgb(233, 233, 233);
					-fx-accent:rgb(169, 218, 169);
					-fx-focus-color: rgb(169, 218, 169);
					-fx-text-fill: rgb(233, 233, 233);
					-fx-font-size: 16px;
					-fx-font-family: 'Arial';
				""";
	
		root.setStyle(css);
		rightPane.setStyle("-fx-background-color:rgb(169, 218, 169); -fx-text-fill: #f1fa8c;");
		menuButton.setStyle("-fx-background-color:rgb(123, 163, 123); -fx-text-fill: #ffffff;");
	}
}
