package com.order.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.bean.FeedbackBean;
import com.order.exceptions.FeedbackNotFoundException;
import com.order.service.FeedbackService;

@RestController
@RequestMapping("/feedback")
@CrossOrigin("*")
public class FeedbackController {
    
    @Autowired
    private FeedbackService feedbackService;
    
    private static Logger log = LoggerFactory.getLogger(FeedbackController.class.getSimpleName());
    
    /**
     * Saves a feedback.
     * 
     * @param feedback The feedback to be saved.
     * @return ResponseEntity<FeedbackBean> A ResponseEntity containing the saved FeedbackBean object and HTTP status code.
     *         If the feedback is successfully saved, returns HttpStatus.CREATED (201). If an IllegalArgumentException occurs
     *         during the save operation, returns HttpStatus.BAD_REQUEST (400).
     */
    @PostMapping("/save")
    public ResponseEntity<FeedbackBean> saveFeedback(@RequestBody FeedbackBean feedback) {
        log.info("FeedbackController::saveFeedback::Started ", feedback);
        try {
            feedback = feedbackService.saveFeedback(feedback);
            log.info("FeedbackController::saveFeedback::Ended");
            return new ResponseEntity<FeedbackBean>(feedback, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("FeedbackController::saveFeedback::" + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves all feedbacks.
     * 
     * @return ResponseEntity<List<FeedbackBean>> A ResponseEntity containing a list of all retrieved FeedbackBean objects and HTTP status code.
     *         If feedbacks are successfully retrieved, returns HttpStatus.OK (200). If no feedbacks are found, returns HttpStatus.NOT_FOUND (404).
     */
    @GetMapping("/get/all")
    public ResponseEntity<List<FeedbackBean>> getAllFeedbacks() {
        log.info("FeedbackController::getAllFeedbacks::Started");
        List<FeedbackBean> feedbacks;
        try {
            feedbacks = feedbackService.getAllFeedbacks();
            log.info("FeedbackController::getAllFeedbacks::Ended");
            return new ResponseEntity<>(feedbacks, HttpStatus.OK);
        } catch (FeedbackNotFoundException e) {
            log.error("FeedbackController::getAllFeedbacks::" + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
