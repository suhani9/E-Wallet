package org.wallet.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.wallet.common.UserCreatedPayload;
import org.wallet.user.dao.IuserRepo;
import org.wallet.user.dto.UserDto;
import org.wallet.user.entity.User;


@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);
    @Value("${kafka.usercreation.topic}")
    private String topic;
    @Autowired
    private KafkaTemplate<String, Object> objectKafkaTemplate;
    @Autowired
    private IuserRepo userRepo;
    public long createUserRegistratiion(UserDto userDto){
        User createdUser = userRepo.save(mapUserDtoToEntity(userDto));
        UserCreatedPayload createdPayLoad = new UserCreatedPayload(createdUser.getUserId(),createdUser.getUserName(),createdUser.getEmail());
        objectKafkaTemplate.send(topic, String.valueOf(createdUser.getUserId()),createdPayLoad);
        logger.info("pushed to kafka in topic {} : payload {}",topic,createdPayLoad);
        return createdUser.getUserId();

    }
    private User mapUserDtoToEntity(UserDto userDto){
        User newUser = new User();
        newUser.setUserName(userDto.getUserName());
        newUser.setEmail(userDto.getEmail());
        newUser.setKycNumber(userDto.getKycNumber());
        return newUser;
    }
}
