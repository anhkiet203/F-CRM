package com.f_crm.entity;

import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePass {
    

   @NotEmpty(message = "Vui lòng nhập mật khẩu hiện tại!")
	    private String oldPassword;

	    @NotEmpty(message = "Vui lòng nhập mật khẩu mới!")
	    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
	    private String newPassword;

	    @NotEmpty(message = "Vui lòng xác nhận mật khẩu!")
	    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
	    @Transient
	    private String confirmPassword;

}
