import java.util.Scanner; 
import java.io.IOException;
import java.util.ArrayList;



public class engine
{
   // initializes the quotelist and stores all the quotes from xml file into quoteList variable
   static QuoteList quoteList = new QuoteList();
   static String quoteFileName = "quotes.xml";
   

   // the current quote
   static Quote quote = new Quote();
   
   
   public static void main(String[] args){
      
      
      // keeps track of user input, if user enters 'exit' the while loop below will stop
      String tracker = "a";
      Scanner in = new Scanner(System.in);
      Scanner searcher = new Scanner(System.in);
      QuoteSaxParser qParser = new QuoteSaxParser (quoteFileName);
      quoteList = qParser.getQuoteList();
      
      // loop runs until user enters 'exit'
      while( !tracker.equalsIgnoreCase("exit") ){
         
         
         switch(tracker) {
            
            case "a":
               printRandomQuote();
               break;
            case "1":
               printRandomQuote();
               break;
            case "2":
               System.out.println("Enter keyword.");
               String key = searcher.nextLine();
               searchQuote( key );
               break;
            case "3":
               System.out.println("Enter keyword.");
               String key = searcher.nextLine();
               searchAuthor( key );
               break;
            case "4":
               System.out.println("Enter keyword.");
               String key = searcher.nextLine();
               searchBoth( key );
               break;
         }
         
         
         
         
         System.out.println("\nPress 1 to get another random quote.");
         System.out.println("Press 2 to search by a keyword in a quote.");
         System.out.println("Press 3 to search by an author.");
         System.out.println("Press 4 to search by both.");
         System.out.println("Enter 'exit' to quit the program.");
         String inn = in.nextLine();
         tracker = inn;
         
         
         
      }
      
     
   }
   
   public static void searchQuote( String keyword )
   {
      QuoteList results = quoteList.search( keyword , 1 );
      System.out.println("Results ("+results.quoteArray.size()+"):");
      for ( int i = 0 ; i < results.quoteArray.size() ; i++ )
      {
         Quote quote1 = quoteList.quoteArray.get(i);
         System.out.println( "  "+quote1.getQuoteText() );
         System.out.println( "  -"+quote1.getAuthor() + "\n" );
      }
   }
   
   public static void searchAuthor( String keyword )
   {
      QuoteList results = quoteList.search( keyword , 0 );
      System.out.println("Results ("+results.quoteArray.size()+"):");
      for ( int i = 0 ; i < results.quoteArray.size() ; i++ )
      {
         Quote quote1 = quoteList.quoteArray.get(i);
         System.out.println( "  "+quote1.getQuoteText() );
         System.out.println( "  -"+quote1.getAuthor() + "\n" );
      }
   }
   
   public static void searchBoth( String keyword )
   {
      QuoteList results = quoteList.search( keyword , 2 );
      System.out.println("Results ("+results.quoteArray.size()+"):");
      for ( int i = 0 ; i < results.quoteArray.size() ; i++ )
      {
         Quote quote1 = quoteList.quoteArray.get(i);
         System.out.println( "  "+quote1.getQuoteText() );
         System.out.println( "  -"+quote1.getAuthor() + "\n" );
      }
   }
   
   public static void printRandomQuote()
   {
      quote = quoteList.getRandomQuote();
      System.out.println("\nRandom quote of the day:");
      System.out.println( "  "+quote.getQuoteText() );
      System.out.println( "  -"+quote.getAuthor() );
   }
} 
