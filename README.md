# DailyHisaab — Roz Ka Hisaab, Ek Jagah
<img width="2752" height="1536" alt="DailyHisaab Logo" src="https://github.com/user-attachments/assets/507bfc06-1db7-4d5a-a2bd-75de37d5f08b" />

**Track your expenses. Understand your spending. Manage your money smarter.**

DailyHisaab is an Android expense-tracking application designed to help users record daily expenses, organize transactions into categories, and monitor their spending through a simple, intuitive interface.

Built with native Android technologies, DailyHisaab focuses on usability, performance, local data storage, and offline access.

---

## About the Project

DailyHisaab is designed for individuals who want a straightforward way to manage their daily spending without relying on complicated financial management tools.

The application enables users to add, view, edit, and delete expense records, categorize transactions, and review spending summaries.

The project is developed using Java and XML in Android Studio, with SQLite used for local data persistence.

### Project Goals

- Make daily expense tracking simple and accessible.
- Provide an organized history of financial transactions.
- Help users understand spending patterns.
- Store expense records locally on the device.
- Support offline usage without requiring a continuous internet connection.
- Deliver a clean and responsive Android user experience.

---

## Features

### 1. Expense Management

- Add new expense records.
- Edit existing expenses.
- Delete expense records.
- Record the expense title, amount, category, date, and optional note.
- Validate user input before saving records.

### 2. Expense Categories

Organize transactions using predefined categories:

- Food
- Travel
- Shopping
- Bills
- Education
- Entertainment
- Health
- Fuel
- Other

### 3. Dashboard

View a summary of recorded expenses from the dashboard.

Depending on the implemented version, the dashboard may display total expenses, recent transactions, and category-wise summaries.

### 4. Transaction History

- View previously recorded expenses.
- Review expense details.
- Access editing and deletion options.
- Browse stored transactions.

### 5. Statistics and Spending Insights

Review expense summaries and visualizations to understand spending patterns, where supported by the current implementation.

### 6. Local Database

DailyHisaab uses SQLite for storing expense records locally on the device.

The database supports persistent storage of transaction details between application sessions.

### 7. Offline-First Experience

Core expense-tracking operations are designed to work offline. Internet access is not required for ordinary local expense recording when no optional online feature is used.

---

## Technology Stack

| Technology | Purpose |
|---|---|
| Java | Android application logic |
| XML | UI layouts and resources |
| Android Studio | Development environment |
| SQLite | Local expense data storage |
| SQLiteOpenHelper | Database creation and management, if used |
| RecyclerView | Transaction lists, if implemented |
| Material Components | UI components, if included in the project |

The exact libraries and Android SDK versions depend on the project's Gradle configuration.

---

## Application Screens

The intended application structure includes:

1. **Dashboard** — Expense overview and recent activity.
2. **Add/Edit Expense** — Create or update expense records.
3. **Transaction History** — Browse saved transactions.
4. **Statistics** — Review expense summaries.
5. **Categories** — Organize expenses.
6. **Settings/About** — Application information and preferences, where implemented.

Refer to the application source code for the screens and features available in the current build.

---

## Project Structure

A typical Android Studio project may use the following structure:

```text
DailyHisaab/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── .../
│   │       ├── res/
│   │       │   ├── drawable/
│   │       │   ├── layout/
│   │       │   ├── mipmap/
│   │       │   └── values/
│   │       └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── gradle/
├── build.gradle
├── settings.gradle
├── gradle.properties
├── .gitignore
└── README.md
```

**Note:** This is a representative structure. Your actual package names, resource folders, and Gradle filenames may differ.

---

## Getting Started

### Prerequisites

Install the following tools:

- Android Studio.
- A compatible Java Development Kit (JDK), as required by the project's Gradle configuration.
- Android SDK components required by the project.
- Git, if you want to clone the repository from the terminal.

### Clone the Repository

Replace `YOUR_USERNAME` with the GitHub account that owns the repository.

```bash
git clone https://github.com/YOUR_USERNAME/DailyHisaab.git
```

### Open the Project

1. Launch Android Studio.
2. Select **Open**.
3. Choose the cloned `DailyHisaab` folder.
4. Allow Gradle synchronization to finish.
5. Install the required Android SDK components if prompted.
6. Connect an Android device or start an emulator.
7. Click **Run** to build and launch the application.

### Build an APK

In Android Studio, select:

**Build → Build Bundle(s) / APK(s) → Build APK(s)**

After a successful build, use the notification's **Locate** option to find the generated APK.

The debug APK is intended for development and testing. Prepare an appropriately signed release build before distributing a production version.

---

## Data Storage and Privacy

DailyHisaab is designed around local expense storage using SQLite.

- Expense records are stored locally when using the offline database implementation.
- The application should not be assumed to provide cloud synchronization or cross-device backup unless those features are explicitly implemented.
- Uninstalling the application or clearing its data may remove locally stored records.
- Do not use real financial information when testing an untrusted build.
- Review the Android manifest, application code, and dependencies to verify actual permissions and data handling before release.

This README describes the intended local-storage design. The current source code determines the application's actual behavior.

---

## Testing

Test the application before distributing it.

Recommended test cases include:

- Adding a valid expense.
- Rejecting empty or invalid expense amounts.
- Handling zero and negative amounts correctly.
- Selecting an expense category.
- Editing an existing expense.
- Deleting an expense.
- Verifying that saved expenses remain after restarting the app.
- Displaying correct expense totals.
- Handling empty transaction history.
- Checking layout behavior on different screen sizes.
- Verifying database behavior after application updates.

---

## Roadmap

Potential future improvements include:

- Monthly and category-based budgets.
- Advanced transaction search and filtering.
- Improved analytics and spending charts.
- Recurring expense management.
- CSV export and local backup/restore.
- Dark mode and additional accessibility improvements.
- Enhanced dashboard customization.

Roadmap items are proposals and should not be considered implemented until they are available in the application.

---

## Screenshots

Add screenshots of the actual running application to a `screenshots/` folder and update the image paths below.

```markdown
![DailyHisaab Dashboard](screenshots/dashboard.png)
![Add Expense Screen](screenshots/add-expense.png)
![Transaction History](screenshots/history.png)
![Statistics Screen](screenshots/statistics.png)
```

---

## Developer

**Abhay Sachin Donde**

Developer and creator of DailyHisaab.

- GitHub: https://github.com/Abhay2030

For questions, bug reports, or suggestions, open an issue in this repository.

---

## Contributing

Contributions, bug reports, and suggestions are welcome.

1. Fork the repository.
2. Create a feature branch.
3. Make your changes.
4. Test the application.
5. Submit a pull request describing the changes.

Please avoid committing secrets, signing keys, personal financial data, or machine-specific configuration files.

---

## License and Copyright

Copyright © 2026 Abhay Sachin Donde. All Rights Reserved.

Unless a separate license file explicitly states otherwise, this copyright notice does not grant permission to redistribute, modify, or commercially use the source code.

Before making the repository public, add a clear license if you intend to permit others to reuse the code. Do not label the project as open source without choosing an appropriate license.

---

**DailyHisaab — Simple expense tracking for everyday life.**
