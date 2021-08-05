#  assignment for technical interview candidate: build a simple company search service

User story: as a customer, I would like to search company by querying company name, so that I can lookup company name and contact information.

Technical Requirement:

System Level:
* design an event driven architecture
* sync data in CSV file from AWS S3 and persist in database
* must be based on the event changes on S3 when a new file is uploaded asynchouously either by manual or automation. 
* make decision on the database technology with justification, such as Relational vs NoSQL
* create a search micro-service with secured REST API to query company data by company name.
* create web UI using React with a search bar and display company information such as CREDITOR NAME, ADDRESS 1 ADDRESS 2, CITY, STATE, POSTAL, ZIP

Component Level:
* must demonstrate SOLID design principles, especially the Open-Closed Principle, designing for extensibility with Elastic Search implementation for advance filter functionality in future dev iternation
* show engineering best practices, such as Design Patterns, Test Driven Development, layer of abstraction, coding standard, etc.
* implement Sorting Company Name In Aphabetical Order interface based on the most efficient data structure and algorithm strategy.

Technology stack:
* Language choice: can be either Java/Kotlin or JavaScript/TypeScript or Python.
* all services must be running on AWS Cloud free-tier
* set up Elastic Search Cluster is optional (s bonus), however your design must able to handle different strategies for search
* set up Devops and Kubernates Cluster is optional (as bonus), you may select any container orchestration solution.

Documentation:
* provide online API documentation for all REST API
* document all technical solution designs in github, including high level architecture, UML diagram, database schema and supporting materials.

Test Data: 
* CSV file is attached for uploading to S3 manually

Deadline: 
* to be completed within 4 days. 
