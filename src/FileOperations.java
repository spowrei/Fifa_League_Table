
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileOperations {

    public static String get_data(String file_name, int row, int col) {
        String ret = "0";
        try {
            File file = new File("../data/" + file_name);
            String player_data = "";
            Scanner scanner = new Scanner(file);
            for (int i = 0; i < row; i++) {
                player_data = scanner.nextLine();
            }
            int space_count = 0;
            int data_size = player_data.length();
            int begin_index = 0;
            int end_index = 0;
            for (int i = 0; i < data_size; i++) {
                if (player_data.charAt(i) == ' ') {
                    space_count++;
                }
                if (space_count == col && col != 0) {
                    begin_index = i + 1;
                } else if (space_count > col || i == data_size - 1) {
                    end_index = i;
                    break;
                }
            }
            System.out.println(player_data.substring(begin_index, end_index));
            //ret = player_data.substring(begin_index, end_index);
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Dosya bulunamadi.");
            e.printStackTrace();
        }

        return (ret);
    }

    public static int get_player_count() {
        try {
            File file = new File("../data/" + "player_data.flt");
            Scanner scanner = new Scanner(file);
            int i;
            for (i = 1; scanner.hasNextLine(); i++) {
                String temp = scanner.nextLine();
                if (temp.equals("")) {
                    i--;
                    break;
                }
            }
            scanner.close();
            i--;
            System.out.println("player count: " + i);
            return (i);
        } catch (FileNotFoundException e) {
            System.out.println("Dosya bulunamadi.");
            e.printStackTrace();
        }
        return (1);
    }

    public static int get_season_count() {
        return (Integer.parseInt(get_data("main_data.flt", 0, 1)));
    }

    public static String get_player(int i) {
		System.out.println("<<<<<<" + get_data("player_data.flt", i, 0));
        return (get_data("player_data.flt", i, 0));
    }

    public static void create_new_season() {
        int new_season = get_season_count() + 1;
        String season_name = "season" + new_season;
        try {
            File file = new File("../data/" + season_name + ".flt");
            if (!file.createNewFile()) {
                System.out.println("Dosya zaten var.");
            }
        } catch (IOException e) {
            System.out.println("Hata 2201.");
            e.printStackTrace();
        }
    }

    public static void fill_the_array(int season_num, int[][] arr) {
        int player_count = arr.length;
        for (int i = 0; i < player_count; i++) {
            for (int j = 0; j < 8; j++) {
                arr[i][j] = Integer.parseInt(get_data("season" + season_num + ".flt", i, j));
            }
        }
    }

    public static void fill_season_data(int season_num, int[][] arr) {
        try {
            FileWriter file = new FileWriter("../data/" + "season" + season_num + ".flt");
            int player_count = get_player_count();
            for (int i = 0; i < player_count; i++) {
                file.write(get_player(i + 1));
                for (int j = 0; j < 8; j++) {
                    file.write(" " + arr[i][j]);
                }
                file.write("\n");
            }
            file.close();
        } catch (IOException e) {
            System.out.println("Hata 2401.");
            e.printStackTrace();
        }
    }

}
