package me.dabpessoa.order;

public class HeapSort {

	/**
     * Algoritmo Heap Sort.
     */
    public static void heapsort( Comparable [ ] a )
    {
        for( int i = a.length / 2; i >= 0; i-- )  /* buildHeap */
            percDown( a, i, a.length );
        for( int i = a.length - 1; i > 0; i-- )
        {
            swapReferences( a, 0, i );            /* deleteMax */
            percDown( a, 0, i );
        }
    }

    private static int leftChild( int i )
    {
        return 2 * i + 1;
    }

    private static void percDown( Comparable [ ] a, int i, int n )
    {
        int child;
        Comparable tmp;

        for( tmp = a[ i ]; leftChild( i ) < n; i = child )
        {
            child = leftChild( i );
            if( child != n - 1 && a[ child ].compareTo( a[ child + 1 ] ) < 0 )
                child++;
            if( tmp.compareTo( a[ child ] ) < 0 )
                a[ i ] = a[ child ];
            else
                break;
        }
        a[ i ] = tmp;
    }
    
    
    /**
     * Method para trocar elementos em um array.
     */
    public static final void swapReferences( Object [ ] a, int index1, int index2 )
    {
        Object tmp = a[ index1 ];
        a[ index1 ] = a[ index2 ];
        a[ index2 ] = tmp;
    }
	
}
