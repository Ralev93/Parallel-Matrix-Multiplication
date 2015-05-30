package bg.uni_sofia.fmi.java.matrix_multiplication.matrix;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author a
 */
public class Matrix {

    private int rows; // m
    private int columns; // n
    private double[][] matrix;
    
    private File file;

    public Matrix() {
        this(0, 0);
    }

    public Matrix(double[][] matrix) {
        rows = matrix.length;
        columns = matrix[0].length;
        this.matrix = matrix;
    }

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        matrix = new double[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setElementAt(int row, int column, double x) {
        matrix[row][column] = x;
    }

    public double getElementAt(int row, int column) {
        return matrix[row][column];
    }

    public void multiplyFromTo(Matrix left, Matrix right, int begin, int end) {
        int i, j;
        
        for (int k = begin; k < end; k++) {
            for (i = 0; i < left.getColumns(); i++) {
                for (j = 0; j < right.getColumns(); j++) {
                    this.matrix[k][j] += left.getElementAt(k, i) * right.getElementAt(i, j);
                }
            }
        }
    }

    public void readFromFile(File file) throws FileNotFoundException, IOException {
//        file = new File(DEFAULT_DIR + path);
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(
                new FileInputStream(file)))) {

            rows = in.readInt();//m
            columns = in.readInt();//n
            matrix = new double[rows][columns];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    matrix[i][j] = in.readDouble();
                }
            }
        }
    }
    

    public void writeToFile(String path) throws FileNotFoundException, IOException {
        file = new File("." + path);
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
                new FileOutputStream(file)))) {

            out.writeInt(rows);
            out.writeInt(columns);

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    out.writeDouble(matrix[i][j]);
                }
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Matrix other = (Matrix) obj;
        if (this.rows != other.rows) {
            return false;
        }
        if (this.columns != other.columns) {
            return false;
        }
        if (!Arrays.deepEquals(this.matrix, other.matrix)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("");
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                str.append(String.format("%f \t", getElementAt(i, j)));
            }
            str.append("\n");
        }
        return new String(str);
    }

}
