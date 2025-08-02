package ksm.haein.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate redisTemplate;

    public boolean compareVerificationCode(String email, String code) {
        if (redisTemplate.hasKey(email)) {
            ValueOperations<String, String> ops = redisTemplate.opsForValue();

            String value = ops.get(email);
            if (code.equals(value)) {
                redisTemplate.delete(email);
                return true;
            }
        }

        return false;
    }
}
