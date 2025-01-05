
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.finalculminating_joshuawu;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
/**
 *
 * @author 335181541
 */
public class CustomerBillingSystem extends javax.swing.JFrame {

    /**
     * Creates new form CustomerBillingSystem
     */
    public CustomerBillingSystem() {
        initComponents();
    }
    public static boolean isPositiveInt(String str) { //checks if a string input is a postive integer
     try {  
        Integer i = Integer.parseInt(str);  
        return (i>0);
     } catch(NumberFormatException e){  
        return false;  
     }
    }
    public static boolean isPositiveLong(String str) { //checks if a string input is a postive long number
     try {  
        Long i = Long.parseLong(str);
        return (i>0);
     } catch(NumberFormatException e){  
        return false;  
     }
    }
    public static double convertCurrency(String baseCurrency, String targetCurrency, double amount) { //converts a specified currency to a targetCurrency using an API
        try {
            String apiUrl = "https://open.er-api.com/v6/latest/" + baseCurrency + "?apikey=Insert Exchange Rate Api Key Here"; //api for OpenExchangeRates

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse JSON response
                String jsonResponse = response.toString();
                double exchangeRate = parseExchangeRate(jsonResponse, targetCurrency);

                return amount * exchangeRate;
            } else {
                System.err.println("Error: HTTP " + responseCode);
            }

        } catch (IOException e) {
        }

