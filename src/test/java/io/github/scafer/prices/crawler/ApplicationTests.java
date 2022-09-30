package io.github.scafer.prices.crawler;

import io.github.scafer.prices.crawler.util.EmbeddedMongoDBServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {
    private static EmbeddedMongoDBServer embeddedMongoDBServer;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    static void setup() throws IOException {
        embeddedMongoDBServer = EmbeddedMongoDBServer.create().start();
        embeddedMongoDBServer.addCollectionFromJson("locales",
                new String(Files.readAllBytes(Paths.get("src/test/resources/mongo-data/local.json"))));
    }

    @AfterAll
    static void tearDown() {
        embeddedMongoDBServer.stop();
    }

    @Test
    void sanityTest() {
        var entity = restTemplate.getForEntity("/actuator/health", String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }
}
