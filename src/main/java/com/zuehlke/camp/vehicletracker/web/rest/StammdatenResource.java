package com.zuehlke.camp.vehicletracker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.zuehlke.camp.vehicletracker.domain.Stammdaten;
import com.zuehlke.camp.vehicletracker.repository.StammdatenRepository;
import com.zuehlke.camp.vehicletracker.web.rest.util.HeaderUtil;
import com.zuehlke.camp.vehicletracker.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Stammdaten.
 */
@RestController
@RequestMapping("/api")
public class StammdatenResource {

    private final Logger log = LoggerFactory.getLogger(StammdatenResource.class);
        
    @Inject
    private StammdatenRepository stammdatenRepository;
    
    /**
     * POST  /stammdatens : Create a new stammdaten.
     *
     * @param stammdaten the stammdaten to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stammdaten, or with status 400 (Bad Request) if the stammdaten has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/stammdatens",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stammdaten> createStammdaten(@Valid @RequestBody Stammdaten stammdaten) throws URISyntaxException {
        log.debug("REST request to save Stammdaten : {}", stammdaten);
        if (stammdaten.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stammdaten", "idexists", "A new stammdaten cannot already have an ID")).body(null);
        }
        Stammdaten result = stammdatenRepository.save(stammdaten);
        return ResponseEntity.created(new URI("/api/stammdatens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stammdaten", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stammdatens : Updates an existing stammdaten.
     *
     * @param stammdaten the stammdaten to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stammdaten,
     * or with status 400 (Bad Request) if the stammdaten is not valid,
     * or with status 500 (Internal Server Error) if the stammdaten couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/stammdatens",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stammdaten> updateStammdaten(@Valid @RequestBody Stammdaten stammdaten) throws URISyntaxException {
        log.debug("REST request to update Stammdaten : {}", stammdaten);
        if (stammdaten.getId() == null) {
            return createStammdaten(stammdaten);
        }
        Stammdaten result = stammdatenRepository.save(stammdaten);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("stammdaten", stammdaten.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stammdatens : get all the stammdatens.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stammdatens in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/stammdatens",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Stammdaten>> getAllStammdatens(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Stammdatens");
        Page<Stammdaten> page = stammdatenRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stammdatens");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stammdatens/:id : get the "id" stammdaten.
     *
     * @param id the id of the stammdaten to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stammdaten, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/stammdatens/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stammdaten> getStammdaten(@PathVariable String id) {
        log.debug("REST request to get Stammdaten : {}", id);
        Stammdaten stammdaten = stammdatenRepository.findOne(id);
        return Optional.ofNullable(stammdaten)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stammdatens/:id : delete the "id" stammdaten.
     *
     * @param id the id of the stammdaten to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/stammdatens/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStammdaten(@PathVariable String id) {
        log.debug("REST request to delete Stammdaten : {}", id);
        stammdatenRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stammdaten", id.toString())).build();
    }

}
