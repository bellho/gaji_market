package kh.spring.gaji.chat.model.service;

import java.util.List;
import java.util.Map;

import kh.spring.gaji.chat.model.dto.ChatMessageDto;
import kh.spring.gaji.chat.model.dto.ChatRoomDto;

public interface ChatService {
	
	List<ChatRoomDto> getChatRoom(String userId);
	List<ChatMessageDto> getChatInfo(int chatNo);
	List<ChatMessageDto> getChatMessage(int chatId);
	int insertChatMessage(Map<String, Object> map);
}