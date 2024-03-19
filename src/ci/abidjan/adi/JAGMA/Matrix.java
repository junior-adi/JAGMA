package ci.abidjan.adi.JAGMAv2;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * T can be : Byte, Short, Integer, Long, Float, Double, or BigDecimal
 * 
 * @param <T>
 */
public class Matrix<T> {

	private T[] array;
	private int nrows;
	private int ncols;
	private Class<T> clazz;

	/**
	 * Default constructor for the Matrix class.
	 * <p>
	 * This constructor is provided for scenarios where specific initialization
	 * methods are not required. It does not perform any initialization of the
	 * matrix elements, leaving the matrix in an uninitialized state.
	 * </p>
	 * <p>
	 * This constructor is particularly useful when creating an instance of the
	 * Matrix class and then immediately initializing it with specific dimensions
	 * and values through other methods, such as {@link #setDimensions(int, int)}
	 * and {@link #set(T[])}.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Integer> matrix = new Matrix<>();
	 * matrix.setNrows(3);
	 * matrix.setNcols(3);
	 * matrix.setClazz(Integer.class);
	 * matrix.setArray(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
	 * </pre>
	 * </p>
	 */
	@SuppressWarnings("unchecked")
	public Matrix() {
		this.nrows = 0;
		this.ncols = 0;
		this.clazz = null;
		this.array = (T[]) Array.newInstance(clazz, nrows * ncols);
	}

	/**
	 * Constructs an empty Matrix with the specified element type.
	 *
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Integer> matrix = new Matrix<>(Integer.class);
	 * matrix.setNrows(3);
	 * matrix.setNcols(3);
	 * matrix.setArray(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
	 * </pre>
	 * </p>
	 * 
	 * @param clazz The Class object representing the type of elements in the
	 *              Matrix. It is used to instantiate the underlying array. Note:
	 *              The class should have a parameterless constructor.
	 */
	@SuppressWarnings("unchecked")
	public Matrix(Class<T> clazz) {
		this.nrows = 0;
		this.ncols = 0;
		this.clazz = clazz;
		this.array = (T[]) Array.newInstance(clazz, nrows * ncols);
	}

	/**
	 * Constructor to create a matrix with specified dimensions and array of
	 * elements.
	 * <p>
	 * This constructor initializes a matrix with the specified number of rows,
	 * columns, and an array of elements. It uses a generic approach to create an
	 * array of type T, where T extends Number, to ensure that the matrix can hold
	 * numeric values. The array is initialized with the provided array of elements.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Integer[] data = { 1, 2, 3, 4 };
	 * Matrix<Integer> matrix = new Matrix<>(2, 2, data, Integer.class);
	 * </pre>
	 * </p>
	 *
	 * @param nrows the number of rows in the matrix
	 * @param ncols the number of columns in the matrix
	 * @param array the array of elements to initialize the matrix
	 * @param clazz the class of the matrix elements (e.g., Integer.class,
	 *              Double.class, etc.)
	 * @throws IllegalArgumentException if the dimensions or array are invalid
	 */
	@SuppressWarnings("unchecked")
	public Matrix(int nrows, int ncols, T[] array, Class<T> clazz) {
		if (nrows <= 0 || ncols <= 0 || array == null || array.length != nrows * ncols) {
			throw new IllegalArgumentException("Invalid dimensions or array");
		}
		this.nrows = nrows;
		this.ncols = ncols;
		this.clazz = clazz;

		this.array = (T[]) Array.newInstance(clazz, nrows * ncols);
		System.arraycopy(array, 0, this.array, 0, array.length);
	}

	/**
	 * Constructor to create a matrix with specified dimensions and array of
	 * elements.
	 * <p>
	 * This constructor initializes a matrix with the specified number of rows,
	 * columns, and an array of elements. It uses a generic approach to create an
	 * array of type T, where T extends Number, to ensure that the matrix can hold
	 * numeric values. The array is initialized with the provided array of elements.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Integer[] data = { 1, 2, 3, 4 };
	 * Matrix<Integer> matrix = new Matrix<>(2, 2, data);
	 * </pre>
	 * </p>
	 *
	 * @param nrows the number of rows in the matrix
	 * @param ncols the number of columns in the matrix
	 * @param array the array of elements to initialize the matrix
	 * @throws IllegalArgumentException if the dimensions or array are invalid
	 */
	@SuppressWarnings("unchecked")
	public Matrix(int nrows, int ncols, T[] array) {
		if (nrows <= 0 || ncols <= 0 || array == null || array.length != nrows * ncols) {
			throw new IllegalArgumentException("Invalid dimensions or array");
		}
		this.nrows = nrows;
		this.ncols = ncols;
		this.clazz = (Class<T>) array.getClass().getComponentType();

		this.array = (T[]) Array.newInstance(clazz, nrows * ncols);
		System.arraycopy(array, 0, this.array, 0, array.length);
	}

	/**
	 * Constructor to create a matrix from a 2D array of elements.
	 * <p>
	 * This constructor initializes a matrix with a 2D array of elements. It uses a
	 * efficient System.arraycopy method to copy the elements from the 2D array to
	 * the internal 1D array representation of the matrix.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 } };
	 * Matrix<Double> matrix = new Matrix<>(data, Double.class);
	 * </pre>
	 * </p>
	 *
	 * @param array2D the 2D array of elements to initialize the matrix
	 * @param clazz   the class of the matrix elements (e.g., Integer.class,
	 *                Double.class, etc.)
	 * @throws IllegalArgumentException if the 2D array is invalid
	 */
	@SuppressWarnings("unchecked")
	public Matrix(T[][] array2D, Class<T> clazz) {
		if (array2D == null || array2D.length == 0 || array2D[0] == null || array2D[0].length == 0) {
			throw new IllegalArgumentException("Invalid 2D array");
		}
		this.nrows = array2D.length;
		this.ncols = array2D[0].length;
		this.clazz = clazz;

		this.array = (T[]) Array.newInstance(clazz, nrows * ncols);
		int idx = 0;
		for (int i = 0; i < nrows; i++) {
			if (array2D[i].length != ncols) {
				throw new IllegalArgumentException("All rows of the 2D array must have the same length");
			}
			System.arraycopy(array2D[i], 0, this.array, idx, ncols);
			idx += ncols;
		}
	}

	/**
	 * Constructor to create a matrix from a 2D array of elements.
	 * <p>
	 * This constructor initializes a matrix with a 2D array of elements. It uses a
	 * efficient System.arraycopy method to copy the elements from the 2D array to
	 * the internal 1D array representation of the matrix.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 } };
	 * Matrix<Double> matrix = new Matrix<>(data);
	 * </pre>
	 * </p>
	 *
	 * @param array2D the 2D array of elements to initialize the matrix
	 * @throws IllegalArgumentException if the 2D array is invalid
	 */
	@SuppressWarnings("unchecked")
	public Matrix(T[][] array2D) {
		if (array2D == null || array2D.length == 0 || array2D[0] == null || array2D[0].length == 0) {
			throw new IllegalArgumentException("Invalid 2D array");
		}
		this.nrows = array2D.length;
		this.ncols = array2D[0].length;
		this.clazz = (Class<T>) array2D[0][0].getClass();
		this.array = (T[]) Array.newInstance(clazz, nrows * ncols);

		int idx = 0;
		for (int i = 0; i < nrows; i++) {
			if (array2D[i].length != ncols) {
				throw new IllegalArgumentException("All rows of the 2D array must have the same length");
			}
			System.arraycopy(array2D[i], 0, this.array, idx, ncols);
			idx += ncols;
		}
	}

	/**
	 * Constructor to create a null matrix (initialized to 0) with specified
	 * dimensions.
	 * <p>
	 * This constructor initializes a matrix with the specified number of rows and
	 * columns, and populates it with zeros. It uses a generic approach to create an
	 * array of type T, where T extends Number, to ensure that the matrix can hold
	 * numeric values. The array is initialized to the size of the matrix (rows *
	 * columns) and filled with zeros.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * </pre>
	 * </p>
	 *
	 * @param nrows the number of rows in the matrix
	 * @param ncols the number of columns in the matrix
	 * @param clazz the class of the matrix elements (e.g., Integer.class,
	 *              Double.class, etc.)
	 * @throws IllegalArgumentException if the dimensions are invalid
	 */
	@SuppressWarnings("unchecked")
	public Matrix(int nrows, int ncols, Class<T> clazz) {
		if (nrows <= 0 || ncols <= 0) {
			throw new IllegalArgumentException("Invalid dimensions");
		}

		this.nrows = nrows;
		this.ncols = ncols;
		this.clazz = clazz;
		this.array = (T[]) Array.newInstance(clazz, nrows * ncols);
		Arrays.fill(this.array, zeroValue(clazz, () -> zeroValue()));

	}

	/**
	 * Constructor to create a matrix with specified dimensions and fill it with a
	 * given value.
	 * <p>
	 * This constructor initializes a matrix with the specified number of rows and
	 * columns, and fills it with the provided value. It uses a generic approach to
	 * create an array of type T, where T extends Number, to ensure that the matrix
	 * can hold numeric values.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(2, 3, 1.0, Double.class);
	 * </pre>
	 * </p>
	 *
	 * @param nrows the number of rows in the matrix
	 * @param ncols the number of columns in the matrix
	 * @param value the value to fill the matrix with
	 * @param clazz the class of the matrix elements (e.g., Integer.class,
	 *              Double.class, etc.)
	 * @throws IllegalArgumentException if the dimensions are invalid
	 */
	public Matrix(int nrows, int ncols, T value, Class<T> clazz) {
		this(nrows, ncols, clazz);
		Arrays.fill(array, value);
	}

	/**
	 * Constructor to create a matrix with specified dimensions and fill it with a
	 * given value.
	 * <p>
	 * This constructor initializes a matrix with the specified number of rows and
	 * columns, and fills it with the provided value. It uses a generic approach to
	 * create an array of type T, where T extends Number, to ensure that the matrix
	 * can hold numeric values.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(2, 3, Double.class, 1.0);
	 * </pre>
	 * </p>
	 *
	 * @param nrows the number of rows in the matrix
	 * @param ncols the number of columns in the matrix
	 * @param clazz the class of the matrix elements (e.g., Integer.class,
	 *              Double.class, etc.)
	 * @param value the value to fill the matrix with
	 * @throws IllegalArgumentException if the dimensions are invalid
	 */
	public Matrix(int nrows, int ncols, Class<T> clazz, T value) {
		this(nrows, ncols, value, clazz);
	}

	/**
	 * Constructor to create a matrix from a 1D array with a specified number of
	 * rows.
	 * <p>
	 * This constructor initializes a matrix from a 1D array of elements, with the
	 * specified number of rows. The number of columns is determined by dividing the
	 * length of the array by the number of rows. It uses a generic approach to
	 * create an array of type T, where T extends Number, to ensure that the matrix
	 * can hold numeric values.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Integer[] data = { 1, 2, 3, 4, 5, 6 };
	 * Matrix<Integer> matrix = new Matrix<>(data, 2);
	 * </pre>
	 * </p>
	 *
	 * @param array1D the 1D array of elements to initialize the matrix
	 * @param mrows   the number of rows in the matrix
	 * @throws IllegalArgumentException if the size of the array is not correct
	 */
	@SuppressWarnings("unchecked")
	public Matrix(T[] array1D, int mrows) {
		this.nrows = mrows;
		this.ncols = (mrows != 0 ? array1D.length / mrows : 0);
		if (mrows * ncols != array1D.length) {
			throw new IllegalArgumentException("The size of the array is not correct!");
		}
		this.clazz = (Class<T>) array1D.getClass().getComponentType();
		this.array = Arrays.copyOf(array1D, array1D.length);
	}

	/**
	 * Constructor to create a matrix from a 1D array with a specified number of
	 * columns.
	 * <p>
	 * This constructor initializes a matrix from a 1D array of elements, with the
	 * specified number of columns. The number of rows is determined by dividing the
	 * length of the array by the number of columns. It uses a generic approach to
	 * create an array of type T, where T extends Number, to ensure that the matrix
	 * can hold numeric values.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Double[] data = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };
	 * Matrix<Double> matrix = new Matrix<>(3, data);
	 * </pre>
	 * </p>
	 *
	 * @param mcols   the number of columns in the matrix
	 * @param array1D the 1D array of elements to initialize the matrix
	 * @throws IllegalArgumentException if the size of the array is not correct
	 */
	@SuppressWarnings("unchecked")
	public Matrix(int mcols, T[] array1D) {
		this.ncols = mcols;
		this.nrows = (mcols != 0 ? array1D.length / mcols : 0);
		if (mcols * nrows != array1D.length) {
			throw new IllegalArgumentException("The size of the array is not correct!");
		}
		this.clazz = (Class<T>) array1D.getClass().getComponentType();
		this.array = Arrays.copyOf(array1D, array1D.length);
	}

	/**
	 * Constructs a new Matrix with the specified number of rows and columns,
	 * element type, and supplier.
	 * <p>
	 * This constructor initializes the Matrix with the specified dimensions and
	 * element type. It also initializes the elements of the Matrix using a Supplier
	 * to provide zero values.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * <b> Example 1</b>
	 * 
	 * Matrix<Integer> matrix = new Matrix<>(3, 3, Integer.class, () -> 0);
	 * 
	 * <b> Example 2</b>
	 * // Define the class of the elements
	 * Class<Float> clazz = Float.class;
	 * 
	 * // Define a Supplier that provides the zero value (in this case, 10)
	 * Supplier<Float> supplier = () -> 10.0f;
	 * // Create a new Matrix instance with the specified dimensions, class, and
	 * // supplier
	 * Matrix<Integer> matrix = new Matrix<>(nrows, ncols, clazz, supplier);
	 * 
	 * </pre>
	 * </p>
	 * 
	 * @param nrows    The number of rows in the Matrix.
	 * @param ncols    The number of columns in the Matrix.
	 * @param clazz    The Class object representing the element type of the Matrix.
	 * @param supplier A Supplier providing zero values for the elements of the
	 *                 Matrix.
	 * @throws IllegalArgumentException if nrows or ncols is less than or equal to
	 *                                  zero.
	 */
	@SuppressWarnings("unchecked")
	public Matrix(int nrows, int ncols, Class<T> clazz, Supplier<T> supplier) {
		if (nrows <= 0 || ncols <= 0) {
			throw new IllegalArgumentException("Invalid dimensions");
		}

		this.nrows = nrows;
		this.ncols = ncols;
		this.clazz = clazz;
		this.array = (T[]) Array.newInstance(clazz, nrows * ncols);

		Arrays.fill(this.array, zeroValue(clazz, supplier));
	}

	/**
	 * Returns a zero value of the specified element type using the provided
	 * Supplier.
	 * <p>
	 * This method returns a zero value of the specified element type using a
	 * Supplier. The Supplier is expected to provide the zero value.
	 * </p>
	 * 
	 * @param clazz    The Class object representing the element type.
	 * @param supplier A Supplier providing zero values for the elements.
	 * @return A zero value of the specified element type.
	 */
	public static <T> T zeroValue(Class<T> clazz, Supplier<T> supplier) {
		return supplier.get();
	}

	/*******************************
	 * GETTERS / SETTERS
	 *******************************/

	/**
	 * Retrieves the internal array representing the matrix in 1D format.
	 * <p>
	 * This method returns the internal array used to store the matrix elements. The
	 * array is in a flat, row-major order, meaning that the elements of each row
	 * are stored consecutively in the array.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<Double>(3, 3);
	 * // Assume matrix is initialized with values
	 * Double[] array = matrix.getArray();
	 * // This permits to set elements easily as
	 * array[0] = 1.0;
	 * array[1] = 2.0;
	 * array[2] = 3.0;
	 * array[3] = 4.0;
	 * array[4] = 5.0;
	 * array[5] = 6.0;
	 * array[6] = 7.0;
	 * array[7] = 8.0;
	 * array[8] = 9.0;
	 * </pre>
	 * </p>
	 *
	 * @return The internal array representing the matrix.
	 */
	public T[] getArray() {
		return array;
	}

	/**
	 * Sets the internal array representing the matrix.
	 * <p>
	 * This method updates the internal array used to store the matrix elements. It
	 * takes a new array as input and copies its contents using the
	 * {@link Arrays#copyOf(Object[], int)} method to ensure that the matrix's
	 * internal representation is updated correctly.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Short> matrix = new Matrix<Short>(3, 3);
	 * Short[] newArray = new T[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	 * matrix.setArray(newArray);
	 * </pre>
	 * </p>
	 *
	 * @param array The new array to set as the matrix's internal representation.
	 */
	public void setArray(T[] array) {
//          this.array = Arrays.copyOf(array, array.length);
		// Check if the input array has the correct length
		if (array.length != nrows * ncols) {
			throw new IllegalArgumentException(
					"The input array must have the same length as the total number of elements in the matrix.");
		}

		// Copy elements from the input array to the matrix
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				this.array[i * ncols + j] = array[i * ncols + j];
			}
		}
	}

	/**
	 * Returns the Class object representing the element type T of the matrix.
	 *
	 * @return the Class object representing the element type T
	 */
	public Class<T> getClazz() {
		return clazz;
	}

	/**
	 * Sets the Class object representing the element type T of the matrix.
	 *
	 * @param clazz the Class object representing the element type T
	 */
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Retrieves the number of rows in the matrix.
	 * <p>
	 * This method returns the number of rows in the matrix as an integer. It is a
	 * simple getter method that provides access to the matrix's row count.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Float> matrix = new Matrix<Float>(3, 3);
	 * // Assume matrix is initialized with values
	 * int numRows = matrix.getNrows();
	 * System.out.println("Number of rows: " + numRows);
	 * </pre>
	 * </p>
	 *
	 * @return The number of rows in the matrix.
	 */
	public int getNrows() {
		return nrows;
	}

	/**
	 * Sets the number of rows in the matrix.
	 * <p>
	 * This method updates the number of rows in the matrix. If the new number of
	 * rows is different from the current number, it creates a new array with the
	 * updated size and copies the existing elements into it, preserving the
	 * existing matrix data. This operation may require resizing the internal array
	 * to accommodate the new dimensions, ensuring that the matrix's structure
	 * remains consistent.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Float> matrix = new Matrix<Float>(3, 3);
	 * matrix.setNrows(4); // Resizes the matrix to 4 rows
	 * </pre>
	 * </p>
	 *
	 * @param nrows The new number of rows for the matrix.
	 */
	public void setNrows(int nrows) {
		if (nrows < 0) {
			throw new IllegalArgumentException("Number of rows cannot be negative.");
		}

		int newSize = nrows * ncols;
		if (newSize < 0 || newSize > Integer.MAX_VALUE - 8) {
			throw new OutOfMemoryError("Required array size too large.");
		}

		T[] newArray = Arrays.copyOf(array, newSize);
		this.array = newArray;
		this.nrows = nrows;

		// Handle case where new size is smaller than old size
		if (nrows < this.nrows) {
			// Truncate the array
			System.arraycopy(array, 0, newArray, 0, nrows * ncols);
			// Fill remaining elements with zero value
			Arrays.fill(newArray, nrows * ncols, this.nrows * ncols, zeroValue());
		}
	}

	/**
	 * Retrieves the number of columns in the matrix.
	 * <p>
	 * This method returns the number of columns in the matrix as an integer. It is
	 * a simple getter method that provides access to the matrix's column count.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<Double>(3, 3);
	 * int numCols = matrix.getNcols();
	 * System.out.println("Number of columns: " + numCols);
	 * </pre>
	 * </p>
	 *
	 * @return The number of columns in the matrix.
	 */
	public int getNcols() {
		return ncols;
	}

	/**
	 * Sets the number of columns in the matrix.
	 * <p>
	 * This method updates the number of columns in the matrix. If the new number of
	 * columns is different from the current number, it creates a new array with the
	 * updated size and copies the existing elements into it, preserving the
	 * existing matrix data. This operation may require resizing the internal array
	 * to accommodate the new dimensions, ensuring that the matrix's structure
	 * remains consistent.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Long> matrix = new Matrix<>(3, 3, Long.class);
	 * matrix.setNcols(4); // Resizes the matrix to 4 columns
	 * </pre>
	 * </p>
	 *
	 * @param ncols The new number of columns for the matrix.
	 */
	public void setNcols(int ncols) {
		if (ncols < 0) {
			throw new IllegalArgumentException("Number of columns cannot be negative.");
		}

		int newSize = nrows * ncols;
		if (newSize < 0 || newSize > Integer.MAX_VALUE - 8) {
			throw new OutOfMemoryError("Required array size too large.");
		}

		T[] newArray = Arrays.copyOf(array, newSize);
		this.array = newArray;
		this.ncols = ncols;

		// Handle case where new size is smaller than old size
		if (ncols < this.ncols) {
			// Truncate the array
			System.arraycopy(array, 0, newArray, 0, nrows * ncols);
			// Fill remaining elements with zero value
			Arrays.fill(newArray, nrows * ncols, this.ncols * nrows, zeroValue());
		}
	}

	/**
	 * Gets the dimensions of the matrix.
	 * 
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Integer> matrix = new Matrix<>(3, 4, 0, Integer.class);
	 * int[] dimensions = matrix.getDimensions();
	 * // dimensions[0] will be 3 (number of rows)
	 * // dimensions[1] will be 4 (number of columns)
	 * </pre>
	 * 
	 * @return An array containing the number of rows at index 0 and the number of
	 *         columns at index 1.
	 * @see #getNrows()
	 * @see #getNcols()
	 */
	public int[] getDimensions() {
		return new int[] { this.getNrows(), this.getNcols() };
	}

	/**
	 * Sets the dimensions of the matrix.
	 * 
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Integer> matrix = new Matrix<>(2, 2, 0, Integer.class);
	 * matrix.setDimensions(3, 4);
	 * // The matrix dimensions are now 3 rows by 4 columns.
	 * </pre>
	 * 
	 * @param nrows The new number of rows.
	 * @param ncols The new number of columns.
	 * @see #setNrows(int)
	 * @see #setNcols(int)
	 */
	public void setDimensions(int nrows, int ncols) {
		setNrows(nrows);
		setNcols(ncols);
	}

	/**
	 * Retrieves the value of a specific element in the matrix.
	 * <p>
	 * This method returns the value of the element at the specified row and column
	 * indices. The indices are zero-based, meaning that the first element is at row
	 * 0, column 0.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * // Fill the matrix with values
	 * matrix.set(0, 1L);
	 * matrix.set(1, 2L);
	 * matrix.set(2, 3L);
	 * matrix.set(3, 4L);
	 * // Assume matrix is initLialized with values
	 * System.out.println(matrix.get(0, 1)); // Retrieves the value at row 0, column 1
	 * </pre>
	 * </p>
	 *
	 * @param row The row index of the element to retrieve.
	 * @param col The column index of the element to retrieve.
	 * @return The value of the element at the specified row and column indices.
	 */
	public T get(int row, int col) {
		if (row < 0 || row >= nrows || col < 0 || col >= ncols) {
			throw new IndexOutOfBoundsException("Invalid row or column index.");
		}

		if (array == null) {
			throw new IllegalStateException("The matrix has not been initialized.");
		}

		return array[row * ncols + col];
	}

	/**
	 * Retrieves the value of a specific element in the matrix's internal 1D array.
	 * <p>
	 * This method returns the value of the element at the specified index in the
	 * internal 1D array representing the matrix. The index is zero-based, and it
	 * directly corresponds to the row-major order of the matrix's elements.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Integer> matrix = new Matrix<Integer>(3, 3, Integer.class);
	 * // Assume matrix is initialized with values
	 * Integer value = matrix.get(2); // Retrieves the value at index 2 in the internal array
	 * </pre>
	 * </p>
	 *
	 * @param i The index in the internal 1D array of the element to retrieve.
	 * @return The value of the element at the specified index.
	 */
	public T get(int i) {
		if (i < 0 || i >= array.length) {
			throw new IndexOutOfBoundsException("Invalid array index.");
		}

		if (array == null) {
			throw new IllegalStateException("The matrix has not been initialized.");
		}

		return array[i];
	}

	/**
	 * Sets all elements in the matrix to the specified value.
	 *
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Integer> matrix = new Matrix<Integer>(3, 3, Integer.class);
	 * // Assume matrix is initialized with values
	 * Integer value = matrix.set(2); // set the value 2 to al eleents pf the internal array
	 * </pre>
	 * </p>
	 * 
	 * @param value The value to set for all elements in the matrix.
	 */
	public void set(T value) {
		for (int i = 0; i < array.length; i++) {
			array[i] = value;
		}
	}

	/**
	 * Sets the value of a specific element in the matrix's internal 1D array.
	 * <p>
	 * This method updates the value of the element at the specified index in the
	 * internal 1D array representing the matrix. The index is zero-based, and it
	 * directly corresponds to the row-major order of the matrix's elements.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * matrix.set(2, 10.0); // Sets the value at index 2 in the internal array to 10.0
	 * </pre>
	 * </p>
	 *
	 * @param i     The index in the internal 1D array of the element to update.
	 * @param value The new value to set for the specified element.
	 */
	public void set(int i, T value) {
		if (i < 0 || i >= array.length) {
			throw new IndexOutOfBoundsException("Invalid array index.");
		}

		if (array == null) {
			throw new IllegalStateException("The matrix has not been initialized.");
		}

		array[i] = value;
	}

	/**
	 * Sets the value of a specific element in the matrix.
	 * <p>
	 * This method updates the value of the element at the specified row and column
	 * indices. The indices are zero-based, meaning that the first element is at row
	 * 0, column 0.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Float> matrix = new Matrix<Float>(3, 3, Float.class);
	 * matrix.set(1, 2, 5.0f); // Sets the value at row 1, column 2 to 5.0f
	 * </pre>
	 * </p>
	 *
	 * @param row   The row index of the element to update.
	 * @param col   The column index of the element to update.
	 * @param value The new value to set for the specified element.
	 */
	public void set(int row, int col, T value) {
		if (row < 0 || row >= nrows || col < 0 || col >= ncols) {
			throw new IndexOutOfBoundsException("Invalid row or column index.");
		}

		if (array == null) {
			throw new IllegalStateException("The matrix has not been initialized.");
		}

		array[row * ncols + col] = value;
	}

	/**
	 * Sets a range of elements in the matrix to the values in a given matrix
	 * object. that means that this method consists on setting or replacing the
	 * range "[from;to[" elements of a matrix A by the "[from;to[" elements of a
	 * given matrix B. In mathematics, a range of "[from;to[" is called the N first
	 * elements. It is calculated by "to-from" and counting like 0,1,..,(to-1)
	 * <p>
	 * This method updates a range of elements in the matrix, starting from the
	 * specified starting index and ending before the specified ending index, with
	 * the values from another matrix object. The method checks if the indices and
	 * the size of the matrix object are valid to ensure that the matrix's structure
	 * remains consistent. If the indices are not valid or the matrix object's size
	 * does not match the expected range, an {@link IllegalArgumentException} is
	 * thrown.
	 * 
	 * </p>
	 * <p>
	 * Example usage: T
	 * 
	 * <pre>
	 * Matrix<Double> matrixA = new Matrix<>(3, 3, Double.class); // initialized to 0.00
	 * // Assume matrixA is initialized with values
	 * Matrix<Double> matrixB = new Matrix<>(new Double[][] { { -5.0, -6.0 }, { -7.0, -8.0 } }, Double.class);
	 * // Assume matrixB is initialized with different values
	 * matrixA.set(0, 4, matrixB); // Sets the values from matrixB into matrixA starting at index 0
	 * </pre>
	 * </p>
	 *
	 * @param from The starting index (inclusive) for setting new values.
	 * @param to   The ending index (exclusive) for setting new values.
	 * @param B    The matrix object containing the new values.
	 * @return The current matrix after setting the new values.
	 * @throws IllegalArgumentException if the indices are not valid or the array
	 *                                  size is not correct.
	 */
	public Matrix<T> set(int from, int to, Matrix<T> B) {
		// Check for valid indices and size
		if (from < 0 || to > array.length || from > to || B.getNrows() * B.getNcols() != to - from) {
			throw new IllegalArgumentException("Invalid indices or incorrect matrix size.");
		}

		// Copy values from matrix B to the specified range in the current matrix
		for (int i = from, k = 0; i < to; i++, k++) {
			int row = i / ncols;
			int col = i % ncols;
			set(row, col, B.get(k));
		}

		return this;
	}

	/**
	 * Sets a range of elements in the matrix to the specified value.
	 * <p>
	 * This method updates a rectangular range of elements in the matrix, starting
	 * from the specified row and column indices and ending at the specified row and
	 * column indices, with the specified value. The method checks if the indices
	 * are valid to ensure that the matrix's structure remains consistent. If the
	 * indices are not valid, an {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Integer> matrix = new Matrix<>(3, 3, 0, Integer.class);
	 * matrix.set(0, 2, 0, 2, 5); // Sets the submatrix from row 0 to row 2 and from column 0 to column 2
	 * 							// to the value 5
	 * </pre>
	 * </p>
	 *
	 * @param rowStart The starting index of the row range (inclusive).
	 * @param rowEnd   The ending index of the row range (inclusive).
	 * @param colStart The starting index of the column range (inclusive).
	 * @param colEnd   The ending index of the column range (inclusive).
	 * @param byValue  The value to set in the specified range of elements.
	 * @throws IllegalArgumentException if the provided row or column indices are
	 *                                  invalid.
	 */
	public void set(int rowStart, int rowEnd, int colStart, int colEnd, T byValue) {
		// Check for valid indices
		if (rowStart < 0 || rowEnd >= nrows || colStart < 0 || colEnd >= ncols || rowStart > rowEnd
				|| colStart > colEnd) {
			throw new IllegalArgumentException("Invalid row or column indices");
		}

		// Iterate over the specified range of rows
		for (int i = rowStart; i <= rowEnd; i++) {
			// Iterate over the specified range of columns
			for (int j = colStart; j <= colEnd; j++) {
				// Set each element in the specified range to the specified value
				set(i, j, byValue);
			}
		}
	}

	/**
	 * Sets values in a submatrix of the current matrix, specified by start and end
	 * indices for both rows and columns.
	 * <p>
	 * This method updates a submatrix of the current matrix with the values
	 * provided in a 2D array. The submatrix is defined by the start and end indices
	 * for both rows and columns. The method checks if the indices are valid and if
	 * the dimensions of the values array match the size of the submatrix. If the
	 * indices are invalid or the dimensions do not match, an
	 * {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with values
	 * Double[][] newValues = { { 7.0, 8.0 }, { 9.0, 10.0 } };
	 * matrix.set(1, 2, 3, 4, newValues); // Sets the values from newValues into the submatrix of matrix
	 * </pre>
	 * </p>
	 *
	 * @param rowStart The start index for the row (inclusive).
	 * @param rowEnd   The end index for the row (exclusive).
	 * @param colStart The start index for the column (inclusive).
	 * @param colEnd   The end index for the column (exclusive).
	 * @param values   A 2D array of values to set in the submatrix. The array must
	 *                 have dimensions equal to the size of the submatrix.
	 * @throws IllegalArgumentException If the indices are invalid or the dimensions
	 *                                  of values do not match the submatrix size.
	 */
	public void set(int rowStart, int rowEnd, int colStart, int colEnd, T[][] values) {
		if (rowStart < 0 || rowStart >= nrows || rowEnd < 0 || rowEnd >= nrows || rowStart > rowEnd) {
			throw new IllegalArgumentException("Invalid row indices for submatrix!");
		}
		if (colStart < 0 || colStart >= ncols || colEnd < 0 || colEnd >= ncols || colStart > colEnd) {
			throw new IllegalArgumentException("Invalid column indices for submatrix!");
		}
		if (values.length != rowEnd - rowStart + 1 || values[0].length != colEnd - colStart + 1) {
			throw new IllegalArgumentException("The dimensions of values do not match the submatrix size.");
		}

		for (int i = rowStart; i <= rowEnd; i++) {
			for (int j = colStart; j <= colEnd; j++) {
				if (i >= 0 && i < nrows && j >= 0 && j < ncols) {
					array[i * ncols + j] = values[i - rowStart][j - colStart];
				}
			}
		}
	}

	/**
	 * Sets values in a submatrix of the current matrix, specified by start and end
	 * indices for both rows and columns.
	 * <p>
	 * This method updates a submatrix of the current matrix with the values
	 * provided in another matrix object. The submatrix is defined by the start and
	 * end indices for both rows and columns. The method checks if the indices are
	 * valid and if the dimensions of the values matrix match the size of the
	 * submatrix. If the indices are invalid or the dimensions do not match, an
	 * {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with values
	 * Matrix<Double> subMatrix = new Matrix<>(2, 2);
	 * // Assume subMatrix is initialized with different values
	 * matrix.set(1, 2, 3, 4, subMatrix); // Sets the values from subMatrix into the submatrix of matrix
	 * </pre>
	 * </p>
	 *
	 * @param rowStart The start index for the row (inclusive).
	 * @param rowEnd   The end index for the row (exclusive).
	 * @param colStart The start index for the column (inclusive).
	 * @param colEnd   The end index for the column (exclusive).
	 * @param q        A matrix object of values to set in the submatrix. The array
	 *                 must have dimensions equal to the size of the submatrix.
	 * @throws IllegalArgumentException If the indices are invalid or the dimensions
	 *                                  of values do not match the submatrix size.
	 */
	public void set(int rowStart, int rowEnd, int colStart, int colEnd, Matrix<T> q) {
		set(rowStart, rowEnd, colStart, colEnd, q.to2DArray());
	}

	/**
	 * Generates a random matrix with the specified dimensions and data type.
	 * <p>
	 * This method creates a new matrix with the given number of rows and columns
	 * and populates it with random values based on the specified data type. The
	 * method supports data types such as Byte, Short, Integer, Long, Float, and
	 * Double. Additionally, it allows for the generation of random Float and Double
	 * values multiplied by a factor when the data type is Float and Double,
	 * respectively.
	 * </p>
	 * <p>
	 * Note: The method uses ThreadLocalRandom for random value generation.
	 * </p>
	 *
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * // Generate a random matrix of Doubles with dimensions 2x3
	 * Matrix<Double> doubleMatrix = new Matrix<>();
	 * doubleMatrix = doubleMatrix.generateRandomMatrix(2, 3, null, Double.class);
	 *
	 * // Generate a random matrix of Floats with dimensions 3x3, each value multiplied by 1.5
	 * Matrix<Float> floatMatrix = new Matrix<>();
	 * floatMatrix = floatMatrix.generateRandomMatrix(3, 3, 1.5f, Float.class);
	 * </pre>
	 * </p>
	 * 
	 * @param nrows        The number of rows in the generated matrix.
	 * @param ncols        The number of columns in the generated matrix.
	 * @param floatsFactor A factor to multiply with the generated Float or Double
	 *                     values. If null, the values will be generated without
	 *                     multiplication.
	 * @param clazz        The data type class of the matrix (e.g., Byte.class,
	 *                     Float.class).
	 * @return A randomly generated matrix with the specified dimensions and data
	 *         type.
	 * @throws IndexOutOfBoundsException If the specified row or column dimension is
	 *                                   invalid.
	 * @throws IllegalArgumentException  If the generic type parameter T is null or
	 *                                   an unsupported type
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> generateRandomMatrix(int nrows, int ncols, Class<T> clazz, T floatsFactor) {

		checkDimensionsValidity(nrows, ncols);

		if (clazz == null) {
			throw new IllegalArgumentException("The generic type parameter T cannot be null.");
		}

		Matrix<T> matrix = new Matrix<>(nrows, ncols, clazz);
//          Matrix<T> matrix = new Matrix<>();
//          matrix.setDimensions(nrows, ncols);
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				T value;
				if (clazz == Byte.class) {
					value = (T) Byte.valueOf((byte) random.nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE + 1));
				} else if (clazz == Short.class) {
					value = (T) Short.valueOf((short) random.nextInt(Short.MIN_VALUE, Short.MAX_VALUE + 1));
				} else if (clazz == Integer.class) {
					value = (T) Integer.valueOf(random.nextInt());
				} else if (clazz == Long.class) {
					value = (T) Long.valueOf(random.nextLong());
				} else if (clazz == Float.class) {
					if (floatsFactor == null) {
						value = (T) Float.valueOf(random.nextFloat());
					} else {
						value = (T) mult((T) Float.valueOf(random.nextFloat()), floatsFactor);
					}
				} else if (clazz == Double.class) {
					if (floatsFactor == null) {
						value = (T) Double.valueOf(random.nextDouble());
					} else {
						value = (T) mult((T) Double.valueOf(random.nextDouble()), floatsFactor);
					}
				} else if (clazz == BigDecimal.class) {
					if (floatsFactor == null) {
						value = (T) BigDecimal.valueOf(random.nextDouble());
					} else {
						BigDecimal randomBigDecimal = BigDecimal.valueOf(random.nextDouble());
						value = (T) mult((T) randomBigDecimal, floatsFactor);
					}
				} else {
					throw new IllegalArgumentException(
							"Unsupported type for random value generation: " + clazz.getName());
				}

				matrix.set(i, j, (T) value);
			}
		}
		return matrix;
	}

	/**
	 * Generates a matrix filled with random values.
	 * <p>
	 * This method creates a new matrix of the specified dimensions and fills it
	 * with random values. The type of the values is determined by the generic type
	 * parameter T, which should extend Number to accommodate various numeric types.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = Matrix<>();
	 * matrix.generateRandomMatrix(3, 3);// won't work
	 * matrix = matrix.generateRandomMatrix(3, 3, Double.class);// works fine
	 * // matrix is now filled with random Double values
	 * </pre>
	 * </p>
	 *
	 * @param nrows Number of rows for the new matrix.
	 * @param ncols Number of columns for the new matrix.
	 * @return A new matrix filled with random values.
	 */
	public Matrix<T> generateRandomMatrix(int nrows, int ncols, Class<T> clazz) {
		return this.generateRandomMatrix(nrows, ncols, clazz, null);
	}

	/**
	 * Generates a matrix filled with random Integer values.
	 * <p>
	 * This method creates a new matrix of the specified dimensions and fills it
	 * with random values of type Integer. It is a alias of
	 * {@link #generateRandomMatrix(int, int, Number, Class)}
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * NB : Always specify the type of the matrix elements !
	 * // Example 1
	 * Matrix<Integer> matrix = Matrix<>(Integer.class);
	 * matrix.randomIntegerMatrix(3, 3, 5);// works fine
	 * matrix = matrix.randomIntegerMatrix(3, 3);// works fine
	 * // matrix is now filled with random Integer values
	 * 
	 * // Example 2
	 * Matrix randomIntegermat = new Matrix<>(Integer.class);
	 * randomIntegermat.randomIntegerMatrix(4, 5, null).print(); // works fine
	 * 
	 * // Example 3
	 * Matrix randomIntegermat = new Matrix(Integer.class);
	 * randomIntegermat.randomIntegerMatrix(4, 5, null).print(); // works fine
	 * 
	 * // Example 4
	 * Matrix<Integer> randomIntegermat = new Matrix<>();
	 * randomIntegermat.setClazz(Integer.class);
	 * randomIntegermat.randomIntegerMatrix(4, 5, null).print(); // works fine
	 * </pre>
	 * </p>
	 *
	 * @param nrows  Number of rows for the new matrix.
	 * @param ncols  Number of columns for the new matrix.
	 * @param factor A factor to multiply with the generated Integer values. If
	 *               null, the values will be generated without multiplication.
	 * @return A new matrix filled with random values.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<Integer> randomIntegerMatrix(int nrows, int ncols, Integer factor) {
		checkDimensionsValidity(nrows, ncols);

		if (clazz == null || clazz.equals(Float.class) || clazz.equals(Double.class)) {
			throw new IllegalArgumentException(
					"The generic type parameter T must be Integer.class and it cannot be null.");
		}

		Matrix<Integer> matrix = new Matrix<>(Integer.class);
		matrix.setDimensions(nrows, ncols);

		ThreadLocalRandom random = ThreadLocalRandom.current();
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				T value;
				if (factor == null) {
					value = (T) Integer.valueOf(random.nextInt());
				} else {
					value = (T) mult((T) Integer.valueOf(random.nextInt()), (T) factor);
				}

				matrix.set(i, j, (Integer) value);
			}
		}
		return matrix;
	}

	/**
	 * Generates a matrix filled with random Float values.
	 * <p>
	 * This method creates a new matrix of the specified dimensions and fills it
	 * with random values of type Float. It is a alias of
	 * {@link #generateRandomMatrix(int, int, Number, Class)}
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * NB : Always specify the type of the matrix elements !
	 * // Example 1
	 * Matrix<Float> matrix = Matrix<>(Float.class);
	 * matrix.randomFloatMatrix(3, 3, 5);// works fine
	 * matrix = matrix.randomFloatMatrix(3, 3);// works fine
	 * // matrix is now filled with random Float values
	 * 
	 * // Example 2
	 * Matrix randomfloatsmat = new Matrix<>(Float.class);
	 * randomfloatsmat.randomFloatMatrix(4, 5, null).print(); // works fine
	 * 
	 * // Example 3
	 * Matrix randomfloatsmat = new Matrix(Float.class);
	 * randomfloatsmat.randomFloatMatrix(4, 5, null).print(); // works fine
	 * 
	 * // Example 4
	 * Matrix<Float> randomfloatsmat = new Matrix<>();
	 * randomfloatsmat.setClazz(Float.class);
	 * randomfloatsmat.randomFloatMatrix(4, 5, null).print(); // works fine
	 * </pre>
	 * </p>
	 *
	 * @param nrows         Number of rows for the new matrix.
	 * @param ncols         Number of columns for the new matrix.
	 * @param floats_factor A factor to multiply with the generated Float or Double
	 *                      values. If null, the values will be generated without
	 *                      multiplication.
	 * @return A new matrix filled with random values.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<Float> randomFloatMatrix(int nrows, int ncols, Float floats_factor) {
		checkDimensionsValidity(nrows, ncols);

		if (clazz == null || !clazz.equals(Float.class)) {
			throw new IllegalArgumentException(
					"The generic type parameter T must be Float.class and it cannot be null.");
		}

		Matrix<Float> matrix = new Matrix<>(Float.class);
		matrix.setDimensions(nrows, ncols);

		ThreadLocalRandom random = ThreadLocalRandom.current();
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				T value;
				if (floats_factor == null) {
					value = (T) Float.valueOf(random.nextFloat());
				} else {
					value = (T) mult((T) Float.valueOf(random.nextFloat()), (T) floats_factor);
				}

				matrix.set(i, j, (Float) value);
			}
		}
		return matrix;
	}

	/**
	 * Generates a matrix filled with random Double values.
	 * <p>
	 * This method creates a new matrix of the specified dimensions and fills it
	 * with random values of type Double. It is a alias of
	 * {@link #generateRandomMatrix(int, int, Number, Class)}
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * NB : Always specify the type of the matrix elements !
	 * // Example 1
	 * Matrix<Double> matrix = Matrix<>(Double.class);
	 * matrix.randomDoubleMatrix(3, 3, 5);// works fine
	 * matrix = matrix.randomDoubleMatrix(3, 3);// works fine
	 * // matrix is now filled with random Double values
	 * 
	 * // Example 2
	 * Matrix randomDoublemat = new Matrix<>(Double.class);
	 * randomDoublemat.randomDoubleMatrix(4, 5, null).print(); // works fine
	 * 
	 * // Example 3
	 * Matrix randomDoublemat = new Matrix(Double.class);
	 * randomDoublemat.randomDoubleMatrix(4, 5, null).print(); // works fine
	 * 
	 * // Example 4
	 * Matrix<Double> randomDoublemat = new Matrix<>();
	 * randomDoublemat.setClazz(Double.class);
	 * randomDoublemat.randomDoubleMatrix(4, 5, null).print(); // works fine
	 * </pre>
	 * </p>
	 *
	 * @param nrows         Number of rows for the new matrix.
	 * @param ncols         Number of columns for the new matrix.
	 * @param floats_factor A factor to multiply with the generated Float or Double
	 *                      values. If null, the values will be generated without
	 *                      multiplication.
	 * @return A new matrix filled with random values.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> randomDoubleMatrix(int nrows, int ncols, Double floats_factor) {
		checkDimensionsValidity(nrows, ncols);

		if (clazz == null || !clazz.equals(Double.class)) {
			throw new IllegalArgumentException(
					"The generic type parameter T must be Double.class and it cannot be null.");
		}

		Matrix<T> matrix = new Matrix<>();
		matrix.setDimensions(nrows, ncols);
		matrix.setClazz(clazz);

		ThreadLocalRandom random = ThreadLocalRandom.current();
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				T value;
				if (floats_factor == null) {
					value = (T) Double.valueOf(random.nextDouble());
				} else {
					value = (T) mult((T) Double.valueOf(random.nextDouble()), (T) floats_factor);
				}

				matrix.set(i, j, (T) value);
			}
		}
		return matrix;
	}

	/**
	 * Generates a binary matrix filled with integers 0s and 1s.
	 *
	 * <p>
	 * This method creates a new matrix of the specified dimensions and fills it
	 * with binary values (0s and 1s) generated randomly. The type of the values is
	 * determined by the generic type parameter T, which is expected to be a
	 * subclass of Number, precisely Byte, Short, Integer and Long. This method is
	 * particularly useful for generating matrices for binary operations or matrices
	 * with binary values, such as adjacency matrices or matrices used in boolean
	 * logic operations.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Integer> binaryMatrix = new Matrix<>();
	 * binaryMatrix = binaryMatrix.generateBinaryMatrix(3, 3, Integer.class);
	 * binaryMatrix.print();
	 * // binaryMatrix is now filled with random 0s and 1s
	 * </pre>
	 * </p>
	 *
	 * @param nrows Number of rows for the new binary matrix.
	 * @param ncols Number of columns for the new binary matrix.
	 * @return A new matrix filled with binary values (0s and 1s).
	 */
	public Matrix<T> randomBinaryMatrix(int nrows, int ncols, Class<T> clazz) {
		if (clazz == null || clazz.equals(Double.class) || clazz.equals(Float.class)) {
			throw new IllegalArgumentException(
					"The type of matrix elemens should be Byte, Short, Integer or Long only.");
		}
		Matrix<T> matrix = new Matrix<>(nrows, ncols, clazz);
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				// Generate a random binary value (0 or 1)
				int binaryValue = random.nextInt(2);

				// Convert the binary value to the generic type T
				T elementValue = convertToGenericType(binaryValue, clazz);

				matrix.set(i, j, elementValue);
			}
		}
		return matrix;
	}

	/*****************************************
	 * ARRAYS MANIPULATION METHODS
	 ***************************************/

	/**
	 * Converts the internal 1D array to a 2D array.
	 * <p>
	 * This method transforms the internal 1D array representation of the matrix
	 * into a 2D array, where each row of the 2D array corresponds to a row in the
	 * matrix. This conversion is useful for operations that require a 2D view of
	 * the matrix data, such as iterating over rows and columns directly.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<Double>(3, 3);
	 * // Assume matrix is initialized with values
	 * T[][] matrix2D = matrix.to2DArray();
	 * // matrix2D now represents the matrix in a 2D format
	 * </pre>
	 * </p>
	 *
	 * @return The internal array as a 2D array.
	 */
	@SuppressWarnings("unchecked")
	public T[][] to2DArray() {
		T[] array1d = this.getArray();
		T[][] array2d = (T[][]) Array.newInstance(clazz, nrows, ncols);

		for (int i = 0; i < nrows; i++) {
			System.arraycopy(array1d, i * ncols, array2d[i], 0, ncols);
		}

		return array2d;
	}

	/**
	 * Converts the internal 2D array to a 1D array.
	 *
	 * <p>
	 * This method transforms the internal 2D array representation of the matrix
	 * into a 1D array, effectively flattening the matrix structure. This conversion
	 * is useful for operations that require a linear view of the matrix data, such
	 * as certain mathematical operations or when interfacing with APIs that expect
	 * 1D arrays.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Float> matrix = new Matrix<Float>(3, 3);
	 * // Assume matrix is initialized with values
	 * T[] matrix1D = matrix.to1DArray();
	 * // matrix1D now represents the matrix in a 1D format
	 * </pre>
	 * </p>
	 *
	 * @return A defensive copy of the internal array as a 1D array.
	 */
	@SuppressWarnings("unchecked")
	public T[] to1DArray() {
		T[] array1d = (T[]) Array.newInstance(clazz, nrows * ncols);

		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				array1d[i * ncols + j] = array[i * ncols + j];
			}
		}

		return array1d;
	}

