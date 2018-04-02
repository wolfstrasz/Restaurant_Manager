package roms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class KitchenController extends AbstractDevice {

    /* Fields */
    private KitchenDisplay      kitchenDisplay;
    private Rack                rack;
    private PassLight           passLight;
    private TicketPrinter       ticketPrinter;

    /* Add logger */
    private static final Logger logger = Logger.getLogger("roms");

    /* Setters and Getters */
    public KitchenController(String instance) {
        super(instance);
    }

    public void setKitchenDisplay(KitchenDisplay kitchenDisplay) {
        this.kitchenDisplay = kitchenDisplay;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }

    public void setPassLight(PassLight passLight) {
        this.passLight = passLight;
    }

    public void setTicketPrinter(TicketPrinter ticketPrinter) {
        this.ticketPrinter = ticketPrinter;
    }

    /* Methods */
    
    public void cancelReadyUp()
    {
        passLight.switchOff();
    }
    
    public void refreshDisplay() {
        logger.fine("\n");
        kitchenDisplay.displayRack(rack);
    }
    
    public void markItem(Integer ticketNumber, String menuID) {
        logger.info("Try to mark item ( " + ticketNumber.toString() + ", " + menuID + ")." + '\n');
        HashMap<Integer, Ticket> rackMap = rack.getRackMap();

        // Error assertion if there is no ticket with the given ticket number;
        if (!rackMap.containsKey(ticketNumber)) {
            
            logger.info("ERROR Occured!" + '\n' 
                            + "There is no existing ticket with number: " 
                            + ticketNumber.toString() + "." + '\n' 
                            + "End of test" + '\n' + '\n');
            assert false;
        }
        
        // If there is a ticket get it in curTicket and get its items.
        Ticket curTicket = rackMap.get(ticketNumber);
        HashMap<String, TicketItem> curTicketItems = curTicket.getTicketItems();
        
        // Error assertion if there is no item with the given menuID in the ticket
        if (!curTicketItems.containsKey(menuID)) {
            
            logger.info("ERROR Occured!" + '\n' 
                            + "There is no existing item with ID: "+ menuID 
                            + " in ticket: " + ticketNumber.toString() + "." + '\n' 
                            + "End of test" + '\n' + '\n');
            assert false;
        }
        
        // Error assertion if an item is ready and it has to be set ready again
        if (curTicketItems.get(menuID).getItemReadyQuantity() ==
            curTicketItems.get(menuID).getQuantity()) {
            
            logger.info("ERROR Occured!"  + '\n' 
                            + "All items from ticket: " + ticketNumber.toString() 
                            + " with ID: " + menuID  + " are ready!." + '\n' 
                            + "End of test" + '\n' + '\n');
            assert false;
        }

        // Depending on how many items are ready do something
        if (curTicket.getAllItemsReadyCount() == 0) {
            ticketPrinter.printTicket(curTicket);
        } else if (curTicket.getAllItemsCount() == curTicket.getAllItemsReadyCount() + 1) {
            passLight.switchOn();
        } else {
            // Do not print ticket or switch light on.
        }
        rack.markReady(ticketNumber, menuID);
    }

}
