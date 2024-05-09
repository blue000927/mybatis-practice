package com.example.mybatispractice.application.service;

import com.example.mybatispractice.domain.model.Person;
import com.example.mybatispractice.infrastructure.mapper.PersonMapper;
import com.example.mybatispractice.presentation.dto.response.PersonResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

  private final PersonMapper personMapper;

  public PersonService(PersonMapper personMapper) {
    this.personMapper = personMapper;
  }

  public PersonResponse viewPerson(Long id) {
    Person person = personMapper.find(id);
    return PersonResponse.from(person);
  }

  @Transactional
  public PersonResponse createPerson(Person person) {
    int id = personMapper.create(person);
    Person foundPerson = personMapper.find((long) id);
    return PersonResponse.from(foundPerson);
  }
}