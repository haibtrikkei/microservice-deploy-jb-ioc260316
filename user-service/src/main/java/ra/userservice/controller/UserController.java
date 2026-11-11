package ra.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.userservice.dto.DataSend;
import ra.userservice.service.SendMessage;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final SendMessage sendMessage;

    @GetMapping
    public ResponseEntity<List<String>> getUsers() {
        List<String> users = List.of("User 1", "User 2", "User 3");
        return ResponseEntity.ok(users);
    }

    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(@RequestBody DataSend dataSend) {
        sendMessage.sendMessage(dataSend.getName()+": "+dataSend.getMessage());
        return ResponseEntity.ok("Đã gửi tin nhắn");
    }
}
