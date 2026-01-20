## Pulse Desk - Comment to ticket system.

## Main idea: 
PulseDesk is a backend system, which analyses users feedback (comments) by integrated AI
(Hugging Face) and creates a ticket if comment was left in order for product to improve. 

## Video demonstration:

## Project structure:
````
├── Controller/
│   ├── CommentController.java      # for REST endpoints comments
│   └── TicketController.java       # for REST endpoints tickets
├── Service/
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

