package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.UCSBSubject;
import edu.ucsb.cs156.example.repositories.UCSBSubjectRepository;
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

@Api(description = "UCSBSubjects")
@RequestMapping("/api/ucsbsubjects")
@RestController
@Slf4j
public class UCSBSubjectsController extends ApiController {

    private static class UCSBSubjectOrError {
        Long id;
        UCSBSubject ucsbSubject;
        ResponseEntity<String> error;

        public UCSBSubjectOrError(Long id) {
            this.id = id;
        }
    }

    @Autowired
    UCSBSubjectRepository ucsbSubjectRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "List all ucsb subjects")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<UCSBSubject> allUCSBSubjects() {
        Iterable<UCSBSubject> ucsbSubject = ucsbSubjectRepository.findAll();
        return ucsbSubject;
    }

    @ApiOperation(value = "Get a single subject (if it belongs to current user)")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<String> getUCSBSubjectById(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
        UCSBSubjectOrError uoe = new UCSBSubjectOrError(id);

        uoe = doesUCSBSubjectExist(uoe);
        if (uoe.error != null) {
            return uoe.error;
        }

        String body = mapper.writeValueAsString(uoe.ucsbSubject);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Update a single ucsbSubject")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("")
    public ResponseEntity<String> updateUCSBSubject(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid UCSBSubject incoming) throws JsonProcessingException {
        UCSBSubjectOrError uoe = new UCSBSubjectOrError(id);

        uoe = doesUCSBSubjectExist(uoe);
        if (uoe.error != null) {
            return uoe.error;
        }

        UCSBSubject oldSubject = uoe.ucsbSubject;
        oldSubject.setSubjectCode(incoming.getSubjectCode());
        oldSubject.setSubjectTranslation(incoming.getSubjectTranslation());
        oldSubject.setDeptCode(incoming.getDeptCode());
        oldSubject.setCollegeCode(incoming.getCollegeCode());
        oldSubject.setRelatedDeptCode(incoming.getRelatedDeptCode());
        oldSubject.setInactive(incoming.isInactive());

        ucsbSubjectRepository.save(oldSubject);

        String body = mapper.writeValueAsString(oldSubject);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Add new subject to database")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/post")
    public UCSBSubject postUCSBSubject(
            @ApiParam("subjectCode") @RequestParam String subjectCode,
            @ApiParam("subjectTranslation") @RequestParam String subjectTranslation,
            @ApiParam("deptCode") @RequestParam String deptCode,
            @ApiParam("collegeCode") @RequestParam String collegeCode,
            @ApiParam("relatedDeptCode") @RequestParam String relatedDeptCode,
            @ApiParam("inactive") @RequestParam boolean inactive) {
        
        log.info("subjectCode={}", subjectCode, "subjectTranslation={}", subjectTranslation, "deptCode={}", deptCode,
         "collegeCode={}", collegeCode, "relatedDeptCode={}", relatedDeptCode, "inactive={}", inactive);

        UCSBSubject ucsbSubject = new UCSBSubject();
        ucsbSubject.setSubjectCode(subjectCode);
        ucsbSubject.setSubjectTranslation(subjectTranslation);
        ucsbSubject.setDeptCode(deptCode);
        ucsbSubject.setCollegeCode(collegeCode);
        ucsbSubject.setRelatedDeptCode(relatedDeptCode);
        ucsbSubject.setInactive(inactive);
        UCSBSubject savedUCSubject = ucsbSubjectRepository.save(ucsbSubject);
        return savedUCSubject;
    }

    @ApiOperation(value = "Delete a UCSBSubject")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("")
    public ResponseEntity<String> deleteUCSBSubject(
            @ApiParam("id") @RequestParam Long id) {
        UCSBSubjectOrError uoe = new UCSBSubjectOrError(id);

        uoe = doesUCSBSubjectExist(uoe);
        if (uoe.error != null) {
            return uoe.error;
        }

        ucsbSubjectRepository.deleteById(id);
        return ResponseEntity.ok().body(String.format("UCSBSubject with id %d deleted", id));
    }

    public UCSBSubjectOrError doesUCSBSubjectExist(UCSBSubjectOrError uoe) {

        Optional<UCSBSubject> optionalUCSBSubject = ucsbSubjectRepository.findById(uoe.id);

        if (optionalUCSBSubject.isEmpty()) {
            uoe.error = ResponseEntity
                    .badRequest()
                    .body(String.format("id %d not found", uoe.id));
        } else {
            uoe.ucsbSubject = optionalUCSBSubject.get();
        }
        return uoe;
    }
}