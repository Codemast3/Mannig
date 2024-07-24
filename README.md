ig":

---

# Mannig App

Mannig is an Android application built using Kotlin. The app provides features for user authentication, profile management, and board creation. It leverages Firebase for user authentication and Firestore for storing user and board data. Firebase Cloud Messaging (FCM) is used for push notifications.

## Features

- User Authentication (Sign Up, Sign In, Sign Out)
- User Profile Management
- Board Creation and Listing
- Firebase Cloud Messaging (FCM) for Push Notifications
- Seamless User Experience with Edge-to-Edge Display Support

## Screenshots

<!-- Include screenshots of your app here -->

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/mannig.git
   ```
2. Open the project in Android Studio.

3. Sync the project with Gradle files.

4. Create a Firebase project in the [Firebase Console](https://console.firebase.google.com/).

5. Add an Android app to your Firebase project and download the `google-services.json` file.

6. Place the `google-services.json` file in the `app` directory of your project.

7. Add Firebase SDK dependencies in your `build.gradle` files:
   
   **Project-level `build.gradle`**:
   ```groovy
   buildscript {
       dependencies {
           classpath 'com.google.gms:google-services:4.3.10'
       }
   }
   ```

   **App-level `build.gradle`**:
   ```groovy
   apply plugin: 'com.android.application'
   apply plugin: 'com.google.gms.google-services'

   dependencies {
       implementation platform('com.google.firebase:firebase-bom:28.4.1')
       implementation 'com.google.firebase:firebase-auth-ktx'
       implementation 'com.google.firebase:firebase-firestore-ktx'
       implementation 'com.google.firebase:firebase-messaging-ktx'
   }
   ```

8. Run the project on an Android device or emulator.

## Usage

### User Authentication

- **Sign Up**: Create a new user account.
- **Sign In**: Sign in with an existing account.
- **Sign Out**: Sign out from the current account.

### Profile Management

- **View Profile**: View and update your profile information.

### Board Management

- **Create Board**: Create new boards.
- **View Boards**: View a list of created boards.

### Push Notifications

- **FCM Integration**: Receive push notifications for updates.

## Project Structure

- `MainActivity.kt`: Main activity of the app.
- `IntroActivity.kt`: Activity for the intro screen with sign-in and sign-up options.
- `MyProfileActivity.kt`: Activity for managing user profiles.
- `CreateBoardActivity.kt`: Activity for creating new boards.
- `FirestoreClass.kt`: Handles Firestore operations.
- `Constants.kt`: Contains constant values used in the app.
- `activity_main.xml`: Layout for the main activity.
- `activity_intro.xml`: Layout for the intro activity.
- `activity_my_profile.xml`: Layout for the profile activity.
- `activity_create_board.xml`: Layout for the create board activity.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request for any improvements or bug fixes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Acknowledgements

- [Firebase](https://firebase.google.com/)
- [Glide](https://github.com/bumptech/glide)

---

F
