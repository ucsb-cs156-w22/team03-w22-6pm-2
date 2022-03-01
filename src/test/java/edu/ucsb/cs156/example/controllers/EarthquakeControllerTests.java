package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.services.EarthquakeQueryService;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.collections.EarthquakesCollection;
import edu.ucsb.cs156.example.collections.StudentCollection;
import edu.ucsb.cs156.example.documents.EarthquakeFeature;
import edu.ucsb.cs156.example.documents.EarthquakeFeatureCollection;
import edu.ucsb.cs156.example.documents.EarthquakeFeatureProperties;
import edu.ucsb.cs156.example.documents.EarthquakeMetadata;
import edu.ucsb.cs156.example.documents.Student;
import edu.ucsb.cs156.example.entities.Todo;
import edu.ucsb.cs156.example.entities.User;
import edu.ucsb.cs156.example.repositories.TodoRepository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import lombok.With;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = EarthquakeController.class)
@Import(TestConfig.class)
public class EarthquakeControllerTests extends ControllerTestCase {

        @MockBean
        EarthquakesCollection earthquakesCollection;

        @MockBean
        EarthquakeQueryService earthquakeQueryService;

        @MockBean
        UserRepository userRepository;

        // Authorization tests for /api/todos/all

        @Test
        public void api_earthquakes_all__logged_out__returns_403() throws Exception {
            mockMvc.perform(get("/api/earthquakes/all"))
                    .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void api_earthquakes_all__user_logged_in__returns_200() throws Exception {
            mockMvc.perform(get("/api/earthquakes/all"))
                    .andExpect(status().isOk());
        }


        // Tests with mocks for database actions

        @WithMockUser(roles = { "USER" })
        @Test
        public void api_earthquakes_all__user_logged_in__returns_a_earthquake_that_exists() throws Exception {

                // arrange

                EarthquakeFeatureProperties properties = EarthquakeFeatureProperties.builder()
                                .mag(2.4)
                                .place("SomePlace")
                                .time(1000000)
                                .updated(1000005)
                                .tz(3)
                                .url("randomURL")
                                .detail("random details")
                                .felt(6)
                                .cdi(3.9)
                                .mmi(1.4)
                                .alert("It's an Earthquake!!")
                                .status("Still Going")
                                .tsunami(0)
                                .sig(4)
                                .net("SomeNet")
                                .code("SomeCode")
                                .ids("SomeIds")
                                .sources("SomeSources")
                                .types("SomeTypes")
                                .nst(6)
                                .dmin(6.2)
                                .rms(4.7)
                                .gap(2)
                                .magType("SomeMagType")
                                .type("SomeType")
                                .title("SomeEarthquake")
                                .build();

                EarthquakeFeature features = EarthquakeFeature.builder()
                                .type("randomType")
                                .properties(properties)
                                .id("randomId")
                                .build();

                List<EarthquakeFeature> lef = new ArrayList<>();
                lef.add(features);

                when(earthquakesCollection.findAll()).thenReturn(lef);

                // act
                MvcResult response = mockMvc.perform(get("/api/earthquakes/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(earthquakesCollection, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(lef);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = {"ADMIN" })
        @Test
        public void api_earthquakes_post__user_logged_in__storeone_adds_earthquake_to_collection() throws Exception {

                // arrange

                EarthquakeFeatureProperties properties = EarthquakeFeatureProperties.builder()
                                .mag(2.4)
                                .place("SomePlace")
                                .time(1000000)
                                .updated(1000005)
                                .tz(3)
                                .url("randomURL")
                                .detail("random details")
                                .felt(6)
                                .cdi(3.9)
                                .mmi(1.4)
                                .alert("It's an Earthquake!!")
                                .status("Still Going")
                                .tsunami(0)
                                .sig(4)
                                .net("SomeNet")
                                .code("SomeCode")
                                .ids("SomeIds")
                                .sources("SomeSources")
                                .types("SomeTypes")
                                .nst(6)
                                .dmin(6.2)
                                .rms(4.7)
                                .gap(2)
                                .magType("SomeMagType")
                                .type("SomeType")
                                .title("SomeEarthquake")
                                .build();
                
                
                EarthquakeFeature features = EarthquakeFeature.builder()
                                .type("randomType")
                                .properties(properties)
                                .id("randomId")
                                .build();

                List<EarthquakeFeature> lef = new ArrayList<EarthquakeFeature>();
                lef.add(features);

                EarthquakeMetadata metadata = EarthquakeMetadata.builder()
                                .generated("randomGenerated")
                                .url("randomURL")
                                .title("randomTitle")
                                .status("randomStatus")
                                .api("randomAPI")
                                .count(4)
                                .build();
                
                EarthquakeFeatureCollection featureCollection = EarthquakeFeatureCollection.builder()
                                .type("randomType")
                                .metadata(metadata)
                                .features(lef)
                                .build();

                
                String minMag = "2";
                String distance = "100";

                String featuresAsJson = mapper.writeValueAsString(featureCollection);  
                String savedFeaturesAsJson = mapper.writeValueAsString(lef);         
                when(earthquakesCollection.saveAll(lef)).thenReturn(lef);

                when(earthquakeQueryService.getJSON(distance, minMag)).thenReturn(featuresAsJson);


                
                // act
                MvcResult response = mockMvc.perform(
                                post(String.format("/api/earthquakes/retrieve?distance=%s&minMag=%s",distance,minMag))
                                .with(csrf()))
                                .andExpect(status().isOk())
                                .andReturn();

                // assert

                verify(earthquakeQueryService, times(1)).getJSON(distance,minMag);
                verify(earthquakesCollection, times(1)).saveAll(lef);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(savedFeaturesAsJson, responseString);
        }

        @WithMockUser(roles = { "USER","ADMIN" })
        @Test
        public void api_purge_earthquake_is_void() throws Exception {

                MvcResult response = mockMvc.perform(post("/api/earthquakes/purge")
                                                .with(csrf()))
                                                .andExpect(status().isOk()).andReturn();

                verify(earthquakesCollection, times(1)).deleteAll(); 
                String responseString = response.getResponse().getContentAsString();
                assertEquals("Purged All Earthquakes",responseString); 
                             
        }

}