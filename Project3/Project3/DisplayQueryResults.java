/* Name: 
 * Course: CNT 4714 Summer 2020
 * Assignment title: Project 3 - Two-Tier Client-Server Application Development With MySQL and JDBC
 * Due Date: June 26, 2020
 */

package Project3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class DisplayQueryResults extends JFrame {
   
   private ResultSetTableModel tableModel = null;
   private JTextArea queryArea;
   private JTextField textFieldUser;
   private JPasswordField passwordField;
   private static JLabel lblConnectionStatus;
   private JComboBox comboBoxDriver, comboBoxURL;
   private JTable resultTable;
   private JScrollPane scrollPane_1;
   
   private String dbDriver, dbURL, userText, passText;
   
   // create ResultSetTableModel and GUI
   public DisplayQueryResults() {
	   
	   	super("SQL Client App");

      	// set up JTextArea in which user types queries
        queryArea = new JTextArea("", 3, 100);
        queryArea.setWrapStyleWord(true);
        queryArea.setLineWrap(true);
         
        JScrollPane scrollPane = new JScrollPane(queryArea,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
         
        // create Box to manage placement of queryArea and 
        // submitButton in GUI
        Box box = Box.createHorizontalBox();
        box.setBounds(393, 36, 381, 188);
        box.add(scrollPane);
         
        // create JTable delegate for tableModel 
        resultTable = new JTable(tableModel);
        resultTable.setGridColor(Color.BLACK);
        getContentPane().setLayout(null);
         
        // place GUI components on content pane
        getContentPane().add(box);
        scrollPane_1 = new JScrollPane(resultTable);
        scrollPane_1.setPreferredSize(new Dimension(764, 202));
        getContentPane().add(scrollPane_1);
         
        // set up JButton for submitting queries
        JButton submitButton = new JButton("Execute SQL Command");
        submitButton.setBounds(594, 235, 180, 23);
        getContentPane().add(submitButton);
        submitButton.setBackground(UIManager.getColor("Button.background"));
        submitButton.setForeground(Color.BLACK);
        submitButton.setOpaque(true);
        // create event listener for submitButton
        submitButton.addActionListener(new ActionListener() {
        		// pass query to table model
              	public void actionPerformed(ActionEvent event) {
              		// perform a new query
              		try {
              			// split up the commands from the text area
              			String[] currQuery = queryArea.getText().split(";");
              			
              			// interate over each command
              			for (int i = 0; i < currQuery.length; i++) {
              				// replace new line characters
              				currQuery[i] = currQuery[i].replaceAll("\n", " ");
              				
              				// split the command at the spaces
              				String[] currLineSplit = currQuery[i].split(" ");
              				
              				// check the first word of the command to see what command type it is
              				if (currLineSplit[0].toLowerCase().equals("select") == true
              						|| currLineSplit[1].toLowerCase().equals("select") == true) {
              					tableModel.setQuery(currQuery[i]);
                			  
              					// create JTable delegate for tableModel
              					resultTable = new JTable(tableModel);
              					resultTable.setGridColor(Color.BLACK);
        				        
              					// place GUI components on content pane
              					scrollPane_1 = new JScrollPane(resultTable);
              					scrollPane_1.setVisible(true);
              					scrollPane_1.setBounds(10, 294, 764, 202);
              					getContentPane().add(scrollPane_1);
              				}
              				else {
              					clearResults();
              					tableModel.setUpdate(currQuery[i]);
              				}
              			}
              		} // end try
              		catch (SQLException sqlException) {
              			JOptionPane.showMessageDialog(null, 
              					sqlException.getMessage(), "Database error", 
              					JOptionPane.ERROR_MESSAGE);                 
              		} // end outer catch
              	} // end actionPerformed
           	}  // end ActionListener inner class          
         ); // end call to addActionListener
         
         // set up the button that clears the result window
         JButton btnClearResult = new JButton("Clear Result Window");
         btnClearResult.setBounds(10, 507, 167, 23);
         getContentPane().add(btnClearResult);
         btnClearResult.addActionListener(new ActionListener() {
          	public void actionPerformed(ActionEvent e) {
          		clearResults();
          	}
          });
         
         // label for the command window
         JLabel lblEnterCommand = new JLabel("Enter An SQL Command");
         lblEnterCommand.setFont(new Font("Tahoma", Font.BOLD, 11));
         lblEnterCommand.setBounds(393, 11, 381, 14);
         getContentPane().add(lblEnterCommand);
         
         // set up the button that clears the command window
         JButton btnCommandClear = new JButton("Clear SQL Command");
         btnCommandClear.setBounds(425, 235, 159, 23);
         getContentPane().add(btnCommandClear);
         btnCommandClear.addActionListener(new ActionListener() {
           	public void actionPerformed(ActionEvent e) {
           		if (e.getSource() == btnCommandClear) {
           			queryArea.setText("");
           		}
           	}
           });
         
         // label for the results window
         JLabel lblResultWindow = new JLabel("SQL Execution Result Window");
         lblResultWindow.setFont(new Font("Tahoma", Font.BOLD, 11));
         lblResultWindow.setBounds(10, 269, 764, 14);
         getContentPane().add(lblResultWindow);
         
         // label for the driver input
         JLabel lblDriver = new JLabel("JDBC Driver");
         lblDriver.setBounds(10, 36, 90, 14);
         getContentPane().add(lblDriver);
         
         // combo box that contains the drivers that can be selected
         comboBoxDriver = new JComboBox();
         comboBoxDriver.setModel(new DefaultComboBoxModel(new String[] {"com.mysql.cj.jdbc.Driver"}));
         comboBoxDriver.setBounds(110, 32, 273, 22);
         getContentPane().add(comboBoxDriver);
         
         // label for the url input
         JLabel lblURL = new JLabel("Database URL");
         lblURL.setBounds(10, 61, 90, 14);
         getContentPane().add(lblURL);
         
         // combo box that contains the db urls that can be selected
         comboBoxURL = new JComboBox();
         comboBoxURL.setModel(new DefaultComboBoxModel(new String[] {"jdbc:mysql://localhost:3306/project3?useTimezone=true&serverTimezone=UTC", "jdbc:mysql://localhost:3306/bikedb?useTimezone=true&serverTimezone=UTC"}));
         comboBoxURL.setBounds(110, 57, 273, 22);
         getContentPane().add(comboBoxURL);
         
         // set up the button that connects to the db with the input given
         JButton btnConnectDB = new JButton("Connect to Database");
         btnConnectDB.setBounds(256, 235, 159, 23);
         getContentPane().add(btnConnectDB);
         btnConnectDB.addActionListener(new ActionListener() {
          	public void actionPerformed(ActionEvent e) {
          		if (e.getSource() == btnConnectDB) {
          			// obtain the driver selected
          			dbDriver = comboBoxDriver.getSelectedItem().toString();
          			// check if there is a driver input
 					if (dbDriver.length() == 0) {
 						JOptionPane.showMessageDialog(null, "You need to select a driver from the dropdown list.");
 						return;
 					}
 					
 					// obtain the db url selected
 					dbURL = comboBoxURL.getSelectedItem().toString();
 					// check if there is a db url input
 					if (dbURL.length() == 0) {
 						JOptionPane.showMessageDialog(null, "You need to select a URL from the dropdown list.");
 						return;
 					}
 					
 					// obtain the username input
 					userText = textFieldUser.getText();
 					// check if there is a username input
 					if (userText.length() == 0) {
 						JOptionPane.showMessageDialog(null, "You need to enter a username for the DB connection.");
 						return;
 					}
 					
 					// obtain the password input
 					passText = String.valueOf(passwordField.getPassword());
 					// check if there is a password input
 					if (passText.length() == 0) {
 						JOptionPane.showMessageDialog(null, "You need to enter a password for the DB connection.");
 						return;
 					}
 					
 					try {
 						// pass the info to ResultSetTableModel for connecting
 						tableModel = new ResultSetTableModel(dbURL, userText, passText);
 					} catch (ClassNotFoundException e1) {
 						JOptionPane.showMessageDialog(null, 
 					            "MySQL driver not found", "Driver not found",
 					            JOptionPane.ERROR_MESSAGE);
 					         
 					    System.exit(1); // terminate application
 					} catch (SQLException e1) {
 						JOptionPane.showMessageDialog( null, e1.getMessage(), 
 					            "Database error", JOptionPane.ERROR_MESSAGE );
 					               
 					    // ensure database connection is closed
 					    tableModel.disconnectFromDatabase();
 					         
 					    System.exit(1);   // terminate application
 					}
          		}
          	}
          });
         
         // panel that contains the connection status
         JPanel panelStatus = new JPanel();
         panelStatus.setBackground(UIManager.getColor("Button.light"));
         FlowLayout fl_panelStatus = (FlowLayout) panelStatus.getLayout();
         fl_panelStatus.setAlignment(FlowLayout.LEFT);
         panelStatus.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
         panelStatus.setBounds(10, 235, 236, 23);
         getContentPane().add(panelStatus);
         
         // label that states the connection status
         lblConnectionStatus = new JLabel("No Connection Now");
         lblConnectionStatus.setHorizontalAlignment(SwingConstants.CENTER);
         panelStatus.add(lblConnectionStatus);
         lblConnectionStatus.setForeground(Color.RED);
         
         // label for the input area
         JLabel lblDBInfo = new JLabel("Enter Database Information");
         lblDBInfo.setFont(new Font("Tahoma", Font.BOLD, 11));
         lblDBInfo.setBounds(10, 11, 373, 14);
         getContentPane().add(lblDBInfo);
         
         // label for the username input
         JLabel lblUser = new JLabel("Username");
         lblUser.setBounds(10, 86, 90, 14);
         getContentPane().add(lblUser);
         
         // text field for the username input
         textFieldUser = new JTextField();
         textFieldUser.setBounds(110, 83, 273, 20);
         getContentPane().add(textFieldUser);
         textFieldUser.setColumns(10);
         
         // label for the password input
         JLabel lblPass = new JLabel("Password");
         lblPass.setBounds(10, 111, 90, 14);
         getContentPane().add(lblPass);
         
         // password field for the password input
         passwordField = new JPasswordField();
         passwordField.setBounds(110, 108, 273, 20);
         getContentPane().add(passwordField);
         
         // panel that contains the results window
         JPanel panel = new JPanel();
         panel.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
         panel.setBackground(UIManager.getColor("Button.light"));
         panel.setBounds(10, 294, 764, 200);
         getContentPane().add(panel);

         setSize(800, 580); // set window size
         setVisible(true); // display window  

         // dispose of window when user quits application (this overrides
         // the default of HIDE_ON_CLOSE)
         setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      
         // ensure database connection is closed when user quits application
         addWindowListener(new WindowAdapter() {
        	 // disconnect from database and exit when window has closed
        	 public void windowClosed(WindowEvent event) {
        		 tableModel.disconnectFromDatabase();
        		 System.exit(0);
        	 } // end method windowClosed
         	} // end WindowAdapter inner class
        		 ); // end call to addWindowListener
   	} // end DisplayQueryResults constructor
   
   // updates the connection status label
   // after a successful connection
   public static void setConnectedStatus(String url) {
	   lblConnectionStatus.setForeground(Color.GREEN);
	   lblConnectionStatus.setText("Connected to " + url + ".");
   }
   
   // updates the connection status label
   // after a disconnection
   public static void desetConnectedStatus() {
	   lblConnectionStatus.setForeground(Color.RED);
	   lblConnectionStatus.setText("No Connection Now");
   }
   
   // clears the results window
   public void clearResults() {
	   scrollPane_1.setVisible(false);
   }
   
   // execute application
   public static void main(String args[]) {
      new DisplayQueryResults();     
   } // end main
} // end class DisplayQueryResults
