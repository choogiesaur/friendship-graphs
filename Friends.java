package friends;

import java.lang.*;
import java.io.*;
import java.util.*;

// AUTHORS: JONATHAN SUKENIK/FIRAS SATTAR © 2013, YO!!!

public class Friends {

	public static void main(String[] args) throws IOException {
		
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in)) ;
		boolean success = false;
		Graph g = new Graph();
		
		while(true){
			try {
				System.out.print("Enter name of input file (____.txt): ");
				String fileName = keyboard.readLine();
				g.buildGraph(fileName);
				System.out.println("Built graph from " + fileName + "!");
				System.out.println();
				break;
			}catch(FileNotFoundException f){
				System.out.println("File not found ;(");
			}catch(IOException e){
				System.out.println("Wat you doin' son?");
			}
		}
		
		int choice = -1;
		while(true){
			choice = -1;
			System.out.println("Options");
			System.out.println("-------");
			System.out.println("1. Students in a school \n");
			System.out.println("2. Shortest introduction chain");
			System.out.println("3. Cliques at a school");
			System.out.println("4. Connectors");
			System.out.println("5. Quit\n");
			try{
				System.out.print("Enter your choice... ");
				choice = Integer.parseInt(keyboard.readLine());
				if(choice < 1 || choice > 5){
					NumberFormatException e = new NumberFormatException();
					throw e;
				}
			}catch(NumberFormatException e){
				System.out.println("Must be a numbah between 1 and 5!");
				System.out.println();
			}if(choice == 1){//students in a school
				String school = null;
				while(true){
					try{
						System.out.print("Enter name of school: ");
						school = keyboard.readLine();
						System.out.println();
						System.out.println("-----------------------");
						System.out.println("Students at " + school + ":");
						System.out.println("-----------------------");
						g.subgraph(school);
						System.out.println();
						break;
					}catch(IOException e){
						System.out.println("Wtf you doin son?");
					}
				}
			}if(choice == 2){
				System.out.println();
				String a = null; String b = null; 
				while(true){
					try{
						System.out.print("Enter name of person 1: ");
						a = keyboard.readLine();
						System.out.print("Enter name of person 2: ");
						b = keyboard.readLine();
						System.out.println();
						
						g.shortestPath(a, b);
						break;
					}catch(IOException e){
						System.out.println("Wtf you doin son?");
					}
				}System.out.println();
			}if(choice == 3){
				String school = null;
				while(true){
					try{
						System.out.print("Enter name of school: ");
						school = keyboard.readLine();
						System.out.println();
						g.cliques(school);
						System.out.println();
						break;
					}catch(IOException e){
						System.out.println("Wtf you doin son?");
					}
				}
			}if(choice == 4){
				g.printConnectors();
			}if(choice == 5){
				System.out.println("Bye! Program closing...");
				return;
			}
		}
	}
}
