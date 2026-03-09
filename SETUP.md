# Setup Instructions

## Project Structure

This is a **Java-only** project with no build system (Maven/Gradle) included. You can run it directly in an IDE or from the command line.

## IDE Setup (Recommended)

### IntelliJ IDEA
1. Open IntelliJ
2. **File → Open** → Select the `stock-simulator` folder
3. Right-click `src/` → **Mark Directory as → Sources Root**
4. Right-click `test/` → **Mark Directory as → Test Sources Root**
5. Run `Main.java` (Ctrl+Shift+F10)

### Eclipse
1. **File → New → Java Project** → Next
2. **Project name:** stock-simulator
3. **Uncheck "Use default location"** → Browse to project folder
4. **Finish**
5. Run `Main.java` as Java Application

### VS Code
1. Install Extension Pack for Java
2. Open folder in VS Code
3. **Run → Start Debugging** (F5) → select Java

## Command Line Setup

### Compile
```bash
cd stock-simulator
javac -d bin src/project/**/*.java
```

### Run
```bash
java -cp bin project.Main
```

## CSV Data Setup

Create a `CSVs/` folder in the project root with CSV files:

```
stock-simulator/
├── src/
├── CSVs/
│   ├── AAPL Stock.csv
│   ├── GOOGL Stock.csv
│   └── sap500indexbenchmark.csv
```

### CSV Format

**Stock/ETF files** (e.g., `AAPL Stock.csv`):
```
Date,Open,High,Low,Close,Volume
2020-01-02,75.04,75.15,74.37,74.72,135480400
2020-01-03,74.29,76.13,74.13,75.80,146322800
...
```

**S&P 500 Benchmark** (`sap500indexbenchmark.csv`):
```
Date,Open,High,Low,Close,Volume
2020-01-02,3257.85,3297.59,3255.49,3283.24,2478310000
...
```

## Testing

### Run All Tests (with JUnit 4)
```bash
javac -cp .:junit-4.13.jar -d bin test/project/*.java src/project/**/*.java
java -cp bin:junit-4.13.jar org.junit.runner.JUnitCore project.BestWorstPerformersTest
```

Note: Download `junit-4.13.jar` from [junit.org](https://junit.org/junit4/)

## Troubleshooting

**"Cannot find symbol"** → Make sure all `.java` files are in the correct package directories matching the folder structure

**"File not found: CSVs"** → Create the CSVs folder in project root and add CSV files with proper formatting

**"No main manifest attribute"** → This is normal for running with command line; use `java -cp bin project.Main`
