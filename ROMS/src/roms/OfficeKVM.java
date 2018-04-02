/**
 * 
 */
package roms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

/**
 * Model of Keyboard, Video monitor and Mouse devices in restaurant office.
 * 
 * @author pbj
 *
 */
public class OfficeKVM extends AbstractIODevice {
    private static final Logger logger = Logger.getLogger("roms");

    /**
     * 
     * @param instanceName
     */
    public OfficeKVM(String instanceName) {
        super(instanceName);
    }

    /**
     * Select device action based on input event message
     * 
     * @param e
     */
    @Override
    public void receiveEvent(Event e) {

        if (e.getMessageName().equals("showMenu") && e.getMessageArgs().size() == 0) {

            showMenu();

        } else if (e.getMessageName().equals("addToMenu") && e.getMessageArgs().size() == 3) {

            String menuID = e.getMessageArg(0);
            String description = e.getMessageArg(1);
            String price = e.getMessageArg(2);
            addToMenu(menuID, description, new Money(price));

        } else if (e.getMessageName().equals("removeFromMenu") && e.getMessageArgs().size() == 1) {

            String menuID = e.getMessageArg(0);
            removeFromMenu(menuID);

        } else {
            super.receiveEvent(e);
        }
    }

    /*
     ***********************************************************************
     * BEGIN MODIFICATION AREA
     ***********************************************************************
     * 
     * Add private field(s) for associations to classes that OfficeKVM objects
     * need to send messages to.
     * 
     * Add public setters for these fields.
     * 
     * Complete the methods for handling trigger input events.
     * 
     */
    private Menu menu;
    /*
     * FIELD(S) AND SETTER(S) FOR MESSAGE DESTINATIONS
     */

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    /*
     * SUPPORT FOR TRIGGER INPUT MESSAGES
     */

    public void showMenu() {

        logger.fine(getInstanceName() + '\n');
        displayMenu(menu);
    }

    public void addToMenu(String menuID, String description, Money price) {

        logger.fine(getInstanceName() + '\n');
        menu.addToMenu(menuID, description, price);
    }

    public void removeFromMenu(String menuID) {

        logger.fine(getInstanceName() + '\n');
        menu.removeFromMenu(menuID);
    }

    /*
     * Put all class modifications above.
     **********************************************************************
     * END MODIFICATION AREA
     *********************************************************************
     */

    /**
     * SUPPORT FOR displayMenu OUTPUT MESSAGE
     */

    public void displayMenu(Menu menu) {
        logger.fine(getInstanceName() + '\n');

        List<String> messageArgs = new ArrayList<String>();
        String[] preludeArgs = { "tuples", "3", "ID", "Description", "Price" };
        messageArgs.addAll(Arrays.asList(preludeArgs));
        messageArgs.addAll(menu.toStrings());
        sendMessage("viewMenu", messageArgs);

    }

}
