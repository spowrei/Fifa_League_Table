public class ArrayOperations {
	public static void initialize(int[][] arr)
	{
		int row = arr.length;
		int col = arr[0].length;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				arr[i][j] = 0;
			}
		}
	}
}
