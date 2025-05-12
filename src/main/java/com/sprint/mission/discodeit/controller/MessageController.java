//package com.sprint.mission.discodeit.controller;
//
//import com.sprint.mission.discodeit.dto.Response.MessageResponse;
//import com.sprint.mission.discodeit.dto.request.create.BinaryContentCreateRequest;
//import com.sprint.mission.discodeit.dto.request.create.MessageCreateRequest;
//import com.sprint.mission.discodeit.dto.request.update.MessageUpdateRequest;
//import com.sprint.mission.discodeit.service.MessageService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/message")
//public class MessageController {
//
//    private final MessageService messageService;
//
//    @RequestMapping(
//            path = "/create"
////            , method = RequestMethod.POST
//            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
//    )
//    @ResponseBody
//    public ResponseEntity<MessageResponse> create(
//            @RequestParam("messageCreateRequest") MessageCreateRequest messageCreateRequest,
//            @RequestPart(value = "attachment", required = false) MultipartFile attachment
//    ) {
//
//        Optional<UUID> attachmentRequest =
//                Optional.ofNullable(attachment)
//                        .flatMap(this::resolveAttachmentRequest);
//
//        MessageResponse createdMessage = messageService.create(messageCreateRequest, attachmentRequest);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
//    }
//
//    @RequestMapping(
//            value = "/{messageId}",
//            method = RequestMethod.GET
//    )
//    @ResponseBody
//    public ResponseEntity<MessageResponse> find(
//            @PathVariable("messageId") UUID messageId
//    ) {
//        MessageResponse message = messageService.find(messageId);
//        return ResponseEntity.ok(message);
//    }
//
//    @RequestMapping(
//            method = RequestMethod.GET
//    )
//    @ResponseBody
//    public ResponseEntity<List<MessageResponse>> findAllByChannelId(
//            @RequestParam("channelId") UUID channelId
//    ) {
//        List<MessageResponse> messages = messageService.findAllByChannelId(channelId);
//        return ResponseEntity.ok(messages);
//    }
//
//    @RequestMapping(
//            value = "/update",
//            method = RequestMethod.PUT
//    )
//    @ResponseBody
//    public ResponseEntity<MessageResponse> update(
//            @RequestBody MessageUpdateRequest messageUpdateRequest
//    ) {
//        MessageResponse updatedMessage = messageService.update(messageUpdateRequest);
//        return ResponseEntity.ok(updatedMessage);
//    }
//
//    @RequestMapping(
//            value = "/delete",
//            method = RequestMethod.DELETE
//    )
//    @ResponseBody
//    public ResponseEntity<String> delete(
//            @RequestParam("messageId") UUID messageId
//    ) {
//        boolean deleted = messageService.delete(messageId);
//        if(deleted) {
//            return ResponseEntity.ok("유저 삭제 성공");
//        } else {
//            return ResponseEntity.badRequest().body("삭제 실패!");
//        }
//    }
//
//    private Optional<BinaryContentCreateRequest> resolveAttachmentRequest(MultipartFile attachment) {
//
//        if(attachment.isEmpty()) {
//            //컨트롤러가 요청받은 파라미터 중 MultipartFile 타입의 데이터가 비어있다면:
//            return Optional.empty();
//        } else {
//            //컨트롤러가 요청받은 파라미터 중 MultipartFile 타입의 데이터가 존재한다면:
//            try{
//                BinaryContentCreateRequest binaryContentRequest = new BinaryContentCreateRequest(
//                        attachment.getBytes(),
//                        attachment.getContentType(),
//                        attachment.getOriginalFilename()
//                );
//                return Optional.of(binaryContentRequest);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//}
//
