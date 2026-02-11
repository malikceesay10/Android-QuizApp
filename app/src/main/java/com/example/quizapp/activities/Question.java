package com.example.quizapp.activities;

public class Question {

    // Deklaration der Variablen, die exakt so heißen müssen wie die Felder in der Firestore-Datenbank.
    // Das ist die Voraussetzung, damit Firebase die Daten automatisch zuordnen kann.
    public String questionText, optionA, optionB, optionC, optionD, correctAnswer, category;

    // Firebase benötigt zwingend einen leeren Konstruktor ohne Parameter.
    // Nur so kann die Methode .toObject(Question.class) im Hintergrund ein neues Objekt
    // erstellen und es anschließend mit den Daten vom Server befüllen.
    public Question() {}

    // Da unsere Variablen "private" sind, brauchen wir diese Methoden,
    // um in der QuizActivity oder LearningActivity auf die Texte zuzugreifen.

    public String getQuestionText() { return questionText; }

    public String getOptionA() { return optionA; }

    public String getOptionB() { return optionB; }

    public String getOptionC() { return optionC; }

    public String getOptionD() { return optionD; }

    public String getCorrectAnswer() { return correctAnswer; }

    public String getCategory() { return category; }
}