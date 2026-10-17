<img width="1331" height="1536" alt="image" src="https://github.com/user-attachments/assets/cbb33b75-044e-4368-b1ee-fa94b4dfadf3" />



DailyHisaab — Offline Android Expense Tracker & Personal Finance App
DailyHisaab — Roz Ka Hisaab, Ek Jagah is an Android expense-tracking application designed to help people record everyday spending, organize expenses by category, review their transaction history, and understand their personal finances in one place.
Built with Java, XML, and SQLite in Android Studio, DailyHisaab focuses on straightforward money management, local data storage, and an interface intended for everyday use in India. It is a practical project for anyone interested in Android app development, expense tracking, budgeting, and offline-first mobile applications.
Project: DailyHisaab Android App
Developer: Abhay Sachin Donde
Repository: github.com/Abhay2030/DailyHisaab

<p align="left">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white" alt="Platform: Android" />
  <img src="https://img.shields.io/badge/Language-Java-ED8B00?logo=openjdk&logoColor=white" alt="Language: Java" />
  <img src="https://img.shields.io/badge/UI-XML-8A2BE2" alt="UI: XML layouts" />
  <img src="https://img.shields.io/badge/Database-SQLite-003B57?logo=sqlite&logoColor=white" alt="Database: SQLite" />
  <img src="https://img.shields.io/badge/Development-Android%20Studio-3DDC84?logo=androidstudio&logoColor=white" alt="Developed with Android Studio" />
</p>

Table of Contents
- Why DailyHisaab?
- Features
- Designed for Everyday Money Management in India
- Screenshots
- Technology Stack
- How It Works
- Getting Started
- Build the App
- Data Storage and Privacy
- Project Goals and Roadmap
- Known Scope and Limitations
- How to Report an Issue
- Author
- License
Why DailyHisaab?
Small daily purchases can be difficult to remember at the end of a week or month. DailyHisaab aims to make personal expense tracking easier by keeping everyday financial records organized in an Android application.
The project is built around a simple idea: record your expenses, review your spending, and make more informed decisions about your money. Instead of presenting money management as a complicated financial dashboard, the app focuses on the basic activities people need regularly—recording expenses, organizing transactions, and reviewing spending patterns.
DailyHisaab is intended for users looking for an Android expense tracker, daily expense manager, personal finance app, or simple budget and spending tracker, with an India-oriented experience.
Features
The current repository describes the following core areas. Availability and behaviour should always be verified against the latest build before release.
Daily Expense Tracking
- Record everyday spending in one place.
- Organize financial records for easier review.
- Use transaction history to look back at recorded expenses.
Expense Categories
- Organize transactions into categories.
- Make expense records easier to search, review, and understand.
- Use category-based organization to support clearer spending summaries.
Spending Overview and Insights
- Review recorded spending.
- Follow monthly spending and budget information where supported by the current build.
- Use available spending summaries to better understand where money is going.
Monthly Budget Tracking
- Review budget information alongside recorded expenses.
- Use budget tracking to help compare spending against a planned amount.
- Check the latest app build for the budget options currently available.
CSV Export
The repository lists CSV export for financial records. A CSV file can be useful for reviewing transactions in spreadsheet software or keeping a separate copy of exported records. Export behaviour should be tested on the current build before relying on it for important records.
Offline-Oriented Local Storage
DailyHisaab uses SQLite for local data storage and is designed around an offline-first experience. This makes local transaction management a central part of the project rather than requiring a cloud account as the core workflow.
Android Interface
- Android application developed using Java and XML layouts.
- UI and financial information designed for a mobile screen.
- Emerald and teal visual direction described by the project.
- Light and dark appearance support as described by the current repository README.
Designed for Everyday Money Management in India
DailyHisaab is intended for day-to-day money management in an Indian context, where expenses may include groceries, transport, mobile recharge, food, rent, bills, shopping, and other regular purchases.
A personal expense tracker can help users:
- Keep everyday expenses from being forgotten.
- Review spending over a selected period.
- Organize purchases into meaningful categories.
- Compare spending with a personal budget.
- Keep a clearer record of day-to-day money movement.
The app is a tracking and record-keeping tool. It is not a bank, payment processor, investment platform, tax adviser, or financial institution.
Screenshots
Screenshots make it easier for users and contributors to understand an Android app before installing or building it. Add genuine screenshots from the latest running build to this section when they are available.
Suggested screenshot set:
Screen	What to show
Home dashboard	Current summary and recent activity, using test data only
Add transaction	Transaction-entry form and category selection
Transaction history	Recorded transactions and available filters
Budgets	Budget summary and category details, if available
Insights	Actual spending charts or summaries, if available
Settings	Current theme and app preferences, if available


Recommended location: docs/screenshots/. Use real app screenshots, avoid publishing private financial data, and remove rows that do not exist in the current build. Once images have been added, embed them here using relative links such as ![DailyHisaab dashboard](docs/screenshots/dashboard.png).
Technology Stack
Technology	Purpose
Java	Android application logic
XML layouts	Android user-interface layouts
SQLite	Local relational data storage
Android Studio	Development environment
Gradle	Project build system


