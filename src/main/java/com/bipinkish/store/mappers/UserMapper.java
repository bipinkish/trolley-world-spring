package com.bipinkish.store.mappers;

import com.bipinkish.store.dto.NewUserRequest;
import com.bipinkish.store.dto.UpdateUserRequest;
import com.bipinkish.store.dto.UserDto;
import com.bipinkish.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(NewUserRequest newUserRequest);
    void mapDtoToEntity(UpdateUserRequest updateUserRequest, @MappingTarget User user);
    User toEntity(UserDto userDto);
}
