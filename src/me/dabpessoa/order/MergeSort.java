package me.dabpessoa.order;

public class MergeSort {

	/**
     * Algoritmo Merge Sort.
     */
    public static void mergeSort( Comparable [ ] a ) {
        Comparable [ ] tmpArray = new Comparable[ a.length ];
        mergeSort( a, tmpArray, 0, a.length - 1 );
    }
    
    /**
     * M�todo interno que faz chamadas recursivas
     */
    private static void mergeSort( Comparable [ ] a, Comparable [ ] tmpArray,
            int left, int right ) {
        if( left < right ) {
            int center = ( left + right ) / 2;
            mergeSort( a, tmpArray, left, center );
            mergeSort( a, tmpArray, center + 1, right );
            merge( a, tmpArray, left, center + 1, right );
        }
    }
    
    /**
     * Met�do interno que ordena duas partes de um subarray.
     */
    private static void merge( Comparable [ ] a, Comparable [ ] tmpArray,
            int leftPos, int rightPos, int rightEnd ) {
        int leftEnd = rightPos - 1;
        int tmpPos = leftPos;
        int numElements = rightEnd - leftPos + 1;
        
        // loop principal
        while( leftPos <= leftEnd && rightPos <= rightEnd )
            if( a[ leftPos ].compareTo( a[ rightPos ] ) <= 0 )
                tmpArray[ tmpPos++ ] = a[ leftPos++ ];
            else
                tmpArray[ tmpPos++ ] = a[ rightPos++ ];
        
        while( leftPos <= leftEnd )     
            tmpArray[ tmpPos++ ] = a[ leftPos++ ];
        
        while( rightPos <= rightEnd )   
            tmpArray[ tmpPos++ ] = a[ rightPos++ ];
        
        // Copiando o array tempor�rio de volta
        for( int i = 0; i < numElements; i++, rightEnd-- )
            a[ rightEnd ] = tmpArray[ rightEnd ];
    }
	
}
