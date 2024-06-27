package net.branium.realtime;

import net.branium.common.RealtimeWeather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RealtimeWeatherRepositoryTests {

    @Autowired
    RealtimeWeatherRepository realtimeWeatherRepo;

    @Test
    void testFindByCountryCodeAndCityNameFound() {
        String countryCode = "US";
        String cityName = "New York City";
        RealtimeWeather realtimeWeather = realtimeWeatherRepo.findByCountryCodeAndCity(countryCode, cityName).get();

        assertAll(
                () -> assertNotNull(realtimeWeather),
                () -> assertNotNull(realtimeWeather.getLocation()),
                () -> assertEquals("NYC_USA", realtimeWeather.getLocationCode()),
                () -> assertEquals("Sunny", realtimeWeather.getStatus())
        );
    }

    @Test
    void testFindByCountryCodeAndCityNameNotFound() {
        String countryCode = "US";
        String cityName = "Los Angeles";
        RealtimeWeather realtimeWeather = realtimeWeatherRepo.findByCountryCodeAndCity(countryCode, cityName).orElse(null);

        System.out.println(realtimeWeather);

        assertAll(() -> assertNull(realtimeWeather));
    }

    @Test
    @Rollback(value = false)
    void testUpdateRealtimeWeatherSuccessful() {
        String realtimeWeatherLocationCode = "DELHI_IN";
        RealtimeWeather realtimeWeather = realtimeWeatherRepo.findById(realtimeWeatherLocationCode).orElse(null);
        if (realtimeWeather != null) {
            realtimeWeather.setHumidity(200);
            realtimeWeather.setLastUpdated(LocalDateTime.now());
            realtimeWeather.setStatus("Rainy");
            RealtimeWeather updatedRealtimeWeather = realtimeWeatherRepo.save(realtimeWeather);

            assertAll(
                    () -> assertNotNull(updatedRealtimeWeather),
                    () -> assertEquals(200, updatedRealtimeWeather.getHumidity()),
                    () -> assertEquals("Rainy", updatedRealtimeWeather.getStatus())
            );
        }
    }
}