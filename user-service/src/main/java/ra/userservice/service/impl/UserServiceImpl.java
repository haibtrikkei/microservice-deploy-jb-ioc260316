package ra.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ra.userservice.entity.Users;
import ra.userservice.repository.UserRepository;
import ra.userservice.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RedissonClient redissonClient;

    @Override
    @Cacheable(value = "users")
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public Users findById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new NoSuchElementException("Không tồn tại user "+id));
    }

    @Override
    @Cacheable(value = "users", key = "#user.userId")
    public Users insert(Users user) {
        return userRepository.save(user);
    }

    @Override
    @CachePut(value = "users", key = "#userId")
    public Users update(Long userId, Users user) {
        RLock lock = redissonClient.getLock("lock:users:" + userId);

        try{
            boolean isLocked = lock.tryLock(10000, 30000, TimeUnit.MICROSECONDS);
            if(isLocked){
                userRepository.findById(userId).orElseThrow(()-> new NoSuchElementException("Không tồn tại user "+userId));
                user.setUserId(userId);
                return userRepository.save(user);
            }else{
                System.out.println("Hệ thống đang bận, vui lòng thử lại vào thời gian khác");
            }
        }catch (InterruptedException  e){
            Thread.currentThread().interrupt();
        }finally {
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
        return null;
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public void deleteById(Long id) {
        userRepository.findById(id).orElseThrow(()-> new NoSuchElementException("Không tồn tại user "+id));
        userRepository.deleteById(id);
    }
}
