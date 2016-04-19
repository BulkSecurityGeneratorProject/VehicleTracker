package com.zuehlke.camp.vehicletracker.web.rest;

import com.zuehlke.camp.vehicletracker.VehicleTrackerApp;
import com.zuehlke.camp.vehicletracker.domain.Order;
import com.zuehlke.camp.vehicletracker.repository.OrderRepository;

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
 * Test class for the OrderResource REST controller.
 *
 * @see OrderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VehicleTrackerApp.class)
@WebAppConfiguration
@IntegrationTest
public class OrderResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_DEVICE_ID = "AAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBB";
    private static final String DEFAULT_ORDER_NR = "AAAAA";
    private static final String UPDATED_ORDER_NR = "BBBBB";

    private static final Double DEFAULT_FROM_LATITUDE = 1D;
    private static final Double UPDATED_FROM_LATITUDE = 2D;

    private static final Double DEFAULT_FROM_LONGITUDE = 1D;
    private static final Double UPDATED_FROM_LONGITUDE = 2D;

    private static final Double DEFAULT_TO_LONGITUDE = 1D;
    private static final Double UPDATED_TO_LONGITUDE = 2D;

    private static final Double DEFAULT_TO_LATITUDE = 1D;
    private static final Double UPDATED_TO_LATITUDE = 2D;

    private static final ZonedDateTime DEFAULT_DEADLINE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DEADLINE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DEADLINE_STR = dateTimeFormatter.format(DEFAULT_DEADLINE);

    @Inject
    private OrderRepository orderRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOrderMockMvc;

    private Order order;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderResource orderResource = new OrderResource();
        ReflectionTestUtils.setField(orderResource, "orderRepository", orderRepository);
        this.restOrderMockMvc = MockMvcBuilders.standaloneSetup(orderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        orderRepository.deleteAll();
        order = new Order();
        order.setDeviceId(DEFAULT_DEVICE_ID);
        order.setOrderNr(DEFAULT_ORDER_NR);
        order.setFromLatitude(DEFAULT_FROM_LATITUDE);
        order.setFromLongitude(DEFAULT_FROM_LONGITUDE);
        order.setToLongitude(DEFAULT_TO_LONGITUDE);
        order.setToLatitude(DEFAULT_TO_LATITUDE);
        order.setDeadline(DEFAULT_DEADLINE);
    }

    @Test
    public void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order

        restOrderMockMvc.perform(post("/api/orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isCreated());

        // Validate the Order in the database
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orders.get(orders.size() - 1);
        assertThat(testOrder.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testOrder.getOrderNr()).isEqualTo(DEFAULT_ORDER_NR);
        assertThat(testOrder.getFromLatitude()).isEqualTo(DEFAULT_FROM_LATITUDE);
        assertThat(testOrder.getFromLongitude()).isEqualTo(DEFAULT_FROM_LONGITUDE);
        assertThat(testOrder.getToLongitude()).isEqualTo(DEFAULT_TO_LONGITUDE);
        assertThat(testOrder.getToLatitude()).isEqualTo(DEFAULT_TO_LATITUDE);
        assertThat(testOrder.getDeadline()).isEqualTo(DEFAULT_DEADLINE);
    }

    @Test
    public void checkDeviceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setDeviceId(null);

        // Create the Order, which fails.

        restOrderMockMvc.perform(post("/api/orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isBadRequest());

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkFromLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setFromLatitude(null);

        // Create the Order, which fails.

        restOrderMockMvc.perform(post("/api/orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isBadRequest());

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkFromLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setFromLongitude(null);

        // Create the Order, which fails.

        restOrderMockMvc.perform(post("/api/orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isBadRequest());

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkToLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setToLongitude(null);

        // Create the Order, which fails.

        restOrderMockMvc.perform(post("/api/orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isBadRequest());

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkToLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setToLatitude(null);

        // Create the Order, which fails.

        restOrderMockMvc.perform(post("/api/orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isBadRequest());

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.save(order);

        // Get all the orders
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId())))
                .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID.toString())))
                .andExpect(jsonPath("$.[*].orderNr").value(hasItem(DEFAULT_ORDER_NR.toString())))
                .andExpect(jsonPath("$.[*].fromLatitude").value(hasItem(DEFAULT_FROM_LATITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].fromLongitude").value(hasItem(DEFAULT_FROM_LONGITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].toLongitude").value(hasItem(DEFAULT_TO_LONGITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].toLatitude").value(hasItem(DEFAULT_TO_LATITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE_STR)));
    }

    @Test
    public void getOrder() throws Exception {
        // Initialize the database
        orderRepository.save(order);

        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(order.getId()))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID.toString()))
            .andExpect(jsonPath("$.orderNr").value(DEFAULT_ORDER_NR.toString()))
            .andExpect(jsonPath("$.fromLatitude").value(DEFAULT_FROM_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.fromLongitude").value(DEFAULT_FROM_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.toLongitude").value(DEFAULT_TO_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.toLatitude").value(DEFAULT_TO_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.deadline").value(DEFAULT_DEADLINE_STR));
    }

    @Test
    public void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateOrder() throws Exception {
        // Initialize the database
        orderRepository.save(order);
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        Order updatedOrder = new Order();
        updatedOrder.setId(order.getId());
        updatedOrder.setDeviceId(UPDATED_DEVICE_ID);
        updatedOrder.setOrderNr(UPDATED_ORDER_NR);
        updatedOrder.setFromLatitude(UPDATED_FROM_LATITUDE);
        updatedOrder.setFromLongitude(UPDATED_FROM_LONGITUDE);
        updatedOrder.setToLongitude(UPDATED_TO_LONGITUDE);
        updatedOrder.setToLatitude(UPDATED_TO_LATITUDE);
        updatedOrder.setDeadline(UPDATED_DEADLINE);

        restOrderMockMvc.perform(put("/api/orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOrder)))
                .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orders.get(orders.size() - 1);
        assertThat(testOrder.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testOrder.getOrderNr()).isEqualTo(UPDATED_ORDER_NR);
        assertThat(testOrder.getFromLatitude()).isEqualTo(UPDATED_FROM_LATITUDE);
        assertThat(testOrder.getFromLongitude()).isEqualTo(UPDATED_FROM_LONGITUDE);
        assertThat(testOrder.getToLongitude()).isEqualTo(UPDATED_TO_LONGITUDE);
        assertThat(testOrder.getToLatitude()).isEqualTo(UPDATED_TO_LATITUDE);
        assertThat(testOrder.getDeadline()).isEqualTo(UPDATED_DEADLINE);
    }

    @Test
    public void deleteOrder() throws Exception {
        // Initialize the database
        orderRepository.save(order);
        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Get the order
        restOrderMockMvc.perform(delete("/api/orders/{id}", order.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
