# SlideMind

SlideMind is a Java-based flashcard generator that helps students convert study material into structured flashcards for easier learning and revision.

The application integrates with the Claude API to generate flashcards automatically and provides a simple interface for navigating, reviewing, and storing flashcards locally.

## Features

- AI-assisted flashcard generation using the Claude API
- Modular Java architecture for maintainability
- Interactive flashcard interface for studying
- Persistent storage of generated flashcards
- Ability to save and revisit previous quizzes
- Lightweight desktop-style workflow

## Tech Stack

- Java
- Claude API
- JSON processing
- File I/O for local storage
- OkHttp for API requests

Libraries used in the project:

- OkHttp
- JSON
- Apache Commons IO
- Apache PDFBox
- Kotlin Standard Library (dependency)

## Project Structure

```bash
SlideMind
├── src
│   ├── SlideMind.java
│   ├── FlashcardPage.java
│   ├── HelloPage.java
│   ├── LoadingPage.java
│   ├── CompletedPage.java
│   ├── Question.java
│   ├── QuizDataLoader.java
│   ├── SavedFlashcardsPage.java
│   ├── SavedQuizInfo.java
│   └── StorageManager.java
│
├── libs
│   ├── okhttp
│   ├── json
│   ├── commons-io
│   ├── pdfbox
│   └── kotlin-stdlib
│
└── bin
    └── compiled classes
```



## How It Works

1. The user inputs learning content.
2. The application sends the content to the Claude API.
3. Claude generates structured flashcards.
4. Flashcards are displayed in the application interface.
5. Generated flashcards can be saved locally for later review.

## Example Workflow

1. Start the application.
2. Enter learning material.
3. Flashcards are generated automatically.
4. Review flashcards interactively.
5. Save them for later revision.

## Future Improvements

- Graphical user interface improvements
- Export flashcards to Anki or other learning tools
- Web-based version of the application
- Support for multiple AI models
- Flashcard sharing between users

## Authors

Ammar Yaser Babaset  
Ahmed Omer Bahaj
Software Engineering Students – University of Jeddah
