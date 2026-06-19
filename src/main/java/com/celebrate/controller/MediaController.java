package com.celebrate.controller;

import com.celebrate.dto.input.ChatMessageInput;
import com.celebrate.dto.input.FormSubmissionInput;
import com.celebrate.dto.response.ChatMessageApiResponse;
import com.celebrate.dto.response.ChatMessageOutputResponse;
import com.celebrate.dto.response.ChatUserOutputResponse;
import com.celebrate.dto.response.DeleteResultResponse;
import com.celebrate.dto.response.FormSubmissionResponse;
import com.celebrate.dto.response.LiveActivityResponse;
import com.celebrate.dto.response.UploadResultResponse;
import com.celebrate.service.SubscriptionPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class MediaController {

    private final SubscriptionPublisher subscriptionPublisher;

    @MutationMapping
    public UploadResultResponse uploadImageToS3(@Argument String image) {
        UploadResultResponse response = new UploadResultResponse();
        response.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRLeNSg0Lkbw8fX8ifI_IYTCf73bTCG0QCQcA&s");
        return response;
    }

    @MutationMapping
    public DeleteResultResponse deleteImageFromS3(@Argument String imageUrl) {
        DeleteResultResponse response = new DeleteResultResponse();
        response.setSuccess(true);
        response.setMessage("Image deleted successfully.");
        return response;
    }

    @MutationMapping
    public LiveActivityResponse addOrUpdateLiveActivityToken(
            @Argument String orderId,
            @Argument String activityId,
            @Argument String token) {
        LiveActivityResponse response = new LiveActivityResponse();
        response.setSuccess(true);
        response.setMessage("Live activity token updated.");
        return response;
    }

    @MutationMapping
    public FormSubmissionResponse sendFormSubmission(@Argument FormSubmissionInput formSubmissionInput) {
        FormSubmissionResponse response = new FormSubmissionResponse();
        response.setMessage("Form submitted successfully.");
        response.setStatus("success");
        return response;
    }

    @MutationMapping
    public ChatMessageApiResponse sendChatMessage(
            @Argument ChatMessageInput message,
            @Argument String orderId) {
        ChatUserOutputResponse userOutput = new ChatUserOutputResponse();
        userOutput.setId(message.getUser() != null ? message.getUser().getId() : "");
        userOutput.setName(message.getUser() != null ? message.getUser().getName() : "");

        ChatMessageOutputResponse data = new ChatMessageOutputResponse();
        data.setId(orderId + "-" + Instant.now().toEpochMilli());
        data.setMessage(message.getMessage());
        data.setUser(userOutput);
        data.setCreatedAt(Instant.now().toString());
        data.setOrderId(orderId);

        subscriptionPublisher.publishChatMessage(data);

        return ChatMessageApiResponse.builder()
                .success(true)
                .message("Message sent.")
                .data(data)
                .build();
    }
}
