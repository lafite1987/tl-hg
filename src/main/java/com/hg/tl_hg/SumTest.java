package com.hg.tl_hg;

public class SumTest {

	public static void main(String[] args) {
		int[] arr = new int[]{324, 353, 211, 53, 534, 521, 13, 543, 54, 112, 5345, 545, 1123, 1231, 144};
		SumTest sumTest = new SumTest(arr);
		int total = sumTest.get(0, 2);
		System.out.println(total);
	}

	private int[] sum;
	
	public SumTest(int[] arr) {
		this.sum = new int[arr.length];
		this.sum[0] = arr[0];
		for(int i = 1; i < arr.length - 1; i++) {
			this.sum[i] = sum[i - 1] + arr[i];
		}
	}
	
	public int get(int i, int j) {
		if(i <= 0) {
			return sum[j];
		}
		int total = sum[j] - sum[i - 1];
		return total;
	}
}
