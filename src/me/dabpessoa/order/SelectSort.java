package me.dabpessoa.order;

public class SelectSort {

	public void ordenaSelecao(int[] vetor){
		int min;
		for(int i = 0; i < vetor.length; i++){
			min = i; //posicao do vetor
			for(int j = i+1; j < vetor.length; j++){
				if(vetor[j] < vetor[min]){
					min = j;
				}
			}
			if(min != i){
				int temp = vetor[i];
				vetor[i] = vetor[min];
				vetor[min] = temp;
			}
		}
	}
	
}
