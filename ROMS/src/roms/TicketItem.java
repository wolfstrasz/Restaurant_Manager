package roms;

import java.util.logging.Logger;

public class TicketItem {

    /* Fields */
    private String              itemDesc;
    private Integer             itemQuantity;
    private Integer             itemReadyQuantity;

    /* Add logger */
    private static final Logger logger = Logger.getLogger("roms");

    /* Constructors */
    public TicketItem(String desc) {
        logger.info("Creating new TicketItem with arguments:  " + desc + '\n');

        itemDesc = desc;
        itemQuantity = 1;
        itemReadyQuantity = 0;
    }

    /* Setters and Getters */
    public String getDesc() {
        return itemDesc;
    }

    public Integer getQuantity() {
        return itemQuantity;
    }

    public Integer getItemReadyQuantity() {
        return itemReadyQuantity;
    }

    /* Methods */
    public void addItem() {
        itemQuantity++;
    }

    public void removeItem() {
        itemQuantity--;
    }
    
    public void setReady() {
        itemReadyQuantity++;
    }

}
