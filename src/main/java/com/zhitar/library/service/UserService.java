package com.zhitar.library.service;

import com.zhitar.library.domain.User;

public interface UserService {

    User findByEmail(String email);

    User save(User user);
}
