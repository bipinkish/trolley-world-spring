package com.bipinkish.store.repositories;

import com.bipinkish.store.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}