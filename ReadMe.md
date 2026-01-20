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
