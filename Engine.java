/* 
 * @author - Zanyar Sherwani
 * @author - Omar Zairi
 *
 * This is the driver class that displays
 * menu prompts to the user and calls other
 * classes as helpers.
 *
 *
 */

import java.util.Scanner; 
import java.io.IOException;
import java.util.ArrayList;
import java.io.*;

public class Engine
{
   // initializes the quotelist and stores all the quotes from xml file into quoteList variable
   static QuoteList quoteList = new QuoteList();
   static String quoteFileName = "quotes.xml";
   
   // blacklist of profane words
   static Blacklist blkList = new Blacklist(); 
   
   // the current quote
   static Quote quote = new Quote();
   
   
   /*   static Scanner in = new Scanner(System.in);
      static Scanner searcher = new Scanner(System.in);
      static QuoteSaxParser qParser;
     //public Engine(){
    qParser = new QuoteSaxParser (quoteFileName);
     quoteList = qParser.getQuoteList();
    // }*/
    static int lastId;
   public static void main(String[] args){
      // keeps track of user input, if user enters 'exit' the while loop below will stop
      String tracker = "a";
       
      // parses the xml file into the quotelist
      Scanner in = new Scanner(System.in);
      Scanner searcher = new Scanner(System.in);
      QuoteSaxParser qParser = new QuoteSaxParser (quoteFileName);
      quoteList = qParser.getQuoteList();
      lastId = Integer.parseInt(getIdOfLastQuote())+1;
      // loop runs until user enters 'exit'
      while( !tracker.equalsIgnoreCase("exit") ){
         
         printRandomQuote();
         
          // user menu
         System.out.println("\nPress 1 to get another random quote.");
         System.out.println("Press 2 to search by a keyword in a quote.");
         System.out.println("Press 3 to search by an author.");
         System.out.println("Press 4 to search by both.");
         System.out.println("Press 5 to search by keywords.");
         System.out.println("Press 6 to add a quote.");
         System.out.println("Enter 'exit' to quit the program.");
         
         String inn = in.nextLine();
         tracker = inn;
         
         // user input from possible menu options
         String key;
         
         // runs desired use case based on user input
         switch(tracker) {
            
            case "2":
               System.out.println("Enter search query.");
               System.out.print("-> ");
               key = searcher.nextLine();
               searchQuote( key );
               break;
            
            case "3":
               System.out.println("Enter search query.");
               System.out.print("-> ");
               key = searcher.nextLine();
               searchAuthor( key );
               break;
            
            case "4":
               System.out.println("Enter search query.");
               System.out.print("-> ");
               key = searcher.nextLine();
               searchBoth( key );
               break;
            
            case "5":
              // Search by keywords
              System.out.println("Enter search query.");
              System.out.print("-> ");
             key = searcher.nextLine();
             searchKeywords( key );
             break;
            
            case "6":
               System.out.println("Enter the quote.");
               System.out.print("-> ");
               key = searcher.nextLine();
               String newQuote = key;
               
               if(blkList.containsBadWord(newQuote))
                  System.out.println("Sorry, innappropriate language will not be added to our database!");
               
               else{
                  
                    System.out.println("Enter the author.");
                    System.out.print("-> ");
                    key = searcher.nextLine();
                    String newAuthor = key;
                    
                    if(blkList.containsBadWord(newAuthor))
                        System.out.println("Sorry, innappropriate language will not be added to our database!");
                     else
                     {
                      
                        String keywordString = null;
                        
                          // Get keywords
                          System.out.println("Enter keywords.");
                          System.out.println("* Seperate keyword or phrases with a comma *");
                          System.out.println("* Limit of 5 keywords and 25 characters per each keyword *");
                          System.out.println("* Example: eating, motivate me, fitness, study guide *");
                          System.out.print("-> ");
                          key = searcher.nextLine();
                        
                          keywordString = checkKeywords(key);
                          
                        
                        addQuote( newQuote , newAuthor , keywordString, key );
                     }
               }
               break;
         }
      }
   }

   /*
    * @author - Zanyar Sherwani
    *
    * This function uses a non-optimal
    * way to check for profanity within 
    * any string passed in
    *
    */
   public static boolean checkForProfanity(String str){
      return true;
   }
  
   /* 
    * @author : Zanyar Sherwani
    * @return id of last quote in the xml file
    *
    * returns the id of the last quote in the xml
    * file so that the next quote's id can be incremented
    * by one
    */
    public static String getIdOfLastQuote(){
      return quoteList.getLastId();
  
    }

   /*
    * @author : Omar Zairi
    * @param text of the quote
    * @param author of the quote
    *
    * Gets a quote text and author name from the user
    * Adds the quote to the current quote list array
    * and adds it to the xml file
    * 
    */
   public static void addQuote( String quote , String author , String keywords, String key )
   {
      // create new quote

      Quote newQuote = new Quote(author,quote,strToArray(key).toArray(new String[strToArray(key).size()]), Integer.toString(lastId));
      
      // put new quote in current quote list
      quoteList.setQuote( newQuote );
      
      // print the old list into a .tmp file and add the new quote to the file
      try (BufferedReader br = new BufferedReader(new FileReader("quotes.xml"))) {
         
         PrintWriter pw = new PrintWriter("quotes.tmp", "UTF-8");
         String line;
         
         // add old quotes to new file
         while ((line = br.readLine()) != null) {
            if( !line.equals("</quote-list>") )
               pw.println(line);
         }
         
         
         
         // write new quote to file
         pw.println("   <quote>");
         pw.println("      <id>"+lastId+"</id>");
         pw.println("      <quote-text>"+quote+"</quote-text>");
         pw.println("      <author>"+author+"</author>");
         pw.println(keywords);
         pw.println("   </quote>");
         
         // close out the list
         pw.println("</quote-list>");
         
         // rename the file 
         File renamer = new File("quotes.tmp");
         renamer.renameTo(new File("quotes.xml"));
         
         br.close();
         pw.close();
      }
      catch(Exception e){
         System.out.println("Error with file.");
      }

      lastId++;
   }
   
