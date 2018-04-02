/**
 * 
 */
package roms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author pbj
 *
 */
public class Ticket {

    /* Fields */
    private String                      tableID;
    private Date                        submissionTime;
    private Integer                     allItemsCounter;
    private Integer                     allItemsReadyCounter;
    private HashMap<String, TicketItem> ticketItems;

    /* Add logger */
    private static final Logger         logger = Logger.getLogger("roms");

    /* Constructors: */
    public Ticket(String deviceName) {

        logger.info("Creating new order ticket for device " + deviceName + '\n');

        allItemsCounter = 0;
        allItemsReadyCounter = 0;
        ticketItems = new HashMap<String, TicketItem>();
    }
    
    public Ticket(Ticket ticket){
        logger.info("Creating new ticket from submited order ticket." + '\n');
        
        this.tableID = ticket.tableID;
        this.allItemsCounter = ticket.getAllItemsCount();
        this.allItemsReadyCounter = 0;
        this.ticketItems = ticket.getTicketItems();
    }

    /* Setters and Getters: */
    public void setTableID(String tableid) {
        tableID = tableid;
    }

    public String getTableID() {
        return tableID;
    }

    public void setSubmissionTime(Date submissionTime) {
        this.submissionTime = submissionTime;
    }

    public Date getSubmissionTime() {
        return this.submissionTime;
    }

    public Integer getAllItemsCount() {
        return allItemsCounter;
    }

    public Integer getAllItemsReadyCount() {
        return allItemsReadyCounter;
    }

    public HashMap<String, TicketItem> getTicketItems() {
        return ticketItems;
    }

    /* Methods: */

    public void increaseAllItemsReadyCount() {
        allItemsReadyCounter++;
    }
    
    public void increaseItemQuantityReady(String menuID) {
        ticketItems.get(menuID).setReady();
    }

    public void addItemToTicket(String menuID, String description) {

        logger.info("Adds new item to the ticket: " + menuID + ", " + description + '\n');

        if (ticketItems.containsKey(menuID)) {
            TicketItem item = ticketItems.get(menuID);
            item.addItem();
            ticketItems.put(menuID, item);
        } else {
            TicketItem item = new TicketItem(description);
            ticketItems.put(menuID, item);
        }
        allItemsCounter++;
    }

    public void removeItemFromTicket(String menuID) {

        logger.info("Removes an item from the ticket with ID: " + menuID + '\n');

        if (!ticketItems.containsKey(menuID)) {
            logger.info("ERROR occured!" + '\n' + "End of test" + '\n' + '\n');
            assert false : "There is no item with such ID in the ticket.";
        }

        if (ticketItems.get(menuID).getQuantity() > 1) {
            TicketItem item = ticketItems.get(menuID);
            item.removeItem();
            ticketItems.put(menuID, item);
        } else {
            ticketItems.remove(menuID);
        }
        allItemsCounter--;
    }

    public List<String> toStrings() {
        logger.info("Retrieving ticket items list" + '\n');
        List<String> ticketList = new ArrayList<String>();
        List<String> menuIDs = new ArrayList<String>();

        // Extract all IDs
        for (String menuID : ticketItems.keySet()) {
            menuIDs.add(menuID);
        }

        // Sort the IDs
        Collections.sort(menuIDs);

        // Iterate over the sorted IDs
        for (int i = 0; i < menuIDs.size(); i++) {
            String menuID = menuIDs.get(i);
            String description = ticketItems.get(menuID).getDesc();
            String quantity = ticketItems.get(menuID).getQuantity().toString();

            ticketList.add(menuID);
            ticketList.add(description);
            ticketList.add(quantity);
        }

        return ticketList;
    }

    /* End of the methods. */

    /**
     * Format ticket as list of strings, with, per ticket item, 3 strings for
     * respectively: - MenuID - Description - Count
     * 
     * Items are expected to be ordered by MenuID.
     * 
     * An example list is:
     * 
     * "D1", "Wine", "1", "D2", "Soft drink", "3", "M1", "Fish", "2", "M2",
     * "Veg chili", "2"
     * 
     * These lists of strings are used by TableDisplay and TicketPrinter to
     * produce formatted ticket output messages.
     * 
     * @return
     */

}