        return -1.0; // Indicates an error
    }

    private static double parseExchangeRate(String jsonResponse, String targetCurrency) {
        // Parse the JSON response to get the exchange rate for the target currency
        String rateKey = targetCurrency;
        int rateIndex = jsonResponse.indexOf(rateKey);
        int valueStartIndex = jsonResponse.indexOf(":", rateIndex) + 1;
        int valueEndIndex = jsonResponse.indexOf(",", valueStartIndex);

        String rateValue = jsonResponse.substring(valueStartIndex, valueEndIndex).trim();

        return Double.parseDouble(rateValue);
    }
   public static String[] readFromFile(String fileName){ //reads from customers.txt or orders.txt and outputs each line as an element of an array
    
    String outputAsString = "";
    String [] outputAsArray;
    
    try {
      File file = new File(fileName); //Creation of file object
      Scanner fileInput = new Scanner(file); //Scanner can also be used to read information from a file. 

      while (fileInput.hasNextLine()){
        outputAsString += fileInput.nextLine()+"\n"; //Converting the file into one large string which can be split into an array of strings later on.
      }
      
      fileInput.close(); //Closing the scanner to prevent memory leaks.
        
      
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred."); //This would happen if something goes wrong such as the file does not exist. 
    }
     
    outputAsArray = outputAsString.split("\n"); //Converting the string into an array of strings delimited (look up that word if you don't know what it is) by the by the end of line character (\n). Remember the .split() commend for the future (maybe for a test??).  
    
    return outputAsArray;
    
  }
    //This method will open the txt in write mode. This method has one string argument which represents the line which the user would like to add to the file. This method returns nothing.  
    public static void writeToFile(String newLine, String fileName){ //Writes the specified string into the files customers.txt or orders.txt
    
    try { 
      
      //To open the file in write mode we are using a three step process. 
      //1. Create File Object
      //2. Create FileOutputSteam Object which takes in File Object as the argument. 
      //3. Create PrintWrite Object which takes in the FileOutputSteam Object as the argument. 
      
      FileWriter myWriter = new FileWriter(fileName, false);
     
      myWriter.write(newLine);
      myWriter.close();
      
    } catch (IOException e) {
      System.out.println("An error occurred.");
    }   
    }    
    private static String generateReceipt(String firstName, String lastName, String address, String postalCode,
                                          String email, String phone, String country, String id, String orderDateStr,
                                          String billDateStr, int pizzaAmt, double pizzaCost, int burgerAmt, double burgerCost,
                                          int friesAmt, double friesCost, int sodaAmt, double sodaCost, int hotdogAmt,
                                          double hotdogCost, double taxRate) {
        // Create a StringBuilder to build the receipt string
        StringBuilder receiptBuilder = new StringBuilder();


        // Format the receipt and append to the StringBuilder
        receiptBuilder.append("=======================\n");
        receiptBuilder.append("RECEIPT\n");
        receiptBuilder.append("=======================\n");
        receiptBuilder.append("Customer Details:\n");
        receiptBuilder.append(String.format("Name: %s %s\n", firstName, lastName));
        receiptBuilder.append(String.format("Address: %s\n", address));
        receiptBuilder.append(String.format("Postal Code: %s\n", postalCode));
        receiptBuilder.append(String.format("Email: %s\n", email));
        receiptBuilder.append(String.format("Phone: %s\n", phone));
        receiptBuilder.append(String.format("Country: %s\n", country));
        receiptBuilder.append("\nOrder Details:\n");
        receiptBuilder.append(String.format("Order ID: %s\n", id));
        receiptBuilder.append(String.format("Order Date: %s\n", orderDateStr));
        receiptBuilder.append(String.format("Bill Date: %s\n", billDateStr));
        receiptBuilder.append("\nItems Ordered:\n");
        receiptBuilder.append(String.format("Pizza (Qty %d)   : $%.2f\n", pizzaAmt, pizzaCost));
        receiptBuilder.append(String.format("Burger (Qty %d)  : $%.2f\n", burgerAmt, burgerCost));
        receiptBuilder.append(String.format("Fries (Qty %d)   : $%.2f\n", friesAmt, friesCost));
        receiptBuilder.append(String.format("Soda (Qty %d)    : $%.2f\n", sodaAmt, sodaCost));
        receiptBuilder.append(String.format("Hotdog (Qty %d)  : $%.2f\n", hotdogAmt, hotdogCost));
        receiptBuilder.append("=======================\n");

        // Calculate total amount before tax
        double subtotal = (pizzaAmt * pizzaCost) + (burgerAmt * burgerCost) + (friesAmt * friesCost) + (sodaAmt * sodaCost) + (hotdogAmt * hotdogCost);
        
        // Calculate tax amount
        double taxAmount = subtotal * taxRate;

        // Calculate total amount including tax
        double totalAmount = subtotal + taxAmount;

        // Append subtotal, tax, and total to the StringBuilder
        receiptBuilder.append(String.format("SUBTOTAL: $%.2f\n", subtotal));
        receiptBuilder.append(String.format("Tax (%.0f%%): $%.2f\n", taxRate * 100, taxAmount));
        receiptBuilder.append("-----------------------\n");
        receiptBuilder.append(String.format("TOTAL AMOUNT: $%.2f\n", totalAmount));
        double convertedAmount;
        switch (country){ //convert to customer's currency and display the conversion
            case "Canada" ->{ 
            }
            case "United States" -> {
                convertedAmount = convertCurrency("CAD","USD",totalAmount);
                receiptBuilder.append(String.format("You will be billed %.2f USD.\n", convertedAmount));
            }
            case "United Kingdom" -> {
                convertedAmount = convertCurrency("CAD","GBP",totalAmount);
                receiptBuilder.append(String.format("You will be billed %.2f GBP.\n", convertedAmount));
            }
            case "Mexico" ->  {
                convertedAmount = convertCurrency("CAD","MXN",totalAmount);
                receiptBuilder.append(String.format("You will be billed %.2f MXN.\n", convertedAmount));
            }
        }
        receiptBuilder.append("=======================\n");

        // Save the formatted receipt as a String
        return receiptBuilder.toString();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Email = new javax.swing.JFrame();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        send = new javax.swing.JButton();
        jSpinner1 = new javax.swing.JSpinner();
        orderDate = new com.toedter.calendar.JDateChooser();
        title = new javax.swing.JLabel();
        fTitle = new javax.swing.JLabel();
        lTitle = new javax.swing.JLabel();
        aTitle = new javax.swing.JLabel();
        pcTitle = new javax.swing.JLabel();
        phoneTitle = new javax.swing.JLabel();
        eTitle = new javax.swing.JLabel();
        cTitle = new javax.swing.JLabel();
        oTitle = new javax.swing.JLabel();
        bTitle = new javax.swing.JLabel();
        cdTitle = new javax.swing.JLabel();
        output = new javax.swing.JTextField();
        oDataTitle = new javax.swing.JLabel();
        addCustomer = new javax.swing.JButton();
        idTitle = new javax.swing.JLabel();
        cNumTitle = new javax.swing.JLabel();
        customers = new javax.swing.JComboBox<>();
        selectCustomer = new javax.swing.JButton();
        orderNumTitle = new javax.swing.JLabel();
        billDate = new com.toedter.calendar.JDateChooser();
        first = new javax.swing.JTextField();
        last = new javax.swing.JTextField();
        address = new javax.swing.JTextField();
        pCode = new javax.swing.JTextField();
        phone = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        country = new javax.swing.JComboBox<>();
        customerID = new javax.swing.JTextField();
        modifyCustomer = new javax.swing.JButton();
        refreshID = new javax.swing.JButton();
        orders = new javax.swing.JComboBox<>();
        foodTitle = new javax.swing.JLabel();
        pizzaTitle = new javax.swing.JCheckBox();
        burgerTitle = new javax.swing.JCheckBox();
        sodaTitle = new javax.swing.JCheckBox();
        friesTitle = new javax.swing.JCheckBox();
        pizza = new javax.swing.JTextField();
        hotdogTitle = new javax.swing.JCheckBox();
        read = new javax.swing.JButton();
        write = new javax.swing.JButton();
        m1 = new javax.swing.JLabel();
        m2 = new javax.swing.JLabel();
        m3 = new javax.swing.JLabel();
        m4 = new javax.swing.JLabel();
        m5 = new javax.swing.JLabel();
        m6 = new javax.swing.JLabel();
        m7 = new javax.swing.JLabel();
        burger = new javax.swing.JTextField();
        soda = new javax.swing.JTextField();
        hotdog = new javax.swing.JTextField();
        fries = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        customersField = new javax.swing.JTextArea();
        addOrder = new javax.swing.JButton();
        modOrder = new javax.swing.JButton();
        readOFile = new javax.swing.JButton();
        sendText = new javax.swing.JButton();
        selectOrder = new javax.swing.JButton();
        check = new javax.swing.JButton();
        wOFile = new javax.swing.JButton();
        printReceipt = new javax.swing.JButton();
        selectC = new javax.swing.JLabel();
        selectO = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        receipt = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        phoneInput = new javax.swing.JTextField();
        checkO = new javax.swing.JButton();
        checkC = new javax.swing.JButton();
        sTitle = new javax.swing.JLabel();
        searchInput = new javax.swing.JTextField();
        search = new javax.swing.JButton();
        sendEmail = new javax.swing.JButton();
        emailInput = new javax.swing.JTextField();
        sendTitle = new javax.swing.JLabel();
        cCustomer = new javax.swing.JButton();
        cOrder = new javax.swing.JButton();

        Email.setTitle("Email");
        Email.setMinimumSize(new java.awt.Dimension(400, 350));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setText("Customer ID:");

        jLabel9.setText("Order #:");

        jTextField1.setText("jTextField1");

        jTextField2.setText("jTextField2");

        send.setText("Send Email");

        javax.swing.GroupLayout EmailLayout = new javax.swing.GroupLayout(Email.getContentPane());
        Email.getContentPane().setLayout(EmailLayout);
        EmailLayout.setHorizontalGroup(
            EmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmailLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(EmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EmailLayout.createSequentialGroup()
                        .addComponent(send)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(EmailLayout.createSequentialGroup()
                        .addGroup(EmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(EmailLayout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(EmailLayout.createSequentialGroup()
                                .addGroup(EmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                                .addGroup(EmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(76, 76, 76))))
        );
        EmailLayout.setVerticalGroup(
            EmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmailLayout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(EmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(EmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(EmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(send)
                .addContainerGap(93, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Customer Billing System");

        orderDate.setDateFormatString("dd/MM/yyyy");

        title.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        title.setForeground(new java.awt.Color(51, 255, 204));
        title.setText("Customer Billing System (CBS)");

        fTitle.setText("First Name:");

        lTitle.setText("Last Name:");

        aTitle.setText("Address:");

        pcTitle.setText("Postal Code:");

        phoneTitle.setText("Phone #:");

        eTitle.setText("Email:");

        cTitle.setText("Country:");

        oTitle.setText("Order Date:");

        bTitle.setText("Billing Date:");

        cdTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cdTitle.setText("Customer Data");

        output.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputActionPerformed(evt);
            }
        });

        oDataTitle.setText("Order Data");

        addCustomer.setText("Create Customer");
        addCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerActionPerformed(evt);
            }
        });

        idTitle.setText("Customer ID:");

        cNumTitle.setText("Customer #:");

        customers.setToolTipText("");
        customers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customersActionPerformed(evt);
            }
        });

        selectCustomer.setText("Select Customer");
        selectCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectCustomerActionPerformed(evt);
            }
        });

        orderNumTitle.setText("Order #:");

        billDate.setDateFormatString("dd/MM/yyyy");

        first.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstActionPerformed(evt);
            }
        });

        last.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastActionPerformed(evt);
            }
        });

        address.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressActionPerformed(evt);
            }
        });

        country.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Canada", "United States", "United Kingdom", "Mexico" }));

        customerID.setEditable(false);
        customerID.setText("0001");

        modifyCustomer.setText("Modify Customer");
        modifyCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyCustomerActionPerformed(evt);
            }
        });

        refreshID.setText("New ID");
        refreshID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshIDActionPerformed(evt);
            }
        });

        orders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ordersActionPerformed(evt);
            }
        });

        foodTitle.setText("Food Items:");

        pizzaTitle.setText("Pizza Slice");
        pizzaTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pizzaTitleActionPerformed(evt);
            }
        });

        burgerTitle.setText("Burger");
        burgerTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                burgerTitleActionPerformed(evt);
            }
        });

        sodaTitle.setText("Soda");
        sodaTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sodaTitleActionPerformed(evt);
            }
        });

        friesTitle.setText("Fries");
        friesTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                friesTitleActionPerformed(evt);
            }
        });

        pizza.setEnabled(false);

        hotdogTitle.setText("Hot Dog");
        hotdogTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hotdogTitleActionPerformed(evt);
            }
        });

        read.setText("Read Customer File");
        read.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readActionPerformed(evt);
            }
        });

        write.setText("Write Customer File");
        write.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeActionPerformed(evt);
            }
        });

        m1.setText("3.99$");

        m2.setText("2.99$");

        m3.setText("5.99$");

        m4.setText("3.99$");

        m5.setText("1.99$");

        m6.setText("Amount");

        m7.setText("Cost");

        burger.setEnabled(false);
        burger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                burgerActionPerformed(evt);
            }
        });

        soda.setEnabled(false);
        soda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sodaActionPerformed(evt);
            }
        });

        hotdog.setEnabled(false);
        hotdog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hotdogActionPerformed(evt);
            }
        });

        fries.setEnabled(false);
        fries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                friesActionPerformed(evt);
            }
        });

        customersField.setEditable(false);
        customersField.setColumns(20);
        customersField.setRows(5);
        jScrollPane1.setViewportView(customersField);
        String[] list = readFromFile("orders.txt");
        for (String str: list){
            String[] vars = str.split(",");
            Order o = new Order(Integer.parseInt(vars[0]),Integer.parseInt(vars[1]),
                Integer.parseInt(vars[2]),Integer.parseInt(vars[3]),Integer.parseInt(vars[4]),vars[6],vars[5]);
            orderList.add(o);
            orders.addItem(o.getOrderIndex());
            Order.incrementIndex();
        }
        list = readFromFile("customers.txt");
        for (String str: list){
            String[] vars = str.split(",");
            Customer custom = new Customer(vars[0],vars[1],vars[2], vars[3],vars[4],vars[5],
                vars[6],vars[7],vars[8],vars[9]);
            customerList.add(custom);
            customersField.append(customerList.get(customerList.size()-1).toString() + "\n");
            customers.addItem(custom.getId());
            Customer.incrementIndex();
        }
        customerID.setText(df.format(Customer.getIdIndex()));

        addOrder.setText("Add");
        addOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addOrderActionPerformed(evt);
            }
        });

        modOrder.setText("Modify");
        modOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modOrderActionPerformed(evt);
            }
        });

        readOFile.setText("Read Order File");
        readOFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readOFileActionPerformed(evt);
            }
        });

        sendText.setText("Send Text Message");
        sendText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendTextActionPerformed(evt);
            }
        });

        selectOrder.setText("Select Order");
        selectOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectOrderActionPerformed(evt);
            }
        });

        check.setText("Check");
        check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkActionPerformed(evt);
            }
        });

        wOFile.setText("Write Order File");
        wOFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wOFileActionPerformed(evt);
            }
        });

        printReceipt.setText("Preview Receipt");
        printReceipt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printReceiptActionPerformed(evt);
            }
        });

        selectC.setText("Customer Selected:");

        selectO.setText("Order Selected:");

        receipt.setEditable(false);
        receipt.setColumns(20);
        receipt.setRows(5);
        jScrollPane2.setViewportView(receipt);

        jLabel1.setText("Send To: (+1)");

        checkO.setText("Check Orders");
        checkO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkOActionPerformed(evt);
            }
        });

        checkC.setText("Check Customers");
        checkC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkCActionPerformed(evt);
            }
        });

        sTitle.setText("Search Customer By First Name:");

        search.setText("Search");
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

        sendEmail.setText("Send");
        sendEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendEmailActionPerformed(evt);
            }
        });

        sendTitle.setText("Send Email To: ");

        cCustomer.setText("Clear Customer Fields");
        cCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cCustomerActionPerformed(evt);
            }
        });

        cOrder.setText("Clear");
        cOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cOrderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(modifyCustomer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(124, 124, 124)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(readOFile)
                            .addComponent(checkC))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(wOFile)
                            .addComponent(checkO))
                        .addGap(168, 168, 168))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(output, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cCustomer))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(selectC)
                                    .addComponent(selectO)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(printReceipt)
                                            .addComponent(jLabel1))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(sendText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(phoneInput))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(sendTitle)
                                .addGap(4, 4, 4)
                                .addComponent(emailInput, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sendEmail)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(orderNumTitle)
                                        .addGap(19, 19, 19))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(bTitle)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(billDate, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(idTitle)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(customerID, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(refreshID)))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(eTitle)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(pcTitle)
                                                            .addComponent(phoneTitle))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(email, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                                                            .addComponent(pCode, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(phone))))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(foodTitle)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(burgerTitle)
                                                    .addComponent(friesTitle)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(orders, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(pizzaTitle)))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(m6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                            .addComponent(pizza, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                                            .addComponent(burger, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                                            .addComponent(fries, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(12, 12, 12))))
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(hotdog, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(sodaTitle)
                                                    .addGap(45, 45, 45)
                                                    .addComponent(soda, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(m1, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(m3)
                                                .addComponent(m2))
                                            .addComponent(m5)
                                            .addComponent(m4)
                                            .addComponent(m7)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(hotdogTitle)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(modOrder)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cOrder)
                                                .addGap(11, 11, 11)
                                                .addComponent(addOrder))
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(12, 12, 12))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(fTitle)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(aTitle)
                                                .addGap(14, 14, 14))
                                            .addComponent(lTitle))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(last, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                                    .addComponent(first)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(cTitle)
                                                .addGap(32, 32, 32))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(oTitle)
                                                .addGap(18, 18, 18)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(country, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(orderDate, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(55, 55, 55)
                                        .addComponent(cdTitle)))
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(title)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(cNumTitle)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(customers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(selectCustomer)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(selectOrder)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(check, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(61, 61, 61)
                                                .addComponent(oDataTitle)))))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(search)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(searchInput, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(14, 14, 14))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(read)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(write)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(cdTitle)
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fTitle)
                            .addComponent(first, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(last, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lTitle)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(title)
                        .addGap(9, 9, 9)
                        .addComponent(oDataTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(customers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cNumTitle))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(selectCustomer)
                            .addComponent(selectOrder)
                            .addComponent(check))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(4, 4, 4)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(3, 3, 3)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(aTitle)
                                        .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(orderNumTitle)
                                    .addComponent(orders, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(pcTitle)
                                .addComponent(pCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(phoneTitle)
                                .addComponent(phone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(eTitle))
                            .addGap(12, 12, 12)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(country, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cTitle))
                            .addGap(23, 23, 23)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(orderDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(oTitle))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(bTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(billDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(idTitle)
                                .addComponent(customerID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(refreshID)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(9, 9, 9)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(m6)
                                .addComponent(m7))
                            .addGap(9, 9, 9)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(pizzaTitle)
                                .addComponent(pizza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(m1)
                                .addComponent(foodTitle))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(burgerTitle)
                                .addComponent(burger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(m3))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(friesTitle)
                                .addComponent(fries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(m2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(sodaTitle)
                                .addComponent(soda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(m5))
                            .addGap(10, 10, 10)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(hotdogTitle)
                                .addComponent(hotdog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(m4))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(addOrder)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(modOrder)
                                    .addComponent(cOrder)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(modifyCustomer)
                    .addComponent(addCustomer)
                    .addComponent(checkC)
                    .addComponent(checkO)
                    .addComponent(search))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(read)
                    .addComponent(write)
                    .addComponent(readOFile)
                    .addComponent(wOFile))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selectC)
                    .addComponent(output, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(selectO)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(printReceipt)
                            .addComponent(sendText))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(phoneInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sendEmail)
                            .addComponent(emailInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sendTitle)))
                    .addComponent(cCustomer))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    DecimalFormat df = new DecimalFormat("0000"); //id format
    ArrayList<Customer> customerList = new ArrayList<>(); //declaration of order and customer ArrayLists
    ArrayList<Order> orderList = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  //date format
    public static final String ACCOUNT_SID = "Insert Twilio SID Here"; //api keys for Twilio (text messages)
    public static final String AUTH_TOKEN = "Insert Auth Token HerE";
    private void selectCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectCustomerActionPerformed
        for (int i = 0; i<customerList.size(); i++){ //extracts all info from selected customer and fills it in the customer JTextFields
            if (customerList.get(i).getId().equals(customers.getSelectedItem())){
                try {
                    first.setText(customerList.get(i).getFirstName());
                    last.setText(customerList.get(i).getLastName());
                    address.setText(customerList.get(i).getAddress());
                    pCode.setText(customerList.get(i).getPostalCode());
                    email.setText(customerList.get(i).getEmail());
                    emailInput.setText(customerList.get(i).getEmail());
                    phone.setText(customerList.get(i).getPhone());
                    phoneInput.setText(customerList.get(i).getPhone());
                    orderDate.setDate(dateFormat.parse(customerList.get(i).getOrderDate()));
                    billDate.setDate(dateFormat.parse(customerList.get(i).getBillDate()));
                    country.setSelectedItem(customerList.get(i).getCountry());
                    customerID.setText(df.format(i+1));
                    selectC.setText("Customer Selected: " + df.format(i+1));
                    break;
                } catch (ParseException ex) {
                    Logger.getLogger(CustomerBillingSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }        
    }//GEN-LAST:event_selectCustomerActionPerformed

    private void customersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customersActionPerformed

    }//GEN-LAST:event_customersActionPerformed

    private void firstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstActionPerformed

    private void addressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addressActionPerformed

    private void lastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lastActionPerformed

    private void addCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerActionPerformed
        boolean correctDate; //creates a new customer. New ID must be pressed and all customer fields need to entered before a customer object can be made.
        try { String oDate = dateFormat.format(orderDate.getDate());
        String bDate = dateFormat.format(billDate.getDate());
        correctDate = true;
        }
        catch (NullPointerException e){
            correctDate = false;
        }
        int currentID = Integer.parseInt(customerID.getText());
        String str1 = first.getText(), str2 = last.getText(), str3 = address.getText(),
                str4 = pCode.getText(), str5 = email.getText(), str6 = phone.getText(), str7 = country.getSelectedItem().toString();
        if (currentID != Customer.getIdIndex()) { //info checking
            output.setText("Customer ID has to be 1 higher than last customer ID. Please press New ID.");
        }
        else if (str1.equals("") || str2.equals("") || str3.equals("") 
                || str4.equals("") || str5.equals("") || str6.equals("")){
            output.setText("One of the customer text fields is empty. Please doublecheck your information.");
        }
        else if (!correctDate){
            output.setText("Please input valid dates.");
        }
        else {
            String oDate = dateFormat.format(orderDate.getDate());
            String bDate = dateFormat.format(billDate.getDate());
            Customer custom = new Customer(str1, str2, str3, str4, str5, str6, oDate, bDate, str7);
            customerList.add(custom);
            Customer.incrementIndex();
            customers.addItem(custom.getId());
            customerID.setText(df.format(Customer.getIdIndex()));
            customersField.setText("");
            for (Customer customerList1 : customerList) {
                customersField.append(customerList1.toString()+"\n");
            }
            output.setText("Customer added");
        }
        
    }//GEN-LAST:event_addCustomerActionPerformed

    private void modifyCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyCustomerActionPerformed
        boolean correctDate; //modifies currently selected customer that is selected in the customerID jTextField.
        try { String oDate = dateFormat.format(orderDate.getDate()); //parse dates
        String bDate = dateFormat.format(billDate.getDate());
        correctDate = true;
        }
        catch (NullPointerException e){
            correctDate = false;
        }
        int currentID = Integer.parseInt(customerID.getText());
        String str1 = first.getText(), str2 = last.getText(), str3 = address.getText(),
                str4 = pCode.getText(), str5 = email.getText(), str6 = phone.getText(), str7 = country.getSelectedItem().toString();    
        if (currentID == Customer.getIdIndex()) { //if statements for info checking
            output.setText("Please select a customer.");
        }
        else if (str1.equals("") || str2.equals("") || str3.equals("") 
                || str4.equals("") || str5.equals("") || str6.equals("")){
            output.setText("One of the customer text fields is empty. Please doublecheck your information.");
        }
        else if (!correctDate){
            output.setText("Please input valid dates.");
        }
        else {
            customerList.remove(currentID-1);
            String oDate = dateFormat.format(orderDate.getDate());
            String bDate = dateFormat.format(billDate.getDate());
            Customer custom = new Customer(str1, str2, str3, str4, str5, str6, oDate, bDate, str7);
            custom.setId(df.format(currentID));
            customerList.add(currentID-1, custom);
            customersField.setText(""); //refresh list of customers
            for (int i = 0; i<customerList.size();i++){
                customersField.append(customerList.get(i).toString()+"\n");
            }
            output.setText("Customer modified");
        }        
    }//GEN-LAST:event_modifyCustomerActionPerformed

    private void refreshIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshIDActionPerformed
        customerID.setText(df.format(Customer.getIdIndex())); //refreshes ID so new customer can be created
    }//GEN-LAST:event_refreshIDActionPerformed

    private void pizzaTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pizzaTitleActionPerformed
        if (pizza.isEnabled()) pizza.setEnabled(false); //if check box is deselected, disable text field
        else pizza.setEnabled(true);
    }//GEN-LAST:event_pizzaTitleActionPerformed

    private void burgerTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_burgerTitleActionPerformed
        if (burger.isEnabled()) burger.setEnabled(false);
        else burger.setEnabled(true);
    }//GEN-LAST:event_burgerTitleActionPerformed

    private void friesTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_friesTitleActionPerformed
        if (fries.isEnabled()) fries.setEnabled(false);
        else fries.setEnabled(true);
    }//GEN-LAST:event_friesTitleActionPerformed

    private void sodaTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sodaTitleActionPerformed
        if (soda.isEnabled()) soda.setEnabled(false);
        else soda.setEnabled(true);
    }//GEN-LAST:event_sodaTitleActionPerformed

    private void hotdogTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hotdogTitleActionPerformed
        if (hotdog.isEnabled()) hotdog.setEnabled(false);
        else hotdog.setEnabled(true);
    }//GEN-LAST:event_hotdogTitleActionPerformed

    private void friesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_friesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_friesActionPerformed

    private void writeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_writeActionPerformed
        String totalList = ""; //writes to the customers.txt file using the customerList and toString methods.
            for (int i = 0; i<customerList.size();i++){
                totalList = totalList+customerList.get(i).toString()+"\n";
            }
        writeToFile(totalList,"customers.txt");
        output.setText("Written to customers.txt");
    }//GEN-LAST:event_writeActionPerformed

    private void readActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readActionPerformed
        String[] list = readFromFile("customers.txt"); //Reads the customers.txt file located in the project folder and creates a customer ArrayList out of it.
        customerList.clear();
        Customer.setIdIndex(1);
        customers.removeAllItems();
        customersField.setText("");
        for (String str: list){
        String[] vars = str.split(",");
        Customer custom = new Customer(vars[0],vars[1],vars[2], vars[3],vars[4],vars[5],
                                                    vars[6],vars[7],vars[8],vars[9]);
        customerList.add(custom);
        customersField.append(customerList.get(customerList.size()-1).toString() + "\n");
        customers.addItem(custom.getId());
        Customer.incrementIndex();
        }
        customerID.setText(df.format(Customer.getIdIndex()));
        output.setText("Read from customers.txt");
    }//GEN-LAST:event_readActionPerformed

    private void addOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addOrderActionPerformed
        int pizzaAmt, burgerAmt, friesAmt, sodaAmt, hotdogAmt; //adds a new order with the filled out order JTextFields. The new order will have the highest ID.
        if (pizzaTitle.isSelected() && isPositiveInt(pizza.getText())){ //if a field is not filled or 0, it will be interpreted as 0
            pizzaAmt = Integer.parseInt(pizza.getText());
        }
        else pizzaAmt = 0;
        if (burgerTitle.isSelected() && isPositiveInt(burger.getText())){
            burgerAmt = Integer.parseInt(burger.getText());
        }
        else burgerAmt = 0;
        if (friesTitle.isSelected() && isPositiveInt(fries.getText())){
            friesAmt = Integer.parseInt(fries.getText());
        }
        else friesAmt = 0;
        if (sodaTitle.isSelected() && isPositiveInt(soda.getText())){
            sodaAmt = Integer.parseInt(soda.getText());
        }
        else sodaAmt = 0;
        if (hotdogTitle.isSelected() && isPositiveInt(hotdog.getText())){
            hotdogAmt = Integer.parseInt(hotdog.getText());
        }
        else hotdogAmt = 0;
        String str = selectC.getText();
        String id;
        if (str.equals("Customer Selected:")){ //info checking
            id = null;
        }
        else{
            id = str.substring(19);
        }
        if (pizzaAmt == 0 && burgerAmt == 0 && friesAmt == 0 && sodaAmt == 0 && hotdogAmt == 0){
            output.setText("You have not entered any amounts yet");
        }
        else if ((id==null)){
            output.setText("Please select a customer first");
        }
        else {
            Order o = new Order(pizzaAmt, burgerAmt, friesAmt, sodaAmt, hotdogAmt, df.format(Order.getIndex()),id);
            orders.addItem(df.format(Order.getIndex()));
            orderList.add(o);
            Order.incrementIndex();
            output.setText("Order #" + df.format(Order.getIndex()-1) + " was created");
            customersField.setText("");
            for (int i = 0; i<orderList.size();i++){
                customersField.append(orderList.get(i).toString()+"\n");
            }
        }
    }//GEN-LAST:event_addOrderActionPerformed

    private void sendTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendTextActionPerformed
        /*
        Sends a text message to the phone number recorded in the phoneInput jTextField. 
        Code that formats the receipt is used to send the message.
        If a phone number is not verified (twilio constraints) it will not work and an error message in the program will display.
        */
        String input = phoneInput.getText();
        if (input.length()!=10 || !isPositiveLong(input)){ //info checking
            output.setText("Phone # must be 10 numbers (e.g 1234567890)");
        }
        else if (selectC.getText().equals("Customer Selected:") || selectO.getText().equals("Order Selected:")){
            output.setText("You have not selected an order or customer yet");
        }
        else {
        String firstName = "", lastName = "", address = "", postalCode = "",
            phone = "", email = "", orderDateStr = "", id = "",
                billDateStr = "", countryStr = "";
        String str1 = selectC.getText().substring(19);
        String str2 = selectO.getText().substring(16);
        int pizzaAmt, burgerAmt, friesAmt, sodaAmt, hotdogAmt;
        double pizzaCost, burgerCost, taxRate, friesCost, sodaCost, hotdogCost;
        pizzaAmt = burgerAmt = friesAmt = sodaAmt = hotdogAmt = 0;
            pizzaCost = burgerCost = taxRate = friesCost = sodaCost = hotdogCost = 0;
        for (int i = 0; i<customerList.size(); i++){
            if (customerList.get(i).getId().equals(str1)){
                    Customer c = customerList.get(i);
                    firstName = c.getFirstName(); lastName = c.getLastName(); address = c.getAddress(); postalCode = c.getPostalCode();
                            phone = c.getPhone(); email = c.getEmail(); orderDateStr = c.getOrderDate();
                            billDateStr = c.getBillDate(); countryStr = c.getCountry();
                    break;
            }
        }
        for (int i = 0; i<orderList.size(); i++){
            if (orderList.get(i).getOrderIndex().equals(str2)){
            Order o = orderList.get(i);
            pizzaAmt = o.getPizzaAmt(); burgerAmt = o.getBurgerAmt(); 
                    friesAmt = o.getFriesAmt(); sodaAmt = o.getSodaAmt(); hotdogAmt = o.getHotdogAmt();
            pizzaCost = Order.getPizzaCost(); burgerCost = Order.getBurgerCost(); id = o.getOrderIndex();
                friesCost = Order.getFriesCost(); sodaCost = Order.getSodaCost(); hotdogCost = Order.getHotdogCost();
            switch (countryStr){
            case "Canada" -> taxRate = 0.13; //tax rates for each country
            case "United States" -> taxRate = 0.10; //0.10 is not correct, it is just for proof of concept
            case "United Kingdom" -> taxRate = 0.20;
            case "Mexico" ->  taxRate = 0.16;
            default -> System.out.println("An error occured.");
            }
            break;
            }
        }
        String formattedReceipt = generateReceipt(firstName, lastName, address, postalCode, email, phone, countryStr, id,
                orderDateStr, billDateStr, pizzaAmt, pizzaCost, burgerAmt, burgerCost, friesAmt, friesCost,
                sodaAmt, sodaCost, hotdogAmt, hotdogCost, taxRate);
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        try{
        Message message = Message
            .creator(
        new PhoneNumber("+1" + input),
        new PhoneNumber("Insert Twilio Phone Here"),
        formattedReceipt
      )
      .create();
        
        System.out.println(message.getSid());
        output.setText("Text message sent");
        }
        catch (com.twilio.exception.ApiException e){
            output.setText("Phone number is not verified. Due to twilio contraints, the phone number needs to verified before use.");
        }
        }
    }//GEN-LAST:event_sendTextActionPerformed

    private void ordersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ordersActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ordersActionPerformed

    private void selectOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectOrderActionPerformed
        for (int i = 0; i<orderList.size(); i++){ //extracts all info from selected order and fills it in the JTextFields
            if (orderList.get(i).getOrderIndex().equals(orders.getSelectedItem())){
                Order o = orderList.get(i);
                if (o.getBurgerAmt()!=0){
                    burger.setText(Integer.toString(o.getBurgerAmt()));
                    burger.setEnabled(true);
                    burgerTitle.setSelected(true);
                }
                else {
                    burger.setText("");
                    burger.setEnabled(false);
                    burgerTitle.setSelected(false);
                }
                if (o.getPizzaAmt()!=0){
                    pizza.setText(Integer.toString(o.getPizzaAmt()));
                    pizza.setEnabled(true);
                    pizzaTitle.setSelected(true);
                }
                else{
                    pizza.setText("");
                    pizza.setEnabled(false);
                    pizzaTitle.setSelected(false);
                }
                if (o.getFriesAmt()!=0){
                    fries.setText(Integer.toString(o.getFriesAmt()));
                    fries.setEnabled(true);
                    friesTitle.setSelected(true);
                }
                else{
                    fries.setText("");
                    fries.setEnabled(false);
                    friesTitle.setSelected(false);
                }
                if (o.getSodaAmt()!=0){
                    soda.setText(Integer.toString(o.getSodaAmt()));    
                    soda.setEnabled(true);
                    sodaTitle.setSelected(true);
                }
                else{
                    soda.setText("");
                    soda.setEnabled(false);
                    sodaTitle.setSelected(false);
                }
                if (o.getHotdogAmt()!=0){
                    hotdog.setText(Integer.toString(o.getHotdogAmt()));               
                    hotdog.setEnabled(true);
                    hotdogTitle.setSelected(true);
                }
                else{
                    hotdog.setText("");
                    hotdog.setEnabled(false);
                    hotdogTitle.setSelected(false);
                }
                selectO.setText("Order Selected: " + o.getOrderIndex());
            }
        }
    }//GEN-LAST:event_selectOrderActionPerformed

    private void readOFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readOFileActionPerformed
        String[] list = readFromFile("orders.txt");
        orderList.clear(); //Reads the orders.txt file located in the project folder and creates a order ArrayList out of it.
        Order.setIndex(1);
        orders.removeAllItems();
        customersField.setText("");
        for (String str: list){
        String[] vars = str.split(",");
        Order o = new Order(Integer.parseInt(vars[0]),Integer.parseInt(vars[1]),
                Integer.parseInt(vars[2]),Integer.parseInt(vars[3]),Integer.parseInt(vars[4]),vars[6],vars[5]);
        orderList.add(o);
        customersField.append(orderList.get(orderList.size()-1).toString() + "\n");
        orders.addItem(o.getOrderIndex());
        Order.incrementIndex();
        }
        output.setText("Read from orders.txt");
    }//GEN-LAST:event_readOFileActionPerformed

    private void wOFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wOFileActionPerformed
        String totalList = ""; //writes to the orders.txt file using the orderList and toString methods.
            for (int i = 0; i<orderList.size();i++){
                totalList = totalList+orderList.get(i).toString()+"\n";
            }
        writeToFile(totalList,"orders.txt");
        output.setText("Written to orders.txt");
    }//GEN-LAST:event_wOFileActionPerformed

    private void checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkActionPerformed
        if (orders.getSelectedItem() == null){ //checks which selected order belongs to which customer. the program records which customer was selected when an order was created.
            output.setText("Please select a customer");
        }
        else {
            for (int i = 0; i<orderList.size(); i++){
                if (orderList.get(i).getOrderIndex().equals(orders.getSelectedItem())){
                    output.setText("This order belongs to Customer #" + orderList.get(i).getOrderCustomerIndex());
                }
            }
        }
    }//GEN-LAST:event_checkActionPerformed

    private void sodaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sodaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sodaActionPerformed

    private void outputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_outputActionPerformed

    private void printReceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printReceiptActionPerformed
        /* previews the receipt format using the selected order and customer and displays in the JTextArea on the right.
            Customers from different countries will have different tax rates and will be billed in their native currency.
        */
        if (selectC.getText().equals("Customer Selected:") || selectO.getText().equals("Order Selected:")){
            output.setText("You have not selected an order or customer yet");
        }
        else {
        String firstName = "", lastName = "", address = "", postalCode = "",
            phone = "", email = "", orderDateStr = "", id = "",
                billDateStr = "", countryStr = "";
        String str1 = selectC.getText().substring(19);
        String str2 = selectO.getText().substring(16);
        int pizzaAmt, burgerAmt, friesAmt, sodaAmt, hotdogAmt;
        double pizzaCost, burgerCost, taxRate, friesCost, sodaCost, hotdogCost;
        pizzaAmt = burgerAmt = friesAmt = sodaAmt = hotdogAmt = 0;
            pizzaCost = burgerCost = taxRate = friesCost = sodaCost = hotdogCost = 0;
        for (int i = 0; i<customerList.size(); i++){ //gets all required info from selected customer
            if (customerList.get(i).getId().equals(str1)){
                    Customer c = customerList.get(i);
                    firstName = c.getFirstName(); lastName = c.getLastName(); address = c.getAddress(); postalCode = c.getPostalCode();
                            phone = c.getPhone(); email = c.getEmail(); orderDateStr = c.getOrderDate();
                            billDateStr = c.getBillDate(); countryStr = c.getCountry();
                    break;
            }
        }
        for (int i = 0; i<orderList.size(); i++){ //gets all required info from selected order. Uses for loop.
            if (orderList.get(i).getOrderIndex().equals(str2)){ 
            Order o = orderList.get(i);
            pizzaAmt = o.getPizzaAmt(); burgerAmt = o.getBurgerAmt(); 
                    friesAmt = o.getFriesAmt(); sodaAmt = o.getSodaAmt(); hotdogAmt = o.getHotdogAmt();
            pizzaCost = Order.getPizzaCost(); burgerCost = Order.getBurgerCost(); id = o.getOrderIndex();
                friesCost = Order.getFriesCost(); sodaCost = Order.getSodaCost(); hotdogCost = Order.getHotdogCost();
            switch (countryStr){ //tax rates
            case "Canada" ->{ 
                taxRate = 0.13;
            }
            case "United States" -> {
                taxRate = 0.10;
            }
            case "United Kingdom" -> {
                taxRate = 0.20;
            }
            case "Mexico" ->  {
                taxRate = 0.16;
            }
            default -> System.out.println("An error occured.");
            }
            break;
            }
        }
        String formattedReceipt = generateReceipt(firstName, lastName, address, postalCode, email, phone, countryStr, id,
                orderDateStr, billDateStr, pizzaAmt, pizzaCost, burgerAmt, burgerCost, friesAmt, friesCost,
                sodaAmt, sodaCost, hotdogAmt, hotdogCost, taxRate);
        receipt.setText(formattedReceipt);
        }
    }//GEN-LAST:event_printReceiptActionPerformed

    private void hotdogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hotdogActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hotdogActionPerformed

    private void burgerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_burgerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_burgerActionPerformed

    private void modOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modOrderActionPerformed
        int pizzaAmt, burgerAmt, friesAmt, sodaAmt, hotdogAmt; //modifies currently selected order that is selected in the orderID jComboBox.
        if (pizzaTitle.isSelected() && isPositiveInt(pizza.getText())){
            pizzaAmt = Integer.parseInt(pizza.getText());
        }
        else pizzaAmt = 0;
        if (burgerTitle.isSelected() && isPositiveInt(burger.getText())){
            burgerAmt = Integer.parseInt(burger.getText());
        }
        else burgerAmt = 0;
        if (friesTitle.isSelected() && isPositiveInt(fries.getText())){
            friesAmt = Integer.parseInt(fries.getText());
        }
        else friesAmt = 0;
        if (sodaTitle.isSelected() && isPositiveInt(soda.getText())){
            sodaAmt = Integer.parseInt(soda.getText());
        }
        else sodaAmt = 0;
        if (hotdogTitle.isSelected() && isPositiveInt(hotdog.getText())){
            hotdogAmt = Integer.parseInt(hotdog.getText());
        }
        else hotdogAmt = 0;
        String str = selectC.getText();
        String id;
        if (str.equals("Customer Selected:")){
            id = null;
        }
        else{
            id = str.substring(19);
        }
        if (pizzaAmt == 0 && burgerAmt == 0 && friesAmt == 0 && sodaAmt == 0 && hotdogAmt == 0){
            output.setText("You have not entered any amounts yet");
        }
        else if ((id==null)){
            output.setText("Please select a customer first");
        }
        else if (selectO.getText().equals("Order Selected:")){
            output.setText("Please select an order to modify first");
        }
        else {
            String currentOrder = selectO.getText().substring(16);
            for (int i = 0;i<orderList.size();i++){
            if (orderList.get(i).getOrderIndex().equals(currentOrder)){ //remove, then add new order
                orderList.remove(i);
                Order o = new Order(pizzaAmt, burgerAmt, friesAmt, sodaAmt, hotdogAmt, currentOrder,id);
                orderList.add(i,o);
                output.setText("Order modified");
            }
            }
            customersField.setText("");
            for (int i = 0;i<orderList.size();i++){
                customersField.append(orderList.get(i).toString()+"\n");
            }
        }
    }//GEN-LAST:event_modOrderActionPerformed

    private void checkCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkCActionPerformed
    customersField.setText(""); //print all customer info
        for (Customer customerList1 : customerList) {
            customersField.append(customerList1.toString()+"\n");
        }
    }//GEN-LAST:event_checkCActionPerformed

    private void checkOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkOActionPerformed
    customersField.setText(""); //print all order info
        for (Order orderList1 : orderList) {
            customersField.append(orderList1.toString()+"\n");
        }
    }//GEN-LAST:event_checkOActionPerformed

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        String str1 = searchInput.getText().toLowerCase(), str2; //search customers by first name and records the amount of instances found
        boolean customFound = false;
        int amountFound = 0;
        int nameSize = str1.length();
        customersField.setText("");
                for (int i = 0; i<customerList.size();i++){
                    str2 = customerList.get(i).getFirstName();
                    if (nameSize<=str2.length()&& str1.equals(str2.substring(0,nameSize).toLowerCase())){
                        customersField.append(customerList.get(i).toString() + "\n");
                        customFound = true;
                        amountFound++;
                    }
                }
                if (!customFound){
                    output.setText("No customers were found");
                }
                else{
                    output.setText(amountFound + " matching customers were found");
                }
    }//GEN-LAST:event_searchActionPerformed

    private void sendEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendEmailActionPerformed
        String input = emailInput.getText(); //sends the receipt for the selected customers and orders to the emailInput JTextField
        if (selectC.getText().equals("Customer Selected:") || selectO.getText().equals("Order Selected:")){
            output.setText("You have not selected an order or customer yet");
        }
        else {
        String firstName = "", lastName = "", address = "", postalCode = "",
            phone = "", email = "", orderDateStr = "", id = "",
                billDateStr = "", countryStr = "";
        String str1 = selectC.getText().substring(19);
        String str2 = selectO.getText().substring(16);
        int pizzaAmt, burgerAmt, friesAmt, sodaAmt, hotdogAmt;
        double pizzaCost, burgerCost, taxRate, friesCost, sodaCost, hotdogCost;
        pizzaAmt = burgerAmt = friesAmt = sodaAmt = hotdogAmt = 0;
            pizzaCost = burgerCost = taxRate = friesCost = sodaCost = hotdogCost = 0;
        for (int i = 0; i<customerList.size(); i++){
            if (customerList.get(i).getId().equals(str1)){
                    Customer c = customerList.get(i);
                    firstName = c.getFirstName(); lastName = c.getLastName(); address = c.getAddress(); postalCode = c.getPostalCode();
                            phone = c.getPhone(); email = c.getEmail(); orderDateStr = c.getOrderDate();
                            billDateStr = c.getBillDate(); countryStr = c.getCountry();
                    break;
            }
        }
        for (int i = 0; i<orderList.size(); i++){
            if (orderList.get(i).getOrderIndex().equals(str2)){
            Order o = orderList.get(i);
            pizzaAmt = o.getPizzaAmt(); burgerAmt = o.getBurgerAmt(); 
                    friesAmt = o.getFriesAmt(); sodaAmt = o.getSodaAmt(); hotdogAmt = o.getHotdogAmt();
            pizzaCost = Order.getPizzaCost(); burgerCost = Order.getBurgerCost(); id = o.getOrderIndex();
                friesCost = Order.getFriesCost(); sodaCost = Order.getSodaCost(); hotdogCost = Order.getHotdogCost();
            switch (countryStr){
            case "Canada" -> taxRate = 0.13; //tax rates for each country
            case "United States" -> taxRate = 0.10; //0.10 is not correct, it is just for proof of concept
            case "United Kingdom" -> taxRate = 0.20;
            case "Mexico" ->  taxRate = 0.16;
            default -> System.out.println("An error occured.");
            }
            break;
            }
        }
        String formattedReceipt = generateReceipt(firstName, lastName, address, postalCode, email, phone, countryStr, id,
                orderDateStr, billDateStr, pizzaAmt, pizzaCost, burgerAmt, burgerCost, friesAmt, friesCost,
                sodaAmt, sodaCost, hotdogAmt, hotdogCost, taxRate);
        SendEmail sendE = new SendEmail(input,"Customer Receipt for " + firstName + " " + lastName,formattedReceipt);
        if (sendE.errorOccured){ //if email is invalid, exception is thrown and error is displayed in program
            output.setText("An error occured. Please double check your email input.");
        }
        else
          output.setText("Email was sent");
        }
    }//GEN-LAST:event_sendEmailActionPerformed

    private void cCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cCustomerActionPerformed
        first.setText(""); //sets all customer fields to blank
        last.setText("");
        address.setText("");
        pCode.setText("");
        email.setText("");
        phone.setText("");
        orderDate.setDate(null);
        billDate.setDate(null);
    }//GEN-LAST:event_cCustomerActionPerformed

    private void cOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cOrderActionPerformed
       pizzaTitle.setSelected(false); //sets all order fields to blank
       burgerTitle.setSelected(false);
       friesTitle.setSelected(false);
       hotdogTitle.setSelected(false);
       sodaTitle.setSelected(false);
       pizza.setText("");
       burger.setText("");
       fries.setText("");
       hotdog.setText("");
       soda.setText("");
       pizza.setEnabled(false);
       burger.setEnabled(false);
       fries.setEnabled(false);
       hotdog.setEnabled(false);
       soda.setEnabled(false);
    }//GEN-LAST:event_cOrderActionPerformed
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CustomerBillingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerBillingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerBillingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerBillingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CustomerBillingSystem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame Email;
    private javax.swing.JLabel aTitle;
    private javax.swing.JButton addCustomer;
    private javax.swing.JButton addOrder;
    private javax.swing.JTextField address;
    private javax.swing.JLabel bTitle;
    private com.toedter.calendar.JDateChooser billDate;
    private javax.swing.JTextField burger;
    private javax.swing.JCheckBox burgerTitle;
    private javax.swing.JButton cCustomer;
    private javax.swing.JLabel cNumTitle;
    private javax.swing.JButton cOrder;
    private javax.swing.JLabel cTitle;
    private javax.swing.JLabel cdTitle;
    private javax.swing.JButton check;
    private javax.swing.JButton checkC;
    private javax.swing.JButton checkO;
    private javax.swing.JComboBox<String> country;
    private javax.swing.JTextField customerID;
    private javax.swing.JComboBox<String> customers;
    private javax.swing.JTextArea customersField;
    private javax.swing.JLabel eTitle;
    private javax.swing.JTextField email;
    private javax.swing.JTextField emailInput;
    private javax.swing.JLabel fTitle;
    private javax.swing.JTextField first;
    private javax.swing.JLabel foodTitle;
    private javax.swing.JTextField fries;
    private javax.swing.JCheckBox friesTitle;
    private javax.swing.JTextField hotdog;
    private javax.swing.JCheckBox hotdogTitle;
    private javax.swing.JLabel idTitle;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel lTitle;
    private javax.swing.JTextField last;
    private javax.swing.JLabel m1;
    private javax.swing.JLabel m2;
    private javax.swing.JLabel m3;
    private javax.swing.JLabel m4;
    private javax.swing.JLabel m5;
    private javax.swing.JLabel m6;
    private javax.swing.JLabel m7;
    private javax.swing.JButton modOrder;
    private javax.swing.JButton modifyCustomer;
    private javax.swing.JLabel oDataTitle;
    private javax.swing.JLabel oTitle;
    private com.toedter.calendar.JDateChooser orderDate;
    private javax.swing.JLabel orderNumTitle;
    private javax.swing.JComboBox<String> orders;
    private javax.swing.JTextField output;
    private javax.swing.JTextField pCode;
    private javax.swing.JLabel pcTitle;
    private javax.swing.JTextField phone;
    private javax.swing.JTextField phoneInput;
    private javax.swing.JLabel phoneTitle;
    private javax.swing.JTextField pizza;
    private javax.swing.JCheckBox pizzaTitle;
    private javax.swing.JButton printReceipt;
    private javax.swing.JButton read;
    private javax.swing.JButton readOFile;
    private javax.swing.JTextArea receipt;
    private javax.swing.JButton refreshID;
    private javax.swing.JLabel sTitle;
    private javax.swing.JButton search;
    private javax.swing.JTextField searchInput;
    private javax.swing.JLabel selectC;
    private javax.swing.JButton selectCustomer;
    private javax.swing.JLabel selectO;
    private javax.swing.JButton selectOrder;
    private javax.swing.JButton send;
    private javax.swing.JButton sendEmail;
    private javax.swing.JButton sendText;
    private javax.swing.JLabel sendTitle;
    private javax.swing.JTextField soda;
    private javax.swing.JCheckBox sodaTitle;
    private javax.swing.JLabel title;
    private javax.swing.JButton wOFile;
    private javax.swing.JButton write;
    // End of variables declaration//GEN-END:variables
}
