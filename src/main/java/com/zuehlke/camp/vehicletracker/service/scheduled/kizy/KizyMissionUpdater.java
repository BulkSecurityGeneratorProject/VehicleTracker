package com.zuehlke.camp.vehicletracker.service.scheduled.kizy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuehlke.camp.vehicletracker.config.JHipsterProperties;
import com.zuehlke.camp.vehicletracker.domain.Location;
import com.zuehlke.camp.vehicletracker.domain.Stammdaten;
import com.zuehlke.camp.vehicletracker.repository.LocationRepository;
import com.zuehlke.camp.vehicletracker.repository.StammdatenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Component
public class KizyMissionUpdater {

    private static final String LOGIN_AUTH_HEADER = "Set-Cookie";
    private static final String AUTH_HEADER = "Cookie";

    private final Logger log = LoggerFactory.getLogger(KizyMissionUpdater.class);

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    private StammdatenRepository stammdatenRepository;

    @Inject
    private LocationRepository locationRepository;

    private RestTemplate KIZY() {
        if (jHipsterProperties.getKizy().isEnabled()) {
            RestTemplate template = new RestTemplate();

            MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
            data.add("username", jHipsterProperties.getKizy().getUsername());
            data.add("password", jHipsterProperties.getKizy().getPassword());
            ResponseEntity<String> result = template.postForEntity("https://myviija.appspot.com/login", data, String.class);

            String authToken = result.getHeaders().getFirst(LOGIN_AUTH_HEADER);

            template.setInterceptors(Collections.singletonList((ClientHttpRequestInterceptor) (request, body, execution) -> {
                HttpHeaders headers = request.getHeaders();
                headers.add(AUTH_HEADER, authToken);
                return execution.execute(request, body);
            }));
            return template;
        }
        return null;
    }

    @Scheduled(fixedRate = 60000)
    public void execute() throws IOException {
        if (jHipsterProperties.getKizy().isEnabled()) {
            String result = KIZY().getForObject("https://myviija.appspot.com/mission/read", String.class);
            Map map = new ObjectMapper().readValue(result, Map.class);
            for (Object m : (Collection) map.get("missions")) {
                Map mission = (Map) m;
                if (mission.get("major") != null) {

                    String deviceId = (String) mission.get("tid");
                    String driver = (String) mission.get("code");
                    String lastPosition = (String) mission.get("lastPosition");

                    Stammdaten stammdaten = stammdatenRepository.findOneByDeviceId(deviceId);
                    if (stammdaten == null) {
                        stammdaten = new Stammdaten();
                        stammdaten.setDeviceId(deviceId);
                        stammdaten.setDriver(driver);
                        stammdaten.setVehicle("?");
                        stammdatenRepository.save(stammdaten);
                    }

                    Location location = new Location();
                    location.setDeviceId(deviceId);

                    String[] pos = lastPosition.split(",");
                    location.setLongitude(Double.parseDouble(pos[1]));
                    location.setLatitude(Double.parseDouble(pos[0]));
                    location.setTime(ZonedDateTime.now());
                    locationRepository.save(location);

                    log.debug("New Kizy location: {}", location);
                }
            }
        }
    }
}