The repository contains Kotlin-based Gradle build scripts (.gradle.kts); this does not mean the Android application source itself is Kotlin. The app's language and UI implementation should be checked against the branch being built.
How It Works
At a high level, DailyHisaab combines an Android user interface with application logic and local SQLite storage.
1. The user enters or reviews financial records through the Android interface.
2. The app's logic processes the entered information.
3. Supported records are stored locally using SQLite.
4. Available transaction, category, budget, or summary screens present the saved information.
5. Supported export functionality can create a file for review outside the app.
The precise transaction fields, calculation rules, and export formats depend on the implementation in the selected branch. Financial totals should be tested whenever calculation logic or the database schema changes.
Getting Started
Prerequisites
- A computer that can run Android Studio.
- Git, or another way to download the repository.
- An Android SDK compatible with the project's Gradle and Android Gradle Plugin configuration.
- An Android emulator or a physical Android device for running the app.
- Internet access may be required the first time Gradle downloads build dependencies. The app's offline-oriented runtime design is separate from the build process.
1. Clone the Repository
git clone https://github.com/Abhay2030/DailyHisaab.git
cd DailyHisaab
2. Open the Project in Android Studio
1. Launch Android Studio.
2. Select Open.
3. Choose the cloned DailyHisaab directory.
4. Allow Android Studio to sync the Gradle project.
5. Resolve any SDK prompts using versions compatible with the repository's build configuration.
3. Run the App
1. Start an Android emulator or connect an Android device with USB debugging enabled.
2. Select the app run configuration in Android Studio.
3. Click Run.
4. Review the build output and resolve any configuration errors if necessary.
Build from the Command Line
On macOS or Linux:
./gradlew assembleDebug
On Windows Command Prompt:
gradlew.bat assembleDebug
If the build succeeds, Gradle typically places the debug APK under the app module's build/outputs/apk/debug/ directory. The exact output path depends on the module and build configuration.
Data Storage and Privacy
DailyHisaab uses a local SQLite database as part of its offline-oriented design. Local storage can reduce dependency on a server for everyday record keeping, but it does not automatically guarantee encryption or protection from every form of device access.
For responsible use:
- Keep your Android device protected with a screen lock.
- Export or back up important information using functionality actually available in the app.
- Avoid using real personal financial records in screenshots, bug reports, or demonstrations.
- Review the current source code and Android permissions before making security or privacy claims.
This README does not claim automatic access to Google Pay, PhonePe, Paytm, bank accounts, SMS messages, or UPI history. Recording a payment method such as UPI is not the same as importing payment transactions automatically.
Project Goals and Roadmap
The following are possible improvement areas, not a statement that every feature is already implemented. Prioritize them based on project scope and verify each item before marking it complete.
- [ ] Add or refine genuine screenshots and app demonstrations.
- [ ] Improve transaction-entry speed and input validation.
- [ ] Review accessibility, text scaling, and touch targets.
- [ ] Expand automated tests for monetary calculations and date ranges.
- [ ] Verify database migration and existing-record preservation.
- [ ] Improve search, filtering, and category management where needed.
- [ ] Test CSV export with empty data, special characters, and large transaction histories.
- [ ] Review dark and light theme consistency.
- [ ] Add Hindi and Marathi localization if supported by the product plan.
- [ ] Evaluate additional reporting and backup options without compromising user control over financial data.
Known Scope and Limitations
DailyHisaab is a personal finance and expense-tracking project. Unless the relevant implementation is present and verified, do not assume that it includes:
- Automatic UPI transaction imports from Google Pay, PhonePe, or Paytm.
- Direct bank-account synchronization.
- Cloud backup or multi-device synchronization.
- Receipt OCR or automatic receipt recognition.
- AI-powered categorization or AI financial advice.
- Encryption beyond protections explicitly implemented in the source code.
- A published Google Play Store release or production signing configuration.
These features may be considered separately in the future. Their inclusion should depend on technical feasibility, privacy requirements, platform policies, and testing.
How to Report an Issue
Found a bug or have a practical feature suggestion?
1. Search the existing GitHub Issues to check whether it has already been reported.
2. Open a new issue if needed.
3. Include your Android version, device or emulator details, steps to reproduce the problem, expected behaviour, actual behaviour, and relevant non-sensitive logs.
4. Never attach real financial statements, account numbers, UPI IDs, phone numbers, passwords, or other personal data to an issue.
When reporting a problem, describe the behaviour you observed rather than assuming its cause.
Author
Abhay Sachin Donde
Developer of DailyHisaab
- GitHub: @Abhay2030
- Repository: DailyHisaab
License
DailyHisaab is marked as proprietary software in the repository. Please read the LICENSE file before copying, modifying, redistributing, or using this project in another application. Do not assume that the code is available under an open-source license unless the repository's license terms explicitly say so.
