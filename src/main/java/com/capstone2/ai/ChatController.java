package com.capstone2.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiImageClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    // application.yml 설정에 맞게 자동으로 생성됩니다
    private final OpenAiChatClient chatClient;
    private final OpenAiImageClient imageClient;
    
    // 텍스트 생성
    @GetMapping("/ai/chat")
    public ChatResponse chat(String message) {
        
        // 프롬프트 셋팅
        List<Message> prompts = new ArrayList<>();
        Message prompt1 = new SystemMessage("""
        너는 간단한 프로젝트 설명을 입력받으면 그 프로젝트에 필요한 기능명세서를 작성해야 돼.
        예를 들어서 입력으로 '간단한 게시판 프로젝트' 라고 입력받으면 게시판 프로젝트에 필요한 기능들을 기능별로 작성하는거야
        
        유저 기능 : 유저 생성, 유저 삭제, 유저 수정, 유저 조회
        게시판 기능 : 게시글 작성, 게시글 조회, 게시글 삭제, 게시글 스크랩, 게시글 좋아요, 게시글 수정
        
        이건 예시고 너가 알려줄 땐 각 기능에 설명을 주가해서 더 자세하게 알려줘
        답변은 json형식으로 해주고, 기능별로 파싱할 수 있도록 나눠서 답변해줘야해
        
        이런 형식으로 파싱할거야 유저기능은 data[0].content, 게시판 기능은 data[0].content 이런 식으로 기능별로 접근 가능하도록
        
        이 결과를 바탕으로 실제로 설계를 진행할 수 있도록 최대한 자세하고 세밀하게 알려줘
        """);
        Message userMessage = new SystemMessage(message);
        
        // 프롬프트 추가
        prompts.add(prompt1);
        prompts.add(userMessage);

        // 생성
        Prompt prompt = new Prompt(prompts);
        return chatClient.call(prompt);
    }
    
    // 이미지 생성
    @GetMapping("ai/image")
    public ImageGeneration image(String message) {

//        List<ImageMessage> prompts = new ArrayList<>();
//        ImageMessage prompt1 = new ImageMessage(message + """
//        그림을 그리라는게 아니라 UML 형식처럼 깔끔하게 ERD 에서 속성을 제외하고 관계만 파악할 수 있을 수준으로
//        깔끔하고 정확하게 그려줘
//        이 기능을 바탕으로 코딩할 수 있을 수준으로 설계를 완성해줘
//        """);

//        prompts.add(prompt1);

        ImagePrompt prompt = new ImagePrompt(message);
        return imageClient.call(prompt).getResult();
    }

}