   // Searches quote list by quote text
   public static void searchQuote( String keyword )
   {
      
      // retrieve list of results based on search
      QuoteList results = quoteList.search( keyword , 1 );
      
      // output the results
      System.out.println("Results ("+results.getSize()+"):");
      
      for ( int i = 0 ; i < results.getSize() ; i++ )
      {
         Quote quote1 = results.getQuote(i);
         System.out.println( "  "+quote1.getQuoteText() );
         System.out.println( "  -"+quote1.getAuthor() + "\n" );
      }
   }
  public static void searchKeywords(String keywords){
      QuoteList results = quoteList.search( keywords, 3 );

      // output the results
      System.out.println("Results ("+results.getSize()+"):");
      
      for ( int i = 0 ; i < results.getSize() ; i++ )
      {
         Quote quote1 = results.getQuote(i);
         System.out.println( "  "+quote1.getQuoteText() );
         System.out.println( "  -"+quote1.getAuthor() + "\n" );
      }
  }
   // Searches quote list by author name
   public static void searchAuthor( String keyword )
   {
      QuoteList results = quoteList.search( keyword , 0 );
      System.out.println("Results ("+results.getSize()+"):");
      
      for ( int i = 0 ; i < results.getSize() ; i++ )
      {
         Quote quote1 = results.getQuote(i);
         System.out.println( "  "+quote1.getQuoteText() );
         System.out.println( "  -"+quote1.getAuthor() + "\n" );
      }
   }
   
   // Searchs quote list by author name and quote text
   public static void searchBoth( String keyword )
   {
      QuoteList results = quoteList.search( keyword , 2 );
      System.out.println("Results ("+results.getSize()+"):");
      
      for ( int i = 0 ; i < results.getSize() ; i++ )
      {
         Quote quote1 = results.getQuote(i);
         System.out.println( "  "+quote1.getQuoteText() );
         System.out.println( "  -"+quote1.getAuthor() + "\n" );
      }
   }
   
   // Gets a random quote from quote list   
   public static void printRandomQuote()
   {
      quote = quoteList.getRandomQuote();
      
      System.out.println("\nRandom quote of the day:");
      System.out.println( "  "+quote.getQuoteText() );
      System.out.println( "  -"+quote.getAuthor() );
   }

   public static ArrayList<String> strToArray(String keywords){
       
  ArrayList<String> keywordList = new ArrayList<String>();


      String[] wordsWithComma = keywords.split(",");
     if(wordsWithComma != null){ 
            int listSize = wordsWithComma.length;

      for(int i = 0; i < listSize && i < 5 ; i++)
      {
        String trimmedWord = wordsWithComma[i];
        trimmedWord = trimmedWord.trim();
     
        if( trimmedWord.length() <= 25 && !trimmedWord.equals("") )
        {
          keywordList.add(trimmedWord);
        }

      }   
      return keywordList; 
     }
     else{
        keywordList.add(keywords);
        return keywordList;
     }
   }
   
   /*
    * Author : Omar Zairi
    *
    * @param : String of keywords separated by commas
    * @return : String of keywords separated by commas
    *          in between XML <keywords> brackets
    *
    * Gets a string passed in that contains the user's keywords
    * for their quote. Each keyword is split by a comma and then
    * checked if it is a valid keyword. Once all keywords are 
    * validated it is returned in a string in between the needed
    * XML brackets.
    *
    * Example : <keywords>health,fitness,food</keywords>
    */
   public static String checkKeywords(String keywords)
   {
      // Split keyword string by commas
      String[] wordsWithComma = keywords.split(",");
      
      // List that will store final list of validated keywords
      ArrayList<String> keywordList = new ArrayList<String>();

      // Amount of words separated by a comma
      int listSize = wordsWithComma.length;

      // Validates each keyword in wordsWithComma l ist
      for(int i = 0; i < listSize && i < 5 ; i++)
      {

        // Trims the white space from keyword
        String trimmedWord = wordsWithComma[i];
        trimmedWord = trimmedWord.trim();
     
        // Checks to see that the keyword isn't blank and is less than
        // 26 characters
        if( trimmedWord.length() <= 25 && !trimmedWord.equals("") )
        {
          keywordList.add(trimmedWord);
        }

      }

      // String to put keywords in XML form
      String xmlString = "      <keywords>";
      
      for(int i = 0; i < keywordList.size() ; i++)
      {
        if( i == keywordList.size()-1 )
          xmlString += keywordList.get(i);
        else
          xmlString += keywordList.get(i)+",";
      }
      
      xmlString += "</keywords>";

      return xmlString;
   }
} 

