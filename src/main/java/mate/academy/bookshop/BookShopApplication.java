package mate.academy.bookshop;

import java.math.BigDecimal;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookShopApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookShopApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book misteryBook = new Book();
            misteryBook.setTitle("Garry Potter");
            misteryBook.setAuthor("joanne rowling");
            misteryBook.setIsbn("978-0747532699");
            misteryBook.setPrice(BigDecimal.TEN);
            misteryBook.setDescription("young wizard's adventure");
            misteryBook.setCoverImage("7777777");

            bookService.save(misteryBook);

            System.out.println(bookService.getAll());
        };
    }
}