//  public T[] to1DArray() {
//      if (array == null) {
//          throw new IllegalStateException("Array is null.");
//      }
//      return Arrays.copyOf(array, array.length);
//  }

	/**
	 * Flattens the matrix into a 1D array.
	 *
	 * <p>
	 * This method returns the matrix's elements in a single array, effectively
	 * converting the matrix into a linearized form.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Double[] flattened = matrix.flatten(); // Flattens the matrix into a 1D array
	 * </pre>
	 * </p>
	 *
	 * @return A defensive copy of the flattened 1D array.
	 */
	public T[] flatten() {
		// Defensive copy to prevent modification of the internal array
		return to1DArray();
	}

	/**
	 * Clones (or makes a copy of) the current matrix.
	 * <p>
	 * This method creates a new matrix that is a deep copy of the current matrix. A
	 * deep copy means that all elements of the matrix are copied, and the new
	 * matrix is independent of the original matrix. This is useful for scenarios
	 * where you need to modify the matrix without affecting the original data.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Integer> matrix = new Matrix<Integer>(3, 3);
	 * // Assume matrix is initialized with values
	 * Matrix<Integer> matrixCopy = matrix.clone();
	 * // matrixCopy is now a separate copy of the original matrix
	 * </pre>
	 * </p>
	 *
	 * @return A new matrix that is a clone of the current matrix.
	 */
	public Matrix<T> clone() {
		// Convert the current matrix to a 1D array
		T[] array1D = this.to1DArray();

		// Create a new matrix with the same dimensions and array
		Matrix<T> clonedMatrix = new Matrix<>(this.nrows, this.ncols, this.clazz);

		// Use Arrays.copyOf for array copying
		clonedMatrix.array = Arrays.copyOf(array1D, array1D.length);

		return clonedMatrix;
	}

	/**
	 * Extracts a range of rows from the matrix.
	 * <p>
	 * This method creates a new matrix that contains a specified range of rows from
	 * the original matrix. The range is defined by the starting and ending indices,
	 * where both indices are inclusive. If the indices are not valid, an
	 * {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> extractedRows = matrix.extractRowsRange(1, 3);
	 * // extractedRows now contains the rows from index 1 to index 3 of the
	 * // original matrix for indices are all inclusive.
	 * </pre>
	 * </p>
	 *
	 * @param from The starting index (inclusive) of the row range to extract.
	 * @param to   The ending index (inclusive) of the row range to extract.
	 * @return A new matrix that contains the specified range of rows.
	 * @throws IllegalArgumentException if the indices are not valid.
	 */
	public Matrix<T> extractRowsRange(int from, int to) {
		if (from < 0 || to >= this.nrows || from > to) {
			throw new IllegalArgumentException("Invalid row indices!");
		}

		int newNrows = to - from + 1; // Adjusted to include the 'to' index
		if (newNrows == 0) {
			// Return an empty matrix if from and to are equal
			return new Matrix<>(0, this.ncols, this.clazz);
		}

		T[] newArray = Arrays.copyOfRange(this.array, from * this.ncols, (to + 1) * this.ncols); // Adjusted to include
																									// the 'to' index

		return new Matrix<>(newNrows, this.ncols, newArray, this.clazz);
	}

	/**
	 * Extracts a range of columns from the matrix.
	 * <p>
	 * This method creates a new matrix that contains a specified range of columns
	 * from the original matrix. The range is defined by the starting and ending
	 * indices, where both indices are inclusive. If the indices are not valid, an
	 * {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> extractedColumns = matrix.extractColumnsRange(1, 3);
	 * // extractedColumns now contains the columns from index 1 to index 2 of
	 * // the original matrix
	 * </pre>
	 * </p>
	 *
	 * @param from The starting index (inclusive) of the column range to extract.
	 * @param to   The ending index (inclusive) of the column range to extract.
	 * @return A new matrix that contains the specified range of columns.
	 * @throws IllegalArgumentException if the indices are not valid.
	 */
	public Matrix<T> extractColumnsRange(int from, int to) {
		Objects.requireNonNull(this, "Matrix object cannot be null");
		if (from < 0 || from >= this.ncols || to < 0 || to >= this.ncols || from > to) {
			throw new IllegalArgumentException("Invalid column indices: from = " + from + ", to = " + to
					+ ". Ensure that 0 <= from <= to <= ncols.");
		}

		int newNcols = to - from + 1; // Adjusted to include the 'to' index
		Matrix<T> result = new Matrix<>(this.nrows, newNcols, this.clazz);

		for (int i = 0; i < this.nrows; i++) {
			for (int j = from; j <= to; j++) { // Adjusted to include the 'to' index
				result.set(i, j - from, this.get(i, j));
			}
		}

		return result;
	}

	/**
	 * Extracts a specific row from the matrix.
	 * <p>
	 * This method creates a new matrix that contains a single row from the original
	 * matrix. The row is specified by its index. If the row index is not valid, an
	 * {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> extractedRow = matrix.extractRow(1);
	 * // extractedRow now contains the row at index 1 of the original matrix
	 * </pre>
	 * </p>
	 *
	 * @param i The index of the row to extract.
	 * @return A new matrix that is a single row of the original matrix.
	 * @throws IllegalArgumentException if the row index is not valid.
	 */
	public Matrix<T> extractRow(int i) {
		// Check if the input index is within the valid range
		if (i < 0 || i >= this.nrows) {
			throw new IllegalArgumentException("Invalid row index: i = " + i + ". Ensure that 0 <= i < nrows.");
		}

		// Create a new array for the extracted row using Arrays.copyOfRange()
		T[] newRow = Arrays.copyOfRange(this.array, i * this.ncols, (i + 1) * this.ncols);

		// Return a new matrix with the extracted row
		return new Matrix<>(1, this.ncols, newRow, this.clazz);
	}

	/**
	 * Returns the specified row of the matrix as a new matrix.
	 * <p>
	 * This method extracts a single row from the original matrix and returns it as
	 * a new matrix. The row is specified by its index. If the row index is not
	 * valid, an {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> rowMatrix = matrix.getRow(1);
	 * // rowMatrix now contains the row at index 1 of the original matrix
	 * </pre>
	 * </p>
	 *
	 * @param row The index of the row to return.
	 * @return The specified row as a new matrix.
	 * @throws IllegalArgumentException if the row index is not valid.
	 * @see extractRow(int i)
	 */
	public Matrix<T> getRow(int row) {
		// Check if the input index is within the valid range
		if (row < 0 || row >= this.nrows) {
			throw new IllegalArgumentException("Invalid row index: row = " + row + ". Ensure that 0 <= row < nrows.");
		}

		T[] newRow = Arrays.copyOfRange(this.array, row * this.ncols, (row + 1) * this.ncols);

		return new Matrix<>(1, this.ncols, newRow, this.clazz);
	}

	/**
	 * Sets the specified row of the matrix to the given row.
	 * <p>
	 * This method replaces a row in the matrix with a new row provided as a matrix.
	 * The row to be replaced is specified by its index, and the new row must be a
	 * matrix with the same number of columns as the original matrix. If the new row
	 * matrix does not have the correct dimensions, an
	 * {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> newRowMatrix = new Matrix<>(1, 3);
	 * // Initialize newRowMatrix with new values
	 * matrix.setRow(1, newRowMatrix);
	 * // The row at index 1 of matrix is now replaced with newRowMatrix
	 * </pre>
	 * </p>
	 *
	 * @param row The index of the row to set.
	 * @param X   The new row matrix.
	 * @throws IllegalArgumentException if the new row matrix dimensions are not
	 *                                  valid.
	 */
	public void setRow(int row, Matrix<T> X) {
		if (row < 0 || row >= nrows) {
			throw new IllegalArgumentException("Invalid row index.");
		}
		if (X.getNrows() != 1 || X.getNcols() != ncols) {
			throw new IllegalArgumentException("Invalid row matrix dimensions.");
		}
		for (int i = 0; i < ncols; i++) {
			set(row, i, X.get(0, i));
		}
	}

	/**
	 * Extracts a specific column from the matrix.
	 * <p>
	 * This method creates a new matrix that contains a single column from the
	 * original matrix. The column is specified by its index. If the column index is
	 * not valid, an {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> columnMatrix = matrix.extractCol(1);
	 * // columnMatrix now contains the column at index 1 of the original matrix
	 * </pre>
	 * </p>
	 *
	 * @param i The index of the column to extract.
	 * @return A new matrix that is a single column of the original matrix.
	 * @throws IllegalArgumentException if the column index is not valid.
	 * @see getColumn(int col)
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> extractCol(int i) {
		if (i < 0 || i >= ncols) {
			throw new IllegalArgumentException("Invalid column index.");
		}

		T[] newCol = (T[]) Array.newInstance(clazz, nrows);

		for (int j = 0; j < nrows; j++) {
			newCol[j] = this.array[j * ncols + i];
		}

		return new Matrix<>(nrows, 1, newCol, this.clazz);
	}

	/**
	 * Returns the specified column of the matrix as a new matrix.
	 * <p>
	 * This method extracts a single column from the original matrix and returns it
	 * as a new matrix. The column is specified by its index. If the column index is
	 * not valid, an {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> columnMatrix = matrix.getColumn(1);
	 * // columnMatrix now contains the column at index 1 of the original matrix
	 * </pre>
	 * </p>
	 *
	 * @param col The index of the column to return.
	 * @return The specified column as a new matrix.
	 * @throws IllegalArgumentException if the column index is not valid.
	 * @see extractCol(int i)
	 */
	public Matrix<T> getColumn(int col) {
		// Check if the input index is within the valid range
		if (col < 0 || col >= this.ncols) {
			throw new IllegalArgumentException(
					"Invalid column index: col = " + col + ". Ensure that 0 <= col < ncols.");
		}

		@SuppressWarnings("unchecked")
		T[] newCol = (T[]) Array.newInstance(clazz, nrows);

		for (int j = 0; j < nrows; j++) {
			newCol[j] = this.array[j * ncols + col];
		}

		return new Matrix<>(this.nrows, 1, newCol, this.clazz);
	}

	/**
	 * Sets the specified column of the matrix to the given column.
	 * <p>
	 * This method replaces a column in the matrix with a new column provided as a
	 * matrix. The column to be replaced is specified by its index, and the new
	 * column must be a matrix with the same number of rows as the original matrix.
	 * If the new column matrix does not have the correct dimensions, an
	 * {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> newColumnMatrix = new Matrix<>(3, 1);
	 * // Initialize newColumnMatrix with new values
	 * matrix.setColumn(1, newColumnMatrix);
	 * // The column at index 1 of matrix is now replaced with newColumnMatrix
	 * </pre>
	 * </p>
	 *
	 * @param col The index of the column to set.
	 * @param X   The new column matrix.
	 * @throws IllegalArgumentException if the new column matrix dimensions are not
	 *                                  valid.
	 */
	public void setColumn(int col, Matrix<T> X) {
		if (X.getNrows() != nrows || X.getNcols() != 1) {
			throw new IllegalArgumentException("Invalid column Matrix dimensions.");
		}
		if (col < 0 || col >= ncols) {
			throw new IllegalArgumentException("Invalid column index.");
		}
		for (int i = 0; i < nrows; i++) {
			set(i, col, X.get(i, 0));
		}
	}

	/**
	 * Retrieves a column vector from the specified position in the matrix.
	 *
	 * @param row The row index of the vector (ignored for column vectors).
	 * @param col The column index of the vector.
	 * @return The column vector as an array of type T.
	 * @throws IndexOutOfBoundsException If the row or column index is out of
	 *                                   bounds.
	 */
	@SuppressWarnings("unchecked")
	public T[] getColumnVector(int row, int col) {
		if (row < 0 || row >= nrows || col < 0 || col >= ncols) {
			throw new IndexOutOfBoundsException("Invalid row or column index");
		}

		T[] columnVector = (T[]) Array.newInstance(clazz, nrows);
		for (int i = 0; i < nrows; i++) {
			columnVector[i] = array[i * ncols + col];
		}
		return columnVector;
	}

	/**
	 * Sets a column vector with the provided data at the specified position in the
	 * matrix.
	 *
	 * @param col    The column index of the vector.
	 * @param vector The array containing the column vector data.
	 * @throws IllegalArgumentException If the column index is invalid or the vector
	 *                                  length is not equal to the number of rows.
	 */
	public void setColumnVector(int col, T[] vector) {
		if (col < 0 || col >= ncols || vector.length != nrows) {
			throw new IllegalArgumentException("Invalid column index or vector length");
		}

		for (int i = 0; i < nrows; i++) {
			array[i * ncols + col] = vector[i];
		}
	}

	@SuppressWarnings("unchecked")
	public Matrix<T> getColumnAsVector(int row, int col) {
		if (row < 0 || row >= nrows || col < 0 || col >= ncols) {
			throw new IndexOutOfBoundsException("Invalid row or column index");
		}

		T[] columnVector = (T[]) Array.newInstance(clazz, nrows);
		for (int i = 0; i < nrows; i++) {
			columnVector[i] = array[i * ncols + col];
		}
		return new Matrix<>(1, columnVector);
	}

	/**
	 * Sets a column vector with the provided data at the specified position in the
	 * matrix.
	 *
	 * @param col    The column index of the vector.
	 * @param vector The array containing the column vector data.
	 * @throws IllegalArgumentException If the column index is invalid or the vector
	 *                                  length is not equal to the number of rows.
	 */
	public void setColumnAsVector(int col, Matrix<T> columnvector) {
		if (!columnvector.isVector()) {
			throw new IllegalArgumentException("Data to set must be a column vector");
		}
		if (col < 0 || col >= ncols || columnvector.getArray().length != nrows) {
			throw new IllegalArgumentException("Invalid column index or vector length");
		}

		T[] vector = columnvector.getArray();
		for (int i = 0; i < nrows; i++) {
			array[i * ncols + col] = vector[i];
		}
	}

	/**
	 * Extracts a subMatrix from the matrix.
	 * <p>
	 * This method creates a new matrix that contains a submatrix of the original
	 * matrix. The submatrix is defined by the starting and ending indices for both
	 * rows and columns. If the indices are not valid, an
	 * {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> subMatrix = matrix.extract(1, 3, 1, 3);
	 * // subMatrix now contains the submatrix from row 1 to row 2 and column 1
	 * // to column 2 of the original matrix
	 * </pre>
	 * </p>
	 *
	 * @param rowstart The starting row index (inclusive) of the subMatrix to
	 *                 extract.
	 * @param rowend   The ending row index (exclusive) of the subMatrix to extract.
	 * @param colstart The starting column index (inclusive) of the subMatrix to
	 *                 extract.
	 * @param colend   The ending column index (exclusive) of the subMatrix to
	 *                 extract.
	 * @return A new matrix that is a subMatrix of the original matrix.
	 * @throws IllegalArgumentException if the indices are not valid.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> extract(int rowstart, int rowend, int colstart, int colend) {
		if (rowstart < 0 || rowend > this.nrows || rowstart >= rowend || colstart < 0 || colend > this.ncols
				|| colstart >= colend) {
			throw new IllegalArgumentException("Invalid row or column indices!");
		}

		int newNrows = rowend - rowstart;
		int newNcols = colend - colstart;
		T[] newArray = (T[]) Array.newInstance(this.clazz, newNrows * newNcols);

		int i, j;
		for (i = rowstart, j = 0; i < rowend; i++, j++) {
			for (int k = colstart; k < colend; k++) {
				newArray[j * newNcols + (k - colstart)] = this.array[i * this.ncols + k];
			}
		}

		return new Matrix<>(newNrows, newNcols, newArray, this.clazz);
	}

	/**
	 * Returns a subMatrix of the current matrix.
	 * <p>
	 * This method extracts a subMatrix from the current matrix, starting from the
	 * first row and ending at the specified row. The subMatrix will have the same
	 * number of columns as the original matrix.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> subMatrix = matrix.getSubMatrix(2); // Extracts a 3x3 subMatrix ending at row 2
	 * </pre>
	 * </p>
	 *
	 * @param endRow The row index where the subMatrix ends.
	 * @return The subMatrix.
	 * @throws IllegalArgumentException if the endRow index is not valid.
	 */
	public Matrix<T> getSubMatrix(int endRow) {
		if (endRow < 0 || endRow >= nrows) {
			throw new IllegalArgumentException("End row index must be between 0 and " + (nrows - 1));
		}

		Matrix<T> subMatrix = new Matrix<>(endRow + 1, ncols, this.clazz);
		for (int i = 0; i <= endRow; i++) {
			for (int j = 0; j < ncols; j++) {
				subMatrix.set(i, j, get(i, j));
			}
		}

		return subMatrix;
	}

	/**
	 * Returns a submatrix of this matrix, excluding the specified row and column.
	 * <p>
	 * This method creates a new matrix by excluding the specified row and column
	 * from the original matrix. The resulting submatrix will have dimensions one
	 * less in both rows and columns than the original matrix.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> subMatrix = matrix.getSubMatrix(1, 2); // Excludes row 1 and column 2
	 * </pre>
	 * </p>
	 *
	 * @param i the index of the row to exclude
	 * @param j the index of the column to exclude
	 * @return the submatrix of this matrix, excluding the specified row and column
	 */
	public Matrix<T> getSubMatrix(int i, int j) {
		// Create a new matrix with one less row and one less column
		Matrix<T> subMatrix = new Matrix<>(nrows - 1, ncols - 1, this.clazz);

		// Copy the elements from this matrix to the submatrix, excluding the specified
		// row and column
		int rowIndex = 0;
		for (int k = 0; k < nrows; k++) {
			if (k == i) {
				// Skip the specified row
				continue;
			}
			int colIndex = 0;
			for (int l = 0; l < ncols; l++) {
				if (l == j) {
					// Skip the specified column
					continue;
				}
				subMatrix.set(rowIndex, colIndex, get(k, l));
				colIndex++;
			}
			rowIndex++;
		}

		return subMatrix;
	}

	/**
	 * Extracts a subMatrix from the original matrix, using specified start and end
	 * indices for rows and columns.
	 * <p>
	 * This method allows for the extraction of a subMatrix from the original matrix
	 * by specifying the start and end indices for both rows and columns. The
	 * indices are 1-based, meaning the first row or column is indexed as 1.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> subMatrix = matrix.getSubMatrix(1, 3, 1, 3); // Extracts a 2x2 subMatrix from the 1st to 3rd row
	 * 															// and column
	 * </pre>
	 * </p>
	 *
	 * @param rowstart The start index for the row (inclusive).
	 * @param rowend   The end index for the row (exclusive).
	 * @param colstart The start index for the column (inclusive).
	 * @param colend   The end index for the column (exclusive).
	 * @return A new matrix object that is a subMatrix of the current matrix.
	 * @throws IllegalArgumentException if the indices are invalid.
	 */
	public Matrix<T> getSubMatrix(int rowstart, int rowend, int colstart, int colend) {
		// Check if the indices are valid
		if (rowstart < 1 || rowend > nrows || colstart < 1 || colend > ncols || rowstart > rowend
				|| colstart > colend) {
			throw new IllegalArgumentException("Invalid indices for subMatrix!");
		}

		// Create a new Matrix to hold the subMatrix
		Matrix<T> subMatrix = new Matrix<>(rowend - rowstart + 1, colend - colstart + 1, this.clazz);

		// Extract the subMatrix
		for (int i = rowstart; i <= rowend; i++) {
			for (int j = colstart; j <= colend; j++) {
				T value = this.array[(i - 1) * ncols + (j - 1)];
				subMatrix.array[(i - rowstart) * subMatrix.ncols + (j - colstart)] = value;
			}
		}

		return subMatrix;
	}

	/**
	 * Sets a submatrix with the provided data at the specified position in the
	 * matrix.
	 *
	 * @param startRow The starting row index of the submatrix.
	 * @param startCol The starting column index of the submatrix.
	 * @param data     The 2D array containing the submatrix data.
	 * @throws IllegalArgumentException If the start index is invalid or the data is
	 *                                  empty.
	 * @throws IllegalArgumentException If the submatrix dimensions exceed the
	 *                                  matrix dimensions.
	 */
	public void setSubMatrix(int startRow, int startCol, T[][] data) {
		if (startRow < 0 || startRow >= nrows || startCol < 0 || startCol >= ncols || data.length == 0
				|| data[0].length == 0) {
			throw new IllegalArgumentException("Invalid start index or data");
		}

		int rows = data.length;
		int cols = data[0].length;

		if (startRow + rows > nrows || startCol + cols > ncols) {
			throw new IllegalArgumentException("Submatrix dimensions exceed matrix dimensions");
		}

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				array[(startRow + i) * ncols + (startCol + j)] = data[i][j];
			}
		}
	}

	/**
	 * Transforms the matrix into a row vector.
	 * <p>
	 * This method converts the matrix into a row vector by creating a new matrix
	 * with a single row and the same number of columns as the original matrix. This
	 * operation is useful for flattening the matrix into a single row, which can be
	 * useful for certain matrix operations or when interfacing with APIs that
	 * expect a single row vector.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> rowVector = matrix.transformToRowVector();
	 * // rowVector now represents the matrix as a single row vector
	 * </pre>
	 * </p>
	 *
	 * @return The matrix transformed into a row vector.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> transformToRowVector() {
		// Cast the array to the generic type
		T[] x = (T[]) getArray();
		// Create a new 2D array with one row
		T[][] C = (T[][]) Array.newInstance(clazz, 1, x.length);
		// Copy the elements from the 1D array to the 2D array
		System.arraycopy(x, 0, C[0], 0, x.length);
		// Return the new Matrix
		return new Matrix<>(C, this.clazz);
	}

	/**
	 * Transforms the matrix into a column vector.
	 * <p>
	 * This method converts the matrix into a column vector by creating a new matrix
	 * with a single column and the same number of rows as the original matrix. This
	 * operation is useful for flattening the matrix into a single column, which can
	 * be useful for certain matrix operations or when interfacing with APIs that
	 * expect a single column vector.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> columnVector = matrix.transformToColumnVector();
	 * // columnVector now represents the matrix as a single column vector
	 * </pre>
	 * </p>
	 *
	 * @return The matrix transformed into a column vector.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> transformToColumnVector() {
		// Cast the array to the generic type
		T[] x = (T[]) getArray();
		// Create a new 2D array with one column
		T[][] C = (T[][]) Array.newInstance(clazz, x.length, 1);
		// Copy the elements from the 1D array to the 2D array
		for (int i = 0; i < x.length; i++) {
			C[i][0] = x[i];
		}
		// Return the new Matrix
		return new Matrix<>(C, this.clazz);
	}

	/**
	 * Concatenates multiple matrices vertically.
	 * <p>
	 * This method concatenates the current matrix with one or more additional
	 * matrices vertically. The operation requires that all matrices have the same
	 * number of rows. The resulting matrix will have the same number of rows as the
	 * original matrix but its number of columns will be the sum of the columns of
	 * all matrices.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * // Assume matrix1 is initialized with Double values
	 * Matrix<Double> matrix2 = new Matrix<>(2, 3);
	 * // Assume matrix2 is initialized with Double values
	 * Matrix<Double> concatenated = matrix1.concatenateVertically(matrix2);
	 * // concatenated now contains the concatenation of matrix1 and matrix2 vertically
	 * </pre>
	 * </p>
	 *
	 * @param matrices Varargs parameter representing the matrices to concatenate.
	 * @return The concatenated matrix.
	 * @throws IllegalArgumentException if the matrices do not have the same number
	 *                                  of rows.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> concatenateVertically(Matrix<T>... matrices) {
		// Check if the matrices array is not empty
		if (matrices.length == 0) {
			throw new IllegalArgumentException("At least one matrix must be provided for concatenation.");
		}

		// Check if all matrices have the same number of columns
		int commonColumns = this.ncols;
		for (Matrix<T> matrix : matrices) {
			if (matrix.ncols != commonColumns) {
				throw new IllegalArgumentException("All matrices must have the same number of columns.");
			}
		}

		// Calculate the total number of rows
		int totalRows = this.nrows;
		for (Matrix<T> matrix : matrices) {
			totalRows += matrix.nrows;
		}

		// Create a new array to hold the concatenated matrix data
		T[] concatenatedArray = (T[]) Array.newInstance(this.clazz, totalRows * commonColumns);

		// Copy the data from the original matrix
		System.arraycopy(this.array, 0, concatenatedArray, 0, this.array.length);

		// Copy the data from the additional matrices
		int offset = this.array.length;
		for (Matrix<T> matrix : matrices) {
			System.arraycopy(matrix.array, 0, concatenatedArray, offset, matrix.array.length);
			offset += matrix.array.length;
		}

		// Create and return the new concatenated matrix
		return new Matrix<>(totalRows, commonColumns, concatenatedArray, this.clazz);
	}

	/**
	 * Concatenates multiple matrices horizontally.
	 * <p>
	 * This method concatenates the current matrix with one or more additional
	 * matrices horizontally. The operation requires that all matrices have the same
	 * number of columns. The resulting matrix will have the same number of columns
	 * as the original matrix but its number of rows will be the sum of the rows of
	 * all matrices.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * // Assume matrix1 is initialized with Double values
	 * Matrix<Double> matrix2 = new Matrix<>(3, 2);
	 * // Assume matrix2 is initialized with Double values
	 * Matrix<Double> concatenated = matrix1.concatenateHorizontally(matrix2);
	 * // concatenated now contains the concatenation of matrix1 and matrix2 horizontally
	 * </pre>
	 * </p>
	 *
	 * @param matrices Varargs parameter representing the matrices to concatenate.
	 * @return The concatenated matrix.
	 * @throws IllegalArgumentException if the matrices do not have the same number
	 *                                  of columns.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> concatenateHorizontally(Matrix<T>... matrices) {
		// Check if the matrices array is not empty
		if (matrices.length == 0) {
			throw new IllegalArgumentException("At least one matrix must be provided for concatenation.");
		}

		// Check if all matrices have the same number of rows
		int commonRows = this.nrows;
		for (Matrix<T> matrix : matrices) {
			if (matrix.nrows != commonRows) {
				throw new IllegalArgumentException("All matrices must have the same number of rows.");
			}
		}

		// Calculate the total number of columns
		int totalColumns = this.ncols;
		for (Matrix<T> matrix : matrices) {
			totalColumns += matrix.ncols;
		}

		// Create a new array to hold the concatenated matrix data
		T[] concatenatedArray = (T[]) Array.newInstance(this.clazz, commonRows * totalColumns);

		// Copy the data from the original matrix
		System.arraycopy(this.array, 0, concatenatedArray, 0, this.array.length);

		// Copy the data from the additional matrices
		int offset = this.array.length;
		for (Matrix<T> matrix : matrices) {
			System.arraycopy(matrix.array, 0, concatenatedArray, offset, matrix.array.length);
			offset += matrix.array.length;
		}

		// Create and return the new concatenated matrix
		return new Matrix<>(commonRows, totalColumns, concatenatedArray, this.clazz);
	}

	/**
	 * Concatenates multiple matrices vertically to create a new matrix.
	 *
	 * @param matrices Matrices to be concatenated vertically.
	 * @return A new matrix obtained by concatenating the input matrices vertically.
	 * @throws IllegalArgumentException If the input matrices have different numbers
	 *                                  of columns. @SuppressWarnings("unchecked")
	 *                                  is used to suppress unchecked generic array
	 *                                  creation warning.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> concatenateByRows(Matrix<T>... matrices) {
		return concatenateVertically(matrices);
	}

	/**
	 * Concatenates multiple matrices horizontally to create a new matrix.
	 *
	 * @param matrices Matrices to be concatenated horizontally.
	 * @return A new matrix obtained by concatenating the input matrices
	 *         horizontally.
	 * @throws IllegalArgumentException If the input matrices have different numbers
	 *                                  of rows. @SuppressWarnings("unchecked") is
	 *                                  used to suppress unchecked generic array
	 *                                  creation warning.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> concatenateByColumns(Matrix<T>... matrices) {
		return concatenateHorizontally(matrices);
	}

	/**
	 * Inserts one or more matrices before a specified row index.
	 * <p>
	 * This method inserts the concatenated data of multiple matrices before the
	 * specified row index. The operation requires that all matrices have the same
	 * number of rows.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(2, 3);
	 * // Assume matrix1 is initialized with Double values
	 * Matrix<Double> matrix2 = new Matrix<>(2, 2);
	 * // Assume matrix2 is initialized with Double values
	 * Matrix<Double> result = originalMatrix.insertBeforeRow(1, matrix1, matrix2);
	 * // result now contains the concatenation of matrix1 and matrix2 before row 1
	 * </pre>
	 * </p>
	 *
	 * @param indexRow The index before which the matrices should be inserted.
	 * @param matrices Varargs parameter representing the matrices to concatenate
	 *                 and insert.
	 * @return The matrix with the inserted data.
	 * @throws IllegalArgumentException if the matrices do not have the same number
	 *                                  of rows or if the index is not valid.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> insertRows(int indexRow, Matrix<T>... matrices) {
		// Check if the matrices array is not empty
		if (matrices.length == 0) {
			throw new IllegalArgumentException("At least one matrix must be provided for insertion.");
		}

		// Check if all matrices have the same number of columns as the current matrix
		int commonColumns = this.ncols;
		for (Matrix<T> matrix : matrices) {
			if (matrix.ncols != commonColumns) {
				throw new IllegalArgumentException(
						"All matrices must have the same number of columns as the current matrix.");
			}
		}

		// Check if the index is valid
		if (indexRow < 0 || indexRow > this.nrows) {
			throw new IllegalArgumentException("Invalid row index for insertion.");
		}

		// Calculate the total number of rows for the concatenated matrix
		int totalRows = 0;
		for (Matrix<T> matrix : matrices) {
			totalRows += matrix.nrows;
		}

		// Create a new array to hold the concatenated matrix data
		T[] concatenatedArray = (T[]) Array.newInstance(this.clazz, totalRows * commonColumns);

		// Copy the data from the matrices to be concatenated
		int offset = 0;
		for (Matrix<T> matrix : matrices) {
			System.arraycopy(matrix.array, 0, concatenatedArray, offset, matrix.array.length);
			offset += matrix.array.length;
		}

		// Calculate the total number of rows after insertion
		int newTotalRows = this.nrows + totalRows;

		// Create a new array to hold the data of the matrix after insertion
		T[] newArray = (T[]) Array.newInstance(this.clazz, newTotalRows * commonColumns);

		// Copy the data before the insertion point
		System.arraycopy(this.array, 0, newArray, 0, indexRow * commonColumns);

		// Copy the concatenated matrix data
		System.arraycopy(concatenatedArray, 0, newArray, indexRow * commonColumns, concatenatedArray.length);

		// Copy the data after the insertion point
		System.arraycopy(this.array, indexRow * commonColumns, newArray, (indexRow + totalRows) * commonColumns,
				(this.nrows - indexRow) * commonColumns);

		// Create and return the new matrix with the inserted data
		return new Matrix<>(newTotalRows, commonColumns, newArray, this.clazz);
	}

	/**
	 * Inserts one or more matrices before the first row.
	 * <p>
	 * This method inserts the concatenated data of multiple matrices before the
	 * first row. The operation requires that all matrices have the same number of
	 * rows.
	 * </p>
	 * <p>
	 * Example usage:
	 *
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(2, 3);
	 * // Assume matrix1 is initialized with Double values
	 * Matrix<Double> matrix2 = new Matrix<>(2, 2);
	 * // Assume matrix2 is initialized with Double values
	 * Matrix<Double> result = originalMatrix.insertBeforeFirstRow(matrix1, matrix2);
	 * // result now contains the concatenation of matrix1 and matrix2 before the first row
	 * </pre>
	 * </p>
	 *
	 * @param matricesToInsert Varargs parameter representing the matrices to
	 *                         concatenate and insert.
	 * @return The matrix with the inserted data.
	 * @throws IllegalArgumentException if the matrices do not have the same number
	 *                                  of rows.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> insertBeforeFirstRow(Matrix<T>... matricesToInsert) {
		return insertRows(0, matricesToInsert);
	}

	/**
	 * Inserts one or more matrices before the last row.
	 * <p>
	 * This method inserts the concatenated data of multiple matrices before the
	 * last row. The operation requires that all matrices have the same number of
	 * rows.
	 * </p>
	 * <p>
	 * Example usage:
	 *
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(2, 3);
	 * // Assume matrix1 is initialized with Double values
	 * Matrix<Double> matrix2 = new Matrix<>(2, 2);
	 * // Assume matrix2 is initialized with Double values
	 * Matrix<Double> result = originalMatrix.insertBeforeLastRow(matrix1, matrix2);
	 * // result now contains the concatenation of matrix1 and matrix2 before the last row
	 * </pre>
	 * </p>
	 *
	 * @param matricesToInsert Varargs parameter representing the matrices to
	 *                         concatenate and insert.
	 * @return The matrix with the inserted data.
	 * @throws IllegalArgumentException if the matrices do not have the same number
	 *                                  of rows.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> insertBeforeLastRow(Matrix<T>... matricesToInsert) {
		return insertRows(this.nrows - 1, matricesToInsert);
	}

	/**
	 * Inserts one or more matrices after a specified row index in the current
	 * matrix. The inserted matrices are concatenated horizontally.
	 * <p>
	 * This method inserts the given matrices after the specified row index,
	 * effectively extending the matrix with additional rows. The operation requires
	 * that all matrices have the same number of rows. The resulting matrix will
	 * have the same number of columns as the original matrix but its number of rows
	 * will be increased by the total number of rows in the inserted matrices.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * // Assume matrix1 is initialized with Double values
	 * Matrix<Double> matrix2 = new Matrix<>(2, 3);
	 * // Assume matrix2 is initialized with Double values
	 * Matrix<Double> resultMatrix = matrix1.insertAfterRow(1, matrix2);
	 * // resultMatrix now contains matrix1 followed by matrix2 after row index 1
	 * </pre>
	 * </p>
	 *
	 * @param indexAfterRow The index after which the matrices will be inserted.
	 * @param matrices      Varargs parameter representing the matrices to insert.
	 * @return The matrix with the inserted data.
	 * @throws IllegalArgumentException if the matrices do not have the same number
	 *                                  of rows or if the specified row index is
	 *                                  invalid.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> insertAfterRow(int indexAfterRow, Matrix<T>... matricesToInsert) {
		return insertRows(indexAfterRow + 1, matricesToInsert);
	}

	@SuppressWarnings("unchecked")
	public Matrix<T> insertAfterFirstRow(int indexAfterRow, Matrix<T>... matricesToInsert) {
		return insertRows(1, matricesToInsert);
	}

	@SuppressWarnings("unchecked")
	public Matrix<T> insertAfterLastRow(int indexAfterRow, Matrix<T>... matricesToInsert) {
		return insertRows(this.nrows, matricesToInsert);
	}

	/**
	 * Inserts one or more columns before the specified column index in the current
	 * matrix. The inserted columns are concatenated vertically.
	 * <p>
	 * This method inserts the given matrices before the specified column index,
	 * effectively extending the matrix with additional columns. The operation
	 * requires that all matrices have the same number of rows. The resulting matrix
	 * will have the same number of rows as the original matrix but its number of
	 * columns will be increased by the total number of columns in the inserted
	 * matrices.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * // Assume matrix1 is initialized with Double values
	 * Matrix<Double> matrix2 = new Matrix<>(3, 2);
	 * // Assume matrix2 is initialized with Double values
	 * Matrix<Double> resultMatrix = matrix1.insertColumn(1, matrix2);
	 * // resultMatrix now contains matrix1 followed by matrix2 before column index 1
	 * </pre>
	 * </p>
	 *
	 * @param indexBeforeColumn The index before which the columns will be inserted.
	 * @param matrices          Varargs parameter representing the matrices to
	 *                          insert as columns.
	 * @return The matrix with the inserted columns.
	 * @throws IllegalArgumentException if the matrices do not have the same number
	 *                                  of rows or if the specified column index is
	 *                                  not valid.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> insertColumns(int indexBeforeColumn, Matrix<T>... matrices) {
		// Check if the index is within bounds
		if (indexBeforeColumn < 0 || indexBeforeColumn > this.ncols) {
			throw new IllegalArgumentException("Invalid column index for insertion.");
		}

		// Calculate the total number of columns in the matrices being inserted
		int totalNewCols = 0;
		for (Matrix<T> matrix : matrices) {
			totalNewCols += matrix.ncols;
		}

		// Calculate the new number of columns after insertion
		int newNumCols = this.ncols + totalNewCols;

		// Create a new array to hold the combined data
		T[] newArray = (T[]) Array.newInstance(clazz, this.nrows * newNumCols);

		// Copy columns before the insertion point
		for (int i = 0; i < indexBeforeColumn; i++) {
			Matrix<T> col = this.extractCol(i);
			for (int j = 0; j < this.nrows; j++) {
				newArray[j * newNumCols + i] = col.get(j, 0);
			}
		}

		// Insert the new columns
		int columnIndex = indexBeforeColumn;
		for (Matrix<T> matrix : matrices) {
			for (int j = 0; j < matrix.getNcols(); j++) {
				Matrix<T> col = matrix.extractCol(j);
				for (int k = 0; k < this.nrows; k++) {
					newArray[k * newNumCols + columnIndex] = col.get(k, 0);
				}
				columnIndex++;
			}
		}

		// Copy columns after the insertion point
		for (int i = indexBeforeColumn; i < this.ncols; i++) {
			Matrix<T> col = this.extractCol(i);
			for (int j = 0; j < this.nrows; j++) {
				newArray[j * newNumCols + columnIndex] = col.get(j, 0);
			}
			columnIndex++;
		}

		return new Matrix<>(this.nrows, newNumCols, newArray, this.clazz);
	}

	/**
	 * Inserts one or more columns before the first column in the current matrix.
	 * The inserted columns are concatenated vertically.
	 * <p>
	 * This method inserts the given matrices before the first column of the matrix,
	 * effectively extending the matrix with additional columns. The operation
	 * requires that all matrices have the same number of rows. The resulting matrix
	 * will have the same number of rows as the original matrix but its number of
	 * columns will be increased by the total number of columns in the inserted
	 * matrices.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * // Assume matrix1 is initialized with Double values
	 * Matrix<Double> matrix2 = new Matrix<>(3, 2);
	 * // Assume matrix2 is initialized with Double values
	 * Matrix<Double> resultMatrix = matrix1.insertBeforeFirstColumn(matrix2);
	 * // resultMatrix now contains matrix2 followed by matrix1 before the first column
	 * </pre>
	 * </p>
	 *
	 * @param matrices Varargs parameter representing the matrices to insert as
	 *                 columns.
	 * @return The matrix with the inserted columns.
	 * @throws IllegalArgumentException if the matrices do not have the same number
	 *                                  of rows.
	 * @see #insertColumn(int, Matrix[])
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> insertBeforeFirstColumn(Matrix<T>... matrices) {
		return insertColumns(0, matrices);
	}

	/**
	 * Inserts one or more columns before the last column in the current matrix. The
	 * inserted columns are concatenated vertically.
	 * <p>
	 * This method inserts the given matrices before the last column of the matrix,
	 * effectively extending the matrix with additional columns. The operation
	 * requires that all matrices have the same number of rows. The resulting matrix
	 * will have the same number of rows as the original matrix but its number of
	 * columns will be increased by the total number of columns in the inserted
	 * matrices.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * // Assume matrix1 is initialized with Double values
	 * Matrix<Double> matrix2 = new Matrix<>(3, 2);
	 * // Assume matrix2 is initialized with Double values
	 * Matrix<Double> resultMatrix = matrix1.insertBeforeLastColumn(matrix2);
	 * // resultMatrix now contains matrix1 followed by matrix2 before the last column
	 * </pre>
	 * </p>
	 *
	 * @param matrices Varargs parameter representing the matrices to insert as
	 *                 columns.
	 * @return The matrix with the inserted columns.
	 * @throws IllegalArgumentException if the matrices do not have the same number
	 *                                  of rows.
	 * @see #insertColumn(int, Matrix[])
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> insertBeforeLastColumn(Matrix<T>... matrices) {
		return insertColumns(this.ncols - 1, matrices);
	}

	/**
	 * Inserts one or more columns after a specified column in the current matrix.
	 * The inserted columns are concatenated vertically.
	 * <p>
	 * This method inserts the given matrices after the specified column of the
	 * matrix, effectively extending the matrix with additional columns. The
	 * operation requires that all matrices have the same number of rows. The
	 * resulting matrix will have the same number of rows as the original matrix but
	 * its number of columns will be increased by the total number of columns in the
	 * inserted matrices.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * // Assume matrix1 is initialized with Double values
	 * Matrix<Double> matrix2 = new Matrix<>(3, 2);
	 * // Assume matrix2 is initialized with Double values
	 * Matrix<Double> resultMatrix = matrix1.insertAfterColumn(1, matrix2);
	 * // resultMatrix now contains matrix1 followed by matrix2 after the second column
	 * </pre>
	 * </p>
	 *
	 * @param indexAfterColumn The index of the column after which the matrices
	 *                         should be inserted.
	 * @param matricesToInsert Varargs parameter representing the matrices to insert
	 *                         as columns.
	 * @return The matrix with the inserted columns.
	 * @throws IllegalArgumentException if the indexAfterColumn is invalid or if the
	 *                                  matrices do not have the same number of
	 *                                  rows.
	 * @see #insertColumn(int, Matrix[])
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> insertAfterColumn(int indexAfterColumn, Matrix<T>... matricesToInsert) {
		return insertColumns(indexAfterColumn + 1, matricesToInsert);
	}

	/**
	 * Inserts one or more columns after the first column in the current matrix. The
	 * inserted columns are concatenated vertically.
	 * <p>
	 * This method inserts the given matrices after the first column of the matrix,
	 * effectively extending the matrix with additional columns. The operation
	 * requires that all matrices have the same number of rows. The resulting matrix
	 * will have the same number of rows as the original matrix but its number of
	 * columns will be increased by the total number of columns in the inserted
	 * matrices.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * // Assume matrix1 is initialized with Double values
	 * Matrix<Double> matrix2 = new Matrix<>(3, 2);
	 * // Assume matrix2 is initialized with Double values
	 * Matrix<Double> resultMatrix = matrix1.insertAfterFirstColumn(matrix2);
	 * // resultMatrix now contains matrix1 followed by matrix2 after the first column
	 * </pre>
	 * </p>
	 *
	 * @param matrices Varargs parameter representing the matrices to insert as
	 *                 columns.
	 * @return The matrix with the inserted columns.
	 * @throws IllegalArgumentException if the matrices do not have the same number
	 *                                  of rows.
	 * @see #insertColumn(int, Matrix[])
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> insertAfterFirstColumn(Matrix<T>... matrices) {
		return insertColumns(1, matrices);
	}

	/**
	 * Inserts one or more columns after the last column in the current matrix. The
	 * inserted columns are concatenated vertically.
	 * <p>
	 * This method inserts the given matrices after the last column of the matrix,
	 * effectively extending the matrix with additional columns. The operation
	 * requires that all matrices have the same number of rows. The resulting matrix
	 * will have the same number of rows as the original matrix but its number of
	 * columns will be increased by the total number of columns in the inserted
	 * matrices.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * // Assume matrix1 is initialized with Double values
	 * Matrix<Double> matrix2 = new Matrix<>(3, 2);
	 * // Assume matrix2 is initialized with Double values
	 * Matrix<Double> resultMatrix = matrix1.insertAfterLastColumn(matrix2);
	 * // resultMatrix now contains matrix1 followed by matrix2 after the last column
	 * </pre>
	 * </p>
	 *
	 * @param matrices Varargs parameter representing the matrices to insert as
	 *                 columns.
	 * @return The matrix with the inserted columns.
	 * @throws IllegalArgumentException if the matrices do not have the same number
	 *                                  of rows.
	 * @see #insertColumn(int, Matrix[])
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> insertAfterLastColumn(Matrix<T>... matrices) {
		return insertColumns(this.ncols, matrices);
	}

	/**
	 * Deletes a range of rows from the matrix.
	 * <p>
	 * This method removes a range of rows from the matrix, starting from the
	 * specified start index up to, but not including, the specified end index. If
	 * the indices are not valid, an {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> updatedMatrix = matrix.deleteRowsRange(1, 3);
	 * // updatedMatrix now contains the original matrix with the second row
	 * // removed
	 * </pre>
	 * </p>
	 *
	 * @param rowStart The starting row index (inclusive).
	 * @param rowEnd   The ending row index (exclusive).
	 * @return The current matrix after deleting the rows.
	 * @throws IllegalArgumentException if the indices are not valid.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> deleteRowsRange(int rowStart, int rowEnd) {
		if (rowStart < 0 || rowEnd >= nrows || rowStart > rowEnd) {
			throw new IllegalArgumentException("Invalid row indices.");
		}

		Matrix<T> top = extractRowsRange(0, rowStart - 1);
		Matrix<T> bottom = extractRowsRange(rowEnd + 1, nrows - 1);

		return top.concatenateByRows(bottom);
	}

	/**
	 * Deletes a range of columns from the matrix.
	 * <p>
	 * This method removes a range of columns from the matrix, starting from the
	 * specified start index up to, but not including, the specified end index. If
	 * the indices are not valid, an {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> updatedMatrix = matrix.deleteColumnsRange(1, 3);
	 * // updatedMatrix now contains the original matrix with the second and
	 * // third columns removed
	 * </pre>
	 * </p>
	 *
	 * @param colstart The starting column index (inclusive).
	 * @param colend   The ending column index (inclusive).
	 * @return The current matrix after deleting the columns.
	 * @throws IllegalArgumentException if the indices are not valid.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> deleteColumnsRange(int colstart, int colend) {
		if (colstart < 0 || colend < 0 || colend >= this.ncols || colstart > colend) {
			throw new IllegalArgumentException("Invalid column range.");
		}

		int newCols = this.ncols - (colend - colstart + 1);
		T[] newArray = (T[]) Array.newInstance(clazz, this.nrows * newCols);

		int idx = 0;
		for (int i = 0; i < this.nrows; i++) {
			for (int j = 0; j < this.ncols; j++) {
				if (j < colstart || j > colend) {
					newArray[idx++] = this.array[i * this.ncols + j];
				}
			}
		}

		return new Matrix<>(this.nrows, newCols, newArray, this.clazz);
	}

	/**
	 * Deletes specific rows from the matrix.
	 * <p>
	 * This method removes specific rows from the matrix based on the provided
	 * indices. The indices are specified as varargs, where each value represents
	 * the index of a row to be removed. If any index is not valid, an
	 * {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> updatedMatrix = matrix.deleteRows(1, 2);
	 * // updatedMatrix now contains the original matrix with the second and
	 * // third rows removed
	 * </pre>
	 * </p>
	 *
	 * @param indices The list of the indices of the rows to delete.
	 * @return The current matrix after deleting the rows.
	 * @throws IllegalArgumentException if any index is not valid.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> deleteRows(int... indices) {

		for (int index : indices) {
			if (index < 0) {
				throw new IllegalArgumentException("All indices must be non-negative.");
			}
		}

		T[][] A = to2DArray();
		int newLength = A.length - indices.length;

		// Check if the number of rows to delete is valid
		if (newLength < 0) {
			throw new IllegalArgumentException("Invalid number of rows to delete.");
		}

		T[][] result = (T[][]) Array.newInstance(clazz, newLength, A[0].length);

		// Sort the indices in ascending order
		Arrays.sort(indices);

		int j = 0, r = 0;
		for (int i = 0; i < A.length; i++) {
			if (j < indices.length && i == indices[j]) {
				j++;
				continue;
			}
			result[r++] = A[i];
		}

		return new Matrix<>(result, this.clazz);
	}

	/**
	 * Deletes specific columns from the matrix.
	 * <p>
	 * This method removes specific columns from the matrix based on the provided
	 * indices. The indices are specified as varargs, where each value represents
	 * the index of a column to be removed. If any index is not valid, an
	 * {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> updatedMatrix = matrix.deleteColumns(1, 2);
	 * // updatedMatrix now contains the original matrix with the second and
	 * // third columns removed
	 * </pre>
	 * </p>
	 *
	 * @param indices The list of the indices of the columns to delete.
	 * @return The current matrix after deleting the columns.
	 * @throws IllegalArgumentException if any index is not valid.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> deleteColumns(int... indices) {

		for (int index : indices) {
			if (index < 0) {
				throw new IllegalArgumentException("All indices must be non-negative.");
			}
		}

		T[][] A = to2DArray();
		int newLength = A[0].length - indices.length;
		T[][] result = (T[][]) Array.newInstance(clazz, A.length, newLength);

		Arrays.sort(indices);
		int j = 0, r = 0;
		for (int i = 0; i < A[0].length; i++) {
			if (j < indices.length && i == indices[j]) {
				j++;
				continue;
			}
			for (int k = 0; k < A.length; k++) {
				result[k][r] = A[k][i];
			}
			r++;
		}

		return new Matrix<>(result, this.clazz);
	}

	/**
	 * Resizes the matrix to the specified dimensions.
	 * <p>
	 * This method adjusts the size of the matrix to the specified number of rows
	 * and columns. It creates a new array with the new dimensions and copies the
	 * elements from the old array to the new array, preserving as much of the
	 * original data as possible. If the new dimensions are larger than the
	 * original, the new elements are initialized with the default value for the
	 * type of the matrix's elements.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * matrix.resize(2, 2);
	 * // matrix now has dimensions 2x2
	 * </pre>
	 * </p>
	 *
	 * @param newNrows The new number of rows.
	 * @param newNcols The new number of columns.
	 */
	@SuppressWarnings("unchecked")
	public void resize(int newNrows, int newNcols) {
		checkDimensionsValidity(newNrows, newNcols);
		// Create a new array with the new dimensions
		T[] newArray = (T[]) Array.newInstance(clazz, newNrows * newNcols);

		// Determine the minimum number of rows and columns to copy
		int minNrows = Math.min(nrows, newNrows);
		int minNcols = Math.min(ncols, newNcols);

		// Copy the elements from the old array to the new array
		for (int i = 0; i < minNrows; i++) {
			for (int j = 0; j < minNcols; j++) {
				newArray[i * newNcols + j] = this.array[i * ncols + j];
			}
		}

		// Update the array and dimensions
		this.array = newArray;
		this.nrows = newNrows;
		this.ncols = newNcols;
	}

	/**
	 * Reshapes the matrix to the specified number of rows and columns.
	 * <p>
	 * This method changes the dimensions of the matrix to the specified number of
	 * rows and columns. It ensures that the total number of elements in the
	 * reshaped matrix matches the total number of elements in the original matrix.
	 * If the total number of elements does not match, an
	 * {@link IllegalStateException} is thrown. The method also checks for valid
	 * dimensions, and if the provided dimensions are invalid (non-positive), it
	 * throws an {@link IllegalArgumentException}.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * matrix.reshape(2, 2); // Reshapes the matrix to 2x2
	 * </pre>
	 * </p>
	 *
	 * @param newRows The new number of rows.
	 * @param newCols The new number of columns.
	 * @throws IllegalArgumentException if the provided row or column dimension is
	 *                                  invalid.
	 * @throws IllegalStateException    if the total number of elements in the
	 *                                  reshaped matrix does not match the total
	 *                                  number of elements in the original matrix.
	 */
	@SuppressWarnings("unchecked")
	public void reshape(int newRows, int newCols)
			throws RuntimeException, IllegalArgumentException, IllegalStateException {
		// Check for valid dimensions
		checkDimensionsValidity(newRows, newCols);
		if (newRows * newCols != nrows * ncols) {
			throw new RuntimeException("The total number of elements must remain the same after reshaping. Expected: "
					+ (nrows * ncols) + ", Actual: " + (newRows * newCols));
		}

		int expectedElements = nrows * ncols;
		int actualElements = newRows * newCols;

		if (actualElements != expectedElements) {
			throw new IllegalStateException(
					String.format("Number of elements must remain the same after reshaping. Expected: %d, Actual: %d",
							expectedElements, actualElements));
		}

		// Create a new array with the new shape
		T[] newArray = (T[]) Array.newInstance(clazz, actualElements);
		System.arraycopy(array, 0, newArray, 0, array.length);

		// Update the array and dimensions
		array = newArray;
		nrows = newRows;
		ncols = newCols;
	}

	/**
	 * Swaps or permutes two rows in the matrix.
	 * <p>
	 * This method swaps the positions of two specified rows in the matrix. It is
	 * useful for reordering rows without needing to manually copy and paste data.
	 * If the indices are not valid, an {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * matrix.swapRows(0, 2);
	 * // The first and third rows of the matrix are now swapped
	 * </pre>
	 * </p>
	 *
	 * @param row1 The index of the first row.
	 * @param row2 The index of the second row.
	 * @throws IllegalArgumentException if the row indices are not valid.
	 */
	public void swapRows(int row1, int row2) {
		// Check if the row indices are valid and not the same
		if (row1 < 0 || row1 >= nrows || row2 < 0 || row2 >= nrows || row1 == row2) {
			throw new IllegalArgumentException("Invalid row indices.");
		}

		// Calculate the offsets for the two rows in the array
		int offset1 = row1 * ncols;
		int offset2 = row2 * ncols;

		// Swap the elements of the two rows
		for (int i = 0; i < ncols; i++) {
			T temp = array[offset1 + i];
			array[offset1 + i] = array[offset2 + i];
			array[offset2 + i] = temp;
		}
	}

	/**
	 * Swaps or permutes two columns in the matrix.
	 * <p>
	 * This method swaps the positions of two specified columns in the matrix. It is
	 * useful for reordering columns without needing to manually copy and paste
	 * data. If the indices are not valid, an {@link IllegalArgumentException} is
	 * thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * matrix.swapCols(0, 2);
	 * // The first and third columns of the matrix are now swapped
	 * </pre>
	 * </p>
	 *
	 * @param col1 The index of the first column.
	 * @param col2 The index of the second column.
	 * @throws IllegalArgumentException if the column indices are not valid.
	 */
	public void swapCols(int col1, int col2) {
		// Check if the column indices are valid
		if (col1 < 0 || col1 >= ncols || col2 < 0 || col2 >= ncols) {
			throw new IllegalArgumentException("Invalid column indices.");
		}

		// Swap the elements of the two columns
		for (int i = 0; i < nrows; i++) {
			T temp = array[i * ncols + col1];
			array[i * ncols + col1] = array[i * ncols + col2];
			array[i * ncols + col2] = temp;
		}
	}

	/**
	 * Shuffles the rows of the matrix.
	 * <p>
	 * This method shuffles the rows of the matrix a specified number of times. Each
	 * shuffle involves swapping each row with a randomly selected row. This method
	 * is useful for randomizing the order of rows in the matrix.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * matrix.shuffleRows(5);
	 * // The rows of the matrix have been shuffled 5 times
	 * </pre>
	 * </p>
	 *
	 * @param times The number of times to shuffle.
	 */
	public void shuffleRows(int times) {
		SecureRandom rand = new SecureRandom();

		for (int t = 0; t < times; t++) {
			for (int i = nrows - 1; i > 0; i--) {
				int j = rand.nextInt(i + 1); // Generate a random index from 0 to i
				// Swap rows i and j
				for (int k = 0; k < ncols; k++) {
					T temp = this.array[i * ncols + k];
					this.array[i * ncols + k] = this.array[j * ncols + k];
					this.array[j * ncols + k] = temp;
				}
			}
		}
	}

	/**
	 * Shuffles the columns of the matrix.
	 * <p>
	 * This method shuffles the columns of the matrix a specified number of times.
	 * Each shuffle involves swapping each column with a randomly selected column.
	 * This method is useful for randomizing the order of columns in the matrix.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * matrix.shuffleColumns(5);
	 * // The columns of the matrix have been shuffled 5 times
	 * </pre>
	 * </p>
	 *
	 * @param times The number of times to shuffle.
	 */
	public void shuffleColumns(int times) {
		// Create a secure random number generator
		SecureRandom rand = new SecureRandom();

		for (int t = 0; t < times; t++) {
			for (int i = ncols - 1; i > 0; i--) {
				int j = rand.nextInt(i + 1); // Generate a random index from 0 to i
				// Swap columns i and j
				for (int k = 0; k < nrows; k++) {
					T temp = this.array[k * ncols + i];
					this.array[k * ncols + i] = this.array[k * ncols + j];
					this.array[k * ncols + j] = temp;
				}
			}
		}
	}

	/**
	 * Shuffles the values within a specified row of the matrix.
	 * <p>
	 * This method shuffles the values within a specified row of the matrix a
	 * specified number of times. Each shuffle involves swapping each value in the
	 * row with a randomly selected value within the same row. This method is useful
	 * for randomizing the order of values within a specific row.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * matrix.shuffleRowValues(0, 5);
	 * // The values within the first row of the matrix have been shuffled 5
	 * // times
	 * </pre>
	 * </p>
	 *
	 * @param row   The index of the row.
	 * @param times The number of times to shuffle.
	 */
	public void shuffleValuesOfRow(int row, int times) {
		// Check if the row index is valid
		if (row < 0 || row >= nrows) {
			throw new IllegalArgumentException("Invalid row index.");
		}

		// Create a secure random number generator
		SecureRandom rand = new SecureRandom();

		// Shuffle the elements of the specified row
		for (int t = 0; t < times; t++) {
			for (int i = 0; i < ncols; i++) {
				int j = rand.nextInt(ncols);
				// Ensure i and j are not the same to avoid unnecessary swaps
				if (i != j) {
					T temp = array[row * ncols + i];
					array[row * ncols + i] = array[row * ncols + j];
					array[row * ncols + j] = temp;
				}
			}
		}
	}

	/**
	 * Shuffles the values within a specified column of the matrix.
	 * <p>
	 * This method shuffles the values within a specified column of the matrix a
	 * specified number of times. Each shuffle involves swapping each value in the
	 * column with a randomly selected value within the same column. This method is
	 * useful for randomizing the order of values within a specific column.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * matrix.shuffleColumnValues(0, 5);
	 * // The values within the first column of the matrix have been shuffled 5
	 * // times
	 * </pre>
	 * </p>
	 *
	 * @param col   The index of the column.
	 * @param times The number of times to shuffle.
	 */
	public void shuffleValuesOfColumn(int col, int times) {
		// Check if the column index is valid
		if (col < 0 || col >= ncols) {
			throw new IllegalArgumentException("Invalid column index.");
		}

		// Create a secure random number generator
		SecureRandom rand = new SecureRandom();

		// Shuffle the elements of the specified column
		for (int t = 0; t < times; t++) {
			for (int i = 0; i < nrows; i++) {
				int j = rand.nextInt(nrows);
				// Ensure i and j are not the same to avoid unnecessary swaps
				if (i != j) {
					T temp = array[i * ncols + col];
					array[i * ncols + col] = array[j * ncols + col];
					array[j * ncols + col] = temp;
				}
			}
		}
	}

	/**
	 * Shuffles the values in the matrix.
	 * <p>
	 * This method shuffles the values in the entire matrix a specified number of
	 * times. Each shuffle involves swapping each value in the matrix with a
	 * randomly selected value from anywhere in the matrix. This method is useful
	 * for randomizing the order of all values in the matrix.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * matrix.shuffleValues(5);
	 * // The values in the matrix have been shuffled 5 times
	 * </pre>
	 * </p>
	 *
	 * @param times The number of times to shuffle.
	 */
	public void shuffleValues(int times) {
		// Create a secure random number generator
		SecureRandom rand = new SecureRandom();

		// Shuffle the elements of the matrix
		for (int t = 0; t < times; t++) {
			for (int i = 0; i < nrows * ncols; i++) {
				int j = rand.nextInt(nrows * ncols);
				T temp = array[i];
				array[i] = array[j];
				array[j] = temp;
			}
		}
	}

	/**
	 * Shuffles the values within specific rows of the matrix.
	 * <p>
	 * This method shuffles the values within specified rows of the matrix a
	 * specified number of times. Each shuffle involves swapping each value in the
	 * specified rows with a randomly selected value within the same row. This
	 * method is useful for randomizing the order of values within specific rows.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * matrix.shuffleRowValues(5, 0, 2);
	 * // The values within the first and third rows of the matrix have been
	 * // shuffled 5 times
	 * </pre>
	 * </p>
	 *
	 * @param times The number of times to shuffle.
	 * @param rows  The indices of the rows.
	 */
	public void shuffleRowsValues(int times, int... rows) {
		SecureRandom rand = new SecureRandom();

		for (int r : rows) {
			if (r < 0 || r >= nrows) {
				throw new IllegalArgumentException("Invalid row index.");
			}

			for (int t = 0; t < times; t++) {
				for (int i = 0; i < ncols; i++) {
					int j = rand.nextInt(ncols);
					if (i != j) {
						T temp = get(r, i);
						set(r, i, get(r, j));
						set(r, j, temp);
					}
				}
			}
		}
	}

	/**
	 * Shuffles the values within specific columns of the matrix.
	 * <p>
	 * This method shuffles the values within specified columns of the matrix a
	 * specified number of times. Each shuffle involves swapping each value in the
	 * specified columns with a randomly selected value within the same column. This
	 * method is useful for randomizing the order of values within specific columns.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * matrix.shuffleColumnValues(5, 0, 2);
	 * // The values within the first and third columns of the matrix have been
	 * // shuffled 5 times
	 * </pre>
	 * </p>
	 *
	 * @param times The number of times to shuffle.
	 * @param cols  The indices of the columns to shuffle.
	 */
	public void shuffleColumnsValues(int times, int... cols) {
		for (int c : cols) {
			if (c < 0 || c >= ncols) {
				throw new IllegalArgumentException("Invalid column index.");
			}
			for (int t = 0; t < times; t++) {
				for (int i = 0; i < nrows; i++) {
					Random rand = new Random();
					int j = rand.nextInt(nrows);
					T temp = array[i * ncols + c];
					array[i * ncols + c] = array[j * ncols + c];
					array[j * ncols + c] = temp;
				}
			}
		}
	}

	/*******************************************
	 * Matrix BASIC OPERATIONS
	 ********************************************/

	/**
	 * Adds a scalar to each element of the matrix.
	 * <p>
	 * This method adds a specified scalar value to each element of the matrix,
	 * returning a new matrix with the result. The operation is element-wise,
	 * meaning that the scalar is added to each element individually.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> result = matrix.plus(2.0);
	 * // result now contains the original matrix with each element increased by
	 * // 2.0
	 * </pre>
	 * </p>
	 *
	 * @param scalar The scalar to add.
	 * @return The current matrix after adding the scalar.
	 */
	public Matrix<T> plus(T scalar) {
		// Validate the input scalar
		if (scalar == null) {
			throw new IllegalArgumentException("The scalar cannot be null.");
		}

		// Create a new matrix with the same dimensions as this matrix
		Matrix<T> result = new Matrix<>(nrows, ncols, clazz);

		for (int i = 0; i < array.length; i++) {
			T sum = add(array[i], scalar);
			result.set(i, sum);
		}

		return result;
	}

	/**
	 * Subtracts a scalar from each element of the matrix.
	 * <p>
	 * This method subtracts a specified scalar value from each element of the
	 * matrix, returning a new matrix with the result. The operation is
	 * element-wise, meaning that the scalar is subtracted from each element
	 * individually.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> result = matrix.minus(2.0);
	 * // result now contains the original matrix with each element decreased by
	 * // 2.0
	 * </pre>
	 * </p>
	 *
	 * @param scalar The scalar to subtract.
	 * @return The current matrix after subtracting the scalar.
	 */
	public Matrix<T> minus(T scalar) {
		// Validate the input scalar
		if (scalar == null) {
			throw new IllegalArgumentException("The scalar cannot be null.");
		}

		// Create a new matrix with the same dimensions as this matrix
		Matrix<T> result = new Matrix<>(nrows, ncols, clazz);

		for (int i = 0; i < array.length; i++) {
			T sum = subtract(array[i], scalar);
			result.set(i, sum);
		}

		return result;
	}

	/**
	 * Adds another matrix to the current matrix.
	 * <p>
	 * This method adds another matrix to the current matrix, element-wise. The
	 * result is a new matrix where each element is the sum of the corresponding
	 * elements in the two matrices. If the dimensions of the two matrices do not
	 * match, an {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * Matrix<Double> matrix2 = new Matrix<>(3, 3);
	 * // Assume matrix1 and matrix2 are initialized with Double values
	 * Matrix<Double> result = matrix1.plus(matrix2);
	 * // result now contains the sum of matrix1 and matrix2, element-wise
	 * </pre>
	 * </p>
	 *
	 * @param B The matrix to be added to the current matrix.
	 * @return A new matrix object that is the result of adding the input matrix to
	 *         the current matrix.
	 * @throws IllegalArgumentException If the dimensions of the two matrices do not
	 *                                  match.
	 */
	public Matrix<T> plus(Matrix<T> B) {
		// Check if the dimensions of the two matrices match
		if (this.nrows != B.nrows || this.ncols != B.ncols) {
			throw new IllegalArgumentException("Matrices dimensions do not match!");
		}

		// Create a new Matrix object to store the result
		Matrix<T> result = new Matrix<>(this.nrows, this.ncols, this.clazz);

		// Iterate over the 1D array directly
		for (int i = 0; i < this.array.length; i++) {
			// Use the add helper method to add corresponding elements in the two matrices
			T value = add(this.array[i], B.array[i]);
			// Set the calculated value in the result Matrix
			result.array[i] = value;
		}

		// Return the result Matrix
		return result;
	}

	/**
	 * Subtracts another matrix from the current matrix.
	 * <p>
	 * This method subtracts another matrix from the current matrix, element-wise.
	 * The result is a new matrix where each element is the difference of the
	 * corresponding elements in the two matrices. If the dimensions of the two
	 * matrices do not match, an {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * Matrix<Double> matrix2 = new Matrix<>(3, 3);
	 * // Assume matrix1 and matrix2 are initialized with Double values
	 * Matrix<Double> result = matrix1.minus(matrix2);
	 * // result now contains the difference of matrix1 and matrix2,
	 * // element-wise
	 * </pre>
	 * </p>
	 *
	 * @param B The matrix to be subtracted from the current matrix.
	 * @return A new matrix object that is the result of subtracting the input
	 *         matrix from the current matrix.
	 * @throws IllegalArgumentException If the dimensions of the two matrices do not
	 *                                  match.
	 */
	public Matrix<T> minus(Matrix<T> B) {
		// Check if the dimensions of the two matrices match
		if (this.nrows != B.nrows || this.ncols != B.ncols) {
			throw new IllegalArgumentException("Matrices dimensions do not match!");
		}

		// Create a new Matrix object to store the result
		Matrix<T> result = new Matrix<>(this.nrows, this.ncols, this.clazz);

		// Iterate over the 1D array directly
		for (int i = 0; i < this.array.length; i++) {
			// Use the subtract helper method to subtract corresponding elements in the two
			// matrices
			T value = subtract(this.array[i], B.array[i]);
			// Set the calculated value in the result Matrix
			result.array[i] = value;
		}

		// Return the result Matrix
		return result;
	}

	/**
	 * Perfoms the A*B multiplication operation where A can be a Vector or a Matrix
	 * and B can also be a Vector or a Matrix. Vector is a one-dimensional row or
	 * column array. Matrix is a two-dimensional row or column array.
	 * 
	 * @param B
	 * @return result of the product.
	 * @throws Exception
	 */
	public Matrix<T> multiply(Matrix<T> B) throws Exception {
		return times(B);
	}

	/**
	 * Perfoms the A*B multiplication excepted vector*vector operation where A can
	 * be a Vector or a Matrix and B can also be a Vector or a Matrix. Vector is a
	 * one-dimensional row or column array. Matrix is a two-dimensional row or
	 * column array. For vector*vector operation, use dot(.) or dotProduct(.).
	 * 
	 * @param B
	 * @return result of the product.
	 * @throws Exception
	 */
	public Matrix<T> times(Matrix<T> B) throws Exception {
		Matrix<T> A = this;
		// Check if A is a vector
		boolean isAVector = A.isVector();

		// Check if B is a vector
		boolean isBVector = B.isVector();

		if (isAVector && isBVector) { // v*v
			throw new Exception(
					"Operation impossible for Vector-Vector multiplication. Use dot(.) or dotProduct(.) method instead.");
		} else if (isAVector && !isBVector) { // v*M
			return multiplyVectorByMatrix(B);
		} else if (!isAVector && isBVector) { // M*v
			return multiplyMatrixByVector(B);
		} else { // M*M
//          return Math.max(A.nrows, A.ncols) == Math.max(B.nrows, B.ncols);
			return multiplyMatrixByMatrix(B);
		}
	}

	/**
	 * Multiplies the current matrix by another matrix.
	 * <p>
	 * This method multiplies the current matrix by another matrix, returning the
	 * product as a new matrix. The operation is matrix multiplication, following
	 * the rules of linear algebra.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * Matrix<Double> matrix2 = new Matrix<>(3, 3);
	 * // Assume matrix1 and matrix2 are initialized with Double values
	 * Matrix<Double> result = matrix1.times(matrix2);
	 * // result now contains the product of matrix1 and matrix2
	 * </pre>
	 * </p>
	 *
	 * @param B The matrix to multiply by.
	 * @return The product of the two matrices.
	 */
	public Matrix<T> multiplyMatrixByMatrix(Matrix<T> B) {
		if (this.ncols != B.nrows) {
			throw new IllegalArgumentException("Matrices dimensions are not compatible for multiplication.");
		}

		Matrix<T> result = new Matrix<>(this.nrows, B.ncols, clazz);

		for (int i = 0; i < this.nrows; i++) {
			for (int k = 0; k < B.ncols; k++) {
				for (int j = 0; j < this.ncols; j++) {
					T product = mult(this.get(i, j), B.get(j, k));
					T current = result.get(i, k);
					result.set(i, k, add(current, product));
				}
			}
		}

		return result;
	}

	/**
	 * Multiplies the current vector (column or row matrix) by another vector,
	 * element-wise, and returns the result.
	 * <p>
	 * This method performs element-wise multiplication of two vectors and returns a
	 * scalar result. The vectors must have the same number of elements for
	 * multiplication. The operation follows the rules of linear algebra.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> vector1 = new Matrix<>(1, 3);
	 * Matrix<Double> vector2 = new Matrix<>(1, 3);
	 * // Assume vector1 and vector2 are initialized with Double values
	 * Double result = vector1.multiplyVectorByVector(vector2);
	 * // result now contains the element-wise product of vector1 and vector2
	 * </pre>
	 * </p>
	 *
	 * @param vector2 The vector to multiply by.
	 * @return The result of element-wise multiplication as a scalar.
	 * @throws IllegalArgumentException if the vectors do not have the same number
	 *                                  of elements for multiplication.
	 */
	public T multiplyVectorByVector(Matrix<T> vector2) {
		Matrix<T> vector1 = this;

		// Check if the vectors are compatible for multiplication
		if (!verifyMultiplicationCompatibility(vector2)) {
			throw new IllegalArgumentException("Vectors must have the same number of elements for multiplication.");
		}

		// Initialize the result to zero
		T result = zeroValue();
		// Perform element-wise multiplication and sum the products
		for (int i = 0; i < vector1.getNcols(); i++) {
			result = add(result, mult(vector1.get(i), vector2.get(i)));
		}

		return (T) result;
	}

	/**
	 * Multiplies the current vector (matrix) by another matrix, resulting in a new
	 * matrix.
	 * <p>
	 * This method transposes the current vector (matrix) and multiplies the
	 * transposed matrix by another matrix. The resulting matrix is returned. The
	 * operation follows the rules of linear algebra.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> vector = new Matrix<>(1, 3);
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume vector and matrix are initialized with Double values
	 * Matrix<Double> result = vector.multiplyVectorByMatrix(matrix);
	 * // result now contains the product of the transposed vector and the matrix
	 * </pre>
	 * </p>
	 *
	 * @param matrix The matrix to multiply by.
	 * @return The product of the transposed vector and the matrix.
	 * @throws IllegalArgumentException if the number of columns in the vector is
	 *                                  not equal to the number of rows in the
	 *                                  matrix.
	 */
	public Matrix<T> multiplyVectorByMatrix(Matrix<T> matrix) {
		// Transpose the matrix and multiply the transposed matrix by the vector
		return this.fasttranspose().multiplyMatrixByMatrix(matrix);
	}

	/**
	 * Multiplies the current matrix by a vector, resulting in a new vector.
	 * <p>
	 * This method performs matrix-vector multiplication, where the current matrix
	 * is multiplied by the given vector. The resulting vector is returned. The
	 * operation follows the rules of linear algebra.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * Matrix<Double> vector = new Matrix<>(3, 1);
	 * // Assume matrix and vector are initialized with Double values
	 * Matrix<Double> result = matrix.multiplyMatrixByVector(vector);
	 * // result now contains the product of the matrix and vector
	 * </pre>
	 * </p>
	 *
	 * @param vector The vector to multiply by.
	 * @return The product of the matrix and the vector.
	 * @throws IllegalArgumentException if the number of columns in the matrix is
	 *                                  not equal to the number of rows in the
	 *                                  vector.
	 */
	public Matrix<T> multiplyMatrixByVector(Matrix<T> vector) {
		Matrix<T> matrix = this;
		// Check if the dimensions are compatible for multiplication
		if (matrix.ncols != vector.nrows) {
			throw new IllegalArgumentException("Matrix and vector dimensions are not compatible for multiplication.");
		}

		// Initialize a result vector with appropriate dimensions
		Matrix<T> result = new Matrix<>(matrix.nrows, 1, clazz);

		// Perform matrix-vector multiplication
		for (int i = 0; i < matrix.nrows; i++) {
			T sum = zeroValue(); // Initialize sum to zero
			for (int j = 0; j < matrix.ncols; j++) {
				T product = mult(matrix.get(i, j), vector.get(j, 0)); // Calculate element-wise product
				sum = add(sum, product); // Accumulate the products
			}
			result.set(i, 0, sum); // Set the computed sum in the result vector
		}

		return result;
	}

	/**
	 * Computes the dot product (inner product) of the current matrix and a vector.
	 * <p>
	 * This method computes the dot product of the current matrix and a vector. The
	 * dot product is a scalar value that results from an operation that combines
	 * the elements of the matrix and the vector. The dimensions of the matrix and
	 * the vector must be compatible for the operation to be valid.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * Matrix<Double> vector = new Matrix<>(3, 1, Double.class);
	 * // Assume matrix and vector are initialized with Double values
	 * Matrix<Double> result = matrix.dotProduct(vector);
	 * // result now contains the dot product of matrix and vector
	 * </pre>
	 * </p>
	 *
	 * @param vector The vector to compute the dot product with.
	 * @return The dot product of the matrix and the vector.
	 * @throws IllegalArgumentException if the dimensions of the matrix and the
	 *                                  vector are not compatible for the dot
	 *                                  product operation.
	 */
	public T dotProduct(Matrix<T> vector) {
		return multiplyVectorByVector(vector);
	}

	/**
	 * Multiplies each element of this matrix by the specified scalar value.
	 * 
	 * @param scalar the scalar value to multiply each element of the matrix by
	 * @return a new Matrix instance with each element multiplied by the scalar
	 * @throws IllegalArgumentException if the scalar value cannot be converted to
	 *                                  the type T
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> dotProduct(T scalar) {
		// Check that the scalar is compatible with the element type of the matrix
		Class<?> scalarClass = scalar.getClass();
		if (!scalarClass.equals(clazz) && !scalarClass.isAssignableFrom(clazz)) {
			throw new IllegalArgumentException("Scalar must be of the same type as the matrix elements.");
		}

		// Create a new 2D array to hold the result
		T[][] result = (T[][]) Array.newInstance(clazz, nrows, ncols);

		// Perform the scalar multiplication
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				result[i][j] = mult(to2DArray()[i][j], scalar);
			}
		}

		return new Matrix<>(result, this.clazz);
	}

	/**
	 * Calculates the dot product of this matrix with a vector.
	 *
	 * @param vector The vector to compute the dot product with.
	 * @return The result of the dot product.
	 */
	public T dot(Matrix<T> vector) {
		return multiplyVectorByVector(vector);
	}

	/**
	 * Multiplies each element of this matrix by a scalar.
	 *
	 * @param scalar The scalar value to multiply with.
	 * @return A new matrix resulting from the element-wise multiplication with the
	 *         scalar.
	 */
	public Matrix<T> dot(T scalar) {
		return dotProduct(scalar);
	}

	/**
	 * Multiplies each element of this matrix or vector by a scalar.
	 *
	 * @param scalar The scalar value to multiply with.
	 * @return A new matrix|vector resulting from the element-wise multiplication
	 *         with the scalar.
	 */
	public Matrix<T> multiplyByScalar(T scalar) {
		return dotProduct(scalar);
	}

	/**
	 * Computes the outer product of this matrix with another matrix. The outer
	 * product of two matrices A and B produces a new matrix where each element (i,
	 * j) of the resulting matrix is the product of the corresponding elements (i,
	 * j) from matrix A and all elements from matrix B.
	 * 
	 * @param B the matrix to compute the outer product with
	 * @return the outer product matrix
	 * @throws IllegalArgumentException if the dimensions of either matrix are not
	 *                                  positive
	 */
	public Matrix<T> outerProduct(Matrix<T> B) throws IllegalArgumentException {
		if (getNrows() <= 0 || getNcols() <= 0 || B.getNrows() <= 0 || B.getNcols() <= 0) {
			throw new IllegalArgumentException("Matrix dimensions must be positive.");
		}

		int rowsA = getNrows();
		int colsA = getNcols();
		int colsB = B.getNcols();

		@SuppressWarnings("unchecked")
		T[][] result = (T[][]) Array.newInstance(clazz, rowsA, colsB);

		if (colsA == 1) { // This matrix is a column vector
			T[] A = to1DArray();
			for (int i = 0; i < rowsA; i++) {
				for (int j = 0; j < colsB; j++) {
					result[i][j] = mult(A[i], B.get(0, j));
				}
			}
		} else if (B.getNrows() == 1) { // B is a row vector
			T[] B_arr = B.to1DArray();
			for (int i = 0; i < rowsA; i++) {
				for (int j = 0; j < colsB; j++) {
					result[i][j] = mult(get(i, 0), B_arr[j]);
				}
			}
		} else { // Both matrices are 2D
			for (int i = 0; i < rowsA; i++) {
				for (int j = 0; j < colsB; j++) {
					T sum = zeroValue();
					for (int k = 0; k < colsA; k++) {
						sum = add(sum, mult(get(i, k), B.get(k, j)));
					}
					result[i][j] = sum;
				}
			}
		}

		return new Matrix<>(result);
	}

	/**
	 * Computes the opposite (negation) of each element in the matrix.
	 * <p>
	 * This method creates a new matrix where each element is the negation of the
	 * corresponding element in the original matrix. The operation is element-wise,
	 * meaning that each element in the matrix is negated individually.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> result = matrix.oppose();
	 * // result now contains the negation of each element in the original
	 * // matrix
	 * </pre>
	 * </p>
	 *
	 * @return The matrix with each element negated.
	 */
	public Matrix<T> oppose() {
		Matrix<T> B = new Matrix<>(nrows, ncols, this.clazz);
		T[] array1D = B.getArray();

		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				array1D[i * ncols + j] = negate(array[i * ncols + j]);
			}
		}

		return B;
	}

	/**
	 * alias of {@link oppose()} method.
	 * 
	 * @return negated matrix or vector
	 */
	public Matrix<T> negate() {
		return this.oppose();
	}

	/**
	 * Performs the transpose of the matrix in an optimized way.
	 * <p>
	 * This method efficiently transposes the matrix, swapping its rows with its
	 * columns. The transpose operation is a fundamental operation in linear algebra
	 * that involves flipping a matrix over its diagonal. This method is optimized
	 * for performance.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> result = matrix.fasttranspose();
	 * // result now contains the transposed matrix
	 * </pre>
	 * </p>
	 *
	 * @return The transposed matrix.
	 */
	@SuppressWarnings("unchecked")
	public final Matrix<T> fasttranspose() {
		// Create a new 2D array to hold the result
		T[][] result = (T[][]) Array.newInstance(clazz, ncols, nrows);

		// Perform the transpose operation
		for (int r = ncols - 1; r >= 0; r--) {
			for (int c = nrows - 1; c >= 0; c--) {
				result[r][c] = array[c * ncols + r];
			}
		}

		return new Matrix<>(result, this.clazz);
	}

	/**
	 * Performs the transpose of the matrix in a naive way.
	 * <p>
	 * This method naively transposes the matrix, swapping its rows with its
	 * columns. The transpose operation is a fundamental operation in linear algebra
	 * that involves flipping a matrix over its diagonal. This method is
	 * straightforward but may be less efficient than optimized methods.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> result = matrix.transpose();
	 * // result now contains the transposed matrix
	 * </pre>
	 * </p>
	 *
	 * @return The transposed matrix.
	 */
	public Matrix<T> transpose() {
		Matrix<T> C = new Matrix<>(ncols, nrows, this.clazz);
		T[] array1D = C.getArray();

		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				array1D[j * nrows + i] = array[i * ncols + j];
			}
		}

		return C;
	}

	@SuppressWarnings("unchecked")
	public Matrix<T> kroneckerProduct(Matrix<T> matrixB) {
		Matrix<T> matrixA = this;
		T[][] A = matrixA.to2DArray();
		T[][] B = matrixB.to2DArray();
		int m = A.length;
		int n = A[0].length;
		int p = B.length;
		int q = B[0].length;
		int resultRows = m * p;
		int resultCols = n * q;

		T[][] result = (T[][]) Array.newInstance(clazz, resultRows, resultCols);

		// Compute Kronecker productby permuting ijkl order for high performance
		for (int i = 0; i < m; i++) {
			for (int k = 0; k < p; k++) {
				for (int j = 0; j < n; j++) {
					for (int l = 0; l < q; l++) {
						result[i * p + k][j * q + l] = mult(A[i][j], B[k][l]);
					}
				}
			}
		}

		return new Matrix<T>(result);
	}

	/***********************************************
	 * ELEMENT-WISE BASIC OPERATIONS
	 *********************************************/

	/**
	 * Computes the Element-wise product also known as the element-wise|Hadamard
	 * product or Schur product.
	 * 
	 * <p>
	 * This method involves multiplying two matrices of the same dimensions element
	 * by element to produce a new matrix of the same size. It is commutative,
	 * associative, and distributive over addition, but it is not the same as the
	 * outer product or inner product. The outer product, on the other hand,
	 * involves multiplying two vectors to create a matrix, while the inner product
	 * results in a scalar value. It is an alias of
	 * {@link #elementWiseMultiply(Matrix)} method.
	 * </p>
	 * 
	 * @param other
	 * @return
	 */
	public Matrix<T> hadamardProduct(Matrix<T> other) {
		return elementWiseMultiply(other);
	}

	/**
	 * Divides each element of the current matrix by the corresponding element of
	 * another matrix.
	 * <p>
	 * This method performs element-wise division of the current matrix by another
	 * matrix. Each element in the current matrix is divided by the corresponding
	 * element in the other matrix. The result is a new matrix with the division
	 * results.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * Matrix<Double> matrix2 = new Matrix<>(3, 3);
	 * // Assume matrix1 and matrix2 are initialized with Double values
	 * Matrix<Double> result = matrix1.divElementByElement(matrix2);
	 * // result now contains the element-wise division of matrix1 by matrix2
	 * </pre>
	 * </p>
	 *
	 * @param B The right operand matrix.
	 * @return The result matrix after element-wise division.
	 */
	public Matrix<T> divElementByElement(Matrix<T> B) {
		// Check if the matrix dimensions are compatible for element-wise division
		if (this.nrows != B.nrows || this.ncols != B.ncols) {
			throw new IllegalArgumentException("Matrices must have the same dimensions for element-wise division.");
		}

		// Initialize a result matrix with appropriate dimensions
		Matrix<T> result = new Matrix<>(this.nrows, this.ncols, clazz);

		// Perform element-wise division of the matrices
		for (int i = 0; i < this.nrows; i++) {
			for (int j = 0; j < this.ncols; j++) {
				T elementA = this.get(i, j);
				T elementB = B.get(i, j);
				// Check if the divisor is zero
				if (isZero(elementB)) {
					throw new ArithmeticException("Division by zero encountered.");
				}
				// Perform element-wise division and store the result in the new matrix
				T divisionResult = divide(elementA, elementB);
				result.set(i, j, divisionResult);
			}
		}

		return result;
	}

	/**
	 * Divides each element of the current matrix by a scalar value.
	 * <p>
	 * This method performs element-wise division of the current matrix by a scalar
	 * value. Each element in the current matrix is divided by the scalar value. The
	 * result is a new matrix with the division results.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> result = matrix.divElementByConstant(2.0);
	 * // result now contains the original matrix with each element divided by
	 * // 2.0
	 * </pre>
	 * </p>
	 *
	 * @param scalar The constant scalar value.
	 * @return The result matrix after element-wise division by the scalar.
	 */
	public Matrix<T> divElementByConstant(T scalar) {
		// Initialize a result matrix with the same dimensions as the original matrix
		Matrix<T> result = new Matrix<>(this.nrows, this.ncols, clazz);

		// Perform element-wise division of each element by the scalar constant
		for (int i = 0; i < this.nrows; i++) {
			for (int j = 0; j < this.ncols; j++) {
				T element = this.get(i, j);
				// Check if the scalar is zero to avoid division by zero
				if (isZero(scalar)) {
					throw new ArithmeticException("Division by zero encountered.");
				}
				// Perform element-wise division and store the result in the new matrix
				T divisionResult = divide(element, scalar);
				result.set(i, j, divisionResult);
			}
		}

		return result;
	}

	/**
	 * Performs element-wise division of the current matrix by another matrix.
	 * <p>
	 * This method divides each element of the current matrix by the corresponding
	 * element of another matrix. The dimensions of the two matrices must match,
	 * otherwise an {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * Matrix<Double> matrix2 = new Matrix<>(3, 3);
	 * // Assume matrix1 and matrix2 are initialized with Double values
	 * Matrix<Double> result = matrix1.elementWiseDivide(matrix2);
	 * // result now contains the element-wise division of matrix1 by matrix2
	 * </pre>
	 * </p>
	 *
	 * @param other The matrix to divide by.
	 * @return The resulting matrix after element-wise division.
	 * @throws IllegalArgumentException If the dimensions of the two matrices do not
	 *                                  match.
	 */
	public Matrix<T> elementWiseDivide(Matrix<T> other) {
		if (nrows != other.nrows || ncols != other.ncols) {
			throw new IllegalArgumentException("Matrices must have the same dimensions for element-wise division.");
		}
		Matrix<T> result = new Matrix<>(nrows, ncols, this.clazz);
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				// Check for division by zero
				if (other.get(i, j) != null && isZero(other.get(i, j))) {
					throw new ArithmeticException("Division by zero is not allowed.");
				}
				result.set(i, j, divide(this.get(i, j), other.get(i, j)));
			}
		}
		return result;
	}

	/**
	 * Performs element-wise multiplication of the current matrix by another matrix.
	 * <p>
	 * This method multiplies each element of the current matrix by the
	 * corresponding element of another matrix. The dimensions of the two matrices
	 * must match, otherwise an {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * Matrix<Double> matrix2 = new Matrix<>(3, 3);
	 * // Assume matrix1 and matrix2 are initialized with Double values
	 * Matrix<Double> result = matrix1.elementWiseMultiply(matrix2);
	 * // result now contains the element-wise multiplication of matrix1 and
	 * // matrix2
	 * </pre>
	 * </p>
	 *
	 * @param other The matrix to multiply by.
	 * @return The resulting matrix after element-wise multiplication.
	 * @throws IllegalArgumentException If the dimensions of the two matrices do not
	 *                                  match.
	 */
	public Matrix<T> elementWiseMultiply(Matrix<T> other) {
		if (nrows != other.nrows || ncols != other.ncols) {
			throw new IllegalArgumentException(
					"Matrices must have the same dimensions for element-wise multiplication.");
		}
		Matrix<T> result = new Matrix<>(nrows, ncols, this.clazz);
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				result.set(i, j, mult(this.get(i, j), other.get(i, j)));
			}
		}
		return result;
	}

	/**
	 * Performs element-wise addition of the current matrix by another matrix.
	 * <p>
	 * This method adds each element of the current matrix to the corresponding
	 * element of another matrix. The dimensions of the two matrices must match,
	 * otherwise an {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * Matrix<Double> matrix2 = new Matrix<>(3, 3);
	 * // Assume matrix1 and matrix2 are initialized with Double values
	 * Matrix<Double> result = matrix1.elementWiseAdd(matrix2);
	 * // result now contains the element-wise addition of matrix1 and matrix2
	 * </pre>
	 * </p>
	 *
	 * @param other The matrix to add.
	 * @return The resulting matrix after element-wise addition.
	 * @throws IllegalArgumentException If the dimensions of the two matrices do not
	 *                                  match.
	 */
	public Matrix<T> elementWiseAdd(Matrix<T> other) {
		return plus(other);
	}

	/**
	 * Performs element-wise subtraction of the current matrix by another matrix.
	 * <p>
	 * This method subtracts each element of the current matrix from the
	 * corresponding element of another matrix. The dimensions of the two matrices
	 * must match, otherwise an {@link IllegalArgumentException} is thrown.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix1 = new Matrix<>(3, 3);
	 * Matrix<Double> matrix2 = new Matrix<>(3, 3);
	 * // Assume matrix1 and matrix2 are initialized with Double values
	 * Matrix<Double> result = matrix1.elementWiseSubtract(matrix2);
	 * // result now contains the element-wise subtraction of matrix1 and
	 * // matrix2
	 * </pre>
	 * </p>
	 *
	 * @param other The matrix to subtract.
	 * @return The resulting matrix after element-wise subtraction.
	 * @throws IllegalArgumentException If the dimensions of the two matrices do not
	 *                                  match.
	 */
	public Matrix<T> elementWiseSubtract(Matrix<T> other) {
		return minus(other);
	}

	/**********************************************
	 * NORMS OR MAGNITUDES OR LENGTHS OR DISTANCES
	 **********************************************/

	/**
	 * Computes the lp-norm of the matrix.
	 * <p>
	 * The lp-norm, also known as the L^p norm, is a measure of the magnitude of the
	 * matrix. It is calculated by summing the absolute values of the matrix
	 * elements raised to the power of p, and then taking the p-th root of the sum.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * double norm = matrix.lpNorm(2); // Computes the L^2 norm (Euclidean norm)
	 * </pre>
	 * </p>
	 *
	 * @param p the order of the norm (p >= 1)
	 * @return the lp-norm
	 */
	public double lpNorm(int p) {
		double sum = 0.0;
		for (T element : array) {
			sum += Math.pow(((BigDecimal) element).doubleValue(), p);
		}
		return Math.pow(sum, 1.0 / p);
	}

	/**
	 * Computes the l1-norm (Manhattan norm) of the matrix.
	 * <p>
	 * The l1-norm is a measure of the magnitude of the matrix, calculated as the
	 * sum of the absolute values of its elements.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * double norm = matrix.l1Norm(); // Computes the L^1 norm (Manhattan norm)
	 * </pre>
	 * </p>
	 *
	 * @return the l1-norm
	 */
	public double l1Norm() {
		return lpNorm(1);
	}

	/**
	 * Computes the l2-norm (Euclidean norm) of the matrix.
	 * <p>
	 * The l2-norm is a measure of the magnitude of the matrix, calculated as the
	 * square root of the sum of the squares of its elements.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * double norm = matrix.l2Norm(); // Computes the L^2 norm (Euclidean norm)
	 * </pre>
	 * </p>
	 *
	 * @return the l2-norm
	 */
	public double l2Norm() {
		return lpNorm(2);
	}

	/**
	 * Computes the lp-norm of the matrix.
	 * <p>
	 * The lp-norm, also known as the L^p norm, is a measure of the magnitude of the
	 * matrix. It is calculated by summing the absolute values of the matrix
	 * elements raised to the power of p, and then taking the p-th root of the sum.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * double norm = matrix.lpNorm(2); // Computes the L^2 norm (Euclidean norm)
	 * </pre>
	 * </p>
	 *
	 * @param p the order of the norm (p >= 1)
	 * @return the lp-norm
	 */
	public T pNorm(int p) {
		T sum = zeroValue();
		for (T element : array) {
			sum = add(sum, pow(element, p));
		}
		return nthRoot(sum, p);
	}

	/**
	 * Computes the l1-norm (Manhattan norm) of the matrix.
	 * <p>
	 * The l1-norm is a measure of the magnitude of the matrix, calculated as the
	 * sum of the absolute values of its elements.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * double norm = matrix.l1Norm(); // Computes the L^1 norm (Manhattan norm)
	 * </pre>
	 * </p>
	 *
	 * @return the l1-norm
	 */
	public T norm1() {
		return pNorm(1);
	}

	/**
	 * Computes the l2-norm (Euclidean norm) of the matrix.
	 * <p>
	 * The l2-norm is a measure of the magnitude of the matrix, calculated as the
	 * square root of the sum of the squares of its elements.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * double norm = matrix.l2Norm(); // Computes the L^2 norm (Euclidean norm)
	 * </pre>
	 * </p>
	 *
	 * @return the l2-norm
	 */
	public T norm2() {
//		T sum = zeroValue();
//		for (T element : array) {
//			sum = add(sum, mult(element, element));
//		}
//		return nthRoot(sum, 2);
		return pNorm(2);
	}

	/**
	 * Computes the infinity-norm of the matrix.
	 * <p>
	 * The infinity-norm is the maximum absolute value of the elements in the
	 * matrix. It is a measure of the magnitude of the matrix.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * double norm = matrix.infinityNorm(); // Computes the infinity-norm
	 * </pre>
	 * </p>
	 *
	 * @return the infinity-norm
	 */
	public double infinityNorm() {
		double maxNorm = 0.0;
		for (T element : array) {
			double absValue = Math.abs(((BigDecimal) element).doubleValue());
			if (absValue > maxNorm) {
				maxNorm = absValue;
			}
		}
		return maxNorm;
	}

	/**
	 * Computes the Frobenius norm of the matrix.
	 * <p>
	 * The Frobenius norm is the square root of the sum of the squares of the
	 * elements of the matrix. It is a measure of the magnitude of the matrix.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * double norm = matrix.frobeniusNorm(); // Computes the Frobenius norm
	 * </pre>
	 * </p>
	 *
	 * @return the Frobenius norm
	 */
	public double frobeniusNorm() {
		double sumOfSquares = 0.0;
		for (T element : array) {
			sumOfSquares += Math.pow(((BigDecimal) element).doubleValue(), 2);
		}
		return Math.sqrt(sumOfSquares);
	}

	/**
	 * Computes the pq-norm of the matrix.
	 * <p>
	 * The pq-norm is a generalization of the lp-norm, where the matrix is first
	 * transformed into a p-norm along the columns, and then the q-norm of the
	 * transformed matrix is calculated.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * double norm = matrix.pq_norm(2, 2); // Computes the 2,2-norm
	 * </pre>
	 * </p>
	 *
	 * @param p the order of the norm along the columns (p >= 1)
	 * @param q the order of the norm of the transformed matrix (q >= 1)
	 * @return the pq-norm
	 */
	public double pq_norm(int p, int q) {
		if (p <= 0 || q <= 0) {
			throw new IllegalArgumentException("p and q must be non-zero and positive.");
		}

		double norm = 0.0;
		T[][] Matrix = this.to2DArray();

		// Calculate the p-norm along the columns
		for (int j = 0; j < ncols; j++) {
			double sum = 0.0;
			for (int i = 0; i < nrows; i++) {
				sum += Math.pow(Math.abs(((BigDecimal) Matrix[i][j]).doubleValue()), p);
			}
			norm += Math.pow(sum, 1.0 / p);
		}

		return Math.pow(norm, 1.0 / q);
	}

	/**
	 * Computes the maximum absolute value of the matrix.
	 * <p>
	 * This method finds the maximum absolute value among all the elements of the
	 * matrix.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * double maxAbs = matrix.maxAbsValue(); // Finds the maximum absolute value in the matrix
	 * </pre>
	 * </p>
	 *
	 * @return the maximum absolute value
	 */
	public double maxAbsValue() {
		double maxAbs = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < array.length; i++) {
			double absValue = Math.abs(((BigDecimal) array[i]).doubleValue());
			if (absValue > maxAbs) {
				maxAbs = absValue;
			}
		}
		return maxAbs;
	}

	/**
	 * Calculates the Ky Fan k-norm of the matrix.
	 * <p>
	 * The Ky Fan k-norm is the sum of the k largest singular values of the matrix.
	 * It is a measure of the matrix's "spread" or "width" in its singular value
	 * decomposition.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * double kyFanNorm = matrix.kyFanNorm(2); // Computes the 2-norm of the matrix's singular values
	 * </pre>
	 * </p>
	 *
	 * @param k The number of largest singular values to sum.
	 * @return The Ky Fan k-norm of the matrix.
	 * @throws IllegalArgumentException if k is less than 1 or greater than the
	 *                                  number of singular values.
	 */
	public double kyFanNorm(int k) throws IllegalArgumentException {
		if (k < 1 || k > Math.min(nrows, ncols)) {
			throw new IllegalArgumentException("k must be between 1 and the number of singular values.");
		}
		// Calculate the singular values
		double[] singularValues = this.singularValues();

		// Sort the singular values in descending order
		Arrays.sort(singularValues);
		int n = singularValues.length;

		// Sum the k largest singular values
		double sum = 0.0;
		for (int i = 0; i < k; i++) {
			sum += singularValues[n - 1 - i];
		}

		return sum;
	}

	/**
	 * Computes the trace of the matrix.
	 * <p>
	 * The trace of a matrix is the sum of the elements on its main diagonal. It is
	 * a measure of the matrix's "identity" or "self".
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * BigDecimal trace = matrix.trace(); // Computes the trace of the matrix
	 * </pre>
	 * </p>
	 *
	 * @return the trace of the matrix
	 * @throws IllegalArgumentException if the matrix is not square
	 */
	public T trace() {
		// Check if the matrix is square
		if (nrows != ncols) {
			throw new IllegalArgumentException("Matrix must be square to calculate trace.");
		}

		// Check if the element type of the matrix is a Number
		if (!Number.class.isAssignableFrom(clazz)) {
			throw new IllegalArgumentException("Element type must be a Number to calculate the trace.");
		}

		// Calculate the sum of the elements on the principal diagonal
		T sum = zeroValue();
		for (int i = 0; i < nrows; i++) {
			T element = array[i * ncols + i];
			sum = add(sum, element);
		}

		return sum;
	}

	/**
	 * Creates an identity matrix with the given number of rows and columns.
	 * <p>
	 * An identity matrix is a square matrix with ones on the main diagonal and
	 * zeros elsewhere. It is often used in linear algebra to represent the identity
	 * transformation.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Float> identityfloats = new Matrix<>(Float.class); 
	 * identityfloats = identityfloats.identity(3); // Creates a 3x3 identity matrix
	 * identityfloats.print()
	 * </pre>
	 * </p>
	 *
	 * @param nrows Number of rows.
	 * @param ncols Number of columns.
	 * @return The identity matrix.
	 * @throws IllegalArgumentException if the number of rows and columns are not
	 *                                  equal.
	 */
	public Matrix<T> identity(int nrows, int ncols) {
		// Check if the dimensions are valid
		if (nrows <= 0 || ncols <= 0) {
			throw new IllegalArgumentException("Invalid dimensions for identity matrix");
		}

		// Create a new matrix with the given dimensions
		Matrix<T> result = new Matrix<>(nrows, ncols, clazz);

		// Fill the diagonal elements with ones
		for (int i = 0; i < Math.min(nrows, ncols); i++) {
			result.set(i, i, oneValue());
		}

		return result;
	}

	public Matrix<T> identity(int nrows, int ncols, Class<T> clazz) {
		// Check if the dimensions are valid
		if (nrows <= 0 || ncols <= 0) {
			throw new IllegalArgumentException("Invalid dimensions for identity matrix");
		}

		// Create a new matrix with the given dimensions and class type
		Matrix<T> result = new Matrix<>(nrows, ncols, clazz);

		// Fill the diagonal elements with ones
		for (int i = 0; i < Math.min(nrows, ncols); i++) {
			result.set(i, i, oneValue());
		}

		return result;
	}

	/**
	 * Creates an identity matrix with the given size.
	 * <p>
	 * An identity matrix is a square matrix with ones on the main diagonal and
	 * zeros elsewhere. This method creates an identity matrix of the specified
	 * size, where the size is the number of rows and columns.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> identityMatrix = Matrix.identity(3); // Creates a 3x3 identity matrix
	 * </pre>
	 * </p>
	 *
	 * @param size The number of rows (and columns) for the identity matrix.
	 * @return The identity matrix of the given size.
	 */
	public Matrix<T> identity(int size) {
		// Check if the dimensions are valid
		if (size <= 0) {
			throw new IllegalArgumentException("Size must be positive.");
		}

		return identity(size, size);
	}

	/**
	 * Sets the current matrix as an identity matrix.
	 *
	 * @throws IllegalArgumentException If the matrix is not square.
	 */
	public void setIdentity() {
		if (nrows != ncols) {
			throw new IllegalArgumentException("Matrix must be square to set as identity");
		}

		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				if (i == j) {
					array[i * ncols + j] = oneValue();
				} else {
					array[i * ncols + j] = zeroValue();
				}
			}
		}
	}

	/**
	 * Creates a matrix with the given number of rows and columns, where all
	 * elements are ones.
	 * <p>
	 * This method creates a matrix filled with ones, with the specified number of
	 * rows and columns.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> onesMatrix = Matrix.ones(3, 3); // Creates a 3x3 matrix filled with ones
	 * </pre>
	 * </p>
	 *
	 * @param nrows Number of rows.
	 * @param ncols Number of columns.
	 * @return The matrix of ones.
	 */
	public Matrix<T> ones(int nrows, int ncols) {
		// Check if the dimensions are valid
		checkDimensionsValidity(nrows, ncols);

		// Check if the element type of the matrix is a Number
		if (!Number.class.isAssignableFrom(clazz)) {
			throw new IllegalArgumentException("Element type must be a Number to create an identity matrix.");
		}
		// Create a new Matrix with the given dimensions
		Matrix<T> B = new Matrix<>(nrows, ncols, oneValue(), this.clazz);

		return B;
	}

	/**
	 * Creates a matrix filled with zeros.
	 * <p>
	 * This method creates a matrix of the specified dimensions, with all elements
	 * set to zero.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> zerosMatrix = Matrix.zeros(3, 3); // Creates a 3x3 matrix filled with zeros
	 * </pre>
	 * </p>
	 *
	 * @param nrows The number of rows for the matrix.
	 * @param ncols The number of columns for the matrix.
	 * @return A matrix filled with zeros.
	 */
	public Matrix<T> zeros(int nrows, int ncols) {
		// Check if the dimensions are valid
		checkDimensionsValidity(nrows, ncols);

		// Check if the element type of the matrix is a Number
		if (!Number.class.isAssignableFrom(clazz)) {
			throw new IllegalArgumentException("Element type must be a Number to create an identity matrix.");
		}
		// Create a new Matrix with the given dimensions
		Matrix<T> B = new Matrix<>(nrows, ncols, zeroValue(), this.clazz);

		return B;
	}

	/**
	 * Creates a square identity matrix of a given size.
	 * <p>
	 * This method is an alias for {@link #identity(int size)} and serves the same
	 * purpose, creating a square identity matrix of the specified size.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> identityMatrix = Matrix.eye(3); // Creates a 3x3 identity matrix
	 * </pre>
	 * </p>
	 *
	 * @param size The number of rows (and columns) for the identity matrix.
	 * @return An identity matrix of the given size.
	 */
	public Matrix<T> eye(int size) {
		// Check if the dimensions are valid
		if (size <= 0) {
			throw new IllegalArgumentException("Size must be strictly positive.");
		}
		return identity(size);
	}
	
	/**
	 * Scales the matrix by a given factor.
	 * <p>
	 * This method creates a new matrix where each element is the product of the corresponding element in the original matrix and the specified scaling factor.
	 * </p>
	 * <p>
	 * The scaling operation is performed element-wise, meaning each element of the matrix is multiplied by the scaling factor independently of the other elements.
	 * </p>
	 *
	 * @param factor The scaling factor by which to multiply each element of the matrix.
	 * @return A new matrix with each element scaled by the specified factor.
	 */
	public Matrix<T> scale(T factor) {
	    Matrix<T> result = new Matrix<>(nrows, ncols, clazz);
	    for (int i = 0; i < nrows; i++) {
	        for (int j = 0; j < ncols; j++) {
	            result.set(i, j, mult(get(i, j), factor));
	        }
	    }
	    return result;
	}

	/**
	 * Calculates the minor of a matrix.
	 * <p>
	 * The minor of a matrix is the determinant of the submatrix obtained by
	 * removing a specified row and column from the original matrix. This method
	 * calculates the minor by excluding the given row and column from the matrix.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> minorMatrix = matrix.minor(0, 0); // Calculates the minor excluding row 0 and column 0
	 * </pre>
	 * </p>
	 *
	 * @param r The row to exclude.
	 * @param c The column to exclude.
	 * @return The minor of the matrix obtained by excluding the given row and
	 *         column.
	 */
	public Matrix<T> minor(int r, int c) {
		// Check if the element type of the matrix is a Number
		if (!Number.class.isAssignableFrom(clazz)) {
			throw new IllegalArgumentException("Element type must be a Number to calculate the minor.");
		}

		// Check if the row and column indices are within bounds
		if (r < 0 || r >= nrows || c < 0 || c >= ncols) {
			throw new IndexOutOfBoundsException("Row or column index out of bounds.");
		}

		int m = nrows - 1;
		int n = ncols - 1;
		Matrix<T> M = new Matrix<>(m, n, this.clazz);

		// Copy the elements of the original Matrix, skipping the specified row
		// and column
		for (int i = 0; i < m; ++i) {
			for (int j = 0; j < n; ++j) {
				int x = (i < r) ? i : i + 1;
				int y = (j < c) ? j : j + 1;
				M.set(i, j, get(x, y));
			}
		}

		return M;
	}

	/**
	 * Converts the current matrix into an upper (superior) triangular matrix.
	 * <p>
	 * This method transforms the current matrix into an upper triangular matrix by
	 * setting all elements below the main diagonal to zero.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> upperTriangularMatrix = matrix.toUpperTriangular();
	 * </pre>
	 * </p>
	 *
	 * @return The upper triangular matrix.
	 */
	public Matrix<T> toUpperTriangular() {
		// Create a copy of the current Matrix
		Matrix<T> B = new Matrix<>(nrows, ncols, clazz);
		// Copy elements from the original matrix to the new matrix
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				B.set(i, j, this.get(i, j));
			}
		}

		// Set all elements below the main diagonal to zero
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < i; j++) {
				B.set(i, j, zeroValue());
			}
		}

		return B;
	}

	/**
	 * Converts the current matrix into a lower (inferior) triangular matrix.
	 * <p>
	 * This method transforms the current matrix into a lower triangular matrix by
	 * setting all elements above the main diagonal to zero.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> lowerTriangularMatrix = matrix.toLowerTriangular();
	 * </pre>
	 * </p>
	 *
	 * @return The lower triangular matrix.
	 */
	public Matrix<T> toLowerTriangular() {
		// Create a copy of the current Matrix
		Matrix<T> B = new Matrix<>(nrows, ncols, clazz);
		// Copy elements from the original matrix to the new matrix
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				B.set(i, j, this.get(i, j));
			}
		}

		// Set all elements above the main diagonal to zero
		for (int i = 0; i < nrows; i++) {
			for (int j = i + 1; j < ncols; j++) {
				B.set(i, j, zeroValue());
			}
		}

		return B;
	}

	/**
	 * Converts the given matrix into an upper (superior) triangular matrix.
	 * <p>
	 * This method transforms the given matrix into an upper triangular matrix by
	 * setting all elements below the main diagonal to zero.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * // Example 1
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> upperTriangularMatrix = Matrix.upperTriangular(matrix);
	 * // Example 2
	 * Matrix<Integer> randintmatrix1 = new Matrix<>(Integer.class);
	 * Matrix<Integer> randintmatrix2 = new Matrix<>(Integer.class);
	 * randintmatrix1 = randintmatrix1.randomIntegerMatrix(3, 3, 1);
	 * randintmatrix1.print();
	 * randintmatrix2.upperTriangular(randintmatrix1).print();
	 * </pre>
	 * </p>
	 *
	 * @param B The matrix to convert.
	 * @return The upper triangular matrix.
	 */
	public Matrix<T> upperTriangular(Matrix<T> B) {
		return B.toUpperTriangular();
	}

	/**
	 * Converts the given matrix into a lower (inferior) triangular matrix.
	 * <p>
	 * This method transforms the given matrix into a lower triangular matrix by
	 * setting all elements above the main diagonal to zero.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> lowerTriangularMatrix = Matrix.lowerTriangular(matrix);
	 * @see {@link #upperTriangular(Matrix)} for analogy.
	 * </pre>
	 * </p>
	 *
	 * @param B The matrix to convert.
	 * @return The lower triangular matrix.
	 */
	public Matrix<T> lowerTriangular(Matrix<T> B) {
		return B.toLowerTriangular();
	}

	/**
	 * Retrieves the diagonal elements of the matrix.
	 * <p>
	 * This method returns the elements on the main diagonal of the matrix. It is
	 * applicable to both square and non-square matrices.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Double[] diagonal = matrix.diag(); // Retrieves the diagonal elements
	 * </pre>
	 * </p>
	 *
	 * @return A 1D array containing the diagonal elements.
	 * @throws IllegalArgumentException if the matrix is not square.
	 */
	@SuppressWarnings("unchecked")
	public T[] diag() {
		// Determine the length of the diagonal
		int length = Math.min(nrows, ncols);

		// Create a new array to hold the diagonal elements
		T[] diag = (T[]) Array.newInstance(clazz, length);

		// Iterate over the diagonal elements and store them in the array
		for (int i = 0; i < length; i++) {
			// Check if the indices are within bounds
			if (i < 0 || i >= nrows || i < 0 || i >= ncols) {
				throw new IndexOutOfBoundsException("Indices out of bounds.");
			}
			diag[i] = get(i, i);
		}

		return diag;
	}

	/**
	 * Retrieves the diagonal elements of the matrix as 1d array converted into
	 * Matrix.
	 * <p>
	 * This method returns the elements on the main diagonal of the matrix. It is
	 * applicable to both square and non-square matrices.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * matrix.diagonal().print(); // Retrieves the diagonal elements
	 * </pre>
	 * </p>
	 *
	 * @return A 1D array containing the diagonal elements.
	 * @throws IllegalArgumentException if the matrix is not square.
	 */
	public Matrix<T> diagonal() {
		// Determine the length of the diagonal
		int length = Math.min(nrows, ncols);

		// Create a new array to hold the diagonal elements
		@SuppressWarnings("unchecked")
		T[] diag = (T[]) Array.newInstance(clazz, length);

		// Iterate over the diagonal elements and store them in the array
		for (int i = 0; i < length; i++) {
			// Check if the indices are within bounds
			if (i < 0 || i >= nrows || i < 0 || i >= ncols) {
				throw new IndexOutOfBoundsException("Indices out of bounds.");
			}
			diag[i] = get(i, i);
		}

		return new Matrix<>(1, length, diag);
	}

	/**
	 * Retrieves the diagonal elements of the matrix as 2d array converted into
	 * Matrix.
	 * <p>
	 * This method returns the elements on the main diagonal of the matrix. It is
	 * applicable to both square and non-square matrices.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * matrix.diagonal().print(); // Retrieves the diagonal elements
	 * </pre>
	 * </p>
	 *
	 * @return A 1D array containing the diagonal elements.
	 * @throws IllegalArgumentException if the matrix is not square.
	 */
	public Matrix<T> diagonalMatrix() {
		Matrix<T> result = new Matrix<>(nrows, ncols, clazz);
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				if (i == j) {
					result.set(i, j, array[i * ncols + j]);
				} else {
					result.set(i, j, zeroValue());
				}
			}
		}
		return result;
	}

	/**
	 * Alias of @see {@link #diagonal()}
	 * 
	 * @return diagonal of the current matrix
	 */
	public Matrix<T> diagonal1D() {
		return diagonal();
	}

	/**
	 * Alias of @see {@link #diagonalMatrix()}
	 * 
	 * @return diagonal of the current matrix
	 */
	public Matrix<T> diagonal2D() {
		return diagonalMatrix();
	}

	/**
	 * Computes the cofactor matrix (also known as the comatrix) of the matrix.
	 * <p>
	 * The cofactor matrix adds input validation to ensure that the matrix is
	 * square, uses LU decomposition to calculate the determinant of the minor
	 * matrix, and uses the clazz field to create a new instance of the element type
	 * directly. LU decomposition is robust for it avoids to lead to overflow or
	 * underflow errors for very large or very small matrices.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> cofactorMatrix = matrix.comatrix(); // Computes the cofactor matrix
	 * </pre>
	 * </p>
	 *
	 * @return The cofactor matrix of the matrix.
	 */
	public Matrix<T> cofactorMatrix() {
		if (nrows != ncols) {
			throw new IllegalArgumentException("Matrix must be square.");
		}

		Matrix<T> comatrix = new Matrix<T>(nrows, ncols, clazz);

		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				Matrix<T> subMatrix = minor(i, j);
				T cofactor = (T) subMatrix.determinant();
				if ((i + j) % 2 == 0) {
					comatrix.set(i, j, cofactor);
				} else {
					comatrix.set(i, j, negate(cofactor));
				}
			}
		}

		return comatrix;
	}

	/**
	 * Alias of {@link #cofactorMatrix()}
	 * 
	 * @return the cofactor matrix of the current matrix
	 */
	public Matrix<T> comatrix() {
		return cofactorMatrix();
	}

	/**
	 * Calculates the determinant of a square matrix using the minor() method for
	 * computation.
	 * <p>
	 * This method computes the determinant of a square matrix using the recursive
	 * definition of determinant, which involves calculating the determinant of the
	 * minor matrices for each element in the first row or column.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * double determinant = matrix.determinant(); // Computes the determinant
	 * </pre>
	 * </p>
	 *
	 * @return The determinant of the matrix.
	 * @throws IllegalArgumentException if the matrix is not square.
	 */
	public T determinant() {
		// Check if the Matrix is square
		if (nrows != ncols) {
			throw new IllegalArgumentException("Matrix must be square.");
		}

		// If the Matrix is 1x1, return the single value
		if (nrows == 1) {
			return array[0];
		}

		// If the Matrix is 2x2, calculate the determinant directly
		if (nrows == 2) {
			T a = array[0], b = array[1], c = array[2], d = array[3];
			return subtract(mult(a, d), mult(b, c));
		}

		// For larger matrices, use the recursive definition of determinant
		T sum = zeroValue();
		T sign = oneValue();
		for (int i = 0; i < ncols; i++) {
			Matrix<T> subMatrix = minor(0, i);
			T subDet = subMatrix.determinant();
			sum = add(sum, mult(sign, mult(get(i), subDet)));
			sign = negate(sign);
		}

		return clazz.cast(sum);
	}

	/**
	 * Calculates the inverse of this matrix using the cofactor matrix method. The
	 * matrix must be square and non-singular (determinant != 0).
	 *
	 * <p>
	 * This method computes the inverse of a square matrix by first calculating the
	 * determinant and cofactor matrix (comatrix) of the current matrix. The inverse
	 * matrix is then obtained by taking the transpose of the comatrix and dividing
	 * each element by the determinant.
	 * </p>
	 *
	 * <p>
	 * The cofactor matrix is calculated using the {@link #cofactorMatrix()} method,
	 * and the determinant is calculated using the {@link #determinant()} method.
	 * </p>
	 *
	 * <p>
	 * Example usage:
	 *
	 * <pre>
	 * Matrix&lt;Double&gt; matrix = new Matrix&lt;&gt;(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix&lt;Double&gt; inverse = matrix.inverseViaComatrix(); // Computes the inverse
	 * </pre>
	 * </p>
	 *
	 * @return The inverse matrix.
	 * @throws IllegalArgumentException if the matrix is not square.
	 * @throws ArithmeticException      if the matrix is singular (determinant ==
	 *                                  0).
	 * @see #cofactorMatrix()
	 * @see #determinant()
	 */
	public Matrix<T> inverseUsingComatrix() {
		if (!isSquare()) {
			throw new IllegalArgumentException("Matrix must be square.");
		}
		T det = determinant();
		if (isSingular()) {
			throw new ArithmeticException("Matrix is singular (determinant = 0), cannot calculate inverse");
		}

		Matrix<T> cofactorMatrix = this.cofactorMatrix();
		Matrix<T> adjugateMatrix = cofactorMatrix.transpose();
		Matrix<T> inverseMatrix = new Matrix<>(nrows, ncols, clazz);

		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				inverseMatrix.set(i, j, divide(adjugateMatrix.get(i, j), det));
			}
		}

		return inverseMatrix;
	}

	/**
	 * Creates a permutation matrix based on the current matrix size, with a
	 * specified permutation of the rows.
	 * <p>
	 * In mathematics, particularly in matrix theory, a permutation matrix is a
	 * square binary matrix that has exactly one entry of 1 in each row and each
	 * column with all other entries 0. An n × n permutation matrix can represent a
	 * permutation of n elements. Pre-multiplying an n-row matrix M by a permutation
	 * matrix P, forming PM, results in permuting the rows of M, while
	 * post-multiplying an n-column matrix M, forming MP, permutes the columns of M.
	 * </p>
	 * <p>
	 * int[] permutation is this array which specifies the order in which the rows
	 * of the matrix should be rearranged. Each integer in the permutation array
	 * corresponds to the index of a row in the matrix. The value at each index in
	 * the permutation array indicates the new position of the row in the
	 * permutation matrix. For example, if permutation is [2, 0, 1], it means the
	 * first row of the original matrix becomes the third row in the permutation
	 * matrix, the second row becomes the first, and the third row becomes the
	 * second.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * int[] permutation = { 2, 0, 1 };
	 * Matrix<Double> permutationMatrix = Matrix.permutationMatrix(3, permutation); // Creates a permutation matrix
	 * </pre>
	 * </p>
	 *
	 * @param permutationRowPositions positions or indices where to set 1s
	 * @return The permutation matrix.
	 */
	public Matrix<T> permutationMatrix(int[] permutationRowPositions) {
		// Determine the size of the matrix based on the permutation array
		int size = permutationRowPositions.length;

		// Initialize a new matrix with the same size as the permutation array
		Matrix<T> permMatrix = new Matrix<>(size, size, clazz);

		// Fill the matrix with zeros
		Arrays.fill(permMatrix.getArray(), zeroValue());

		// Set the diagonal elements to 1 based on the permutation array
		for (int i = 0; i < size; i++) {
			int permIndex = permutationRowPositions[i];
			if (permIndex >= 0 && permIndex < size) {
				permMatrix.set(i, permIndex, oneValue());
			} else {
				throw new IllegalArgumentException("Invalid permutation index: " + permIndex);
			}
		}

		return permMatrix;
	}

	/**
	 * Creates a permutation matrix of the given size, with a specified permutation
	 * of the rows.
	 * <p>
	 * In mathematics, particularly in matrix theory, a permutation matrix is a
	 * square binary matrix that has exactly one entry of 1 in each row and each
	 * column with all other entries 0. An n × n permutation matrix can represent a
	 * permutation of n elements. Pre-multiplying an n-row matrix M by a permutation
	 * matrix P, forming PM, results in permuting the rows of M, while
	 * post-multiplying an n-column matrix M, forming MP, permutes the columns of M.
	 * </p>
	 * <p>
	 * int[] permutation is this array which specifies the order in which the rows
	 * of the matrix should be rearranged. Each integer in the permutation array
	 * corresponds to the index of a row in the matrix. The value at each index in
	 * the permutation array indicates the new position of the row in the
	 * permutation matrix. For example, if permutation is [2, 0, 1], it means the
	 * first row of the original matrix becomes the third row in the permutation
	 * matrix, the second row becomes the first, and the third row becomes the
	 * second.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * int[] permutation = { 2, 0, 1 };
	 * Matrix<Double> permutationMatrix = Matrix.permutationMatrix(3, permutation); // Creates a permutation matrix
	 * </pre>
	 * </p>
	 *
	 * @param size        The size of the matrix.
	 * @param permutation The positions or indices where to set 1s.
	 * @return The permutation matrix.
	 */
	public Matrix<T> permutationMatrix(int size, int[] permutation) {
		// Initialize a new matrix with the specified size
		Matrix<T> permMatrix = new Matrix<>(size, size, clazz);

		// Fill the matrix with zeros
		Arrays.fill(permMatrix.getArray(), zeroValue());

		// Check if the permutation array is valid for the given size
		if (permutation.length != size) {
			throw new IllegalArgumentException("The permutation array must have the same length as the matrix size.");
		}

		// Set the diagonal elements based on the permutation array
		for (int i = 0; i < size; i++) {
			int permIndex = permutation[i];
			if (permIndex >= 0 && permIndex < size) {
				permMatrix.set(i, permIndex, oneValue());
			} else {
				throw new IllegalArgumentException("Invalid permutation index: " + permIndex);
			}
		}

		return permMatrix;
	}

	/**
	 * Creates a permutation matrix of the given size.
	 * <p>
	 * In mathematics, particularly in matrix theory, a permutation matrix is a
	 * square binary matrix that has exactly one entry of 1 in each row and each
	 * column with all other entries 0.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> permutationMatrix = Matrix.permutationMatrix(3); // Creates a 3x3 permutation matrix
	 * </pre>
	 * </p>
	 *
	 * @return The permutation matrix.
	 */
	public Matrix<T> permutationMatrix(int size) {
		// Validate input
		if (size <= 0) {
			throw new IllegalArgumentException("Size must be positive.");
		}

		// Generate a random permutation of indices
		int[] permutationIndicesArray = new int[size];
		for (int i = 0; i < size; i++) {
			permutationIndicesArray[i] = i;
		}
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			int randomIndex = random.nextInt(size);
			int temp = permutationIndicesArray[i];
			permutationIndicesArray[i] = permutationIndicesArray[randomIndex];
			permutationIndicesArray[randomIndex] = temp;
		}

		// Create and return the permutation matrix
		return permutationMatrix(size, permutationIndicesArray);
	}

	/**
	 * alias {@link #permutationMatrix(int)}
	 * 
	 * @return permutationMatrix
	 */
	public Matrix<T> permutationMatrix() {
		// Validate input
		if (getNrows() != getNcols()) {
			throw new IllegalArgumentException("Matrix must be square.");
		}
		return permutationMatrix(getNrows());
	}

	/**
	 * Creates a permutation matrix of the given dimensions.
	 * <p>
	 * This method creates a permutation matrix with ones on the main diagonal and
	 * zeros elsewhere, where the number of rows and columns are specified.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> permutationMatrix = Matrix.permutationMatrix(3, 3); // Creates a 3x3 permutation matrix
	 * </pre>
	 * </p>
	 *
	 * @param nrows Number of rows.
	 * @param ncols Number of columns.
	 * @return The permutation matrix.
	 */
	public Matrix<T> permutationMatrix(int nrows, int ncols) {
		// Validate the input
		if (nrows <= 0 || ncols <= 0 || nrows != ncols) {
			throw new IllegalArgumentException("Number of rows and columns must be positive and equal.");
		}

		return permutationMatrix(nrows);
	}

	/*********************************
	 * MATRICES ADVANCED OPERATIONS
	 **********************************/

	/**
	 * Performs LU Decomposition on the current matrix.
	 * <p>
	 * LU Decomposition decomposes a square matrix into the product of a lower
	 * triangular matrix (L) and an upper triangular matrix (U). This method returns
	 * an array containing the L and U matrices.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double>[] luDecomposition = matrix.luDecomposition(); // Performs LU decomposition
	 * 
	 * Integer[][] intarr2 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
	 * Matrix<Integer> matintg2 = new Matrix<>(intarr2);
	 * Matrix<Integer>[] ludecompInt1 = matintg2.luDecomposition();
	 * ludecompInt1[0].print(); // L = [[1, 0, 0],[4, 1, 0],[7, 2, 1]]
	 * ludecompInt1[1].print(); // U = [[1, 2, 3],[0, -3, -6],[0, 0, 0]]
	 * </pre>
	 * </p>
	 *
	 * @return An array containing at position 0 the L(ower Triangular matrix) and
	 *         at position 1 the U(pper Triangular matrix).
	 * @throws IllegalArgumentException if the matrix is not square.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T>[] luDecomposition() {
		// Ensure square Matrix
		if (nrows != ncols) {
			throw new IllegalArgumentException("LU decomposition only supported for square matrices.");
		}

		// Create L and U matrices
		Matrix<T> L = new Matrix<>(nrows, ncols, this.clazz);
		Matrix<T> U = new Matrix<>(nrows, ncols, this.clazz);

		// Iterate through the Matrix
		for (int i = 0; i < nrows; i++) {
			// Upper Triangular
			for (int k = i; k < ncols; k++) {
				T sum = zeroValue();
				for (int j = 0; j < i; j++) {
					sum = add(sum, mult(L.get(i, j), U.get(j, k)));
				}
				U.set(i, k, subtract(get(i, k), sum));
			}

			// Lower Triangular
			for (int k = i; k < ncols; k++) {
				if (i == k) {
					L.set(i, i, oneValue());
				} else {
					T sum = zeroValue();
					for (int j = 0; j < i; j++) {
						sum = add(sum, mult(L.get(k, j), U.get(j, i)));
					}

					// Handle potential division by zero
					if (compareTo(U.get(i, i), zeroValue()) == 0) {
						throw new ArithmeticException("Division by zero during LU decomposition");
					}

					L.set(k, i, divide(subtract(get(k, i), sum), U.get(i, i)));
				}
			}
		}

		return new Matrix[] { L, U };
	}

	/**
	 * Performs LU factorization with partial pivoting on this matrix. Our class
	 * names this method pluFacotization or pluDecomposition. This Method decomposes
	 * the matrix into the product of a lower triangular matrix (L), an upper
	 * triangular matrix (U), and a permutation matrix (P). The decomposition is
	 * used for solving systems of linear equations, calculating determinants, and
	 * inverting matrices.
	 * <p>
	 * The method uses partial pivoting to improve numerical stability, especially
	 * for matrices that are close to being singular.
	 * </p>
	 * <p>
	 * Example usge :
	 * 
	 * <pre>
	 * // Create a Matrix instance for a 3x3 matrix of Doubles Matrix<Double> matrix
	 * = new Matrix<>(3, 3, Double.class); // Perform LU factorization with pivoting
	 * Matrix<Double>[] result = matrix.luFactorizationWithPivoting(); // Access the
	 * results Matrix<Double> L = result[0]; Matrix<Double> U = result[1];
	 * Matrix<Double> P = result[2];
	 * </pre>
	 * 
	 * <pre>
	 * // Create a Matrix instance for a 3x3 matrix of Doubles
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Perform LU factorization with pivoting
	 * Matrix<Double>[] result = matrix.luFactorizationWithPivoting();
	 * // Access the results
	 * Matrix<Double> L = result[0];
	 * Matrix<Double> U = result[1];
	 * Matrix<Double> P = result[2];
	 * // A = P*L*U
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @return An array containing three matrices: the lower triangular matrix (L),
	 *         the upper triangular matrix (U), and a permutation matrix (P).
	 * @throws IllegalArgumentException if the matrix is not square.
	 */
	public Matrix<T>[] luFactorizationWithPartialPivoting() {
		if (nrows != ncols) {
			throw new IllegalArgumentException("LU factorization requires a square matrix.");
		}

		Matrix<T> L = new Matrix<>(nrows, ncols, clazz);
		Matrix<T> U = new Matrix<>(nrows, ncols, clazz);
		Matrix<T> P = new Matrix<>(nrows, ncols, clazz);

		// Initialize L as an identity matrix, U as a copy of this matrix, and P as an
		// identity matrix
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				L.set(i, j, (i == j) ? oneValue() : zeroValue());
				U.set(i, j, get(i, j));
				P.set(i, j, (i == j) ? oneValue() : zeroValue());
			}
		}

		// Partial pivoting
		for (int k = 0; k < nrows - 1; k++) {
			// Find the maximum element in column k
			int maxIndex = k;
			for (int i = k + 1; i < nrows; i++) {
				if (compare(abs(get(i, k)), abs(get(maxIndex, k))) > 0) {
					maxIndex = i;
				}
			}

			// Swap rows k and maxIndex in U and P
			if (maxIndex != k) {
				for (int j = 0; j < ncols; j++) {
					T temp = U.get(k, j);
					U.set(k, j, U.get(maxIndex, j));
					U.set(maxIndex, j, temp);

					temp = P.get(k, j);
					P.set(k, j, P.get(maxIndex, j));
					P.set(maxIndex, j, temp);
				}
			}

			// Perform LU factorization
			for (int i = k + 1; i < nrows; i++) {
				T factor = divide(U.get(i, k), U.get(k, k));
				for (int j = k; j < ncols; j++) {
					U.set(i, j, subtract(U.get(i, j), mult(factor, U.get(k, j))));
				}
				for (int j = 0; j < nrows; j++) {
					L.set(i, j, (j < k) ? zeroValue() : (j == k) ? factor : L.get(i, j));
				}
			}
		}

		// Return L, U, and P as an array
		@SuppressWarnings("unchecked")
		Matrix<T>[] result = (Matrix<T>[]) new Matrix[] { L, U, P };
		return result;
	}

	/**
	 * alias of @see {@link #luFactorizationWithPartialPivoting()}
	 * 
	 * @return LUP, An array containing three matrices: the lower triangular matrix
	 *         (L), the upper triangular matrix (U), and a permutation matrix (P).
	 */
	public Matrix<T>[] lupDecomposition() {
		return luFactorizationWithPartialPivoting();
	}

	/**
	 * alias of @see {@link #luFactorizationWithPartialPivoting()}
	 * 
	 * @return LUP, An array containing three matrices: the lower triangular matrix
	 *         (L), the upper triangular matrix (U), and a permutation matrix (P).
	 */
	public Matrix<T>[] lupFactorization() {
		return luFactorizationWithPartialPivoting();
	}

	/**
	 * Performs QR decomposition using the Householder method.
	 * <p>
	 * The Householder method is a more complex but generally more stable and
	 * efficient way to perform QR decomposition compared to other methods like
	 * Givens or Gram-Schmidt. This method decomposes the matrix into an orthogonal
	 * matrix Q and an upper triangular matrix R and is applicable to both singular
	 * and non-singular matrices.
	 * </p>
	 * <p>
	 * For non-singular matrices, the decomposition allows for solving systems of
	 * linear equations, computing the determinant, and finding the inverse of the
	 * original matrix. For singular matrices, the decomposition can be used for
	 * solving least squares problems and finding the pseudo-inverse.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double>[] qrDecomposition = matrix.qrDecompositionHouseholder(); // Performs QR decomposition using
	 * 																		// Householder
	 * </pre>
	 * </p>
	 *
	 * @return An array containing at position 0 the Q matrix and at position 1 the
	 *         R matrix.
	 * @throws IllegalArgumentException      if the matrix is not square.
	 * @throws UnsupportedOperationException if the operation is not supported for
	 *                                       the given type.
	 * @throws ArithmeticException           if an arithmetic error occurs during
	 *                                       the computation.
	 */
	public Matrix<T>[] qrDecompositionByHouseholder() {
		if (nrows <= ncols) {
			return qrDecompositionHouseholder();
		}
		return qrDecompositionHouseholderReflections();
	}

	@SuppressWarnings("unchecked")
	private Matrix<T>[] qrDecompositionHouseholder() throws UnsupportedOperationException, ArithmeticException {
		// Ensure T is a numeric type
		if (!Number.class.isAssignableFrom(clazz) && !clazz.equals(BigDecimal.class)) {
			throw new IllegalArgumentException(
					"Matrix type must be a subclass of Number or BigDecimal for QR decomposition.");
		}

		// Initialize Q as an identity matrix and R as a copy of the original matrix
		Matrix<T> Q = new Matrix<>(nrows, ncols, clazz);
		Matrix<T> R = new Matrix<>(nrows, ncols, clazz);

		Q = this.identity(nrows, ncols); // identity matrix
		R = this.clone(); // copy or clone of original matrix

		// Householder QR decomposition
		for (int k = 0; k < ncols - 1; k++) {
			T[] x = (T[]) Array.newInstance(clazz, nrows - k);
			for (int i = k; i < nrows; i++) {
				x[i - k] = R.getArray()[(i * ncols) + k];
			}

			Matrix<T> xmat = new Matrix<>(x, 1);
			T[] e = (T[]) Array.newInstance(clazz, nrows - k);
			e[0] = oneValue();

			T alpha = zeroValue();
			if (compareTo(x[0], zeroValue()) < 0) {
				alpha = negate(oneValue());
			} else {
				alpha = xmat.norm2();
			}

			T[] u = (T[]) Array.newInstance(clazz, nrows - k);
			for (int i = 0; i < u.length; i++) {
				u[i] = add(x[i], mult(alpha, e[i]));
			}

			Matrix<T> umat = new Matrix<>(u, 1);
			T normU = umat.norm2();

			T[] v = (T[]) Array.newInstance(clazz, nrows - k);
			for (int i = 0; i < v.length; i++) {
				v[i] = divide(u[i], normU);
			}

			T[][] QMin = (T[][]) Array.newInstance(clazz, nrows - k, nrows - k);
			for (int i = 0; i < nrows - k; i++) {
				for (int j = 0; j < nrows - k; j++) {
					QMin[i][j] = (i == j) ? subtract(oneValue(), mult(mult(twoValue(), v[i]), v[j]))
							: mult(mult(negate(twoValue()), v[i]), v[j]);
				}
			}

			T[][] Qt = (T[][]) Array.newInstance(clazz, nrows, nrows);
			for (int i = 0; i < nrows; i++) {
				for (int j = 0; j < nrows; j++) {
					if (i < k || j < k) {
						Qt[i][j] = (i == j) ? oneValue() : zeroValue();
					} else {
						Qt[i][j] = QMin[i - k][j - k];
					}
				}
			}

			Matrix<T> Qtmat = new Matrix<>(Qt);

			if (k == 0) {
				try {
					Q = Qtmat;
					R = Qtmat.multiply(this);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					Q = Qtmat.multiply(Q);
					R = Qtmat.multiply(R);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}

		// Return Q and R as an array
		Matrix<T>[] result = (Matrix<T>[]) new Matrix[] { Q.fasttranspose(), R };
		return result;
	}

	@SuppressWarnings("unchecked")
	private Matrix<T>[] qrDecompositionHouseholderReflections() {
		if (nrows <= ncols) {
			throw new IllegalArgumentException("Matrix must have more rows than columns");
		}

		Matrix<T> qMatrix = new Matrix<>(nrows, nrows, clazz);
		Matrix<T> rMatrix = new Matrix<>(nrows, ncols, clazz);

		// Initialize R as a copy of this matrix
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				rMatrix.array[i * ncols + j] = array[i * ncols + j];
			}
		}

		// Initialize Q as the identity matrix
		for (int i = 0; i < nrows; i++) {
			qMatrix.array[i * nrows + i] = oneValue();
		}

		for (int k = 0; k < ncols; k++) {
			T[] x = (T[]) new Object[nrows - k];
			T sigma = zeroValue();

			// Copy the remaining submatrix into x
			for (int i = k; i < nrows; i++) {
				x[i - k] = rMatrix.array[i * ncols + k];
				sigma = add(sigma, mult(x[i - k], x[i - k]));
			}

			sigma = squareRoot(sigma);
			T mu;
			if (compareTo(x[0], zeroValue()) < 0) {
				x[0] = negate(sigma);
			} else {
				x[0] = add(x[0], sigma);
			}
			mu = divide(zeroValue(), sqrt(mult(sigma, add(sigma, abs(x[0])))));

			// Apply the Householder transformation
			for (int i = k; i < nrows; i++) {
				T sum = zeroValue();
				for (int j = k; j < nrows; j++) {
					sum = add(sum, mult(x[j - k], rMatrix.array[j * ncols + i]));
				}
				sum = mult(mu, sum);
				for (int j = k; j < nrows; j++) {
					rMatrix.array[j * ncols + i] = subtract(rMatrix.array[j * ncols + i], mult(x[j - k], sum));
				}
			}

			for (int i = 0; i < nrows; i++) {
				T sum = zeroValue();
				for (int j = k; j < nrows; j++) {
					sum = add(sum, mult(qMatrix.array[i * nrows + j], x[j - k]));
				}
				sum = mult(mu, sum);
				for (int j = k; j < nrows; j++) {
					qMatrix.array[i * nrows + j] = subtract(qMatrix.array[i * nrows + j], mult(x[j - k], sum));
				}
			}
		}

		return new Matrix[] { qMatrix, rMatrix };
	}

	/**
	 * Performs QR decomposition using the Givens method.
	 * <p>
	 * The Givens method is a simple and intuitive method for performing QR
	 * decomposition. However, it can be numerically unstable, especially for
	 * matrices with small or large off-diagonal elements. This method decomposes
	 * the matrix into an orthogonal matrix Q and an upper triangular matrix R.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double>[] qrDecomposition = matrix.qrDecompositionGivens(); // Performs QR decomposition using Givens
	 * </pre>
	 * </p>
	 *
	 * @return An array containing at position 0 the Q matrix and at position 1 the
	 *         R matrix.
	 * @throws Exception
	 * @throws IllegalArgumentException      if the matrix is not square.
	 * @throws UnsupportedOperationException if the operation is not supported for
	 *                                       the given type.
	 * @throws ArithmeticException           if an arithmetic error occurs during
	 *                                       the computation.
	 */
