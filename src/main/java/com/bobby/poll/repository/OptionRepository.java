package com.bobby.poll.repository;

import com.bobby.poll.domain.Option;
import org.springframework.data.repository.CrudRepository;

public interface OptionRepository extends CrudRepository<Option, Long> {

}