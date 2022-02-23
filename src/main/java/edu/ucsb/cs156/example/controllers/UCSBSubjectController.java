
package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.UCSBSubjects;
import edu.ucsb.cs156.example.entities.User;
import edu.ucsb.cs156.example.models.CurrentUser;
import edu.ucsb.cs156.example.repositories.UCSBSubjectsRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Api(description = "UCSB Subjects")
@RequestMapping("/api/UCSBSubjects")
@RestController
@Slf4j
public class UCSBSubjectController extends ApiController {

    @Autowired
    UCSBSubjectsRepository ucsbSubjectRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "List all subjects")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public Iterable<UCSBSubjects> allUCSBSubjects() {
        //loggingService.logMethod();
        Iterable<UCSBSubjects> ucsbSubject = ucsbSubjectRepository.findAll();
        return ucsbSubject;
    }

    public class UCSBSubjectOrError {
        Long id;
        UCSBSubjects ucsbSubject;
        ResponseEntity<String> error;

        public UCSBSubjectOrError(Long id) {
            this.id = id;
        }
    }

    public UCSBSubjectOrError doesUCSBSubjectExist(UCSBSubjectOrError soe) {

        Optional<UCSBSubjects> optionalUCSBSubject = ucsbSubjectRepository.findById(soe.id);

        if (optionalUCSBSubject.isEmpty()) {
            soe.error = ResponseEntity
                    .badRequest()
                    .body(String.format("id %d not found", soe.id));
        } else {
            soe.ucsbSubject = optionalUCSBSubject.get();
        }
        return soe;
    }

    public UCSBSubjectOrError doesUCSBSubjectExist2(UCSBSubjectOrError soe) {

        Optional<UCSBSubjects> optionalUCSBSubject = ucsbSubjectRepository.findById(soe.id);

        if (optionalUCSBSubject.isEmpty()) {
            soe.error = ResponseEntity
                    .badRequest()
                    .body(String.format("record %d not found", soe.id));
        } else {
            soe.ucsbSubject = optionalUCSBSubject.get();
        }
        return soe;
    }

    @ApiOperation(value = "Get a single UCSBSubject with id")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<String> getUCSBSubjectById(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
        //loggingService.logMethod();
        UCSBSubjectOrError soe = new UCSBSubjectOrError(id);

        soe = doesUCSBSubjectExist(soe);
        if (soe.error != null) {
            return soe.error;
        }
        
        String body = mapper.writeValueAsString(soe.ucsbSubject);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Update a single ucsbSubject")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("")
    public ResponseEntity<String> putTodoById(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid UCSBSubjects incomingUCSBSubject) throws JsonProcessingException {
        //loggingService.logMethod();

        UCSBSubjectOrError uoe = new UCSBSubjectOrError(id);

        uoe = doesUCSBSubjectExist(uoe);
        if (uoe.error != null) {
            return uoe.error;
        }

        ucsbSubjectRepository.save(incomingUCSBSubject);

        String body = mapper.writeValueAsString(incomingUCSBSubject);
        return ResponseEntity.ok().body(body);
    }


    @ApiOperation(value = "Create a new Subject")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/post")
    public UCSBSubjects postUCSBSubject(
            @ApiParam("subjectCode") @RequestParam String subjectCode,
            @ApiParam("subjectTranslation") @RequestParam String subjectTranslation,
            @ApiParam("deptCode") @RequestParam String deptCode,
            @ApiParam("collegeCode") @RequestParam String collegeCode,
            @ApiParam("relatedDeptCode") @RequestParam String relatedDeptCode,
            @ApiParam("inactive") @RequestParam boolean inactive,
            @ApiParam("id") @RequestParam long id) {
        //loggingService.logMethod();
        
        log.info("subjectCode={}", subjectCode, "subjectTranslation={}", subjectTranslation, "deptCode={}", deptCode,
         "collegeCode={}", collegeCode, "relatedDeptCode={}", relatedDeptCode, "inactive={}", inactive, "id={}", id);

        UCSBSubjects ucsbSubject = new UCSBSubjects();
        ucsbSubject.setSubjectCode(subjectCode);
        ucsbSubject.setSubjectTranslation(subjectTranslation);
        ucsbSubject.setDeptCode(deptCode);
        ucsbSubject.setCollegeCode(collegeCode);
        ucsbSubject.setRelatedDeptCode(relatedDeptCode);
        ucsbSubject.setInactive(inactive);
        ucsbSubject.setId(id);
        UCSBSubjects savedUCSubject = ucsbSubjectRepository.save(ucsbSubject);
        return savedUCSubject;

        
    }


    @ApiOperation(value = "Delete a UCSBSubject")
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("")
    public ResponseEntity<String> deleteTodo(
            @ApiParam("id") @RequestParam Long id) {
        //loggingService.logMethod();


        UCSBSubjectOrError soe = new UCSBSubjectOrError(id);

        soe = doesUCSBSubjectExist2(soe);
        if (soe.error != null) {
            return soe.error;
        }

        
        ucsbSubjectRepository.deleteById(id);
        return ResponseEntity.ok().body(String.format("record %d deleted", id));

    }
}
