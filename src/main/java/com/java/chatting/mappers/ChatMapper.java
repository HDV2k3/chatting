package com.java.chatting.mappers;
import com.java.chatting.dto.request.ChatRequest;
import com.java.chatting.dto.response.ChatResponse;
import com.java.chatting.entities.Chat;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface ChatMapper {
    Chat requestToEntity(ChatRequest chatRequest);

    ChatResponse entityToResponse(Chat chat);
}
