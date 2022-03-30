/* Name: 
 * Course: CNT 4714 - Summer 2020
 * Assignment title: Project 1 - Event-driver Enterprise Simulation
 * Date: Sunday May 31, 2020
 */

package project1;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.awt.event.ActionEvent;

public class gui extends JFrame {

	// main pane
	private static JPanel contentPane;
	
	// text fields
	private static JTextField textNumItems;
	private static JTextField textBookID;
	private static JTextField textQuantity;
	private static JTextField textInfo;
	private static JTextField textSubtotal;
	
	// labels
	private static JLabel lblNumItem;
	private static JLabel lblBookID;
	private static JLabel lblQuantity;
	private static JLabel lblInfo;
	private static JLabel lblSubtotal;
	
	// smaller pane for buttons
	private static JPanel panelBut;
	
	// buttons
	private static JButton btnProcess;
	private static JButton btnConfirm;
	private static JButton btnView;
	private static JButton btnFinish;
	private static JButton btnNew;
	private static JButton btnExit;
	
	// variables used throughout the program
	public static String lineNum = "", line = "", delimiter = ", ";
	public static String[] currLine;
	public static int found = 0, bookNum = 1, discount = 0, numItems = 0, quantity = 0, tax = 6;
	public static float subtotal = 0, itemTotal = 0, itemPrice = 0;
	public static ArrayList<String> booksInCart = new ArrayList<String>();
	public static ArrayList<String> booksInCart2 = new ArrayList<String>();
	public static ArrayList<String> booksForFile = new ArrayList<String>();
	
	// file variables used throughout the program
	public static File inFile = new File("inventory.txt");
	public static Scanner scanner = null;
	public static File outFile = new File("transactions.txt");
	public static FileWriter writer = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui frame = new gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public gui() {
		
		// overall pane initial customization
		setTitle("Old Book Shop");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 665, 210);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// number of items label initial customization
		lblNumItem = new JLabel("Enter number of items in this order:");
		lblNumItem.setBounds(10, 11, 204, 14);
		contentPane.add(lblNumItem);
		
		// number of items text field initial customization
		textNumItems = new JTextField();
		textNumItems.setBounds(224, 8, 415, 20);
		contentPane.add(textNumItems);
		textNumItems.setColumns(10);
		
		// book id label initial customization
		lblBookID = new JLabel("Enter Book ID for item #" + bookNum + ":");
		lblBookID.setBounds(10, 36, 204, 14);
		contentPane.add(lblBookID);
		
		// book id text field initial customization
		textBookID = new JTextField();
		textBookID.setColumns(10);
		textBookID.setBounds(224, 33, 415, 20);
		contentPane.add(textBookID);
		
		// quantity label initial customization
		lblQuantity = new JLabel("Enter the Quantity for item #" + bookNum + ":");
		lblQuantity.setBounds(10, 61, 204, 14);
		contentPane.add(lblQuantity);
		
		// quantity text field initial customization
		textQuantity = new JTextField();
		textQuantity.setColumns(10);
		textQuantity.setBounds(224, 58, 415, 20);
		contentPane.add(textQuantity);
		
		// information label initial customization
		lblInfo = new JLabel("Item #" + bookNum + " info:");
		lblInfo.setBounds(10, 86, 204, 14);
		contentPane.add(lblInfo);
		
		// information text field initial customization
		textInfo = new JTextField();
		textInfo.setEditable(false);
		textInfo.setColumns(10);
		textInfo.setBounds(224, 83, 415, 20);
		contentPane.add(textInfo);
		
		// subtotal label initial customization
		lblSubtotal = new JLabel("Order subtotal for " + (bookNum - 1) + " item(s):");
		lblSubtotal.setBounds(10, 111, 204, 14);
		contentPane.add(lblSubtotal);
		
