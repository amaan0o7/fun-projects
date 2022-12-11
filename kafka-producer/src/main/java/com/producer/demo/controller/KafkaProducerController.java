package com.producer.demo.controller;

import com.producer.demo.dto.Books;
import com.producer.demo.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class KafkaProducerController {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaProducerController.class);

    @Autowired
    KafkaTemplate<String, Books> kafkaTemplate;
    private static  final String TOPIC = "mfs_collective_topic";

    @PostMapping("/generateEvent")
    public Response sendEvents(@RequestBody Books books){
        LOG.info("inside send event method : {}",books);
        List<Books> requestBookList = books.getBookList();
        List<Books> failedToProcess = new ArrayList<>();
        Response response = new Response();
        try{
            requestBookList.forEach(currBook -> {
                ListenableFuture<SendResult<String, Books>> future = kafkaTemplate.send(TOPIC,currBook);
                future.addCallback(new ListenableFutureCallback<SendResult<String, Books>>() {
                    @Override
                    public void onFailure(Throwable ex) {
                        LOG.error("Exception occurred while publishing interaction event object ::: {} ", ex);
                        failedToProcess.add(currBook);
                    }

                    @Override
                    public void onSuccess(SendResult<String, Books> result) {
                        RecordMetadata recordMetadata = result.getRecordMetadata();
                        LOG.info("Callback - sent to: {} " + recordMetadata.topic() + "- {}" + recordMetadata.partition() + "- {}" + recordMetadata.offset());
                    }
                });
            });
        } catch (Exception e) {
            LOG.error("Exception occurred while publishing interaction event object ::: {} ", e);
            e.printStackTrace();
        }
        if(failedToProcess.size()>0){
            response.setMessage("Failed to Process!");
            failedToProcess.get(0).setSize(failedToProcess.size());
            response.setResult(failedToProcess);
            response.setStatus(HttpStatus.EXPECTATION_FAILED);
        }
        response.setMessage("Event send Successfully!");
        response.setResult(books.getBookList());
        response.setStatus(HttpStatus.ACCEPTED);
        return response;
    }
}
