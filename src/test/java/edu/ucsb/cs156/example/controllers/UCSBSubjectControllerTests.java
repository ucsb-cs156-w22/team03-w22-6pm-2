package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.UCSBSubjects;
import edu.ucsb.cs156.example.entities.User;
import edu.ucsb.cs156.example.repositories.TodoRepository;
import edu.ucsb.cs156.example.repositories.UCSBSubjectsRepository;

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

@WebMvcTest(controllers = UCSBSubjectController.class)
@Import(TestConfig.class)
public class UCSBSubjectControllerTests extends ControllerTestCase {

    @MockBean
    UCSBSubjectsRepository ucsbSubjectRepository;

    @MockBean
    UserRepository userRepository;



   

    // Authorization tests for /api/todos/all

    @Test
    public void api_UCSBSubject_all__logged_out__returns_403() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().is(403));
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_UCSBSubject_all__user_logged_in__returns_200() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().isOk());
    }

    // Authorization tests for /api/todos/post

    @Test
    public void api_UCSBSubject_post__logged_out__returns_403() throws Exception {
        mockMvc.perform(post("/api/UCSBSubjects/post"))
                .andExpect(status().is(403));
    }

    

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_UCSBSubjects__user_logged_in__search_for_UCSBSubjects_that_does_not_exist() throws Exception {

        // arrange


        when(ucsbSubjectRepository.findById(eq(7L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/todos?id=7"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert

        verify(ucsbSubjectRepository, times(1)).findById(eq(7L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSBSubject with id 7 not found", responseString);
    }

    

    
   

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_UCSBSubject_all__user_logged_in() throws Exception {

        
        UCSBSubjects ucsbSubject1 = UCSBSubjects.builder().subjectCode("123").subjectTranslation("123").deptCode("123").collegeCode("123").relatedDeptCode("123").inactive(true).id(1L).build();
        UCSBSubjects ucsbSubject2 = UCSBSubjects.builder().subjectCode("1234").subjectTranslation("1234").deptCode("1234").collegeCode("1234").relatedDeptCode("1234").inactive(true).id(1L).build();

        ArrayList<UCSBSubjects> expectedUCSBSubjects = new ArrayList<>();
        expectedUCSBSubjects.addAll(Arrays.asList(ucsbSubject1, ucsbSubject2));
        when(ucsbSubjectRepository.findAll()).thenReturn(expectedUCSBSubjects);

        
        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().isOk()).andReturn();

        
        verify(ucsbSubjectRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedUCSBSubjects);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @Test
    public void api_UCSBSubject__delete_UCSBSubject() throws Exception {

    // arrange

    UCSBSubjects  ucsbSubject = UCSBSubjects.builder()
        .subjectCode("123")
        .subjectTranslation("123")
        .collegeCode("123")
        .deptCode("123")
        .collegeCode("123")
        .relatedDeptCode("123")
        .inactive(false)
        .id(123L).build();

    when(ucsbSubjectRepository.findById(eq(123L))).thenReturn(Optional.of(ucsbSubject));

    // act
    MvcResult response = mockMvc.perform(
        delete("/api/UCSBSubjects?id=123")
                .with(csrf()))
        .andExpect(status().isOk()).andReturn();

    // assert

    verify(ucsbSubjectRepository, times(1)).findById(123L);
    verify(ucsbSubjectRepository, times(1)).deleteById(123L);
    String responseString = response.getResponse().getContentAsString();
    assertEquals("record with id 123 deleted", responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_UCSBSubject__logged_in__cannot_delete_UCSBSubject_that_does_not_exist() throws Exception {
        // arrange

        UCSBSubjects  ucsbSubject = UCSBSubjects.builder()
            .subjectCode("123")
            .subjectTranslation("123")
            .collegeCode("123")
            .deptCode("123")
            .collegeCode("123")
            .relatedDeptCode("123")
            .inactive(false)
            .id(123L).build();

        when(ucsbSubjectRepository.findById(eq(123L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/UCSBSubjects?id=1123")
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).findById(17L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("record 123 not found", responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_UCSBSubject__user_logged_in__search_for_UCSBSubject_that_exists() throws Exception {

        // arrange

        UCSBSubjects ucsbSubject = UCSBSubjects.builder().subjectCode("123")
                .subjectTranslation("123")
                .deptCode("123")
                .collegeCode("123")
                .relatedDeptCode("123")
                .inactive(false)
                .id(17L)
                .build();

        when(ucsbSubjectRepository.findById(eq(17L))).thenReturn(Optional.of(ucsbSubject));

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects?id=17"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(ucsbSubjectRepository, times(1)).findById(eq(27L));
        String expectedJson = mapper.writeValueAsString(ucsbSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }




    @WithMockUser(roles = { "USER" })
    @Test
    public void api_UCSBSubject__user_logged_in__search_for_UCSBSubject_that_does_not_exist() throws Exception {

        // arrange

        

        when(ucsbSubjectRepository.findById(eq(7L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects?id=7"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert

        verify(ucsbSubjectRepository, times(1)).findById(eq(7L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSBSubject with id 7 not found", responseString);
    }



    @WithMockUser(roles = { "USER" })
    @Test
    public void api_UCSBSubject_post__user_logged_in() throws Exception {
        

        UCSBSubjects ucsbSubject = UCSBSubjects.builder()
                .subjectCode("123")
                .subjectTranslation("123")
                .deptCode("T123")
                .collegeCode("123")
                .relatedDeptCode("123")
                .inactive(true)
                .id(0L)
                .build();

        when(ucsbSubjectRepository.save(eq(ucsbSubject))).thenReturn(ucsbSubject);

        
        MvcResult response = mockMvc.perform(
                post("/api/UCSBSubjects/post?subjectCode=Test Code&subjectTranslation=Test Translation&deptCode=Test DeptCode&collegeCode=Test CollegeCode&relatedDeptCode=Test RelatedDeptCode&inactive=true")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        
        verify(ucsbSubjectRepository, times(1)).save(ucsbSubject);
        String expectedJson = mapper.writeValueAsString(ucsbSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    } 


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_UCSBSubject__user_logged_in__put_UCSBSubject() throws Exception {
        // arrange

        
        UCSBSubjects ucsbSubject1 = UCSBSubjects.builder().subjectCode("123")
                .subjectTranslation("123").deptCode("123").collegeCode("123").relatedDeptCode("123").inactive(false).id(67L).build();
        
        UCSBSubjects ucsbSubject2 = UCSBSubjects.builder().subjectCode("234")
                .subjectTranslation("234").deptCode("234").collegeCode("234").relatedDeptCode("234").inactive(false).id(67L).build();
        
        UCSBSubjects ucsbSubject3 = UCSBSubjects.builder().subjectCode("234")
                .subjectTranslation("234").deptCode("234").collegeCode("234").relatedDeptCode("234").inactive(false).id(67L).build();

        String requestBody = mapper.writeValueAsString(ucsbSubject2);
        String expectedReturn = mapper.writeValueAsString(ucsbSubject3);

        when(ucsbSubjectRepository.findById(eq(67L))).thenReturn(Optional.of(ucsbSubject1));

        // act
        MvcResult response = mockMvc.perform(
                put("/api/UCSBSubjects?id=67")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).findById(67L);
        verify(ucsbSubjectRepository, times(1)).save(ucsbSubject3); // should be saved with correct user
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedReturn, responseString);
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_UCSBSubject__user_logged_in__cannot_put_UCSBSubject_that_does_not_exist() throws Exception {
        // arrange

        UCSBSubjects ucsbSubject = UCSBSubjects.builder().subjectCode("123")
                .subjectTranslation("123").deptCode("123").collegeCode("123").relatedDeptCode("123").inactive(false).id(67L).build();
        
        String requestBody = mapper.writeValueAsString(ucsbSubject);

        when(ucsbSubjectRepository.findById(eq(67L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                put("/api/UCSBSubjects?id=67")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).findById(7L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSBSubject with id 67 not found", responseString);
    }
}