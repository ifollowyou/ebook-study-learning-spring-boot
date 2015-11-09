package org.test.bookpub;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.test.bookpub.repository.PublisherRepository;

@Configuration
@UsedForTesting
public class TestMockBeansConfig {
    @Bean
    @Primary
    public PublisherRepository createMockPublisherRepository() {
        return Mockito.mock(PublisherRepository.class);
    }
}
