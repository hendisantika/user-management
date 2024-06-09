package com.hendisantika.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 22/03/20
 * Time: 10.07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ChangePasswordForm {
    @NotNull
    private Long id;

    @NotBlank(message = "Current Password must not be blank")
    private String currentPassword;

    @NotBlank(message = "New Password must not be blank")
    private String newPassword;

    @NotBlank(message = "Confirm Password must not be blank")
    private String confirmPassword;

    public ChangePasswordForm(Long id) {
        this.id = id;
    }
}
