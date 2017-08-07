package com.singlepage.cms;

import com.singlepage.cms.models.Users;
import com.singlepage.cms.repositories.UsersRepository;
import com.singlepage.cms.services.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.util.ArrayList;

@Component
class AppRunner implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final BookingService bookingService;
    private final UsersRepository usersRepository;

    public AppRunner(BookingService bookingService, UsersRepository usersRepository) {
        this.bookingService = bookingService;
        this.usersRepository = usersRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // truncate table
        bookingService.deleteAllBookings();

        bookingService.book("Alice", "Bob",  "Carol");
        Assert.isTrue(bookingService.findAllBookings().size() == 3,
                "First booking should work with no problem");
        logger.info("Alice, Bob and Carol have been booked");

        // using UsersRepository to try to book
        Iterable<Users> users = usersRepository.findAll();
        ArrayList<String> firstNames = new ArrayList<String>();
        users.forEach(user -> firstNames.add(user.getFirstName()));
        String target[] = new String[firstNames.size()];
        target = firstNames.toArray(target);
        try {
            bookingService.book(target);
        } catch (RuntimeException e) {
            logger.info("v--- The following exception is expect because at least one of firstNames was too " +
                    "big for the DB ---v");
            logger.error(e.getMessage());
        }


        for (String user : bookingService.findAllBookings()) {
            logger.info("So far, " + user + " is booked.");
        }
        logger.info("You shouldn't see  Frodo or Legolas. Legolas violated DB constraints, " +
                "and Chris was rolled back in the same TX");
        Assert.isTrue(bookingService.findAllBookings().size() == 3,
                "'Samuel' should have triggered a rollback");

        try {
            bookingService.book("Buddy", null);
        } catch (RuntimeException e) {
            logger.info("v--- The following exception is expect because null is not " +
                    "valid for the DB ---v");
            logger.error(e.getMessage());
        }

        for (String user : bookingService.findAllBookings()) {
            logger.info("So far, " + user + " is booked.");
        }
        logger.info("You shouldn't see Buddy or null. null violated DB constraints, and " +
                "Buddy was rolled back in the same TX");
        Assert.isTrue(bookingService.findAllBookings().size() == 3,
                "'null' should have triggered a rollback");
    }
}