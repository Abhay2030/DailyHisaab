<img width="1331" height="1536" alt="image" src="https://github.com/user-attachments/assets/cbb33b75-044e-4368-b1ee-fa94b4dfadf3" />


DailyHisaab — Roz Ka Hisaab, Ek Jagah
A simple Android expense tracker for everyday money management.
Track daily expenses, organize transactions, review spending, and manage your personal finances from one place.
<p>
  <a href="https://github.com/Abhay2030/DailyHisaab">
    <img src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white" alt="Platform: Android">
  </a>
  <img src="https://img.shields.io/badge/Language-Java-ED8B00?logo=openjdk&logoColor=white" alt="Language: Java">
  <img src="https://img.shields.io/badge/UI-XML-8A2BE2" alt="User interface: XML">
  <img src="https://img.shields.io/badge/Database-SQLite-003B57?logo=sqlite&logoColor=white" alt="Database: SQLite">
  <img src="https://img.shields.io/badge/IDE-Android%20Studio-3DDC84?logo=androidstudio&logoColor=white" alt="Development environment: Android Studio">
</p>

Repository: Abhay2030/DailyHisaab
Developer: Abhay Sachin Donde
</div>

Table of Contents
- About DailyHisaab
- Why DailyHisaab?
- Features
- Designed for Everyday Expenses in India
- Screenshots
- Technology Stack
- Application Overview
- Getting Started
- Build Instructions
- Data Storage and Privacy
- Roadmap
- Scope and Limitations
- Reporting Bugs and Requesting Features
- About the Developer
- License
About DailyHisaab
DailyHisaab is an Android expense tracker and personal finance management application designed to help users record everyday spending, organize transactions, and review their expenses in one place.
Built with Java, XML layouts, SQLite, and Android Studio, the project focuses on straightforward money management and local data storage. Its goal is to make daily expense tracking approachable for people who want a clearer record of where their money goes.
DailyHisaab is designed around a simple principle:
Record your expenses. Understand your spending. Make more informed decisions.

Whether you are tracking groceries, transport, rent, food, mobile recharges, bills, or other everyday purchases, a consistent record can make your personal finances easier to review.
Project: DailyHisaab — Android Expense Tracker
Repository: github.com/Abhay2030/DailyHisaab
Developer: Abhay Sachin Donde

Why DailyHisaab?
Small purchases can be easy to forget. Over time, those expenses can make it difficult to understand spending habits or compare actual spending with a budget.
DailyHisaab aims to make this process easier by bringing everyday financial records into one Android application.
The project emphasizes:
- Simple expense recording — keep everyday transactions organized.
- Category-based tracking — make spending easier to review.
- Clear financial overviews — understand recorded income and expenses where supported by the current build.
- Local-first data handling — use SQLite for local storage.
- Practical Android development — demonstrate an Android application built with Java, XML, and SQLite.
DailyHisaab is a tracking and record-keeping tool. It is not a bank, payment processor, investment platform, or financial institution.
Features
The following sections describe the app's intended core areas. The availability and behaviour of individual features depend on the current source code and build. Verify each feature in the version you use before relying on it for important financial records.
Expense Tracking
- Record day-to-day expenses.
- Keep transaction information organized.
- Review previously recorded spending.
- Support a clearer overview of everyday money movement.
Transaction Categories
- Organize transactions by category.
- Make individual records easier to review.
- Support category-based spending summaries where available.
Examples of everyday expense categories include food, groceries, transport, fuel, rent, bills, shopping, education, health, and other personal expenses.
Spending Overview
- Review recorded expenses.
- View financial summaries where supported by the current build.
- Understand spending over a selected period when period-based reporting is available.
Budget Tracking
- Review budget information alongside recorded spending where supported.
- Compare expenses with a planned budget.
- Identify areas where additional budget tracking may be useful.
CSV Export
The project documentation identifies CSV export as an intended or available capability. Confirm that export works in the current build before using it for important records. A valid CSV file can be opened in compatible spreadsheet applications for further review.
Local SQLite Storage
DailyHisaab uses SQLite for local data storage. The application is designed around an offline-oriented workflow, with local transaction management as a central part of the project.
Local storage does not automatically mean that data is encrypted or protected against every form of device access. See Data Storage and Privacy.
Android User Interface
- Android application developed with Java.
- XML-based layouts.
- Android Studio development workflow.
- SQLite local database.
- An emerald and green visual direction described by the project.
Designed for Everyday Expenses in India
DailyHisaab is intended for everyday money management in India, where regular spending may include:
- Groceries and kirana purchases
- Food, snacks, and dining
- Public transport, travel, and fuel
- Rent and household bills
- Mobile recharge and utilities
- Shopping and personal care
- Education and healthcare
- Other day-to-day purchases
A daily expense manager can help users keep better records, review spending patterns, and compare expenses with personal goals. DailyHisaab is intended to support that record-keeping process without presenting itself as a banking or payment application.
Screenshots
Add genuine screenshots from the latest running app build to help visitors understand the interface before building or trying the project.
Screen	Suggested screenshot content
Home dashboard	Current financial overview and recent activity, using test data
Add transaction	Transaction-entry form and category selection
Transaction history	Saved transactions and available filters
Budgets	Budget overview and category details, if implemented
Insights	Actual charts or spending summaries, if implemented
Settings	Available preferences and theme options, if implemented


