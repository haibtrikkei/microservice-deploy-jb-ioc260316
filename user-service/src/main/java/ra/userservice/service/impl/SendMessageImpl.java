package ra.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import ra.userservice.service.SendMessage;

@Service
@RequiredArgsConstructor
public class SendMessageImpl implements SendMessage {
    private final StringRedisTemplate  stringRedisTemplate;
    @Override
    public void sendMessage(String message) {
        stringRedisTemplate.convertAndSend("chanel-name", message);
    }
}
