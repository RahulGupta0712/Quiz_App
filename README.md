# Quiz Application

This Quiz Application is a dynamic platform designed to test users' knowledge across diverse topics like world events, history, entertainment, and more. It provides a competitive and engaging quiz experience where users can track their performance on a leaderboard and improve their scores over time. This README provides a detailed overview of the application's features, user flow, and technical specifications.

## Features

### User Registration & Authentication
- **Sign-Up**: Users register with an email, password, and display name.
- **Email Verification**: Only users with verified emails can log in, preventing unauthorized or spam accounts.
- **Google Sign-In**: Users have the option to log in via Google for convenience; after Google sign-in, users are prompted to enter their display name.
- **Persistent Login**: After logging in once, users stay signed in, even after closing the app, for easy re-access.
- **Password Reset**: Users can reset their password via email if forgotten.

### Quiz Mechanics
- **Question Selection**: Each quiz consists of 10 randomly selected questions from a large database of over 500 questions stored in Firebase Realtime Database.
- **Multiple Choice**: Each question provides four answer options, with one correct answer.
- **Timed Responses**: Users have 30 seconds to answer each question; unanswered questions are skipped automatically.
- **Answer Feedback**: After each question, feedback is provided:
  - Correct answers are highlighted in green.
  - If the chosen answer is incorrect, it appears in red.
- **Quiz Exit Option**: Users can exit the quiz at any time, allowing flexibility if they need to pause or stop mid-quiz.

### Leaderboard
- **Score Tracking**: Each user’s highest score is saved and used to rank them on the leaderboard.
- **Highlighting**: The current user’s entry is highlighted in a unique color, allowing easy identification among users with similar names.
- **RecyclerView Integration**: The leaderboard is displayed in a scrollable format powered by RecyclerView, making it easy to navigate and view rankings.
- **Fragments**: The app uses fragments for a streamlined user experience across different sections, including the Home and Leaderboard screens.

### User Interface
- **Progress Bar**: Shows the remaining number of questions, giving the user a visual indicator of their quiz progress.
- **Responsive Design**: The application adapts to different screen sizes and orientations, providing an optimal user experience.

## User Flow

1. **Registration**: Users sign up with email and password, or use Google Sign-In and enter a display name.
2. **Email Verification**: After registering, users must verify their email before they can log in.
3. **Login**: Users log in with either email and password or Google. After initial login, they remain signed in.
4. **Quiz Start**: Users can begin a quiz, answering each question within 30 seconds or choosing to exit anytime.
5. **Answer Feedback**: After each question, feedback is provided using colored indicators for correct or incorrect answers.
6. **Quiz Completion**: At the end of the 10 questions, or upon exit, the user’s score is displayed.
7. **Leaderboard Access**: Users can view the leaderboard, where scores are ranked by highest score.
8. **Logout**: Users can sign out of the app whenever they wish.

## Leaderboard

The leaderboard ranks users based on their highest quiz scores, updating in real-time. The leaderboard is organized in descending order of score, with the current user’s entry highlighted in a unique color.

## Technical Specifications

### Architecture
- **Backend**: Firebase Realtime Database for managing user data, questions, scores, and leaderboard information in real-time.
- **Frontend**: Android-based user interface with fragments for different screens (Home, Leaderboard).
- **RecyclerView**: Efficiently displays the leaderboard in a scrollable format for quick access to user rankings.

### Database Structure

- **Users**: Contains user data, including email, display name, and highest score.
- **Quiz Questions**: Stores a large collection of questions with corresponding options and answers.
- **Leaderboard**: Dynamically displays user scores in descending order, using each user’s highest score.

### Dependencies

- **Firebase Authentication**: Manages user authentication, including Google sign-in and email verification.
- **Firebase Realtime Database**: Real-time updates for storing and retrieving questions, scores, and user data.
- **RecyclerView**: Displays leaderboard entries in a scrollable list for smooth and responsive navigation.
- **Fragments**: Organizes screens into sections, providing an intuitive and multi-screen experience with Home and Leaderboard views.

## Video Demonstration

Watch the [video demonstration of the Quiz App](https://youtu.be/s2H_NGBuh5U) for a detailed guide on its functionality and features.

---

This documentation provides a comprehensive overview of the Quiz Application for developers and users. Contributions and suggestions are welcome to improve the app’s performance and expand its features.
