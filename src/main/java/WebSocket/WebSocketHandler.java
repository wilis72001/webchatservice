package WebSocket;


import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String, Set<WebSocketSession>> userSessions = new ConcurrentHashMap<>();
    private final List<WebSocketSession> sessions = new ArrayList<>();

    private final String USERNAME_ATTRIBUTE = "username"; // 定义保存用户名的属性名

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = (String) session.getAttributes().get(USERNAME_ATTRIBUTE); // 获取实际的用户名
        if(username != null){
            userSessions.computeIfAbsent(username, key -> new HashSet<>()).add(session);
            sessions.add(session);
        } else {
            // 如果用户名为空，可以进行一些处理，例如关闭连接或发送错误消息
            session.close();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // 处理收到的消息
        String payload = message.getPayload();
        // 可以将收到的消息广播给所有在线用户
        broadcast(payload);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        userSessions.values().forEach(sessions -> sessions.remove(session));
        sessions.remove(session);
    }

    private void broadcast(String message) throws IOException {
        for (WebSocketSession session : sessions) {
            session.sendMessage(new TextMessage(message));
        }
    }
}
