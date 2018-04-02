/**
 * 
 */
package roms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

/**
 * @author pbj
 *
 */
public class SystemTest extends TestBasis {

    /*
     ***********************************************************************
     * BEGIN MODIFICATION AREA
     ***********************************************************************
     * Add all your JUnit tests for your system below.
     * 
    /*
     * Tests for Phase 1: - Test OfficeKVM methods
     */

    @Test
    public void addToMenu_NewItem() {
        /*
         *  Tests if new menu item is created and put in the menu.
         */
        logger.info(makeBanner("addToMenu_NewItem"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 19:00, OfficeKVM, okvm, showMenu");

        expect("1 19:00, OfficeKVM, okvm, viewMenu, tuples, 3," 
                + " ID, Description, Price,"
                + " M1, Chicken Breast, 4.13," 
                + " M2, Meatballs, 0.99");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void addToMenu_UpdateItem() {
        /*
         * Tests if it updates an item when we try to add an Item with the same menuID.
         */
        logger.info(makeBanner("addToMenu_UpdateItem"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Meatballs, 0.40");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");
        input("1 19:00, OfficeKVM, okvm, showMenu");

        expect("1 19:00, OfficeKVM, okvm, viewMenu, tuples, 3," 
                + " ID, Description, Price,"
                + " D1, Coca-cola, 1.73," 
                + " M1, Chicken Breast, 4.13," 
                + " M2, Meatballs, 0.99");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void removeFromMenu_ExistingItem() {
        /* Tests if we remove an item when the item exists in the menu. */
        logger.info(makeBanner("removeFromMenu_ExistingItem"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 19:02, OfficeKVM, okvm, removeFromMenu, M1");
        input("1 19:00, OfficeKVM, okvm, showMenu");

        expect("1 19:00, OfficeKVM, okvm, viewMenu, tuples, 3," 
                + " ID, Description, Price,"
                + " M2, Meatballs, 0.99");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test(expected=AssertionError.class)
    public void assert_removeFromMenu_NonExistingItem() {
        /*
         * Tests Error Assertion if we try to remove an item that does not
         * exists in the menu.
         * 
         */
        logger.info(makeBanner("assert_removeFromMenu_NonExistingItem"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 19:02, OfficeKVM, okvm, removeFromMenu, M3");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test(expected=AssertionError.class)
    public void assert_addToMenu_NegativePrice() {
        /*
         * Tests if an assertion error is sent when an item with negative price
         * is to be added.
         * 
         */
        logger.info(makeBanner("assert_addToMenu_NegativePrice"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, -0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");
        
        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    /*
     * Tests for Phase 2: - TableDisplay methods.
     */

    @Test
    public void showMenuAtTable() {
        /*
         * Tests if we can display the menu on the table display. Before and
         * after an Item is added.
         */
        logger.info(makeBanner("showMenuAtTable"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 19:22, TableDisplay, td1, showMenu");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");
        input("1 5:00, TableDisplay, td1, showMenu");

        expect("1 19:22, TableDisplay, td1, viewMenu, tuples, 3," 
                + " ID, Description, Price,"
                + " M1, Chicken Breast, 4.13," 
                + " M2, Meatballs, 0.99");
        expect("1 5:00, TableDisplay, td1, viewMenu, tuples, 3," 
                + " ID, Description, Price,"
                + " D1, Coca-cola, 1.73," 
                + " M1, Chicken Breast, 4.13," 
                + " M2, Meatballs, 0.99");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void startOrder() {
        /* Tests if it creates a new order. */
        logger.info(makeBanner("startOrder"));

        input("1 19:00, TableDisplay, td2, startOrder");
        input("1 20:00, TableDisplay, td2, showTicket");

        expect("1 20:00, TableDisplay, td2, viewTicket, tuples, 3, " 
                + "ID, Description, Count");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void startOrderExistingOrder() {
        /* Tests if it creates a new order over the last one. */
        logger.info(makeBanner("startOrderExistingOrder"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:00, TableDisplay, td2, startOrder");
        input("1 20:00, TableDisplay, td2, showTicket");

        expect("1 20:00, TableDisplay, td2, viewTicket, tuples, 3, ID, Description, Count");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void addMenuItem() {
        /* Tests if an item is added when it is a new one to the ticket. */
        logger.info(makeBanner("addMenuItem"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 20:00, TableDisplay, td1, showTicket");

        expect("1 20:00, TableDisplay, td1, viewTicket, tuples, 3, " 
                + "ID, Description, Count, "
                + "M2, Meatballs, 1");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void addMenuItem_Same() {
        /*
         * Tests if the quantity of an existing item is increased when we add an
         * item that is already in the ticket.
         */
        logger.info(makeBanner("addMenuItem_Same"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, D1");
        input("1 20:00, TableDisplay, td1, showTicket");

        expect("1 20:00, TableDisplay, td1, viewTicket, tuples, 3, " 
                + "ID, Description, Count, "
                + "D1, Coca-cola, 1, " 
                + "M2, Meatballs, 2");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test(expected=AssertionError.class)
    public void assert_addMenuItem_NonExisting() {
        /*
         * Tests if error message is sent if we try to add an item that does not
         * exist to the ticket.
         * 
         */
        logger.info(makeBanner("assert_addMenuItem_NonExisting"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, D7");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void removeMenuItem_Existing_CountMany() {
        /*
         * Tests if the quantity of an existing item is decreased when we remove
         * an item that has quantity >1.
         */
        logger.info(makeBanner("removeMenuItem_Existing_CountMany"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, D1");
        input("1 19:13, TableDisplay, td1, removeMenuItem, M2");
        input("1 20:00, TableDisplay, td1, showTicket");

        expect("1 20:00, TableDisplay, td1, viewTicket, tuples, 3, " 
                + "ID, Description, Count, "
                + "D1, Coca-cola, 1, " 
                + "M2, Meatballs, 1");
        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void removeMenuItem_Existing_CountOne() {
        /*
         * Tests if the whole item is removed from the ticket list when it has
         * quantity = 1 and remove request is made.
         */
        logger.info(makeBanner("removeMenuItem_Existing_CountOne"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, D1");
        input("1 19:13, TableDisplay, td1, removeMenuItem, D1");
        input("1 20:00, TableDisplay, td1, showTicket");

        expect("1 20:00, TableDisplay, td1, viewTicket, tuples, 3, " 
                + "ID, Description, Count, "
                + "M2, Meatballs, 2");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test(expected=AssertionError.class)
    public void assert_removeMenuItem_NonExisting() {
        /*
         * Tests the assertion if we try to remove a non-existing item.
         * 
         */
        logger.info(makeBanner("assert_removeMenuItem_NonExisting"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, D1");
        input("1 19:13, TableDisplay, td1, removeMenuItem, D1");
        input("1 19:25, TableDisplay, td1, removeMenuItem, D1");
        
        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void submitOrder() {
        /*
         * Tests if an order is successfully submitted.
         */
        logger.info(makeBanner("submitOrder"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, D1");
        input("1 19:13, TableDisplay, td1, removeMenuItem, D1");
        input("1 20:00, TableDisplay, td1, showTicket");
        input("1 20:20, TableDisplay, td1, submitOrder");
        input("1 20:20, Clock, c, tick");
        expect("1 20:00, TableDisplay, td1, viewTicket, tuples, 3, " 
                + "ID, Description, Count, "
                + "M2, Meatballs, 2");
        expect("1 20:20, KitchenDisplay, kd, viewRack, tuples, 6, "
                + "Time, Ticket#, MenuID, Description, #Ordered, #Ready, "
                + "0, 1, M2, Meatballs, 2, 0");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void payBill() {
        /*
         * Tests the payBill method of the table display. Tests if it is
         * connected to the expected cr and rp.
         */
        logger.info(makeBanner("payBill"));

        input("1 20:00, TableDisplay, td1, startOrder");
        input("1 20:02, TableDisplay, td1, submitOrder");
        input("1 21:30, TableDisplay, td1, payBill");
        input("1 21:32, CardReader, cr1, acceptCardDetails, SOMEDETAILS");
        input("1 21:33, BankClient, bc, acceptAuthorisationCode, SOMECODE");

        expect("1 21:30, TableDisplay, td1, approveBill, Total:, 0.00");
        expect("1 21:32, BankClient, bc, makePayment, SOMEDETAILS, 0.00");
        expect("1 21:33, ReceiptPrinter, rp1, takeReceipt, Total:, 0.00, AuthCode:, SOMECODE");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void noConnectionInOrderTickets() {
        /*
         * Tests if two table displays control only their own tickets and wrong
         * functionality does not exist.
         */
        logger.info(makeBanner("noConnectionInOrderTickets"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:01, TableDisplay, td2, startOrder");

        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td2, addMenuItem, M2");
        input("1 19:04, TableDisplay, td1, addMenuItem, D1");
        input("1 19:07, TableDisplay, td2, addMenuItem, M2");
        input("1 19:09, TableDisplay, td2, addMenuItem, M2");
        input("1 19:09, TableDisplay, td1, addMenuItem, D1");
        input("1 19:10, TableDisplay, td2, addMenuItem, M2");
        input("1 19:13, TableDisplay, td1, removeMenuItem, M2");

        input("1 20:00, TableDisplay, td1, showTicket");
        input("1 21:00, TableDisplay, td2, showTicket");

        expect("1 20:00, TableDisplay, td1, viewTicket, tuples, 3, " 
                + "ID, Description, Count, "
                + "D1, Coca-cola, 2, " 
                + "M2, Meatballs, 1");
        expect("1 21:00, TableDisplay, td2, viewTicket, tuples, 3, " 
                + "ID, Description, Count, "
                + "M2, Meatballs, 4");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void noConnectionInTableUnits() {
        /*
         * Tests if table devices use their own printers and card readers.
         */
        logger.info(makeBanner("NoConnectionInTableUnits"));

        input("1 20:00, TableDisplay, td1, startOrder");
        input("1 20:02, TableDisplay, td1, submitOrder");
        input("1 20:00, TableDisplay, td2, startOrder");
        input("1 20:02, TableDisplay, td2, submitOrder");

        input("1 20:02, TableDisplay, td2, payBill");
        input("1 21:32, CardReader, cr2, acceptCardDetails, SOMEDETAILS");
        input("1 21:33, BankClient, bc, acceptAuthorisationCode, FirstPay");

        input("1 21:30, TableDisplay, td1, payBill");
        input("1 21:37, CardReader, cr1, acceptCardDetails, VISA");
        input("1 21:39, BankClient, bc, acceptAuthorisationCode, SecondPay");

        expect("1 20:02, TableDisplay, td2, approveBill, Total:, 0.00");
        expect("1 21:32, BankClient, bc, makePayment, SOMEDETAILS, 0.00");
        expect("1 21:33, ReceiptPrinter, rp2, takeReceipt, Total:, 0.00, AuthCode:, FirstPay");

        expect("1 21:30, TableDisplay, td1, approveBill, Total:, 0.00");
        expect("1 21:37, BankClient, bc, makePayment, VISA, 0.00");
        expect("1 21:39, ReceiptPrinter, rp1, takeReceipt, Total:, 0.00, AuthCode:, SecondPay");
        ;

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }
    /*
     * Phase 3 Tests:
     */
    
    @Test
    public void cancelReadyUpLight() {
        logger.info(makeBanner("cancelReadyUpLight"));

        input("1 18:00, PassButton, pb, press");
        expect("1 18:00, PassLight, pl, viewSwitchedOff");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    /*
     * Many of the inputs of the tests below will be equals as they are meant to
     * set up an environment that passes Phase 1 and 2. "// Phase 3 Start " -
     * will show from where input differs. i.e where inputs for Phase 3 start
     */
    @Test
    public void turnLightOnPrintTicket() {
        logger.info(makeBanner("turnLightOnPrintTicket"));

        input("1 10:00, OfficeKVM, okvm, addToMenu, M1, Fish, 8.88");
        input("1 10:01, TableDisplay, td1, startOrder");
        input("1 10:02, TableDisplay, td1, addMenuItem, M1");
        input("1 10:06, TableDisplay, td1, addMenuItem, M1");
        input("1 10:13, TableDisplay, td1, submitOrder");
        input("1 10:14, KitchenDisplay, kd, itemReady, 1, M1");
        input("1 10:18, KitchenDisplay, kd, itemReady, 1, M1");

        expect("1 10:14, TicketPrinter, tp, takeTicket, tuples, 3, "
                + "Table:, Tab-1, _, "
                + "ID, Description, Count, "
                + "M1, Fish, 2");
        expect("1 10:18, PassLight, pl, viewSwitchedOn");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void ready_Existing_First() {
        /*
         * Tests to see if when the first item of an order is ready it will
         * print a ticket.
         */
        logger.info(makeBanner("ready_Existing_First"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");
        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M1");
        input("1 19:06, TableDisplay, td1, addMenuItem, D1");
        input("1 19:07, TableDisplay, td1, addMenuItem, M1");
        input("1 19:08, TableDisplay, td1, addMenuItem, D1");
        input("1 19:13, TableDisplay, td1, submitOrder");
        input("1 19:01, TableDisplay, td2, startOrder");
        input("1 19:02, TableDisplay, td2, addMenuItem, M2");
        input("1 19:08, TableDisplay, td2, addMenuItem, D1");
        input("1 19:52, TableDisplay, td2, submitOrder");

        // Phase 3 Start
        input("1 20:14, KitchenDisplay, kd, itemReady, 1, M1");
        input("1 20:15, KitchenDisplay, kd, itemReady, 2, D1");

        expect("1 20:14, TicketPrinter, tp, takeTicket, tuples, 3, "
                + "Table:, Tab-1, _, "
                + "ID, Description, Count, " 
                + "D1, Coca-cola, 2, "
                + "M1, Chicken Breast, 2");
        expect("1 20:15, TicketPrinter, tp, takeTicket, tuples, 3, "
                + "Table:, Tab-2, _, "
                + "ID, Description, Count, "
                + "D1, Coca-cola, 1, "
                + "M2, Meatballs, 1");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void ready_Existing_NotFirstNorLast() {
        logger.info(makeBanner("ready_Existing_NotFirstNorLast"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");
        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M1");
        input("1 19:06, TableDisplay, td1, addMenuItem, D1");
        input("1 19:07, TableDisplay, td1, addMenuItem, M1");
        input("1 19:08, TableDisplay, td1, addMenuItem, D1");
        input("1 19:13, TableDisplay, td1, submitOrder");

        // Phase 3 Start
        input("1 20:14, KitchenDisplay, kd, itemReady, 1, D1");
        input("1 20:15, KitchenDisplay, kd, itemReady, 1, M1");

        expect("1 20:14, TicketPrinter, tp, takeTicket, tuples, 3, "
                + "Table:, Tab-1, _, "
                + "ID, Description, Count, " 
                + "D1, Coca-cola, 2, "
                + "M1, Chicken Breast, 2");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void ready_Existing_Last() {
        logger.info(makeBanner("ready_Existing_Last"));
        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");
        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M1");
        input("1 19:06, TableDisplay, td1, addMenuItem, D1");
        input("1 19:07, TableDisplay, td1, addMenuItem, M1");
        input("1 19:08, TableDisplay, td1, addMenuItem, D1");
        input("1 19:13, TableDisplay, td1, submitOrder");

        // Phase 3 Start
        input("1 20:14, KitchenDisplay, kd, itemReady, 1, D1");
        input("1 20:14, KitchenDisplay, kd, itemReady, 1, D1");
        input("1 20:15, KitchenDisplay, kd, itemReady, 1, M1");
        input("1 20:30, KitchenDisplay, kd, itemReady, 1, M1");
        input("1 20:30, Clock, c, tick");
        expect("1 20:14, TicketPrinter, tp, takeTicket, tuples, 3, "
                + "Table:, Tab-1, _, "
                + "ID, Description, Count, " 
                + "D1, Coca-cola, 2, "
                + "M1, Chicken Breast, 2");
        expect("1 20:30, PassLight, pl, viewSwitchedOn");
        expect("1 20:30, KitchenDisplay, kd, viewRack, tuples, 6, "
                + "Time, Ticket#, MenuID, Description, #Ordered, #Ready, ");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test(expected=AssertionError.class)
    public void assert_ready_NonExistingTicketNumber() {
        /*
         * Tests if error message is sent when we try to mark an item ready from
         * a ticket that does not exist.
         * 
         */
        logger.info(makeBanner("assert_ready_NonExistingTicketNumber"));
        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");
        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M1");
        input("1 19:06, TableDisplay, td1, addMenuItem, D1");
        input("1 19:07, TableDisplay, td1, addMenuItem, M1");
        input("1 19:08, TableDisplay, td1, addMenuItem, D1");
        input("1 19:13, TableDisplay, td1, submitOrder");

        // Phase 3 Start
        input("1 20:14, KitchenDisplay, kd, itemReady, 2, D1");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test(expected=AssertionError.class)
    public void assert_ready_NonExistingMenuID() {
        /*
         * 
         * Tests if error message is sent when we try to mark an item ready from
         * a ticket that does not exist.
         * 
         */
        logger.info(makeBanner("assert_ready_NonExistingMenuID"));
        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");
        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M1");
        input("1 19:06, TableDisplay, td1, addMenuItem, D1");
        input("1 19:07, TableDisplay, td1, addMenuItem, M1");
        input("1 19:08, TableDisplay, td1, addMenuItem, D1");
        input("1 19:13, TableDisplay, td1, submitOrder");

        // Phase 3 Start
        input("1 20:14, KitchenDisplay, kd, itemReady, 1, D2");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void testTick() {
        logger.info(makeBanner("testTick"));
        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");
        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M1");
        input("1 19:06, TableDisplay, td1, addMenuItem, D1");
        input("1 19:07, TableDisplay, td1, addMenuItem, M1");
        input("1 19:08, TableDisplay, td1, addMenuItem, D1");
        input("1 19:13, TableDisplay, td1, submitOrder");
        input("1 19:01, TableDisplay, td2, startOrder");
        input("1 19:02, TableDisplay, td2, addMenuItem, M2");
        input("1 19:08, TableDisplay, td2, addMenuItem, D1");
        input("1 19:52, TableDisplay, td2, submitOrder");

        // Phase 3 Start
        input("1 20:30, Clock, c, tick");
        expect("1 20:30, KitchenDisplay, kd, viewRack, tuples, 6, "
                + "Time, Ticket#, MenuID, Description, #Ordered, #Ready, "
                + "77, 1, D1, Coca-cola, 2, 0, " 
                + "77, 1, M1, Chicken Breast, 2, 0, "
                + "38, 2, D1, Coca-cola, 1, 0, " 
                + "38, 2, M2, Meatballs, 1, 0");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }
    /*
     * Full System Tests (All phases)
     */

    @Test
    public void fullSystemTest() {
        logger.info("Full System Test #1 ");

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:00, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:00, OfficeKVM, okvm, addToMenu, M3, Soup, 1.25");
        input("1 18:00, OfficeKVM, okvm, addToMenu, M8, Pizza, 9.4");
        input("1 18:00, OfficeKVM, okvm, showMenu");
        expect("1 18:00, OfficeKVM, okvm, viewMenu, tuples, 3, "
                    + "ID, Description, Price, "
                    + "M1, Chicken Breast, 4.13, "
                    + "M2, Meatballs, 0.99, "
                    + "M3, Soup, 1.25, "
                    + "M8, Pizza, 9.40");

        input("1 18:01, OfficeKVM, okvm, addToMenu, D3, Beer, 2");
        input("1 18:01, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");
        input("1 18:01, OfficeKVM, okvm, addToMenu, D2, Soft Drink, 0.99");
        input("1 18:01, OfficeKVM, okvm, addToMenu, D4, Pepsi, 1.72");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.4");
        input("1 18:01, OfficeKVM, okvm, showMenu");
        
        expect("1 18:01, OfficeKVM, okvm, viewMenu, tuples, 3, " 
                    + "ID, Description, Price, " 
                    + "D1, Coca-cola, 1.73, " 
                    + "D2, Soft Drink, 0.99, " 
                    + "D3, Beer, 2.00, "
                    + "D4, Pepsi, 1.72, " 
                    + "M1, Chicken Breast, 4.13, " 
                    + "M2, Meatballs, 0.40, " 
                    + "M3, Soup, 1.25, " 
                    + "M8, Pizza, 9.40");
         
        input("1 19:00, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, D1");
        input("1 19:02, TableDisplay, td1, addMenuItem, M8");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, D3");
        input("1 19:03, TableDisplay, td1, showTicket");
        expect("1 19:03, TableDisplay, td1, viewTicket, tuples, 3, " 
                + "ID, Description, Count, " 
                + "D1, Coca-cola, 1, " 
                + "D3, Beer, 1, " 
                + "M2, Meatballs, 2, " 
                + "M8, Pizza, 1");
        
        input("1 19:10, TableDisplay, td1, submitOrder");
        input("1 19:15, Clock, c, tick");
        expect("1 19:15, KitchenDisplay, kd, viewRack, tuples, 6, " 
                + "Time, Ticket#, MenuID, Description, #Ordered, #Ready, "
                + "5, 1, D1, Coca-cola, 1, 0, " 
                + "5, 1, D3, Beer, 1, 0, "
                + "5, 1, M2, Meatballs, 2, 0, " 
                + "5, 1, M8, Pizza, 1, 0");
        
        input("1 19:14, KitchenDisplay, kd, itemReady, 1, D1");
        input("1 19:14, KitchenDisplay, kd, itemReady, 1, M2");
        expect("1 19:14, TicketPrinter, tp, takeTicket, tuples, 3, "
                + "Table:, Tab-1, _, "
                + "ID, Description, Count, " 
                + "D1, Coca-cola, 1, "
                + "D3, Beer, 1, " 
                + "M2, Meatballs, 2, " 
                + "M8, Pizza, 1");

        input("1 19:05, TableDisplay, td2, startOrder");
        input("1 19:05, TableDisplay, td2, addMenuItem, D1");
        input("1 19:20, TableDisplay, td2, submitOrder");
        input("1 19:22, Clock, c, tick");
        expect("1 19:22, KitchenDisplay, kd, viewRack, tuples, 6, " 
                + "Time, Ticket#, MenuID, Description, #Ordered, #Ready, " 
                + "12, 1, D1, Coca-cola, 1, 1, " 
                + "12, 1, D3, Beer, 1, 0, " 
                + "12, 1, M2, Meatballs, 2, 1, " 
                + "12, 1, M8, Pizza, 1, 0, " 
                + "2, 2, D1, Coca-cola, 1, 0");
         
        input("1 19:25, KitchenDisplay, kd, itemReady, 1, D3");
        input("1 19:25, KitchenDisplay, kd, itemReady, 2, D1");
        expect("1 19:25, TicketPrinter, tp, takeTicket, tuples, 3, "
                + "Table:, Tab-2, _, "
                + "ID, Description, Count, " 
                + "D1, Coca-cola, 1");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
        /* 
         * This should do some job, we hope.
         * We might not make it in time to finish this test.
         * But still it is a test.
         */
    }
    
    /*
     * Put all your JUnit system-level tests above.
     ***********************************************************************
     * END MODIFICATION AREA
     ***********************************************************************
     */

    /**
     * Set up system to be tested and connect interface devices to event
     * distributor and collector.
     * 
     * Setup is divided into two parts:
     * 
     * 1. Where IO device objects are created and connected to event distributor
     * and event collector.
     * 
     * 2. Where core system implementation objects are created and links are
     * added between all the implementation objects and the IO device objects.
     * 
     * The first part configures sufficient IO device objects for all 3
     * implementation phases defined in the Coursework 3 handout. It should not
     * be touched.
     * 
     * The second part here only gives the configuration for the demonstration
     * design. It has to be modified for the system implementation.
     * 
     * 
     */
    @Override
    protected void setupSystem() {

        /*
         * PART 1: - IO Device creation. - Establishing links between IO device
         * objects and test framework objects.
         */

        /*
         * IO DEVICES FOR KITCHEN AREA
         * 
         * Create IO objects for kitchen. Connect them to the event distributor
         * and event collector.
         */

        KitchenDisplay display = new KitchenDisplay("kd");
        display.addDistributorLinks(distributor);
        display.setCollector(collector);

        Clock.initialiseInstance();
        Clock.getInstance().addDistributorLinks(distributor);

        TicketPrinter printer = new TicketPrinter("tp");
        printer.setCollector(collector);

        /*
         * IO DEVICES FOR PASS
         */

        /*
         * Create IO objects for PASS Connect them to the event distributor and
         * event collector.
         */

        PassLight light = new PassLight("pl");
        light.setCollector(collector);

        PassButton button = new PassButton("pb");
        button.addDistributorLinks(distributor);

        // IO DEVICES FOR OFFICE

        // Create IO objects for office.
        // Connect them to the event distributor and event collector.

        OfficeKVM officeKVM = new OfficeKVM("okvm");
        officeKVM.addDistributorLinks(distributor);
        officeKVM.setCollector(collector);

        BankClient bankClient = new BankClient("bc");
        bankClient.addDistributorLinks(distributor);
        bankClient.setCollector(collector);

        // IO DEVICE FOR TABLES

        final int NUM_TABLES = 2;

        List<TableDisplay> tableDisplays = new ArrayList<TableDisplay>();
        List<ReceiptPrinter> receiptPrinters = new ArrayList<ReceiptPrinter>();
        List<CardReader> cardReaders = new ArrayList<CardReader>();

        for (int i = 1; i <= NUM_TABLES; i++) {

            String iString = Integer.toString(i);

            // Create IO objects for a table.
            // Connect to event distributor and collector

            TableDisplay tableDisplay = new TableDisplay("td" + iString);
            tableDisplay.addDistributorLinks(distributor);
            tableDisplay.setCollector(collector);
            tableDisplays.add(tableDisplay);

            ReceiptPrinter receiptPrinter = new ReceiptPrinter("rp" + iString);
            receiptPrinter.setCollector(collector);
            receiptPrinters.add(receiptPrinter);

            CardReader cardReader = new CardReader("cr" + iString);
            cardReader.addDistributorLinks(distributor);
            cardReaders.add(cardReader);

        }

        /*
         * PART 2: - Implementation object creation. - Establishing links
         * between different implementation objects and IO Device objects and
         * implementation objects.
         */

        /*
         ******************************************************************
         * BEGIN MODIFICATION AREA
         ******************************************************************
         * Put below code for creating implementation objects and connecting
         * them to the interface device objects.
         */

        // We removed the System Core instead we use KitchenController.
        // Phase 1 Objects:
        Menu menu = new Menu();

        // Phase 2 Objects:
        Rack rack = new Rack();
        List<TableConnection> tableConnections = new ArrayList<TableConnection>();
        for (int i = 1; i <= NUM_TABLES; i++) {

            TableConnection tableConnection = new TableConnection();
            tableConnections.add(tableConnection);

        }
        // Phase 3 Objects
        KitchenController kitchenController = new KitchenController("kc");

        // GENERAL
        // Create implementation objects that link to more than one area
        // and add links between implementation objects and interface device
        // objects in different areas.

        // Phase 1 Connections:
        officeKVM.setMenu(menu);

        // Phase 2 Connections:
        for (int i = 0; i < NUM_TABLES; i++) {
            tableDisplays.get(i).setTableConnection(tableConnections.get(i));

            tableConnections.get(i).setBankClient(bankClient);
            tableConnections.get(i).setCardReader(cardReaders.get(i));
            tableConnections.get(i).setReceiptPrinter(receiptPrinters.get(i));
            tableConnections.get(i).setTableDisplay(tableDisplays.get(i));
            tableConnections.get(i).setMenu(menu);
            tableConnections.get(i).setRack(rack);

        }

        // Phase 3 Connections:
        Clock.getInstance().setKitchenController(kitchenController);
        display.setKitchenController(kitchenController);
        button.setKitchenController(kitchenController);

        kitchenController.setKitchenDisplay(display);
        kitchenController.setRack(rack);
        kitchenController.setPassLight(light);
        kitchenController.setTicketPrinter(printer);
        kitchenController.setPassLight(light);
        /*
         * Put above code for creating implementation objects and connecting
         * them to the interface device objects.
         ******************************************************************
         * END MODIFICATION AREA
         ******************************************************************
         */

    }

}
