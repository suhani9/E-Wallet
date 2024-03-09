package org.wallet.user.dto;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private long userId;
    @NonNull
    private String userName;
    @NonNull
    private String email;
    @NonNull
    private String kycNumber;

}
