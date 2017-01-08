package me.dabpessoa.order;

import java.util.Queue;

public class RadixSort {

//	 private static final int MAX_CHARS = 28;
//     private static void radixSort (String [] v) 
//    { 
//       Queue queues [ ] = createQueues();
//       for ( int pos = maxSize( v ) - 1; pos >= 0; pos--) 
//       { 
//          for (int i = 0; i < v.length; i++) 
//          { 
//             int q = queueNo( v [ i ], pos );
//             queues [ q ].enqueue( v [ i ] );
//          }
//          restore ( queues, v );
//       }
//    }
//     private static void restore ( Queue [ ] qs, String [ ] v) 
//    { 
//       int contv = 0;
//       for ( int q = 0; q < qs.length; q++ )
//          while (! qs[ q ].empty( ) )
//             v [ contv++ ] = qs [ q ].dequeue( );
//    }
// 	
//     private static Queue[] createQueues()
//    { 
//       Queue[] result = new Queue [ MAX_CHARS ];
//       for (int i = 0; i < MAX_CHARS; i++)
//          result [ i ] = new Queue();
//       return result ;
//    }
// 
//     private static int queueNo ( String string , int pos) 
//    { 
//       if (pos >= string.length ()) 
//          return 0;
//       char ch = string .charAt(pos);
//       if (ch >= 'A' && ch <= 'Z') 
//          return ch - 'A' + 1;
//       else if (ch >= 'a' && ch <= 'z') 
//          return ch - 'a' + 1;
//       else 
//          return 27;
//    }
//     private static int maxSize(String [] v)
//    {
//       int maiorValor = v[0]. length ();
//    
//       for (int i = 1; i < v.length; i++)
//          if (maiorValor < v[i ]. length ())
//             maiorValor = v[i ]. length ();
//       return maiorValor;
//    }
	
}
