package ro.secur.auth.util;

import org.modelmapper.ModelMapper;
import ro.secur.auth.dto.User;
import ro.secur.auth.entity.UserEntity;

public class UserMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static User mapEntityToDto(UserEntity userEntity) {
        User user = modelMapper.map(userEntity, User.class);
        return user;
    }

    public static UserEntity mapDtoToEntity(User user) {
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        return userEntity;
    }

}
