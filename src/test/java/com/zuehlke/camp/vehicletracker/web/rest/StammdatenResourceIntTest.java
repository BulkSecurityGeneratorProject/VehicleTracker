package com.zuehlke.camp.vehicletracker.web.rest;

import com.zuehlke.camp.vehicletracker.VehicleTrackerApp;
import com.zuehlke.camp.vehicletracker.domain.Stammdaten;
import com.zuehlke.camp.vehicletracker.repository.StammdatenRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the StammdatenResource REST controller.
 *
 * @see StammdatenResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VehicleTrackerApp.class)
@WebAppConfiguration
@IntegrationTest
public class StammdatenResourceIntTest {

    private static final String DEFAULT_DEVICE_ID = "AAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBB";
    private static final String DEFAULT_DRIVER = "AAAAA";
    private static final String UPDATED_DRIVER = "BBBBB";
    private static final String DEFAULT_VEHICLE = "AAAAA";
    private static final String UPDATED_VEHICLE = "BBBBB";

    @Inject
    private StammdatenRepository stammdatenRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStammdatenMockMvc;

    private Stammdaten stammdaten;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StammdatenResource stammdatenResource = new StammdatenResource();
        ReflectionTestUtils.setField(stammdatenResource, "stammdatenRepository", stammdatenRepository);
        this.restStammdatenMockMvc = MockMvcBuilders.standaloneSetup(stammdatenResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stammdatenRepository.deleteAll();
        stammdaten = new Stammdaten();
        stammdaten.setDeviceId(DEFAULT_DEVICE_ID);
        stammdaten.setDriver(DEFAULT_DRIVER);
        stammdaten.setVehicle(DEFAULT_VEHICLE);
    }

    @Test
    public void createStammdaten() throws Exception {
        int databaseSizeBeforeCreate = stammdatenRepository.findAll().size();

        // Create the Stammdaten

        restStammdatenMockMvc.perform(post("/api/stammdatens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stammdaten)))
                .andExpect(status().isCreated());

        // Validate the Stammdaten in the database
        List<Stammdaten> stammdatens = stammdatenRepository.findAll();
        assertThat(stammdatens).hasSize(databaseSizeBeforeCreate + 1);
        Stammdaten testStammdaten = stammdatens.get(stammdatens.size() - 1);
        assertThat(testStammdaten.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testStammdaten.getDriver()).isEqualTo(DEFAULT_DRIVER);
        assertThat(testStammdaten.getVehicle()).isEqualTo(DEFAULT_VEHICLE);
    }

    @Test
    public void checkDeviceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = stammdatenRepository.findAll().size();
        // set the field null
        stammdaten.setDeviceId(null);

        // Create the Stammdaten, which fails.

        restStammdatenMockMvc.perform(post("/api/stammdatens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stammdaten)))
                .andExpect(status().isBadRequest());

        List<Stammdaten> stammdatens = stammdatenRepository.findAll();
        assertThat(stammdatens).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDriverIsRequired() throws Exception {
        int databaseSizeBeforeTest = stammdatenRepository.findAll().size();
        // set the field null
        stammdaten.setDriver(null);

        // Create the Stammdaten, which fails.

        restStammdatenMockMvc.perform(post("/api/stammdatens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stammdaten)))
                .andExpect(status().isBadRequest());

        List<Stammdaten> stammdatens = stammdatenRepository.findAll();
        assertThat(stammdatens).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkVehicleIsRequired() throws Exception {
        int databaseSizeBeforeTest = stammdatenRepository.findAll().size();
        // set the field null
        stammdaten.setVehicle(null);

        // Create the Stammdaten, which fails.

        restStammdatenMockMvc.perform(post("/api/stammdatens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stammdaten)))
                .andExpect(status().isBadRequest());

        List<Stammdaten> stammdatens = stammdatenRepository.findAll();
        assertThat(stammdatens).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllStammdatens() throws Exception {
        // Initialize the database
        stammdatenRepository.save(stammdaten);

        // Get all the stammdatens
        restStammdatenMockMvc.perform(get("/api/stammdatens?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stammdaten.getId())))
                .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID.toString())))
                .andExpect(jsonPath("$.[*].driver").value(hasItem(DEFAULT_DRIVER.toString())))
                .andExpect(jsonPath("$.[*].vehicle").value(hasItem(DEFAULT_VEHICLE.toString())));
    }

    @Test
    public void getStammdaten() throws Exception {
        // Initialize the database
        stammdatenRepository.save(stammdaten);

        // Get the stammdaten
        restStammdatenMockMvc.perform(get("/api/stammdatens/{id}", stammdaten.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stammdaten.getId()))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID.toString()))
            .andExpect(jsonPath("$.driver").value(DEFAULT_DRIVER.toString()))
            .andExpect(jsonPath("$.vehicle").value(DEFAULT_VEHICLE.toString()));
    }

    @Test
    public void getNonExistingStammdaten() throws Exception {
        // Get the stammdaten
        restStammdatenMockMvc.perform(get("/api/stammdatens/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateStammdaten() throws Exception {
        // Initialize the database
        stammdatenRepository.save(stammdaten);
        int databaseSizeBeforeUpdate = stammdatenRepository.findAll().size();

        // Update the stammdaten
        Stammdaten updatedStammdaten = new Stammdaten();
        updatedStammdaten.setId(stammdaten.getId());
        updatedStammdaten.setDeviceId(UPDATED_DEVICE_ID);
        updatedStammdaten.setDriver(UPDATED_DRIVER);
        updatedStammdaten.setVehicle(UPDATED_VEHICLE);

        restStammdatenMockMvc.perform(put("/api/stammdatens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStammdaten)))
                .andExpect(status().isOk());

        // Validate the Stammdaten in the database
        List<Stammdaten> stammdatens = stammdatenRepository.findAll();
        assertThat(stammdatens).hasSize(databaseSizeBeforeUpdate);
        Stammdaten testStammdaten = stammdatens.get(stammdatens.size() - 1);
        assertThat(testStammdaten.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testStammdaten.getDriver()).isEqualTo(UPDATED_DRIVER);
        assertThat(testStammdaten.getVehicle()).isEqualTo(UPDATED_VEHICLE);
    }

    @Test
    public void deleteStammdaten() throws Exception {
        // Initialize the database
        stammdatenRepository.save(stammdaten);
        int databaseSizeBeforeDelete = stammdatenRepository.findAll().size();

        // Get the stammdaten
        restStammdatenMockMvc.perform(delete("/api/stammdatens/{id}", stammdaten.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Stammdaten> stammdatens = stammdatenRepository.findAll();
        assertThat(stammdatens).hasSize(databaseSizeBeforeDelete - 1);
    }
}
