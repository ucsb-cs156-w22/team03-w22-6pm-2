package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.UCSBSubject;
import edu.ucsb.cs156.example.repositories.UCSBSubjectRepository;

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
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UCSBSubjectsController.class)
@Import(TestConfig.class)
public class UCSBSubjectsControllerTests extends ControllerTestCase {

        @MockBean
        UCSBSubjectRepository ucsbSubjectRepository;

        @MockBean
        UserRepository userRepository;

        // Authorization tests for /api/ucsbsubjects/admin/all

        @Test
        public void logged_out_users_cannot_get_all() throws Exception {
                mockMvc.perform(get("/api/ucsbsubjects/all"))
                                .andExpect(status().is(403)); // logged out users can't get all
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_users_can_get_all() throws Exception {
                mockMvc.perform(get("/api/ucsbsubjects/all"))
                                .andExpect(status().is(200)); // logged
        }

        @Test
        public void logged_out_users_cannot_get_by_id() throws Exception {
                mockMvc.perform(get("/api/ucsbsubjects?id=7"))
                                .andExpect(status().is(403)); // logged out users can't get by id
        }

        // Authorization tests for /api/ucsbsubjects/post
        // (Perhaps should also have these for put and delete)

        @Test
        public void logged_out_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/ucsbsubjects/post"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_regular_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/ucsbsubjects/post"))
                                .andExpect(status().is(403)); // only admins can post
        }

        // Tests with mocks for database actions

        @WithMockUser(roles = { "USER" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_exists() throws Exception {

                // arrange
                // LocalDateTime ldt = LocalDateTime.parse("2022-01-03T00:00:00");

                UCSBSubject ucsbSubject = UCSBSubject.builder()
                                .subjectCode("MATH")
                                .subjectTranslation("Math")
                                .deptCode("MATH")
                                .collegeCode("L&S")
                                .relatedDeptCode("null")
                                .inactive(false)
                                .build();

                when(ucsbSubjectRepository.findById(eq(7L))).thenReturn(Optional.of(ucsbSubject));

                // act
                MvcResult response = mockMvc.perform(get("/api/ucsbsubjects?id=7"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(ucsbSubjectRepository, times(1)).findById(eq(7L));
                String expectedJson = mapper.writeValueAsString(ucsbSubject);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_does_not_exist() throws Exception {

                // arrange

                when(ucsbSubjectRepository.findById(eq(7L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(get("/api/ucsbsubjects?id=7"))
                                .andExpect(status().isBadRequest()).andReturn();

                // assert

                verify(ucsbSubjectRepository, times(1)).findById(eq(7L));
                String responseString = response.getResponse().getContentAsString();
                assertEquals("UCSBSubject with id 7 not found", responseString);
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_user_can_get_all_ucsbsubjects() throws Exception {

                // arrange
                // LocalDateTime ldt1 = LocalDateTime.parse("2022-01-03T00:00:00");

                UCSBSubject ucsbSubject1 = UCSBSubject.builder()
                                .subjectCode("MATH")
                                .subjectTranslation("Math")
                                .deptCode("MATH")
                                .collegeCode("L&S")
                                .relatedDeptCode("null")
                                .inactive(false)
                                .build();

                // LocalDateTime ldt2 = LocalDateTime.parse("2022-03-11T00:00:00");

                UCSBSubject ucsbSubject2 = UCSBSubject.builder()
                                .subjectCode("CMPSC")
                                .subjectTranslation("Computer Science")
                                .deptCode("CMPSC")
                                .collegeCode("CoE")
                                .relatedDeptCode("null")
                                .inactive(false)
                                .build();

                ArrayList<UCSBSubject> expectedSubjects = new ArrayList<>();
                expectedSubjects.addAll(Arrays.asList(ucsbSubject1, ucsbSubject2));

                when(ucsbSubjectRepository.findAll()).thenReturn(expectedSubjects);

                // act
                MvcResult response = mockMvc.perform(get("/api/ucsbsubjects/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(ucsbSubjectRepository, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedSubjects);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void an_admin_user_can_post_a_new_ucsbdate() throws Exception {
                // arrange

                // LocalDateTime ldt1 = LocalDateTime.parse("2022-01-03T00:00:00");

                UCSBSubject ucsbSubject1 = UCSBSubject.builder()
                                .subjectCode("CMPSC")
                                .subjectTranslation("Computer_Science")
                                .deptCode("CMPSC")
                                .collegeCode("CoE")
                                .relatedDeptCode("null")
                                .inactive(false)
                                .build();

                when(ucsbSubjectRepository.save(eq(ucsbSubject1))).thenReturn(ucsbSubject1);

                // act
                MvcResult response = mockMvc.perform(
                                post("/api/ucsbsubjects/post?subjectCode=CMPSC&subjectTranslation=Computer_Science&deptCode=CMPSC&collegeCode=CoE&relatedDeptCode=null&inactive=false")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(ucsbSubjectRepository, times(1)).save(ucsbSubject1);
                String expectedJson = mapper.writeValueAsString(ucsbSubject1);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_can_delete_a_subject() throws Exception {
                // arrange

                // LocalDateTime ldt1 = LocalDateTime.parse("2022-01-03T00:00:00");

                UCSBSubject ucsbSubject1 = UCSBSubject.builder()
                                .subjectCode("CMPSC")
                                .subjectTranslation("Computer_Science")
                                .deptCode("CMPSC")
                                .collegeCode("CoE")
                                .relatedDeptCode("null")
                                .inactive(false)
                                .build();
             
               
                when(ucsbSubjectRepository.findById(eq(15L))).thenReturn(Optional.of(ucsbSubject1));

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/ucsbsubjects?id=15")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(ucsbSubjectRepository, times(1)).findById(15L);
                verify(ucsbSubjectRepository, times(1)).deleteById(15L);
                String responseString = response.getResponse().getContentAsString();
                assertEquals("UCSBSubject with id 15 deleted", responseString);
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_tries_to_delete_non_existant_ucsbsubject_and_gets_right_error_message() throws Exception {
                // arrange

                when(ucsbSubjectRepository.findById(eq(15L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/ucsbsubjects?id=15")
                                                .with(csrf()))
                                .andExpect(status().isBadRequest()).andReturn();

                // assert
                verify(ucsbSubjectRepository, times(1)).findById(15L);
                String responseString = response.getResponse().getContentAsString();
                assertEquals("UCSBSubject with id 15 not found", responseString);
        }

    
        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_can_edit_an_existing_ucsbsubject() throws Exception {
                // arrange

                // LocalDateTime ldt1 = LocalDateTime.parse("2022-01-03T00:00:00");
                // LocalDateTime ldt2 = LocalDateTime.parse("2023-01-03T00:00:00");

                UCSBSubject ucsbSubjectOrig = UCSBSubject.builder()
                                .subjectCode("CMPSC")
                                .subjectTranslation("Computer_Science")
                                .deptCode("CMPSC")
                                .collegeCode("CoE")
                                .relatedDeptCode("null")
                                .inactive(false)
                                .build();
             
                UCSBSubject ucsbSubjectEdited = UCSBSubject.builder()
                                .subjectCode("MATH")
                                .subjectTranslation("Math")
                                .deptCode("MATH")
                                .collegeCode("L&S")
                                .relatedDeptCode("null")
                                .inactive(false)
                                .build();
             
                String requestBody = mapper.writeValueAsString(ucsbSubjectEdited);

                when(ucsbSubjectRepository.findById(eq(67L))).thenReturn(Optional.of(ucsbSubjectOrig));

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/ucsbsubjects?id=67")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(ucsbSubjectRepository, times(1)).findById(67L);
                verify(ucsbSubjectRepository, times(1)).save(ucsbSubjectEdited); // should be saved with correct user
                String responseString = response.getResponse().getContentAsString();
                assertEquals(requestBody, responseString);
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_cannot_edit_ucsbsubject_that_does_not_exist() throws Exception {
                // arrange

                // LocalDateTime ldt1 = LocalDateTime.parse("2022-01-03T00:00:00");

                UCSBSubject ucsbEditedSubject = UCSBSubject.builder()
                                .subjectCode("MATH")
                                .subjectTranslation("Math")
                                .deptCode("MATH")
                                .collegeCode("L&S")
                                .relatedDeptCode("null")
                                .inactive(false)
                                .build();

                String requestBody = mapper.writeValueAsString(ucsbEditedSubject);

                when(ucsbSubjectRepository.findById(eq(67L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/ucsbsubjects?id=67")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isBadRequest()).andReturn();

                // assert
                verify(ucsbSubjectRepository, times(1)).findById(67L);
                String responseString = response.getResponse().getContentAsString();
                assertEquals("UCSBSubject with id 67 not found", responseString);
        }
}
