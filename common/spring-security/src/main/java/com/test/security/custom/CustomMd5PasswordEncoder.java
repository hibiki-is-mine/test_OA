package com.test.security.custom;

import com.test.common.utils.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomMd5PasswordEncoder  implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return MD5Util.getMD5(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(MD5Util.getMD5(charSequence.toString()));
    }
}
