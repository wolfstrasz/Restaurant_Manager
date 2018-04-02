    @Test
    public void cancelReadyUpLight() {
        logger.info(makeBanner("cancelReadyUpLight"));

        input("1 18:00, PassButton, pb, press");
        expect("1 18:00, PassLight, pl, viewSwitchedOff");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    /*
     * Tests for Phase 1: - Test OfficeKVM methods
     */

    @Test
    public void addToMenu_NewItem() {
        /* Tests if the item is added to the menu. */
        logger.info(makeBanner("OfficeKVM_addToMenu_NewItem"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 19:00, OfficeKVM, okvm, showMenu");

        expect("1 19:00, OfficeKVM, okvm, viewMenu, tuples, 3," + " ID, Description, Price,"
                + " M1, Chicken Breast, 4.13," + " M2, Meatballs, 0.99");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void addToMenu_UpdateItem() {
        /*
         * Tests if it updates an item if we try to add an Item with the same
         * ID.
         */
        logger.info(makeBanner("OfficeKVM_addToMenu_UpdateItem"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Meatballs, 0.40");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");
        input("1 19:00, OfficeKVM, okvm, showMenu");

        expect("1 19:00, OfficeKVM, okvm, viewMenu, tuples, 3," + " ID, Description, Price," + " D1, Coca-cola, 1.73,"
                + " M1, Chicken Breast, 4.13," + " M2, Meatballs, 0.99");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void removeFromMenu_ExistingItem() {
        /* Tests if we remove an item when the item exists in the menu. */
        logger.info(makeBanner("OfficeKVM_removeFromMenu_ExistingItem"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 19:02, OfficeKVM, okvm, removeFromMenu, M1");
        input("1 19:00, OfficeKVM, okvm, showMenu");

        expect("1 19:00, OfficeKVM, okvm, viewMenu, tuples, 3," + " ID, Description, Price," + " M2, Meatballs, 0.99");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void removeFromMenu_NonExistingItem() {
        /*
         * Tests Error Assertion if we try to remove an item that does not
         * exists in the menu.
         */
        logger.info(makeBanner("OfficeKVM_removeFromMenu_NonExistingItem"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 19:02, OfficeKVM, okvm, removeFromMenu, M3");
        input("1 19:00, OfficeKVM, okvm, showMenu");

        expect("1 19:00, OfficeKVM, okvm, viewMenu, tuples, 3," 
                + " ID, Description, Price,"
                + " M1, Chicken Breast, 4.13," 
                + " M2, Meatballs, 0.99");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void addToMenu_NegativePrice() {
        /*
         * Tests if an assertion error is sent when an item with negative price
         * is to be added.
         */
        logger.info(makeBanner("OfficeKVM_addToMenu_NegativePrice"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, -0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");
        input("1 19:00, OfficeKVM, okvm, showMenu");

        expect("1 19:00, OfficeKVM, okvm, viewMenu, tuples, 3," + " ID, Description, Price," + " D1, Coca-cola, 1.73,"
                + " M1, Chicken Breast, 4.13");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    /*
     * Tests for Phase 2: - TableDisplay methods.
     */

    @Test
    public void showTableMenu() {
        /*
         * Tests if we can display the menu on the table display. Before and
         * after an Item is added.
         */
        logger.info(makeBanner("TableDisplay_showTableMenu"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 19:22, TableDisplay, td1, showMenu");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");
        input("1 5:00, TableDisplay, td1, showMenu");

        expect("1 19:22, TableDisplay, td1, viewMenu, tuples, 3," + " ID, Description, Price,"
                + " M1, Chicken Breast, 4.13," + " M2, Meatballs, 0.99");
        expect("1 5:00, TableDisplay, td1, viewMenu, tuples, 3," + " ID, Description, Price," + " D1, Coca-cola, 1.73,"
                + " M1, Chicken Breast, 4.13," + " M2, Meatballs, 0.99");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void startOrder() {
        /* Tests if it creates a new order. */
        logger.info(makeBanner("TableDisplay_startOrder"));

        input("1 19:00, TableDisplay, td2, startOrder");
        input("1 20:00, TableDisplay, td2, showTicket");

        expect("1 20:00, TableDisplay, td2, viewTicket, tuples, 3, " + "ID, Description, Count");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void startOrderExistingOrder() {
        /* Tests if it creates a new order over the last one. */
        logger.info(makeBanner("TableDisplay_startOrderExistingOrder"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:00, TableDisplay, td2, startOrder");
        input("1 20:00, TableDisplay, td2, showTicket");

        expect("1 20:00, TableDisplay, td2, viewTicket, tuples, 3, " 
                    + "ID, Description, Count");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void addMenuItem() {
        /* Tests if an item is added when it is a new one to the ticket. */
        logger.info(makeBanner("TableDisplay_addMenuItem"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 20:00, TableDisplay, td1, showTicket");

        expect("1 20:00, TableDisplay, td1, viewTicket, tuples, 3, " + "ID, Description, Count, " + "M2, Meatballs, 1");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void addMenuItem_Twice() {
        /*
         * Tests if the quantity of an existing item is increased when we add an
         * item that is already in the ticket.
         */
        logger.info(makeBanner("TableDisplay_addMenuItem_Twice"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, D1");
        input("1 20:00, TableDisplay, td1, showTicket");

        expect("1 20:00, TableDisplay, td1, viewTicket, tuples, 3, " + "ID, Description, Count, " + "D1, Coca-cola, 1, "
                + "M2, Meatballs, 2");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void addMenuItem_NonExisting() {
        /*
         * Tests if error message is sent if we try to add an item that does not
         * exist to the ticket.
         */
        logger.info(makeBanner("TableDisplay_addMenuItem_NonExisting"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, D7");
        input("1 20:00, TableDisplay, td1, showTicket");

        expect("1 20:00, TableDisplay, td1, viewTicket, tuples, 3, " + "ID, Description, Count, " + "M2, Meatballs, 2");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void removeMenuItem_Existing_CountMany() {
        /*
         * Tests if the quantity of an existing item is decreased when we remove
         * an item that has quantity >1.
         */
        logger.info(makeBanner("TableDisplay_removeMenuItem_Existing_CountMany"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, D1");
        input("1 19:13, TableDisplay, td1, removeMenuItem, M2");
        input("1 20:00, TableDisplay, td1, showTicket");

        expect("1 20:00, TableDisplay, td1, viewTicket, tuples, 3, " + "ID, Description, Count, " + "D1, Coca-cola, 1, "
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
        logger.info(makeBanner("TableDisplay_removeMenuItem_Existing_CountOne"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, D1");
        input("1 19:13, TableDisplay, td1, removeMenuItem, D1");
        input("1 20:00, TableDisplay, td1, showTicket");

        expect("1 20:00, TableDisplay, td1, viewTicket, tuples, 3, " + "ID, Description, Count, " + "M2, Meatballs, 2");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void removeMenuItem_NonExisting() {
        /*
         * Tests the assertion if we try to remove a non-existing item
         */
        logger.info(makeBanner("TableDisplay_removeMenuItem_NonExisting"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, D1");
        input("1 19:13, TableDisplay, td1, removeMenuItem, D1");
        input("1 19:25, TableDisplay, td1, removeMenuItem, D1");
        input("1 20:00, TableDisplay, td1, showTicket");

        expect("1 20:00, TableDisplay, td1, viewTicket, tuples, 3, " + "ID, Description, Count, " + "M2, Meatballs, 2");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void submitOrder() {
        /*
         * Tests if an order is successfully submited.
         */
        logger.info(makeBanner("TableDisplay_submitOrder"));

        input("1 18:00, OfficeKVM, okvm, addToMenu, M1, Chicken Breast, 4.13");
        input("1 18:01, OfficeKVM, okvm, addToMenu, M2, Meatballs, 0.99");
        input("1 18:03, OfficeKVM, okvm, addToMenu, D1, Coca-cola, 1.73");

        input("1 19:01, TableDisplay, td1, startOrder");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, M2");
        input("1 19:02, TableDisplay, td1, addMenuItem, D1");
        input("1 19:13, TableDisplay, td1, removeMenuItem, D1");
        input("1 20:00, TableDisplay, td1, showTicket");
        input("1 19:20, TableDisplay, td1, submitOrder");

        expect("1 20:00, TableDisplay, td1, viewTicket, tuples, 3, " + "ID, Description, Count, " + "M2, Meatballs, 2");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void payBill() {
        /*
         * Tests the payBill method of the table display. Tests if it is
         * connected to the expected cr and rp.
         */
        logger.info(makeBanner("TableDisplay_payBill()"));

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
    public void noConnectionInTickets() {
        /*
         * Tests if two table displays control only their own tickets and wrong
         * functionality does not exist.
         */
        logger.info(makeBanner("Check if both displays use same ticket"));

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

        expect("1 20:00, TableDisplay, td1, viewTicket, tuples, 3, " + "ID, Description, Count, " + "D1, Coca-cola, 2, "
                + "M2, Meatballs, 1");
        expect("1 21:00, TableDisplay, td2, viewTicket, tuples, 3, " + "ID, Description, Count, " + "M2, Meatballs, 4");

        runAndCheck();
        logger.info("End of test" + '\n' + '\n');
    }

    @Test
    public void noConnectionInTableDevices() {
        /*
         * Tests if table devices use their own printers and card readers.
         */
        logger.info(makeBanner("Check if groups of table devices mismatch"));

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
       
    /*
     * Many of the inputs of the tests below
     * will be equals as they are meant to set up
     * an environment that passes Phase 1 and 2.
     * "// Phase 3 Start " - will show from where input differs.
     * i.e where inputs for Phase 3 start
     */
     @Test
     public void turnLightOnPrintTicket(){
         logger.info(makeBanner("Turn light on"));
         
         input("1 10:00, OfficeKVM, okvm, addToMenu, M1, Fish, 8.88");
         input("1 10:01, TableDisplay, td1, startOrder");
         input("1 10:02, TableDisplay, td1, addMenuItem, M1");
         input("1 10:06, TableDisplay, td1, addMenuItem, M1");
         input("1 10:13, TableDisplay, td1, submitOrder");
         input("1 10:14, KitchenDisplay, kd, itemReady, 1, M1");
         input("1 10:18, KitchenDisplay, kd, itemReady, 1, M1");
         
         expect("1 10:14, TicketPrinter, tp, takeTicket, tuples, 3, "
                     + "Table:, Tab-1, _, ID, Description, Count, M1, Fish, 2");
         expect("1 10:18, PassLight, pl, viewSwitchedOn");
         
         runAndCheck();
         logger.info("End of test" + '\n' + '\n');
     }
     
     @Test 
     public void ready_Existing_First() {
         /*
          * Tests to see if when the first item of 
          * an order is ready it will print a ticket.
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
                 + "Table:, Tab-1, _, ID, Description, Count, "
                 + "D1, Coca-cola, 2, "
                 + "M1, Chicken Breast, 2");
         expect("1 20:15, TicketPrinter, tp, takeTicket, tuples, 3, "
                 + "Table:, Tab-2, _, ID, Description, Count, "
                 + "D1, Coca-cola, 1, "
                 + "M2, Meatballs, 1");
         
         
         runAndCheck();
         logger.info("End of test" + '\n' + '\n');
     }
     
     @Test public void ready_Existing_NotFirstNorLast() {
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
                 + "Table:, Tab-1, _, ID, Description, Count, "
                 + "D1, Coca-cola, 2, "
                 + "M1, Chicken Breast, 2");
         
         runAndCheck();
         logger.info("End of test" + '\n' + '\n');
     }
     
     @Test public void ready_Existing_Last() {
         logger.info(makeBanner(""));
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
         
         expect("1 20:14, TicketPrinter, tp, takeTicket, tuples, 3, "
                 + "Table:, Tab-1, _, ID, Description, Count, "
                 + "D1, Coca-cola, 2, "
                 + "M1, Chicken Breast, 2");
         expect("1 20:30, PassLight, pl, viewSwitchedOn");
         
         
         runAndCheck();
         logger.info("End of test" + '\n' + '\n');
     }
     
     @Test public void ready_NonExistingTicketNumber() {
         logger.info(makeBanner(""));
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
     
     @Test public void ready_NonExistingMenuID() {
         logger.info(makeBanner(""));
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
     
     @Test public void testTick() {
         logger.info(makeBanner(""));
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
     
