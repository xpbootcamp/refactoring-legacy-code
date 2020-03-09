package cn.xpbootcamp.legacy_code.repository;

import cn.xpbootcamp.legacy_code.entity.User;

public interface UserRepository {
    User find(long id);
}
