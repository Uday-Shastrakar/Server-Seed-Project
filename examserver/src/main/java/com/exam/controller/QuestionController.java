package com.exam.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.exam.model.exam.Question;
import com.exam.model.exam.Quiz;
import com.exam.service.QuestionService;
import com.exam.service.QuizService;
@RestController
@CrossOrigin("*")
@RequestMapping("/question")
public class QuestionController {
    @Autowired
	private QuestionService service;
    
    @Autowired
    private QuizService quizService;
    
    //add qustions
    @PostMapping("/")
    public ResponseEntity<Question> add(@RequestBody Question question){
    	return ResponseEntity.ok(this.service.addQuestion(question));
    }
    //update the question
    @PutMapping("/")
    public ResponseEntity<Question> update(@RequestBody Question question){
    	return ResponseEntity.ok(this.service.updateQuestion(question));
    }
    
    //get all question of any quiz(with shuffle)
    @GetMapping("/quiz/{qid}")
    public ResponseEntity<?> getQuestionsOfQuiz(@PathVariable("qid")Long qid){
//    	Quiz quiz=new Quiz();
//    	quiz.setQid(qid);
//    	Set<Question> questionOfQuiz= this.service.getQuestionOfQuiz(quiz);
//    	return ResponseEntity.ok(questionOfQuiz);
    	
         Quiz quiz =this.quizService.getQuiz(qid);
         Set<Question> questions = quiz.getQuestions();
         List list =new ArrayList(questions);
         if(list.size()>Integer.parseInt(quiz.getNumberOfQuestions())) {
        	 list=list.subList(0,Integer.parseInt(quiz.getNumberOfQuestions()+1));
        	}
         
         list.forEach((q)->{
        	((Question) q).setAnswer("");
         });
         Collections.shuffle(list);
         return ResponseEntity.ok(list);
    	
    }
    @GetMapping("/quiz/all/{qid}")
    public ResponseEntity<?> getQuestionsOfQuizAdmin(@PathVariable("qid")Long qid){
       	Quiz quiz=new Quiz();
     	quiz.setQid(qid);
    	Set<Question> questionOfQuiz= this.service.getQuestionOfQuiz(quiz);
    	return ResponseEntity.ok(questionOfQuiz);
    	
      
    //     return ResponseEntity.ok(list);
    	
    }
     
    
    //get single question
    @GetMapping("/{quesId}")
    public  Question get(@PathVariable("quesId")Long quesId) {
    	return this.service.getQuestion(quesId);
    }
   
    //delete question
    @DeleteMapping("/{quesId}")
    public void delete(@PathVariable("quesId")Long quesId) {
    	this.service.deleteQuestion(quesId);
    }
    
    //eval question
    @PostMapping("/eval-quiz")
    public ResponseEntity<?> evalQuiz(@RequestBody List<Question> questions){
    	System.out.println(questions);
    	
    	double marksGot=0;
    	int correctAnswers=0;
    	int attempted =0;
    	for(Question q:questions){
    		//single questions
    	Question question=	this.service.get(q.getQuesId());
    	      if(question.getAnswer().equals(q.getGivenAnswer())) {
    	    	  //correct
    	    	  correctAnswers++;
    	     double marksSingle = Double.parseDouble(questions.get(0).getQuiz().getMaxMarks())/questions.size();         
    	    			//  this.questions[0].quiz.maxMarks/this.questions.length;
     	         marksGot += marksSingle;
    	      }
    	      if(q.getGivenAnswer()!=null){
    	        attempted++;
    	      }
    	}

        Map<String, Object> map = new HashMap<>();
        map.put("marksGot", marksGot);
        map.put("correctAnswers", correctAnswers);
        map.put("attempted", attempted);

    	return  ResponseEntity.ok(map);
    }
    
    
    
   
    
 }
