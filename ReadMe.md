## Pulse Desk - Comment to ticket system.

# Table of contents: 
- [Main idea](Main idea)
- [Video demonstration](Video demontration)
- [Project structure](Project steucture)

## Main idea: 
**PulseDesk** is an intelligent backend system that **automatically analyzes 
user feedback using AI** and creates support tickets only when necessary. It **helps support teams focus on real issues** by filtering out positive feedback and automatically categorizing problems.

# Problems: 
1. **Important issues can be missed.**
2. People have to read all feedbacks in order to find bad ones.
3. No standardized categorization.

# Solutions:
1. **Automatically detect** if a comment requires support.
2. **Create tickets** only for actual problems.
3. **Categorize issues** (Bug, Feature, Billing....).
4. **Assign priority levels** (High, Medium, Low).
5. **Filter out** compliments and positive feedback.

# Features: 
- **AI-Powered Analysis** - Intelligent comment classification.
- **Automatic Ticket Creation** - Only for issues requiring support.
- **Smart Categorization** - 5 categories (Bug, Feature, Billing, Account, Other).
- **Priority Assignment** - Automatic priority levels.
- **RESTful API** - Easy integration with any frontend.
- **H2 Database** - In-memory storage for quick development.
- **Web UI** - Simple interface for testing.

## Video demonstration:

## Project structure:
````
├── Controller/
│   ├── CommentController.java      # for REST endpoints comments
│   └── TicketController.java       # for REST endpoints tickets
├── Logic/
│   ├── CommentService.java         # comment's logic 
│   ├── TicketService.java          # ticket's logic
│   └── HuggingFaceService.java     # AI integration
├── model/
│   ├── Comment.java                # comment's model
│   ├── Ticket.java                 # ticket's model
│   └── AIAnalysisResult.java       # AI answers DTO
├── repository/
│   ├── CommentRepository.java      # Data storage
│   └── TicketRepository.java
└── PulseDeskApplication.java       # Main class 
````

## How it works (screenshots)

1. Firstly, the program is being ran on preferred IDE.
Before everything http://localhost:8080/tickets and http://localhost:8080/comments
are empty:
   ![FirstStage](src/main/resources/Pictures/CommentsFirstStage.png)

   ![FirstStage](src/main/resources/Pictures/TicketsFirstStage.png)

2. Example: a good comment is being left. 
   
   For example, a good comment is being left by a happy user:
   ![GoodComment](src/main/resources/Pictures/GoodCommentIsBeingLeft.png)
   
   The comment is being saved:
   ![GoodComment](src/main/resources/Pictures/GoodCommentIsVisible.png)
   
   However! The ticket is not being created! Because AI analysed it 
   and decided the user does not need help:
   ![GoodComment](src/main/resources/Pictures/TicketIsNotBeingCreated.png)

3. Example: a bad comment is being left.
   For example, a bad comment is being left by a dissapointed user:
   ![BadComment](src/main/resources/Pictures/BadCommentIsAdded.png)

   The comment is being saved:
   ![BadComment](src/main/resources/Pictures/BadCommentIsVisibleInComments.png)

   And most importantly a ticket is being created after AI analyses it, so,
   staff can recognise the issue.
   ![BadComment](src/main/resources/Pictures/TicketIsBeingCreated.png)

## Using a website: 

# Leaving a bad comment: 
![BadComment](src/main/resources/Pictures/BadCommentOnWebsite.png)

# Few moments later a ticket is created: 
![TicketIsCreated](src/main/resources/Pictures/TicketIsCreated.png)

# Good Comment (when it is sent a ticket is NOT created):
![TicketIsCreated](src/main/resources/Pictures/GoodComment.png)


## Getting started
Before you begin, ensure you have:

- Java 17 or higher
- Maven 3.6+ 
- Git 
1. **Clone the repository**
``
git clone https://github.com/brumeras/Pulse-Desk
cd HuggingAPI
``
2. **Verify Java & Maven**
````
1. Check Java version
java -version

# Should show: openjdk version "17.x.x"

2. Check Maven version
mvn -version

# Should show: Apache Maven 3.6.x or higher
````
3. **Build the Project**
``mvn clean install
``
4. **Run the Application**
``
mvn spring-boot:run
``
5. **Verify It's Running**
Open in browser:
``
http://localhost:8080/tickets
``
## AI categories and priorities

````
Categories:
BUG - technical issues
FEATURE - request on new features
BILLING - issues with payment 
ACCOUNT - issues with an account
OTHER - other problems

Priorities:
HIGH - critical and urgent problem
MEDIUM - medium priority
LOW - low priority
````

