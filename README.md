# Interview Preparation Bot 🤖

[![Kotlin Version](https://img.shields.io/badge/Kotlin-1.9.23-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6.7-brightgreen.svg)](https://developer.android.com/jetpack/compose)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A sophisticated, AI-powered native Android application designed to be a personal interview coach for job seekers. Built entirely in Kotlin and Jetpack Compose, this app leverages the Groq API to conduct realistic mock interviews, provide instant feedback, and generate comprehensive performance reports.

---

## ✨ Demo & Screenshots

*(Consider adding a short GIF of your app in action here for an even better demo!)*

| Home Screen | Setup Screen | Chat Screen |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/74014923-ffdb-402d-942d-396addbdece0" alt="Home Screen" width="250"/> | <img src="https://github.com/user-attachments/assets/dcf71407-56c2-4b5a-958e-588c34fca8ac" alt="Setup Screen" width="250"/> | <img src="https://github.com/user-attachments/assets/7a420e8b-d7dd-41be-a5af-3bf3ea880831" alt="Chat Screen" width="250"/> |

| Summary Screen | Voice Input | Typing Indicator |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/3eaaae27-21db-4aba-8404-01def2f7993b" alt="Summary Screen" width="250"/> | <img src="https://github.com/user-attachments/assets/1c27537e-0314-445f-8e77-9ce74bc885d9" alt="Voice Input" width="250"/> | <img src="https://github.com/user-attachments/assets/ba82abbe-4e82-4423-ab43-263b65104f88" alt="Typing Indicator" width="250"/> |


---

## 🚀 Features

### Core Features
- **Dynamic Interview Customization:** Tailor interviews by job role (Software Engineer, PM, etc.), mode (Technical, Behavioral), and specific domains.
- **AI-Powered Conversation:** Engage in a real-time chat with an AI that asks relevant questions and provides instant, constructive feedback on your answers.
- **Persistent Session History:** All completed interviews are saved locally using Room, allowing you to track your progress over time.
- **Comprehensive Performance Reports:** Receive a detailed summary after each interview, including a final score, a list of strengths, areas for improvement, and suggested study resources.

### Advanced "Wow" Features
- **🎤 Real-Time Voice-to-Text:** A seamless voice input system that transcribes your spoken answers directly into the text field.
- **🧠 Intelligent "Bot-is-Typing" Indicator:** A clean, animated indicator appears while the AI is processing, and all input is disabled to prevent errors and improve UX.
- **⏩ Contextual "Skip Question" Feature:** A "Skip" button appears in the top bar, allowing you to request a new question without disrupting the interview flow.

---

## 🛠️ Tech Stack & Architecture

This project was built using a modern, scalable, and maintainable tech stack, following industry best practices.

- **Language:** 100% [Kotlin](https://kotlinlang.org/)
- **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for a fully declarative and reactive UI.
- **Architecture:** MVVM (Model-View-ViewModel) for a clean separation of concerns.
- **Asynchronous Operations:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html) for managing all background tasks.
- **API Communication:** [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/) for type-safe and efficient network requests.
- **Local Database:** [Room](https://developer.android.com/training/data-storage/room) for robust, on-device data persistence.
- **Data Serialization:** [Gson](https://github.com/google/gson) for converting data objects to and from JSON.
- **Navigation:** [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation) to manage screen transitions.

---

## 🤖 AI Integration

The app's intelligence is powered by a flexible and robust integration with a leading Large Language Model.

- **API Provider:** [Groq API](https://groq.com/) - Chosen for its high speed and generous free tier, allowing for a smooth, real-time conversational experience.
- **LLM Model:** **`mixtral-8x7b-32768`** - A powerful and fast open-source model served via the Groq platform.
- **Prompt Engineering:** Highly specific system prompts were crafted to guide the AI's behavior, ensuring it asks relevant questions, provides constructive feedback, and generates summaries in a valid, parsable JSON format.

---

## ⚙️ Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

- Android Studio (latest stable version)
- JDK 17 or higher

### Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone [https://github.com/your-username/your-repository-name.git](https://github.com/your-username/your-repository-name.git)
    ```
2.  **Open the project in Android Studio.**

3.  **Add Your Groq API Key:**
    - In the root directory of the project, create a new file named `local.properties`.
    - Inside `local.properties`, add the following line, replacing `"your_api_key_here"` with your actual API key from the Groq Console:
      ```properties
      GROQ_API_KEY="your_api_key_here"
      ```
    - The project's `.gitignore` file is already configured to keep this file private, so you won't accidentally commit your secret key.

4.  **Sync Gradle and Run:**
    - Let Android Studio sync the project's Gradle files.
    - Build and run the app on an emulator or a physical device.

---

## 📄 License

This project is distributed under the MIT License. See `LICENSE` for more information.