//	public Matrix<T>[] qrDecompositionGivens() throws Exception {
	@SuppressWarnings("unchecked")
	public Matrix<T>[] qrDecompositionGivens() {
		T[][] A = to2DArray();
		int m = A.length;
		int n = A[0].length;
		T[][] Q = (T[][]) Array.newInstance(clazz, m, m);
		for (int i = 0; i < m; i++) {
			Q[i][i] = oneValue();
		}
		T[][] R = this.clone().to2DArray();

		for (int j = 0; j < n; j++) {
			for (int i = m - 1; i > j; i--) {
				T a = R[i - 1][j];
				T b = R[i][j];
				T r = sqrt(add(mult(a, a), mult(b, b)));
				T c = divide(a, r);
				T s = divide(negate(b), r);

				for (int k = 0; k < n; k++) {
					T temp = R[i - 1][k];
					R[i - 1][k] = subtract(mult(c, R[i - 1][k]), mult(s, R[i][k]));
					R[i][k] = add(mult(s, temp), mult(c, R[i][k]));
				}

				for (int k = 0; k < m; k++) {
					T temp = Q[k][i - 1];
					Q[k][i - 1] = subtract(mult(c, Q[k][i - 1]), mult(s, Q[k][i]));
					Q[k][i] = add(mult(s, temp), mult(c, Q[k][i]));
				}
			}
		}

		Matrix<T>[] result = (Matrix<T>[]) new Matrix[] { new Matrix<>(Q), new Matrix<>(R) };
		return result;
	}

	/**
	 * Performs QR decomposition using the Gram-Schmidt method.
	 * <p>
	 * The Gram-Schmidt method is a classical method for orthonormalizing a set of
	 * vectors in an inner product space, most commonly the Euclidean space R^n. It
	 * is particularly efficient for sparse matrices because it allows for early
	 * termination when processing zeros. This method decomposes the matrix into an
	 * orthogonal matrix Q and an upper triangular matrix R.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double>[] qrDecomposition = matrix.qrDecompositionGramSchmidt(); // Performs QR decomposition using
	 * 																		// Gram-Schmidt
	 * </pre>
	 * </p>
	 *
	 * @return An array containing at position 0 the Q matrix and at position 1 the
	 *         R matrix.
	 * @throws IllegalArgumentException      if the matrix is not square.
	 * @throws UnsupportedOperationException if the operation is not supported for
	 *                                       the given type.
	 * @throws ArithmeticException           if an arithmetic error occurs during
	 *                                       the computation.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T>[] qrDecompositionGramSchmidt() throws UnsupportedOperationException, ArithmeticException {
		// Ensure T is a numeric type
		if (!Number.class.isAssignableFrom(clazz) && !clazz.equals(BigDecimal.class)) {
			throw new IllegalArgumentException(
					"Matrix type must be a subclass of Number or BigDecimal for QR decomposition.");
		}

		T[][] A = to2DArray();
		int m = A.length;
		int n = A[0].length;

		T[][] Q = (T[][]) Array.newInstance(clazz, m, n);
		T[][] R = (T[][]) Array.newInstance(clazz, n, n);

		// Gram-Schmidt process
		for (int j = 0; j < n; j++) {
			for (int i = 0; i < j; i++) {
				T dotProduct = zeroValue();
				for (int k = 0; k < m; k++) {
					T qElement = Q[k][i];
					T aElement = A[k][j];
					if (qElement == null || aElement == null) {
						continue;
					}
					dotProduct = add(dotProduct, mult(qElement, aElement));
				}
				R[i][j] = dotProduct;
				for (int k = 0; k < m; k++) {
					T aElement = A[k][j];
					T rElement = R[i][j];
					T qElement = Q[k][i];
					if (aElement == null || rElement == null || qElement == null) {
						continue;
					}
					A[k][j] = subtract(aElement, mult(rElement, qElement));
				}
			}

			T norm = zeroValue();
			for (int k = 0; k < m; k++) {
				T aElement = A[k][j];
				if (aElement == null) {
					continue;
				}
				norm = add(norm, mult(aElement, aElement));
			}
			norm = sqrt(norm);
			R[j][j] = norm;

			for (int k = 0; k < m; k++) {
				T aElement = A[k][j];
				if (aElement == null) {
					Q[k][j] = zeroValue();
				} else {
					Q[k][j] = divide(aElement, norm);
				}
			}
		}

		// replace null values by zeros
		for (int i = 0; i < R.length; i++) {
			for (int j = 0; j < R[i].length; j++) {
				if (R[i][j] == null) {
					R[i][j] = zeroValue();
				}
				if (Q[i][j] == null) {
					Q[i][j] = zeroValue();
				}
			}
		}

		return new Matrix[] { new Matrix<>(Q), new Matrix<>(R) };
	}

	/**
	 * Performs Cholesky L (lower triangular) decomposition on the current matrix.
	 * <p>
	 * This method decomposes a symmetric, positive-definite matrix into the product
	 * of a lower triangular matrix L and its conjugate transpose. This
	 * decomposition is particularly efficient for numerical computation.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values and is symmetric,
	 * // positive-definite
	 * Matrix<Double> L = matrix.choleskyLDecomposition(); // Performs Cholesky L decomposition
	 * // after decomposition, verify if <b>A = L.multiply(L.fastranspose())</b>
	 * </pre>
	 * </p>
	 *
	 * @return The lower triangular matrix L.
	 * @throws IllegalArgumentException if the matrix is not square.
	 */
	public Matrix<T> choleskyDecomposition() {
		if (nrows != ncols) {
			throw new IllegalArgumentException("Cholesky decomposition only supported for square matrices.");
		}

		// Check if the matrix is positive definite
		for (int i = 0; i < nrows; i++) {
			if (compare(get(i, i), zeroValue()) <= 0) {
				throw new IllegalArgumentException("Matrix is not positive definite.");
			}
		}

		Matrix<T> L = new Matrix<>(nrows, ncols, this.clazz);

		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j <= i; j++) {
				T sum = (T) zeroValue();

				if (j == i) { // Diagonal elements
					for (int k = 0; k < j; k++) {
						sum = add(sum, mult(L.get(j, k), L.get(j, k)));
					}
					// Ensure the square root operation is valid
					T diff = subtract(get(j, j), sum);
					if (compare(diff, zeroValue()) < 0) {
						throw new ArithmeticException("Cannot take the square root of a negative number.");
					}
					L.set(j, j, sqrt(diff));
				} else { // Off-diagonal elements
					for (int k = 0; k < j; k++) {
						sum = add(sum, mult(L.get(i, k), L.get(j, k)));
					}
					// Ensure division by zero is avoided
					if (compare(L.get(j, j), zeroValue()) == 0) {
						throw new ArithmeticException("Division by zero.");
					}
					L.set(i, j, divide(subtract(get(i, j), sum), L.get(j, j)));
				}
			}
		}

		return L;
	}

	/**
	 * Performs QR decomposition on the matrix by specifying the method name to use.
	 * <p>
	 * This method decomposes a matrix into the product of an orthogonal matrix Q
	 * and an upper triangular matrix R. The decomposition method can be specified
	 * as "Householder", "Givens", or "GramScmidt".
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double>[] qr = matrix.qrDecomposition("Householder"); // Performs QR decomposition using Householder
	 * Matrix<Double>[] Q = qr[0]; // get Q
	 * Matrix<Double>[] R = qr[1]; // get R
	 * </pre>
	 * </p>
	 *
	 * @param qrMethodName "classic" | "Householder" | "Givens" | "GramScmidt"
	 * @return An array of two matrices [Q, R] where Q is an orthogonal matrix and R
	 *         is an upper triangular matrix.
	 * @throws Exception                     if the specified decomposition method
	 *                                       is not supported or does not exist.
	 * @throws UnsupportedOperationException if the matrix has more columns than
	 *                                       rows.
	 */
	public Matrix<T>[] qrDecomposition(String qrMethodName) throws Exception {
		if (qrMethodName.equalsIgnoreCase("Householder")) {
			return qrDecompositionHouseholder();
		} else if (qrMethodName.equalsIgnoreCase("Givens")) {
			return qrDecompositionGivens();
		} else if (qrMethodName.equalsIgnoreCase("GramScmidt")) {
			return qrDecompositionGramSchmidt();
		} else {
			throw new Exception(qrMethodName + " not yet implemented or doesn't exist. Check a linear algebra book.");
		}
	}

	/**
	 * Performs back substitution on the given upper triangular matrix and
	 * right-hand side vector.
	 * <p>
	 * Back substitution is used to solve systems of linear equations that have been
	 * transformed into an upper triangular form. This method iterates from the
	 * bottom row up, solving for each variable in turn.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> A = new Matrix<>(3, 3);
	 * Matrix<Double> b = new Matrix<>(3, 1);
	 * // Assume A and b are initialized with Double values
	 * Matrix<Double> x = Matrix.backSubstitution(A, b); // Solves for x in Ax = b
	 * </pre>
	 * </p>
	 *
	 * @param A The upper triangular matrix.
	 * @param b The right-hand side vector.
	 * @return The solution vector.
	 * @throws IllegalArgumentException if the matrix dimensions do not match the
	 *                                  right-hand side vector.
	 */
	public Matrix<T> backwardSubstitution(Matrix<T> A, Matrix<T> b) {

		// Ensure T is a numeric type
		if (!Number.class.isAssignableFrom(clazz) && !clazz.equals(BigDecimal.class)) {
			throw new IllegalArgumentException("Matrix type must be a subclass of Number for LU decomposition.");
		}

		int size = A.getNrows();
		Matrix<T> x = new Matrix<>(size, 1, this.clazz);

		for (int i = size - 1; i >= 0; i--) {
			T sum = (T) zeroValue();
			for (int j = i + 1; j < size; j++) {
				sum = add(sum, mult(A.get(i, j), x.get(j, 0)));
			}
			x.set(i, 0, divide(subtract(b.get(i, 0), sum), A.get(i, i)));
		}

		return x;
	}

	/**
	 * Performs forward substitution on the given lower triangular matrix and
	 * right-hand side vector.
	 * <p>
	 * Forward substitution is used to solve systems of linear equations that have
	 * been transformed into a lower triangular form. This method iterates from the
	 * top row down, solving for each variable in turn.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> A = new Matrix<>(3, 3);
	 * Matrix<Double> b = new Matrix<>(3, 1);
	 * // Assume A and b are initialized with Double values
	 * Matrix<Double> x = Matrix.forwardSubstitution(A, b); // Solves for x in Ax = b
	 * </pre>
	 * </p>
	 *
	 * @param A The lower triangular matrix.
	 * @param b The right-hand side vector.
	 * @return The solution vector.
	 * @throws IllegalArgumentException if the matrix dimensions do not match the
	 *                                  right-hand side vector.
	 */
	public Matrix<T> forwardSubstitution(Matrix<T> A, Matrix<T> b) {

		// Ensure T is a numeric type
		if (!Number.class.isAssignableFrom(clazz) && !clazz.equals(BigDecimal.class)) {
			throw new IllegalArgumentException("Matrix type must be a subclass of Number for LU decomposition.");
		}

		int size = A.getNrows();
		Matrix<T> x = new Matrix<>(size, 1, this.clazz);

		for (int i = 0; i < size; i++) {
			T sum = (T) zeroValue();
			for (int j = 0; j < i; j++) {
				sum = add(sum, mult(A.get(i, j), x.get(j, 0)));
			}
			x.set(i, 0, divide(subtract(b.get(i, 0), sum), A.get(i, i)));
		}

		return x;
	}

	/**
	 * Calculates the inverse of this matrix using LU decomposition.
	 * <p>
	 * This method uses LU decomposition to decompose the matrix into a lower
	 * triangular matrix L and an upper triangular matrix U. Then, for each column
	 * of the inverse matrix, it solves the system of equations Ax = e where e is a
	 * unit vector with 1 at the position corresponding to the column. It does this
	 * by first solving the system Ly = e by forward substitution, then solving the
	 * system Ux = y by backward substitution.
	 * </p>
	 * 
	 * @return The inverse of this matrix.
	 * @throws ArithmeticException If the matrix is singular (i.e., its determinant
	 *                             is zero), an ArithmeticException is thrown.
	 */
	public Matrix<T> inverseLU() {
		int n = nrows;
		if (n != ncols) {
			throw new ArithmeticException("Matrix must be square to calculate inverse");
		}

		Matrix<T>[] lu = luDecomposition(); // Assume this method returns an array of [L, U]
		Matrix<T> L = lu[0];
		Matrix<T> U = lu[1];

		Matrix<T> inverse = new Matrix<>(n, n, clazz);
		for (int i = 0; i < n; i++) {
			Matrix<T> e = new Matrix<>(n, 1, clazz); // Unit vector with 1 at the i-th position
			e.set(i, 0, oneValue());
			Matrix<T> y = forwardSubstitution(L, e); // Solve Ly = e
			Matrix<T> x = backwardSubstitution(U, y); // Solve Ux = y
			inverse.setColumn(i, x); // Set the i-th column of the inverse matrix
		}

		return inverse;
	}

	/**
	 * Normalizes a column vector.
	 * <p>
	 * This method computes the norm of the given column vector and then divides
	 * each element of the vector by its norm, resulting in a unit vector (a vector
	 * of length 1) pointing in the same direction as the original vector.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> column = new Matrix<>(3, 1);
	 * // Assume column is initialized with Double values
	 * Matrix<Double> normalizedColumn = matrix.normalize(column); // Normalizes the column vector
	 * </pre>
	 * </p>
	 *
	 * @param column The column vector to normalize.
	 * @return The normalized column vector.
	 * @throws UnsupportedOperationException if the type is not supported.
	 */
	public Matrix<T> normalize(Matrix<T> column) {
		T norm = zeroValue();
		for (int i = 0; i < column.getNrows(); i++) {
			norm = add(norm, mult(column.get(i, 0), column.get(i, 0)));
		}
		norm = squareRoot(norm);
		for (int i = 0; i < column.getNrows(); i++) {
			column.set(i, 0, divide(column.get(i, 0), norm));
		}
		return column;
	}

	/**
	 * Solves a system of linear equations represented by the matrix equation Ax =
	 * b.
	 * <p>
	 * This method solves the system of linear equations by performing either QR
	 * decomposition or LU decomposition, followed by either back substitution or
	 * forward substitution, depending on the specified decomposition method and
	 * substitution flag.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> A = new Matrix<>(3, 3);
	 * Matrix<Double> b = new Matrix<>(3, 1);
	 * // Assume A and b are initialized with Double values
	 * Matrix<Double> x = matrix.solveEquation(A, b, "QR", true); // Solves Ax = b using QR decomposition and back
	 * 															// substitution
	 * </pre>
	 * </p>
	 *
	 * @param A                       The matrix A in the equation.
	 * @param b                       The right-hand side vector in the equation.
	 * @param decompositionMethodName The name of the decomposition method to use
	 *                                ("QR" or "LU").
	 * @param substitution            true for back substitution, false for forward
	 *                                substitution.
	 * @return The solution vector x.
	 * @throws UnsupportedOperationException if the decomposition method is not
	 *                                       supported.
	 * @throws IllegalArgumentException      if the decomposition method name is
	 *                                       invalid.
	 */
	public Matrix<T> solveEquation(Matrix<T> A, Matrix<T> b, String decompositionMethodName, boolean substitution) {
		// Check if A and b are compatible
//      if (A.nrows != b.ncols) {
//          throw new IllegalArgumentException("Matrix A and vector (column-vector) b must have the same number of rows.");
//      }
		// Perform QR decomposition or LU decomposition based on the method name
		if (decompositionMethodName.equals("QR")) {
			// Perform QR decomposition
			A.qrDecompositionHouseholder(); // the more efficient and stable.
		} else if (decompositionMethodName.equals("LU")) {
			// Perform LU decomposition
			A.luDecomposition();
		} else {
			throw new IllegalArgumentException("Invalid decomposition method name. Only 'QR' and 'LU' are supported.");
		}

		// Solve the system of linear equations using back substitution or forward
		// substitution
		if (substitution) {
			// Assuming you have a method backSubstitution() that performs back substitution
			return backwardSubstitution(A, b);
		} else {
			// Assuming you have a method forwardSubstitution() that performs forward
			// substitution
			return forwardSubstitution(A, b);
		}
	}

	/**
	 * Calculates the determinant of the matrix using the Householder QR
	 * decomposition method.
	 * <p>
	 * This method computes the determinant of a square matrix by first performing
	 * the Householder QR decomposition, which decomposes the matrix into an
	 * orthogonal matrix Q and an upper triangular matrix R. The determinant is then
	 * calculated by multiplying the diagonal elements of the upper triangular
	 * matrix R.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * double determinant = matrix.determinantByHouseholderQR(); // Computes the determinant using Householder QR
	 * </pre>
	 * </p>
	 *
	 * @return The determinant of the matrix.
	 * @throws RuntimeException if the matrix is not square.
	 */
	public T determinantByHouseholderQR() throws RuntimeException {
		if (nrows != ncols) {
			throw new RuntimeException("Matrix must be square.");
		}
		// Perform QR decomposition
		Matrix<T>[] qrMatrices = this.qrDecompositionHouseholder();
		Matrix<T> upperTriangular = qrMatrices[1];

		// Start with the identity value for multiplication
		T det = oneValue();

		// Multiply the diagonal elements
		for (int i = 0; i < nrows; i++) {
			det = mult(det, upperTriangular.get(i, i));
		}

		return det;
	}

	/**
	 * Calculates the determinant of the matrix using the Gram-Schmidt QR
	 * decomposition method.
	 * <p>
	 * This method computes the determinant of a square matrix by first performing
	 * the Gram-Schmidt QR decomposition, which decomposes the matrix into an
	 * orthogonal matrix Q and an upper triangular matrix R. The determinant is then
	 * calculated by multiplying the diagonal elements of the upper triangular
	 * matrix R.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * double determinant = matrix.determinantByQRGramSchmidt(); // Computes the determinant using Gram-Schmidt QR
	 * </pre>
	 * </p>
	 *
	 * @return The determinant of the matrix.
	 * @throws RuntimeException if the matrix is not square.
	 */
	public T determinantByQRGramSchmidt() throws RuntimeException {
		if (nrows != ncols) {
			throw new RuntimeException("Matrix must be square.");
		}
		// Perform QR decomposition
		Matrix<T>[] qrMatrices = this.qrDecompositionGramSchmidt();
		Matrix<T> upperTriangular = qrMatrices[1];

		// Start with the identity value for multiplication
		T det = oneValue();

		// Multiply the diagonal elements
		for (int i = 0; i < nrows; i++) {
			det = mult(det, upperTriangular.get(i, i));
		}

		return det;
	}

	/**
	 * Calculates the determinant of the matrix using the Givens QR decomposition
	 * method.
	 * <p>
	 * This method computes the determinant of a square matrix by first performing
	 * the Givens QR decomposition, which decomposes the matrix into an orthogonal
	 * matrix Q and an upper triangular matrix R. The determinant is then calculated
	 * by multiplying the diagonal elements of the upper triangular matrix R.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * double determinant = matrix.determinantByQRGivens(); // Computes the determinant using Givens QR
	 * </pre>
	 * </p>
	 *
	 * @return The determinant of the matrix.
	 * @throws RuntimeException if the matrix is not square.
	 */
	public T determinantByQRGivens() throws RuntimeException {
		if (nrows != ncols) {
			throw new RuntimeException("Matrix must be square.");
		}
		// Perform QR decomposition
		Matrix<T>[] qrMatrices = null;
		try {
			qrMatrices = this.qrDecompositionGivens();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Matrix<T> upperTriangular = qrMatrices[1];

		// Start with the identity value for multiplication
		T det = oneValue();

		// Multiply the diagonal elements
		for (int i = 0; i < nrows; i++) {
			det = mult(det, upperTriangular.get(i, i));
		}

		return det;
	}

	/**
	 * Inverts the matrix using the Gauss-Jordan elimination method.
	 * <p>
	 * This method computes the inverse of a square matrix by augmenting the
	 * original matrix with the identity matrix and then applying Gauss-Jordan
	 * elimination to transform the left side of the augmented matrix into the
	 * identity matrix.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> inverseMatrix = matrix.inverseGaussJordan(); // Computes the inverse using Gauss-Jordan
	 * 															// elimination
	 * </pre>
	 * </p>
	 *
	 * @return The inverse matrix.
	 * @throws RuntimeException if the matrix is not square or singular.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> inverseGaussJordan() {
		if (nrows != ncols) {
			throw new IllegalArgumentException("Matrix must be square for inversion.");
		}

		// Identity Matrix of the same size as the original Matrix
		Matrix<T> identity = identity(nrows);

		// Augment the original Matrix with the identity Matrix
		Matrix<T> augmentedMatrix = this.concatenateHorizontally(identity);

		// Apply Gauss-Jordan elimination to transform the left side into the identity
		// Matrix
		for (int i = 0; i < nrows; i++) {
			// Find pivot
			int pivotRow = i;
			for (int j = i + 1; j < nrows; j++) {
				if (compare(augmentedMatrix.get(j, i), augmentedMatrix.get(pivotRow, i)) > 0) {
					pivotRow = j;
				}
			}

			// Swap rows to make the pivot the current row
			if (pivotRow != i) {
				augmentedMatrix.swapRows(i, pivotRow);
			}

			// Scale the pivot row to make the pivot element equal to 1
			T pivotValue = augmentedMatrix.get(i, i);
			if (compare(pivotValue, zeroValue()) == 0) {
				throw new ArithmeticException("Matrix is singular.");
			}
			for (int j = 0; j < ncols * 2; j++) {
				augmentedMatrix.set(i, j, divide(augmentedMatrix.get(i, j), pivotValue));
			}

			// Eliminate other rows to make their i-th column elements equal to 0
			for (int j = 0; j < nrows; j++) {
				if (j != i) {
					T factor = augmentedMatrix.get(j, i);
					for (int k = 0; k < ncols * 2; k++) {
						T newValue = subtract(augmentedMatrix.get(j, k), mult(factor, augmentedMatrix.get(i, k)));
						augmentedMatrix.set(j, k, newValue);
					}
				}
			}
		}

		// Extract the inverted Matrix from the right side of the augmented
		// Matrix
		Matrix<T> invertedMatrix = new Matrix<>(nrows, ncols, this.clazz);
		for (int i = 0; i < nrows; i++) {
			System.arraycopy(augmentedMatrix.getArray(), i * ncols + ncols, invertedMatrix.getArray(), i * ncols,
					ncols);
		}

		return invertedMatrix;
	}
	
	/**
	 * Computes the pseudo-inverse of the matrix using Singular Value Decomposition (SVD).
	 * <p>
	 * The pseudo-inverse is calculated by performing SVD on the matrix, inverting the singular values matrix,
	 * and then multiplying the result by the transpose of the V matrix and the transpose of the U matrix.
	 * This method is useful for solving systems of linear equations when the matrix is not square or is singular.
	 * </p>
	 * <p>
	 * This method assumes that the SVD decomposition has been performed and that the matrix is non-singular.
	 * </p>
	 *
	 * @param b
	 * @param lambda
	 * @return The pseudo-inverse matrix of the original matrix.
	 * @throws Exception 
	 * @throws IllegalArgumentException if the matrix is singular or if the SVD decomposition has not been performed.
	 */
	public Matrix<T> ridgeRegression(Matrix<T> b, T lambda) throws Exception {
	    // Step 1: Calculate X^T * X + lambda * I
	    Matrix<T> XtX = this.transpose().multiply(this);
	    Matrix<T> lambdaI = new Matrix<>(clazz);
	    lambdaI = lambdaI.identity(nrows).scale(lambda);
	    Matrix<T> A = XtX.plus(lambdaI);

	    // Step 2: Calculate X^T * b
	    Matrix<T> XtB = this.transpose().multiply(b);

	    // Step 3: Solve the system of equations A * w = X^T * b for w
	    Matrix<T> w = A.solveLU(XtB);

	    return w;
	}

	/**
	 * Performs the Jacobi eigenvalues decomposition iterative and basic algorithm.
	 * <p>
	 * This method computes the eigenvalues of the matrix using the Jacobi
	 * eigenvalues decomposition. It iteratively refines the matrix until
	 * convergence is reached or the maximum number of iterations is exceeded.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> eigendecomposition = matrix.eigenvalueDecomposition(); // Computes the eigenvalues
	 * Double[][] eigenValuesVectorsArray = matrix.eigenvalueDecomposition().to2DArray(); // Computes the eigenvalues
	 * Double[] eigenvalues = eigenValuesVectorsArray[0]; // get the eigenvalues
	 * Double[] eigenvectors = eigenValuesVectorsArray[1]; // get the eigenvectors
	 * </pre>
	 * </p>
	 *
	 * @return The eigenvalues as a matrix.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> eigenvalueDecomposition() {
		int dim = nrows;

		// Working Matrix M starts as a copy of A
		Number[][] M = new Number[dim][dim];
		for (int i = 0; i < dim; i++) {
			System.arraycopy(array, i * dim, M[i], 0, dim);
		}

		// These values affect performances. Be careful!
		double tolerance = 1e-10; // can be change for better results and improvements
		int maxIterations = 1000; // can be increased for more accuracy
		int counter = 0;

		// Main loop
		while (!converged(M, tolerance) && counter <= maxIterations) {
			// Identify largest off-diagonal element
			int idxI = -1, idxJ = -1;
			double maxOffDiag = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < dim; i++) {
				for (int j = i + 1; j < dim; j++) {
					double currVal = Math.abs(M[i][j].doubleValue());
					if (currVal > maxOffDiag) {
						idxI = i;
						idxJ = j;
						maxOffDiag = currVal;
					}
				}
			}

			// Break early if all off-diagonal elements close enough to zero
			if (maxOffDiag < tolerance) {
				break;
			}

			// Calculate rotation angles
			double diff = M[idxI][idxI].doubleValue() - M[idxJ][idxJ].doubleValue();
			double cosTheta = diff
					/ Math.sqrt(diff * diff + 4 * M[idxI][idxJ].doubleValue() * M[idxI][idxJ].doubleValue());
			double sinTheta = -Math.signum(M[idxI][idxJ].doubleValue()) * Math.sqrt(1 - cosTheta * cosTheta);

			// Apply rotation
			for (int k = 0; k < dim; k++) {
				double tmp = M[k][idxI].doubleValue();
				M[k][idxI] = cosTheta * tmp - sinTheta * M[k][idxJ].doubleValue();
				M[k][idxJ] = sinTheta * tmp + cosTheta * M[k][idxJ].doubleValue();
			}

			counter++;
		}

		// Results
		Number[] eigValues = new Number[dim];
		Number[][] eigVecs = new Number[dim][dim];

		// Assign eigenvalues
		for (int i = 0; i < dim; i++) {
			eigValues[i] = M[i][i];
		}

		// Assign eigenvectors from M
		for (int i = 0; i < dim; i++) {
			eigVecs[i] = Arrays.copyOfRange(M[i], 0, dim);
		}

		// Normalize eigenvectors
		for (int i = 0; i < dim; i++) {
			double len = 0.0;
			for (int j = 0; j < dim; j++) {
				len += eigVecs[i][j].doubleValue() * eigVecs[i][j].doubleValue();
			}
			len = Math.sqrt(len);
			for (int j = 0; j < dim; j++) {
				eigVecs[i][j] = eigVecs[i][j].doubleValue() / len;
			}
		}

		// Return the eigenvalue decomposition as a 2D array
		T[][] eigenDecomposition = (T[][]) new Number[2][];
		eigenDecomposition[0] = (T[]) eigValues;
		eigenDecomposition[1] = (T[]) Arrays.stream(eigVecs).flatMap(Arrays::stream).toArray(Number[]::new); // convert
																												// 2D
		// array to 1D
		// easily

		return new Matrix<>(eigenDecomposition, this.clazz);
	}

	/**
	 * Computes the eigenvalues of the matrix.
	 * <p>
	 * This method calculates the eigenvalues of a square matrix by performing a QR
	 * decomposition and then using the diagonal elements of the resulting matrix.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Double[] eigenvalues = matrix.eigenvalues(); // Computes the eigenvalues
	 * </pre>
	 * </p>
	 *
	 * @return A 1D array containing the eigenvalues.
	 * @throws IllegalArgumentException if the matrix is not square.
	 */
	public T[] eigenvalues() {
		if (nrows != ncols) {
			throw new IllegalArgumentException("Matrix must be square.");
		}
		Matrix<T>[] qr = this.qrDecompositionHouseholder();
		Matrix<T> Q = qr[0];
		Matrix<T> R = qr[1];
		Matrix<T> A = null;
		try {
			A = R.multiply(Q);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// The eigenvalues are the diagonal elements of A_next
		return A.diag();
	}

	/**
	 * Computes the eigenvectors of the matrix.
	 * <p>
	 * This method calculates the eigenvectors of a square matrix by performing a QR
	 * decomposition and then extracting the columns of the resulting matrix Q,
	 * which represent the eigenvectors.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Double> matrix = new Matrix<>(3, 3, Double.class);
	 * // Assume matrix is initialized with Double values
	 * Matrix<Double> eigenvectors = matrix.eigenvectors(); // Computes the eigenvectors
	 * </pre>
	 * </p>
	 *
	 * @return A 2D array where each row is an eigenvector.
	 * @throws IllegalArgumentException if the matrix is not square.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> eigenvectors() {
		if (nrows != ncols) {
			throw new IllegalArgumentException("Matrix must be square.");
		}
		Matrix<T>[] qr = this.qrDecompositionHouseholder();
		Matrix<T> Q = qr[0];
//          Matrix<T> R = qr[1];
//          Matrix<T> A_next = R.multiply(Q);
		// The eigenvectors are the columns of Q
		T[][] eigenvectors = (T[][]) new Number[ncols][];
		for (int i = 0; i < ncols; i++) {
			eigenvectors[i] = Q.getColumn(i).to1DArray();
		}
		return new Matrix<>(eigenvectors, this.clazz);
	}

	/**
	 * Method. Check matrix convergence (Jacobi eigenvalues) using off-diagonal
	 * element norm as tolerance (e.g., 1e-10). Choose tolerance carefully: too low
	 * = slow/no convergence, too high = inaccurate results. Experiment for optimal
	 * balance. (299 chars)
	 *
	 * @param M         The matrix to check for convergence.
	 * @param tolerance The tolerance within which the norm of the off-diagonal
	 *                  elements should fall for convergence.
	 * @return true if the norm of the off-diagonal elements is less than the
	 *         tolerance, false otherwise.
	 */
	/**
	 * Checks matrix convergence using off-diagonal element norm as tolerance.
	 * <p>
	 * This method evaluates the convergence of the matrix based on the norm of its
	 * off-diagonal elements. Convergence is determined if the norm of the
	 * off-diagonal elements falls below a specified tolerance. It is based on the
	 * Jacobi eigenvalues. Choose tolerance carefully: too low => slow/no
	 * convergence, too high => inaccurate results. Experiment for optimal balance
	 * </p>
	 *
	 * @param M         The matrix to check for convergence.
	 * @param tolerance The tolerance within which the norm of the off-diagonal
	 *                  elements should fall for convergence.
	 * @return true if the norm of the off-diagonal elements is less than the
	 *         tolerance, false otherwise.
	 */
	public boolean converged(Number[][] M, double tolerance) {
		// Initialize the norm of the off-diagonal elements
		double offDiagonalNorm = 0;

		// Loop over the Matrix
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M.length; j++) {
				// Exclude diagonal elements
				if (i != j) {
					// Add the square of the current off-diagonal element to the norm
					offDiagonalNorm += M[i][j].doubleValue() * M[i][j].doubleValue();
				}
			}
		}

		// Take the square root of the sum to finalize the computation of the norm
		offDiagonalNorm = Math.sqrt(offDiagonalNorm);

		// Check if the norm is less than the tolerance
		return offDiagonalNorm < tolerance;
	}

	/**
	 * Calculates the Singular Value Decomposition (SVD) of the matrix using
	 * Householder QR decomposition.
	 * <p>
	 * This method decomposes the matrix into three matrices U, Σ, and V^T, where U
	 * and V are orthogonal matrices and Σ is a diagonal matrix containing the
	 * singular values of the original matrix. The decomposition is performed using
	 * the Householder QR method.
	 * </p>
	 *
	 * @return An array containing the U, Σ, and V^T matrices of the SVD.
	 * @throws RuntimeException if the matrix is not square.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T>[] singularValueDecompositionWithHouseholderQR() throws RuntimeException {
		if (nrows != ncols) {
			throw new RuntimeException("Matrix must be square.");
		}
		// Initialize U and V as identity matrices
		Matrix<T> U = identity(nrows, ncols);
		Matrix<T> V = identity(nrows, ncols);

		// Perform QR decomposition iteratively
		for (int i = 0; i < nrows - 1; i++) {
			// Perform QR decomposition on the subMatrix from the i-th row and column
			// to the end
			Matrix<T> subMatrix = getSubMatrix(i, nrows - 1, i, ncols - 1);
			Matrix<T>[] qrMatrices = subMatrix.qrDecompositionHouseholder();
			Matrix<T> Q = qrMatrices[0];
			Matrix<T> R = qrMatrices[1];

			// Update U and V
			U.set(i, nrows - 1, i, ncols - 1, Q);
			V.set(i, nrows - 1, i, ncols - 1, Q.transpose());

			// Update the original Matrix
			set(i, nrows - 1, i, ncols - 1, R);
		}

		// The singular values are the diagonal elements of the original Matrix
		// after the transformations
		Matrix<T> S = new Matrix<>(this.diag(), 1);

		return new Matrix[] { U, S, V.transpose() };
	}

	/**
	 * Calculates the Singular Value Decomposition (SVD) of the matrix using
	 * Gram-Schmidt QR decomposition.
	 * <p>
	 * This method decomposes the matrix into three matrices U, Σ, and V^T, where U
	 * and V are orthogonal matrices and Σ is a diagonal matrix containing the
	 * singular values of the original matrix. The decomposition is performed using
	 * the Gram-Schmidt QR method.
	 * </p>
	 *
	 * @return An array containing the U, Σ, and V^T matrices of the SVD.
	 * @throws RuntimeException if the matrix is not square.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T>[] singularValueDecompositionWithGramSchmidtQR() throws RuntimeException {
		if (nrows != ncols) {
			throw new RuntimeException("Matrix must be square.");
		}
		// Initialize U and V as identity matrices
		Matrix<T> U = identity(nrows, ncols);
		Matrix<T> V = identity(nrows, ncols);

		// Perform QR decomposition iteratively
		for (int i = 0; i < nrows - 1; i++) {
			// Perform QR decomposition on the subMatrix from the i-th row and column
			// to the end
			Matrix<T> subMatrix = getSubMatrix(i, nrows - 1, i, ncols - 1);
			Matrix<T>[] qrMatrices = subMatrix.qrDecompositionGramSchmidt();
			Matrix<T> Q = qrMatrices[0];
			Matrix<T> R = qrMatrices[1];

			// Update U and V
			U.set(i, nrows - 1, i, ncols - 1, Q);
			V.set(i, nrows - 1, i, ncols - 1, Q.transpose());

			// Update the original Matrix
			set(i, nrows - 1, i, ncols - 1, R);
		}

		// The singular values are the diagonal elements of the original Matrix
		// after the transformations
		Matrix<T> S = new Matrix<>(this.diag(), 1);

		return (Matrix<T>[]) new Matrix[] { U, S, V.transpose() };
	}

	/**
	 * Calculates the Singular Value Decomposition (SVD) of the matrix using Givens
	 * QR decomposition.
	 * <p>
	 * This method decomposes the matrix into three matrices U, Σ, and V^T, where U
	 * and V are orthogonal matrices and Σ is a diagonal matrix containing the
	 * singular values of the original matrix. The decomposition is performed using
	 * the Givens QR method.
	 * </p>
	 *
	 * @return An array containing the U, Σ, and V^T matrices of the SVD.
	 * @throws RuntimeException if the matrix is not square.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T>[] singularValueDecompositionWithGivensQR() throws RuntimeException {
		if (nrows != ncols) {
			throw new RuntimeException("matrix must be square.");
		}
		// Initialize U and V as identity matrices
		Matrix<T> U = identity(nrows, ncols);
		Matrix<T> V = identity(nrows, ncols);

		// Perform QR decomposition iteratively
		for (int i = 0; i < nrows - 1; i++) {
			// Perform QR decomposition on the subMatrix from the i-th row and column
			// to the end
			Matrix<T> subMatrix = getSubMatrix(i, nrows - 1, i, ncols - 1);
			Matrix<T>[] qrMatrices = null;
			try {
				qrMatrices = subMatrix.qrDecompositionGivens();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Matrix<T> Q = qrMatrices[0];
			Matrix<T> R = qrMatrices[1];

			// Update U and V
			U.set(i, nrows - 1, i, ncols - 1, Q);
			V.set(i, nrows - 1, i, ncols - 1, Q.transpose());

			// Update the original Matrix
			set(i, nrows - 1, i, ncols - 1, R);
		}

		// The singular values are the diagonal elements of the original Matrix
		// after the transformations
		Matrix<T> S = new Matrix<>(this.diag(), 1);

		return (Matrix<T>[]) new Matrix[] { U, S, V.transpose() };
	}

	/**
	 * Method. Calculate the singular values of a matrix.
	 * 
	 * @return The singular values of the matrix.
	 * @throws RuntimeException if the matrix is not square.
	 */
	public double[] singularValues() throws RuntimeException {
		if (nrows != ncols) {
			throw new RuntimeException("Matrix must be square.");
		}
		// Perform SVD decomposition
		Matrix<T>[] svdMatrices = this.singularValueDecompositionWithHouseholderQR();
		Matrix<T> S = svdMatrices[1];

		// Extract the singular values from the diagonal of S
		double[] singularValues = new double[nrows];
		for (int i = 0; i < nrows; i++) {
			singularValues[i] = ((BigDecimal) S.get(i, i)).doubleValue();
		}

		return singularValues;

	}

	/**
	 * Calculates the singular values of a matrix using a specified decomposition
	 * method.
	 * <p>
	 * This method calculates the singular values of the matrix by performing a
	 * Singular Value Decomposition (SVD) using the specified decomposition method.
	 * The supported methods include "Householder", "GramSchmidt", and "Givens".
	 * </p>
	 *
	 * @param decompositionMethodName The name of the decomposition method to use.
	 * @return An array of double values representing the singular values of the
	 *         matrix.
	 * @throws RuntimeException if the matrix is not square or if an invalid
	 *                          decomposition method name is provided.
	 */
	public double[] singularValues(String decompositionMethodName) throws RuntimeException {
		if (nrows != ncols) {
			throw new RuntimeException("Matrix must be square.");
		}
		Matrix<T>[] svdMatrices;
		if (decompositionMethodName.equalsIgnoreCase("Householder")) {
			// Perform SVD decomposition using Householder QR
			svdMatrices = this.singularValueDecompositionWithHouseholderQR();
		} else if (decompositionMethodName.equalsIgnoreCase("GramSchmidt")) {
			// Perform SVD decomposition using Gram-Schmidt QR
			svdMatrices = this.singularValueDecompositionWithGramSchmidtQR();
		} else if (decompositionMethodName.equalsIgnoreCase("Givens")) {
			// Perform SVD decomposition using Givens QR
			svdMatrices = this.singularValueDecompositionWithGivensQR();
		} else {
			throw new IllegalArgumentException("Invalid decomposition method name.");
		}

		Matrix<T> S = svdMatrices[1];

		// Extract the singular values from the diagonal of S
		double[] singularValues = new double[nrows];
		for (int i = 0; i < nrows; i++) {
			singularValues[i] = ((BigDecimal) S.get(i, i)).doubleValue();
		}

		return singularValues;
	}

	/**
	 * Calculates the adjugate of the matrix. The adjugate of a matrix is the
	 * transpose of the cofactor matrix.
	 * <p>
	 * This method computes the adjugate by calculating the cofactor for each
	 * element, applying the appropriate sign, and then transposing the resulting
	 * matrix. The adjugate is defined for square matrices only.
	 * </p>
	 *
	 * @return The adjugate Matrix.
	 * @throws IllegalStateException if the matrix is not square.
	 */
	public Matrix<T> adjugate() {
		if (!isSquare()) {
			throw new IllegalStateException("Matrix must be square to compute the adjugate.");
		}

		Matrix<T> adjugate = new Matrix<>(ncols, nrows, this.clazz);
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				// Calculate the cofactor
				Matrix<T> subMatrix = this.minor(i, j);
				T cofactor = subMatrix.determinant();

				// Apply the sign based on the position
				int sign = ((i + j) % 2 == 0) ? 1 : -1;

				// Multiply cofactor by sign and cast to T
				T cofactorValue = (T) mult(cofactor, convertToGenericType(sign, clazz));

				// Assign the cofactor with the appropriate sign to the adjugate Matrix
				// Note: The indices are swapped for transposition
				adjugate.set(j, i, cofactorValue);
			}
		}
		return adjugate;
	}

	/**
	 * Raises the matrix to the power of an exponent.
	 * <p>
	 * This method calculates the matrix raised to a specified exponent. For square
	 * matrices, it supports both positive and negative exponents. The method first
	 * checks if the matrix is square, then calculates the identity matrix of the
	 * same size and raises it to the specified exponent by multiplying it by the
	 * original matrix.
	 * </p>
	 *
	 * @param exponent The exponent to which the matrix is raised.
	 * @return The matrix raised to the specified power.
	 * @throws UnsupportedOperationException if the matrix is not square.
	 */
	public Matrix<T> power(int exponent) throws UnsupportedOperationException {
		// Ensure the Matrix is square
		if (nrows != ncols) {
			throw new UnsupportedOperationException("Matrix must be square to be raised to a power.");
		}

		// Create an identity Matrix of the same size as the current
		// Matrix
		Matrix<T> result = this.eye(nrows);

		// If the exponent is 0, the resulting Matrix is the identity
		// Matrix
		if (exponent == 0) {
			return result;
		}

		// If the exponent is negative, calculate the inverse and set the exponent to
		// its absolute value
		if (exponent < 0) {
			result = result.inverseUsingComatrix();
			exponent = -exponent;
		}

		// Multiply the Matrix by itself exponent times
		for (int i = 0; i < exponent; i++) {
			try {
				result = result.multiply(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Return the resulting Matrix
		return result;
	}

	/**
	 * Solves equations by nonsingular systems and least squares.
	 * <p>
	 * This method solves a system of linear equations or performs a least squares
	 * approximation using LU decomposition. It first decomposes the matrix into
	 * lower and upper triangular matrices (L and U) and then solves the system by
	 * forward and backward substitution.
	 * </p>
	 *
	 * @param b The right-hand side of the equation.
	 * @return The solution matrix.
	 */
	public Matrix<T> solveLU(Matrix<T> b) {
		Matrix<T>[] LU = this.luDecomposition();
		Matrix<T> L = LU[0];
		Matrix<T> U = LU[1];
		Matrix<T> y = forwardSubstitution(L, b);
		Matrix<T> x = backwardSubstitution(U, y);
		return x;
	}

	/**
	 * Solves a system of linear equations using LU decomposition.
	 * <p>
	 * This method decomposes the matrix into its LU components and then solves the
	 * system of equations for the solution. It involves two main steps: performing
	 * LU decomposition on the matrix and solving the system of equations for the
	 * solution.
	 * </p>
	 * <p>
	 * The method assumes that the LU decomposition has been performed and that the
	 * matrix is square and non-singular.
	 * </p>
	 *
	 * @param b The right-hand side of the system of linear equations. It should be
	 *          a column vector.
	 * @return The solution matrix X, which is a column vector containing the
	 *         solution to the system of equations.
	 * @throws IllegalArgumentException if the matrix is not square or if the LU
	 *                                  decomposition has not been performed.
	 */
	public Matrix<T> solveLUP(Matrix<T> b) {
		// Step 1: Perform LU decomposition
		Matrix<T> L = lupDecomposition()[0];
		Matrix<T> U = luDecomposition()[1];
		Matrix<T> P = luDecomposition()[2]; // Assuming you have a method to get the permutation matrix

		// Step 2: Solve the system of equations for the solution
		// This involves solving L*Y = P*b for Y, then U*X = Y for X
		// This step is highly dependent on the specific type T and the implementation
		// of your Matrix class

		// Apply permutation matrix to b
		Matrix<T> permutedB = applyPermutation(P, b);

		// Solve L*Y = P*b for Y using forward substitution
		Matrix<T> Y = forwardSubstitution(L, permutedB);

		// Solve U*X = Y for X using backward substitution
		Matrix<T> X = backwardSubstitution(U, Y);

		// Return the solution matrix X
		return X;
	}

	/**
	 * Applies a permutation matrix to another matrix.
	 * <p>
	 * This method creates a new matrix permutedB of the same size as b. Then, it
	 * goes through each row of the permutation matrix P, finds the index where the
	 * value is 1, and uses that index to get the corresponding value from b.
	 * </p>
	 * 
	 * @param P The permutation matrix.
	 * @param b The matrix to which the permutation is to be applied.
	 * @return The matrix b after applying the permutation matrix P.
	 */
	public Matrix<T> applyPermutation(Matrix<T> P, Matrix<T> b) {
		int n = P.getNrows();
		Matrix<T> permutedB = new Matrix<>(n, 1, clazz);

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (compare(P.get(i, j), oneValue()) == 0) {
					permutedB.set(i, 0, b.get(j, 0));
					break;
				}
			}
		}

		return permutedB;
	}

	/**
	 * Calculates the condition number of the matrix.
	 * <p>
	 * The condition number of a matrix is the ratio of the largest singular value
	 * to the smallest singular value. It provides a measure of how sensitive the
	 * solution of the system of equations is to small changes in the coefficients.
	 * </p>
	 *
	 * @return The condition number of the matrix.
	 */
	public BigDecimal conditionNumber() {
		double[] singularValues = this.singularValues();
		BigDecimal max = BigDecimal.valueOf(Double.NEGATIVE_INFINITY);
		BigDecimal min = BigDecimal.valueOf(Double.POSITIVE_INFINITY);

		for (double value : singularValues) {
			BigDecimal bdValue = BigDecimal.valueOf(value);
			max = max.max(bdValue);
			min = min.min(bdValue);
		}

		return max.divide(min, RoundingMode.HALF_UP);
	}

//	public double conditionNumber() {
//		double[] singularValues = this.singularValues();
//		double max = Arrays.stream(singularValues).max().getAsDouble();
//		double min = Arrays.stream(singularValues).min().getAsDouble();
//		return max / min;
//	}

	/**
	 * Calculates the rank of the matrix.
	 * <p>
	 * The rank of a matrix is the maximum number of linearly independent rows (or
	 * columns). It can be found as the number of nonzero singular values. This
	 * method uses the singular values of the matrix to determine its rank.
	 * </p>
	 *
	 * @return The rank of the matrix.
	 */
	public int rank() {
		double tolerance = 1e-10; // tolerance
		double[] singularValues = this.singularValues();
		int rank = 0;
		for (double sv : singularValues) {
			if (sv > tolerance) {
				rank++;
			}
		}
		return rank;
	}

	/**
	 * Calculates the pseudoinverse of the matrix.
	 * <p>
	 * The pseudoinverse of a matrix A, denoted A⁺, is a generalization of the
	 * inverse matrix. It is defined and unique for all matrices whose entries are
	 * real or complex numbers. This method computes the pseudoinverse by performing
	 * a singular value decomposition (SVD) and then calculating the pseudoinverse
	 * of the singular values.
	 * </p>
	 *
	 * @return The pseudoinverse of the matrix.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> pseudoinverse() {
		Matrix<T>[] svd = this.singularValueDecompositionWithHouseholderQR();
		Matrix<T> U = svd[0];
		Matrix<T> S = svd[1];
		Matrix<T> V = svd[2];

		// Compute the pseudoinverse of the singular values
		for (int i = 0; i < S.nrows; i++) {
			if (((BigDecimal) S.get(i, i)).doubleValue() != 0) {
				T pseudoInverseValue = (T) Double.valueOf(1.0 / ((BigDecimal) S.get(i, i)).doubleValue());
				S.set(i, i, pseudoInverseValue);
			}
		}

		// Compute the pseudoinverse of the Matrix
		Matrix<T> pseudoinverse = null;
		try {
			pseudoinverse = V.multiply(S.transpose()).multiply(U.transpose());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pseudoinverse;
	}

	/**
	 * Returns the conjugate transpose of the matrix.
	 * <p>
	 * The conjugate transpose of a matrix is obtained by taking the transpose and
	 * then taking the complex conjugate of each element. For real matrices, the
	 * transpose and conjugate transpose are the same.
	 * </p>
	 *
	 * @return The conjugate transpose of the matrix.
	 */
	public Matrix<T> getConjugateTranspose() {
		Matrix<T> transpose = new Matrix<>(ncols, nrows, this.clazz);

		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				T element = (T) to2DArray()[i][j];
				// For complex matrices, you would take the complex conjugate here.
				// For real matrices, this step is not needed.
				transpose.set(j, i, element);
			}
		}

		return transpose;
	}

	/************************************************
	 * STRASSEN-LIKE Matrix OPERATIONS
	 **********************************************/

	/**
	 * Performs Strassen's matrix multiplication algorithm.
	 * <p>
	 * This method implements Strassen's algorithm for matrix multiplication, which
	 * is a divide-and-conquer algorithm that recursively breaks down the input
	 * matrices into smaller sub-matrices and combines the results to obtain the
	 * product. This method is efficient for large matrices and takes advantage of
	 * the principle of parallelism in the computation.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Integer> A = new Matrix<>(3, 3);
	 * Matrix<Integer> B = new Matrix<>(3, 3);
	 * // Assume A and B are initialized with values
	 * Matrix<Integer> C = A.strassen(B);
	 * </pre>
	 * </p>
	 *
	 * @param B The matrix to multiply with the current matrix.
	 * @return The product of the current matrix and matrix B, computed using
	 *         Strassen's algorithm.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> strassen(Matrix<T> B) {
		int n = nrows;
		T[][] A = to2DArray();
		T[][] Barray = B.to2DArray();
		T[][] C = (T[][]) new Number[n][n];
		if (n <= 32) {
			try {
				return this.multiply(B);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int m = n / 2;
		T[][] A11 = (T[][]) new Number[m][m];
		T[][] A12 = (T[][]) new Number[m][m];
		T[][] A21 = (T[][]) new Number[m][m];
		T[][] A22 = (T[][]) new Number[m][m];
		T[][] B11 = (T[][]) new Number[m][m];
		T[][] B12 = (T[][]) new Number[m][m];
		T[][] B21 = (T[][]) new Number[m][m];
		T[][] B22 = (T[][]) new Number[m][m];
		Matrix<T> A1 = new Matrix<>(A11, this.clazz);
		Matrix<T> A2 = new Matrix<>(A12, this.clazz);
		Matrix<T> A3 = new Matrix<>(A21, this.clazz);
		Matrix<T> A4 = new Matrix<>(A22, this.clazz);
		Matrix<T> B1 = new Matrix<>(B11, this.clazz);
		Matrix<T> B2 = new Matrix<>(B12, this.clazz);
		Matrix<T> B3 = new Matrix<>(B21, this.clazz);
		Matrix<T> B4 = new Matrix<>(B22, this.clazz);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				A11[i][j] = A[i][j];
				A12[i][j] = A[i][j + m];
				A21[i][j] = A[i + m][j];
				A22[i][j] = A[i + m][j + m];
				B11[i][j] = Barray[i][j];
				B12[i][j] = Barray[i][j + m];
				B21[i][j] = Barray[i + m][j];
				B22[i][j] = Barray[i + m][j + m];
			}
		}
		T[][] C11 = A1.strassen(B1).to2DArray();
//          T[][] C12 = A1.strassen(B2).to2DArray();
		T[][] C21 = A3.strassen(B1).to2DArray();
		T[][] C22 = A4.strassen(B4).to2DArray();
		T[][] C31 = A2.strassen(B2).to2DArray();
		T[][] C32 = A4.strassen(B3).to2DArray();
		T[][] C41 = A1.strassen(B4).to2DArray();
//          T[][] C42 = A3.strassen(B2).to2DArray();
		T[][] C51 = A2.strassen(B3).to2DArray();
//          T[][] C52 = A4.strassen(B4).to2DArray();
//          T[][] C61 = A1.strassen(B2).to2DArray();
		T[][] C62 = A3.strassen(B4).to2DArray();
		T[][] C71 = A2.strassen(B1).to2DArray();
//          T[][] C72 = A4.strassen(B1).to2DArray();

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				C[i][j] = subtract(add(C11[i][j], C41[i][j]), add(C51[i][j], C71[i][j]));
				C[i][j + m] = add(C31[i][j], C51[i][j]);
				C[i + m][j] = add(C21[i][j], C41[i][j]);
				C[i + m][j + m] = subtract(add(C11[i][j], C22[i][j]), add(C32[i][j], C62[i][j]));
			}
		}
		return new Matrix<>(C, this.clazz);
	}

	/**
	 * Performs Strassen's matrix multiplication algorithm on two matrices.
	 * <p>
	 * This method implements a generalization of Strassen's algorithm for matrix
	 * multiplication, taking two matrices as input and returning their product. The
	 * algorithm is optimized for large matrices by partitioning them into smaller
	 * sub-matrices and performing the multiplication in a divide-and-conquer
	 * fashion. This method ensures that the matrix multiplication is performed
	 * efficiently, leveraging the parallelism inherent in Strassen's algorithm.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Integer> A = new Matrix<>(3, 3);
	 * Matrix<Integer> B = new Matrix<>(3, 3);
	 * // Assume A and B are initialized with values
	 * Matrix<Integer> C = Matrix.strassenMultiply(A, B);
	 * </pre>
	 * </p>
	 *
	 * @param A The first matrix to multiply.
	 * @param B The second matrix to multiply.
	 * @return The product of matrices A and B, computed using Strassen's algorithm.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T> strassenMultiply(Matrix<T> A, Matrix<T> B) throws Exception {
		int n = nrows;
		if (n == 1) {
			// Ensure this.multiply(B) is correctly implemented for 1x1 matrices
			return A.multiply(B);
		} else {
			// Ensure partition is correctly implemented
			Matrix<T>[][] partitionedThis = partition(A);
			Matrix<T>[][] partitionedB = partition(B);

			Matrix<T> P1 = strassenMultiply(partitionedThis[0][0].plus(partitionedThis[1][1]),
					partitionedB[0][0].plus(partitionedB[1][1]));
			Matrix<T> P2 = strassenMultiply(partitionedThis[1][0].plus(partitionedThis[1][1]), partitionedB[0][0]);
			Matrix<T> P3 = strassenMultiply(partitionedThis[0][0], partitionedB[0][1].minus(partitionedB[1][1]));
			Matrix<T> P4 = strassenMultiply(partitionedThis[1][1], partitionedB[1][0].minus(partitionedB[0][0]));
			Matrix<T> P5 = strassenMultiply(partitionedThis[0][0].plus(partitionedThis[0][1]), partitionedB[1][1]);
			Matrix<T> P6 = strassenMultiply(partitionedThis[1][0].minus(partitionedThis[0][0]),
					partitionedB[0][0].plus(partitionedB[0][1]));
			Matrix<T> P7 = strassenMultiply(partitionedThis[0][1].minus(partitionedThis[1][1]),
					partitionedB[1][0].plus(partitionedB[1][1]));

			Matrix<T> C11 = P1.plus(P4).minus(P5).plus(P7);
			Matrix<T> C12 = P3.plus(P5);
			Matrix<T> C21 = P2.plus(P4);
			Matrix<T> C22 = P1.plus(P3).minus(P2).plus(P6);

			// Ensure concatenateHorizontally is correctly implemented
			Matrix<T> C = concatenateHorizontally(C11, C12, C21, C22);
			return C;
		}
	}

	/**
	 * Partitions the given matrix into four equal-sized sub-matrices.
	 * <p>
	 * This method divides the input matrix into four sub-matrices of equal size,
	 * assuming the matrix is square and its dimensions are evenly divisible by 2.
	 * The method returns a 2D array containing the four sub-matrices, arranged in a
	 * 2x2 grid. This partitioning is a crucial step in the Strassen algorithm, as
	 * it allows for the recursive application of the algorithm to the sub-matrices.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix<Integer> B = new Matrix<>(4, 4);
	 * // Assume B is initialized with values
	 * Matrix<Integer>[][] partitions = B.partition();
	 * </pre>
	 * </p>
	 *
	 * @param B The matrix to partition.
	 * @return A 2D array containing the four sub-matrices of B.
	 */
	@SuppressWarnings("unchecked")
	public Matrix<T>[][] partition(Matrix<T> B) {
		int midRow = B.getNrows() / 2;
		int midCol = B.getNcols() / 2;

		Matrix<T> topLeft = B.getSubMatrix(0, midRow - 1, 0, midCol - 1);
		Matrix<T> topRight = B.getSubMatrix(0, midRow - 1, midCol, B.getNcols() - 1);
		Matrix<T> bottomLeft = B.getSubMatrix(midRow, B.getNrows() - 1, 0, midCol - 1);
		Matrix<T> bottomRight = B.getSubMatrix(midRow, B.getNrows() - 1, midCol, B.getNcols() - 1);

		return new Matrix[][] { { topLeft, topRight }, { bottomLeft, bottomRight } };
	}

	/**
	 * Partitions this matrix into four equal-sized sub-matrices.
	 * <p>
	 * This method is a convenience method that partitions the current matrix into
	 * four equal-sized sub-matrices. It assumes that the matrix is square and its
	 * dimensions are evenly divisible by 2. The method returns a 2D array
	 * containing the four sub-matrices, arranged in a 2x2 grid. This partitioning
	 * is a crucial step in the Strassen algorithm, as it allows for the recursive
	 * application of the algorithm to the sub-matrices.
	 * </p>
	 *
	 * @return A 2D array containing the four sub-matrices of the current matrix.
	 */
	public Matrix<T>[][] partition() {
		return partition(this);
	}

	/*******************************************
	 * MISCELLANOUS METHODS
	 *****************************************/
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
//           sb.append("[");
		for (int i = 0; i < nrows; i++) {
			if (i > 0) {
//                           sb.append(",\n");
				sb.append("\n");
			}
			sb.append("[");
			for (int j = 0; j < ncols; j++) {
				if (j > 0) {
					sb.append(",\t"); // elements separator. Can also use--> sb.append(",\t");
				}
				sb.append(array[i * ncols + j]);
			}
			sb.append("]");
		}

