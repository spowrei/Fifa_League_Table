
public class ArrayOperations {

	public static void initialize(int[][] arr) {
		int row = arr.length;
		int col = arr[0].length;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				arr[i][j] = 0;
			}
		}
	}

	public static void fixture_to_season_array(int[][] arr, int player_count, int season) {
		int line_count = 0;

		initialize(arr);
		for (int i = 1; i < player_count; i++) {
			line_count += i;
		}
		line_count *= 2;
		for (int i = 1; i <= line_count; i++) {
			String[] line = FileOperations.give_fixture_line(i, season);
			if (line[2].equals("-") || line[3].equals("-")) {
				continue;
			}
			int p1 = Integer.parseInt(line[0]);
			int p2 = Integer.parseInt(line[1]);
			int s1 = Integer.parseInt(line[2]);
			int s2 = Integer.parseInt(line[3]);
			arr[p1][0]++;
			arr[p1][5] += s1;
			arr[p1][6] += s2;
			arr[p1][7] += s1 - s2;

			arr[p2][0]++;
			arr[p2][5] += s2;
			arr[p2][6] += s1;
			arr[p2][7] += s2 - s1;
			if (s1 > s2) {
				arr[p1][1]++;
				arr[p2][3]++;
				arr[p1][4] += 3;
			} else if (s1 < s2) {
				arr[p1][3]++;
				arr[p2][1]++;
				arr[p2][4] += 3;
			} else {
				arr[p1][2]++;
				arr[p2][2]++;
				arr[p1][4]++;
				arr[p2][4]++;
			}
		}
	}
}