		// subtotal text field initial customization
		textSubtotal = new JTextField();
		textSubtotal.setEditable(false);
		textSubtotal.setColumns(10);
		textSubtotal.setBounds(224, 108, 416, 20);
		contentPane.add(textSubtotal);
		
		// button panel initial customization
		panelBut = new JPanel();
		panelBut.setBackground(Color.BLUE);
		panelBut.setBounds(0, 136, 649, 35);
		contentPane.add(panelBut);
		
		// process button initial customization and action when pressed
		btnProcess = new JButton("Process Item #" + bookNum);
		btnProcess.setBounds(10, 142, 111, 23);
		panelBut.add(btnProcess);
		btnProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnProcess) {
					// check if textNumItems is empty
					// if not then grab the data to send to processing
					String numItemsText = textNumItems.getText();
					if (numItemsText.length() == 0) {
						JOptionPane.showMessageDialog(null, "You need to input the number of items in the order.");
						return;
					}
					numItems = Integer.parseInt(numItemsText);
					// check if textBookID is empty
					// if not then grab the data to send to processing
					String bookIDText = textBookID.getText();
					if (bookIDText.length() == 0) {
						JOptionPane.showMessageDialog(null, "You need to input the book ID.");
						return;
					}
					//check if textQuantity is empty
					// if not then grab the data to send to processing
					String quantityText = textQuantity.getText();
					if (quantityText.length() == 0) {
						JOptionPane.showMessageDialog(null, "You need to input the quantity.");
						return;
					}
					quantity = Integer.parseInt(quantityText);
					
					// process the book
					itemProcess(bookIDText);
				}
			}
		});
		
		// confirm button initial customization and action when pressed
		btnConfirm = new JButton("Confirm Item #" + bookNum);
		btnConfirm.setEnabled(false);
		btnConfirm.setBounds(131, 142, 111, 23);
		panelBut.add(btnConfirm);
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnConfirm)
					//confirm the book
					itemConfirm();
			}
		});
		
		// view order button initial customization and action when pressed
		btnView = new JButton("View Order");
		btnView.setEnabled(false);
		btnView.setBounds(252, 142, 89, 23);
		panelBut.add(btnView);
		btnView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnView)
					// view the order currently
					viewOrder();
			}
		});
		
		// finish order button initial customization and action when pressed
		btnFinish = new JButton("Finish Order");
		btnFinish.setEnabled(false);
		btnFinish.setBounds(351, 142, 91, 23);
		panelBut.add(btnFinish);
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnFinish)
					// finish the order at the end
					finishOrder();
			}
		});
		
		// new order button initial customization and action when pressed
		btnNew = new JButton("New Order");
		btnNew.setBounds(452, 142, 89, 23);
		panelBut.add(btnNew);
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnNew) {
					// clear the order to start a new order
					// also reset all of the customizations to the gui
					clear();
				}
			}
		});
		
		// exit button initial customization and action when pressed
		btnExit = new JButton("Exit");
		btnExit.setBounds(551, 142, 89, 23);
		panelBut.add(btnExit);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnExit)
					// exit the program
					System.exit(0);
			}
		});
	}
	
	// this function processes the current book order
	// it checks if the book id is present in the inventory
	// and also calculates the discount, total price,
	// and edits some of the gui customizations
	public void itemProcess(String bookID) {
		// initial values for each use of the function
		found = 0;
		try {
			scanner = new Scanner(inFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// scan the inventory file for the book id
		// raises flag if found
		while (scanner.hasNext()) {
			line = scanner.nextLine();
			currLine = line.split(delimiter);
			if (bookID.contentEquals(currLine[0])) {
				found = 1;
				break;
			}
		}
		// if the book id was not found, output a message and end the function
		if (found == 0) {
			JOptionPane.showMessageDialog(null, "Book ID " + bookID + " not in file.");
			return;
		}
		
		// check which discount should be applied
		// according to the quantity of the book wanted
		if (quantity >= 15)
			discount = 20;
		else if (quantity >= 10)
			discount = 15;
		else if (quantity >= 5)
			discount = 10;
		
		// grab the price of a single book
		// then calculate the total price for this book order
		itemPrice = Float.parseFloat(currLine[2]);
		itemTotal = (itemPrice * quantity) - ((itemPrice * quantity) * ((float) discount / 100));
		
		// set lblInfo to the correct book number in the order
		lblInfo.setText("Item #" + bookNum + " info:");
		
		// disable textNumItems and btnProcess
		// enable btnConfirm
		// set btnConfirm to the correct book number in the order
		// also output the book information to the textInfo field
		textNumItems.setEnabled(false);
		btnProcess.setEnabled(false);
		btnConfirm.setEnabled(true);
		btnConfirm.setText("Confirm Item #" + bookNum);
		textInfo.setText(currLine[0] + " " + currLine[1] + " $" + String.format("%.2f", itemPrice) + " " + quantity + " " + discount + "% $" + String.format("%.2f", itemTotal));
	}
	
	// this function confirms the current book order
	// it also creates 2 strings using the book information from processing
	// those will make outputting the full string easier
	// also updates the gui with the wanted customizations
	// depending on this book number and the number in the order
	public void itemConfirm() {
		JOptionPane.showMessageDialog(null, "Item #" + bookNum + " accepted.");
		
		// create 2 strings from the item information
		booksInCart.add(currLine[0] + " " + currLine[1] + " $" + String.format("%.2f", itemPrice) + " " + quantity + " " + discount + "% $" + String.format("%.2f", itemTotal));
		booksInCart2.add(currLine[0] + ", " + currLine[1] + ", " + String.format("%.2f", itemPrice) + ", " + quantity + ", " + String.format("%.2f", ((float) discount / 100)) + ", " + String.format("%.2f", itemTotal));
		
		// add to the subtotal of the order
		subtotal += itemTotal;
		
		bookNum++;
		// if the book number is still in the number of items
		// its not the end of the order yet
		// so update the gui parts as needed
		if (bookNum <= numItems) {
			lblBookID.setText("Enter Book ID for item #" + bookNum + ":");
			textBookID.setText("");
			lblQuantity.setText("Enter the Quantity for item #" + bookNum + ":");
			textQuantity.setText("");
			lblSubtotal.setText("Order subtotal for " + (bookNum - 1) + " item(s):");
			textSubtotal.setText("$" + String.format("%.2f", subtotal));
			btnProcess.setText("Process Item #" + bookNum);
			
			btnProcess.setEnabled(true);
			btnConfirm.setEnabled(false);
			btnView.setEnabled(true);
			btnFinish.setEnabled(true);
		}
		// if the book number is over the number of items in the order
		// then it is the end of the order
		// the gui parts are updated as needed
		else {
			lblBookID.setText("");
			textBookID.setText("");
			textBookID.setEnabled(false);
			lblQuantity.setText("");
			textQuantity.setText("");
			textQuantity.setEnabled(false);
			lblSubtotal.setText("Order subtotal for " + (bookNum - 1) + " item(s):");
			textSubtotal.setText("$" + String.format("%.2f", subtotal));
			btnProcess.setText("Process Item");
			btnConfirm.setText("Confirm Item");
			
			btnProcess.setEnabled(false);
			btnConfirm.setEnabled(false);
			btnView.setEnabled(true);
			btnFinish.setEnabled(true);
		}
	}
	
	// this function will set everything that is changed 
	// back to its default setting to start a new order
	public static void clear() {
		bookNum = 1;
		lblNumItem.setText("Enter number of items in this order:");
		textNumItems.setText("");
		textNumItems.setEnabled(true);
		lblBookID.setText("Enter Book ID for item #" + bookNum + ":");
		textBookID.setText("");
		textBookID.setEnabled(true);
		lblQuantity.setText("Enter the Quantity for item #" + bookNum + ":");
		textQuantity.setText("");
		textQuantity.setEnabled(true);
		lblInfo.setText("Item #" + bookNum + " info:");
		textInfo.setText("");
		lblSubtotal.setText("Order subtotal for " + (bookNum - 1) + " item(s):");
		textSubtotal.setText("");
		btnProcess.setText("Process Item #" + bookNum);
		btnProcess.setEnabled(true);
		btnConfirm.setText("Confirm Item #" + bookNum);
		btnConfirm.setEnabled(false);
		btnView.setEnabled(false);
		btnFinish.setEnabled(false);
		lineNum = "";
		line = "";
		found = 0;
		subtotal = 0;
		itemPrice = 0;
		itemTotal = 0;
		discount = 0;
		booksInCart = new ArrayList<String>();
		booksInCart2 = new ArrayList<String>();
		booksForFile = new ArrayList<String>();
		scanner = null;
		writer = null;
	}
	
	// this function creates a large string
	// that is the view message dialog
	public static void viewOrder() {
		String viewDialog = "";
		int i;
		
		// loop through and create the view order dialog string
		for (i = 1; i < bookNum; i++)
			viewDialog = viewDialog + i + ": " + booksInCart.get(i - 1) + "\n";
		
		JOptionPane.showMessageDialog(null, viewDialog);
	}
	
	// this function creates a large string that
	// is the invoice message dialog
	// it also outputs the order to the
	// transactions file and calculates the tax amount
	// for the order and then the total for the order
	public static void finishOrder() {
		float taxAmount = 0, orderTotal = 0;
		String finishDialog = "";
		
		// format the date and time
		ZonedDateTime zdt = ZonedDateTime.now();
		DateTimeFormatter dialogPattern = DateTimeFormatter.ofPattern("M/dd/yy, hh:mm:ss a z", Locale.ENGLISH);
		DateTimeFormatter filePattern = DateTimeFormatter.ofPattern("ddMMyyyyhhmm");
		
		// start creating the invoice dialog string
		finishDialog = finishDialog + "Date: " + zdt.format(dialogPattern) + "\n\n";
		finishDialog = finishDialog + "Number of line items: " + numItems + "\n\n";
		finishDialog = finishDialog + "Item# / ID / Title / Price / Qty / Disc% / Subtotal:\n\n";
		int i;
		for (i = 1; i < bookNum; i++)
			finishDialog = finishDialog + i + ": " + booksInCart.get(i - 1) + "\n";
		finishDialog = finishDialog + "\n\nOrder subtotal: $" + String.format("%.2f", subtotal) + "\n\n";
		finishDialog = finishDialog + "Tax rate:             " + tax + "%\n\n";
		
		// calculate the tax amount for the order
		taxAmount = subtotal * ((float) tax / 100);
		
		// continue creating teh invoice dialog string
		finishDialog = finishDialog + "Tax amount:      $" + String.format("%.2f", taxAmount) + "\n\n";
		
		// calculate the total for the order
		orderTotal = subtotal + taxAmount;
		
		// finish creating the invoice dialog string
		finishDialog = finishDialog + "Order total:        $" + String.format("%.2f", orderTotal) + "\n\n";
		finishDialog = finishDialog + "Thanks for shopping at the Old Book Shop!";
		
		// loop through the number of books in this order
		// and output the transactions to the output file
		for (i = 1; i < bookNum; i++) {
			booksForFile.add(zdt.format(filePattern) + ", " + booksInCart2.get(i - 1) + ", " + zdt.format(dialogPattern) + "\n");
			if (!outFile.exists()) {
				try {
					outFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				writer = new FileWriter(outFile, true);
				
				BufferedWriter bufferedWriter = new BufferedWriter(writer);
				bufferedWriter.write(booksForFile.get(i - 1));
				bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		JOptionPane.showMessageDialog(null, finishDialog);
	}
}