Suggested folder: docs/screenshots/
Once screenshots are added, embed them using relative paths, for example:
![DailyHisaab dashboard](docs/screenshots/dashboard.png)
Only include screenshots of screens that exist in the current build. Use test data and remove personal or financial information before publishing screenshots.
Technology Stack
Technology	Purpose
Java	Android application logic
XML layouts	User-interface layout definitions
SQLite	Local relational data storage
Android Studio	Android development environment
Gradle	Build automation and dependency management


The project may use Kotlin-based Gradle configuration files such as .gradle.kts even when the application source code is written in Java. The programming language used by the app itself should be verified against the branch being built.
Application Overview
At a high level, DailyHisaab connects the Android interface, application logic, and local database.
1. User interaction: The user enters or reviews financial information through the Android interface.
2. Application logic: The app validates and processes supported inputs.
3. Local storage: Supported records are saved in SQLite.
4. Review: Available screens display saved transactions and summaries.
5. Export: If implemented and verified, supported export functionality generates files for use outside the app.
The precise data fields, calculations, and available workflows depend on the current implementation. Financial totals should be tested after changes to calculation logic, transaction handling, or the database schema.
Getting Started
Prerequisites
Before building DailyHisaab, make sure you have:
- Android Studio
- Git or another way to download the repository
- An Android SDK compatible with the project's Gradle configuration
- An Android emulator or physical Android device
- Internet access for the initial download of required build dependencies, if they are not already cached
The initial build may require an internet connection even if the app's intended runtime workflow is offline-oriented.
1. Clone the Repository
Open a terminal and run:
git clone https://github.com/Abhay2030/DailyHisaab.git
cd DailyHisaab
2. Open the Project
1. Launch Android Studio.
2. Select Open.
3. Choose the cloned DailyHisaab project directory.
4. Allow Gradle synchronization to finish.
5. Install or select SDK components compatible with the project's configuration.
6. Resolve any build errors reported by Android Studio before running the app.
3. Run on an Android Device or Emulator
1. Start an Android emulator or connect a physical Android device.
2. If using a physical device, enable USB debugging where appropriate.
3. Select the app's run configuration.
4. Click Run in Android Studio.
5. Confirm that the application launches and test its available workflows.
Build Instructions
You can build a debug APK from the command line using the Gradle wrapper included in the repository.
Windows
gradlew.bat assembleDebug
macOS or Linux
./gradlew assembleDebug
If the build succeeds, the debug APK is typically generated under:
app/build/outputs/apk/debug/
The exact output location may differ depending on the project's module structure and build configuration.
If the build fails, review the Gradle output and confirm that the installed JDK, Android SDK, Gradle version, and Android Gradle Plugin are compatible with the project.
Data Storage and Privacy
DailyHisaab uses SQLite as part of its local-storage design. Local storage can reduce the need for a server in everyday record keeping, but it should not be treated as a guarantee of encryption or complete protection from device access.
For safer use:
- Keep your Android device protected with a screen lock.
- Use only backup and export features that are actually implemented.
- Avoid including real financial records in screenshots, demonstrations, or bug reports.
- Review the source code and Android permissions before making security or privacy claims.
- Do not share exported financial files publicly unless they have been checked for personal information.
This README does not claim automatic access to Google Pay, PhonePe, Paytm, bank accounts, SMS messages, or UPI transaction history. Recording UPI as a payment method is not the same as importing transactions automatically.
Roadmap
The following items are potential improvement areas, not claims that the features are already implemented. Update this checklist as work is completed and verified.
- [ ] Add screenshots from the latest app build.
- [ ] Review and improve the transaction-entry workflow.
- [ ] Strengthen input validation and error handling.
- [ ] Test financial calculations and date-range boundaries.
- [ ] Verify database migration and existing-record preservation.
- [ ] Review search, filters, and category management.
- [ ] Test CSV export with empty datasets, special characters, and large histories.
- [ ] Check visual consistency across light and dark themes.
- [ ] Improve accessibility, text scaling, and touch targets.
- [ ] Evaluate Hindi and Marathi localization.
- [ ] Assess reporting and backup options while preserving user control over financial data.
Scope and Limitations
DailyHisaab is a personal finance and expense-tracking Android project. Unless the relevant implementation is present and verified, do not assume that the app includes:
- Automatic UPI imports from Google Pay, PhonePe, or Paytm
- Direct bank-account synchronization
- Cloud backup or multi-device synchronization
- Receipt scanning or OCR-based data extraction
- AI-powered categorization or financial advice
- Encryption or biometric protection beyond what is explicitly implemented
- A published Google Play Store release or production signing configuration
These capabilities can be evaluated separately based on technical feasibility, privacy requirements, platform policies, and testing.
Reporting Bugs and Requesting Features
Found a bug or have an improvement idea?
1. Search existing GitHub Issues to see whether it has already been reported.
2. Open a new issue if needed.
3. Describe the steps to reproduce the issue.
4. Include your Android version, device or emulator details, expected behaviour, actual behaviour, and relevant non-sensitive logs.
5. For feature requests, explain the problem the feature would solve and how you expect it to work.
Do not include passwords, account numbers, UPI IDs, phone numbers, bank statements, real financial records, or other sensitive personal information in public issues.
About the Developer
Abhay Sachin Donde
Developer of DailyHisaab
- GitHub profile: @Abhay2030
- Project repository: DailyHisaab
License
Review the repository's LICENSE file before copying, modifying, redistributing, or using this project in another application.
If the repository does not contain a LICENSE file, do not assume that the code is open source or available for unrestricted reuse. Add a license only after deciding which terms you want the project to use.
<div align="center">

DailyHisaab — Roz Ka Hisaab, Ek Jagah
A clearer way to keep track of everyday expenses.
</div>
