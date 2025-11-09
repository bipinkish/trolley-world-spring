package com.bipinkish.store.repositories;

import com.bipinkish.store.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}