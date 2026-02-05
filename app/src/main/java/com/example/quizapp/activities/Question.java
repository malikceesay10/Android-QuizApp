package com.example.quizapp.activities;

public class Question {
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String category;

    // Der leere Konstruktor ist zwingend für Firebase!
    public Question() {}

    // Getter-Methoden, damit die QuizActivity die Texte abrufen kann
    public String getQuestionText() { return questionText; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getCategory() { return category; }
}