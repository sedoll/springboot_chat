package com.websocket.controller;

import com.websocket.dto.RoomForm;
import com.websocket.entity.Chat;
import com.websocket.entity.Room;
import com.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoomController {
    private final ChatService chatService;

    @GetMapping("/chat")
    public String chatHome(Model model) {
        return "redirect:/chat/roomList";
    }

    /**
     * 채팅방 참여하기
     * @param roomId 채팅방 id
     */
    @PreAuthorize("hasAnyRole('USER','TEACHER','ADMIN')")
    @GetMapping("/chat/{roomId}")
    public String joinRoom(@PathVariable(required = false) Long roomId, Principal principal, Model model) {
        List<Chat> chatList = chatService.findAllChatByRoomId(roomId);
        model.addAttribute("logId", principal.getName());
        model.addAttribute("roomId", roomId);
        model.addAttribute("chatList", chatList);
        return "chat/room";
    }

    /**
     * 채팅방 등록
     * @param form
     */
    @PreAuthorize("hasAnyRole('USER','TEACHER','ADMIN')")
    @PostMapping("/chat/room")
    public String createRoom(RoomForm form) {
        chatService.createRoom(form.getName());
        return "redirect:/chat/roomList";
    }

    /**
     * 채팅방 리스트 보기
     */
    @GetMapping("/chat/roomList")
    public String roomList(Model model) {
        List<Room> roomList = chatService.findAllRoom();
        model.addAttribute("roomList", roomList);
        return "chat/roomList";
    }

    /**
     * 방만들기 폼
     */
    @PreAuthorize("hasAnyRole('USER','TEACHER','ADMIN')")
    @GetMapping("/chat/roomForm")
    public String roomForm() {
        return "chat/roomForm";
    }
}
