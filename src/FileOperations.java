
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FileOperations {

    public static String get_data(String file_name, int row, int element) {
        String ret = "0";
        try {
            File file = new File("../data/" + file_name);
            String player_data = "";
            Scanner scanner = new Scanner(file);
            for (int i = 0; i < row; i++) {
                player_data = scanner.nextLine();
            }
            int space_count = 0;
            int begin_index = 0;
            int end_index = player_data.length();
            for (int i = 0; i < end_index; i++) {
                if (player_data.charAt(i) == ' ') {
                    space_count++;
                }
                if (space_count == element - 1 && element != 1) {
                    begin_index = i; 
                }else if (element == 1) {
                    begin_index = 0;
                }
                if (space_count == element) {
                    end_index = i;
                    break;
                }
            }
            System.out.println(">>>> " + "b:" + begin_index + "  e:" + end_index + "  ->" + player_data.substring(begin_index, end_index));
            ret = player_data.substring(begin_index, end_index);
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Dosya bulunamadi. 2101");
            e.printStackTrace();
        }

        return (ret);
    }

    public static void set_data(String file_name, int row, int element, String new_value) {
        try {
            File file = new File("../data/" + file_name);
            List<String> lines = new ArrayList<>();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();
            String[] data = lines.get(row - 1).split(" ");
            data[element - 1] = new_value;
            lines.set(row - 1, String.join(" ", data));
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            for (String line : lines) {
                writer.println(line);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Dosya islem hatasi olustu.");
            e.printStackTrace();
        }
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
            return (i);
        } catch (FileNotFoundException e) {
            System.out.println("Dosya bulunamadi.2201");
            e.printStackTrace();
        }
        return (1);
    }

    public static int get_season_count() {
        return (Integer.parseInt(get_data("main_data.flt", 1, 2)));
    }

    public static String get_player(int i) { //! buna bi bak
        return (get_data("player_data.flt", i, 1));
    }

    public static void create_new_season() {
        int new_season = get_season_count() + 1;
        String[] file_names = {"season" + new_season, "seasonf" + new_season};

        for (int i = 0; i < 2; i++) {
            String file_name = file_names[i];
            try {
                File file = new File("../data/" + file_name + ".flt");
                if (!file.createNewFile()) {
                    System.out.println("Dosya zaten var: " + file_name);
                }
            } catch (IOException e) {
                System.out.println("Hata 2201: " + file_name);
                e.printStackTrace();
            }
        }
    }

    public static void fill_the_array(int season_num, int[][] arr) {
        int player_count = arr.length;
        for (int i = 1; i <= player_count; i++) {
            for (int j = 1; j <= 8; j++) {
                arr[i - 1][j - 1] = Integer.parseInt(get_data("season" + season_num + ".flt", i, j));
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

    public static void create_new_fixture(int n) {
        int season = get_season_count() + 1;
        String file_name = "../data/" + "seasonf" + season + ".flt";
        try {
            FileWriter file = new FileWriter(file_name);

            ArrayList<String> pairs = new ArrayList<>();

            // İkili kombinasyonları oluştur
            for (int i = 0; i < n; i++) {
                for (int j = i; j < n; j++) {
                    pairs.add(i + " " + j);
                }
            }

            // Rastgele sırayla yazdır
            Collections.shuffle(pairs);

            for (String pair : pairs) {
                file.write(pair + " 0 0\n");
            }
            for (String pair : pairs) {
                String[] numbers = pair.split(" ");
                file.write(numbers[1] + " " + numbers[0] + " 0 0\n");
            }

            file.close();
        } catch (IOException e) {
            System.out.println("Bir hata olustu. fixture_creater");
            e.printStackTrace();
        }
    }

    public static String[] create_player_array() {
        int player_count = get_player_count();
        String[] arr = new String[player_count];

        for (int i = 0; i < player_count; i++) {
            arr[i] = get_data("../data/player_data.flt", i+1, 1);
        }
        return (arr);
    }
}
