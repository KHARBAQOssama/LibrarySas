package helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Models.Book;
import library.Main;

public class Displayer {
    public static Scanner console = new Scanner(System.in);
    public static void displayOptions(String[] options){
        int length= options.length;
        for (int i=0 ;i< length;i++){
            int number = i+1;
            String option = "|     "+ Text.BOLD+Text.GREEN +number+"-  " +Text.GREEN +Text.BOLD + Text.RESET +options[i];
            int count = 100 - 70 - options[i].length();
            for (int j=0;j<count;j++){
                option += " ";
            }
            option += "|";
            System.out.println(option);
        }
    }

    public  static  void lastChoices(){
        System.out.println("do you wanna :");
        System.out.println("1- go back to home");
        System.out.println("2- end process");
        int choice = console.nextInt();

        if(choice == 1){
            Main.beginProcess();
        }else{
            return;
        }
    }

    public static void displayBook(Book book){
        System.out.println("Title : "+book.getTitle());
        System.out.println("ISBN : "+book.getISBN());
        System.out.println("Author : "+book.getAuthor().getName());
        System.out.println("Quantity : "+book.getQuantity());
        System.out.println("Available Books : "+book.getAvailable());
        System.out.println("Lost Books: "+book.getLost());
        System.out.println("-----------------------------------------");
    }
    public static void displayBooks(List<Book> books){
        if(books.size() == 0){
            System.out.println("There is no books for the moment");
        }else{
            for (Book book : books){
                displayBook(book);
            }
        }

        lastChoices();
    }
}
