/**
 * 
 */
package roms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.Collections;

/**
 * The Order rack
 * 
 * @author pbj
 *
 */
public class Rack {

    /* Fields */
    private Integer                  ticketNumber = 0;
    private HashMap<Integer, Ticket> ticketList   = new HashMap<Integer, Ticket>();

    /* Add logger */
    private static final Logger      logger       = Logger.getLogger("roms");

    /* Constructors */
    Rack() {
        ticketNumber = 0;
        ticketList = new HashMap<Integer, Ticket>();
    }

    /* Setters and Getters */
    public HashMap<Integer, Ticket> getRackMap() {
        logger.info("Retrieving of ticket list" + '\n');
        return ticketList;
    }

    /* Methods */
    public void receiveOrder(Ticket orderTicket) {
        ticketNumber++;

        logger.info("Adding new ticket to rack with number " 
                        + ticketNumber.toString() + "." + '\n');

        Ticket ticket = new Ticket(orderTicket);
        Date curDateAndTime = Clock.getInstance().getDateAndTime();
        ticket.setSubmissionTime(curDateAndTime);
        ticketList.put(this.ticketNumber, ticket);
    }

    public void markReady(Integer tNumber, String menuID) {
        logger.info("Item from ticket " + tNumber.toString() 
                        + " with ID " + menuID + " is marked ready." + '\n');

        ticketList.get(tNumber).increaseItemQuantityReady(menuID);
        ticketList.get(tNumber).increaseAllItemsReadyCount();

        // remove ticket from ticketList if all items ready
        if (ticketList.get(tNumber).getAllItemsCount() == 
            ticketList.get(tNumber).getAllItemsReadyCount()) {
            
            ticketList.remove(tNumber);
        }

    }

    /**
     * Format rack contents as list of strings, with, per order item in each
     * order, 6 strings for respectively:
     * 
     * - Time - elapsed time in minutes since order submitted. - Ticket number -
     * MenuID identifying an order item on the ticket - Description of Menu item
     * with MenuID - Count, the number of MenuID items ordered - Ready number,
     * the number of MenuID items actually ready
     * 
     * All 6 tuples for order items in a given order have the same time and
     * number. This format with the time and ticket number repeated is not the
     * most elegant, but is chosen for simplicity.
     * 
     * The rack presents the contents of each order ticket with one or more
     * incomplete item orders. Tickets are placed in order of urgency, the most
     * urgent first. Specifically, Order items are expected to be ordered -
     * First by Ticket number, in increasing order (Each submitted ticket gets a
     * unique number, and numbers are allocated in ascending order, so lower
     * numbered tickets are always more urgent than higher numbered tickets.) -
     * second, by MenuID.
     * 
     * An example list is:
     * 
     * "15", "1", "D1", "Wine", "1", "0", 
     * "15", "1", "D3", "Tap water", "2",
     * "2", "15", "1", "M1", "Fish", "3", "0", 
     * "9", "2", "D4", "Coffee", "2", "2", 
     * "9", "2", "P2", "Cake", "2", "1"
     * 
     * This list of strings is used by the KitchenDisplay to show the contents
     * of the rack.
     * 
     * @return
     */
    public List<String> toStrings() {
        logger.fine("");
        List<String> ss = new ArrayList<String>();
        List<Integer> ticketNums = new ArrayList<Integer>();
        List<String> menuIDs = new ArrayList<String>();

        // Get all tickets' numbers
        for (Integer number : ticketList.keySet()) {

            ticketNums.add(number);
        }

        /*
         * As the rack sets the ticket number when the next ticket is submitted
         * the time since submission will increase the lower the ticket number
         * becomes, so instead of sorting by submission time we can sort by
         * ticket number.
         */

        // Sort ticket numbers so the lowest one is first
        Collections.sort(ticketNums);

        for (int index = 0; index < ticketNums.size(); index++) {

            // For each ticket DO:
            Integer curTicketNum = ticketNums.get(index);
            Ticket curTicket = ticketList.get(curTicketNum);
            HashMap<String, TicketItem> curTicketItems = curTicket.getTicketItems();

            // Get time passed since submission
            Date subTime = curTicket.getSubmissionTime();
            Date curTime = Clock.getInstance().getDateAndTime();
            Integer timePassed = Clock.minutesBetween(subTime, curTime);

            // Get all item's menuIDs from the ticket
            menuIDs = new ArrayList<String>();
            for (String menuID : curTicketItems.keySet()) {
                menuIDs.add(menuID);
            }

            // Sort the menuIDs
            Collections.sort(menuIDs);

            for (int ind = 0; ind < menuIDs.size(); ind++) {

                // Get each item
                String id = menuIDs.get(ind);
                TicketItem item = curTicketItems.get(id);

                // Put arguments in order
                ss.add(timePassed.toString());
                ss.add(curTicketNum.toString());
                ss.add(id);
                ss.add(curTicketItems.get(id).getDesc().toString());
                ss.add(item.getQuantity().toString());
                ss.add(item.getItemReadyQuantity().toString());
            }
        }

        return ss;
    }

}
