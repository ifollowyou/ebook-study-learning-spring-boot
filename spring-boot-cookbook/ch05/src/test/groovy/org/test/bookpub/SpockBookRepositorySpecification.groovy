package org.test.bookpub

import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ConfigurableWebApplicationContext
import org.test.bookpub.entity.Author
import org.test.bookpub.entity.Book
import org.test.bookpub.entity.Publisher
import org.test.bookpub.repository.BookRepository
import org.test.bookpub.repository.PublisherRepository;
import spock.lang.Shared;
import spock.lang.Specification

import javax.sql.DataSource

import static org.hamcrest.CoreMatchers.containsString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@ContextConfiguration(
        classes = [BookPubApplication.class,
                TestMockBeansConfig.class],
        loader = SpringApplicationContextLoader.class)
class SpockBookRepositorySpecification extends Specification {
    @Autowired
    private ConfigurableWebApplicationContext context

    @Shared
    boolean sharedSetupDone = false

    @Autowired
    private DataSource ds;

    @Autowired
    private BookRepository repository;

    @Shared
    private MockMvc mockMvc;

    void setup() {
        if (!sharedSetupDone) {
            mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

            sharedSetupDone = true
        }
        ResourceDatabasePopulator populator =
                new ResourceDatabasePopulator(
                        context.getResource("classpath:/packt-books.sql"));
        DatabasePopulatorUtils.execute(populator, ds);
    }

    @Transactional
    def "Test RESTful GET"() {
        when:
        def result = mockMvc.perform(get("/books/${isbn}"));

        then:
        result.andExpect(status().isOk())
        result.andExpect(content().string(containsString(title)));

        where:
        isbn               | title
        "978-1-78398-478-7"|"Orchestrating Docker"
        "978-1-78528-415-1"|"Spring Boot Recipes"
    }

    @Transactional
    def "Insert another book"() {
        setup:
        def existingBook = repository.findBookByIsbn("978-1-78528-415-1")
        def newBook = new Book("978-1-12345-678-9",
                "Some Future Book",
                existingBook.getAuthor(),
                existingBook.getPublisher())

        expect:
        repository.count() == 3

        when:
        def savedBook = repository.save(newBook)

        then:
        repository.count() == 4
        savedBook.id > -1
    }

    @Autowired
    private PublisherRepository publisherRepository

    def "Test RESTful GET books by publisher"() {
        setup:
        Publisher publisher = new Publisher("Strange Books")
        publisher.setId(999)
        Book book = new Book("978-1-98765-432-1",
                "Mystery Book",
                new Author("John", "Doe"),
                publisher)
        publisher.setBooks([book])
        Mockito.when(publisherRepository.count()).
                thenReturn(1L)
        Mockito.when(publisherRepository.findOne(1L)).
                thenReturn(publisher)

        when:
        def result = mockMvc.perform(get("/books/publisher/1"))

        then:
        result.andExpect(status().isOk())
        result.andExpect(content().
                string(containsString("Strange Books")))

        cleanup:
        Mockito.reset(publisherRepository)
    }
}
