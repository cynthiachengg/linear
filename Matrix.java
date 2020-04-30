import java.util.Scanner;

public class Matrix {
	public int row;
	public int col;
	private double[][] arr;
	// private int deleteRow;

	public Matrix() {
		this(0, 0);
	}

	public Matrix(int n) {
		this(n, n);
	}

	public Matrix(int m, int n) {
		this.row = m;
		this.col = n;
		this.arr = new double[m][n];
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				this.arr[i][j] = 0;
			}
		}
	}

	public Matrix(int num, boolean t) {
		this.row = num;
		this.col = num;
		this.arr = new double[num][num];
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				if (i == j) {
					this.arr[i][j] = 1;
				} else {
					this.arr[i][j] = 0;
				}
			}
		}
	}

	public Matrix(double[][] arr) {
		// can be broken if someone inputs a 2d array which has a diff number of
		// elements in each row
		this.row = arr.length;
		this.col = arr[0].length;
		this.arr = new double[this.row][this.col];
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				this.arr[i][j] = arr[i][j];
			}
		}
	}

	public void printMatrix() {
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				System.out.print(this.arr[i][j] + "  ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public double getElement(int row, int col) {
		return arr[row][col];
	}
	

	// idea override '+' operation
	public Matrix add(Matrix other) {
		if ((this.row == other.row) && (this.col == other.col)) {
			Matrix finalMatrix = new Matrix(this.row, this.col);
			for (int i = 0; i < this.row; i++) {
				for (int j = 0; j < this.col; j++) {
					finalMatrix.arr[i][j] = this.arr[i][j] + other.arr[i][j];
				}
			}
			return finalMatrix;
		}
		return null;
	}

	public Matrix scalarMult(int num) {
		Matrix resMatrix = new Matrix(this.row, this.col);
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				resMatrix.arr[i][j] = this.arr[i][j] * num;
			}
		}
		return resMatrix;
	}

	public Matrix sub(Matrix other) {
		return add(other.scalarMult(-1));
	}

	public Matrix mult(Matrix other) {
		if (this.col == other.row) {
			Matrix resMatrix = new Matrix(this.row, other.col);
			for (int i = 0; i < this.row; i++) {
				for (int j = 0; j < this.col; j++) {
					resMatrix.arr[i][j] = multCell(this, other, i, j);
				}
			}
			return resMatrix;
		}
		return null;
	}

	private int multCell(Matrix first, Matrix second, int row, int col) {
		int cell = 0;
		for (int i = 0; i < second.arr.length; i++) {
			cell += first.arr[row][i] * second.arr[i][col];
		}
		return cell;
	}

	private double findScalar(int row1, int row2, int col) {
		// if y = 0 dont do anything to the Matrix
		// if x = 0 need to switch the order of rows around
		double x = this.arr[row1][col];
		double y = this.arr[row2][col];
		// this.printMatrix();
		// System.out.println("x val: " + x);
		// System.out.println("y val: " + y);
		// System.out.println(-x/y);
		if (x == 0)
			return 0.0;
		return -y / x;
	}

	private void divRow(int row1, double num) {
		// System.out.println(num);
		for (int i = 0; i < this.row; i++) {
			this.arr[row1][i] = (1 / num) * this.arr[row1][i];
		}
	}

	private void addMultRows(int row1, int row2, double scalar) {
		// adds to the row2
		for (int i = 0; i < this.row; i++) {
			this.arr[row2][i] = scalar * this.arr[row1][i] + this.arr[row2][i];
		}
	}

	private boolean rowEqZero(int row1) {
		boolean equalsZero = true;
		for (int i = 0; i < this.row; i++) {
			if ((int) this.arr[row1][i] != 0) {
				equalsZero = false;
			}
		}
		return equalsZero;
	}

	public Matrix inverse() {
		int invertible = this.zeroInPivots();
		if(invertible>=0) {
			if(!this.rowShift()) {
				return null;
			}
		}
		else if(invertible==-2) {
			return null;
		}
		Matrix fin = new Matrix(this.row, true);
		for (int i = 0; i < this.col; i++) {
			// System.out.println("column: " + i);
			for (int j = 0; j < this.col - i - 1; j++) {
				int num2 = this.col - j - 1;
				// System.out.println("row: " + num2);
				double x = findScalar(i, num2, i);
				// System.out.println(x);
				this.addMultRows(i, num2, x);
				fin.addMultRows(i, num2, x);
				// fin.printMatrix();
				// this.printMatrix();
			}
		}
		for (int i = 0; i < this.col; i++) {
			int k = this.col - i - 1;
			// System.out.println("column: " + k);
			for (int j = 0; j < this.col - i - 1; j++) {
				int num3 = j;
				// System.out.println("row: " + num3);
				double y = findScalar(k, num3, k);
				// System.out.println(y);
				// fin.multRow(x, num2);
				this.addMultRows(k, num3, y);
				fin.addMultRows(k, num3, y);
				// fin.printMatrix();
				// this.printMatrix();
			}
		}
		for (int i = 0; i < this.row; i++) {
			if (rowEqZero(i) == true) {
				return null;
			}
		}
		for (int i = 0; i < this.row; i++) {
			// System.out.println(this.arr[i][i]);
			if (this.arr[i][i] != 1) {
				fin.divRow(i, this.arr[i][i]);
				this.divRow(i, this.arr[i][i]);
			}
		}
		// this.printMatrix();
		// fin.printMatrix();
		return fin;
	}

	public Matrix lower() {
		Matrix fin = new Matrix(this.row, true);
		for (int i = 0; i < this.col; i++) {
			for (int j = 0; j < this.col - i - 1; j++) {
				int num2 = this.col - j - 1;
				double x = findScalar(i, num2, i);
				this.addMultRows(i, num2, x);
				fin.addMultRows(i, num2, x);
			}
		}
		return fin;
	}

	public Matrix upper() {
		Matrix fin = new Matrix(this.row, true);
		for (int i = 0; i < this.col; i++) {
			int k = this.col - i - 1;
			for (int j = 0; j < this.col - i - 1; j++) {
				int num3 = j;
				double y = findScalar(k, num3, k);
				this.addMultRows(k, num3, y);
				fin.addMultRows(k, num3, y);
			}
		}
		return fin;
	}

	public int zeroInPivots() {
		if (row != col) {
			return -2; // means matrix is not square
		}
		for (int i = 0; i < row; i++) {
			if (arr[i][i] == 0) {
				return i; // returns row with 0
			}
		}
		return -1; // means there are no 0s in any pivots
	}

	public boolean rowShift() {
		if (row != col) {
			return false;
		}
		for (int i = 0; i < row; i++) {
			if (arr[i][i] == 0) {
				for (int j = 0; j < row; j++) {
					if (arr[j][i] != 0 && j != i && arr[i][j] != 0) {
						double[] temp = arr[j];
						arr[j] = arr[i];
						arr[i] = temp;
						rowShift();
					}
				}
			}
		}
		if (zeroInPivots() == -1) {
			return true;
		}
		return false;
	}
}
