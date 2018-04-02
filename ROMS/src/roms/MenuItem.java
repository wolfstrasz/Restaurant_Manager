package roms;

import java.util.logging.Logger;

public class MenuItem {

    /* Fields */
    private String              description;
    private Money               price;

    /* Add logger */
    private static final Logger logger = Logger.getLogger("roms");

    /* Constructors */
    public MenuItem(String description, Money price) {

        logger.info("Creating new MenuItem with arguments: "
                    + description + ", " + price.toString() + '\n');
        this.description = description;
        this.price = price;
    }

    /* Getters */
    String getDescription() {

        return description;
    }

    Money getPrice() {

        return price;
    }
}
