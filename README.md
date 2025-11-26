## Table of Contents
- [Build Challenge - Java](#build-challenge--java)
- [Deliverables (Checklist)](#deliverables-checklist)
- [Tech & Requirements](#tech--requirements)
- [Assignment 1 (Java)](#assignment-1-java)
  - [Build & Test (Windows)](#build--test-windows)
  - [Output screenshot (queue & wait)](#output-screenshot-outputs-with-queue-and-wait-modes)
  - [Test Success screenshot](#test-success-screenshot-unit-tests-for-all-running-types)
- [Assignment 2 (Java)](#assignment-2-java)
  - [Build & Test (Windows)](#build--test-windows-1)
  - [Output screenshot (prints results to console)](#output-screenshot-prints-results-to-console)
  - [Test Success screenshot (all analysis methods)](#test-success-screenshot-unit-tests-for-all-analysis-methods)


# Build Challenge - Java

**Public repo:** https://github.com/HarshDThoriya/build-challenge

This repository contains two assignments.

## Deliverables (Checklist)
- ✅ Public GitHub repository URL
- ✅ Complete source code (A1 + A2 in this repo)
- ✅ Unit tests for all analysis methods
- ✅ README with setup instructions and sample output (this file)
- ✅ Results of all analyses printed to console

## Tech & Requirements

- **JDK 21+**
- **Apache Maven** (`mvn`)
- **JUnit 5**
- **Apache Commons CSV** (declared in A2 `pom.xml`)

> On Windows, ensure `JAVA_HOME` points to your JDK and `%JAVA_HOME%\bin` is in `PATH`.

---

## Assignment 1 (Java) 
  Two implementations of the classic producer–consumer pattern:
  - `BlockingQueue` (ArrayBlockingQueue)
  - Custom `BoundedBuffer` using `wait/notify`
  - Clean termination via **poison pill**
  - Unit tests: stress, blocking semantics, wait

  **Packages:** `com.harsh.pc`  
  **Entry point:** `com.harsh.pc.Main`  
  **Modes:**  
  - `--mode=queue` → ArrayBlockingQueue  
  - `--mode=wait`  → Custom BoundedBuffer (wait/notify)

  ### Build & Test (Windows)
  ```bash
  cd producer-consumer-java

  mvn test

  # Build the JAR
  mvn -DskipTests package

  # BlockingQueue implementation
  java -cp target/producer-consumer-java-1.0.0.jar com.harsh.pc.Main --mode=queue

  # wait/notify implementation
  java -cp target/producer-consumer-java-1.0.0.jar com.harsh.pc.Main --mode=wait

  ```

  ### Output screenshot (outputs with queue and wait modes)
  ![Screenshot of the outputs with queue and wait modes](https://github.com/HarshDThoriya/build-challenge/blob/main/producer-consumer-java/assets/Screenshot%202025-11-25%20185151.png)

  ### Test Success screenshot (unit tests for all running types)
  ![Screenshot of the successful running of tests](https://github.com/HarshDThoriya/build-challenge/blob/main/producer-consumer-java/assets/Screenshot%202025-11-25%20182101.png)


## Assignment 2 (Java)
  Reads a “superstore / Online Retail II” CSV and performs analytics:
  - Total revenue
  - Top-10 Countries by revenue
  - Top-10 Products by revenue
  - Revenue by month
  - Average Order Value
  - Top Customer by Revenue 

  Prints a summary to console (Screenshots attached below).

  Unit tests: total revenue, revenue by country, products by revenue, revenue by month, average order value, top customers by revenue

  **Packages:** `com.salesanalysis`  
  **Entry point:** `com.salesanalysis.SalesAnalysisApplication`  
  **Dataset:** Included in the repository. Located at 
  ```bash
  sales-analysis-java/src/main/resources/data/online_retail_II.csv
  ```
  **Dataset for tests:** Included in the repository. Located at 
  ```bash
  sales-analysis-java/src/test/resources/sample_sales.csv
  ```

  ### Build & Test (Windows)
  ```bash
  cd sales-analysis-java

  mvn test

  # Build the JAR
  mvn -DskipTests package

  # Running the driver code
  java -cp target/classes com.salesanalysis.SalesAnalysisApplication

  ```

  ### Output screenshot (prints results to console)
  ![Screenshot of the output which prints results to console](https://github.com/HarshDThoriya/build-challenge/blob/main/sales-analysis-java/assets/Screenshot%202025-11-25%20181939.png)

  ### Test Success screenshot (unit tests for all analysis methods)
  ![Screenshot of the successful running of unit tests for all analysis methods](https://github.com/HarshDThoriya/build-challenge/blob/main/sales-analysis-java/assets/Screenshot%202025-11-25%20182001.png)

---



