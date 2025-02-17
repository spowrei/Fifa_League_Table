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
        // Sol tarafi tablo olarak olustur
        TableView<String[]> table = new TableView<>();
        TableColumn<String[], String> col1 = new TableColumn<>("Oyuncu");
        TableColumn<String[], String> col2 = new TableColumn<>("OM");
        TableColumn<String[], String> col3 = new TableColumn<>("G");
        TableColumn<String[], String> col4 = new TableColumn<>("B");
        TableColumn<String[], String> col5 = new TableColumn<>("M");
        TableColumn<String[], String> col6 = new TableColumn<>("P");
        TableColumn<String[], String> col7 = new TableColumn<>("AG");
		TableColumn<String[], String> col8 = new TableColumn<>("YG");
		TableColumn<String[], String> col9 = new TableColumn<>("A");

        // Sütunlara değer atama
        col1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        col2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        col3.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));
        col4.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3]));
        col5.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[4]));
        col6.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[5]));
        col7.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[6]));
		col8.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[7]));
        col9.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[8]));

        table.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7, col8, col9);

        // Tabloya 5 oyuncu ekle ve tüm değerleri 0 yap
        ObservableList<String[]> data = FXCollections.observableArrayList(
			new String[]{"Ahmet", "0", "0", "0", "0", "0", "0", "0", "0"},
			new String[]{"Kaan", "0", "0", "0", "0", "0", "0", "0", "0"},
			new String[]{"Mehmet", "0", "0", "0", "0", "0", "0", "0", "0"},
			new String[]{"Mustafa", "0", "0", "0", "0", "0", "0", "0", "0"},
            new String[]{"Yahya", "0", "0", "0", "0", "0", "0", "0", "0"}
        );
        table.setItems(data);

        // Sag taraf: string, textfield, string, textfield, string (yan yana olacak sekilde)
        HBox rightPane = new HBox(10);
        rightPane.getChildren().add(new Label("Mustafa"));

        TextField tf1 = new TextField();
        tf1.setMaxWidth(40); // Sadece iki karakter icin genislik ayarla
		tf1.setMaxHeight(20); // Sadece iki karakter icin genislik ayarla
        rightPane.getChildren().add(tf1);

        rightPane.getChildren().add(new Label("-"));

        TextField tf2 = new TextField();
        tf2.setMaxWidth(40); // Sadece iki karakter icin genislik ayarla
		tf2.setMaxHeight(20); // Sadece iki karakter icin genislik ayarla
        rightPane.getChildren().add(tf2);

        rightPane.getChildren().add(new Label("Ahmet"));

        // SplitPane ile iki bolme
        SplitPane splitPane = new SplitPane(table, rightPane);
        splitPane.setDividerPositions(0.7);

        // Sol uste buton
        Button menuButton = new Button("Geçmiş Sezonlar");

        // Acilir menu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Season 1");
        MenuItem item2 = new MenuItem("Season 2");
        MenuItem item3 = new MenuItem("Season 3");
        contextMenu.getItems().addAll(item1, item2, item3);

        // Butona tiklandiginda menuyu goster
        menuButton.setOnAction(e -> contextMenu.show(menuButton,
                menuButton.localToScreen(0, menuButton.getHeight()).getX(),
                menuButton.localToScreen(0, menuButton.getHeight()).getY()));

        // Ust kisim
        HBox topBar = new HBox(10, menuButton);

        // Ana layout
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(splitPane);

        // CSS stilini uygula
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
        contextMenu.setStyle("-fx-background-color: #B3D8A8; -fx-text-fill: #000000;");

        // Sahne ve pencere ayarlari
        Scene scene = new Scene(root, 1000, 400);
        primaryStage.setTitle("Fifa League Table");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}