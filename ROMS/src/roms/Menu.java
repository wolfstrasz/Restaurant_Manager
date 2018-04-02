/**
 * 
 */
package roms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * 
 * Class that stores the menu items in a HashMap with </br>
 * menuID as key and MenuItem as value. </br>
 *
 * @author s1509922 </br>
 *
 */

public class Menu {

    

    /* Store the menu in a map with key = ID and value = MenuItem */
    private HashMap<String, MenuItem> menu   = new HashMap<String, MenuItem>();

    /* Add logger */
    private static final Logger       logger = Logger.getLogger("roms");

    /* Getters */
    /**
     * 
     * @return Returns the whole menu as HashMap
     */
    public HashMap<String, MenuItem> getMenu() {
        return menu;
    }

    /* Methods */
    
    /**
     * 
     * @param 
     * @return Retrieves the menu item with the given menuID.
     * 
     */
    public MenuItem getMenuItem(String menuID) {

        logger.info("Retrieving menu item with ID -> " + menuID + '\n');

        if (!menu.containsKey(menuID)) {
            logger.info("ERROR occured!" + '\n' 
                            + "An item with ID: " + menuID + "does not exists." + '\n'
                            + "End of test" + '\n' + '\n');
            assert false;
        }
        return menu.get(menuID);
    }
    /**
     * Creates an item with the given menuID,description and price.
     * @param menuID
     * 
     */
    public void addToMenu(String menuID, String description, Money price) {

        logger.info("Adding new item to menu -> " + menuID + ", " + description + ", "
                + price.toString() + '\n');

        if (price.toString().contains("-")) {
            logger.info("ERROR occured!" + '\n' 
                            + "The price has to be a positive number." + '\n' 
                            + "End of test" + '\n' + '\n');
            assert false;
        }

        MenuItem item = new MenuItem(description, price);
        menu.put(menuID, item);
    }

    /**
     * Removes an item with the given menuID from the menu.
     * @param menuID
     * 
     */
    public void removeFromMenu(String menuID) {

        logger.info("Removing item from menu with ID -> " + menuID + '\n');
        
        if (!menu.containsKey(menuID)) {
            logger.info("ERROR occured!" + '\n' 
                            + "An item with ID: " + menuID + " does not exist." + '\n' 
                            + "End of test" + '\n' + '\n');
            //assert false : "An item with this ID does not exist in the menu.(removeFromMenu)";
            assert false;
        }
        menu.remove(menuID);
        
    }
    /**
     * 
     * Format menu as list of strings, with, per menu item, 3 strings for 
     * respectively: 
     * - MenuID 
     * - Description
     * - Price 
     * 
     * Items are expected to be ordered by MenuID.
     * 
     * An example list is:
     * 
     * "D1", "Wine",        "2.50",
     * "D2", "Soft drink",  "1.50",
     * "M1", "Fish",        "7.95",
     * "M2", "Veg chili",   "6.70"
     * 
     * These lists of strings are used by TableDisplay and TicketPrinter
     * to produce formatted ticket output messages.
     * 
     * @return
     */
    public List<String> toStrings() {

        logger.info("Retrieving menu items list." + '\n');

        List<String> menuList = new ArrayList<String>();
        List<String> menuIDs = new ArrayList<String>();

        // Extract all IDs
        for (String menuID : menu.keySet()) {
            menuIDs.add(menuID);
        }

        // Sort the IDs
        Collections.sort(menuIDs);

        // Iterate over the sorted IDs
        for (int i = 0; i < menuIDs.size(); i++) {
            String menuID = menuIDs.get(i);
            MenuItem item = menu.get(menuID);
            String price = item.getPrice().toString();
            String description = item.getDescription();

            menuList.add(menuID);
            menuList.add(description);
            menuList.add(price);
        }
        return menuList;
    }

}