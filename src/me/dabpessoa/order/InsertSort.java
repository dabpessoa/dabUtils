package me.dabpessoa.order;

public class InsertSort {

	public void ordenaInsercao(int[] vetor){
		int n = vetor.length;
		for (int i = 0; i < n; i++) {
			for (int j = i ; j > 0 ; j--) {
				if (vetor[j-1] > vetor[j]) {
					int temp = vetor[j];
					vetor[j] = vetor[j-1];
					vetor[j-1] = temp;
				}
			}
		}
	}
	
}
