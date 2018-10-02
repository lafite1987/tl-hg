package com.hg.tl_hg;

import java.util.Arrays;

public class QuickSort {

	public static int partition(int[] arr, int i, int j) {
		int base = arr[i];
		while(i < j) {
			while(i < j && arr[j] >= base) {
				j--;
			}
			if(i < j) {
				arr[i] = arr[j];
			}
			while(i < j && arr[i] <= base) {
				i++;
			}
			if(i < j) {
				arr[j] = arr[i];
			}
		}
		arr[i] = base;
		return i;
	}
	
	public static void quicksort(int[] arr, int start, int end) {
		if(start > end) {
			return;
		}
		int partition = partition(arr, start, end);
		quicksort(arr, 0, partition - 1);
		quicksort(arr, partition + 1, end);
	}
	
	public static int search(int[] seq, int v, int low, int high) {
        while (low <= high) {
            int mid = (low + high) / 2;
            if (v == seq[mid]) {
                return mid;
            } else if (v > seq[mid]) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return Integer.MIN_VALUE;
    }
	
	public static void main(String[] args) {
		int[] arr = new int[]{32, 54, 52, 33, 56, 85, 11, 45, 66};
		quicksort(arr, 0, arr.length - 1);
		System.out.println(Arrays.toString(arr));
		int index = search(arr, 52, 0, arr.length - 1);
		System.out.println(index);
	}
	
	
}
