package edu.ucsb.cs156.example.repositories;

import edu.ucsb.cs156.example.entities.UCSBSubjects;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UCSBSubjectsRepository extends CrudRepository<UCSBSubjects, Long> {
}