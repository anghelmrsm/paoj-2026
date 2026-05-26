package com.pao.laboratory12;

import com.pao.laboratory12.model.Author;
import com.pao.laboratory12.model.Book;
import com.pao.laboratory12.model.Reader;
import com.pao.laboratory12.repository.AuthorRepository;
import com.pao.laboratory12.repository.BookRepository;
import com.pao.laboratory12.repository.LoanRepository;
import com.pao.laboratory12.repository.ReaderRepository;
import com.pao.laboratory12.service.AuditService;
import com.pao.laboratory12.service.LibraryService;
import com.pao.laboratory12.util.DatabaseConnection;
import com.pao.laboratory12.util.SchemaInitializer;

public class Main {
    public static void main(String[] args) throws Exception {
        SchemaInitializer.resetSchema();
        AuditService audit = AuditService.getInstance();
        AuthorRepository authors = new AuthorRepository();
        BookRepository books = new BookRepository();
        ReaderRepository readers = new ReaderRepository();
        LoanRepository loans = new LoanRepository();
        LibraryService library = LibraryService.getInstance();

        Author author = new Author("Gabriel Garcia Marquez", "CO");
        authors.save(author); audit.log("add_author");
        Book first = new Book("100 de ani de singuratate", author.getId());
        Book second = new Book("Dragostea in vremea holerei", author.getId());
        books.save(first); books.save(second); audit.log("add_book");
        Reader reader = new Reader("Ion Popescu", "ion@example.com");
        readers.save(reader); audit.log("add_reader");
        System.out.println(books.findAll()); audit.log("list_books");
        System.out.println(books.findById(first.getId()).orElse(null)); audit.log("find_book_by_id");
        first.setTitle("100 de ani de singuratate - editie noua");
        books.update(first); audit.log("update_book");
        long loanId = library.borrowBook(reader.getId(), first.getId()); audit.log("borrow_book");
        library.returnBook(loanId); audit.log("return_book");
        library.getActiveLoansWithDetails().forEach(System.out::println); audit.log("report_active_loans");
        loans.delete(loanId);
        readers.delete(reader.getId()); audit.log("delete_reader");
        System.out.println("Demo Lab12 finalizat.");
        DatabaseConnection.getInstance().close();
    }
}
