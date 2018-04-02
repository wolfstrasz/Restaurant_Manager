package roms;

import java.util.logging.Logger;

public class TableConnection {

    /* Fields */
    private TableDisplay   tableDisplay;
    private ReceiptPrinter receiptPrinter;
    private CardReader     cardReader;
    private BankClient     bankClient;
    private Rack           rack;
    private Menu           menu;
    
    /* Add logger */
    private static final Logger       logger = Logger.getLogger("roms");
    
    /* Setters */
    public void setTableDisplay(TableDisplay tableDisplay) {
        this.tableDisplay = tableDisplay;
    }

    public void setReceiptPrinter(ReceiptPrinter receiptPrinter) {
        this.receiptPrinter = receiptPrinter;
    }

    public void setCardReader(CardReader cardReader) {
        this.cardReader = cardReader;
    }

    public void setBankClient(BankClient bankClient) {
        this.bankClient = bankClient;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    
    /* Methods */

    public String getCardDetails() {
        logger.fine("In mediator class");
        return cardReader.waitForCardDetails();
    }

    public String requestAuthorisation(String cardDetails, Money paymentAmount) {
        logger.fine("In mediator class");
        return bankClient.authorisePayment(cardDetails, paymentAmount);
    }

    public void requestReceipt(Money paymentAmount, String authorisationCode) {
        logger.fine("In mediator class");
        receiptPrinter.printReceipt(paymentAmount, authorisationCode);
    }

    public void sendOrder(Ticket orderTicket) {
        logger.info("Sending order ticket from table: " + orderTicket.getTableID() + "\n");
        orderTicket.setTableID("Tab-" + tableDisplay.getInstanceName().substring(2));
        rack.receiveOrder(orderTicket);
    }

    public Menu requestMenu() {
        return menu;
    }
    
    public MenuItem requestMenuItem(String menuID) {
        return menu.getMenuItem(menuID);
    }
    
    public Money requestItemPrice(String menuID) {
        return menu.getMenuItem(menuID).getPrice();
    }

}
