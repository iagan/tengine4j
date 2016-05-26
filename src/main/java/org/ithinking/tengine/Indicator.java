package org.ithinking.tengine;

/**
 * 指示器
 * 
 * @author fuchujian
 *
 */
public class Indicator {

	public int index = 0;
	private int row = 1;
	private int size = 0;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isOdd() {
		return index % 2 != 0;
	}

	public boolean isEven() {
		return index % 2 == 0;
	}

	/**
	 * 判断奇数行
	 * 
	 * @return
	 */
	public boolean isOddRow() {
		return row % 2 != 0;
	}

	/**
	 * 判断偶数行
	 * 
	 * @return
	 */
	public boolean isEvenRow() {
		return row % 2 == 0;
	}

	public boolean isFirst() {
		return index == 0;
	}

	public boolean isLast() {
		return index == size - 1;
	}

	/**
	 * 判断是否第一行
	 * 
	 * @return
	 */
	public boolean isFirstRow() {
		return row == 1;
	}

	/**
	 * 判断是否最后一行(与index判断相同)
	 * @return
	 */
	public boolean isLastRow() {
		return isLast();
	}

	@Override
	public String toString() {
		return Integer.toString(index);
	}
}
