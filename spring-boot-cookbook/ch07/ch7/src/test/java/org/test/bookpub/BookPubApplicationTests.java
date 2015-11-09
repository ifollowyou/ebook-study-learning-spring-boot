package org.test.bookpub;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.test.bookpub.entity.Book;
import org.test.bookpub.repository.BookRepository;

import javax.sql.DataSource;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BookPubApplication.class)
@WebIntegrationTest("server.port:0")
public class BookPubApplicationTests {
	@Autowired
	private WebApplicationContext context;
	@Autowired
	private BookRepository repository;
	@Autowired
	private DataSource ds;
	@Value("${local.server.port}")
	private int port;

	private MockMvc mockMvc;
	private RestTemplate restTemplate = new TestRestTemplate();
	private static boolean loadDataFixtures = true;

	@Before
	public void setupMockMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Before
	public void loadDataFixtures() {
		if (loadDataFixtures) {
			ResourceDatabasePopulator populator = new ResourceDatabasePopulator(context.getResource("classpath:/test-data.sql"));
			DatabasePopulatorUtils.execute(populator, ds);
			loadDataFixtures = false;
		}
	}

	@Test
	public void contextLoads() {
		assertEquals(3, repository.count());
	}

	@Test
	public void webappBookIsbnApi() {
		Book book = restTemplate.getForObject("http://localhost:" + port + "/books/978-1-78528-415-1", Book.class);
		assertNotNull(book);
		assertEquals("Packt", book.getPublisher().getName());
	}

	@Test
	public void webappPublisherApi() throws Exception {
		mockMvc.perform(get("/publishers/1")).
				andExpect(status().isOk()).
				andExpect(content().contentType(MediaType.parseMediaType("application/hal+json"))).
				andExpect(content().string(containsString("Packt"))).
				andExpect(jsonPath("$.name").value("Packt"));
	}
}
