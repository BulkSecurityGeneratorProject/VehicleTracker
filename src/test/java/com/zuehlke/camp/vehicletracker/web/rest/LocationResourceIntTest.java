package com.zuehlke.camp.vehicletracker.web.rest;

import com.zuehlke.camp.vehicletracker.VehicleTrackerApp;
import com.zuehlke.camp.vehicletracker.domain.Location;
import com.zuehlke.camp.vehicletracker.repository.LocationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the LocationResource REST controller.
 *
 * @see LocationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VehicleTrackerApp.class)
@WebAppConfiguration
@IntegrationTest
public class LocationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_DEVICE_ID = "AAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBB";

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Double DEFAULT_LATTITUDE = 1D;
    private static final Double UPDATED_LATTITUDE = 2D;

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIME_STR = dateTimeFormatter.format(DEFAULT_TIME);

    @Inject
    private LocationRepository locationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLocationMockMvc;

    private Location location;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LocationResource locationResource = new LocationResource();
        ReflectionTestUtils.setField(locationResource, "locationRepository", locationRepository);
        this.restLocationMockMvc = MockMvcBuilders.standaloneSetup(locationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        locationRepository.deleteAll();
        location = new Location();
        location.setDeviceId(DEFAULT_DEVICE_ID);
        location.setLongitude(DEFAULT_LONGITUDE);
        location.setLattitude(DEFAULT_LATTITUDE);
        location.setTime(DEFAULT_TIME);
    }

    @Test
    public void createLocation() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // Create the Location

        restLocationMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(location)))
                .andExpect(status().isCreated());

        // Validate the Location in the database
        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeCreate + 1);
        Location testLocation = locations.get(locations.size() - 1);
        assertThat(testLocation.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testLocation.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testLocation.getLattitude()).isEqualTo(DEFAULT_LATTITUDE);
        assertThat(testLocation.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    public void checkDeviceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setDeviceId(null);

        // Create the Location, which fails.

        restLocationMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(location)))
                .andExpect(status().isBadRequest());

        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setLongitude(null);

        // Create the Location, which fails.

        restLocationMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(location)))
                .andExpect(status().isBadRequest());

        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkLattitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setLattitude(null);

        // Create the Location, which fails.

        restLocationMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(location)))
                .andExpect(status().isBadRequest());

        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setTime(null);

        // Create the Location, which fails.

        restLocationMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(location)))
                .andExpect(status().isBadRequest());

        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllLocations() throws Exception {
        // Initialize the database
        locationRepository.save(location);

        // Get all the locations
        restLocationMockMvc.perform(get("/api/locations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId())))
                .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID.toString())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].lattitude").value(hasItem(DEFAULT_LATTITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME_STR)));
    }

    @Test
    public void getLocation() throws Exception {
        // Initialize the database
        locationRepository.save(location);

        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(location.getId()))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.lattitude").value(DEFAULT_LATTITUDE.doubleValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME_STR));
    }

    @Test
    public void getNonExistingLocation() throws Exception {
        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateLocation() throws Exception {
        // Initialize the database
        locationRepository.save(location);
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location
        Location updatedLocation = new Location();
        updatedLocation.setId(location.getId());
        updatedLocation.setDeviceId(UPDATED_DEVICE_ID);
        updatedLocation.setLongitude(UPDATED_LONGITUDE);
        updatedLocation.setLattitude(UPDATED_LATTITUDE);
        updatedLocation.setTime(UPDATED_TIME);

        restLocationMockMvc.perform(put("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLocation)))
                .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locations.get(locations.size() - 1);
        assertThat(testLocation.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testLocation.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testLocation.getLattitude()).isEqualTo(UPDATED_LATTITUDE);
        assertThat(testLocation.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    public void deleteLocation() throws Exception {
        // Initialize the database
        locationRepository.save(location);
        int databaseSizeBeforeDelete = locationRepository.findAll().size();

        // Get the location
        restLocationMockMvc.perform(delete("/api/locations/{id}", location.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