//           sb.append("]");
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Matrix))
			return false;
		Matrix<?> Matrix = (Matrix<?>) o;
		return Arrays.equals(array, Matrix.array) && nrows == Matrix.nrows && ncols == Matrix.ncols;
	}

	/**
	 * Method. Print the matrix to the standard output in a formatted manner.
	 * <p>
	 * As a nice way to print a multidimensional array, This method converts the
	 * matrix to a 2D array and then formats the output string to make it more
	 * readable. It replaces occurrences of "], [" with a newline character to
	 * separate rows and removes unnecessary brackets and spaces to clean up the
	 * output.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * GenericMatrix&lt;Double&gt; matrix = new GenericMatrix&lt;&gt;(3, 3);
	 * // Assume matrix is initialized with values
	 * matrix.print();
	 * </pre>
	 * </p>
	 */
	public void print() {
		this.print(",\t", "\n");
	}

	/*
	 * public void print() { T[][] array2d = to2DArray();
	 * System.out.println(Arrays.deepToString(array2d).replace("], ", ",\n") //
	 * replace "], " by "\n" <----- there // is a space into "], " to consider.
	 * .replace("[ ", "") // replace "[ " by "" <----- no space .replace("[", "") //
	 * replace "[" by "" <----- no space .replace("[[", "") // replace "[[" by ""
	 * <----- space .replace("]]", "") // replace "]]" by "" <----- no space
	 * .replace(",\n", "\n")); // replace ",\n" by "\n" }
	 */

	/**
	 * Method. Print the matrix in a style resembling MATLAB's matrix printing.
	 * <p>
	 * This method converts the matrix to a 2D array and formats the output string
	 * to mimic MATLAB's matrix printing style. It replaces occurrences of "], ["
	 * with "]\n" to separate rows and adjusts the brackets to closely match
	 * MATLAB's output format.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * GenericMatrix&lt;Double&gt; matrix = new GenericMatrix&lt;&gt;(3, 3);
	 * // Assume matrix is initialized with values
	 * matrix.printAsMatlab();
	 * </pre>
	 * </p>
	 */
	public void printAsMatlab() {
		System.out.println(this.toString());
	}

	public void printLikeMatlab() {
		System.out.println(this.toString());
	}
	/*
	 * public void printAsMatlab() {
	 * System.out.println(Arrays.deepToString(to2DArray()).replace("], ",
	 * "]\n").replace("[[", "[").replace("]]", "]")); }
	 */

	/**
	 * Prints the matrix in a format similar to Matlab.
	 * 
	 * @param valuesSeparator    the separator to use between matrix values
	 * @param interRowsSeparator the separator to use between rows
	 */
	public void print(String valuesSeparator, String interRowsSeparator) {
		if (array == null) {
			System.out.println("Matrix is not initialized.");
			return;
		}

		StringBuilder matlabMatrix = new StringBuilder();
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				T element = array[i * ncols + j];
				String elementStr;
				// Handle different types of numbers
				if (element instanceof Float || element instanceof Double || element instanceof BigDecimal) {
					// Ensure floating-point numbers are in a format MATLAB can understand
					elementStr = String.format(Locale.US, "%.2f", element);
				} else if (element instanceof Byte || element instanceof Short || element instanceof Integer
						|| element instanceof Long) {
					// For integral types, simply use the toString() method
					elementStr = element.toString();
				} else {
					// Fallback for other types
					elementStr = element.toString();
				}
				matlabMatrix.append(elementStr);
				if (j < ncols - 1) {
					matlabMatrix.append(valuesSeparator);
				}
			}
			matlabMatrix.append(interRowsSeparator);
		}

		System.out.println(matlabMatrix.toString());
	}

	/**
	 * Method. Check if this matrix is positive definite.
	 *
	 * A matrix is positive definite if it satisfies the following conditions: 1.
	 * The matrix is square (i.e. has the same number of rows and columns) 2. The
	 * matrix is symmetric (i.e. equal to its own transpose) 3. All the eigenvalues
	 * of the matrix are positive
	 *
	 * @return true if the matrix is positive definite, false otherwise
	 */
	public boolean isPositiveDefinite() {
		// Check if the matrix is square
		if (nrows != ncols) {
			return false;
		}

		// Check if the matrix is symmetric
		if (!equals(transpose())) {
			return false;
		}

		// Calculate the eigenvalues of the matrix
		T[] eigenvalues = eigenvalues();

		// Check if all the eigenvalues are positive
		for (T eigenvalue : eigenvalues) {
			if (((BigDecimal) eigenvalue).doubleValue() <= 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Method. Check if this matrix satisfies the metric property.
	 *
	 * A matrix satisfies the metric property if it is positive definite and all its
	 * diagonal elements are equal to 1.
	 *
	 * @return true if the matrix satisfies the metric property, false otherwise
	 */
	public boolean isMetricPositiveDefined() {
		// Check if the matrix is positive definite
		if (!isPositiveDefinite()) {
			return false;
		}

		// Check if all the diagonal elements are equal to 1
		for (int i = 0; i < nrows; i++) {
			if (!get(i, i).equals(oneValue())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Method. Return true if the matrix is diagonal, false otherwise.
	 *
	 * A matrix is diagonal if all non-diagonal elements are zero.
	 *
	 * @return true if the matrix is diagonal, false otherwise
	 */
	public boolean isDiagonal2() {
		if (nrows != ncols) {
			return false;
		}
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				if (i != j && !array[i * ncols + j].equals(0)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Method. Return true if the matrix is an identity matrix, false otherwise.
	 *
	 * An identity matrix is a square matrix with ones on the main diagonal and
	 * zeros elsewhere.
	 *
	 * @return true if the matrix is an identity matrix, false otherwise
	 */
	public boolean isIdentity2() {
		if (nrows != ncols) {
			return false;
		}
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				if (i == j && !array[i * ncols + j].equals(1)) {
					return false;
				} else if (i != j && !array[i * ncols + j].equals(0)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Method. Return true if the matrix is orthogonal, false otherwise.
	 *
	 * A matrix is orthogonal if its transpose is equal to its inverse.
	 *
	 * @return true if the matrix is orthogonal, false otherwise
	 */
	public boolean isOrthogonal2() {
		if (nrows != ncols) {
			return false;
		}
		Matrix<T> transpose = transpose();
		Matrix<T> inverse = transpose.inverseUsingComatrix();
		return this.equals(inverse);
	}

	/**
	 * Method. Return true if the matrix is upper triangular, false otherwise.
	 *
	 * An upper triangular matrix is a matrix where all elements below the main
	 * diagonal are zero.
	 *
	 * @return true if the matrix is upper triangular, false otherwise
	 */
	public boolean isUpperTriangular2() {
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < i; j++) {
				if (!array[i * ncols + j].equals(0)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Method. Return true if the matrix is lower triangular, false otherwise.
	 *
	 * A lower triangular matrix is a matrix where all elements above the main
	 * diagonal are zero.
	 *
	 * @return true if the matrix is lower triangular, false otherwise
	 */
	public boolean isLowerTriangular2() {
		for (int i = 0; i < nrows; i++) {
			for (int j = i + 1; j < ncols; j++) {
				if (!array[i * ncols + j].equals(0)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Method. Check if the matrix is empty or not.
	 *
	 * @return true if the matrix is empty, false otherwise
	 */
	public boolean isEmpty() {
		return nrows == 0 || ncols == 0;
	}

	/**
	 * Method. Check if the matrix is diagonal or not.
	 *
	 * @return true if the matrix is diagonal, false otherwise
	 */
	public boolean isDiagonal() {
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				if (i != j && !get(i, j).equals(0)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Method. Check if the matrix is identity or not.
	 *
	 * @return true if the matrix is identity, false otherwise
	 */
	public boolean isIdentity() {
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				if (i == j && !get(i, j).equals(1)) {
					return false;
				} else if (i != j && !get(i, j).equals(0)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Method. Check if the matrix is orthogonal or not.
	 *
	 * @return true if the matrix is orthogonal, false otherwise
	 * @throws Exception
	 */
	public boolean isOrthogonal() throws Exception {
		Matrix<T> transpose = this.transpose();
		return this.multiply(transpose).isIdentity();
	}

	/**
	 * Method. Check if the matrix is upper triangular or not.
	 *
	 * @return true if the matrix is upper triangular, false otherwise
	 */
	public boolean isUpperTriangular() {
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < i; j++) {
				if (!get(i, j).equals(0)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Method. Check if the matrix is lower triangular or not.
	 *
	 * @return true if the matrix is lower triangular, false otherwise
	 */
	public boolean isLowerTriangular() {
		for (int i = 0; i < nrows; i++) {
			for (int j = i + 1; j < ncols; j++) {
				if (!get(i, j).equals(0)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Method. Check if the matrix is symmetric or not.
	 *
	 * @return true if the matrix is symmetric, false otherwise
	 */
	public boolean isSymmetric() {
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				if (!get(i, j).equals(get(j, i))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks if the matrix is symmetric and positive definite.
	 * <p>
	 * This method verifies whether the matrix is both symmetric and positive
	 * definite. A matrix is symmetric if it is equal to its transpose, and positive
	 * definite if all its principal minors are positive.
	 * </p>
	 *
	 * @return true if the matrix is symmetric and positive definite, false
	 *         otherwise.
	 */
	public boolean isSymmetricPositiveDefined() {
		// Check if the Matrix is square
		if (nrows != ncols) {
			return false;
		}

		// Check if the Matrix is symmetric
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < i; j++) {
				if (!array[i * ncols + j].equals(array[j * ncols + i])) {
					return false;
				}
			}
		}

		// Check if the Matrix is positive definite
		for (int i = 0; i < nrows; i++) {
			Matrix<T> subMatrix = getSubMatrix(i);
			if (compare(subMatrix.determinant(), zeroValue()) <= 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Method. Check if the matrix is square or not.
	 *
	 * @return true if the matrix is square, false otherwise
	 */
	public boolean isSquare() {
		return nrows == ncols;
	}

	/**
	 * Method. Return true if the matrix is nonsingular, false otherwise. A matrix
	 * is nonsingular if its determinant is not zero.
	 */
	public boolean isNonsingular() {
//          return Math.abs(determinant()) > 0;
		return compare(sqrt(mult(determinant(), determinant())), zeroValue()) > 0;
	}

	/**
	 * Method. Return true if the matrix is singular, false otherwise. A matrix is
	 * singular if its determinant is zero.
	 * 
	 * <p>
	 * A matrix is singular if it does not have an inverse, meaning its determinant
	 * is zero. Singular matrices do not have a multiplicative inverse and cannot be
	 * used to solve systems of linear equations.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix&lt;Double&gt; matrix = new Matrix&lt;&gt;(3, 3);
	 * // Assume matrix is initialized with values
	 * boolean isSingular = matrix.isSingular();
	 * if (isSingular) {
	 * 	System.out.println("The matrix is singular.");
	 * } else {
	 * 	System.out.println("The matrix is regular.");
	 * }
	 * </pre>
	 * </p>
	 *
	 * @return {@code true} if the matrix is singular, {@code false} otherwise.
	 */
	public boolean isSingular() {
		return compare(determinant(), zeroValue()) == 0;
	}

	/**
	 * Method .Return true if the matrix is full rank, false otherwise. A matrix is
	 * full rank if its rank is equal to the minimum of its number of rows and
	 * columns.
	 */
	public boolean isFullRank() {
		int rank = Math.min(nrows, ncols);
		for (int i = 0; i < rank; i++) {
			if (compare(sqrt(mult(getSubMatrix(i, i).determinant(), getSubMatrix(i, i).determinant())),
					zeroValue()) == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method. Check if the columns of this matrix span only the column space.
	 *
	 * @return true if the columns span only the column space, false otherwise
	 */
	public boolean isColumnSpaceOnly() {
		// A matrix spans only the column space if it has full column rank
		return rank() == ncols;
	}

	/**
	 * Method. Check if the rows of this matrix span only the row space.
	 *
	 * @return true if the rows span only the row space, false otherwise
	 */
	public boolean isRowSpaceOnly() {
		// A matrix spans only the row space if it has full row rank
		return rank() == nrows;
	}

	/**
	 * Method. Check if the vectors in this matrix are linearly dependent.
	 *
	 * @return true if the vectors are linearly dependent, false otherwise
	 */
	public boolean isLinearlyDependent() {
		// A matrix is linearly dependent if its determinant is 0
		return compare(determinant(), zeroValue()) == 0;
	}

	/**
	 * Method. Check if the vectors in this matrix are linearly independent.
	 *
	 * @return true if the vectors are linearly independent, false otherwise
	 */
	public boolean isLinearlyIndependent() {
		// A matrix is linearly independent if its determinant is not 0
		return compare(determinant(), zeroValue()) != 0;
	}

	/**
	 * Method. Checks if the matrix is normal.
	 * <p>
	 * A matrix is considered normal if it commutes with its conjugate transpose. In
	 * other words, for a matrix A, A must be equal to its conjugate transpose A*
	 * when multiplied by A and A* by A.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix&lt;Double&gt; matrix = new Matrix&lt;&gt;(3, 3);
	 * // Assume matrix is initialized with values
	 * boolean isNormal = matrix.isNormal();
	 * if (isNormal) {
	 * 	System.out.println("The matrix is normal.");
	 * } else {
	 * 	System.out.println("The matrix is not normal.");
	 * }
	 * </pre>
	 * </p>
	 *
	 * @return {@code true} if the matrix is normal, {@code false} otherwise.
	 * @throws Exception
	 */
	public boolean isNormal() throws Exception {
		// Assuming getConjugateTranspose() returns the conjugate transpose of the
		// matrix
		Matrix<T> conjugateTranspose = this.getConjugateTranspose();
		return this.times(conjugateTranspose).equals(conjugateTranspose.times(this));
	}

	/**
	 * Checks if the matrix is regular (non-singular).
	 * <p>
	 * A matrix is regular if it has an inverse, meaning its determinant is not
	 * zero. A regular matrix can be multiplied by its inverse to produce the
	 * identity matrix.
	 * </p>
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * Matrix&lt;Double&gt; matrix = new Matrix&lt;&gt;(3, 3);
	 * // Assume matrix is initialized with values
	 * boolean isRegular = matrix.isRegular();
	 * if (isRegular) {
	 * 	System.out.println("The matrix is regular.");
	 * } else {
	 * 	System.out.println("The matrix is singular.");
	 * }
	 * </pre>
	 * </p>
	 *
	 * @return {@code true} if the matrix is regular (non-singular), {@code false}
	 *         otherwise.
	 */
	public boolean isRegular() {
		// Assuming getDeterminant() returns the determinant of the matrix
		return compare(determinant(), zeroValue()) != 0;
	}

	/*************************************
	 * HELPER METHODS
	 **************************************/

	/**
	 * Method. Check if the dimensions of the Matrix are valid.
	 *
	 * @throws IllegalArgumentException if the number of rows or columns is not
	 *                                  positive.
	 */
	@SuppressWarnings("unused")
	private void checkDimensionsSign() {
		if (nrows <= 0 || ncols <= 0) {
			throw new IllegalArgumentException("Number of rows and columns must be positive.");
		}
	}

	// Helper method to check if the given array is a valid permutation
	@SuppressWarnings("unused")
	private boolean isValidPermutation(int[] permutation) {
		int[] sortedPermutation = Arrays.copyOf(permutation, permutation.length);
		Arrays.sort(sortedPermutation);

		for (int i = 0; i < sortedPermutation.length; i++) {
			if (sortedPermutation[i] != i + 1) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Method. Check if the given dimensions are valid.
	 *
	 * @param nrows The number of rows to check.
	 * @param ncols The number of columns to check.
	 * @throws IllegalArgumentException if the number of rows or columns is not
	 *                                  positive.
	 */
	@SuppressWarnings("unused")
	private void checkDimensionsSign(int nrows, int ncols) {
		if (nrows <= 0 || ncols <= 0) {
			throw new IllegalArgumentException("Number of rows and columns must be positive.");
		}
	}

	/**
	 * Method. Check the dimensions of the right Matrix B by comparing them to that
	 * of the left. Useful for Addition and Subtraction of two matrices.
	 *
	 * @param B the right Matrix (the operand)
	 * @throws IllegalArgumentException if Matrix dimensions are not identical
	 */
	@SuppressWarnings("unused")
	private void checkDimensionsIdentity(Matrix<T> B) {
		if (this.nrows != B.nrows || this.ncols != B.ncols) {
			throw new IllegalArgumentException("Matrix dimensions must be identical!");
		}
	}

	private void checkDimensionsValidity(int nrows, int ncols) {
		if (nrows <= 0 || nrows >= Integer.MAX_VALUE || ncols <= 0) {
			throw new IndexOutOfBoundsException("Number of rows or columns is not valid");
		}
	}

	/**
	 * Method. Check if the number of columns of Matrix A equals the number of rows
	 * of the Matrix B so that the operation C = AxB is possible
	 *
	 * @param B Operand
	 * @throws IllegalArgumentException if the number of columns of A is not equal
	 *                                  to the number of rows of B
	 */
	private boolean checkSiColiLico(Matrix<T> B) { // si nombre de colonnes de A égale nombre de lignes de B, alors A*B
		// possible
		if (ncols != B.nrows) {
			throw new IllegalArgumentException(
					"Number of columns of A is not equal to the number of rows of B, so Matrix product is not possible.");
		}
		return ncols == B.nrows; // true
	}

	private boolean isVector() {
		// Check if the vector is one-dimensional (row or column vector)
		if (this.nrows != 1 && this.ncols != 1) {
			return false;
		}
		return true;
	}

	/**
	 * Convenience method to verify compatibility for multiplication based on
	 * dimensions.
	 * 
	 * @param A The first matrix or vector.
	 * @param B The second matrix or vector.
	 * @return True if the dimensions are compatible for multiplication, false
	 *         otherwise.
	 */
	private boolean verifyMultiplicationCompatibility(Matrix<T> B) {
		Matrix<T> A = this;
		// Check if A is a vector
		boolean isAVector = A.isVector();

		// Check if B is a vector
		boolean isBVector = B.isVector();

		if (isAVector && isBVector) { // v*v
			return Math.max(A.nrows, A.ncols) == Math.max(B.nrows, B.ncols);
		} else if (isAVector && !isBVector) { // v*M
			return Math.max(A.nrows, A.ncols) == Math.max(B.nrows, B.ncols);
		} else if (!isAVector && isBVector) { // M*v
			return Math.max(A.nrows, A.ncols) == Math.max(B.nrows, B.ncols);
		} else { // M*M
//	        return Math.max(A.nrows, A.ncols) == Math.max(B.nrows, B.ncols);
			return checkSiColiLico(B);
		}
	}

	@SuppressWarnings("unchecked")
	private T add(T a, T b) {
		if (a == null) {
			a = zeroValue();
		}
		if (b == null) {
			b = zeroValue();
		}
		if (a instanceof Byte && b instanceof Byte) {
			return (T) Byte.valueOf((byte) (((Byte) a) + ((Byte) b)));
		} else if (a instanceof Short && b instanceof Short) {
			return (T) Short.valueOf((short) (((Short) a) + ((Short) b)));
		} else if (a instanceof Integer && b instanceof Integer) {
			return (T) Integer.valueOf(((Integer) a) + ((Integer) b));
		} else if (a instanceof Long && b instanceof Long) {
			return (T) Long.valueOf(((Long) a) + ((Long) b));
		} else if (a instanceof Float && b instanceof Float) {
			return (T) Float.valueOf(((Float) a) + ((Float) b));
		} else if (a instanceof Double && b instanceof Double) {
			return (T) Double.valueOf(((Double) a) + ((Double) b));
		} else if (a instanceof BigDecimal && b instanceof BigDecimal) {
			return (T) ((BigDecimal) a).add((BigDecimal) b);
		}
		throw new UnsupportedOperationException("Unsupported type for addition");
	}

	@SuppressWarnings("unchecked")
	private T subtract(T a, T b) {
		if (a == null) {
			a = zeroValue();
		}
		if (b == null) {
			b = zeroValue();
		}
		if (a instanceof Byte && b instanceof Byte) {
			return (T) Byte.valueOf((byte) (((Byte) a) - ((Byte) b)));
		} else if (a instanceof Short && b instanceof Short) {
			return (T) Short.valueOf((short) (((Short) a) - ((Short) b)));
		} else if (a instanceof Integer && b instanceof Integer) {
			return (T) Integer.valueOf(((Integer) a) - ((Integer) b));
		} else if (a instanceof Long && b instanceof Long) {
			return (T) Long.valueOf(((Long) a) - ((Long) b));
		} else if (a instanceof Float && b instanceof Float) {
			return (T) Float.valueOf(((Float) a) - ((Float) b));
		} else if (a instanceof Double && b instanceof Double) {
			return (T) Double.valueOf(((Double) a) - ((Double) b));
		} else if (a instanceof BigDecimal && b instanceof BigDecimal) {
			return (T) ((BigDecimal) a).subtract((BigDecimal) b);
		}
		throw new UnsupportedOperationException("Unsupported type for subtraction");
	}

	/**
	 * Method. Negate a value.
	 *
	 * @param a The value to negate.
	 * @return The negated value.
	 * @throws UnsupportedOperationException if the type is not supported.
	 */
	@SuppressWarnings("unchecked")
	private T negate(T a) {

		if (a == null) {
			a = zeroValue();
		}

		if (a instanceof Byte) {
			return (T) Byte.valueOf((byte) -((Byte) a));
		} else if (a instanceof Short) {
			return (T) Short.valueOf((short) -((Short) a));
		} else if (a instanceof Integer) {
			return (T) Integer.valueOf(-((Integer) a));
		} else if (a instanceof Long) {
			return (T) Long.valueOf(-((Long) a));
		} else if (a instanceof Float) {
			return (T) Float.valueOf(-((Float) a));
		} else if (a instanceof Double) {
			return (T) Double.valueOf(-((Double) a));
		} else if (a instanceof BigDecimal) {
			return (T) ((BigDecimal) a).negate();
		}
		throw new UnsupportedOperationException("Unsupported type for negation");
	}

	@SuppressWarnings("unchecked")
	private T mult(T a, T b) {
		if (a == null) {
			a = zeroValue();
		}
		if (b == null) {
			b = zeroValue();
		}
		if (a instanceof Byte && b instanceof Byte) {
			return (T) Byte.valueOf((byte) (((Byte) a) * ((Byte) b)));
		} else if (a instanceof Short && b instanceof Short) {
			return (T) Short.valueOf((short) (((Short) a) * ((Short) b)));
		} else if (a instanceof Integer && b instanceof Integer) {
			return (T) Integer.valueOf(((Integer) a) * ((Integer) b));
		} else if (a instanceof Long && b instanceof Long) {
			return (T) Long.valueOf(((Long) a) * ((Long) b));
		} else if (a instanceof Float && b instanceof Float) {
			return (T) Float.valueOf(((Float) a) * ((Float) b));
		} else if (a instanceof Double && b instanceof Double) {
			return (T) Double.valueOf(((Double) a) * ((Double) b));
		} else if (a instanceof BigDecimal && b instanceof BigDecimal) {
			return (T) ((BigDecimal) a).multiply((BigDecimal) b);
		}
		throw new UnsupportedOperationException("Unsupported type for multiplication");
	}

	@SuppressWarnings("unchecked")
	private T divide(T a, T b) {
		if (a == null) {
			a = zeroValue();
		}
		if (b == null) {
			b = zeroValue();
		}
		if (a instanceof Byte && b instanceof Byte) {
			return (T) Byte.valueOf((byte) (((Byte) a) / ((Byte) b)));
		} else if (a instanceof Short && b instanceof Short) {
			return (T) Short.valueOf((short) (((Short) a) / ((Short) b)));
		} else if (a instanceof Integer && b instanceof Integer) {
			return (T) Integer.valueOf(((Integer) a) / ((Integer) b));
		} else if (a instanceof Long && b instanceof Long) {
			return (T) Long.valueOf(((Long) a) / ((Long) b));
		} else if (a instanceof Float && b instanceof Float) {
			return (T) Float.valueOf(((Float) a) / ((Float) b));
		} else if (a instanceof Double && b instanceof Double) {
			return (T) Double.valueOf(((Double) a) / ((Double) b));
		} else if (a instanceof BigDecimal && b instanceof BigDecimal) {
			return (T) ((BigDecimal) a).divide((BigDecimal) b);
		}
		throw new UnsupportedOperationException("Unsupported type for division");
	}

	@SuppressWarnings("unchecked")
	private T zeroValue() {
		if (clazz == Integer.class) {
			return (T) Integer.valueOf(0);
		} else if (clazz == Double.class) {
			return (T) Double.valueOf(0.0);
		} else if (clazz == Float.class) {
			return (T) Float.valueOf(0.0f);
		} else if (clazz == Long.class) {
			return (T) Long.valueOf(0L);
		} else if (clazz == Short.class) {
			return (T) Short.valueOf((short) 0);
		} else if (clazz == Byte.class) {
			return (T) Byte.valueOf((byte) 0);
		} else if (clazz == BigDecimal.class) {
			return (T) BigDecimal.ZERO;
		} else {
			throw new IllegalArgumentException("Unsupported element type: " + clazz.getName());
		}
	}

	@SuppressWarnings("unchecked")
	private T oneValue() {
		if (clazz == Integer.class) {
			return (T) Integer.valueOf(1);
		} else if (clazz == Double.class) {
			return (T) Double.valueOf(1.0);
		} else if (clazz == Float.class) {
			return (T) Float.valueOf(1.0f);
		} else if (clazz == Long.class) {
			return (T) Long.valueOf(1L);
		} else if (clazz == Short.class) {
			return (T) Short.valueOf((short) 1);
		} else if (clazz == Byte.class) {
			return (T) Byte.valueOf((byte) 1);
		} else if (clazz == BigDecimal.class) {
			return (T) BigDecimal.ONE;
		} else {
			throw new IllegalArgumentException("Unsupported element type: " + clazz.getName());
		}
	}

	/**
	 * Returns the value 2 of the element type.
	 *
	 * @return The value 2 of the element type.
	 */
	@SuppressWarnings("unchecked")
	private T twoValue() {
		if (clazz == Integer.class) {
			return (T) Integer.valueOf(2);
		} else if (clazz == Double.class) {
			return (T) Double.valueOf(2.0);
		} else if (clazz == Float.class) {
			return (T) Float.valueOf(2.0f);
		} else if (clazz == Long.class) {
			return (T) Long.valueOf(2L);
		} else if (clazz == Short.class) {
			return (T) Short.valueOf((short) 2);
		} else if (clazz == Byte.class) {
			return (T) Byte.valueOf((byte) 2);
		} else if (clazz == BigDecimal.class) {
			return (T) BigDecimal.valueOf(2);
		} else {
			throw new IllegalArgumentException("Unsupported element type: " + clazz.getName());
		}
	}

	@SuppressWarnings("unchecked")
	private T convertToGenericType(int value, Class<T> clazz) {
		if (clazz == Integer.class) {
			return (T) Integer.valueOf(value);
		} else if (clazz == Long.class) {
			return (T) Long.valueOf(value);
		} else if (clazz == Byte.class) {
			return (T) Byte.valueOf((byte) value);
		} else if (clazz == Short.class) {
			return (T) Short.valueOf((short) value);
		} else if (clazz == Float.class) {
			return (T) Float.valueOf(value);
		} else if (clazz == Double.class) {
			return (T) Double.valueOf(value);
		} else if (clazz == BigDecimal.class) {
			return (T) BigDecimal.valueOf(value);
		} else {
			throw new IllegalArgumentException("Unsupported type for binary matrix generation: " + clazz.getName());
		}
	}

	@SuppressWarnings("unchecked")
	private T convertToGenericType(double value, Class<T> clazz) {
		if (clazz == Integer.class) {
			return (T) Integer.valueOf((int) value); // Corrected: Cast double to int before casting to T
		} else if (clazz == Long.class) {
			return (T) Long.valueOf((long) value);
		} else if (clazz == Byte.class) {
			return (T) Byte.valueOf((byte) value);
		} else if (clazz == Short.class) {
			return (T) Short.valueOf((short) value);
		} else if (clazz == Float.class) {
			return (T) Float.valueOf((float) value);
		} else if (clazz == Double.class) {
			return (T) Double.valueOf(value);
		} else if (clazz == BigDecimal.class) {
			return (T) BigDecimal.valueOf(value);
		} else {
			throw new IllegalArgumentException("Unsupported type for binary matrix generation: " + clazz.getName());
		}
	}

	@SuppressWarnings("unused")
	private T convertToGenericType(T value, Class<T> clazz) {
		return value;
	}

	private boolean isZero(T value) {
		if (clazz == Integer.class) {
			return ((Integer) value).intValue() == 0;
		} else if (clazz == Double.class) {
			return ((Double) value).doubleValue() == 0.0;
		} else if (clazz == Float.class) {
			return ((Float) value).floatValue() == 0.0f;
		} else if (clazz == Long.class) {
			return ((Long) value).longValue() == 0L;
		} else if (clazz == Short.class) {
			return ((Short) value).shortValue() == (short) 0;
		} else if (clazz == Byte.class) {
			return ((Byte) value).byteValue() == (byte) 0;
		} else if (clazz == BigDecimal.class) {
			return ((BigDecimal) value).compareTo(BigDecimal.ZERO) == 0;
		} else {
			throw new IllegalArgumentException("Unsupported element type: " + clazz.getName());
		}
	}

	@SuppressWarnings("unchecked")
	private T abs(T value) {
		if (value instanceof Integer) {
			return (T) Integer.valueOf(Math.abs((Integer) value));
		} else if (value instanceof Long) {
			return (T) Long.valueOf(Math.abs((Long) value));
		} else if (value instanceof Float) {
			return (T) Float.valueOf(Math.abs((Float) value));
		} else if (value instanceof Double) {
			return (T) Double.valueOf(Math.abs((Double) value));
		} else if (value instanceof Short) {
			return (T) Short.valueOf((short) Math.abs((Short) value));
		} else if (value instanceof Byte) {
			return (T) Byte.valueOf((byte) Math.abs((Byte) value));
		} else if (value instanceof BigDecimal) {
			return (T) ((BigDecimal) value).abs();
		}
		throw new UnsupportedOperationException("Unsupported type for absolute value");
	}

	/**
	 * Method. Returns the square root of an element.
	 *
	 * @param x The element whose square root we want to compute.
	 * @return The square root of the element.
	 */
	private T sqrt(T x) {
		// Check if the element is non-negative
		if (compareTo(x, zeroValue()) < 0) {
			throw new IllegalArgumentException("Cannot take the square root of a negative number");
		}

		// If x is null, return null or zero
		if (x == null) {
			return zeroValue(); // instead of null
		}

		// Compute the square root using the Babylonian method
		T guess = divide(x, twoValue()); // Initial guess
		T prevGuess = zeroValue();

		// Iterate until convergence
		while (!guess.equals(prevGuess)) {
			prevGuess = guess;
			guess = divide(add(guess, divide(x, guess)), twoValue());
		}

		return guess;
	}

	/**
	 * Computes the nth root of a given number using the Babylonian method.
	 *
	 * @param x The number for which the nth root is to be computed.
	 * @param p The degree of the root to be computed.
	 * @return The nth root of the given number.
	 * @throws IllegalArgumentException if the number is negative.
	 */
	private T nthRoot(T x, int p) {
		// Check if the element is non-negative
		if (compareTo(x, zeroValue()) < 0) {
			throw new IllegalArgumentException("Cannot take the square root of a negative number");
		}

		// Compute the nth root using the Babylonian method
		T tolerance = convertToGenericType(1e-15, clazz);
		T guess = divide(x, convertToGenericType(p, clazz));
		T prevGuess = zeroValue();
		T precision = convertToGenericType(tolerance, clazz); // Define a small precision value

		while (!guess.equals(prevGuess)) {
			prevGuess = guess;
			guess = divide(add(guess, divide(x, guess)), convertToGenericType(p, clazz));
			// Check if the difference between the current guess and the previous guess is
			// less than the precision
			if (compareTo(abs(subtract(guess, prevGuess)), precision) < 0) {
				break;
			}
		}
		return guess;
	}

	/**
	 * Method. Calculate the square root of a value.
	 *
	 * @param a The value to calculate the square root of.
	 * @return The square root of the value.
	 * @throws UnsupportedOperationException if the type is not supported.
	 */
	@SuppressWarnings("unchecked")
	private T squareRoot(T a) {
		if (a == null) {
			a = zeroValue();
		}
		if (clazz == Integer.class) {
			return (T) Integer.valueOf((int) Math.sqrt((Integer) a));
		} else if (clazz == Double.class) {
			return (T) Double.valueOf(Math.sqrt((Double) a));
		} else if (clazz == Float.class) {
			return (T) Float.valueOf((float) Math.sqrt((Float) a));
		} else if (clazz == Long.class) {
			return (T) Long.valueOf((long) Math.sqrt((Long) a));
		} else if (clazz == Short.class) {
			return (T) Short.valueOf((short) Math.sqrt((Short) a));
		} else if (clazz == Byte.class) {
			return (T) Byte.valueOf((byte) Math.sqrt((Byte) a));
		} else if (clazz == BigDecimal.class) {
			return (T) ((BigDecimal) a).sqrt(MathContext.DECIMAL128); // can use other desired MathContext
		} else {
			throw new IllegalArgumentException("Unsupported element type: " + clazz.getName());
		}
	}

	private T pow(T value, int power) {
		if (compare(value, zeroValue()) == 0 && power <= 0) {
			throw new IllegalArgumentException("Base 0 with non-positive exponent is undefined.");
		}

		if (power == 0) {
			return oneValue();
		}

		if (power == 1) {
			return value;
		}

		T result = oneValue();
		for (int i = 0; i < Math.abs(power); i++) {
			if (Double.isInfinite((double) mult(result, value))) {
				throw new ArithmeticException("Arithmetic overflow occurred.");
			}
			result = mult(value, result);
		}

		if (power < 0) {
			return divide(oneValue(), result);
		} else {
			return result;
		}
	}

	@SuppressWarnings("unused")
	private T signum(T x, T y) {
		int comparison = compare(x, zeroValue());

		if (comparison == 0) {
			return zeroValue();
		} else if (comparison < 0) {
			return negate(oneValue());
		} else {
			return oneValue();
		}
	}

	private int compare(T a, T b) {
		/*
		 * Integer a = 5; Integer b = 10;
		 * 
		 * int result = a.compareTo(b); if (result < 0) {
		 * System.out.println("a est inférieur à b"); } else if (result > 0) {
		 * System.out.println("a est supérieur à b"); } else {
		 * System.out.println("a est égal à b"); }
		 */
		// a.compareTo(b) ==> result = a-b
		if (a instanceof Integer && b instanceof Integer) {
			return ((Integer) a).compareTo((Integer) b);
		} else if (a instanceof Double && b instanceof Double) {
			return ((Double) a).compareTo((Double) b);
		} else if (a instanceof Float && b instanceof Float) {
			return ((Float) a).compareTo((Float) b);
		} else if (a instanceof Long && b instanceof Long) {
			return ((Long) a).compareTo((Long) b);
		} else if (a instanceof Short && b instanceof Short) {
			return ((Short) a).compareTo((Short) b);
		} else if (a instanceof Byte && b instanceof Byte) {
			return ((Byte) a).compareTo((Byte) b);
		} else if (a instanceof BigDecimal && b instanceof BigDecimal) {
			return ((BigDecimal) a).compareTo((BigDecimal) b);
		} else {
			throw new IllegalArgumentException(
					"Unsupported types for comparison: " + a.getClass().getName() + " and " + b.getClass().getName());
		}
	}

	// Compare two values
	private int compareTo(T a, T b) {
		if (clazz == Float.class) {
			return Float.compare((Float) a, (Float) b);
		} else if (clazz == Double.class) {
			return Double.compare((Double) a, (Double) b);
		} else if (clazz == BigDecimal.class) {
			return ((BigDecimal) a).compareTo((BigDecimal) b);
		} else if (Number.class.isAssignableFrom(clazz)) {
			// Gérer d'autres types numériques
			double aValue = ((Number) a).doubleValue();
			double bValue = ((Number) b).doubleValue();
			return Double.compare(aValue, bValue);
		} else {
			throw new IllegalArgumentException("Unsupported type for comparison: " + clazz.getName());
		}
	}

}// class
